package com.yrek.ifstd.glulx;

import java.io.Serializable;

class StringTable implements Serializable {
    private static final long serialVersionUID = 0L;
    private static final boolean TRACE = false;

    final int table;

    StringTable(int table) {
        this.table = table;
    }

    static StringTable create(State state, int table) {
        if (table != 0) {
            return new StringTable(table);
        } else {
            return new StringTable(0) {
                private static final long serialVersionUID = 0L;
                @Override void print(Machine machine, int addr, int bit, boolean resuming) {
                    throw new IllegalArgumentException("No string table set");
                }
            };
        }
    }

    // If resuming is true, there is a call stub at the top of the stack
    // on entry and there will be a call stub at the top of the stack on
    // return.
    // If resuming is false, the machine is ready to execute the instruction
    // at the pc on entry and will be ready to execute the instruction at
    // the pc on return.
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
                    Glulx.trace.print(String.format("[decode char:%x,%c]", machine.state.load8(node+1)&255, (char) (machine.state.load8(node+1)&255)));
                }
                if (machine.ioSys.streamCharFromEncoded(machine, machine.state.load8(node+1), addr, bit, resuming)) {
                    return;
                }
                node = root;
                break;
            case 3:
                if (TRACE && Glulx.trace != null) {
                    Glulx.trace.print(String.format("[decode string:%x]", node+1));
                }
                if (machine.ioSys.streamStringFromEncoded(machine, node+1, addr, bit, resuming)) {
                    return;
                }
                node = root;
                break;
            case 4:
                if (TRACE && Glulx.trace != null) {
                    Glulx.trace.print(String.format("[decode unichar:%x,%s]", machine.state.load32(node+1), new String(Character.toChars(machine.state.load32(node+1)))));
                }
                if (machine.ioSys.streamUnicharFromEncoded(machine, machine.state.load32(node+1), addr, bit, resuming)) {
                    return;
                }
                node = root;
                break;
            case 5:
                if (TRACE && Glulx.trace != null) {
                    Glulx.trace.print(String.format("[decode string:%x]", node+1));
                }
                if (machine.ioSys.streamStringUnicodeFromEncoded(machine, node+1, addr, bit, resuming)) {
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

    private boolean indirect(Machine machine, int resumeAddr, int bit, int indirectLevel, int tableAddr, int nargs, int argsAddr, boolean resuming) {
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
                Insn.pushCallStub(machine.state, 11, 0);
            }
            machine.state.pc = resumeAddr;
            Insn.pushCallStub(machine.state, 10, bit);
            int[] args = new int[nargs];
            for (int i = 0; i < nargs; i++) {
                args[i] = machine.state.load32(argsAddr + 4*i);
            }
            Instruction.call(machine.state, tableAddr, args);
            if (resuming) {
                Insn.pushCallStub(machine.state, 11, 0);
            }
            return true;
        case 0xe0:
            if (TRACE && Glulx.trace != null) {
                Glulx.trace.print(String.format("[indirect string:%x]", tableAddr+1));
            }
            return machine.ioSys.streamStringFromEncoded(machine, tableAddr+1, resumeAddr, bit, resuming);
        case 0xe1:
            if (TRACE && Glulx.trace != null) {
                Glulx.trace.print(String.format("[indirect encoded string:%x]", tableAddr+1));
            }
            if (!resuming) {
                Insn.pushCallStub(machine.state, 11, 0);
            }
            machine.state.pc = resumeAddr;
            Insn.pushCallStub(machine.state, 10, bit);
            print(machine, tableAddr+1, 0, true);
            if (!resuming) {
                Insn.returnValue(machine, 0);
            }
            return true;
        case 0xe2:
            if (TRACE && Glulx.trace != null) {
                Glulx.trace.print(String.format("[indirect unistring:%x]", tableAddr+1));
            }
            return machine.ioSys.streamStringUnicodeFromEncoded(machine, tableAddr+4, resumeAddr, bit, resuming);
        default:
            throw new IllegalArgumentException("Invalid indirect reference");
        }
    }
}
