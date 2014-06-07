package com.yrek.ifstd.glulx;

import java.util.Random;

class Machine {
    public byte advancePC() {
        return 0x0;
    }

    public void branch(int offset) {
        switch (offset) {
        case 0: case 1:
            functionReturn(offset);
            break;
        default:
        }
    }

    public void functionReturn(int result) {
    }

    public Random getRandom() {
        return null;
    }

    public int popStack() {
        return 0;
    }

    public void call(int addr, int[] args, int destType, int destAddr, boolean tailCall) {
    }

    public int getRAMStart() {
        return 0;
    }
}
