package com.yrek.ifstd.zcode;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;

import com.yrek.ifstd.glk.GlkByteArray;
import com.yrek.ifstd.glk.GlkDispatch;

class State implements Serializable {
    private static final long serialVersionUID = 0L;

    public static int VERSION = 0x0;
    public static int FLAGS1 = 0x1;
    public static int RELEASE_NUMBER = 0x2;
    public static int HIGH_MEMORY = 0x4;
    public static int INITIAL_PC = 0x6;
    public static int DICTIONARY = 0x8;
    public static int OBJECT_TABLE = 0xa;
    public static int GLOBAL_VAR_TABLE = 0xc;
    public static int STATIC_MEMORY = 0xe;
    public static int FLAGS2 = 0x10;
    public static int SERIAL_NUMBER = 0x12;
    public static int ABBREVIATION_TABLE = 0x18;
    public static int LENGTH = 0x1a;
    public static int CHECKSUM = 0x1c;
    public static int INTERPRETER_NUMBER = 0x1e;
    public static int INTERPRETER_VERSION = 0x1f;
    public static int SCREEN_HEIGHT_CHARS = 0x20;
    public static int SCREEN_WIDTH_CHARS = 0x21;
    public static int SCREEN_WIDTH = 0x22;
    public static int SCREEN_HEIGHT = 0x24;
    public static int FONT_WIDTH = 0x26;
    public static int FONT_HEIGHT = 0x27;
    public static int ROUTINE_OFFSET = 0x28;
    public static int PRINT_OFFSET = 0x2a;
    public static int BACKGROUND_COLOR = 0x2c;
    public static int FOREGROUND_COLOR = 0x2d;
    public static int TERMINATING_CHARACTERS = 0x2e;
    public static int PIXEL_WIDTH = 0x30;
    public static int REVISION_NUMBER = 0x32;
    public static int ALPHABET_TABLE = 0x34;
    public static int EXTRA_HEADERS = 0x36;

    public static int EXTRA_HEADERS_SIZE = 0;
    public static int EXTRA_HEADERS_MOUSE_X = 1;
    public static int EXTRA_HEADERS_MOUSE_Y = 2;
    public static int EXTRA_HEADERS_UNICODE_TABLE = 3;
    public static int EXTRA_HEADERS_FLAGS3 = 4;
    public static int EXTRA_HEADERS_FOREGROUND_COLOR = 5;
    public static int EXTRA_HEADERS_BACKGROUND_COLOR = 6;

    public static int INTERPRETER_NUMBER_VALUE = 1;
    public static int INTERPRETER_VERSION_VALUE = 49;
    public static int REVISION_NUMBER_VALUE = 0x101;

    byte[] ram;
    int pc;
    StackFrame frame;
    int version;
    int globalVarTable;
    private transient Dictionary dictionary;

    void load(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        version = in.read();
        if (version < 1 || version > 8) {
            throw new IllegalArgumentException("Unrecognized Z-machine version: "+version);
        }
        out.write(version);
        byte[] buffer = new byte[8192];
        for (int len = in.read(buffer); len >= 0; len = in.read(buffer)) {
            out.write(buffer, 0, len);
        }
        ram = out.toByteArray();
        pc = read16(INITIAL_PC);
        if (version != 6) {
            frame = new StackFrame(null, 0, 0, 0, 0);
        } else {
            pc = unpack(pc, true);
            frame = new StackFrame(null, 0, 0, 0, read8(pc));
            pc++;
        }
        globalVarTable = read16(GLOBAL_VAR_TABLE);
    }

    void init(int screenWidth, int screenHeight) throws IOException {
        //... FLAGS1
        //... FLAGS2
        store8(INTERPRETER_NUMBER, INTERPRETER_NUMBER_VALUE);
        store8(INTERPRETER_VERSION, INTERPRETER_VERSION_VALUE);
        store8(SCREEN_HEIGHT_CHARS, screenHeight);
        store8(SCREEN_WIDTH_CHARS, screenWidth);
        store16(SCREEN_WIDTH, screenWidth);
        store16(SCREEN_HEIGHT, screenHeight);
        store8(FONT_WIDTH, 1);
        store8(FONT_HEIGHT, 1);
        //... BACKGROUND_COLOR
        //... FOREGROUND_COLOR
        store16(REVISION_NUMBER, REVISION_NUMBER_VALUE);
    }

    boolean loadSave(DataInput in, boolean preserveFlags) throws IOException {
        if (in.readInt() != 0x464f524d) { // FORM
            return false;
        }
        int size = in.readInt();
        byte[] readRam = null;
        StackFrame readFrame = null;
        int readPc = 0;
        boolean gotHd = false;
        boolean gotMem = false;
        boolean gotStks = false;
        byte[] hd = new byte[] {
            (byte) read8(RELEASE_NUMBER),
            (byte) read8(RELEASE_NUMBER+1),
            (byte) read8(SERIAL_NUMBER),
            (byte) read8(SERIAL_NUMBER+1),
            (byte) read8(SERIAL_NUMBER+2),
            (byte) read8(SERIAL_NUMBER+3),
            (byte) read8(SERIAL_NUMBER+4),
            (byte) read8(SERIAL_NUMBER+5),
            (byte) read8(CHECKSUM),
            (byte) read8(CHECKSUM+1),
        };
        int count = 0;
        while (count < size) {
            int id = in.readInt();
            int len = in.readInt();
            count += 8 + ((len + 1)/2)*2;
            switch (id) {
            case 0x49466864: // IFhd
                if (gotHd || len != 13) {
                    return false;
                }
                gotHd = true;
                for (byte b : hd) {
                    if (b != in.readByte()) {
                        return false;
                    }
                }
                readPc = (in.readByte()&255) << 16;
                readPc += in.readShort() & 65535;
                in.readByte();
                break;
            case 0x554d656d: // UMem
                if (gotMem) {
                    return false;
                }
                gotMem = true;
                readRam = new byte[len];
                in.readFully(readRam);
                if (len % 2 != 0) {
                    in.readByte();
                }
                break;
            case 0x434d656d: // CMem
                if (gotMem) {
                    return false;
                }
                gotMem = true;
                throw new RuntimeException("not implemented");
            case 0x53746b73: // Stks
                if (gotStks) {
                    return false;
                }
                gotStks = true;
                readFrame = StackFrame.read(in, len);
                break;
            default:
                in.skipBytes(((len+1)/2)*2);
                break;
            }
        }
        if (!gotHd || !gotMem || !gotStks) {
            return false;
        }
        ram = readRam;
        frame = readFrame;
        pc = readPc;
        return true;
    }

    void writeSave(DataOutput out) throws IOException {
        out.writeInt(0x464f524d); // FORM
        int size = 0;
        size += 8 + 14; // IFhd
        size += 8 + ((ram.length + 1)/2)*2; // UMem
        size += 8 + ((frame.totalSaveSize()+1)/2)*2; // Stks
        out.writeInt(size);
        out.writeInt(0x49466864); // IFhd
        out.writeInt(13);
        out.writeShort(read16(RELEASE_NUMBER));
        out.writeShort(read16(SERIAL_NUMBER));
        out.writeShort(read16(SERIAL_NUMBER)+2);
        out.writeShort(read16(SERIAL_NUMBER)+4);
        out.writeShort(read16(CHECKSUM));
        out.write(pc >> 16);
        out.writeShort(pc);
        out.write(0);
        out.writeInt(0x554d656d); // UMem
        out.writeInt(ram.length);
        out.write(ram, 0, ram.length);
        if ((ram.length % 2) != 0) {
            out.write(0);
        }
        frame.save(out);
    }

    void copyFrom(State state, boolean deepCopyStack, boolean preserveFlags) {
        preserveFlags = preserveFlags && ram != null;
        int preserve = 0;
        if (preserveFlags) {
            preserve = read8(FLAGS2) & 3;
        }
        if (ram == null || ram.length != state.ram.length) {
            ram = new byte[state.ram.length];
        }
        System.arraycopy(state.ram, 0, ram, 0, ram.length);
        if (preserveFlags) {
            store8(FLAGS2, (read8(FLAGS2) & 252) | preserve);
        }
        pc = state.pc;
        if (!deepCopyStack) {
            frame = state.frame;
        } else {
            frame = new StackFrame(state.frame);
        }
        globalVarTable = read16(GLOBAL_VAR_TABLE);
    }

    int read8(int location) {
        return ram[location]&255;
    }

    int read16(int location) {
        return ((ram[location]&255) << 8) | (ram[location+1]&255);
    }

    void store8(int location, int value) {
        ram[location] = (byte) value;
    }

    void store16(int location, int value) {
        ram[location] = (byte) (value >> 8);
        ram[location+1] = (byte) value;
    }

    int unpack(int address, boolean forRoutine) {
        switch (version) {
        case 1: case 2: case 3:
            return address*2;
        case 4: case 5:
            return address*4;
        case 6: case 7:
            if (forRoutine) {
                return address*4 + 8*read16(ROUTINE_OFFSET);
            } else {
                return address*4 + 8*read16(PRINT_OFFSET);
            }
        case 8:
            return address*8;
        default:
            throw new IllegalArgumentException("Unrecognized Z-machine version: " + version);
        }
    }

    int readVar(int var) {
        if (var == 0) {
            return frame.pop();
        } else if (var < 0 || var >= 256) {
            return 0;
        } else if (var < 16) {
            return frame.locals[var - 1];
        } else {
            return read16(globalVarTable + 2*(var - 16));
        }
    }

    int peekVar(int var) {
        if (var == 0) {
            return frame.peek();
        } else if (var < 0 || var >= 256) {
            return 0;
        } else if (var < 16) {
            return frame.locals[var - 1];
        } else {
            return read16(globalVarTable + 2*(var - 16));
        }
    }

    String traceVar(int var) {
        if (var == 0) {
            return String.format("(-SP)=%x",frame.peek());
        } else if (var < 0 || var >= 256) {
            return String.format("?=%x",var);
        } else if (var < 16) {
            return String.format("l%x=%x",var,frame.locals[var-1]);
        } else {
            return String.format("g%x=%x",var-16,read16(globalVarTable + 2*(var - 16)));
        }
    }

    void storeVar(int var, int val) {
        if (var == 0) {
            frame.push(val);
        } else if (var < 0 || var >= 256) {
        } else if (var < 16) {
            frame.locals[var-1] = val;
        } else {
            store16(globalVarTable + 2*(var - 16), val);
        }
    }

    void overwriteVar(int var, int val) {
        if (var == 0) {
            frame.pop();
            frame.push(val);
        } else if (var < 0 || var >= 256) {
        } else if (var < 16) {
            frame.locals[var-1] = val;
        } else {
            store16(globalVarTable + 2*(var - 16), val);
        }
    }

    boolean objAttr(int obj, int attr) {
        if (obj == 0) {
            return false;
        }
        int objTable = read16(OBJECT_TABLE);
        int objAddr;
        if (version < 4) {
            objAddr = objTable + 62 + 9*(obj - 1);
        } else {
            objAddr = objTable + 126 + 14*(obj - 1);
        }
        return (read8(objAddr + attr/8) & (128 >> (attr&7))) != 0;
    }

    void objSetAttr(int obj, int attr, boolean value) {
        if (obj == 0) {
            return;
        }
        int objTable = read16(OBJECT_TABLE);
        int objAddr;
        if (version < 4) {
            objAddr = objTable + 62 + 9*(obj - 1);
        } else {
            objAddr = objTable + 126 + 14*(obj - 1);
        }
        int b = read8(objAddr + attr/8);
        if (value) {
            b |= 128 >> (attr&7);
        } else {
            b &= ~(128 >> (attr&7));
        }
        store8(objAddr + attr/8, b);
    }

    int objParent(int obj) {
        if (obj == 0) {
            return 0;
        }
        int objTable = read16(OBJECT_TABLE);
        if (version < 4) {
            return read8(objTable + 62 + 9*(obj - 1) + 4);
        } else {
            return read16(objTable + 126 + 14*(obj - 1) + 6);
        }
    }

    void objSetParent(int obj, int parent) {
        if (obj == 0) {
            return;
        }
        int objTable = read16(OBJECT_TABLE);
        if (version < 4) {
            store8(objTable + 62 + 9*(obj - 1) + 4, parent);
        } else {
            store16(objTable + 126 + 14*(obj - 1) + 6, parent);
        }
    }

    int objSibling(int obj) {
        if (obj == 0) {
            return 0;
        }
        int objTable = read16(OBJECT_TABLE);
        if (version < 4) {
            return read8(objTable + 62 + 9*(obj - 1) + 5);
        } else {
            return read16(objTable + 126 + 14*(obj - 1) + 8);
        }
    }

    void objSetSibling(int obj, int sibling) {
        if (obj == 0) {
            return;
        }
        int objTable = read16(OBJECT_TABLE);
        if (version < 4) {
            store8(objTable + 62 + 9*(obj - 1) + 5, sibling);
        } else {
            store16(objTable + 126 + 14*(obj - 1) + 8, sibling);
        }
    }

    int objChild(int obj) {
        if (obj == 0) {
            return 0;
        }
        int objTable = read16(OBJECT_TABLE);
        if (version < 4) {
            return read8(objTable + 62 + 9*(obj - 1) + 6);
        } else {
            return read16(objTable + 126 + 14*(obj - 1) + 10);
        }
    }

    void objSetChild(int obj, int child) {
        if (obj == 0) {
            return;
        }
        int objTable = read16(OBJECT_TABLE);
        if (version < 4) {
            store8(objTable + 62 + 9*(obj - 1) + 6, child);
        } else {
            store16(objTable + 126 + 14*(obj - 1) + 10, child);
        }
    }

    int objProperties(int obj) {
        if (obj == 0) {
            return 0;
        }
        int objTable = read16(OBJECT_TABLE);
        if (version < 4) {
            return read16(objTable + 62 + 9*(obj - 1) + 7);
        } else {
            return read16(objTable + 126 + 14*(obj - 1) + 12);
        }
    }

    void objMove(int obj, int newParent) {
        int oldParent = objParent(obj);
        if (oldParent != 0) {
            int oldSibling = objSibling(obj);
            int child = objChild(oldParent);
            if (child == obj) {
                objSetChild(oldParent, oldSibling);
            }
            while (child != 0) {
                int sibling = objSibling(child);
                if (sibling == obj) {
                    objSetSibling(child, oldSibling);
                    break;
                }
                child = sibling;
            }
        }
        objSetParent(obj, newParent);
        if (newParent == 0) {
            objSetSibling(obj, 0);
        } else {
            objSetSibling(obj, objChild(newParent));
            objSetChild(newParent, obj);
        }
    }

    int getProp(int obj, int prop) {
        if (obj == 0 || prop == 0) {
            return 0;
        }
        int objTable = read16(OBJECT_TABLE);
        if (version < 4) {
            int props = read16(objTable + 62 + 9*(obj - 1) + 7);
            props += 1 + 2*read8(props);
            for (int size = read8(props); size != 0; size = read8(props)) {
                if ((size & 31) > prop) {
                    props += 2 + (size >> 5);
                    continue;
                } else if ((size & 31) == prop) {
                    if ((size >> 5) == 0) {
                        return read8(props + 1);
                    }
                    return read16(props + 1);
                } else {
                    break;
                }
            }
        } else {
            int props = read16(objTable + 126 + 14*(obj - 1) + 12);
            props += 1 + 2*read8(props);
            for (int size = read8(props); size != 0; size = read8(props)) {
                if ((size & 63) > prop) {
                    switch (size & 192) {
                    case 0:
                        props += 2;
                        continue;
                    case 64:
                        props += 3;
                        continue;
                    default:
                        int len = read8(props + 1) & 63;
                        if (len == 0) {
                            len = 64;
                        }
                        props += 2 + len;
                        continue;
                    }
                } else if ((size & 63) == prop) {
                    switch (size & 192) {
                    case 0:
                        return read8(props + 1);
                    case 64:
                        return read16(props + 1);
                    default:
                        switch (read8(props + 1) & 63) {
                        case 1:
                            return read8(props + 2);
                        default:
                            return read16(props + 2);
                        }
                    }
                } else {
                    break;
                }
            }
        }
        return read16(objTable + (prop - 1)*2);
    }

    int getPropAddr(int obj, int prop) {
        if (obj == 0 || prop == 0) {
            return 0;
        }
        int objTable = read16(OBJECT_TABLE);
        if (version < 4) {
            int props = read16(objTable + 62 + 9*(obj - 1) + 7);
            props += 1 + 2*read8(props);
            for (int size = read8(props); size != 0; size = read8(props)) {
                if ((size & 31) > prop) {
                    props += 2 + (size >> 5);
                    continue;
                } else if ((size & 31) == prop) {
                    return props + 1;
                } else {
                    break;
                }
            }
        } else {
            int props = read16(objTable + 126 + 14*(obj - 1) + 12);
            props += 1 + 2*read8(props);
            for (int size = read8(props); size != 0; size = read8(props)) {
                if ((size & 63) > prop) {
                    switch (size & 192) {
                    case 0:
                        props += 2;
                        continue;
                    case 64:
                        props += 3;
                        continue;
                    default:
                        int len = read8(props + 1) & 63;
                        if (len == 0) {
                            len = 64;
                        }
                        props += 2 + len;
                        continue;
                    }
                } else if ((size & 63) == prop) {
                    if ((size & 128) == 0) {
                        return props + 1;
                    } else {
                        return props + 2;
                    }
                } else {
                    break;
                }
            }
        }
        return 0;
    }

    int getNextProp(int obj, int prop) {
        if (obj == 0) {
            return 0;
        }
        int objTable = read16(OBJECT_TABLE);
        boolean nextProp = prop == 0;
        if (version < 4) {
            int props = read16(objTable + 62 + 9*(obj - 1) + 7);
            props += 1 + 2*read8(props);
            for (int size = read8(props); size != 0; size = read8(props)) {
                int p = size & 31;
                if (nextProp) {
                    return p;
                } else if (p == prop) {
                    nextProp = true;
                } else if (p < prop) {
                    break;
                }
                props += 2 + (size >> 5);
            }
        } else {
            int props = read16(objTable + 126 + 14*(obj - 1) + 12);
            props += 1 + 2*read8(props);
            for (int size = read8(props); size != 0; size = read8(props)) {
                int p = size & 63;
                if (nextProp) {
                    return p;
                } else if (p == prop) {
                    nextProp = true;
                } else if (p < prop) {
                    break;
                }
                switch (size & 192) {
                case 0:
                    props += 2;
                    break;
                case 64:
                    props += 3;
                    break;
                default:
                    int len = read8(props + 1) & 63;
                    if (len == 0) {
                        len = 64;
                    }
                    props += 2 + len;
                    break;
                }
            }
        }
        return 0;
    }

    int getPropLen(int propAddr) {
        if (propAddr == 0) {
            return 0;
        }
        if (version < 4) {
            return 1 + (read8(propAddr - 1) >> 5);
        } else {
            int size = read8(propAddr - 1);
            switch (size & 192) {
            case 0:
                return 1;
            case 64:
                return 2;
            default:
                size &= 63;
                if (size == 0) {
                    size = 64;
                }
                return size;
            }
        }
    }

    GlkByteArray getBuffer(final int addr, final int length) {
        return new GlkByteArray() {
            int readIndex = 0;
            int writeIndex = 0;
            @Override public int getByteElement() {
                readIndex++;
                return read8(addr+readIndex-1);
            }
            @Override public void setByteElement(int element) {
                writeIndex++;
                store8(addr+writeIndex-1, element);
            }
            @Override public int getByteElementAt(int index) {
                return read8(addr+index);
            }
            @Override public void setByteElementAt(int index, int element) {
                store8(addr+index, element);
            }
            @Override public int getReadArrayIndex() {
                return readIndex;
            }
            @Override public int setReadArrayIndex(int index) {
                readIndex = index;
                return index;
            }
            @Override public int getWriteArrayIndex() {
                return writeIndex;
            }
            @Override public int setWriteArrayIndex(int index) {
                writeIndex = index;
                return index;
            }
            @Override public int getArrayLength() {
                return length;
            }
            @Override public void setArrayLength(int length) {
            }
        };
    }

    Dictionary getDictionary() {
        if (dictionary == null) {
            dictionary = new Dictionary(this);
        }
        return dictionary;
    }
}
