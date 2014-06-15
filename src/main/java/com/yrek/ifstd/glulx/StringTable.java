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
        int node = machine.state.load32(table+8);
        int stream = machine.state.load8(addr);
        for (;;) {
            int type = machine.state.load8(node);
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
                if (!resuming) {
                    Instruction.pushCallStub(machine.state, 11, 0);
                }
                machine.state.pc = addr;
                Instruction.pushCallStub(machine.state, 10, bit);
                machine.ioSys.streamChar(machine, machine.state.load8(node+1));
                return;
            case 3:
                if (!resuming) {
                    Instruction.pushCallStub(machine.state, 11, 0);
                }
                machine.state.pc = addr;
                Instruction.pushCallStub(machine.state, 10, bit);
                machine.ioSys.putString(machine, node+1);
                return;
            case 4:
                if (!resuming) {
                    Instruction.pushCallStub(machine.state, 11, 0);
                }
                throw new RuntimeException("unimplemented");
            case 5:
                if (!resuming) {
                    Instruction.pushCallStub(machine.state, 11, 0);
                }
                machine.state.pc = addr;
                Instruction.pushCallStub(machine.state, 10, bit);
                machine.ioSys.putStringUnicode(machine, node+1);
                return;
            case 8:
                if (!resuming) {
                    Instruction.pushCallStub(machine.state, 11, 0);
                }
                machine.state.pc = addr;
                Instruction.pushCallStub(machine.state, 10, bit);
                throw new RuntimeException("unimplemented");
            case 9:
                if (!resuming) {
                    Instruction.pushCallStub(machine.state, 11, 0);
                }
                machine.state.pc = addr;
                Instruction.pushCallStub(machine.state, 10, bit);
                throw new RuntimeException("unimplemented");
            case 10:
                if (!resuming) {
                    Instruction.pushCallStub(machine.state, 11, 0);
                }
                machine.state.pc = addr;
                Instruction.pushCallStub(machine.state, 10, bit);
                throw new RuntimeException("unimplemented");
            case 11:
                if (!resuming) {
                    Instruction.pushCallStub(machine.state, 11, 0);
                }
                machine.state.pc = addr;
                Instruction.pushCallStub(machine.state, 10, bit);
                throw new RuntimeException("unimplemented");
            default:
                throw new IllegalArgumentException("Invalid string table");
            }
        }
    }
}
