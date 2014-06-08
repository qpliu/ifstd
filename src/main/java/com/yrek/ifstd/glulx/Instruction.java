package com.yrek.ifstd.glulx;

import java.util.Arrays;

abstract class Instruction {
    private static final Instruction[] table = new Instruction[0x1ca];
    private final Operands operands;

    Instruction(int opcode, Operands operands) {
        table[opcode] = this;
        this.operands = operands;
    }

    protected Result execute(Machine machine) {
        throw new AssertionError();
    }

    protected Result execute(Machine machine, Operand arg1) {
        throw new AssertionError();
    }

    protected Result execute(Machine machine, Operand arg1, Operand arg2) {
        throw new AssertionError();
    }

    protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
        throw new AssertionError();
    }

    protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4) {
        throw new AssertionError();
    }

    protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4, Operand arg5) {
        throw new AssertionError();
    }

    protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4, Operand arg5, Operand arg6, Operand arg7) {
        throw new AssertionError();
    }

    protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4, Operand arg5, Operand arg6, Operand arg7, Operand arg8) {
        throw new AssertionError();
    }

    public enum Result {
        Continue, Tick, Quit, Restart;
    }

    public static Result executeNext(Machine machine) {
        int opcode;
        switch (machine.state.load8(machine.state.pc) & 0xe0) {
        case 0x00: case 0x20: case 0x40: case 0x60:
            opcode = machine.state.advancePC8() & 0x7f;
            break;
        case 0x80: case 0xa0:
            opcode = machine.state.advancePC16() & 0x3fff;
            break;
        case 0xc0:
            opcode = machine.state.advancePC32() & 0x1fffffff;
            break;
        case 0xe0:
            throw new IllegalArgumentException("Unrecognized opcode");
        default:
            throw new AssertionError();
        }
        Instruction insn = table[opcode];
        switch (insn.operands) {
        case Z:
            return insn.execute(machine);
        case L: case S:
            int modes = machine.state.advancePC8();
            Operand arg1 = readOperand(machine, modes&15);
            return insn.execute(machine, arg1);
        case L2: case LS: case SL: case S2:
            modes = machine.state.advancePC8();
            arg1 = readOperand(machine, modes&15);
            Operand arg2 = readOperand(machine, (modes>>4)&15);
            return insn.execute(machine, arg1, arg2);
        case L3: case L2S:
            modes = machine.state.advancePC8();
            arg1 = readOperand(machine, modes&15);
            arg2 = readOperand(machine, (modes>>4)&15);
            modes = machine.state.advancePC8();
            Operand arg3 = readOperand(machine, modes&15);
            return insn.execute(machine, arg1, arg2, arg3);
        case L4: case L3S: case L2S2:
            modes = machine.state.advancePC8();
            arg1 = readOperand(machine, modes&15);
            arg2 = readOperand(machine, (modes>>4)&15);
            modes = machine.state.advancePC8();
            arg3 = readOperand(machine, modes&15);
            Operand arg4 = readOperand(machine, (modes>>4)&15);
            return insn.execute(machine, arg1, arg2, arg3, arg4);
        case L4S:
            modes = machine.state.advancePC8();
            arg1 = readOperand(machine, modes&15);
            arg2 = readOperand(machine, (modes>>4)&15);
            modes = machine.state.advancePC8();
            arg3 = readOperand(machine, modes&15);
            arg4 = readOperand(machine, (modes>>4)&15);
            modes = machine.state.advancePC8();
            Operand arg5 = readOperand(machine, modes&15);
            return insn.execute(machine, arg1, arg2, arg3, arg4, arg5);
        case L6S:
            modes = machine.state.advancePC8();
            arg1 = readOperand(machine, modes&15);
            arg2 = readOperand(machine, (modes>>4)&15);
            modes = machine.state.advancePC8();
            arg3 = readOperand(machine, modes&15);
            arg4 = readOperand(machine, (modes>>4)&15);
            modes = machine.state.advancePC8();
            arg5 = readOperand(machine, modes&15);
            Operand arg6 = readOperand(machine, (modes>>4)&15);
            modes = machine.state.advancePC8();
            Operand arg7 = readOperand(machine, modes&15);
            return insn.execute(machine, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        case L7S:
            modes = machine.state.advancePC8();
            arg1 = readOperand(machine, modes&15);
            arg2 = readOperand(machine, (modes>>4)&15);
            modes = machine.state.advancePC8();
            arg3 = readOperand(machine, modes&15);
            arg4 = readOperand(machine, (modes>>4)&15);
            modes = machine.state.advancePC8();
            arg5 = readOperand(machine, modes&15);
            arg6 = readOperand(machine, (modes>>4)&15);
            modes = machine.state.advancePC8();
            arg7 = readOperand(machine, modes&15);
            Operand arg8 = readOperand(machine, (modes>>4)&15);
            return insn.execute(machine, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        }
        throw new AssertionError();
    }

    static {
        // arguments MUST be executed in order (due to stack addressing modes)
        new Instruction(0x00, Operands.Z) { // nop
            @Override protected Result execute(Machine machine) {
                return Result.Continue;
            }
        };
        new Instruction(0x10, Operands.L2S) { // add
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) + arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x11, Operands.L2S) { // sub
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) - arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x12, Operands.L2S) { // mul
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) * arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x13, Operands.L2S) { // div
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) / arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x14, Operands.L2S) { // mod
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) % arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x15, Operands.LS) { // neg
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, -arg1.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x18, Operands.L2S) { // bitand
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) & arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x19, Operands.L2S) { // bitor
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) | arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x1A, Operands.L2S) { // bitxor
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) ^ arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x1B, Operands.LS) { // bitnot
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, ~arg1.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x1C, Operands.L2S) { // shiftl
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) << arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x1D, Operands.L2S) { // sshiftr
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) >> arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x1E, Operands.L2S) { // ushiftr
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) >>> arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x20, Operands.L) { // jump
            @Override protected Result execute(Machine machine, Operand arg1) {
                branch(machine.state, arg1.load32(machine.state));
                return Result.Tick;
            }
        };
        new Instruction(0x22, Operands.L2) { // jz
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                if (a1 == 0) {
                    branch(machine.state, a2);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x23, Operands.L2) { // jnz
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                if (a1 != 0) {
                    branch(machine.state, a2);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x24, Operands.L3) { // jeq
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int a3 = arg3.load32(machine.state);
                if (a1 == a2) {
                    branch(machine.state, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x25, Operands.L3) { // jne
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int a3 = arg3.load32(machine.state);
                if (a1 != a2) {
                    branch(machine.state, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x26, Operands.L3) { // jlt
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int a3 = arg3.load32(machine.state);
                if (a1 < a2) {
                    branch(machine.state, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x27, Operands.L3) { // jge
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int a3 = arg3.load32(machine.state);
                if (a1 >= a2) {
                    branch(machine.state, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x28, Operands.L3) { // jgt
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int a3 = arg3.load32(machine.state);
                if (a1 > a2) {
                    branch(machine.state, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x29, Operands.L3) { // jle
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int a3 = arg3.load32(machine.state);
                if (a1 <= a2) {
                    branch(machine.state, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x2A, Operands.L3) { // jltu
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                long a1 = arg1.load32(machine.state) & 0xffffffffL;
                long a2 = arg2.load32(machine.state) & 0xffffffffL;
                int a3 = arg3.load32(machine.state);
                if (a1 < a2) {
                    branch(machine.state, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x2B, Operands.L3) { // jgeu
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                long a1 = arg1.load32(machine.state) & 0xffffffffL;
                long a2 = arg2.load32(machine.state) & 0xffffffffL;
                int a3 = arg3.load32(machine.state);
                if (a1 >= a2) {
                    branch(machine.state, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x2C, Operands.L3) { // jgtu
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                long a1 = arg1.load32(machine.state) & 0xffffffffL;
                long a2 = arg2.load32(machine.state) & 0xffffffffL;
                int a3 = arg3.load32(machine.state);
                if (a1 > a2) {
                    branch(machine.state, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x2D, Operands.L3) { // jleu
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                long a1 = arg1.load32(machine.state) & 0xffffffffL;
                long a2 = arg2.load32(machine.state) & 0xffffffffL;
                int a3 = arg3.load32(machine.state);
                if (a1 <= a2) {
                    branch(machine.state, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x30, Operands.L2S) { // call
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int[] args = new int[a2];
                for (int i = 0; i < a2; i++) {
                    args[i] = machine.state.pop32();
                }
                pushCallStub(machine.state, arg3.getDestType(), arg3.getDestAddr());
                machine.state.fp = machine.state.sp;
                call(machine.state, a1, args);
                return Result.Tick;
            }
        };
        new Instruction(0x31, Operands.L) { // return
            @Override protected Result execute(Machine machine, Operand arg1) {
                returnValue(machine.state, arg1.load32(machine.state));
                return Result.Tick;
            }
        };
        new Instruction(0x32, Operands.SL) { // catch
        };
        new Instruction(0x33, Operands.L2) { // throw
        };
        new Instruction(0x34, Operands.L2) { // tailcall
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int[] args = new int[a2];
                for (int i = 0; i < a2; i++) {
                    args[i] = machine.state.pop32();
                }
                machine.state.sp = machine.state.fp;
                call(machine.state, a1, args);
                return Result.Tick;
            }
        };
        new Instruction(0x40, Operands.LS) { // copy
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, arg1.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x41, Operands.LS) { // copys
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store16(machine.state, arg1.load16(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x42, Operands.LS) { // copyb
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store8(machine.state, arg1.load8(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x44, Operands.LS) { // sexs
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int arg = arg1.load32(machine.state);
                if ((arg & 0x8000) == 0) {
                    arg &= 0x7fff;
                } else {
                    arg |= 0xffff0000;
                }
                arg2.store32(machine.state, arg);
                return Result.Continue;
            }
        };
        new Instruction(0x45, Operands.LS) { // sexb
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int arg = arg1.load32(machine.state);
                if ((arg & 0x80) == 0) {
                    arg &= 0x7f;
                } else {
                    arg |= 0xffffff00;
                }
                arg2.store32(machine.state, arg);
                return Result.Continue;
            }
        };
        new Instruction(0x48, Operands.L2S) { // aload
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, machine.state.load32(arg1.load32(machine.state) + 4*arg2.load32(machine.state)));
                return Result.Continue;
            }
        };
        new Instruction(0x49, Operands.L2S) { // aloads
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, machine.state.load16(arg1.load32(machine.state) + 2*arg2.load32(machine.state)) & 65535);
                return Result.Continue;
            }
        };
        new Instruction(0x4A, Operands.L2S) { // aloadb
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, machine.state.load8(arg1.load32(machine.state) + arg2.load32(machine.state)) & 255);
                return Result.Continue;
            }
        };
        new Instruction(0x4B, Operands.L2S) { // aloadbit
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                while (a2 < 0) {
                    a2 += 8;
                    a1--;
                }
                while (a2 >= 8) {
                    a2 -= 8;
                    a1++;
                }
                arg3.store32(machine.state, (a1 >> a2) & 1);
                return Result.Continue;
            }
        };
        new Instruction(0x4C, Operands.L3) { // astore
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                machine.state.store32(arg1.load32(machine.state) + 4*arg2.load32(machine.state), arg3.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x4D, Operands.L3) { // astores
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                machine.state.store16(arg1.load32(machine.state) + 2*arg2.load32(machine.state), arg3.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x4E, Operands.L3) { // astoreb
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                machine.state.store8(arg1.load32(machine.state) + arg2.load32(machine.state), arg3.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x4F, Operands.L3) { // astorebit
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                while (a2 < 0) {
                    a2 += 8;
                    a1--;
                }
                while (a2 >= 8) {
                    a2 -= 8;
                    a1++;
                }
                int b = machine.state.load8(a1);
                if (arg3.load32(machine.state) == 0) {
                    b &= ~(1 << a2);
                } else {
                    b |= 1 << a2;
                }
                machine.state.store8(a1, b);
                return Result.Continue;
            }
        };
        new Instruction(0x50, Operands.S) { // stkcount
            @Override protected Result execute(Machine machine, Operand arg1) {
                arg1.store32(machine.state, (machine.state.sp - machine.state.fp - machine.state.sload32(machine.state.fp))/4);
                return Result.Continue;
            }
        };
        new Instruction(0x51, Operands.LS) { // stkpeek
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, machine.state.sload32(machine.state.sp - 4 - 4*arg1.load32(machine.state)));
                return Result.Continue;
            }
        };
        new Instruction(0x52, Operands.Z) { // stkswap
            @Override protected Result execute(Machine machine) {
                byte tmp = machine.state.stack[machine.state.sp - 1];
                machine.state.stack[machine.state.sp - 1] = machine.state.stack[machine.state.sp - 5];
                machine.state.stack[machine.state.sp - 5] = tmp;
                tmp = machine.state.stack[machine.state.sp - 2];
                machine.state.stack[machine.state.sp - 2] = machine.state.stack[machine.state.sp - 6];
                machine.state.stack[machine.state.sp - 6] = tmp;
                tmp = machine.state.stack[machine.state.sp - 3];
                machine.state.stack[machine.state.sp - 3] = machine.state.stack[machine.state.sp - 7];
                machine.state.stack[machine.state.sp - 7] = tmp;
                tmp = machine.state.stack[machine.state.sp - 4];
                machine.state.stack[machine.state.sp - 4] = machine.state.stack[machine.state.sp - 8];
                machine.state.stack[machine.state.sp - 8] = tmp;
                return Result.Continue;
            }
        };
        new Instruction(0x53, Operands.L2) { // stkroll
        };
        new Instruction(0x54, Operands.L) { // stkcopy
            @Override protected Result execute(Machine machine, Operand arg1) {
                int n = arg1.load32(machine.state);
                int addr = machine.state.sp - 4*n;
                for (int i = 0; i < n; i++) {
                    machine.state.push32(machine.state.sload32(addr));
                    addr += 4;
                }
                return Result.Continue;
            }
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
            @Override protected Result execute(Machine machine, Operand arg1) {
                throw new IllegalArgumentException("debugtrap instruction");
            }
        };
        new Instruction(0x102, Operands.S) { // getmemsize
            @Override protected Result execute(Machine machine, Operand arg1) {
                arg1.store32(machine.state, machine.state.memory.length);
                return Result.Continue;
            }
        };
        new Instruction(0x103, Operands.LS) { // setmemsize
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, 0);
                return Result.Continue;
            }
        };
        new Instruction(0x104, Operands.L) { // jumpabs
            @Override protected Result execute(Machine machine, Operand arg1) {
                machine.state.pc = arg1.load32(machine.state);
                return Result.Tick;
            }
        };
        new Instruction(0x110, Operands.LS) { // random
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, machine.random.nextInt(arg1.load32(machine.state)));
                return Result.Continue;
            }
        };
        new Instruction(0x111, Operands.L) { // setrandom
            @Override protected Result execute(Machine machine, Operand arg1) {
                machine.random.setSeed((long) arg1.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x120, Operands.Z) { // quit
            @Override protected Result execute(Machine machine) {
                return Result.Quit;
            }
        };
        new Instruction(0x121, Operands.S) { // verify
        };
        new Instruction(0x122, Operands.Z) { // restart
            @Override protected Result execute(Machine machine) {
                return Result.Restart;
            }
        };
        new Instruction(0x123, Operands.LS) { // save
        };
        new Instruction(0x124, Operands.LS) { // restore
        };
        new Instruction(0x125, Operands.S) { // saveundo
            @Override protected Result execute(Machine machine, Operand arg1) {
                machine.saveUndo = machine.state.copyTo(machine.saveUndo);
                arg1.store32(machine.state, 0);
                arg1.store32(machine.saveUndo, -1);
                return Result.Tick;
            }
        };
        new Instruction(0x126, Operands.S) { // restoreundo
            @Override protected Result execute(Machine machine, Operand arg1) {
                if (machine.saveUndo == null) {
                    arg1.store32(machine.state, 1);
                } else {
                    machine.state.copyFrom(machine.saveUndo, machine.protectStart, machine.protectLength);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x127, Operands.L2) { // protect
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                machine.protectStart = arg1.load32(machine.state);
                machine.protectLength = arg2.load32(machine.state);
                return Result.Continue;
            }
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
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                int[] args = new int[0];
                pushCallStub(machine.state, arg2.getDestType(), arg2.getDestAddr());
                machine.state.fp = machine.state.sp;
                call(machine.state, a1, args);
                return Result.Tick;
            }
        };
        new Instruction(0x161, Operands.L2S) { // callfi
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int[] args = new int[] {
                    arg2.load32(machine.state),
                };
                pushCallStub(machine.state, arg3.getDestType(), arg3.getDestAddr());
                machine.state.fp = machine.state.sp;
                call(machine.state, a1, args);
                return Result.Tick;
            }
        };
        new Instruction(0x162, Operands.L3S) { // callfii
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4) {
                int a1 = arg1.load32(machine.state);
                int[] args = new int[] {
                    arg2.load32(machine.state),
                    arg3.load32(machine.state),
                };
                pushCallStub(machine.state, arg4.getDestType(), arg4.getDestAddr());
                machine.state.fp = machine.state.sp;
                call(machine.state, a1, args);
                return Result.Tick;
            }
        };
        new Instruction(0x163, Operands.L4S) { // callfiii
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4, Operand arg5) {
                int a1 = arg1.load32(machine.state);
                int[] args = new int[] {
                    arg2.load32(machine.state),
                    arg3.load32(machine.state),
                    arg4.load32(machine.state),
                };
                pushCallStub(machine.state, arg5.getDestType(), arg5.getDestAddr());
                machine.state.fp = machine.state.sp;
                call(machine.state, a1, args);
                return Result.Tick;
            }
        };
        new Instruction(0x170, Operands.L2) { // mzero
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                Arrays.fill(machine.state.memory, a2, a1 + a2, (byte) 0);
                return Result.Continue;
            }
        };
        new Instruction(0x171, Operands.L3) { // mcopy
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int a3 = arg3.load32(machine.state);
                System.arraycopy(machine.state.memory, a2, machine.state.memory, a3, a1);
                return Result.Continue;
            }
        };
        new Instruction(0x178, Operands.LS) { // malloc
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, 0);
                return Result.Continue;
            }
        };
        new Instruction(0x179, Operands.L) { // mfree
            @Override protected Result execute(Machine machine, Operand arg1) {
                arg1.store32(machine.state, 0);
                return Result.Continue;
            }
        };
        new Instruction(0x180, Operands.L2) { // accelfunc
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                return Result.Continue;
            }
        };
        new Instruction(0x181, Operands.L2) { // accelparam
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                return Result.Continue;
            }
        };
        new Instruction(0x190, Operands.LS) { // numtof
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) arg1.load32(machine.state)));
                return Result.Continue;
            }
        };
        new Instruction(0x191, Operands.LS) { // ftonumz
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                float arg = Float.intBitsToFloat(arg1.load32(machine.state));
                arg2.store32(machine.state, Math.round(Math.copySign((float) Math.floor(Math.abs(arg)), arg)));
                return Result.Continue;
            }
        };
        new Instruction(0x192, Operands.LS) { // ftonumn
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Math.round(Float.intBitsToFloat(arg1.load32(machine.state))));
                return Result.Continue;
            }
        };
        new Instruction(0x198, Operands.LS) { // ceil
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.ceil((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x199, Operands.LS) { // floor
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.floor((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1A0, Operands.L2S) { // fadd
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, Float.floatToIntBits(Float.intBitsToFloat(arg1.load32(machine.state)) + Float.intBitsToFloat(arg2.load32(machine.state))));
                return Result.Continue;
            }
        };
        new Instruction(0x1A1, Operands.L2S) { // fsub
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, Float.floatToIntBits(Float.intBitsToFloat(arg1.load32(machine.state)) - Float.intBitsToFloat(arg2.load32(machine.state))));
                return Result.Continue;
            }
        };
        new Instruction(0x1A2, Operands.L2S) { // fmul
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, Float.floatToIntBits(Float.intBitsToFloat(arg1.load32(machine.state)) * Float.intBitsToFloat(arg2.load32(machine.state))));
                return Result.Continue;
            }
        };
        new Instruction(0x1A3, Operands.L2S) { // fdiv
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, Float.floatToIntBits(Float.intBitsToFloat(arg1.load32(machine.state)) / Float.intBitsToFloat(arg2.load32(machine.state))));
                return Result.Continue;
            }
        };
        new Instruction(0x1A4, Operands.L2S2) { // fmod
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg1.load32(machine.state));
                float q = Math.copySign((float) Math.floor(Math.abs(a1)/Math.abs(a2)), a1*a2);
                float r = a1 - q*a2;
                arg3.store32(machine.state, Float.floatToIntBits(r));
                arg4.store32(machine.state, Float.floatToIntBits(q));
                return Result.Continue;
            }
        };
        new Instruction(0x1A8, Operands.LS) { // sqrt
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.sqrt((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1A9, Operands.LS) { // exp
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.exp((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1AA, Operands.LS) { // log
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.log((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1AB, Operands.L2S) { // pow
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, Float.floatToIntBits((float) Math.pow((double) Float.intBitsToFloat(arg1.load32(machine.state)), (double) Float.intBitsToFloat(arg2.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1B0, Operands.LS) { // sin
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.sin((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1B1, Operands.LS) { // cos
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.cos((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1B2, Operands.LS) { // tan
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.tan((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1B3, Operands.LS) { // asin
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.asin((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1B4, Operands.LS) { // acos
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.acos((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1B5, Operands.LS) { // atan
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.atan((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1B6, Operands.L2S) { // atan2
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, Float.floatToIntBits((float) Math.atan2((double) Float.intBitsToFloat(arg1.load32(machine.state)), (double) Float.intBitsToFloat(arg2.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1C0, Operands.L4) { // jfeq
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg2.load32(machine.state));
                float a3 = Float.intBitsToFloat(arg3.load32(machine.state));
                int a4 = arg4.load32(machine.state);
                if (Math.abs(a1 - a2) < Math.abs(a3)) {
                    branch(machine.state, a4);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x1C1, Operands.L4) { // jfne
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg2.load32(machine.state));
                float a3 = Float.intBitsToFloat(arg3.load32(machine.state));
                int a4 = arg4.load32(machine.state);
                if (!(Math.abs(a1 - a2) < Math.abs(a3))) {
                    branch(machine.state, a4);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x1C2, Operands.L3) { // jflt
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg2.load32(machine.state));
                int a3 = arg3.load32(machine.state);
                if (a1 < a2) {
                    branch(machine.state, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x1C3, Operands.L3) { // jfle
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg2.load32(machine.state));
                int a3 = arg3.load32(machine.state);
                if (a1 <= a2) {
                    branch(machine.state, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x1C4, Operands.L3) { // jfgt
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg2.load32(machine.state));
                int a3 = arg3.load32(machine.state);
                if (a1 > a2) {
                    branch(machine.state, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x1C5, Operands.L3) { // jfge
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg2.load32(machine.state));
                int a3 = arg3.load32(machine.state);
                if (a1 >= a2) {
                    branch(machine.state, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x1C8, Operands.L2) { // jisnan
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                int a2 = arg2.load32(machine.state);
                if (Float.isNaN(a1)) {
                    branch(machine.state, a2);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x1C9, Operands.L2) { // jisinf
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                int a2 = arg2.load32(machine.state);
                if (Float.isInfinite(a1)) {
                    branch(machine.state, a2);
                }
                return Result.Tick;
            }
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

    private interface Operand {
        int load8(State state);
        int load16(State state);
        int load32(State state);
        void store8(State state, int value);
        void store16(State state, int value);
        void store32(State state, int value);
        int getDestType();
        int getDestAddr();
    }

    private static final Operand operand0 = new Operand() {
        @Override public int load8(State state) { return 0; }
        @Override public int load16(State state) { return 0; }
        @Override public int load32(State state) { return 0; }
        @Override public void store8(State state, int value) {}
        @Override public void store16(State state, int value) {}
        @Override public void store32(State state, int value) {}
        @Override public int getDestType() { return 0; }
        @Override public int getDestAddr() { return 0; }
    };

    private static class ConstantOperand implements Operand {
        private final int value;
        ConstantOperand(int value) {
            this.value = value;
        }
        @Override public int load8(State state) { return value; }
        @Override public int load16(State state) { return value; }
        @Override public int load32(State state) { return value; }
        @Override public void store8(State state, int value) {
            throw new IllegalArgumentException("Illegal store addressing mode");
        }
        @Override public void store16(State state, int value) {
            throw new IllegalArgumentException("Illegal store addressing mode");
        }
        @Override public void store32(State state, int value) {
            throw new IllegalArgumentException("Illegal store addressing mode");
        }
        @Override public int getDestType() {
            throw new IllegalArgumentException("Illegal store addressing mode");
        }
        @Override public int getDestAddr() {
            throw new IllegalArgumentException("Illegal store addressing mode");
        }
    };

    private static class MemoryOperand implements Operand {
        private final int addr;
        MemoryOperand(int addr) {
            this.addr = addr;
        }
        @Override public int load8(State state) {
            return state.load8(addr);
        }
        @Override public int load16(State state) {
            return state.load16(addr);
        }
        @Override public int load32(State state) {
            return state.load32(addr);
        }
        @Override public void store8(State state, int value) {
            state.store8(addr, value);
        }
        @Override public void store16(State state, int value) {
            state.store16(addr, value);
        }
        @Override public void store32(State state, int value) {
            state.store32(addr, value);
        }
        @Override public int getDestType() { return 1; }
        @Override public int getDestAddr() { return addr; }
    };

    private static final Operand operand8 = new Operand() {
        @Override public int load8(State state) {
            return state.pop32() & 255;
        }
        @Override public int load16(State state) {
            return state.pop32() & 65535;
        }
        @Override public int load32(State state) {
            return state.pop32();
        }
        @Override public void store8(State state, int value) {
            state.push32(value & 255);
        }
        @Override public void store16(State state, int value) {
            state.push32(value & 65535);
        }
        @Override public void store32(State state, int value) {
            state.push32(value);
        }
        @Override public int getDestType() { return 3; }
        @Override public int getDestAddr() { return 0; }
    };

    private static class LocalOperand implements Operand {
        private final int value;
        LocalOperand(int value) {
            this.value = value;
        }
        @Override public int load8(State state) {
            return 0;
        }
        @Override public int load16(State state) {
            return 0;
        }
        @Override public int load32(State state) {
            return 0;
        }
        @Override public void store8(State state, int value) {
        }
        @Override public void store16(State state, int value) {
        }
        @Override public void store32(State state, int value) {
        }
        @Override public int getDestType() { return 2; }
        @Override public int getDestAddr() { return value; }
    };

    private static Operand readOperand(Machine machine, int mode) {
        switch (mode) {
        case 0:
            return operand0;
        case 1:
            return new ConstantOperand(machine.state.advancePC8());
        case 2:
            return new ConstantOperand(machine.state.advancePC16());
        case 3:
            return new ConstantOperand(machine.state.advancePC32());
        case 4:
            throw new IllegalArgumentException("Unrecognized addressing mode");
        case 5:
            return new MemoryOperand(machine.state.advancePC8() & 255);
        case 6:
            return new MemoryOperand(machine.state.advancePC16() & 65535);
        case 7:
            return new MemoryOperand(machine.state.advancePC32());
        case 8:
            return operand8;
        case 9:
            return new LocalOperand(machine.state.advancePC8() & 255);
        case 10:
            return new LocalOperand(machine.state.advancePC16() & 65535);
        case 11:
            return new LocalOperand(machine.state.advancePC32());
        case 12:
            throw new IllegalArgumentException("Unrecognized addressing mode");
        case 13:
            return new MemoryOperand(machine.RAMStart + (machine.state.advancePC8() & 255));
        case 14:
            return new MemoryOperand(machine.RAMStart + (machine.state.advancePC16() & 65535));
        case 15:
            return new MemoryOperand(machine.RAMStart + machine.state.advancePC16());
        default:
            throw new AssertionError();
        }
    }

    private static void branch(State state, int offset) {
        switch (offset) {
        case 0: case 1:
            returnValue(state, offset);
            break;
        default:
            state.pc += offset - 2;
            break;
        }
    }

    private static void pushCallStub(State state, int destType, int destAddr) {
        state.push32(destType);
        state.push32(destAddr);
        state.push32(state.pc);
        state.push32(state.fp);
    }

    private static void call(State state, int addr, int[] args) {
    }

    private static void returnValue(State state, int value) {
    }
}
