package com.yrek.ifstd.glulx;

class StringTable {
    final int table;

    StringTable(int table) {
        this.table = table;
    }

    static StringTable create(State state, int table) {
        return new StringTable(table);
    }

    void print(Machine machine, int addr, int bit, boolean resuming) {
        final int root = machine.state.load32(table+8);
        int node = root;
        int stream = machine.state.load8(addr);
        for (;;) {
            switch (machine.state.load8(node)) {
            case 0:
                node = machine.state.load32(node + (((stream>>bit)&1) == 0 ? 1 : 5));
                bit++;
                if (bit >= 8) {
                    bit = 0;
                    addr++;
                    stream = machine.state.load8(addr);
                }
                break;
            case 1:
                if (resuming) {
                    Instruction.returnValue(machine, 0);
                }
                return;
            case 2:
                resuming = machine.ioSys.putChar(machine, machine.state.load8(node+1), resuming);
                if (resuming) {
                    machine.state.pc = addr;
                    Instruction.pushCallStub(machine.state, 10, bit);
                    return;
                }
                node = root;
                break;
            case 3:
                resuming = machine.ioSys.putString(machine, node+1, resuming);
                if (resuming) {
                    machine.state.pc = addr;
                    Instruction.pushCallStub(machine.state, 10, bit);
                    return;
                }
                node = root;
                break;
            case 4:
                resuming = machine.ioSys.putCharUnicode(machine, machine.state.load32(node+1), resuming);
                if (resuming) {
                    machine.state.pc = addr;
                    Instruction.pushCallStub(machine.state, 10, bit);
                    return;
                }
                node = root;
                break;
            case 5:
                resuming = machine.ioSys.putStringUnicode(machine, node+1, resuming);
                if (resuming) {
                    machine.state.pc = addr;
                    Instruction.pushCallStub(machine.state, 10, bit);
                    return;
                }
                node = root;
                break;
            case 8:
                resuming = indirect(machine, 1, node+1, 0, 0, resuming);
                if (resuming) {
                    return;
                }
                node = root;
                break;
            case 9:
                resuming = indirect(machine, 2, node+1, 0, 0, resuming);
                if (resuming) {
                    return;
                }
                node = root;
                break;
            case 10:
                resuming = indirect(machine, 1, node+1, machine.state.load32(node+5), node+9, resuming);
                if (resuming) {
                    return;
                }
                node = root;
                break;
            case 11:
                resuming = indirect(machine, 2, node+1, machine.state.load32(node+5), node+9, resuming);
                if (resuming) {
                    return;
                }
                node = root;
                break;
            default:
                throw new IllegalArgumentException("Invalid string table");
            }
        }
    }

    private boolean indirect(Machine machine, int indirectLevel, int addr, int nargs, int argsAddr, boolean resuming) {
        for (int i = 0; i < indirectLevel; i++) {
            addr = machine.state.load32(addr);
        }
        switch (machine.state.load8(addr)&255) {
        case 0xc0:
            throw new RuntimeException("unimplemented");
        case 0xc1:
            throw new RuntimeException("unimplemented");
        case 0xe0:
            throw new RuntimeException("unimplemented");
        case 0xe1:
            throw new RuntimeException("unimplemented");
        case 0xe2:
            throw new RuntimeException("unimplemented");
        default:
            throw new IllegalArgumentException("Invalid indirect reference");
        }
    }
}
