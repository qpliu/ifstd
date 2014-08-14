package com.yrek.ifstd.zcode;

import java.io.IOException;

import com.yrek.ifstd.glk.GlkEvent;
import com.yrek.ifstd.glk.GlkFile;
import com.yrek.ifstd.glk.GlkGestalt;
import com.yrek.ifstd.glk.GlkStream;
import com.yrek.ifstd.glk.GlkWindow;
import com.yrek.ifstd.glk.GlkWindowArrangement;
import com.yrek.ifstd.glk.GlkWindowSize;
import com.yrek.ifstd.glk.UnicodeString;

abstract class Instruction {
    private static final boolean TRACE = false;

    private final String name;
    private final boolean call7;
    private final boolean store;
    private final boolean branch;
    private final boolean literalString;

    private Instruction(String name, boolean call7, boolean store, boolean branch, boolean literalString) {
        this.name = name;
        this.call7 = call7;
        this.store = store;
        this.branch = branch;
        this.literalString = literalString;
    }

    boolean store(int version) {
        return store;
    }

    boolean branch(int version) {
        return branch;
    }

    abstract Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException;

    private static final Instruction[] _2OP = new Instruction[] {
        null,
        new Instruction("je", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                boolean eq = false;
                int a0 = operands[0].getValue();
                for (int i = 1; i < operands.length; i++) {
                    if (operands[i].getValue() == a0) {
                        eq = true;
                    }
                }
                if (eq == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("jl", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                if (((short) operands[0].getValue() < (short) operands[1].getValue()) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("jg", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                if (((short) operands[0].getValue() > (short) operands[1].getValue()) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("dec_chk", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getSignedValue();
                short val = (short) (machine.state.readVar(a0) - 1);
                machine.state.storeVar(a0, val&65535);
                if (((int) val < a1) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("inc_chk", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getSignedValue();
                short val = (short) (machine.state.readVar(a0) + 1);
                machine.state.storeVar(a0, val&65535);
                if (((int) val > a1) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("jin", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                if ((machine.state.objParent(a0) == a1) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("test", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                if (((a0 & a1) == a1) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("or", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.storeVar(store, operands[0].getValue() | operands[1].getValue());
                return Result.Continue;
            }
        },
        new Instruction("and", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.storeVar(store, operands[0].getValue() & operands[1].getValue());
                return Result.Continue;
            }
        },
        new Instruction("test_attr", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                if (machine.state.objAttr(a0, a1) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("set_attr", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                machine.state.objSetAttr(a0, a1, true);
                return Result.Continue;
            }
        },
        new Instruction("clear_attr", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                machine.state.objSetAttr(a0, a1, false);
                return Result.Continue;
            }
        },
        new Instruction("store", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.overwriteVar(operands[0].getValue(), operands[1].getValue());
                return Result.Continue;
            }
        },
        new Instruction("insert_obj", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.objMove(operands[0].getValue(), operands[1].getValue());
                return Result.Continue;
            }
        },
        new Instruction("loadw", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                machine.state.storeVar(store, machine.state.read16((a0 + 2*a1)&65535));
                return Result.Continue;
            }
        },
        new Instruction("loadb", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                machine.state.storeVar(store, machine.state.read8((a0 + a1)&65535));
                return Result.Continue;
            }
        },
        new Instruction("get_prop", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                machine.state.storeVar(store, machine.state.getProp(a0, a1));
                return Result.Continue;
            }
        },
        new Instruction("get_prop_addr", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                machine.state.storeVar(store, machine.state.getPropAddr(a0, a1));
                return Result.Continue;
            }
        },
        new Instruction("get_next_prop", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                machine.state.storeVar(store, machine.state.getNextProp(a0, a1));
                return Result.Continue;
            }
        },
        new Instruction("add", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.storeVar(store, (operands[0].getSignedValue() + operands[1].getSignedValue())&65535);
                return Result.Continue;
            }
        },
        new Instruction("sub", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.storeVar(store, (operands[0].getSignedValue() - operands[1].getSignedValue())&65535);
                return Result.Continue;
            }
        },
        new Instruction("mul", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.storeVar(store, (operands[0].getSignedValue() * operands[1].getSignedValue())&65535);
                return Result.Continue;
            }
        },
        new Instruction("div", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.storeVar(store, (operands[0].getSignedValue() / operands[1].getSignedValue())&65535);
                return Result.Continue;
            }
        },
        new Instruction("mod", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                machine.state.storeVar(store, (operands[0].getSignedValue() % operands[1].getSignedValue())&65535);
                return Result.Continue;
            }
        },
        new Instruction("call_2s", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return doCall(machine, operands[0].getValue(), operands[1].getValue(), 0, 0, 0, 0, 0, 0, 1, store);
            }
        },
        new Instruction("call_2n", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return doCall(machine, operands[0].getValue(), operands[1].getValue(), 0, 0, 0, 0, 0, 0, 1, -1);
            }
        },
        new Instruction("set_color", false, false, false, false) {
            private final int[] colors = new int[] {
                -2, // current
                -1, // default
                0x00000000, // black
                0x00e80000, // red
                0x0000d000, // green
                0x00e8e800, // yellow
                0x000000d0, // blue
                0x00f800f8, // magenta
                0x0000e8e8, // cyan
                0x00f8f8f8, // white
                0x00b0b0b0, // light gray
                0x00888888, // medium gray
                0x00585858, // dark gray
                -1,
                -1,
                -4, // transparent
            };
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                int a2 = operands.length > 2 ? operands[2].getValue() : -1;
                setColors(machine, a2, colors[a0], colors[a1]);
                return Result.Continue;
            }
        },
        new Instruction("throw", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                while (machine.state.frame.index > a1) {
                    machine.state.frame = machine.state.frame.parent;
                }
                return retVal(machine, a0);
            }
        },
        null,
        null,
        null,
    };

    private static final Instruction[] _1OP = new Instruction[] {
        new Instruction("jz", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                if ((operands[0].getValue() == 0) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("get_sibling", false, true, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int result = machine.state.objSibling(operands[0].getValue());
                machine.state.storeVar(store, result);
                if (result != 0) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("get_child", false, true, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int result = machine.state.objChild(operands[0].getValue());
                machine.state.storeVar(store, result);
                if (result != 0) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("get_parent", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.storeVar(store, machine.state.objParent(operands[0].getValue()));
                return Result.Continue;
            }
        },
        new Instruction("get_prop_len", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.storeVar(store, machine.state.getPropLen(operands[0].getValue()));
                return Result.Continue;
            }
        },
        new Instruction("inc", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                machine.state.storeVar(a0, (machine.state.readVar(a0) + 1)&65535);
                return Result.Continue;
            }
        },
        new Instruction("dec", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                machine.state.storeVar(a0, (machine.state.readVar(a0) - 1)&65535);
                return Result.Continue;
            }
        },
        new Instruction("print_addr", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                StringBuilder sb = new StringBuilder();
                ZSCII.decode(sb, machine.state, operands[0].getValue());
                machine.glk.glk.putStringUni(new UnicodeString.US(sb));
                return Result.Continue;
            }
        },
        new Instruction("call_1s", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return doCall(machine, operands[0].getValue(), 0, 0, 0, 0, 0, 0, 0, 0, store);
            }
        },
        new Instruction("remove_obj", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.objMove(operands[0].getValue(), 0);
                return Result.Continue;
            }
        },
        new Instruction("print_obj", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                StringBuilder sb = new StringBuilder();
                ZSCII.decode(sb, machine.state, machine.state.objProperties(operands[0].getValue()) + 1);
                machine.glk.glk.putStringUni(new UnicodeString.US(sb));
                return Result.Continue;
            }
        },
        new Instruction("ret", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return retVal(machine, operands[0].getValue());
            }
        },
        new Instruction("jump", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.pc += operands[0].getSignedValue() - 2;
                return Result.Tick;
            }
        },
        new Instruction("print_paddr", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                StringBuilder sb = new StringBuilder();
                ZSCII.decode(sb, machine.state, machine.state.unpack(operands[0].getValue(), false));
                machine.glk.glk.putStringUni(new UnicodeString.US(sb));
                return Result.Continue;
            }
        },
        new Instruction("load", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.storeVar(store, machine.state.peekVar(operands[0].getValue()&65535));
                return Result.Continue;
            }
        },
        new Instruction("not/call_1n", false, true, false, false) {
            @Override boolean store(int version) {
                return version < 5;
            }
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                if (machine.state.version < 5) {
                    machine.state.storeVar(store, ~operands[0].getValue());
                    return Result.Continue;
                }
                return doCall(machine, operands[0].getValue(), 0, 0, 0, 0, 0, 0, 0, 0, -1);
            }
        },
    };

    private static final Instruction[] _0OP = new Instruction[] {
        new Instruction("rtrue", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return retVal(machine, 1);
            }
        },
        new Instruction("rfalse", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return retVal(machine, 0);
            }
        },
        new Instruction("print", false, false, false, true) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.glk.glk.putString(literalString);
                return Result.Continue;
            }
        },
        new Instruction("print_ret", false, false, false, true) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.glk.glk.putString(literalString.append('\n'));
                return retVal(machine, 1);
            }
        },
        new Instruction("nop", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return Result.Continue;
            }
        },
        new Instruction("save", false, false, false, false) {
            @Override boolean store(int version) {
                return version > 4;
            }
            @Override boolean branch(int version) {
                return version <= 4;
            }
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return doSave(machine, store, cond, branch);
            }
        },
        new Instruction("restore", false, false, false, false) {
            @Override boolean store(int version) {
                return version >= 4;
            }
            @Override boolean branch(int version) {
                return version < 4;
            }
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return doRestore(machine, store);
            }
        },
        new Instruction("restart", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.copyFrom(machine.load(), false, true);
                return Result.Continue;
            }
        },
        new Instruction("ret_popped", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return retVal(machine, machine.state.frame.pop());
            }
        },
        new Instruction("pop/catch", false, false, false, false) {
            @Override boolean store(int version) {
                return version >= 5;
            }
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                if (machine.state.version < 5) {
                    machine.state.frame.pop();
                    return Result.Continue;
                }
                machine.state.storeVar(store, machine.state.frame.index);
                return Result.Continue;
            }
        },
        new Instruction("quit", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return Result.Quit;
            }
        },
        new Instruction("new_line", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.glk.glk.putChar('\n');
                return Result.Continue;
            }
        },
        new Instruction("verify", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return doBranch(machine, branch);
            }
        },
        null,
        new Instruction("piracy", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return doBranch(machine, branch);
            }
        },
    };

    private static final Instruction[] VAR = new Instruction[] {
        new Instruction("call", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return doCall(machine, operands[0].getValue(),
                              operands.length > 1 ? operands[1].getValue() : 0,
                              operands.length > 2 ? operands[2].getValue() : 0,
                              operands.length > 3 ? operands[3].getValue() : 0,
                              0, 0, 0, 0,
                              operands.length - 1, store);
            }
        },
        new Instruction("storew", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.store16((operands[0].getValue() + 2*operands[1].getValue())&65535, operands[2].getValue());
                return Result.Continue;
            }
        },
        new Instruction("storeb", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.store8((operands[0].getValue() + operands[1].getValue())&65535, operands[2].getValue());
                return Result.Continue;
            }
        },
        new Instruction("put_prop", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                int a2 = operands[2].getValue();
                int addr = machine.state.getPropAddr(a0, a1);
                switch (machine.state.getPropLen(addr)) {
                case 1:
                    machine.state.store8(addr, a2);
                    break;
                case 2:
                    machine.state.store16(addr, a2);
                    break;
                default:
                    throw new IllegalArgumentException();
                }
                return Result.Continue;
            }
        },
        new Instruction("sread", false, false, false, false) {
            @Override boolean store(int version) {
                return version >= 5;
            }
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                int a2 = operands.length > 2 ? operands[2].getValue() : 0;
                int a3 = operands.length > 3 ? operands[3].getValue() : 0;
                updateWindowsPreInput(machine);
                int bufferAddress = machine.state.version < 5 ? a0+1 : a0+2;
                machine.mainWindow.requestLineEvent(machine.state.getBuffer(bufferAddress, machine.state.read8(a0)), 0);
                GlkEvent event;
                for (;;) {
                    event = machine.glk.glk.select();
                    if (event.type == GlkEvent.TypeLineInput) {
                        break;
                    } else if (machine.suspending) {
                        machine.state.pc = oldPc;
                        return Result.Tick;
                    }
                    machine.handleEvent(event);
                }
                if (machine.state.version < 5) {
                    machine.state.store8(a0+1+event.val1, 0);
                } else {
                    machine.state.store8(a0+1, event.val1);
                }
                for (int i = 0; i < event.val1; i++) {
                    machine.state.store8(bufferAddress+i, Character.toLowerCase(machine.state.read8(bufferAddress+i)));
                }
                if (a1 != 0) {
                    machine.state.getDictionary().parse(bufferAddress, event.val1, a1);
                }
                if (machine.state.version >= 5) {
                    machine.state.storeVar(store, 13);
                }
                updateWindowsPostInput(machine);
                return Result.Continue;
            }
        },
        new Instruction("print_char", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                StringBuilder sb = new StringBuilder();
                ZSCII.appendZSCII(sb, machine.state, operands[0].getValue());
                machine.glk.glk.putStringUni(new UnicodeString.US(sb));
                return Result.Continue;
            }
        },
        new Instruction("print_num", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.glk.glk.putString(String.valueOf(operands[0].getSignedValue()));
                return Result.Continue;
            }
        },
        new Instruction("random", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int result = 0;
                if (a0 == 0) {
                    machine.random.setSeed(System.nanoTime());
                } else if (a0 < 0) {
                    machine.random.setSeed(-a0);
                } else {
                    result = machine.random.nextInt(a0);
                }
                machine.state.storeVar(store, result);
                return Result.Continue;
            }
        },
        new Instruction("push", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.frame.push(operands[0].getValue());
                return Result.Continue;
            }
        },
        new Instruction("pull", false, false, false, false) {
            @Override boolean store(int version) {
                return version == 6;
            }
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                if (machine.state.version != 6) {
                    machine.state.overwriteVar(operands[0].getValue(), machine.state.frame.pop());
                    return Result.Continue;
                }
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("split_window", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                if (a0 == 0) {
                    if (machine.upperWindow != null) {
                        machine.upperWindow.close();
                        machine.upperWindow = null;
                    }
                } else if (machine.upperWindow == null) {
                    machine.upperWindow = machine.glk.glk.windowOpen(machine.mainWindow, GlkWindowArrangement.MethodAbove | GlkWindowArrangement.MethodFixed | GlkWindowArrangement.MethodNoBorder, a0, GlkWindow.TypeTextGrid, 1);
                    machine.glk.add(machine.upperWindow);
                } else {
                    machine.upperWindow.getParent().setArrangement(GlkWindowArrangement.MethodAbove | GlkWindowArrangement.MethodFixed | GlkWindowArrangement.MethodNoBorder, a0, machine.upperWindow);
                    if (machine.state.version <= 3) {
                        machine.upperWindow.clear();
                    }
                }
                return Result.Continue;
            }
        },
        new Instruction("set_window", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                if (a0 != 0 && machine.upperWindow != null) {
                    machine.currentWindow = 1;
                    machine.glk.glk.setWindow(machine.upperWindow);
                } else {
                    machine.currentWindow = 0;
                    machine.glk.glk.setWindow(machine.mainWindow);
                }
                return Result.Continue;
            }
        },
        new Instruction("call_vs2", true, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return doCall(machine, operands[0].getValue(),
                              operands.length > 1 ? operands[1].getValue() : 0,
                              operands.length > 2 ? operands[2].getValue() : 0,
                              operands.length > 3 ? operands[3].getValue() : 0,
                              operands.length > 4 ? operands[4].getValue() : 0,
                              operands.length > 5 ? operands[5].getValue() : 0,
                              operands.length > 6 ? operands[6].getValue() : 0,
                              operands.length > 7 ? operands[7].getValue() : 0,
                              operands.length - 1, store);
            }
        },
        new Instruction("erase_window", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                if (a0 == 0) {
                    machine.mainWindow.clear();
                } else if (a0 == -1) {
                    if (machine.upperWindow != null) {
                        machine.upperWindow.close();
                        machine.upperWindow = null;
                    }
                    machine.mainWindow.clear();
                } else if (a0 == -2) {
                    if (machine.upperWindow != null) {
                        machine.upperWindow.clear();
                    }
                    machine.mainWindow.clear();
                } else if (machine.upperWindow != null) {
                    machine.upperWindow.clear();
                }
                return Result.Continue;
            }
        },
        new Instruction("erase_line", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                if (a0 != 1 || machine.currentWindow == 0 || machine.upperWindow == null) {
                    return Result.Continue;
                }
                int x = machine.upperWindow.getCursorX();
                int y = machine.upperWindow.getCursorY();
                for (int i = y; i < machine.screenWidth; i++) {
                    machine.upperWindow.getStream().putChar(' ');
                }
                machine.upperWindow.moveCursor(x, y);
                return Result.Continue;
            }
        },
        new Instruction("set_cursor", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                if (machine.currentWindow != 0 && machine.upperWindow != null) {
                    GlkWindowSize size = machine.upperWindow.getSize();
                    int x = a0 >= 32768 ? size.width - a0 - 65536 : a0 - 1;
                    int y = a1 >= 32768 ? size.height - a1 - 65536 : a1 - 1;
                    machine.upperWindow.moveCursor(x, y);
                }
                return Result.Continue;
            }
        },
        new Instruction("get_cursor", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int x;
                int y;
                if (machine.currentWindow != 0 && machine.upperWindow != null) {
                    x = machine.upperWindow.getCursorX();
                    y = machine.upperWindow.getCursorY();
                } else {
                    x = machine.mainWindow.getCursorX();
                    y = machine.mainWindow.getCursorY();
                }
                machine.state.store16(a0, y+1);
                machine.state.store16(a0+2, x+1);
                return Result.Continue;
            }
        },
        new Instruction("set_text_style", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int style;
                switch (operands[0].getValue()) {
                case 0: style = GlkStream.StyleNormal; break;
                case 1: style = GlkStream.StyleAlert; break;
                case 2: style = GlkStream.StyleHeader; break;
                case 3: style = GlkStream.StyleUser1; break;
                case 4: style = GlkStream.StyleEmphasized; break;
                case 5: style = GlkStream.StyleBlockQuote; break;
                case 6: style = GlkStream.StyleSubheader; break;
                case 7: style = GlkStream.StyleUser2; break;
                default: 
                    style = GlkStream.StylePreformatted;
                    break;
                }
                machine.mainWindow.getStream().setStyle(style);
                if (machine.upperWindow != null) {
                    machine.upperWindow.getStream().setStyle(style);
                }
                return Result.Continue;
            }
        },
        new Instruction("buffer_mode", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return Result.Continue;
            }
        },
        new Instruction("output_stream", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("input_stream", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("sound_effect", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("read_char", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands.length > 1 ? operands[1].getValue() : 0;
                int a2 = operands.length > 2 ? operands[2].getValue() : 0;
                updateWindowsPreInput(machine);
                machine.mainWindow.requestCharEvent();
                GlkEvent event;
                for (;;) {
                    event = machine.glk.glk.select();
                    if (event.type == GlkEvent.TypeCharInput) {
                        break;
                    } else if (machine.suspending) {
                        machine.state.pc = oldPc;
                        return Result.Tick;
                    }
                    machine.handleEvent(event);
                }
                machine.state.storeVar(store, event.val1);
                updateWindowsPostInput(machine);
                return Result.Continue;
            }
        },
        new Instruction("scan_table", false, true, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                int a2 = operands[2].getValue();
                int a3 = operands.length > 3 ? operands[3].getValue() : 0x82;
                int table = a1;
                int entrySize = a3 & 127;
                int result = 0;
                if ((a3 & 128) != 0) {
                    for (int i = 0; i < a2; i++) {
                        if (a0 == machine.state.read16(table)) {
                            result = table;
                            break;
                        }
                        table += entrySize;
                    }
                } else {
                    for (int i = 0; i < a2; i++) {
                        if (a0 == machine.state.read8(table)) {
                            result = table;
                            break;
                        }
                        table += entrySize;
                    }
                }
                machine.state.storeVar(store, result);
                if ((table != 0) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("not", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.state.storeVar(store, operands[0].getValue()^65535);
                return Result.Continue;
            }
        },
        new Instruction("call_vn", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return doCall(machine, operands[0].getValue(),
                              operands.length > 1 ? operands[1].getValue() : 0,
                              operands.length > 2 ? operands[2].getValue() : 0,
                              operands.length > 3 ? operands[3].getValue() : 0,
                              0, 0, 0, 0,
                              operands.length - 1, -1);
            }
        },
        new Instruction("call_vn2", true, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                return doCall(machine, operands[0].getValue(),
                              operands.length > 1 ? operands[1].getValue() : 0,
                              operands.length > 2 ? operands[2].getValue() : 0,
                              operands.length > 3 ? operands[3].getValue() : 0,
                              operands.length > 4 ? operands[4].getValue() : 0,
                              operands.length > 5 ? operands[5].getValue() : 0,
                              operands.length > 6 ? operands[6].getValue() : 0,
                              operands.length > 7 ? operands[7].getValue() : 0,
                              operands.length - 1, -1);
            }
        },
        new Instruction("tokenise", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                int a2 = operands.length > 2 ? operands[2].getValue() : 0;
                int a3 = operands.length > 3 ? operands[3].getValue() : 0;
                int bufferAddress = machine.state.version < 5 ? a0+1 : a0+2;
                int bufferLength = machine.state.read8(bufferAddress-1);
                if (machine.state.version < 5) {
                    for (int i = 0; i < bufferLength; i++) {
                        if (machine.state.read8(bufferAddress+i) == 0) {
                            bufferLength = i;
                        }
                    }
                }
                machine.state.getDictionary().parse(a2, bufferAddress, bufferLength, a1, a3 != 0);
                return Result.Continue;
            }
        },
        new Instruction("encode_text", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                int a2 = operands[2].getValue();
                int a3 = operands[3].getValue();
                StringBuilder sb = new StringBuilder();
                for (int i = a2; i < a1 + a2; i++) {
                    ZSCII.appendZSCII(sb, machine.state, machine.state.read8(i));
                }
                long encoded = ZSCII.encode(machine.state, sb.toString());
                machine.state.store16(a3, (int) (encoded >> 32));
                machine.state.store16(a3 + 2, (int) (encoded >> 16));
                machine.state.store16(a3 + 4, (int) encoded);
                return Result.Continue;
            }
        },
        new Instruction("copy_table", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                int a2 = operands[2].getValue();
                boolean forward = false;
                if (a2 >= 32768) {
                    a2 = 65536 - a2;
                    forward = true;
                }
                if (a1 == 0) {
                    for (int i = 0; i < a2; i++) {
                        machine.state.store8(a0+i, 0);
                    }
                } else if (forward || a0 < a1) {
                    for (int i = 0; i < a2; i++) {
                        machine.state.store8(a0+i, machine.state.read8(a1+i));
                    }
                } else {
                    for (int i = a2-1; i >= 0; i--) {
                        machine.state.store8(a0+i, machine.state.read8(a1+i));
                    }
                }                
                return Result.Continue;
            }
        },
        new Instruction("print_table", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                int a2 = operands.length > 2 ? operands[2].getValue() : 1;
                int a3 = operands.length > 3 ? operands[3].getValue() : 0;
                GlkWindow window = machine.currentWindow == 0 || machine.upperWindow == null ? machine.mainWindow : machine.upperWindow;
                int x = window.getCursorX();
                int y = window.getCursorY();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < a2; i++) {
                    sb.setLength(0);
                    for (int j = 0; j < a1; j++) {
                        ZSCII.appendZSCII(sb, machine.state, machine.state.read8(a0));
                        a0++;
                    }
                    window.moveCursor(x, y + i);
                    window.getStream().putStringUni(new UnicodeString.US(sb));
                    a0 += a3;
                }
                return Result.Continue;
            }
        },
        new Instruction("check_arg_count", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                if (((machine.state.frame.args & (1 << (a0 - 1))) != 0) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
    };

    private static final Instruction[] EXT = new Instruction[] {
        new Instruction("save", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                for (Operand operand : operands) {
                    operand.getValue();
                }
                return doSave(machine, store, cond, branch);
            }
        },
        new Instruction("restore", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                for (Operand operand : operands) {
                    operand.getValue();
                }
                return doRestore(machine, store);
            }
        },
        new Instruction("log_shift", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getSignedValue();
                machine.state.storeVar(store, (a1 > 0 ? a0 << a1 : a0 >>> -a1) & 65535);
                return Result.Continue;
            }
        },
        new Instruction("art_shift", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getSignedValue();
                int a1 = operands[1].getSignedValue();
                machine.state.storeVar(store, (a1 > 0 ? a0 << a1 : a0 >> -a1) & 65535);
                return Result.Continue;
            }
        },
        new Instruction("set_font", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("draw_picture", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("picture_data", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("erase_picture", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("set_margins", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("save_undo", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                State undoState;
                if (machine.undoStateIndex >= machine.undoStates.length) {
                    undoState = machine.undoStates[0];
                    for (int i = 0; i < machine.undoStates.length-1; i++) {
                        machine.undoStates[i] = machine.undoStates[i+1];
                    }
                    machine.undoStates[machine.undoStates.length-1] = undoState;
                } else {
                    undoState = machine.undoStates[machine.undoStateIndex];
                    if (undoState == null) {
                        undoState = new State();
                    }
                    machine.undoStates[machine.undoStateIndex] = undoState;
                    machine.undoStateIndex++;
                }
                undoState.copyFrom(machine.state, true, false);
                undoState.storeVar(store, 2);
                machine.state.storeVar(store, 1);
                return Result.Continue;
            }
        },
        new Instruction("restore_undo", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                if (machine.undoStateIndex <= 0) {
                    machine.state.storeVar(store, 0);
                    return Result.Continue;
                }
                machine.undoStateIndex--;
                machine.state.copyFrom(machine.undoStates[machine.undoStateIndex], true, false);
                return Result.Continue;
            }
        },
        new Instruction("print_unicode", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                machine.glk.glk.putCharUni(operands[0].getValue());
                return Result.Continue;
            }
        },
        new Instruction("check_unicode", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int result = 0;
                if (machine.glk.glk.gestalt(GlkGestalt.CharOutput, a0) != 0) {
                    result |= 1;
                }
                if (machine.glk.glk.gestalt(GlkGestalt.CharInput, a0) != 0) {
                    result |= 2;
                }
                machine.state.storeVar(store, result);
                return Result.Continue;
            }
        },
        new Instruction("set_true_color", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                int a2 = operands.length > 2 ? operands[2].getValue() : -1;
                int fg = a0 < 0 ? a0 : (a0 & 31) << 19 | (a0 & 992) << 6 | (a0 & 31744) >>> 7;
                int bg = a1 < 0 ? a1 : (a1 & 31) << 19 | (a1 & 992) << 6 | (a1 & 31744) >>> 7;
                setColors(machine, a2, fg, bg);
                return Result.Continue;
            }
        },
        null,
        null,
        new Instruction("move_window", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("window_size", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("window_style", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("get_wind_prop", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("scroll_window", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("pop_stack", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("read_mouse", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("mouse_window", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("push_stack", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("put_wind_prop", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("print_form", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("make_menu", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("picture_table", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("buffer_screen", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString, int oldPc) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
    };

    public enum Result {
        Continue, Tick, Quit;
    }

    public static Result executeNext(Machine machine) throws IOException {
        int oldPc = machine.state.pc;
        int opcode = machine.state.read8(machine.state.pc);
        machine.state.pc++;
        Instruction insn;
        Operand[] operands;
        switch (opcode & 240) {
        case 0: case 16:
            insn = _2OP[opcode&31];
            operands = new Operand[] { new Operand(machine, Operand.SMALL), new Operand(machine, Operand.SMALL) };
            break;
        case 32: case 48:
            insn = _2OP[opcode&31];
            operands = new Operand[] { new Operand(machine, Operand.SMALL), new Operand(machine, Operand.VAR) };
            break;
        case 64: case 80:
            insn = _2OP[opcode&31];
            operands = new Operand[] { new Operand(machine, Operand.VAR), new Operand(machine, Operand.SMALL) };
            break;
        case 96: case 112:
            insn = _2OP[opcode&31];
            operands = new Operand[] { new Operand(machine, Operand.VAR), new Operand(machine, Operand.VAR) };
            break;
        case 128:
            insn = _1OP[opcode&15];
            operands = new Operand[] { new Operand(machine, Operand.LARGE) };
            break;
        case 144:
            insn = _1OP[opcode&15];
            operands = new Operand[] { new Operand(machine, Operand.SMALL) };
            break;
        case 160:
            insn = _1OP[opcode&15];
            operands = new Operand[] { new Operand(machine, Operand.VAR) };
            break;
        case 176:
            if (opcode != 190) {
                insn = _0OP[opcode&15];
                operands = new Operand[0];
            } else {
                insn = EXT[machine.state.read8(machine.state.pc)];
                machine.state.pc++;
                operands = null;
            }
            break;
        case 192: case 208:
            insn = _2OP[opcode&31];
            operands = null;
            break;
        case 224: case 240:
            insn = VAR[opcode&31];
            operands = null;
            break;
        default:
            throw new AssertionError();
        }
        if (operands == null) {
            int count = 0;
            int types = machine.state.read8(machine.state.pc);
            for (int i = 0; i < 4; i++) {
                if ((types & 192) == 192) {
                    break;
                }
                count++;
                types <<= 2;
            }
            if (insn.call7) {
                types = machine.state.read8(machine.state.pc+1);
                for (int i = 0; i < 4; i++) {
                    if ((types & 192) == 192) {
                        break;
                    }
                    count++;
                    types <<= 2;
                }
            }
            operands = new Operand[count];
            types = machine.state.read8(machine.state.pc);
            machine.state.pc++;
            if (!insn.call7) {
                for (int i = 0; i < count; i++) {
                    operands[i] = new Operand(machine, (types >> (6 - 2*i))&3);
                }
            } else {
                int types2 = machine.state.read8(machine.state.pc);
                machine.state.pc++;
                for (int i = 0; i < Math.min(count, 4); i++) {
                    operands[i] = new Operand(machine, (types >> (6 - 2*i))&3);
                }
                for (int i = 4; i < count; i++) {
                    operands[i] = new Operand(machine, (types2 >> (14 - 2*i))&3);
                }
            }
        }
        int store = 0;
        if (insn.store(machine.state.version)) {
            store = machine.state.read8(machine.state.pc);
            machine.state.pc++;
        }
        boolean cond = false;
        int branch = 0;
        if (insn.branch(machine.state.version)) {
            branch = machine.state.read8(machine.state.pc);
            machine.state.pc++;
            cond = (branch & 128) != 0;
            branch &= 127;
            if ((branch & 64) != 0) {
                branch &= 63;
            } else {
                branch = (branch << 8) | machine.state.read8(machine.state.pc);
                machine.state.pc++;
                if (branch >= 8192) {
                    branch -= 16384;
                }
            }
        }
        StringBuilder literalString = null;
        if (insn.literalString) {
            literalString = new StringBuilder();
            ZSCII.decode(literalString, machine.state, machine.state.pc);
            while (machine.state.read8(machine.state.pc) < 128) {
                machine.state.pc += 2;
            }
            machine.state.pc += 2;
        }
        if (TRACE) {
            System.out.print(String.format("%04x:%s", oldPc, insn.name));
            for (int i = 0; i < operands.length; i++) {
                System.out.print(String.format(" %s",operands[i].traceValue()));
            }
            if (insn.store(machine.state.version)) {
                if (store == 0) {
                    System.out.print(" ->(SP+)");
                } else if (store < 0 || store >= 256) {
                    System.out.print(String.format(" ->?=%x",store));
                } else if (store < 16) {
                    System.out.print(String.format(" ->l%x",store));
                } else {
                    System.out.print(String.format(" ->g%x",store-16));
                }
            }
            if (insn.branch(machine.state.version)) {
                System.out.print(String.format(" %s:%x->", cond, branch));
                switch (branch) {
                case 0: System.out.print("rfalse"); break;
                case 1: System.out.print("rtrue"); break;
                default:
                    System.out.print(String.format("%04x",machine.state.pc+branch-2));
                    break;
                }
            }
            if (insn.literalString) {
                System.out.print(String.format(" str:%s", literalString));
            }
            System.out.println();
        }
        return insn.execute(machine, operands, store, cond, branch, literalString, oldPc);
    }

    private static class Operand {
        static final int LARGE = 0;
        static final int SMALL = 1;
        static final int VAR = 2;

        final Machine machine;
        final int type;
        final int value;

        Operand(Machine machine, int type) {
            this.machine = machine;
            this.type = type;
            switch (type) {
            case LARGE:
                value = machine.state.read16(machine.state.pc);
                machine.state.pc += 2;
                break;
            case SMALL:
            case VAR:
                value = machine.state.read8(machine.state.pc);
                machine.state.pc++;
                break;
            default:
                throw new AssertionError();
            }
        }

        int getValue() {
            switch (type) {
            case LARGE: case SMALL:
                return value;
            case VAR:
                return machine.state.readVar(value);
            default:
                throw new AssertionError();
            }
        }

        int getSignedValue() {
            int val = getValue() & 65535;
            return val >= 32768 ? val - 65536 : val;
        }

        String traceValue() {
            switch (type) {
            case LARGE: case SMALL:
                return String.format("#%x", value);
            case VAR:
                return machine.state.traceVar(value);
            default:
                throw new AssertionError();
            }
        }
    }

    private static Result doBranch(Machine machine, int branch) {
        switch (branch) {
        case 0:
        case 1:
            return retVal(machine, branch);
        default:
            machine.state.pc += branch - 2;
            return Result.Tick;
        }
    }

    private static Result retVal(Machine machine, int val) {
        StackFrame frame = machine.state.frame;
        machine.state.frame = frame.parent;
        machine.state.pc = frame.returnAddress;
        if (frame.result >= 0) {
            machine.state.storeVar(frame.result, val);
        }
        return Result.Tick;
    }

    @SuppressWarnings("fallthrough")
    private static Result doCall(Machine machine, int addr, int a1, int a2, int a3, int a4, int a5, int a6, int a7, int argc, int result) {
        if (addr == 0) {
            if (result >= 0) {
                machine.state.storeVar(result, 0);
            }
            return Result.Tick;
        }
        addr = machine.state.unpack(addr, true);
        StackFrame frame = new StackFrame(machine.state.frame, machine.state.pc, result, 127 >> (7-argc), machine.state.read8(addr));
        addr++;
        if (machine.state.version < 5) {
            for (int i = 0; i < frame.locals.length; i++) {
                frame.locals[i] = machine.state.read16(addr);
                addr += 2;
            }
        }
        switch (Math.min(argc, frame.locals.length)) {
        default: frame.locals[6] = a7; /*FALLTHROUGH*/
        case 6: frame.locals[5] = a6; /*FALLTHROUGH*/
        case 5: frame.locals[4] = a5; /*FALLTHROUGH*/
        case 4: frame.locals[3] = a4; /*FALLTHROUGH*/
        case 3: frame.locals[2] = a3; /*FALLTHROUGH*/
        case 2: frame.locals[1] = a2; /*FALLTHROUGH*/
        case 1: frame.locals[0] = a1; /*FALLTHROUGH*/
        case 0:
        }
        machine.state.frame = frame;
        machine.state.pc = addr;
        return Result.Tick;
    }

    private static Result doSave(Machine machine, int store, boolean cond, int branch) throws IOException {
        GlkFile file = machine.glk.glk.fileCreateByPrompt(GlkFile.UsageSavedGame, GlkFile.ModeWrite, 0);
        GlkStream stream = null;
        if (file != null) {
            stream = machine.glk.glk.streamOpenFile(file, GlkFile.ModeWrite, 0);
        }
        if (stream == null) {
            if (machine.state.version <= 4) {
                if (!cond) {
                    doBranch(machine, branch);
                }
            } else {
                machine.state.storeVar(store, 0);
            }
            return Result.Continue;
        }
        try {
            if (machine.state.version <= 4) {
                if (cond) {
                    doBranch(machine, branch);
                }
                machine.state.writeSave(stream.getDataOutput());
            } else {
                State saveState = new State();
                saveState.copyFrom(machine.state, true, false);
                saveState.storeVar(store, 2);
                saveState.writeSave(stream.getDataOutput());
                machine.state.storeVar(store, 1);
            }
        } finally {
            stream.close();
        }
        return Result.Continue;
    }

    private static Result doRestore(Machine machine, int store) throws IOException {
        GlkFile file = machine.glk.glk.fileCreateByPrompt(GlkFile.UsageSavedGame, GlkFile.ModeRead, 0);
        GlkStream stream = null;
        if (file != null) {
            stream = machine.glk.glk.streamOpenFile(file, GlkFile.ModeRead, 0);
        }
        if (stream == null) {
            if (machine.state.version >= 4) {
                machine.state.storeVar(store, 0);
            }
            return Result.Continue;
        }
        try {
            if (!machine.state.loadSave(stream.getDataInput(), true)) {
                if (machine.state.version >= 4) {
                    machine.state.storeVar(store, 0);
                }
                return Result.Continue;
            }
        } finally {
            stream.close();
        }
        return Result.Continue;
    }

    private static void setColors(Machine machine, int window, int foreground, int background) {
        setColor(machine, GlkWindow.TypeTextBuffer, GlkStream.StyleHintTextColor, foreground);
        setColor(machine, GlkWindow.TypeTextGrid, GlkStream.StyleHintTextColor, foreground);
        setColor(machine, GlkWindow.TypeTextBuffer, GlkStream.StyleHintBackColor, background);
        setColor(machine, GlkWindow.TypeTextGrid, GlkStream.StyleHintBackColor, background);
    }

    private static void setColor(Machine machine, int windowType, int styleHint, int color) {
        if (color == -2) {
            return;
        }
        if (color < 0) {
            machine.glk.glk.styleHintClear(windowType, GlkStream.StyleNormal, styleHint);
            machine.glk.glk.styleHintClear(windowType, GlkStream.StyleEmphasized, styleHint);
            machine.glk.glk.styleHintClear(windowType, GlkStream.StylePreformatted, styleHint);
            machine.glk.glk.styleHintClear(windowType, GlkStream.StyleHeader, styleHint);
            machine.glk.glk.styleHintClear(windowType, GlkStream.StyleSubheader, styleHint);
            machine.glk.glk.styleHintClear(windowType, GlkStream.StyleAlert, styleHint);
            machine.glk.glk.styleHintClear(windowType, GlkStream.StyleNote, styleHint);
            machine.glk.glk.styleHintClear(windowType, GlkStream.StyleBlockQuote, styleHint);
            machine.glk.glk.styleHintClear(windowType, GlkStream.StyleInput, styleHint);
            machine.glk.glk.styleHintClear(windowType, GlkStream.StyleUser1, styleHint);
            machine.glk.glk.styleHintClear(windowType, GlkStream.StyleUser2, styleHint);
        } else {
            machine.glk.glk.styleHintSet(windowType, GlkStream.StyleNormal, styleHint, color);
            machine.glk.glk.styleHintSet(windowType, GlkStream.StyleEmphasized, styleHint, color);
            machine.glk.glk.styleHintSet(windowType, GlkStream.StylePreformatted, styleHint, color);
            machine.glk.glk.styleHintSet(windowType, GlkStream.StyleHeader, styleHint, color);
            machine.glk.glk.styleHintSet(windowType, GlkStream.StyleSubheader, styleHint, color);
            machine.glk.glk.styleHintSet(windowType, GlkStream.StyleAlert, styleHint, color);
            machine.glk.glk.styleHintSet(windowType, GlkStream.StyleNote, styleHint, color);
            machine.glk.glk.styleHintSet(windowType, GlkStream.StyleBlockQuote, styleHint, color);
            machine.glk.glk.styleHintSet(windowType, GlkStream.StyleInput, styleHint, color);
            machine.glk.glk.styleHintSet(windowType, GlkStream.StyleUser1, styleHint, color);
            machine.glk.glk.styleHintSet(windowType, GlkStream.StyleUser2, styleHint, color);
        }
    }

    private static void updateWindowsPreInput(Machine machine) throws IOException {
        if (machine.state.version <= 3) {
            int vars = machine.state.read16(State.GLOBAL_VAR_TABLE);
            int obj = machine.state.read16(vars);
            int s1 = machine.state.read16(vars+2);
            int s2 = machine.state.read16(vars+4);
            int objProperties = machine.state.objProperties(obj);
            StringBuilder sb = new StringBuilder();
            ZSCII.decode(sb, machine.state, objProperties+1);
            if (machine.upperWindow == null) {
                machine.upperWindow = machine.glk.glk.windowOpen(machine.mainWindow, GlkWindowArrangement.MethodAbove | GlkWindowArrangement.MethodFixed | GlkWindowArrangement.MethodNoBorder, 1, GlkWindow.TypeTextGrid, 1);
                machine.glk.add(machine.upperWindow);
            }
            String score;
            if (machine.state.version < 3 || (machine.state.read8(State.FLAGS1) & 2) == 0) {
                score = " " + s1 + "/" + s2;
            } else {
                score = String.format(" %d:%02d", s1 % 24, s2 % 60);
            }
            while (sb.length() + score.length() < machine.screenWidth) {
                sb.append(' ');
            }
            sb.setLength(Math.max(0,machine.screenWidth - score.length()));
            sb.append(score);
            machine.upperWindow.moveCursor(0, 0);
            machine.upperWindow.getStream().putString(sb);
        }
        //... handle delayed upperwindow manipulation for quote boxes
    }

    private static void updateWindowsPostInput(Machine machine) throws IOException {
        //... handle delayed upperwindow manipulation for quote boxes
    }
}
