package com.yrek.ifstd.zcode;

import java.io.IOException;

abstract class Instruction {
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

    abstract Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException;

    private static final Instruction[] _2OP = new Instruction[] {
        null,
        new Instruction("je", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
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
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                if (((short) operands[0].getValue() < (short) operands[1].getValue()) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("jg", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                if (((short) operands[0].getValue() > (short) operands[1].getValue()) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("dec_chk", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                int val = machine.state.readVar(a0) - 1;
                machine.state.storeVar(a0, val);
                if (((short) val < (short) a1) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("inc_chk", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                int val = machine.state.readVar(a0) + 1;
                machine.state.storeVar(a0, val);
                if (((short) val > (short) a1) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("jin", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                if ((machine.state.objParent(a0) == a1) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("test", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                if (((a0 & a1) == a1) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("or", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.storeVar(store, operands[0].getValue() | operands[1].getValue());
                return Result.Continue;
            }
        },
        new Instruction("and", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.storeVar(store, operands[0].getValue() & operands[1].getValue());
                return Result.Continue;
            }
        },
        new Instruction("test_attr", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                if (machine.state.objAttr(a0, a1) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("set_attr", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                machine.state.objSetAttr(a0, a1, true);
                return Result.Continue;
            }
        },
        new Instruction("clear_attr", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                machine.state.objSetAttr(a0, a1, false);
                return Result.Continue;
            }
        },
        new Instruction("store", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.storeVar(operands[0].getValue(), operands[1].getValue());
                return Result.Continue;
            }
        },
        new Instruction("insert_obj", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.objMove(operands[0].getValue(), operands[1].getValue());
                return Result.Continue;
            }
        },
        new Instruction("loadw", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                machine.state.storeVar(store, machine.state.read16(a0 + 2*a1));
                return Result.Continue;
            }
        },
        new Instruction("loadb", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                machine.state.storeVar(store, machine.state.read8(a0 + a1));
                return Result.Continue;
            }
        },
        new Instruction("get_prop", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                machine.state.storeVar(store, machine.state.getProp(a0, a1));
                return Result.Continue;
            }
        },
        new Instruction("get_prop_addr", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                machine.state.storeVar(store, machine.state.getPropAddr(a0, a1));
                return Result.Continue;
            }
        },
        new Instruction("get_next_prop", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                int a0 = operands[0].getValue();
                int a1 = operands[1].getValue();
                machine.state.storeVar(store, machine.state.getNextProp(a0, a1));
                return Result.Continue;
            }
        },
        new Instruction("add", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.storeVar(store, operands[0].getValue() + operands[1].getValue());
                return Result.Continue;
            }
        },
        new Instruction("sub", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.storeVar(store, operands[0].getValue() - operands[1].getValue());
                return Result.Continue;
            }
        },
        new Instruction("mul", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.storeVar(store, operands[0].getValue() * operands[1].getValue());
                return Result.Continue;
            }
        },
        new Instruction("div", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.storeVar(store, operands[0].getValue() / operands[1].getValue());
                return Result.Continue;
            }
        },
        new Instruction("mod", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.storeVar(store, operands[0].getValue() % operands[1].getValue());
                return Result.Continue;
            }
        },
        new Instruction("call_2s", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("call_2n", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("set_colour", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("throw", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        null,
        null,
        null,
    };

    private static final Instruction[] _1OP = new Instruction[] {
        new Instruction("jz", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                if ((operands[0].getValue() == 0) == cond) {
                    return doBranch(machine, branch);
                }
                return Result.Continue;
            }
        },
        new Instruction("get_sibling", false, true, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("get_child", false, true, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("get_parent", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("get_prop_len", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("inc", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                int a0 = operands[0].getValue();
                machine.state.storeVar(a0, machine.state.readVar(a0) + 1);
                return Result.Continue;
            }
        },
        new Instruction("dec", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                int a0 = operands[0].getValue();
                machine.state.storeVar(a0, machine.state.readVar(a0) - 1);
                return Result.Continue;
            }
        },
        new Instruction("print_addr", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("call_1s", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("remove_obj", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("print_obj", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("ret", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                return retVal(machine, operands[0].getValue());
            }
        },
        new Instruction("jump", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.pc += operands[0].getValue() - 2;
                return Result.Tick;
            }
        },
        new Instruction("print_paddr", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("load", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.storeVar(store, machine.state.readVar(operands[0].getValue()&65535));
                return Result.Continue;
            }
        },
        new Instruction("not/call_1n", false, true, false, false) {
            @Override boolean store(int version) {
                return version < 5;
            }
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                if (machine.state.version < 5) {
                    machine.state.storeVar(store, ~operands[0].getValue());
                    return Result.Continue;
                }
                throw new RuntimeException("unimplemented");
            }
        },
    };

    private static final Instruction[] _0OP = new Instruction[] {
        new Instruction("rtrue", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                return retVal(machine, 1);
            }
        },
        new Instruction("rfalse", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                return retVal(machine, 0);
            }
        },
        new Instruction("print", false, false, false, true) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("print_ret", false, false, false, true) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("nop", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                return Result.Continue;
            }
        },
        new Instruction("save", false, false, false, false) {
            @Override boolean store(int version) {
                return version >= 4;
            }
            @Override boolean branch(int version) {
                return version < 4;
            }
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("restore", false, false, false, false) {
            @Override boolean store(int version) {
                return version >= 4;
            }
            @Override boolean branch(int version) {
                return version < 4;
            }
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("restart", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("ret_popped", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                return retVal(machine, machine.state.frame.pop());
            }
        },
        new Instruction("pop/catch", false, false, false, false) {
            @Override boolean store(int version) {
                return version >= 5;
            }
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                if (machine.state.version < 5) {
                    machine.state.frame.pop();
                    return Result.Continue;
                }
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("quit", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                return Result.Quit;
            }
        },
        new Instruction("new_line", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("verify", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                return doBranch(machine, branch);
            }
        },
        null,
        new Instruction("piracy", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                return doBranch(machine, branch);
            }
        },
    };

    private static final Instruction[] VAR = new Instruction[] {
        new Instruction("call", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("storew", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.store16((operands[0].getValue() + 2*operands[1].getValue())&65535, operands[2].getValue());
                return Result.Continue;
            }
        },
        new Instruction("storeb", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.store8((operands[0].getValue() + operands[1].getValue())&65535, operands[2].getValue());
                return Result.Continue;
            }
        },
        new Instruction("put_prop", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("sread", false, false, false, false) {
            @Override boolean store(int version) {
                return version >= 5;
            }
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("print_char", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("print_num", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("random", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
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
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.frame.push(operands[0].getValue());
                return Result.Continue;
            }
        },
        new Instruction("pull", false, false, false, false) {
            @Override boolean store(int version) {
                return version == 6;
            }
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("split_window", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("set_window", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("call_vs2", true, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("erase_window", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("erase_line", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("set_cursor", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("get_cursor", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("set_text_style", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("buffer_mode", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("output_stream", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("input_stream", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("sound_effect", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("read_char", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("scan_table", false, true, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("not", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                machine.state.storeVar(store, ~operands[0].getValue());
                return Result.Continue;
            }
        },
        new Instruction("call_vn", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("call_vn2", true, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("tokenise", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("encode_text", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("copy_table", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("print_table", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("check_arg_count", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
    };

    private static final Instruction[] EXT = new Instruction[] {
        new Instruction("save", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("result", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("log_shift", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("art_shift", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("set_font", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("draw_picture", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("erase_picture", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("set_margins", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("save_undo", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("restore_undo", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("print_unicode", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("check_unicode", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("set_true_color", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        null,
        null,
        new Instruction("move_window", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("window_size", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("window_style", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("get_wind_prop", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("scroll_window", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("pop_stack", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("read_mouse", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("mouse_window", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("push_stack", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("put_wind_prop", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("print_form", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("make_menu", false, false, true, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("picture_table", false, false, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
        new Instruction("buffer_string", false, true, false, false) {
            @Override Result execute(Machine machine, Operand[] operands, int store, boolean cond, int branch, StringBuilder literalString) throws IOException {
                throw new RuntimeException("unimplemented");
            }
        },
    };

    public enum Result {
        Continue, Tick, Quit;
    }

    public static Result executeNext(Machine machine) throws IOException {
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
            if (insn.call7 && count == 4) {
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
                machine.state.pc++;
                for (int i = 0; i < Math.min(count, 4); i++) {
                    operands[i] = new Operand(machine, (types >> (6 - 2*i))&3);
                }
                if (count > 4) {
                    types = machine.state.read8(machine.state.pc-1);
                    for (int i = 4; i < count; i++) {
                        operands[i] = new Operand(machine, (types >> (14 - 2*i))&3);
                    }
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
        return insn.execute(machine, operands, store, cond, branch, literalString);
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
        machine.state.storeVar(frame.returnAddress, val);
        return Result.Tick;
    }
}
