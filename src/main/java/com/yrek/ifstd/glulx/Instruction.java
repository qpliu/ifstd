package com.yrek.ifstd.glulx;

abstract class Instruction {
    private static final Instruction[] table = new Instruction[0x1ca];
    private final Operands operands;

    Instruction(int opcode, Operands operands) {
        table[opcode] = this;
        this.operands = operands;
    }

    protected void execute(Machine machine) {
        throw new AssertionError();
    }

    protected void execute(Machine machine, Operand arg1) {
        throw new AssertionError();
    }

    protected void execute(Machine machine, Operand arg1, Operand arg2) {
        throw new AssertionError();
    }

    protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
        throw new AssertionError();
    }

    protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4) {
        throw new AssertionError();
    }

    protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4, Operand arg5) {
        throw new AssertionError();
    }

    protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4, Operand arg5, Operand arg6, Operand arg7) {
        throw new AssertionError();
    }

    protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4, Operand arg5, Operand arg6, Operand arg7, Operand arg8) {
        throw new AssertionError();
    }

    public static void executeNext(Machine machine) {
        int opcode = machine.advancePC() & 255;
        switch (opcode & 0xe0) {
        case 0x00: case 0x20: case 0x40: case 0x60:
            break;
        case 0x80: case 0xa0:
            opcode = ((opcode & 0x3f) << 8) | (machine.advancePC() & 255);
            break;
        case 0xc0:
            opcode = ((opcode & 0x1f) << 24)
                | ((machine.advancePC() & 255) << 16)
                | ((machine.advancePC() & 255) << 8)
                | (machine.advancePC() & 255);
            break;
        case 0xe0:
            throw new IllegalStateException();
        default:
            throw new AssertionError();
        }
        Instruction insn = table[opcode];
        switch (insn.operands) {
        case Z:
            insn.execute(machine);
            break;
        case L: case S:
            byte modes = machine.advancePC();
            Operand arg1 = readOperand(machine, modes&15);
            insn.execute(machine, arg1);
            break;
        case L2: case LS: case SL: case S2:
            modes = machine.advancePC();
            arg1 = readOperand(machine, modes&15);
            Operand arg2 = readOperand(machine, (modes>>4)&15);
            insn.execute(machine, arg1, arg2);
            break;
        case L3: case L2S:
            modes = machine.advancePC();
            arg1 = readOperand(machine, modes&15);
            arg2 = readOperand(machine, (modes>>4)&15);
            modes = machine.advancePC();
            Operand arg3 = readOperand(machine, modes&15);
            insn.execute(machine, arg1, arg2, arg3);
            break;
        case L4: case L3S: case L2S2:
            modes = machine.advancePC();
            arg1 = readOperand(machine, modes&15);
            arg2 = readOperand(machine, (modes>>4)&15);
            modes = machine.advancePC();
            arg3 = readOperand(machine, modes&15);
            Operand arg4 = readOperand(machine, (modes>>4)&15);
            insn.execute(machine, arg1, arg2, arg3, arg4);
            break;
        case L4S:
            modes = machine.advancePC();
            arg1 = readOperand(machine, modes&15);
            arg2 = readOperand(machine, (modes>>4)&15);
            modes = machine.advancePC();
            arg3 = readOperand(machine, modes&15);
            arg4 = readOperand(machine, (modes>>4)&15);
            modes = machine.advancePC();
            Operand arg5 = readOperand(machine, modes&15);
            insn.execute(machine, arg1, arg2, arg3, arg4, arg5);
            break;
        case L6S:
            modes = machine.advancePC();
            arg1 = readOperand(machine, modes&15);
            arg2 = readOperand(machine, (modes>>4)&15);
            modes = machine.advancePC();
            arg3 = readOperand(machine, modes&15);
            arg4 = readOperand(machine, (modes>>4)&15);
            modes = machine.advancePC();
            arg5 = readOperand(machine, modes&15);
            Operand arg6 = readOperand(machine, (modes>>4)&15);
            modes = machine.advancePC();
            Operand arg7 = readOperand(machine, modes&15);
            insn.execute(machine, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
            break;
        case L7S:
            modes = machine.advancePC();
            arg1 = readOperand(machine, modes&15);
            arg2 = readOperand(machine, (modes>>4)&15);
            modes = machine.advancePC();
            arg3 = readOperand(machine, modes&15);
            arg4 = readOperand(machine, (modes>>4)&15);
            modes = machine.advancePC();
            arg5 = readOperand(machine, modes&15);
            arg6 = readOperand(machine, (modes>>4)&15);
            modes = machine.advancePC();
            arg7 = readOperand(machine, modes&15);
            Operand arg8 = readOperand(machine, (modes>>4)&15);
            insn.execute(machine, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
            break;
        }
    }

    static {
        new Instruction(0x00, Operands.Z) { // nop
            @Override protected void execute(Machine machine) {
            }
        };
        new Instruction(0x10, Operands.L2S) { // add
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, arg1.load32() + arg2.load32());
            }
        };
        new Instruction(0x11, Operands.L2S) { // sub
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, arg1.load32() - arg2.load32());
            }
        };
        new Instruction(0x12, Operands.L2S) { // mul
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, arg1.load32() * arg2.load32());
            }
        };
        new Instruction(0x13, Operands.L2S) { // div
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, arg1.load32() / arg2.load32());
            }
        };
        new Instruction(0x14, Operands.L2S) { // mod
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, arg1.load32() % arg2.load32());
            }
        };
        new Instruction(0x15, Operands.LS) { // neg
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, -arg1.load32());
            }
        };
        new Instruction(0x18, Operands.L2S) { // bitand
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, arg1.load32() & arg2.load32());
            }
        };
        new Instruction(0x19, Operands.L2S) { // bitor
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, arg1.load32() | arg2.load32());
            }
        };
        new Instruction(0x1A, Operands.L2S) { // bitxor
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, arg1.load32() ^ arg2.load32());
            }
        };
        new Instruction(0x1B, Operands.LS) { // bitnot
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, ~arg1.load32());
            }
        };
        new Instruction(0x1C, Operands.L2S) { // shiftl
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, arg1.load32() << arg2.load32());
            }
        };
        new Instruction(0x1D, Operands.L2S) { // sshiftr
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, arg1.load32() >> arg2.load32());
            }
        };
        new Instruction(0x1E, Operands.L2S) { // ushiftr
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, arg1.load32() >>> arg2.load32());
            }
        };
        new Instruction(0x20, Operands.L) { // jump
        };
        new Instruction(0x22, Operands.L2) { // jz
        };
        new Instruction(0x23, Operands.L2) { // jnz
        };
        new Instruction(0x24, Operands.L3) { // jeq
        };
        new Instruction(0x25, Operands.L3) { // jne
        };
        new Instruction(0x26, Operands.L3) { // jlt
        };
        new Instruction(0x27, Operands.L3) { // jge
        };
        new Instruction(0x28, Operands.L3) { // jgt
        };
        new Instruction(0x29, Operands.L3) { // jle
        };
        new Instruction(0x2A, Operands.L3) { // jltu
        };
        new Instruction(0x2B, Operands.L3) { // jgeu
        };
        new Instruction(0x2C, Operands.L3) { // jgtu
        };
        new Instruction(0x2D, Operands.L3) { // jleu
        };
        new Instruction(0x30, Operands.L2S) { // call
        };
        new Instruction(0x31, Operands.L) { // return
        };
        new Instruction(0x32, Operands.SL) { // catch
        };
        new Instruction(0x33, Operands.L2) { // throw
        };
        new Instruction(0x34, Operands.L2) { // tailcall
        };
        new Instruction(0x40, Operands.LS) { // copy
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, arg1.load32());
            }
        };
        new Instruction(0x41, Operands.LS) { // copys
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store16(machine, arg1.load16());
            }
        };
        new Instruction(0x42, Operands.LS) { // copyb
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store8(machine, arg1.load8());
            }
        };
        new Instruction(0x44, Operands.LS) { // sexs
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                int arg = arg1.load32();
                if ((arg & 0x8000) == 0) {
                    arg &= 0x7fff;
                } else {
                    arg |= 0xffff0000;
                }
                arg2.store32(machine, arg);
            }
        };
        new Instruction(0x45, Operands.LS) { // sexb
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                int arg = arg1.load32();
                if ((arg & 0x80) == 0) {
                    arg &= 0x7f;
                } else {
                    arg |= 0xffffff00;
                }
                arg2.store32(machine, arg);
            }
        };
        new Instruction(0x48, Operands.L2S) { // aload
        };
        new Instruction(0x49, Operands.L2S) { // aloads
        };
        new Instruction(0x4A, Operands.L2S) { // aloadb
        };
        new Instruction(0x4B, Operands.L2S) { // aloadbit
        };
        new Instruction(0x4C, Operands.L3) { // astore
        };
        new Instruction(0x4D, Operands.L3) { // astores
        };
        new Instruction(0x4E, Operands.L3) { // astoreb
        };
        new Instruction(0x4F, Operands.L3) { // astorebit
        };
        new Instruction(0x50, Operands.S) { // stkcount
        };
        new Instruction(0x51, Operands.LS) { // stkpeek
        };
        new Instruction(0x52, Operands.Z) { // stkswap
        };
        new Instruction(0x53, Operands.L2) { // stkroll
        };
        new Instruction(0x54, Operands.L) { // stkcopy
        };
        new Instruction(0x70, Operands.L) { // streamchar
        };
        new Instruction(0x71, Operands.L) { // streamnum
        };
        new Instruction(0x72, Operands.L) { // streamstr
        };
        new Instruction(0x73, Operands.L) { // streamunichar
        };
        new Instruction(0x100, Operands.L2S) { // gestalt
        };
        new Instruction(0x101, Operands.L) { // debugtrap
            @Override protected void execute(Machine machine, Operand arg1) {
                throw new IllegalStateException();
            }
        };
        new Instruction(0x102, Operands.S) { // getmemsize
        };
        new Instruction(0x103, Operands.LS) { // setmemsize
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, 0);
            }
        };
        new Instruction(0x104, Operands.L) { // jumpabs
        };
        new Instruction(0x110, Operands.LS) { // random
        };
        new Instruction(0x111, Operands.L) { // setrandom
        };
        new Instruction(0x120, Operands.Z) { // quit
        };
        new Instruction(0x121, Operands.S) { // verify
        };
        new Instruction(0x122, Operands.Z) { // restart
        };
        new Instruction(0x123, Operands.LS) { // save
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, 1);
            }
        };
        new Instruction(0x124, Operands.LS) { // restore
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, 1);
            }
        };
        new Instruction(0x125, Operands.S) { // saveundo
            @Override protected void execute(Machine machine, Operand arg1) {
                arg1.store32(machine, 1);
            }
        };
        new Instruction(0x126, Operands.S) { // restoreundo
            @Override protected void execute(Machine machine, Operand arg1) {
                arg1.store32(machine, 1);
            }
        };
        new Instruction(0x127, Operands.L2) { // protect
        };
        new Instruction(0x130, Operands.L2S) { // glk
        };
        new Instruction(0x140, Operands.S) { // getstringtbl
        };
        new Instruction(0x141, Operands.L) { // setstringtbl
        };
        new Instruction(0x148, Operands.S2) { // getiosys
        };
        new Instruction(0x149, Operands.L2) { // setiosys
        };
        new Instruction(0x150, Operands.L7S) { // linearsearch
        };
        new Instruction(0x151, Operands.L7S) { // binarysearch
        };
        new Instruction(0x152, Operands.L6S) { // linkedsearch
        };
        new Instruction(0x160, Operands.LS) { // callf
        };
        new Instruction(0x161, Operands.L2S) { // callfi
        };
        new Instruction(0x162, Operands.L3S) { // callfii
        };
        new Instruction(0x163, Operands.L4S) { // callfiii
        };
        new Instruction(0x170, Operands.L2) { // mzero
        };
        new Instruction(0x171, Operands.L3) { // mcopy
        };
        new Instruction(0x178, Operands.LS) { // malloc
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, 0);
            }
        };
        new Instruction(0x179, Operands.L) { // mfree
            @Override protected void execute(Machine machine, Operand arg1) {
                arg1.store32(machine, 0);
            }
        };
        new Instruction(0x180, Operands.L2) { // accelfunc
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
            }
        };
        new Instruction(0x181, Operands.L2) { // accelparam
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
            }
        };
        new Instruction(0x190, Operands.LS) { // numtof
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, Float.floatToIntBits((float) arg1.load32()));
            }
        };
        new Instruction(0x191, Operands.LS) { // ftonumz
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                float arg = Float.intBitsToFloat(arg1.load32());
                arg2.store32(machine, Math.round(Math.copySign((float) Math.floor(Math.abs(arg)), arg)));
            }
        };
        new Instruction(0x192, Operands.LS) { // ftonumn
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, Math.round(Float.intBitsToFloat(arg1.load32())));
            }
        };
        new Instruction(0x198, Operands.LS) { // ceil
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, Float.floatToIntBits((float) Math.ceil((double) Float.intBitsToFloat(arg1.load32()))));
            }
        };
        new Instruction(0x199, Operands.LS) { // floor
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, Float.floatToIntBits((float) Math.floor((double) Float.intBitsToFloat(arg1.load32()))));
            }
        };
        new Instruction(0x1A0, Operands.L2S) { // fadd
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, Float.floatToIntBits(Float.intBitsToFloat(arg1.load32()) + Float.intBitsToFloat(arg2.load32())));
            }
        };
        new Instruction(0x1A1, Operands.L2S) { // fsub
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, Float.floatToIntBits(Float.intBitsToFloat(arg1.load32()) - Float.intBitsToFloat(arg2.load32())));
            }
        };
        new Instruction(0x1A2, Operands.L2S) { // fmul
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, Float.floatToIntBits(Float.intBitsToFloat(arg1.load32()) * Float.intBitsToFloat(arg2.load32())));
            }
        };
        new Instruction(0x1A3, Operands.L2S) { // fdiv
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, Float.floatToIntBits(Float.intBitsToFloat(arg1.load32()) / Float.intBitsToFloat(arg2.load32())));
            }
        };
        new Instruction(0x1A4, Operands.L2S2) { // fmod
        };
        new Instruction(0x1A8, Operands.LS) { // sqrt
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, Float.floatToIntBits((float) Math.sqrt((double) Float.intBitsToFloat(arg1.load32()))));
            }
        };
        new Instruction(0x1A9, Operands.LS) { // exp
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, Float.floatToIntBits((float) Math.exp((double) Float.intBitsToFloat(arg1.load32()))));
            }
        };
        new Instruction(0x1AA, Operands.LS) { // log
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, Float.floatToIntBits((float) Math.log((double) Float.intBitsToFloat(arg1.load32()))));
            }
        };
        new Instruction(0x1AB, Operands.L2S) { // pow
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, Float.floatToIntBits((float) Math.pow((double) Float.intBitsToFloat(arg1.load32()), (double) Float.intBitsToFloat(arg2.load32()))));
            }
        };
        new Instruction(0x1B0, Operands.LS) { // sin
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, Float.floatToIntBits((float) Math.sin((double) Float.intBitsToFloat(arg1.load32()))));
            }
        };
        new Instruction(0x1B1, Operands.LS) { // cos
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, Float.floatToIntBits((float) Math.cos((double) Float.intBitsToFloat(arg1.load32()))));
            }
        };
        new Instruction(0x1B2, Operands.LS) { // tan
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, Float.floatToIntBits((float) Math.tan((double) Float.intBitsToFloat(arg1.load32()))));
            }
        };
        new Instruction(0x1B3, Operands.LS) { // asin
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, Float.floatToIntBits((float) Math.asin((double) Float.intBitsToFloat(arg1.load32()))));
            }
        };
        new Instruction(0x1B4, Operands.LS) { // acos
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, Float.floatToIntBits((float) Math.acos((double) Float.intBitsToFloat(arg1.load32()))));
            }
        };
        new Instruction(0x1B5, Operands.LS) { // atan
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine, Float.floatToIntBits((float) Math.atan((double) Float.intBitsToFloat(arg1.load32()))));
            }
        };
        new Instruction(0x1B6, Operands.L2S) { // atan2
            @Override protected void execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine, Float.floatToIntBits((float) Math.atan2((double) Float.intBitsToFloat(arg1.load32()), (double) Float.intBitsToFloat(arg2.load32()))));
            }
        };
        new Instruction(0x1C0, Operands.L4) { // jfeq
        };
        new Instruction(0x1C1, Operands.L4) { // jfne
        };
        new Instruction(0x1C2, Operands.L3) { // jflt
        };
        new Instruction(0x1C3, Operands.L3) { // jfle
        };
        new Instruction(0x1C4, Operands.L3) { // jfgt
        };
        new Instruction(0x1C5, Operands.L3) { // jfge
        };
        new Instruction(0x1C8, Operands.L2) { // jisnan
        };
        new Instruction(0x1C9, Operands.L2) { // jisinf
        };
    }

    private enum Operands {
        Z,
        L, L2, L3, L4,
        LS, L2S, L3S, L4S, L6S, L7S,
        L2S2,
        S, SL,
        S2,
            ;
    }

    interface Operand {
        int load8();
        int load16();
        int load32();
        void store8(Machine machine, int value);
        void store16(Machine machine, int value);
        void store32(Machine machine, int value);
    }

    private static final Operand operand0 = new Operand() {
        @Override public int load8() { return 0; }
        @Override public int load16() { return 0; }
        @Override public int load32() { return 0; }
        @Override public void store8(Machine machine, int value) {}
        @Override public void store16(Machine machine, int value) {}
        @Override public void store32(Machine machine, int value) {}
    };

    private static class ConstantOperand implements Operand {
        private final int value;
        ConstantOperand(int value) {
            this.value = value;
        }
        @Override public int load8() { return value; }
        @Override public int load16() { return value; }
        @Override public int load32() { return value; }
        @Override public void store8(Machine machine, int value) {
            throw new IllegalStateException();
        }
        @Override public void store16(Machine machine, int value) {
            throw new IllegalStateException();
        }
        @Override public void store32(Machine machine, int value) {
            throw new IllegalStateException();
        }
    };

    private static Operand readOperand(Machine machine, int mode) {
        switch (mode) {
        case 0:
            return operand0;
        case 1:
            int value = (int) machine.advancePC();
            return new ConstantOperand(value);
        case 2:
            value = (((int) machine.advancePC()) << 8)
                | (machine.advancePC() & 255);
            return new ConstantOperand(value);
        case 3:
            value = (((int) machine.advancePC()) << 24)
                | ((machine.advancePC() & 255) << 16)
                | ((machine.advancePC() & 255) << 8)
                | ((machine.advancePC() & 255) << 0);
            return new ConstantOperand(value);
        case 4:
            throw new IllegalStateException();
        case 5:
            machine.advancePC();
            return null;
        case 6:
            machine.advancePC();
            machine.advancePC();
            return null;
        case 7:
            machine.advancePC();
            machine.advancePC();
            machine.advancePC();
            machine.advancePC();
            return null;
        case 8:
            return null;
        case 9:
            machine.advancePC();
            return null;
        case 10:
            machine.advancePC();
            machine.advancePC();
            return null;
        case 11:
            machine.advancePC();
            machine.advancePC();
            machine.advancePC();
            machine.advancePC();
            return null;
        case 12:
            throw new IllegalStateException();
        case 13:
            machine.advancePC();
            return null;
        case 14:
            machine.advancePC();
            machine.advancePC();
            return null;
        case 15:
            machine.advancePC();
            machine.advancePC();
            machine.advancePC();
            machine.advancePC();
            return null;
        default:
            throw new AssertionError();
        }
    }
}
