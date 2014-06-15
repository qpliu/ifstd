package com.yrek.ifstd.glulx;

abstract class IOSys {
    final int rock;
    final int mode;

    protected IOSys(int mode, int rock) {
        this.rock = rock;
        this.mode = mode;
    }

    void streamStr(Machine machine, int addr) {
        switch (machine.state.load8(addr) & 255) {
        case 0xe0:
            putString(machine, addr+1);
            break;
        case 0xe1:
            machine.stringTable.print(machine, addr, 0, false);
            break;
        case 0xe2:
            putStringUnicode(machine, addr+4);
            break;
        default:
            throw new IllegalArgumentException("Not a string");
        }
    }

    void resumePrintCompressed(Machine machine, int addr, int bit) {
        machine.stringTable.print(machine, addr, bit, true);
    }

    void resumePrintNumber(Machine machine, int number, int pos) {
        throw new AssertionError();
    }

    void resumePrint(Machine machine, int addr) {
        throw new AssertionError();
    }

    void resumePrintUnicode(Machine machine, int addr) {
        throw new AssertionError();
    }

    abstract void streamChar(Machine machine, int ch);
    abstract void streamUnichar(Machine machine, int ch);
    abstract void streamNum(Machine machine, int num);

    abstract void putString(Machine machine, int addr);
    abstract void putStringUnicode(Machine machine, int addr);
}
