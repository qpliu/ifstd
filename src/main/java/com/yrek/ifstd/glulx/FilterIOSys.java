package com.yrek.ifstd.glulx;

class FilterIOSys extends IOSys {
    FilterIOSys(int rock) {
        super(1, rock);
    }

    @Override
    void resumePrintNumber(Machine machine, int number, int pos) {
        String num = String.valueOf(number);
        if (pos < num.length()) {
            machine.state.pc = number;
            Instruction.pushCallStub(machine.state, 12, pos+1);
            Instruction.call(machine.state, rock, new int[] { (int) num.charAt(pos) });
            Instruction.pushCallStub(machine.state, 11, 0);
        }
    }

    @Override
    void resumePrint(Machine machine, int addr) {
        int ch = machine.state.load8(addr) & 255;
        if (ch != 0) {
            machine.state.pc = addr+1;
            Instruction.pushCallStub(machine.state, 13, 0);
            Instruction.call(machine.state, rock, new int[] { ch });
            Instruction.pushCallStub(machine.state, 11, 0);
        }
    }

    @Override
    void resumePrintUnicode(Machine machine, int addr) {
        int ch = machine.state.load32(addr);
        if (ch != 0) {
            machine.state.pc = addr+4;
            Instruction.pushCallStub(machine.state, 14, 0);
            Instruction.call(machine.state, rock, new int[] { ch });
            Instruction.pushCallStub(machine.state, 11, 0);
        }
    }

    @Override
    void streamChar(Machine machine, int ch) {
        Instruction.pushCallStub(machine.state, 0, 0);
        Instruction.call(machine.state, rock, new int[] { ch });
    }

    @Override
    void streamUnichar(Machine machine, int ch) {
        Instruction.pushCallStub(machine.state, 0, 0);
        Instruction.call(machine.state, rock, new int[] { ch });
    }

    @Override
    void streamNum(Machine machine, int num) {
        Instruction.pushCallStub(machine.state, 11, 0);
        resumePrintNumber(machine, num, 0);
    }

    @Override
    void streamString(Machine machine, int addr) {
        Instruction.pushCallStub(machine.state, 11, 0);
        resumePrint(machine, addr);
    }

    @Override
    void streamStringUnicode(Machine machine, int addr) {
        Instruction.pushCallStub(machine.state, 11, 0);
        resumePrintUnicode(machine, addr);
    }

    @Override
    boolean streamCharFromEncoded(Machine machine, int ch, int resumeAddr, int bit, boolean resuming) {
        if (!resuming) {
            Instruction.pushCallStub(machine.state, 11, 0);
        }
        machine.state.pc = resumeAddr;
        Instruction.pushCallStub(machine.state, 10, bit);
        Instruction.call(machine.state, rock, new int[] { ch });
        if (resuming) {
            Instruction.pushCallStub(machine.state, 11, 0);
        }
        return true;
    }

    @Override
    boolean streamUnicharFromEncoded(Machine machine, int ch, int resumeAddr, int bit, boolean resuming) {
        if (!resuming) {
            Instruction.pushCallStub(machine.state, 11, 0);
        }
        machine.state.pc = resumeAddr;
        Instruction.pushCallStub(machine.state, 10, bit);
        Instruction.call(machine.state, rock, new int[] { ch });
        if (resuming) {
            Instruction.pushCallStub(machine.state, 11, 0);
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
            Instruction.pushCallStub(machine.state, 11, 0);
        }
        machine.state.pc = resumeAddr;
        Instruction.pushCallStub(machine.state, 10, bit);
        machine.state.pc = addr+1;
        Instruction.pushCallStub(machine.state, 13, 0);
        Instruction.call(machine.state, rock, new int[] { ch });
        if (resuming) {
            Instruction.pushCallStub(machine.state, 11, 0);
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
            Instruction.pushCallStub(machine.state, 11, 0);
        }
        machine.state.pc = resumeAddr;
        Instruction.pushCallStub(machine.state, 10, bit);
        machine.state.pc = addr+4;
        Instruction.pushCallStub(machine.state, 14, 0);
        Instruction.call(machine.state, rock, new int[] { ch });
        if (resuming) {
            Instruction.pushCallStub(machine.state, 11, 0);
        }
        return true;
    }
}
