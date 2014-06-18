package com.yrek.ifstd.glulx;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import com.yrek.ifstd.glk.GlkDispatchArgument;

abstract class Instruction {
    private static final boolean TRACE = false;

    private static final Instruction[] table = new Instruction[0x1ca];
    private final String name;
    private final Operands operands;

    Instruction(int opcode, String name, Operands operands) {
        table[opcode] = this;
        this.name = name;
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
        Continue, Tick, Quit;
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
        if (TRACE && Glulx.trace != null) {
            Glulx.trace.println();
            Glulx.trace.print(String.format("fp:%04x sp:%04x:",machine.state.fp,machine.state.sp));
            for (int i = machine.state.fp; i < machine.state.sp; i += 4) {
                Glulx.trace.print(String.format(" %x",machine.state.sload32(i)));
                if (i+4 == machine.state.fp + machine.state.sload32(machine.state.fp+4)) {
                    Glulx.trace.print(" L:");
                }
                if (i+4 == machine.state.fp + machine.state.sload32(machine.state.fp)) {
                    Glulx.trace.print(" S:");
                }
            }
            Glulx.trace.println();
            Glulx.trace.print(String.format("pc:%06x %04x %15s",machine.state.pc,opcode,insn==null?"null":insn.name));
        }
        switch (insn.operands) {
        case Z:
            return insn.execute(machine);
        case L: case S:
            int modes = machine.state.advancePC8();
            Operand arg1 = readOperand(machine, modes&15);
            return insn.execute(machine, arg1);
        case L2: case LS: case SL: case S2:
            modes = machine.state.advancePC8();
            int mode1 = modes&15;
            int mode2 = (modes>>4)&15;
            arg1 = readOperand(machine, mode1);
            Operand arg2 = readOperand(machine, mode2);
            return insn.execute(machine, arg1, arg2);
        case L3: case L2S:
            modes = machine.state.advancePC8();
            mode1 = modes&15;
            mode2 = (modes>>4)&15;
            modes = machine.state.advancePC8();
            int mode3 = modes&15;
            arg1 = readOperand(machine, mode1);
            arg2 = readOperand(machine, mode2);
            Operand arg3 = readOperand(machine, mode3);
            return insn.execute(machine, arg1, arg2, arg3);
        case L4: case L3S: case L2S2:
            modes = machine.state.advancePC8();
            mode1 = modes&15;
            mode2 = (modes>>4)&15;
            modes = machine.state.advancePC8();
            mode3 = modes&15;
            int mode4 = (modes>>4)&15;
            arg1 = readOperand(machine, mode1);
            arg2 = readOperand(machine, mode2);
            arg3 = readOperand(machine, mode3);
            Operand arg4 = readOperand(machine, mode4);
            return insn.execute(machine, arg1, arg2, arg3, arg4);
        case L4S:
            modes = machine.state.advancePC8();
            mode1 = modes&15;
            mode2 = (modes>>4)&15;
            modes = machine.state.advancePC8();
            mode3 = modes&15;
            mode4 = (modes>>4)&15;
            modes = machine.state.advancePC8();
            int mode5 = modes&15;
            arg1 = readOperand(machine, mode1);
            arg2 = readOperand(machine, mode2);
            arg3 = readOperand(machine, mode3);
            arg4 = readOperand(machine, mode4);
            Operand arg5 = readOperand(machine, mode5);
            return insn.execute(machine, arg1, arg2, arg3, arg4, arg5);
        case L6S:
            modes = machine.state.advancePC8();
            mode1 = modes&15;
            mode2 = (modes>>4)&15;
            modes = machine.state.advancePC8();
            mode3 = modes&15;
            mode4 = (modes>>4)&15;
            modes = machine.state.advancePC8();
            mode5 = modes&15;
            int mode6 = (modes>>4)&15;
            modes = machine.state.advancePC8();
            int mode7 = modes&15;
            arg1 = readOperand(machine, mode1);
            arg2 = readOperand(machine, mode2);
            arg3 = readOperand(machine, mode3);
            arg4 = readOperand(machine, mode4);
            arg5 = readOperand(machine, mode5);
            Operand arg6 = readOperand(machine, mode6);
            Operand arg7 = readOperand(machine, mode7);
            return insn.execute(machine, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        case L7S:
            modes = machine.state.advancePC8();
            mode1 = modes&15;
            mode2 = (modes>>4)&15;
            modes = machine.state.advancePC8();
            mode3 = modes&15;
            mode4 = (modes>>4)&15;
            modes = machine.state.advancePC8();
            mode5 = modes&15;
            mode6 = (modes>>4)&15;
            modes = machine.state.advancePC8();
            mode7 = modes&15;
            int mode8 = (modes>>4)&15;
            arg1 = readOperand(machine, mode1);
            arg2 = readOperand(machine, mode2);
            arg3 = readOperand(machine, mode3);
            arg4 = readOperand(machine, mode4);
            arg5 = readOperand(machine, mode5);
            arg6 = readOperand(machine, mode6);
            arg7 = readOperand(machine, mode7);
            Operand arg8 = readOperand(machine, mode8);
            return insn.execute(machine, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        }
        throw new AssertionError();
    }

    static {
        // arguments MUST be executed in order (due to stack addressing modes)
        new Instruction(0x00, "nop", Operands.Z) {
            @Override protected Result execute(Machine machine) {
                return Result.Continue;
            }
        };
        new Instruction(0x10, "add", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) + arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x11, "sub", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) - arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x12, "mul", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) * arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x13, "div", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) / arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x14, "mod", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) % arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x15, "neg", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, -arg1.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x18, "bitand", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) & arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x19, "bitor", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) | arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x1A, "bitxor", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, arg1.load32(machine.state) ^ arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x1B, "bitnot", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, ~arg1.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x1C, "shiftl", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                if (a2 < 0 || a2 >= 32) {
                    arg3.store32(machine.state, 0);
                } else {
                    arg3.store32(machine.state, a1 << a2);
                }
                return Result.Continue;
            }
        };
        new Instruction(0x1D, "sshiftr", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                if (a2 < 0 || a2 >= 32) {
                    if (a1 >= 0) {
                        arg3.store32(machine.state, 0);
                    } else {
                        arg3.store32(machine.state, -1);
                    }
                } else {
                    arg3.store32(machine.state, a1 >> a2);
                }
                return Result.Continue;
            }
        };
        new Instruction(0x1E, "ushiftr", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                if (a2 < 0 || a2 >= 32) {
                    arg3.store32(machine.state, 0);
                } else {
                    arg3.store32(machine.state, a1 >>> a2);
                }
                return Result.Continue;
            }
        };
        new Instruction(0x20, "jump", Operands.L) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                return branch(machine, arg1.load32(machine.state));
            }
        };
        new Instruction(0x22, "jz", Operands.L2) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                if (a1 == 0) {
                    return branch(machine, a2);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x23, "jnz", Operands.L2) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                if (a1 != 0) {
                    return branch(machine, a2);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x24, "jeq", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int a3 = arg3.load32(machine.state);
                if (a1 == a2) {
                    return branch(machine, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x25, "jne", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int a3 = arg3.load32(machine.state);
                if (a1 != a2) {
                    return branch(machine, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x26, "jlt", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int a3 = arg3.load32(machine.state);
                if (a1 < a2) {
                    return branch(machine, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x27, "jge", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int a3 = arg3.load32(machine.state);
                if (a1 >= a2) {
                    return branch(machine, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x28, "jgt", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int a3 = arg3.load32(machine.state);
                if (a1 > a2) {
                    return branch(machine, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x29, "jle", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int a3 = arg3.load32(machine.state);
                if (a1 <= a2) {
                    return branch(machine, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x2A, "jltu", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                long a1 = arg1.load32(machine.state) & 0xffffffffL;
                long a2 = arg2.load32(machine.state) & 0xffffffffL;
                int a3 = arg3.load32(machine.state);
                if (a1 < a2) {
                    return branch(machine, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x2B, "jgeu", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                long a1 = arg1.load32(machine.state) & 0xffffffffL;
                long a2 = arg2.load32(machine.state) & 0xffffffffL;
                int a3 = arg3.load32(machine.state);
                if (a1 >= a2) {
                    return branch(machine, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x2C, "jgtu", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                long a1 = arg1.load32(machine.state) & 0xffffffffL;
                long a2 = arg2.load32(machine.state) & 0xffffffffL;
                int a3 = arg3.load32(machine.state);
                if (a1 > a2) {
                    return branch(machine, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x2D, "jleu", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                long a1 = arg1.load32(machine.state) & 0xffffffffL;
                long a2 = arg2.load32(machine.state) & 0xffffffffL;
                int a3 = arg3.load32(machine.state);
                if (a1 <= a2) {
                    return branch(machine, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x30, "call", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int[] args = new int[a2];
                for (int i = 0; i < a2; i++) {
                    args[i] = machine.state.pop32();
                }
                pushCallStub(machine.state, arg3.getDestType(), arg3.getDestAddr());
                call(machine.state, a1, args);
                return Result.Tick;
            }
        };
        new Instruction(0x31, "return", Operands.L) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                if (machine.state.fp == 0) {
                    return Result.Quit;
                }
                int a1 = arg1.load32(machine.state);
                machine.state.sp = machine.state.fp;
                returnValue(machine, a1);
                return Result.Tick;
            }
        };
        new Instruction(0x32, "catch", Operands.SL) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                // The operand evaluation order is NOT in order in this case:
                // 1. load(branch offset)
                // 2. push call stub
                // 3. store(catch token)
                int a2 = arg2.load32(machine.state);
                pushCallStub(machine.state, arg1.getDestType(), arg1.getDestAddr());
                arg1.store32(machine.state, machine.state.sp);
                return branch(machine, a2);
            }
        };
        new Instruction(0x33, "throw", Operands.L2) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                machine.state.sp = arg2.load32(machine.state);
                returnValue(machine, a1);
                return Result.Tick;
            }
        };
        new Instruction(0x34, "tailcall", Operands.L2) {
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
        new Instruction(0x40, "copy", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                arg2.store32(machine.state, a1);
                return Result.Continue;
            }
        };
        new Instruction(0x41, "copys", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store16(machine.state, arg1.load16(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x42, "copyb", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store8(machine.state, arg1.load8(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x44, "sexs", Operands.LS) {
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
        new Instruction(0x45, "sexb", Operands.LS) {
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
        new Instruction(0x48, "aload", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, machine.state.load32(arg1.load32(machine.state) + 4*arg2.load32(machine.state)));
                return Result.Continue;
            }
        };
        new Instruction(0x49, "aloads", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, machine.state.load16(arg1.load32(machine.state) + 2*arg2.load32(machine.state)) & 65535);
                return Result.Continue;
            }
        };
        new Instruction(0x4A, "aloadb", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, machine.state.load8(arg1.load32(machine.state) + arg2.load32(machine.state)) & 255);
                return Result.Continue;
            }
        };
        new Instruction(0x4B, "aloadbit", Operands.L2S) {
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
                arg3.store32(machine.state, (machine.state.load8(a1) >> a2) & 1);
                return Result.Continue;
            }
        };
        new Instruction(0x4C, "astore", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                machine.state.store32(arg1.load32(machine.state) + 4*arg2.load32(machine.state), arg3.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x4D, "astores", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                machine.state.store16(arg1.load32(machine.state) + 2*arg2.load32(machine.state), arg3.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x4E, "astoreb", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                machine.state.store8(arg1.load32(machine.state) + arg2.load32(machine.state), arg3.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x4F, "astorebit", Operands.L3) {
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
        new Instruction(0x50, "stkcount", Operands.S) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                arg1.store32(machine.state, (machine.state.sp - machine.state.fp - machine.state.sload32(machine.state.fp))/4);
                return Result.Continue;
            }
        };
        new Instruction(0x51, "stkpeek", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                arg2.store32(machine.state, machine.state.sload32(machine.state.sp - 4 - 4*a1));
                return Result.Continue;
            }
        };
        new Instruction(0x52, "stkswap", Operands.Z) {
            @Override protected Result execute(Machine machine) {
                machine.state.roll(2, 1);
                return Result.Continue;
            }
        };
        new Instruction(0x53, "stkroll", Operands.L2) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                machine.state.roll(arg1.load32(machine.state), arg2.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x54, "stkcopy", Operands.L) {
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
        new Instruction(0x70, "streamchar", Operands.L) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                machine.ioSys.streamChar(machine, arg1.load32(machine.state)&255);
                return Result.Continue;
            }
        };
        new Instruction(0x71, "streamnum", Operands.L) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                machine.ioSys.streamNum(machine, arg1.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x72, "streamstr", Operands.L) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                machine.ioSys.streamStringObject(machine, arg1.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x73, "streamunichar", Operands.L) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                machine.ioSys.streamUnichar(machine, arg1.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x100, "gestalt", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                switch (a1) {
                case Gestalt.GlulxVersion:
                    arg3.store32(machine.state, Glulx.GlulxVersion);
                    break;
                case Gestalt.TerpVersion:
                    arg3.store32(machine.state, Glulx.TerpVersion);
                    break;
                case Gestalt.ResizeMem:
                    arg3.store32(machine.state, 0);
                    break;
                case Gestalt.Undo:
                    arg3.store32(machine.state, 1);
                    break;
                case Gestalt.IOSystem:
                    switch (a2) {
                    case Gestalt.IOSystem_null:
                    case Gestalt.IOSystem_filter:
                    case Gestalt.IOSystem_Glk:
                        arg3.store32(machine.state, 1);
                        break;
                    default:
                        arg3.store32(machine.state, 0);
                        break;
                    }
                    break;
                case Gestalt.Unicode:
                    arg3.store32(machine.state, 1);
                    break;
                case Gestalt.MemCopy:
                    arg3.store32(machine.state, 1);
                    break;
                case Gestalt.MAlloc:
                    arg3.store32(machine.state, 0);
                    break;
                case Gestalt.MAllocHeap:
                    arg3.store32(machine.state, 0);
                    break;
                case Gestalt.Acceleration:
                    arg3.store32(machine.state, 1);
                    break;
                case Gestalt.AccelFunc:
                    arg3.store32(machine.state, 0);
                    break;
                case Gestalt.Float:
                    arg3.store32(machine.state, 1);
                    break;
                default:
                    arg3.store32(machine.state, 0);
                    break;
                }
                return Result.Continue;
            }
        };
        new Instruction(0x101, "debugtrap", Operands.L) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                throw new IllegalArgumentException("debugtrap instruction");
            }
        };
        new Instruction(0x102, "getmemsize", Operands.S) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                arg1.store32(machine.state, machine.state.memorySize());
                return Result.Continue;
            }
        };
        new Instruction(0x103, "setmemsize", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, 1);
                return Result.Continue;
            }
        };
        new Instruction(0x104, "jumpabs", Operands.L) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                machine.state.pc = arg1.load32(machine.state);
                return Result.Tick;
            }
        };
        new Instruction(0x110, "random", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                if (a1 == 0) {
                    arg2.store32(machine.state, machine.random.nextInt());
                } else if (a1 > 0) {
                    arg2.store32(machine.state, machine.random.nextInt(a1));
                } else {
                    arg2.store32(machine.state, -machine.random.nextInt(-a1));
                }
                return Result.Continue;
            }
        };
        new Instruction(0x111, "setrandom", Operands.L) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                int a1 = arg1.load32(machine.state);
                if (a1 == 0) {
                    machine.random.setSeed(System.nanoTime());
                } else {
                    machine.random.setSeed((long) a1);
                }
                return Result.Continue;
            }
        };
        new Instruction(0x120, "quit", Operands.Z) {
            @Override protected Result execute(Machine machine) {
                return Result.Quit;
            }
        };
        new Instruction(0x121, "verify", Operands.S) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                arg1.store32(machine.state, 0);
                return Result.Continue;
            }
        };
        new Instruction(0x122, "restart", Operands.Z) {
            @Override protected Result execute(Machine machine) {
                try {
                    machine.state.readFile(machine.getData(), machine.protectStart, machine.protectLength);
                } catch (Exception e) {
                    throw new AssertionError(e);
                }
                return Result.Continue;
            }
        };
        new Instruction(0x123, "save", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                pushCallStub(machine.state, arg2.getDestType(), arg2.getDestAddr());
                throw new RuntimeException("unimplemented");
            }
        };
        new Instruction(0x124, "restore", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                throw new RuntimeException("unimplemented");
            }
        };
        new Instruction(0x125, "saveundo", Operands.S) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                machine.saveUndo = machine.state.copyTo(machine.saveUndo);
                arg1.store32(machine.state, 0);
                arg1.store32(machine.saveUndo, -1);
                return Result.Tick;
            }
        };
        new Instruction(0x126, "restoreundo", Operands.S) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                if (machine.saveUndo == null) {
                    arg1.store32(machine.state, 1);
                } else {
                    machine.state.copyFrom(machine.saveUndo, machine.protectStart, machine.protectLength);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x127, "protect", Operands.L2) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                machine.protectStart = arg1.load32(machine.state);
                machine.protectLength = arg2.load32(machine.state);
                return Result.Continue;
            }
        };
        new Instruction(0x130, "glk", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                GlkDispatchArgument[] args = new GlkDispatchArgument[a2];
                for (int i = 0; i < a2; i++) {
                    args[i] = new GlkArgument(machine, machine.state.pop32());
                }
                try {
                    arg3.store32(machine.state, machine.glk.dispatch(a1, args));
                    return Result.Continue;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        new Instruction(0x140, "getstringtbl", Operands.S) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                arg1.store32(machine.state, machine.stringTable.table);
                return Result.Continue;
            }
        };
        new Instruction(0x141, "setstringtbl", Operands.L) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                machine.stringTable = StringTable.create(machine.state, arg1.load32(machine.state));
                return Result.Continue;
            }
        };
        new Instruction(0x148, "getiosys", Operands.S2) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg1.store32(machine.state, machine.ioSys.mode);
                arg2.store32(machine.state, machine.ioSys.rock);
                return Result.Continue;
            }
        };
        new Instruction(0x149, "setiosys", Operands.L2) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                switch (a1) {
                case 0: machine.ioSys = new NullIOSys(a2); break;
                case 1: machine.ioSys = new FilterIOSys(a2); break;
                case 2: machine.ioSys = new GlkIOSys(machine.glk, a2); break;
                default: machine.ioSys = new NullIOSys(a2); break;
                }
                return Result.Continue;
            }
        };
        new Instruction(0x150, "linearsearch", Operands.L7S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4, Operand arg5, Operand arg6, Operand arg7, Operand arg8) {
                int key = arg1.load32(machine.state);
                int keySize = arg2.load32(machine.state);
                int start = arg3.load32(machine.state);
                int structSize = arg4.load32(machine.state);
                int numStructs = arg5.load32(machine.state);
                int keyOffset = arg6.load32(machine.state);
                int options = arg7.load32(machine.state);
                byte[] searchKey = searchKey(machine.state, key, keySize, options);
                boolean returnIndex = (options & 4) != 0;
                boolean zeroKeyTerminates = (options & 2) != 0;
                for (int i = 0; numStructs == -1 || i < numStructs; i++) {
                    if (searchKeyCompare(machine.state, start + i*structSize + keyOffset, searchKey) == 0) {
                        arg8.store32(machine.state,  returnIndex ? i : start + i*structSize);
                        return Result.Continue;
                    } else if (zeroKeyTerminates && searchKeyZero(machine.state, start + i*structSize + keyOffset, keySize)) {
                        break;
                    }
                }
                arg8.store32(machine.state,  returnIndex ? -1 : 0);
                return Result.Continue;
            }
        };
        new Instruction(0x151, "binarysearch", Operands.L7S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4, Operand arg5, Operand arg6, Operand arg7, Operand arg8) {
                int key = arg1.load32(machine.state);
                int keySize = arg2.load32(machine.state);
                int start = arg3.load32(machine.state);
                int structSize = arg4.load32(machine.state);
                int numStructs = arg5.load32(machine.state);
                int keyOffset = arg6.load32(machine.state);
                int options = arg7.load32(machine.state);
                byte[] searchKey = searchKey(machine.state, key, keySize, options);
                boolean returnIndex = (options & 4) != 0;
                int lo = 0;
                int hi = numStructs;
                for (;;) {
                    int i = (lo + hi) / 2;
                    int c = searchKeyCompare(machine.state, start + i*structSize + keyOffset, searchKey);
                    if (c == 0) {
                        arg8.store32(machine.state,  returnIndex ? i : start + i*structSize);
                        return Result.Continue;
                    }
                    if (c < 0 && i != hi) {
                        hi = i;
                    } else if (c > 0 && i != lo) {
                        lo = i;
                    } else {
                        arg8.store32(machine.state,  returnIndex ? -1 : 0);
                        return Result.Continue;
                    }
                }
            }
        };
        new Instruction(0x152, "linkedsearch", Operands.L6S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4, Operand arg5, Operand arg6, Operand arg7) {
                int key = arg1.load32(machine.state);
                int keySize = arg2.load32(machine.state);
                int start = arg3.load32(machine.state);
                int keyOffset = arg4.load32(machine.state);
                int nextOffset = arg5.load32(machine.state);
                int options = arg6.load32(machine.state);
                byte[] searchKey = searchKey(machine.state, key, keySize, options);
                boolean zeroKeyTerminates = (options & 2) != 0;
                for (;;) {
                    if (start == 0) {
                        arg7.store32(machine.state, 0);
                        return Result.Continue;
                    } else if (searchKeyCompare(machine.state, start + keyOffset, searchKey) == 0) {
                        arg7.store32(machine.state, start);
                        return Result.Continue;
                    } else if (zeroKeyTerminates && searchKeyZero(machine.state, start + keyOffset, keySize)) {
                        arg7.store32(machine.state, 0);
                        return Result.Continue;
                    }
                    start = machine.state.load32(start + nextOffset);
                }
            }
        };
        new Instruction(0x160, "callf", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                int[] args = new int[0];
                pushCallStub(machine.state, arg2.getDestType(), arg2.getDestAddr());
                call(machine.state, a1, args);
                return Result.Tick;
            }
        };
        new Instruction(0x161, "callfi", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int[] args = new int[] {
                    arg2.load32(machine.state),
                };
                pushCallStub(machine.state, arg3.getDestType(), arg3.getDestAddr());
                call(machine.state, a1, args);
                return Result.Tick;
            }
        };
        new Instruction(0x162, "callfii", Operands.L3S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4) {
                int a1 = arg1.load32(machine.state);
                int[] args = new int[] {
                    arg2.load32(machine.state),
                    arg3.load32(machine.state),
                };
                pushCallStub(machine.state, arg4.getDestType(), arg4.getDestAddr());
                call(machine.state, a1, args);
                return Result.Tick;
            }
        };
        new Instruction(0x163, "callfiii", Operands.L4S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4, Operand arg5) {
                int a1 = arg1.load32(machine.state);
                int[] args = new int[] {
                    arg2.load32(machine.state),
                    arg3.load32(machine.state),
                    arg4.load32(machine.state),
                };
                pushCallStub(machine.state, arg5.getDestType(), arg5.getDestAddr());
                call(machine.state, a1, args);
                return Result.Tick;
            }
        };
        new Instruction(0x170, "mzero", Operands.L2) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                Arrays.fill(machine.state.memory, a2, a1 + a2, (byte) 0);
                return Result.Continue;
            }
        };
        new Instruction(0x171, "mcopy", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                int a1 = arg1.load32(machine.state);
                int a2 = arg2.load32(machine.state);
                int a3 = arg3.load32(machine.state);
                System.arraycopy(machine.state.memory, a2, machine.state.memory, a3, a1);
                return Result.Continue;
            }
        };
        new Instruction(0x178, "malloc", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, 0);
                return Result.Continue;
            }
        };
        new Instruction(0x179, "mfree", Operands.L) {
            @Override protected Result execute(Machine machine, Operand arg1) {
                arg1.store32(machine.state, 0);
                return Result.Continue;
            }
        };
        new Instruction(0x180, "accelfunc", Operands.L2) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg1.load32(machine.state);
                arg2.load32(machine.state);
                return Result.Continue;
            }
        };
        new Instruction(0x181, "accelparam", Operands.L2) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg1.load32(machine.state);
                arg2.load32(machine.state);
                return Result.Continue;
            }
        };
        new Instruction(0x190, "numtof", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) arg1.load32(machine.state)));
                return Result.Continue;
            }
        };
        new Instruction(0x191, "ftonumz", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                float arg = Float.intBitsToFloat(a1);
                int a2;
                if (Float.isNaN(arg)) {
                    a2 = (a1 & 0x80000000) == 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                } else if (arg < 0.0f) {
                    a2 = Math.round((float) Math.ceil(arg));
                } else {
                    a2 = Math.round((float) Math.floor(arg));
                }
                arg2.store32(machine.state, a2);
                return Result.Continue;
            }
        };
        new Instruction(0x192, "ftonumn", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                int a1 = arg1.load32(machine.state);
                float arg = Float.intBitsToFloat(a1);
                int a2;
                if (Float.isNaN(arg)) {
                    a2 = (a1 & 0x80000000) == 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                } else {
                    a2 = Math.round(arg);
                }
                arg2.store32(machine.state, a2);
                return Result.Continue;
            }
        };
        new Instruction(0x198, "ceil", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.ceil((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x199, "floor", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.floor((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1A0, "fadd", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, Float.floatToIntBits(Float.intBitsToFloat(arg1.load32(machine.state)) + Float.intBitsToFloat(arg2.load32(machine.state))));
                return Result.Continue;
            }
        };
        new Instruction(0x1A1, "fsub", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, Float.floatToIntBits(Float.intBitsToFloat(arg1.load32(machine.state)) - Float.intBitsToFloat(arg2.load32(machine.state))));
                return Result.Continue;
            }
        };
        new Instruction(0x1A2, "fmul", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, Float.floatToIntBits(Float.intBitsToFloat(arg1.load32(machine.state)) * Float.intBitsToFloat(arg2.load32(machine.state))));
                return Result.Continue;
            }
        };
        new Instruction(0x1A3, "fdiv", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, Float.floatToIntBits(Float.intBitsToFloat(arg1.load32(machine.state)) / Float.intBitsToFloat(arg2.load32(machine.state))));
                return Result.Continue;
            }
        };
        new Instruction(0x1A4, "fmod", Operands.L2S2) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg2.load32(machine.state));
                float r = a1 % a2;
                float q = Math.copySign(Math.abs(a1 - r)/Math.abs(a2), Math.signum(a1)*Math.signum(a2));
                arg3.store32(machine.state, Float.floatToIntBits(r));
                arg4.store32(machine.state, Float.floatToIntBits(q));
                return Result.Continue;
            }
        };
        new Instruction(0x1A8, "sqrt", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.sqrt((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1A9, "exp", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.exp((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1AA, "log", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.log((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1AB, "pow", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg2.load32(machine.state));
                float a3;
                if (a1 == 1.0f || (a1 == -1.0f && Float.isInfinite(a2))) {
                    a3 = 1.0f;
                } else {
                    a3 = (float) Math.pow((double) a1, (double) a2);
                }
                arg3.store32(machine.state, Float.floatToIntBits(a3));
                return Result.Continue;
            }
        };
        new Instruction(0x1B0, "sin", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.sin((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1B1, "cos", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.cos((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1B2, "tan", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.tan((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1B3, "asin", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.asin((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1B4, "acos", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.acos((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1B5, "atan", Operands.LS) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                arg2.store32(machine.state, Float.floatToIntBits((float) Math.atan((double) Float.intBitsToFloat(arg1.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1B6, "atan2", Operands.L2S) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                arg3.store32(machine.state, Float.floatToIntBits((float) Math.atan2((double) Float.intBitsToFloat(arg1.load32(machine.state)), (double) Float.intBitsToFloat(arg2.load32(machine.state)))));
                return Result.Continue;
            }
        };
        new Instruction(0x1C0, "jfeq", Operands.L4) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg2.load32(machine.state));
                float a3 = Float.intBitsToFloat(arg3.load32(machine.state));
                int a4 = arg4.load32(machine.state);
                if (Float.isNaN(a1) || Float.isNaN(a2) || Float.isNaN(a3)) {
                    return Result.Tick;
                } else if (Float.isInfinite(a3)) {
                    if (Float.isInfinite(a1) && Float.isInfinite(a2) && Math.signum(a1)*Math.signum(a2) < 0.0f) {
                        return Result.Tick;
                    } else {
                        return branch(machine, a4);
                    }
                } else if (Float.isInfinite(a1) && Float.isInfinite(a2)) {
                    if (Math.signum(a1)*Math.signum(a2) < 0.0f) {
                        return Result.Tick;
                    } else {
                        return branch(machine, a4);
                    }
                } else if (Math.abs(a1 - a2) <= Math.abs(a3)) {
                    return branch(machine, a4);
                } else {
                    return Result.Tick;
                }
            }
        };
        new Instruction(0x1C1, "jfne", Operands.L4) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3, Operand arg4) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg2.load32(machine.state));
                float a3 = Float.intBitsToFloat(arg3.load32(machine.state));
                int a4 = arg4.load32(machine.state);
                if (Float.isNaN(a1) || Float.isNaN(a2) || Float.isNaN(a3)) {
                    return branch(machine, a4);
                } else if (Float.isInfinite(a3)) {
                    if (Float.isInfinite(a1) && Float.isInfinite(a2) && Math.signum(a1)*Math.signum(a2) < 0.0f) {
                        return branch(machine, a4);
                    } else {
                        return Result.Tick;
                    }
                } else if (Float.isInfinite(a1) && Float.isInfinite(a2)) {
                    if (Math.signum(a1)*Math.signum(a2) < 0.0f) {
                        return branch(machine, a4);
                    } else {
                        return Result.Tick;
                    }
                } else if (Math.abs(a1 - a2) <= Math.abs(a3)) {
                    return Result.Tick;
                } else {
                    return branch(machine, a4);
                }
            }
        };
        new Instruction(0x1C2, "jflt", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg2.load32(machine.state));
                int a3 = arg3.load32(machine.state);
                if (a1 < a2) {
                    return branch(machine, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x1C3, "jfle", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg2.load32(machine.state));
                int a3 = arg3.load32(machine.state);
                if (a1 <= a2) {
                    return branch(machine, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x1C4, "jfgt", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg2.load32(machine.state));
                int a3 = arg3.load32(machine.state);
                if (a1 > a2) {
                    return branch(machine, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x1C5, "jfge", Operands.L3) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2, Operand arg3) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                float a2 = Float.intBitsToFloat(arg2.load32(machine.state));
                int a3 = arg3.load32(machine.state);
                if (a1 >= a2) {
                    return branch(machine, a3);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x1C8, "jisnan", Operands.L2) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                int a2 = arg2.load32(machine.state);
                if (Float.isNaN(a1)) {
                    return branch(machine, a2);
                }
                return Result.Tick;
            }
        };
        new Instruction(0x1C9, "jisinf", Operands.L2) {
            @Override protected Result execute(Machine machine, Operand arg1, Operand arg2) {
                float a1 = Float.intBitsToFloat(arg1.load32(machine.state));
                int a2 = arg2.load32(machine.state);
                if (Float.isInfinite(a1)) {
                    return branch(machine, a2);
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
        @Override public String toString() { return "zero"; }
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
        @Override public String toString() { return Integer.toHexString(value); }
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
        @Override public String toString() { return "M:"+Integer.toHexString(addr); }
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
        @Override public String toString() { return "stack"; }
    };

    private static class LocalOperand implements Operand {
        private final int addr;
        LocalOperand(int addr) {
            this.addr = addr;
        }
        @Override public int load8(State state) {
            int localsPos = state.sload32(state.fp + 4);
            return state.sload8(state.fp + localsPos + addr);
        }
        @Override public int load16(State state) {
            int localsPos = state.sload32(state.fp + 4);
            return state.sload16(state.fp + localsPos + addr);
        }
        @Override public int load32(State state) {
            int localsPos = state.sload32(state.fp + 4);
            return state.sload32(state.fp + localsPos + addr);
        }
        @Override public void store8(State state, int value) {
            int localsPos = state.sload32(state.fp + 4);
            state.sstore8(state.fp + localsPos + addr, value);
        }
        @Override public void store16(State state, int value) {
            int localsPos = state.sload32(state.fp + 4);
            state.sstore16(state.fp + localsPos + addr, value);
        }
        @Override public void store32(State state, int value) {
            int localsPos = state.sload32(state.fp + 4);
            state.sstore32(state.fp + localsPos + addr, value);
        }
        @Override public int getDestType() { return 2; }
        @Override public int getDestAddr() { return addr; }
        @Override public String toString() { return "L:"+Integer.toHexString(addr); }
    };

    private static Operand readOperand(Machine machine, int mode) {
        Operand result;
        switch (mode) {
        case 0:
            result = operand0;
            break;
        case 1:
            result = new ConstantOperand(machine.state.advancePC8());
            break;
        case 2:
            result = new ConstantOperand(machine.state.advancePC16());
            break;
        case 3:
            result = new ConstantOperand(machine.state.advancePC32());
            break;
        case 4:
            throw new IllegalArgumentException("Unrecognized addressing mode");
        case 5:
            result = new MemoryOperand(machine.state.advancePC8() & 255);
            break;
        case 6:
            result = new MemoryOperand(machine.state.advancePC16() & 65535);
            break;
        case 7:
            result = new MemoryOperand(machine.state.advancePC32());
            break;
        case 8:
            result = operand8;
            break;
        case 9:
            result = new LocalOperand(machine.state.advancePC8() & 255);
            break;
        case 10:
            result = new LocalOperand(machine.state.advancePC16() & 65535);
            break;
        case 11:
            result = new LocalOperand(machine.state.advancePC32());
            break;
        case 12:
            throw new IllegalArgumentException("Unrecognized addressing mode");
        case 13:
            result = new MemoryOperand(machine.state.load32(8) + (machine.state.advancePC8() & 255));
            break;
        case 14:
            result = new MemoryOperand(machine.state.load32(8) + (machine.state.advancePC16() & 65535));
            break;
        case 15:
            result = new MemoryOperand(machine.state.load32(8) + machine.state.advancePC16());
            break;
        default:
            throw new AssertionError();
        }
        if (TRACE && Glulx.trace != null) {
            Glulx.trace.print(" "+result);
        }
        return result;
    }

    private static Result branch(Machine machine, int offset) {
        switch (offset) {
        case 0: case 1:
            if (machine.state.fp == 0) {
                return Result.Quit;
            }
            machine.state.sp = machine.state.fp;
            returnValue(machine, offset);
            return Result.Tick;
        default:
            if (TRACE && Glulx.trace != null) {
                Glulx.trace.print(" branch:"+(offset-2));
            }
            machine.state.pc += offset - 2;
            return Result.Tick;
        }
    }

    static void pushCallStub(State state, int destType, int destAddr) {
        if (TRACE && Glulx.trace != null) {
            Glulx.trace.println();
            Glulx.trace.print(String.format("call stub:%08x %08x pc=%08x fp=%08x sp=%08x",destType,destAddr, state.pc, state.fp,state.sp));
        }
        state.push32(destType);
        state.push32(destAddr);
        state.push32(state.pc);
        state.push32(state.fp);
    }

    static void call(State state, int addr, int[] args) {
        state.fp = state.sp;
        boolean pushArgs;
        switch (state.load8(addr) & 255) {
        case 0xc0: pushArgs = true; break;
        case 0xc1: pushArgs = false; break;
        default: throw new IllegalArgumentException("Invalid call target");
        }
        int localsPos = 8;
        int localsSize = 0;
        loop:
        for (int i = 1;; i += 2) {
            int localType = state.load8(addr + i) & 255;
            int localCount = state.load8(addr + i + 1) & 255;
            localsPos += 2;
            switch (localType) {
            case 1: case 2: case 4:
                localsSize = align(localsSize, localType);
                localsSize += localType * localCount;
                break;
            case 0:
                if (localCount != 0) {
                    throw new IllegalArgumentException("Invalid locals format");
                }
                state.pc = addr + i + 2;
                break loop;
            default:
                throw new IllegalArgumentException("Invalid locals format");
            }
        }
        localsPos = align(localsPos, 4);
        localsSize = align(localsSize, 4);
        if (TRACE && Glulx.trace != null) {
            Glulx.trace.println();
            Glulx.trace.print(String.format("function %08x %s args localsSize:%d",addr,pushArgs?"stack":"local",localsSize));
        }
        state.push32(localsPos + localsSize);
        state.push32(localsPos);
        assert localsPos >= 12;
        for (int i = 0; i < localsPos - 12; i += 4) {
            state.push32(state.load32(addr + 1 + i));
        }
        state.push32(state.load32(addr + 1 + localsPos - 12) & 0xffff0000);
        int argIndex = 0;
        int index = 0;
        int local = 0;
        loop2:
        for (int i = 1;; i += 2) {
            int localType = state.load8(addr + i) & 255;
            int localCount = state.load8(addr + 1 + i) & 255;
            switch (localType) {
            case 1:
                for (int j = 0; j < localCount; j++) {
                    switch (index%4) {
                    case 0:
                        if (!pushArgs && argIndex < args.length) {
                            local = args[argIndex] << 24;
                            argIndex++;
                        } else {
                            local = 0;
                        }
                        break;
                    case 1: case 2:
                        if (!pushArgs && argIndex < args.length) {
                            local |= (args[argIndex] & 255) << (24 - 8*(index%4));
                            argIndex++;
                        }
                        break;
                    case 3:
                        if (!pushArgs && argIndex < args.length) {
                            local |= args[argIndex] & 255;
                            argIndex++;
                        }
                        state.push32(local);
                        break;
                    default:
                        throw new AssertionError();
                    }
                    index++;
                }
                break;
            case 2:
                if (index%4 == 3) {
                    state.push32(local);
                }
                if (index%2 == 1) {
                    index++;
                }
                for (int j = 0; j < localCount; j++) {
                    switch (index%4) {
                    case 0:
                        if (!pushArgs && argIndex < args.length) {
                            local = args[argIndex] << 16;
                            argIndex++;
                        } else {
                            local = 0;
                        }
                        break;
                    case 2:
                        if (!pushArgs && argIndex < args.length) {
                            local |= args[argIndex] & 65535;
                            argIndex++;
                        }
                        state.push32(local);
                        break;
                    default:
                        throw new AssertionError();
                    }
                    index += 2;
                }
                break;
            case 4:
                if (index%4 != 0) {
                    state.push32(local);
                    while (index%4 != 0) {
                        index++;
                    }
                }
                for (int j = 0; j < localCount; j++) {
                    if (!pushArgs && argIndex < args.length) {
                        state.push32(args[argIndex]);
                        argIndex++;
                    } else {
                        state.push32(0);
                    }
                    index += 4;
                }
                break;
            case 0:
                if (index%4 != 0) {
                    state.push32(local);
                    while (index%4 != 0) {
                        index++;
                    }
                }
                assert index == localsSize;
                break loop2;
            default:
                throw new AssertionError();
            }
        }
        if (pushArgs) {
            for (int i = args.length - 1; i >= 0; i--) {
                state.push32(args[i]);
            }
            state.push32(args.length);
        }
    }

    static void returnValue(Machine machine, int value) {
        for (;;) {
            int fp = machine.state.sload32(machine.state.sp-4);
            int pc = machine.state.sload32(machine.state.sp-8);
            int destAddr = machine.state.sload32(machine.state.sp-12);
            int destType = machine.state.sload32(machine.state.sp-16);
            machine.state.sp -= 16;
            if (TRACE && Glulx.trace != null) {
                Glulx.trace.println();
                Glulx.trace.print(String.format("return %x fp:%04x sp:%04x pc:%08x destAddr:%08x destType:%x", value, fp, machine.state.sp, pc, destAddr, destType));
            }
            switch (destType) {
            case 0:
                machine.state.fp = fp;
                machine.state.pc = pc;
                return;
            case 1:
                machine.state.fp = fp;
                machine.state.pc = pc;
                machine.state.store32(destAddr, value);
                return;
            case 2:
                machine.state.fp = fp;
                machine.state.pc = pc;
                int localsPos = machine.state.sload32(fp + 4);
                machine.state.sstore32(fp + localsPos + destAddr, value);
                return;
            case 3:
                machine.state.fp = fp;
                machine.state.pc = pc;
                machine.state.push32(value);
                return;
            case 10:
                machine.ioSys.resumePrintCompressed(machine, pc, destAddr);
                break;
            case 11:
                machine.state.fp = fp;
                machine.state.pc = pc;
                return;
            case 12:
                machine.ioSys.resumePrintNumber(machine, pc, destAddr);
                break;
            case 13:
                machine.ioSys.resumePrint(machine, pc);
                break;
            case 14:
                machine.ioSys.resumePrintUnicode(machine, pc);
                break;
            default:
                throw new IllegalArgumentException("stack corruption");
            }
        }
    }

    private static byte[] searchKey(State state, int key, int keySize, int options) {
        byte[] searchKey = new byte[keySize];
        if ((options & 1) == 0) {
            switch (keySize) {
            case 1: searchKey[0] = (byte) key; break;
            case 2: State.store16(searchKey, 0, key); break;
            case 4: State.store32(searchKey, 0, key); break;
            default: throw new IllegalArgumentException("invalid search KeySize");
            }
        } else {
            for (int i = 0; i < keySize; i++) {
                searchKey[i] = (byte) state.load8(key + i);
            }
        }
        return searchKey;
    }

    private static int searchKeyCompare(State state, int addr, byte[] searchKey) {
        for (int i = 0; i < searchKey.length; i++) {
            int c = (searchKey[i] & 255) - (state.load8(addr + i) & 255);
            if (c != 0) {
                return c;
            }
        }
        return 0;
    }

    private static boolean searchKeyZero(State state, int addr, int keySize) {
        for (int i = 0; i < keySize; i++) {
            if (state.load8(addr + i) != 0) {
                return false;
            }
        }
        return true;
    }

    private static int align(int n, int alignment) {
        while (n % alignment != 0) {
            n++;
        }
        return n;
    }
}
