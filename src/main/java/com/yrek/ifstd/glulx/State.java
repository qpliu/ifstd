package com.yrek.ifstd.glulx;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

class State implements Serializable {
    private static final long serialVersionUID = 0L;

    int pc;
    int sp;
    int fp;
    byte[] memory;
    int[] stack;
    int ramStart;
    transient int localsp;

    void readFile(DataInput in, int protectStart, int protectLength) throws IOException {
        byte[] protect = null;
        if (memory != null && protectLength > 0) {
            protect = new byte[protectLength];
            System.arraycopy(memory, protectStart, protect, 0, protectLength);
        }
        byte[] header = new byte[36];
        in.readFully(header, 0, 36);
        if (load32(header, 0) != 0x476c756c) {
            throw new IllegalArgumentException("Bad magic");
        }
        int version = load32(header, 4);
        if (version < 0x0002000 && version > 0x000301ff) {
            throw new IllegalArgumentException("Unrecognized Glulx version");
        }
        ramStart = load32(header, 8);
        int extStart = load32(header, 12);
        int endMem = load32(header, 16);
        int stackSize = load32(header, 20);
        if (memory == null || memory.length < endMem) {
            memory = new byte[endMem];
        } else {
            Arrays.fill(memory, extStart, memory.length, (byte) 0);
        }
        System.arraycopy(header, 0, memory, 0, 36);
        in.readFully(memory, 36, extStart - 36);
        if (!verify()) {
            throw new IllegalArgumentException("Failed checksum");
        }
        if (stack == null || stack.length < stackSize/4) {
            stack = new int[stackSize/4];
        }
        Arrays.fill(stack, 0, stack.length, 0);
        if (protect != null) {
            System.arraycopy(protect, 0, memory, protectStart, protectLength);
        }
        pc = 0;
        sp = 0;
        fp = 0;
    }

    void readSave(DataInput story, DataInput in, int protectStart, int protectLength) throws IOException {
        readFile(story, protectStart, protectLength);
        byte[] protect = null;
        if (protectLength > 0) {
            protect = new byte[protectLength];
            if (memory.length > protectStart) {
                System.arraycopy(memory, protectStart, protect, 0, Math.min(protectLength, memory.length - protectStart));
            }
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
                in.readFully(hd, 0, 128);
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
                int index = ramStart;
                byte[] cmem = new byte[size];
                in.readFully(cmem, 0, size);
                for (int i = 0; i < size; i++) {
                    if (cmem[i] != 0) {
                        if (index < memory.length) {
                            memory[index] ^= cmem[i];
                        }
                        index++;
                    } else {
                        i++;
                        if (i >= size) {
                            break;
                        }
                        index += (cmem[i]&255) + 1;
                    }
                }
                if (index != memory.length) {
                    memory = Arrays.copyOf(memory, index);
                }
                break;
            case 0x554d656d: // UMem
                if (!gotIFhd) {
                    throw new IllegalArgumentException("No IFhd");
                }
                int endMem = ramStart + size;
                if (memory.length != endMem) {
                    memory = Arrays.copyOf(memory, endMem);
                }
                in.readFully(memory, ramStart, size);
                break;
            case 0x53746b73: //Stks
                if (!gotIFhd) {
                    throw new IllegalArgumentException("No IFhd");
                }
                sp = size;
                for (int i = 0; i < size/4; i++) {
                    stack[i] = in.readInt();
                }
                break;
            default:
                in.skipBytes(size);
                break;
            }
        }
        if (protect != null && memory.length > protectStart) {
            System.arraycopy(protect, 0, memory, protectStart, Math.min(protectLength, memory.length - protectStart));
        }
    }

    void writeSave(DataInput in, DataOutput out) throws IOException {
        out.writeInt(0x464f524d); // FORM
        int length = 0;
        length += 4; // IFZS
        length += 4 + 4 + 128; // IFhd
        length += 4 + 4 + memory.length - ramStart; // UMem
        length += 4 + 4 + sp; // Stks
        out.writeInt(length);
        out.writeInt(0x49465a53); // IFZS
        out.writeInt(0x49466864); //IFhd
        out.writeInt(128);
        byte[] header = new byte[128];
        in.readFully(header);
        out.write(header);
        out.writeInt(0x554d656d); // UMem
        out.writeInt(memory.length - ramStart);
        out.write(memory, ramStart, memory.length - ramStart);
        out.writeInt(0x53746b73); // Stks
        out.writeInt(sp);
        for (int i = 0; i < sp/4; i++) {
            out.writeInt(stack[i]);
        }
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
            if (memory.length > protectStart) {
                Arrays.fill(memory, protectStart, Math.min(memory.length, protectStart + protectLength), (byte) 0);
                if (oldMemory.length > protectStart) {
                    System.arraycopy(oldMemory, protectStart, memory, protectStart, Math.min(Math.min(protectLength, memory.length - protectStart), oldMemory.length - protectStart));
                }
            }
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
        return stack[sp/4];
    }

    void push32(int value) {
        stack[sp/4] = value;
        sp += 4;
    }

    int sload8(int addr) {
        return (stack[addr/4] >> (8*(3-addr%4))) & 255;
    }

    int sload16(int addr) {
        return (stack[addr/4] >> (16*(1-addr/2%2))) & 65535;
    }

    int sload32(int addr) {
        return stack[addr/4];
    }

    void sstore8(int addr, int value) {
        int v = stack[addr/4];
        v &= ~(255 << 8*(3-addr%4));
        v |= (value&255) << 8*(3-addr%4);
        stack[addr/4] = v;
    }

    void sstore16(int addr, int value) {
        int v = stack[addr/4];
        v &= ~(65535 << 16*(1-addr/2%2));
        v |= (value&65535) << 16*(1-addr/2%2);
        stack[addr/4] = v;
    }

    void sstore32(int addr, int value) {
        stack[addr/4] = value;
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

    int setMemorySize(int size) {
        if (size == memory.length) {
            return 0;
        }
        memory = Arrays.copyOf(memory, size);
        return 0;
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
        int[] tmp = new int[count];
        System.arraycopy(stack, sp/4 - count, tmp, 0, count);
        System.arraycopy(tmp, count - places, stack, sp/4 - count, places);
        System.arraycopy(tmp, 0, stack, sp/4 - (count - places), count - places);
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
