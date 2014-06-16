package com.yrek.ifstd.glulx;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

class State {
    int pc;
    int sp;
    int fp;
    byte[] memory;
    byte[] stack;

    void readFile(InputStream in, int protectStart, int protectLength) throws IOException {
        byte[] protect = null;
        if (memory != null && protectLength > 0) {
            protect = new byte[protectLength];
            System.arraycopy(memory, protectStart, protect, 0, protectLength);
        }
        byte[] header = new byte[36];
        readBytes(in, header, 0, 36);
        if (load32(header, 0) != 0x476c756c) {
            throw new IllegalArgumentException("Bad magic");
        }
        int version = load32(header, 4);
        if (version < 0x0002000 && version > 0x000301ff) {
            throw new IllegalArgumentException("Unrecognized Glulx version");
        }
        int extStart = load32(header, 12);
        int endMem = load32(header, 16);
        int stackSize = load32(header, 20);
        if (memory == null || memory.length < endMem) {
            memory = new byte[endMem];
        } else {
            Arrays.fill(memory, extStart, memory.length, (byte) 0);
        }
        System.arraycopy(header, 0, memory, 0, 36);
        readBytes(in, memory, 36, extStart - 36);
        if (!verify()) {
            throw new IllegalArgumentException("Failed checksum");
        }
        if (stack == null || stack.length < stackSize) {
            stack = new byte[stackSize];
        }
        Arrays.fill(stack, 0, stack.length, (byte) 0);
        if (protect != null) {
            System.arraycopy(protect, 0, memory, protectStart, protectLength);
        }
        pc = 0;
        sp = 0;
        fp = 0;
    }

    private void readBytes(InputStream in, byte[] buffer, int start, int count) throws IOException {
        for (int total = 0; total < count; ) {
            int n = in.read(buffer, start + total, count - total);
            if (n < 0) {
                throw new IllegalArgumentException("premature EOF");
            }
            total += n;
        }
    }

    void readSave(InputStream story, InputStream save, int protectStart, int protectLength) throws IOException {
        readFile(story, protectStart, protectLength);
        DataInputStream in = new DataInputStream(save);
        byte[] protect = null;
        if (protectLength > 0) {
            protect = new byte[protectLength];
            System.arraycopy(memory, protectStart, protect, 0, protectLength);
        }
        int id = in.readInt();
        if (id != 0x464f524d) {
            throw new IllegalArgumentException("Bad magic");
        }

        int length = in.readInt();
        id = in.readInt();
        if (id != 0x49465a53) {
            throw new IllegalArgumentException("Bad magic");
        }
        int count = 4;
        boolean gotIFhd = false;
        while (count < length) {
            id = in.readInt();
            int size = in.readInt();
            count += 8 + size;
            switch (id) {
            case 0x49466864: // IFhd
                gotIFhd = true;
                if (size != 128) {
                    throw new IllegalArgumentException("IFhd length not 128");
                }
                byte[] hd = new byte[128];
                readBytes(in, hd, 0, 128);
                for (int i = 0; i < 128; i++) {
                    if (hd[i] != memory[i]) {
                        throw new IllegalArgumentException("IFhd mismatch");
                    }
                }
                break;
            case 0x434d656d: // CMem
                if (!gotIFhd) {
                    throw new IllegalArgumentException("No IFhd");
                }
                int index = load32(8);
                for (int i = 0; i < size; i++) {
                    int b = in.read();
                    if (b < 0) {
                        throw new EOFException();
                    } else if (b != 0) {
                        memory[index] ^= (byte) b;
                        index++;
                    } else {
                        i++;
                        if (i >= size) {
                            break;
                        }
                        b = in.read();
                        if (b < 0) {
                            throw new EOFException();
                        }
                        index += b + 1;
                    }
                }
                break;
            case 0x554d656d: // UMem
                if (!gotIFhd) {
                    throw new IllegalArgumentException("No IFhd");
                }
                readBytes(in, memory, load32(8), size);
                break;
            case 0x53746b73: //Stks
                if (!gotIFhd) {
                    throw new IllegalArgumentException("No IFhd");
                }
                sp = size;
                readBytes(in, stack, 0, size);
                break;
            default:
                in.skip((long) size);
                break;
            }
        }
        if (protect != null) {
            System.arraycopy(protect, 0, memory, protectStart, protectLength);
        }
    }

    void writeSave(OutputStream save, int destType, int destAddr) throws IOException {
        DataOutputStream out = new DataOutputStream(save);
        out.writeInt(0x464f524d); // FORM
        int length = 0;
        length += 4; // IFZS
        length += 4 + 4 + 128; // IFhd
        length += 4 + 4 + memory.length - load32(8); // UMem
        length += 4 + 4 + sp; // Stks
        out.writeInt(length);
        out.writeInt(0x49465a53); // IFZS
        out.writeInt(0x49466864); //IFhd
        out.writeInt(128);
        out.write(memory, 0, 128);
        out.writeInt(0x554d656d); // UMem
        out.writeInt(memory.length - load32(8));
        out.write(memory, load32(8), memory.length - load32(8));
        out.writeInt(0x53746b73); // Stks
        out.writeInt(sp);
        out.write(stack, 0, sp);
    }

    State copyTo(State saveState) {
        if (saveState == null) {
            saveState = new State();
        }
        saveState.copyFrom(this, 0, 0);
        return saveState;
    }

    void copyFrom(State saveState, int protectStart, int protectLength) {
        if (memory == null) {
            memory = Arrays.copyOf(saveState.memory, saveState.memory.length);
        } else if (memory.length != saveState.memory.length) {
            byte[] oldMemory = memory;
            memory = Arrays.copyOf(saveState.memory, saveState.memory.length);
            System.arraycopy(oldMemory, protectStart, memory, protectStart, protectLength);
        } else {
            System.arraycopy(saveState.memory, 0, memory, 0, protectStart);
            System.arraycopy(saveState.memory, protectStart + protectLength, memory, protectStart + protectLength, memory.length - protectStart - protectLength);
        }
        if (stack == null || stack.length != saveState.stack.length) {
            stack = Arrays.copyOf(saveState.stack, saveState.stack.length);
        } else {
            System.arraycopy(saveState.stack, 0, stack, 0, stack.length);
        }
        pc = saveState.pc;
        sp = saveState.sp;
        fp = saveState.fp;
    }

    int advancePC8() {
        return memory[pc++];
    }

    int advancePC16() {
        pc += 2;
        return load16(memory, pc - 2);
    }

    int advancePC32() {
        pc += 4;
        return load32(memory, pc - 4);
    }

    int load8(int addr) {
        return memory[addr];
    }

    int load16(int addr) {
        return load16(memory, addr);
    }

    int load32(int addr) {
        return load32(memory, addr);
    }

    void store8(int addr, int value) {
        memory[addr] = (byte) value;
    }

    void store16(int addr, int value) {
        store16(memory, addr, value);
    }

    void store32(int addr, int value) {
        store32(memory, addr, value);
    }

    int pop32() {
        sp -= 4;
        return load32(stack, sp);
    }

    void push32(int value) {
        store32(stack, sp, value);
        sp += 4;
    }

    int sload8(int addr) {
        return stack[addr];
    }

    int sload16(int addr) {
        return load16(stack, addr);
    }

    int sload32(int addr) {
        return load32(stack, addr);
    }

    void sstore8(int addr, int value) {
        stack[addr] = (byte) value;
    }

    void sstore16(int addr, int value) {
        store16(stack, addr, value);
    }

    void sstore32(int addr, int value) {
        store32(stack, addr, value);
    }

    static int load16(byte[] bytes, int index) {
        return (bytes[index] << 8) | (bytes[index+1] & 255);
    }

    static int load32(byte[] bytes, int index) {
        return (bytes[index] << 24)
            | ((bytes[index+1] & 255) << 16)
            | ((bytes[index+2] & 255) << 8)
            | (bytes[index+3] & 255);
    }

    static void store16(byte[] bytes, int index, int value) {
        bytes[index] = (byte) (value >> 8);
        bytes[index+1] = (byte) value;
    }

    static void store32(byte[] bytes, int index, int value) {
        bytes[index] = (byte) (value >> 24);
        bytes[index+1] = (byte) (value >> 16);
        bytes[index+2] = (byte) (value >> 8);
        bytes[index+3] = (byte) value;
    }

    int memorySize() {
        return memory.length;
    }

    void roll(int count, int places) {
        if (count == 0) {
            return;
        }
        places %= count;
        if (places == 0) {
            return;
        } else if (places < 0) {
            places += count;
        }
        byte[] tmp = new byte[count*4];
        System.arraycopy(stack, sp - count*4, tmp, 0, count*4);
        System.arraycopy(tmp, (count - places)*4, stack, sp - count*4, places*4);
        System.arraycopy(tmp, 0, stack, sp - (count - places)*4, (count - places)*4);
    }

    boolean verify() {
        int checksum = load32(32);
        int sum = 0;
        for (int i = 0; i + 3 < memory.length; i += 4) {
            sum += load32(i);
        }
        return sum - checksum == checksum;
    }
}
