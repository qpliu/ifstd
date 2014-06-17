package com.yrek.ifstd.glulx;

class StringTable {
    private static final boolean TRACE = false;

    final int table;

    StringTable(int table) {
        this.table = table;
    }

    static StringTable create(State state, int table) {
        return new StringTable(table);
    }

    void print(Machine machine, int addr, int bit, boolean resuming) {
        if (TRACE && Glulx.trace != null) {
            Glulx.trace.print(String.format("[%s decode:%x[%d]]", resuming ? "resume": "start", addr, bit));
        }
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
                if (TRACE && Glulx.trace != null) {
                    Glulx.trace.print(String.format("[end decode:%x[%d]]", addr, bit));
                }
                return;
            case 2:
                if (TRACE && Glulx.trace != null) {
                    Glulx.trace.print(String.format("[decode char:%x]", machine.state.load8(node+1)&255));
                }
                if (!resuming && machine.ioSys.isFilter()) {
                    Instruction.pushCallStub(machine.state, 0x11, 0);
                }
                machine.ioSys.streamChar(machine, machine.state.load8(node+1));
                if (machine.ioSys.isFilter()) {
                    return;
                }
                node = root;
                break;
            case 3:
                if (TRACE && Glulx.trace != null) {
                    Glulx.trace.print(String.format("[decode string:%x]", node+1));
                }
                if (!resuming && machine.ioSys.isFilter()) {
                    Instruction.pushCallStub(machine.state, 0x11, 0);
                }
                machine.ioSys.streamString(machine, node+1);
                if (machine.ioSys.isFilter()) {
                    return;
                }
                node = root;
                break;
            case 4:
                if (TRACE && Glulx.trace != null) {
                    Glulx.trace.print(String.format("[decode unichar:%x]", machine.state.load32(node+1)));
                }
                if (!resuming && machine.ioSys.isFilter()) {
                    Instruction.pushCallStub(machine.state, 0x11, 0);
                }
                machine.ioSys.streamUnichar(machine, machine.state.load32(node+1));
                if (machine.ioSys.isFilter()) {
                    return;
                }
                node = root;
                break;
            case 5:
                if (TRACE && Glulx.trace != null) {
                    Glulx.trace.print(String.format("[decode string:%x]", node+1));
                }
                if (!resuming && machine.ioSys.isFilter()) {
                    Instruction.pushCallStub(machine.state, 0x11, 0);
                }
                machine.ioSys.streamStringUnicode(machine, node+1);
                if (machine.ioSys.isFilter()) {
                    return;
                }
                node = root;
                break;
            case 8:
                if (indirect(machine, addr, bit, 1, node+1, 0, 0, resuming)) {
                    return;
                }
                node = root;
                break;
            case 9:
                if (indirect(machine, addr, bit, 2, node+1, 0, 0, resuming)) {
                    return;
                }
                node = root;
                break;
            case 10:
                if (indirect(machine, addr, bit, 1, node+1, machine.state.load32(node+5), node+9, resuming)) {
                    return;
                }
                node = root;
                break;
            case 11:
                if (indirect(machine, addr, bit, 2, node+1, machine.state.load32(node+5), node+9, resuming)) {
                    return;
                }
                node = root;
                break;
            default:
                throw new IllegalArgumentException("Invalid string table");
            }
        }
    }

    private boolean indirect(Machine machine, int addr, int bit, int indirectLevel, int tableAddr, int nargs, int argsAddr, boolean resuming) {
        for (int i = 0; i < indirectLevel; i++) {
            tableAddr = machine.state.load32(tableAddr);
        }
        switch (machine.state.load8(tableAddr)&255) {
        case 0xc0:
        case 0xc1:
            if (TRACE && Glulx.trace != null) {
                Glulx.trace.print(String.format("[indirect function:%x]", tableAddr+1));
            }
            if (!resuming) {
                Instruction.pushCallStub(machine.state, 0x11, 0);
            }
            machine.state.pc = addr;
            Instruction.pushCallStub(machine.state, 0x10, bit);
            int[] args = new int[nargs];
            for (int i = 0; i < nargs; i++) {
                args[i] = machine.state.load32(argsAddr + 4*i);
            }
            Instruction.call(machine.state, tableAddr, args);
            return true;
        case 0xe0:
            if (TRACE && Glulx.trace != null) {
                Glulx.trace.print(String.format("[indirect string:%x]", tableAddr+1));
            }
            if (!resuming && machine.ioSys.isFilter()) {
                Instruction.pushCallStub(machine.state, 0x11, 0);
            }
            machine.ioSys.streamString(machine, tableAddr+1);
            return machine.ioSys.isFilter();
        case 0xe1:
            if (TRACE && Glulx.trace != null) {
                Glulx.trace.print(String.format("[indirect encoded string:%x]", tableAddr+1));
            }
            throw new RuntimeException("unimplemented");
        case 0xe2:
            if (TRACE && Glulx.trace != null) {
                Glulx.trace.print(String.format("[indirect unistring:%x]", tableAddr+1));
            }
            if (!resuming && machine.ioSys.isFilter()) {
                Instruction.pushCallStub(machine.state, 0x11, 0);
            }
            machine.ioSys.streamStringUnicode(machine, tableAddr+4);
            return machine.ioSys.isFilter();
        default:
            throw new IllegalArgumentException("Invalid indirect reference");
        }
    }
}
