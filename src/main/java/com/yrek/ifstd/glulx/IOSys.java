package com.yrek.ifstd.glulx;

abstract class IOSys {
    private static final boolean TRACE = false;
    final int rock;
    final int mode;

    protected IOSys(int mode, int rock) {
        this.rock = rock;
        this.mode = mode;
    }

    void streamStringObject(Machine machine, int addr) {
        if (TRACE && Glulx.trace != null) {
            Glulx.trace.println();
            Glulx.trace.print(String.format("streamStr:%08x:%02x", addr, machine.state.load8(addr) & 255));
        }
        switch (machine.state.load8(addr) & 255) {
        case 0xe0:
            if (isFilter()) {
                Instruction.pushCallStub(machine.state, 0x11, 0);
            }
            streamString(machine, addr+1);
            break;
        case 0xe1:
            machine.stringTable.print(machine, addr+1, 0, false);
            break;
        case 0xe2:
            if (isFilter()) {
                Instruction.pushCallStub(machine.state, 0x11, 0);
            }
            streamStringUnicode(machine, addr+4);
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
    abstract void streamString(Machine machine, int addr);
    abstract void streamStringUnicode(Machine machine, int addr);

    boolean isFilter() {
        return false;
    }
}
