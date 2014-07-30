package com.yrek.ifstd.zcode;

import java.io.Serializable;

class State implements Serializable {
    private static final long serialVersionUID = 0L;

    public static int VERSION = 0x0;
    public static int GLOBAL_VAR_TABLE = 0xc;
    public static int ABBREVIATION_TABLE = 0x18;
    public static int ALPHABET_TABLE = 0x18;
    public static int ROUTINE_OFFSET = 0x28;
    public static int PRINT_OFFSET = 0x2a;
    public static int EXTRA_HEADERS = 0x36;

    public static int EXTRA_HEADERS_SIZE = 0;
    public static int EXTRA_HEADERS_UNICODE_TABLE = 3;

    byte[] ram;
    int pc;
    StackFrame frame;

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

    short readVar(int var) {
        if (var == 0) {
            return (short) frame.pop();
        } else if (var < 0 || var >= 256) {
            return (short) 0;
        } else if (var < 16) {
            return (short) frame.locals[var - 1];
        } else {
            return (short) read16(read16(GLOBAL_VAR_TABLE) + 2*(var - 16));
        }
    }

    void storeVar(int var, short val) {
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
