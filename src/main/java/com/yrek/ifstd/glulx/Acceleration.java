package com.yrek.ifstd.glulx;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

class Acceleration implements Serializable {
    private static final long serialVersionUID = 0L;

    private static final Function[] table = new Function[14];

    HashMap<Integer,Integer> functions = new HashMap<Integer,Integer>();
    int[] parameters = new int[9];

    boolean gestalt(int index) {
        return index > 0 && index < table.length && table[index] != null;
    }

    void setParameter(int index, int value) {
        if (index >= 0 && index < parameters.length) {
            parameters[index] = value;
        }
    }

    void accelerate(int index, int addr) {
        if (index <= 0 || index >= table.length || table[index] == null) {
            functions.remove(addr);
            return;
        }
        functions.put(addr, index);
    }

    Function get(int addr) {
        Integer index = functions.get(addr);
        if (index == null) {
            return null;
        }
        return table[index];
    }

    private static final int WORDSIZE = 4;

    private static void ERROR(Machine machine, String message) {
        try {
            machine.glk.glk.putChar('\n');
            machine.glk.glk.putString(message);
            machine.glk.glk.putChar('\n');
        } catch (IOException e) {
        }
    }

    private static boolean OBJ_IN_CLASS(Machine machine, int obj, int[] parameters) {
        return machine.state.load32(obj + 13 + parameters[7]) == parameters[2];
    }

    private static int FUNC_1_Z__Region(Machine machine, int addr, int[] parameters) {
        if (addr < 36 || addr >= machine.state.memorySize()) {
            return 0;
        }
        switch (machine.state.load8(addr) & 0xf0) {
        case 0xe0: case 0xf0:
            return 3;
        case 0xc0: case 0xd0:
            return 2;
        case 0x70:
            return addr >= machine.state.load32(8) ? 1 : 0;
        default:
            return 0;
        }
    }

    private static int FUNC_2_CP__Tab(Machine machine, int obj, int id, int[] parameters) {
        if (FUNC_1_Z__Region(machine, obj, parameters) != 1) {
            ERROR(machine, "[** Programming error: tried to find the ~.~ of (something) **]");
            return 0;
        }
        int otab = machine.state.load32(obj+16);
        if (otab == 0) {
            return 0;
        }
        int max = machine.state.load32(otab);
        return Instruction.binarySearch(machine.state, id, 2, otab+4, 10, max, 0, 0);
    }

    private static int FUNC_3_RA__Pr(Machine machine, int obj, int id, int[] parameters) {
        int cla = 0;
        if ((id & 0xffff0000) != 0) {
            cla = machine.state.load32(parameters[0] + 4*(id & 0xffff));
            if (FUNC_5_OC__Cl(machine, obj, cla, parameters) == 0) {
                return 0;
            }
            id >>>= 16;
            obj = cla;
        }
        int prop = FUNC_2_CP__Tab(machine, obj, id, parameters);
        if (prop == 0) {
            return 0;
        }
        if (cla == 0 && (id < parameters[1] || id >= parameters[1]+8) && OBJ_IN_CLASS(machine, obj, parameters)) {
            return 0;
        }
        if (obj != machine.state.load32(parameters[6]) && (machine.state.load8(prop + 9) & 1) != 0) {
            return 0;
        }
        return machine.state.load32(prop + 4);
    }

    private static int FUNC_4_RL__Pr(Machine machine, int obj, int id, int[] parameters) {
        int cla = 0;
        if ((id & 0xffff0000) != 0) {
            cla = machine.state.load32(parameters[0] + 4*(id & 0xffff));
            if (FUNC_5_OC__Cl(machine, obj, cla, parameters) == 0) {
                return 0;
            }
            id >>>= 16;
            obj = cla;
        }
        int prop = FUNC_2_CP__Tab(machine, obj, id, parameters);
        if (prop == 0) {
            return 0;
        }
        if (cla == 0 && (id < parameters[1] || id >= parameters[1]+8) && OBJ_IN_CLASS(machine, obj, parameters)) {
            return 0;
        }
        if (obj != machine.state.load32(parameters[6]) && (machine.state.load8(prop + 9) & 1) != 0) {
            return 0;
        }
        return WORDSIZE*machine.state.load16(prop + 2);
    }

    private static int FUNC_5_OC__Cl(Machine machine, int obj, int cla, int[] parameters) {
        switch (FUNC_1_Z__Region(machine, obj, parameters)) {
        case 3:
            return cla == parameters[5] ? 1 : 0;
        case 2:
            return cla == parameters[4] ? 1 : 0;
        case 1:
            break;
        default:
            return 0;
        }
        if (cla == parameters[2]) {
            if (obj == parameters[2] || obj == parameters[3] || obj == parameters[4] || obj == parameters[5] || OBJ_IN_CLASS(machine, obj, parameters)) {
                return 1;
            } else {
                return 0;
            }
        }
        if (cla == parameters[3]) {
            if (obj == parameters[2] || obj == parameters[3] || obj == parameters[4] || obj == parameters[5] || OBJ_IN_CLASS(machine, obj, parameters)) {
                return 0;
            } else {
                return 1;
            }
        }
        if (cla == parameters[4] || cla == parameters[5]) {
            return 0;
        }
        if (!OBJ_IN_CLASS(machine, cla, parameters)) {
            ERROR(machine, "[** Programming error: tried to apply 'ofclass' with non-class **]");
            return 0;
        }
        int inlist = FUNC_3_RA__Pr(machine, obj, 2, parameters);
        if (inlist == 0) {
            return 0;
        }
        int inlistlen = FUNC_4_RL__Pr(machine, obj, 2, parameters) / WORDSIZE;
        for (int jx = 0; jx < inlistlen; jx++) {
            if (machine.state.load32(inlist + 4*jx) == cla) {
                return 1;
            }
        }
        return 0;
    }

    private static int FUNC_6_RV__Pr(Machine machine, int obj, int id, int[] parameters) {
        int addr = FUNC_3_RA__Pr(machine, obj, id, parameters);
        if (addr == 0) {
            if (id > 0 && id < parameters[1]) {
                return machine.state.load32(parameters[8] + 4*id);
            } else {
                ERROR(machine, "[** Programming error: tried to read (something) **]");
                return 0;
            }
        }
        return machine.state.load32(addr);
    }

    private static int FUNC_7_OP__Pr(Machine machine, int obj, int id, int[] parameters) {
        switch (FUNC_1_Z__Region(machine, obj, parameters)) {
        case 3:
            return (id == parameters[1]+6 || id == parameters[1]+7) ? 1 : 0;
        case 2:
            return id == parameters[1]+5 ? 1 : 0;
        case 1:
            break;
        default:
            return 0;
        }
        if (id >= parameters[1] && id < parameters[1] + 8 && OBJ_IN_CLASS(machine, obj, parameters)) {
            return 1;
        }
        if (FUNC_3_RA__Pr(machine, obj, id, parameters) != 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int FUNC_8_CP__Tab(Machine machine, int obj, int id, int[] parameters) {
        if (FUNC_1_Z__Region(machine, obj, parameters) != 1) {
            ERROR(machine, "[** Programming error: tried to find the ~.~ of (something) **]");
            return 0;
        }
        int otab = machine.state.load32(obj + 4*(3 + parameters[7]/4));
        if (otab == 0) {
            return 0;
        }
        int max = machine.state.load32(otab);
        return Instruction.binarySearch(machine.state, id, 2, otab+4, 10, max, 0, 0);
    }

    private static int FUNC_9_RA__Pr(Machine machine, int obj, int id, int[] parameters) {
        int cla = 0;
        if ((id & 0xffff0000) != 0) {
            cla = machine.state.load32(parameters[0] + 4*(id & 0xffff));
            if (FUNC_11_OC__Cl(machine, obj, cla, parameters) == 0) {
                return 0;
            }
            id >>>= 16;
            obj = cla;
        }
        int prop = FUNC_8_CP__Tab(machine, obj, id, parameters);
        if (prop == 0) {
            return 0;
        }
        if (cla == 0 && (id < parameters[1] || id >= parameters[1]+8) && OBJ_IN_CLASS(machine, obj, parameters)) {
            return 0;
        }
        if (obj != machine.state.load32(parameters[6]) && (machine.state.load8(prop + 9) & 1) != 0) {
            return 0;
        }
        return machine.state.load32(prop + 4);
    }

    private static int FUNC_10_RL__Pr(Machine machine, int obj, int id, int[] parameters) {
        int cla = 0;
        if ((id & 0xffff0000) != 0) {
            cla = machine.state.load32(parameters[0] + 4*(id & 0xffff));
            if (FUNC_11_OC__Cl(machine, obj, cla, parameters) == 0) {
                return 0;
            }
            id >>>= 16;
            obj = cla;
        }
        int prop = FUNC_8_CP__Tab(machine, obj, id, parameters);
        if (prop == 0) {
            return 0;
        }
        if (cla == 0 && (id < parameters[1] || id >= parameters[1]+8) && OBJ_IN_CLASS(machine, obj, parameters)) {
            return 0;
        }
        if (obj != machine.state.load32(parameters[6]) && (machine.state.load8(prop + 9) & 1) != 0) {
            return 0;
        }
        return WORDSIZE*machine.state.load16(prop + 2);
    }

    private static int FUNC_11_OC__Cl(Machine machine, int obj, int cla, int[] parameters) {
        switch (FUNC_1_Z__Region(machine, obj, parameters)) {
        case 3:
            return cla == parameters[5] ? 1 : 0;
        case 2:
            return cla == parameters[4] ? 1 : 0;
        case 1:
            break;
        default:
            return 0;
        }
        if (cla == parameters[2]) {
            if (obj == parameters[2] || obj == parameters[3] || obj == parameters[4] || obj == parameters[5] || OBJ_IN_CLASS(machine, obj, parameters)) {
                return 1;
            } else {
                return 0;
            }
        }
        if (cla == parameters[3]) {
            if (obj == parameters[2] || obj == parameters[3] || obj == parameters[4] || obj == parameters[5] || OBJ_IN_CLASS(machine, obj, parameters)) {
                return 0;
            } else {
                return 1;
            }
        }
        if (cla == parameters[4] || cla == parameters[5]) {
            return 0;
        }
        if (!OBJ_IN_CLASS(machine, cla, parameters)) {
            ERROR(machine, "[** Programming error: tried to apply 'ofclass' with non-class **]");
            return 0;
        }
        int inlist = FUNC_9_RA__Pr(machine, obj, 2, parameters);
        if (inlist == 0) {
            return 0;
        }
        int inlistlen = FUNC_10_RL__Pr(machine, obj, 2, parameters) / WORDSIZE;
        for (int jx = 0; jx < inlistlen; jx++) {
            if (machine.state.load32(inlist + 4*jx) == cla) {
                return 1;
            }
        }
        return 0;
    }

    private static int FUNC_12_RV__Pr(Machine machine, int obj, int id, int[] parameters) {
        int addr = FUNC_9_RA__Pr(machine, obj, id, parameters);
        if (addr == 0) {
            if (id > 0 && id < parameters[1]) {
                return machine.state.load32(parameters[8] + 4*id);
            } else {
                ERROR(machine, "[** Programming error: tried to read (something) **]");
                return 0;
            }
        }
        return machine.state.load32(addr);
    }

    private static int FUNC_13_OP__Pr(Machine machine, int obj, int id, int[] parameters) {
        switch (FUNC_1_Z__Region(machine, obj, parameters)) {
        case 3:
            return (id == parameters[1]+6 || id == parameters[1]+7) ? 1 : 0;
        case 2:
            return id == parameters[1]+5 ? 1 : 0;
        case 1:
            break;
        default:
            return 0;
        }
        if (id >= parameters[1] && id < parameters[1] + 8 && OBJ_IN_CLASS(machine, obj, parameters)) {
            return 1;
        }
        if (FUNC_9_RA__Pr(machine, obj, id, parameters) != 0) {
            return 1;
        } else {
            return 0;
        }
    }

    abstract static class Function {
        final String name;

        private Function(String name, int index) {
            this.name = name;
            table[index] = this;
        }

        abstract int call(Machine machine, int[] args, int[] parameters);
    }

    private static int arg(int[] args, int index) {
        return args.length > index ? args[index] : 0;
    }

    static {
        new Function("FUNC_1_Z__Region", 1) {
            @Override int call(Machine machine, int[] args, int[] parameters) {
                return FUNC_1_Z__Region(machine, arg(args, 0), parameters);
            }
        };
        new Function("FUNC_2_CP__Tab", 2) {
            @Override int call(Machine machine, int[] args, int[] parameters) {
                return FUNC_2_CP__Tab(machine, arg(args, 0), arg(args, 1), parameters);
            }
        };
        new Function("FUNC_3_RA__Pr", 3) {
            @Override int call(Machine machine, int[] args, int[] parameters) {
                return FUNC_3_RA__Pr(machine, arg(args, 0), arg(args, 1), parameters);
            }
        };
        new Function("FUNC_4_RL__Pr", 4) {
            @Override int call(Machine machine, int[] args, int[] parameters) {
                return FUNC_4_RL__Pr(machine, arg(args, 0), arg(args, 1), parameters);
            }
        };
        new Function("FUNC_5_OC__Cl", 5) {
            @Override int call(Machine machine, int[] args, int[] parameters) {
                return FUNC_5_OC__Cl(machine, arg(args, 0), arg(args, 1), parameters);
            }
        };
        new Function("FUNC_6_RV__Pr", 6) {
            @Override int call(Machine machine, int[] args, int[] parameters) {
                return FUNC_6_RV__Pr(machine, arg(args, 0), arg(args, 1), parameters);
            }
        };
        new Function("FUNC_7_OP__Pr", 7) {
            @Override int call(Machine machine, int[] args, int[] parameters) {
                return FUNC_7_OP__Pr(machine, arg(args, 0), arg(args, 1), parameters);
            }
        };
        new Function("FUNC_8_CP__Tab", 8) {
            @Override int call(Machine machine, int[] args, int[] parameters) {
                return FUNC_8_CP__Tab(machine, arg(args, 0), arg(args, 1), parameters);
            }
        };
        new Function("FUNC_9_RA__Pr", 9) {
            @Override int call(Machine machine, int[] args, int[] parameters) {
                return FUNC_9_RA__Pr(machine, arg(args, 0), arg(args, 1), parameters);
            }
        };
        new Function("FUNC_10_RL__Pr", 10) {
            @Override int call(Machine machine, int[] args, int[] parameters) {
                return FUNC_10_RL__Pr(machine, arg(args, 0), arg(args, 1), parameters);
            }
        };
        new Function("FUNC_11_OC__Cl", 11) {
            @Override int call(Machine machine, int[] args, int[] parameters) {
                return FUNC_11_OC__Cl(machine, arg(args, 0), arg(args, 1), parameters);
            }
        };
        new Function("FUNC_12_RV__Pr", 12) {
            @Override int call(Machine machine, int[] args, int[] parameters) {
                return FUNC_12_RV__Pr(machine, arg(args, 0), arg(args, 1), parameters);
            }
        };
        new Function("FUNC_13_OP__Pr", 13) {
            @Override int call(Machine machine, int[] args, int[] parameters) {
                return FUNC_13_OP__Pr(machine, arg(args, 0), arg(args, 1), parameters);
            }
        };
    }
}
