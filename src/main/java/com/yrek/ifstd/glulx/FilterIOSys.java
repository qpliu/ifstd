package com.yrek.ifstd.glulx;

class FilterIOSys extends IOSys {
    private static final long serialVersionUID = 0L;

    FilterIOSys(int rock) {
        super(1, rock);
    }

    @Override
    void resumePrintNumber(Machine machine, int number, int pos) {
        String num = String.valueOf(number);
        if (pos < num.length()) {
            machine.state.pc = number;
            Insn.pushCallStub(machine.state, 12, pos+1);
            Insn.resumeCallf(machine.state, rock, (int) num.charAt(pos), 0, 0, 1);
            Insn.pushCallStub(machine.state, 11, 0);
        }
    }

    @Override
    void resumePrint(Machine machine, int addr) {
        int ch = machine.state.load8(addr) & 255;
        if (ch != 0) {
            machine.state.pc = addr+1;
            Insn.pushCallStub(machine.state, 13, 0);
            Insn.resumeCallf(machine.state, rock, ch, 0, 0, 1);
            Insn.pushCallStub(machine.state, 11, 0);
        }
    }

    @Override
    void resumePrintUnicode(Machine machine, int addr) {
        int ch = machine.state.load32(addr);
        if (ch != 0) {
            machine.state.pc = addr+4;
            Insn.pushCallStub(machine.state, 14, 0);
            Insn.resumeCallf(machine.state, rock, ch, 0, 0, 1);
            Insn.pushCallStub(machine.state, 11, 0);
        }
    }

    @Override
    void streamChar(Machine machine, int ch) {
        Insn.pushCallStub(machine.state, 0, 0);
        Insn.resumeCallf(machine.state, rock, ch, 0, 0, 1);
    }

    @Override
    void streamUnichar(Machine machine, int ch) {
        Insn.pushCallStub(machine.state, 0, 0);
        Insn.resumeCallf(machine.state, rock, ch, 0, 0, 1);
    }

    @Override
    void streamNum(Machine machine, int num) {
        Insn.pushCallStub(machine.state, 11, 0);
        resumePrintNumber(machine, num, 0);
    }

    @Override
    void streamString(Machine machine, int addr) {
        Insn.pushCallStub(machine.state, 11, 0);
        resumePrint(machine, addr);
    }

    @Override
    void streamStringUnicode(Machine machine, int addr) {
        Insn.pushCallStub(machine.state, 11, 0);
        resumePrintUnicode(machine, addr);
    }

    @Override
    boolean streamCharFromEncoded(Machine machine, int ch, int resumeAddr, int bit, boolean resuming) {
        if (!resuming) {
            Insn.pushCallStub(machine.state, 11, 0);
        }
        machine.state.pc = resumeAddr;
        Insn.pushCallStub(machine.state, 10, bit);
        Insn.resumeCallf(machine.state, rock, ch, 0, 0, 1);
        if (resuming) {
            Insn.pushCallStub(machine.state, 11, 0);
        }
        return true;
    }

    @Override
    boolean streamUnicharFromEncoded(Machine machine, int ch, int resumeAddr, int bit, boolean resuming) {
        if (!resuming) {
            Insn.pushCallStub(machine.state, 11, 0);
        }
        machine.state.pc = resumeAddr;
        Insn.pushCallStub(machine.state, 10, bit);
        Insn.resumeCallf(machine.state, rock, ch, 0, 0, 1);
        if (resuming) {
            Insn.pushCallStub(machine.state, 11, 0);
        }
        return true;
    }

    @Override
    boolean streamStringFromEncoded(Machine machine, int addr, int resumeAddr, int bit, boolean resuming) {
        int ch = machine.state.load8(addr) & 255;
        if (ch == 0) {
            return false;
        }
        if (!resuming) {
            Insn.pushCallStub(machine.state, 11, 0);
        }
        machine.state.pc = resumeAddr;
        Insn.pushCallStub(machine.state, 10, bit);
        machine.state.pc = addr+1;
        Insn.pushCallStub(machine.state, 13, 0);
        Insn.resumeCallf(machine.state, rock, ch, 0, 0, 1);
        if (resuming) {
            Insn.pushCallStub(machine.state, 11, 0);
        }
        return true;
    }

    @Override
    boolean streamStringUnicodeFromEncoded(Machine machine, int addr, int resumeAddr, int bit, boolean resuming) {
        int ch = machine.state.load32(addr);
        if (ch == 0) {
            return false;
        }
        if (!resuming) {
            Insn.pushCallStub(machine.state, 11, 0);
        }
        machine.state.pc = resumeAddr;
        Insn.pushCallStub(machine.state, 10, bit);
        machine.state.pc = addr+4;
        Insn.pushCallStub(machine.state, 14, 0);
        Insn.resumeCallf(machine.state, rock, ch, 0, 0, 1);
        if (resuming) {
            Insn.pushCallStub(machine.state, 11, 0);
        }
        return true;
    }
}
