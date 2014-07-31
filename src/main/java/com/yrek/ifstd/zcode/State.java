package com.yrek.ifstd.zcode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;

import com.yrek.ifstd.glk.GlkDispatch;

class State implements Serializable {
    private static final long serialVersionUID = 0L;

    public static int VERSION = 0x0;
    public static int FLAGS1 = 0x1;
    public static int HIGH_MEMORY = 0x4;
    public static int INITIAL_PC = 0x6;
    public static int DICTIONARY = 0x8;
    public static int OBJECT_TABLE = 0xa;
    public static int GLOBAL_VAR_TABLE = 0xc;
    public static int STATIC_MEMORY = 0xe;
    public static int FLAGS2 = 0x10;
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

    byte[] ram;
    int pc;
    StackFrame frame;

    void load(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int version = in.read();
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
    }

    void init(int screenWidth, int screenHeight) throws IOException {
        //...
    }

    void loadSave(DataInputStream in) throws IOException {
        //...
    }

    void writeSave(DataOutputStream in) throws IOException {
        //...
    }

    void copyFrom(State state, boolean deepCopyStack, boolean preserveFlags) {
        preserveFlags = preserveFlags && ram != null;
        //...
        if (ram == null || ram.length != state.ram.length) {
            ram = new byte[state.ram.length];
        }
        System.arraycopy(state.ram, 0, ram, 0, ram.length);
        //...
        pc = state.pc;
        if (!deepCopyStack) {
            frame = state.frame;
        } else {
            frame = new StackFrame(state.frame);
        }
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
        switch (read8(VERSION)) {
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
            throw new IllegalArgumentException("Unrecognized Z-machine version: " + read8(VERSION));
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
            return read16(read16(GLOBAL_VAR_TABLE) + 2*(var - 16));
        }
    }

    void storeVar(int var, int val) {
        if (var == 0) {
            frame.push(val);
        } else if (var < 0 || var >= 256) {
        } else if (var < 16) {
            frame.locals[var-1] = val;
        } else {
            store16(read16(GLOBAL_VAR_TABLE) + 2*(var - 16), val);
        }
    }
}
