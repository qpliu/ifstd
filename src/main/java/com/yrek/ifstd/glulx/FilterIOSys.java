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
            Instruction.pushCallStub(machine.state, 0x12, pos+1);
            Instruction.call(machine.state, rock, new int[] { (int) num.charAt(pos) });
        }
    }

    @Override
    void resumePrint(Machine machine, int addr) {
        int ch = machine.state.load8(addr) & 255;
        if (ch != 0) {
            Instruction.pushCallStub(machine.state, 0x13, addr+1);
            Instruction.call(machine.state, rock, new int[] { ch });
        }
    }

    @Override
    void resumePrintUnicode(Machine machine, int addr) {
        int ch = machine.state.load32(addr);
        if (ch != 0) {
            Instruction.pushCallStub(machine.state, 0x14, addr+4);
            Instruction.call(machine.state, rock, new int[] { ch });
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
        Instruction.pushCallStub(machine.state, 0x11, 0);
        machine.state.pc = num;
        Instruction.pushCallStub(machine.state, 0x12, 1);
        Instruction.call(machine.state, rock, new int[] { (int) String.valueOf(num).charAt(0) });
    }

    @Override
    void streamString(Machine machine, int addr) {
        resumePrint(machine, addr);
    }

    @Override
    void streamStringUnicode(Machine machine, int addr) {
        resumePrintUnicode(machine, addr);
    }

    boolean isFilter() {
        return true;
    }
}
