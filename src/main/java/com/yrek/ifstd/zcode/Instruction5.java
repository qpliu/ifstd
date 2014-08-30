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

class Instruction5 {
    public static Instruction.Result executeNext(Machine machine) throws IOException {
        switch (machine.state.read8(machine.state.pc++)) {
        case 0:
            throw new IllegalArgumentException();
        case 1:
            operandsSS(machine);
            return insnJE(machine, 2);
        case 2:
            operandsSS(machine);
            return insnJL(machine);
        case 3:
            operandsSS(machine);
            return insnJG(machine);
        case 4:
            operandsSS(machine);
            return insnDEC_CHK(machine);
        case 5:
            operandsSS(machine);
            return insnINC_CHK(machine);
        case 6:
            operandsSS(machine);
            return insnJIN(machine);
        case 7:
            operandsSS(machine);
            return insnTEST(machine);
        case 8:
            operandsSS(machine);
            return insnOR(machine);
        case 9:
            operandsSS(machine);
            return insnAND(machine);
        case 10:
            operandsSS(machine);
            return insnTEST_ATTR(machine);
        case 11:
            operandsSS(machine);
            return insnSET_ATTR(machine);
        case 12:
            operandsSS(machine);
            return insnCLEAR_ATTR(machine);
        case 13:
            operandsSS(machine);
            return insnSTORE(machine);
        case 14:
            operandsSS(machine);
            return insnINSERT_OBJ(machine);
        case 15:
            operandsSS(machine);
            return insnLOADW(machine);
        case 16:
            operandsSS(machine);
            return insnLOADB(machine);
        case 17:
            operandsSS(machine);
            return insnGET_PROP(machine);
        case 18:
            operandsSS(machine);
            return insnGET_PROP_ADDR(machine);
        case 19:
            operandsSS(machine);
            return insnGET_NEXT_PROP(machine);
        case 20:
            operandsSS(machine);
            return insnADD(machine);
        case 21:
            operandsSS(machine);
            return insnSUB(machine);
        case 22:
            operandsSS(machine);
            return insnMUL(machine);
        case 23:
            operandsSS(machine);
            return insnDIV(machine);
        case 24:
            operandsSS(machine);
            return insnMOD(machine);
        case 25:
            operandsSS(machine);
            return insnCALL_2S(machine);
        case 26:
            operandsSS(machine);
            return insnCALL_2N(machine);
        case 27:
            operandsSS(machine);
            return insnSET_COLOR(machine, 2);
        case 28:
            operandsSS(machine);
            return insnTHROW(machine);
        case 29:
        case 30:
        case 31:
        case 32:
            throw new IllegalArgumentException();
        case 33:
            operandsSV(machine);
            return insnJE(machine, 2);
        case 34:
            operandsSV(machine);
            return insnJL(machine);
        case 35:
            operandsSV(machine);
            return insnJG(machine);
        case 36:
            operandsSV(machine);
            return insnDEC_CHK(machine);
        case 37:
            operandsSV(machine);
            return insnINC_CHK(machine);
        case 38:
            operandsSV(machine);
            return insnJIN(machine);
        case 39:
            operandsSV(machine);
            return insnTEST(machine);
        case 40:
            operandsSV(machine);
            return insnOR(machine);
        case 41:
            operandsSV(machine);
            return insnAND(machine);
        case 42:
            operandsSV(machine);
            return insnTEST_ATTR(machine);
        case 43:
            operandsSV(machine);
            return insnSET_ATTR(machine);
        case 44:
            operandsSV(machine);
            return insnCLEAR_ATTR(machine);
        case 45:
            operandsSV(machine);
            return insnSTORE(machine);
        case 46:
            operandsSV(machine);
            return insnINSERT_OBJ(machine);
        case 47:
            operandsSV(machine);
            return insnLOADW(machine);
        case 48:
            operandsSV(machine);
            return insnLOADB(machine);
        case 49:
            operandsSV(machine);
            return insnGET_PROP(machine);
        case 50:
            operandsSV(machine);
            return insnGET_PROP_ADDR(machine);
        case 51:
            operandsSV(machine);
            return insnGET_NEXT_PROP(machine);
        case 52:
            operandsSV(machine);
            return insnADD(machine);
        case 53:
            operandsSV(machine);
            return insnSUB(machine);
        case 54:
            operandsSV(machine);
            return insnMUL(machine);
        case 55:
            operandsSV(machine);
            return insnDIV(machine);
        case 56:
            operandsSV(machine);
            return insnMOD(machine);
        case 57:
            operandsSV(machine);
            return insnCALL_2S(machine);
        case 58:
            operandsSV(machine);
            return insnCALL_2N(machine);
        case 59:
            operandsSV(machine);
            return insnSET_COLOR(machine, 2);
        case 60:
            operandsSV(machine);
            return insnTHROW(machine);
        case 61:
        case 62:
        case 63:
        case 64:
            throw new IllegalArgumentException();
        case 65:
            operandsVS(machine);
            return insnJE(machine, 2);
        case 66:
            operandsVS(machine);
            return insnJL(machine);
        case 67:
            operandsVS(machine);
            return insnJG(machine);
        case 68:
            operandsVS(machine);
            return insnDEC_CHK(machine);
        case 69:
            operandsVS(machine);
            return insnINC_CHK(machine);
        case 70:
            operandsVS(machine);
            return insnJIN(machine);
        case 71:
            operandsVS(machine);
            return insnTEST(machine);
        case 72:
            operandsVS(machine);
            return insnOR(machine);
        case 73:
            operandsVS(machine);
            return insnAND(machine);
        case 74:
            operandsVS(machine);
            return insnTEST_ATTR(machine);
        case 75:
            operandsVS(machine);
            return insnSET_ATTR(machine);
        case 76:
            operandsVS(machine);
            return insnCLEAR_ATTR(machine);
        case 77:
            operandsVS(machine);
            return insnSTORE(machine);
        case 78:
            operandsVS(machine);
            return insnINSERT_OBJ(machine);
        case 79:
            operandsVS(machine);
            return insnLOADW(machine);
        case 80:
            operandsVS(machine);
            return insnLOADB(machine);
        case 81:
            operandsVS(machine);
            return insnGET_PROP(machine);
        case 82:
            operandsVS(machine);
            return insnGET_PROP_ADDR(machine);
        case 83:
            operandsVS(machine);
            return insnGET_NEXT_PROP(machine);
        case 84:
            operandsVS(machine);
            return insnADD(machine);
        case 85:
            operandsVS(machine);
            return insnSUB(machine);
        case 86:
            operandsVS(machine);
            return insnMUL(machine);
        case 87:
            operandsVS(machine);
            return insnDIV(machine);
        case 88:
            operandsVS(machine);
            return insnMOD(machine);
        case 89:
            operandsVS(machine);
            return insnCALL_2S(machine);
        case 90:
            operandsVS(machine);
            return insnCALL_2N(machine);
        case 91:
            operandsVS(machine);
            return insnSET_COLOR(machine, 2);
        case 92:
            operandsVS(machine);
            return insnTHROW(machine);
        case 93:
        case 94:
        case 95:
        case 96:
            throw new IllegalArgumentException();
        case 97:
            machine.noperands = 2;
            operandsVV(machine);
            return insnJE(machine, 2);
        case 98:
            operandsVV(machine);
            return insnJL(machine);
        case 99:
            operandsVV(machine);
            return insnJG(machine);
        case 100:
            operandsVV(machine);
            return insnDEC_CHK(machine);
        case 101:
            operandsVV(machine);
            return insnINC_CHK(machine);
        case 102:
            operandsVV(machine);
            return insnJIN(machine);
        case 103:
            operandsVV(machine);
            return insnTEST(machine);
        case 104:
            operandsVV(machine);
            return insnOR(machine);
        case 105:
            operandsVV(machine);
            return insnAND(machine);
        case 106:
            operandsVV(machine);
            return insnTEST_ATTR(machine);
        case 107:
            operandsVV(machine);
            return insnSET_ATTR(machine);
        case 108:
            operandsVV(machine);
            return insnCLEAR_ATTR(machine);
        case 109:
            operandsVV(machine);
            return insnSTORE(machine);
        case 110:
            operandsVV(machine);
            return insnINSERT_OBJ(machine);
        case 111:
            operandsVV(machine);
            return insnLOADW(machine);
        case 112:
            operandsVV(machine);
            return insnLOADB(machine);
        case 113:
            operandsVV(machine);
            return insnGET_PROP(machine);
        case 114:
            operandsVV(machine);
            return insnGET_PROP_ADDR(machine);
        case 115:
            operandsVV(machine);
            return insnGET_NEXT_PROP(machine);
        case 116:
            operandsVV(machine);
            return insnADD(machine);
        case 117:
            operandsVV(machine);
            return insnSUB(machine);
        case 118:
            operandsVV(machine);
            return insnMUL(machine);
        case 119:
            operandsVV(machine);
            return insnDIV(machine);
        case 120:
            operandsVV(machine);
            return insnMOD(machine);
        case 121:
            operandsVV(machine);
            return insnCALL_2S(machine);
        case 122:
            operandsVV(machine);
            return insnCALL_2N(machine);
        case 123:
            operandsVV(machine);
            return insnSET_COLOR(machine, 2);
        case 124:
            operandsVV(machine);
            return insnTHROW(machine);
        case 125:
        case 126:
        case 127:
            throw new IllegalArgumentException();
        case 128:
            return insnJZ(machine, operandL(machine));
        case 129:
            return insnGET_SIBLING(machine, operandL(machine));
        case 130:
            return insnGET_CHILD(machine, operandL(machine));
        case 131:
            return insnGET_PARENT(machine, operandL(machine));
        case 132:
            return insnGET_PROP_LEN(machine, operandL(machine));
        case 133:
            return insnINC(machine, operandL(machine));
        case 134:
            return insnDEC(machine, operandL(machine));
        case 135:
            return insnPRINT_ADDR(machine, operandL(machine));
        case 136:
            return insnCALL_1S(machine, operandL(machine));
        case 137:
            return insnREMOVE_OBJ(machine, operandL(machine));
        case 138:
            return insnPRINT_OBJ(machine, operandL(machine));
        case 139:
            return insnRET(machine, operandL(machine));
        case 140:
            return insnJUMP(machine, operandL(machine));
        case 141:
            return insnPRINT_PADDR(machine, operandL(machine));
        case 142:
            return insnLOAD(machine, operandL(machine));
        case 143:
            return insnCALL_1N(machine, operandL(machine));
        case 144:
            return insnJZ(machine, operandS(machine));
        case 145:
            return insnGET_SIBLING(machine, operandS(machine));
        case 146:
            return insnGET_CHILD(machine, operandS(machine));
        case 147:
            return insnGET_PARENT(machine, operandS(machine));
        case 148:
            return insnGET_PROP_LEN(machine, operandS(machine));
        case 149:
            return insnINC(machine, operandS(machine));
        case 150:
            return insnDEC(machine, operandS(machine));
        case 151:
            return insnPRINT_ADDR(machine, operandS(machine));
        case 152:
            return insnCALL_1S(machine, operandS(machine));
        case 153:
            return insnREMOVE_OBJ(machine, operandS(machine));
        case 154:
            return insnPRINT_OBJ(machine, operandS(machine));
        case 155:
            return insnRET(machine, operandS(machine));
        case 156:
            return insnJUMP(machine, operandS(machine));
        case 157:
            return insnPRINT_PADDR(machine, operandS(machine));
        case 158:
            return insnLOAD(machine, operandS(machine));
        case 159:
            return insnCALL_1N(machine, operandS(machine));
        case 160:
            return insnJZ(machine, operandV(machine));
        case 161:
            return insnGET_SIBLING(machine, operandV(machine));
        case 162:
            return insnGET_CHILD(machine, operandV(machine));
        case 163:
            return insnGET_PARENT(machine, operandV(machine));
        case 164:
            return insnGET_PROP_LEN(machine, operandV(machine));
        case 165:
            return insnINC(machine, operandV(machine));
        case 166:
            return insnDEC(machine, operandV(machine));
        case 167:
            return insnPRINT_ADDR(machine, operandV(machine));
        case 168:
            return insnCALL_1S(machine, operandV(machine));
        case 169:
            return insnREMOVE_OBJ(machine, operandV(machine));
        case 170:
            return insnPRINT_OBJ(machine, operandV(machine));
        case 171:
            return insnRET(machine, operandV(machine));
        case 172:
            return insnJUMP(machine, operandV(machine));
        case 173:
            return insnPRINT_PADDR(machine, operandV(machine));
        case 174:
            return insnLOAD(machine, operandV(machine));
        case 175:
            return insnCALL_1N(machine, operandV(machine));
        case 176:
            return insnRTRUE(machine);
        case 177:
            return insnRFALSE(machine);
        case 178:
            return insnPRINT(machine);
        case 179:
            return insnPRINT_RET(machine);
        case 180:
            return Instruction.Result.Continue; // NOP
        case 181:
        case 182:
            throw new IllegalArgumentException();
        case 183:
            return insnRESTART(machine);
        case 184:
            return insnRET_POPPED(machine);
        case 185:
            return insnCATCH(machine);
        case 186:
            return insnQUIT(machine);
        case 187:
            return insnNEW_LINE(machine);
        case 188:
            return Instruction.Result.Continue; // SHOW_STATUS -> NOP
        case 189:
            return insnVERIFY(machine);
        case 190:
            return insnEXTENDED(machine);
        case 191:
            return insnPIRACY(machine);
        case 192:
            throw new IllegalArgumentException();
        case 193:
            return insnJE(machine, operandsVAR(machine));
        case 194:
            operandsVAR(machine);
            return insnJL(machine);
        case 195:
            operandsVAR(machine);
            return insnJG(machine);
        case 196:
            operandsVAR(machine);
            return insnDEC_CHK(machine);
        case 197:
            operandsVAR(machine);
            return insnINC_CHK(machine);
        case 198:
            operandsVAR(machine);
            return insnJIN(machine);
        case 199:
            operandsVAR(machine);
            return insnTEST(machine);
        case 200:
            operandsVAR(machine);
            return insnOR(machine);
        case 201:
            operandsVAR(machine);
            return insnAND(machine);
        case 202:
            operandsVAR(machine);
            return insnTEST_ATTR(machine);
        case 203:
            operandsVAR(machine);
            return insnSET_ATTR(machine);
        case 204:
            operandsVAR(machine);
            return insnCLEAR_ATTR(machine);
        case 205:
            operandsVAR(machine);
            return insnSTORE(machine);
        case 206:
            operandsVAR(machine);
            return insnINSERT_OBJ(machine);
        case 207:
            operandsVAR(machine);
            return insnLOADW(machine);
        case 208:
            operandsVAR(machine);
            return insnLOADB(machine);
        case 209:
            operandsVAR(machine);
            return insnGET_PROP(machine);
        case 210:
            operandsVAR(machine);
            return insnGET_PROP_ADDR(machine);
        case 211:
            operandsVAR(machine);
            return insnGET_NEXT_PROP(machine);
        case 212:
            operandsVAR(machine);
            return insnADD(machine);
        case 213:
            operandsVAR(machine);
            return insnSUB(machine);
        case 214:
            operandsVAR(machine);
            return insnMUL(machine);
        case 215:
            operandsVAR(machine);
            return insnDIV(machine);
        case 216:
            operandsVAR(machine);
            return insnMOD(machine);
        case 217:
            operandsVAR(machine);
            return insnCALL_2S(machine);
        case 218:
            operandsVAR(machine);
            return insnCALL_2N(machine);
        case 219:
            return insnSET_COLOR(machine, operandsVAR(machine));
        case 220:
            operandsVAR(machine);
            return insnTHROW(machine);
        case 221:
        case 222:
        case 223:
            throw new IllegalArgumentException();
        case 224:
            return insnCALL_VS(machine, operandsVAR(machine));
        case 225:
            operandsVAR(machine);
            return insnSTOREW(machine);
        case 226:
            operandsVAR(machine);
            return insnSTOREB(machine);
        case 227:
            operandsVAR(machine);
            return insnPUT_PROP(machine);
        case 228:
            int oldPc = machine.state.pc-1;
            return insnREAD(machine, operandsVAR(machine), oldPc);
        case 229:
            operandsVAR(machine);
            return insnPRINT_CHAR(machine);
        case 230:
            operandsVAR(machine);
            return insnPRINT_NUM(machine);
        case 231:
            operandsVAR(machine);
            return insnRANDOM(machine);
        case 232:
            operandsVAR(machine);
            return insnPUSH(machine);
        case 233:
            operandsVAR(machine);
            return insnPULL(machine);
        case 234:
            operandsVAR(machine);
            return insnSPLIT_WINDOW(machine);
        case 235:
            operandsVAR(machine);
            return insnSET_WINDOW(machine);
        case 236:
            return insnCALL_VS(machine, operandsVAR2(machine));
        case 237:
            operandsVAR(machine);
            return insnERASE_WINDOW(machine);
        case 238:
            operandsVAR(machine);
            return insnERASE_LINE(machine);
        case 239:
            operandsVAR(machine);
            return insnSET_CURSOR(machine);
        case 240:
            operandsVAR(machine);
            return insnGET_CURSOR(machine);
        case 241:
            operandsVAR(machine);
            return insnSET_TEXT_STYLE(machine);
        case 242:
            operandsVAR(machine);
            return insnBUFFER_MODE(machine);
        case 243:
            operandsVAR(machine);
            return insnOUTPUT_STREAM(machine);
        case 244:
            operandsVAR(machine);
            return insnINPUT_STREAM(machine);
        case 245:
            operandsVAR(machine);
            return insnSOUND_EFFECT(machine);
        case 246:
            oldPc = machine.state.pc-1;
            operandsVAR(machine);
            return insnREAD_CHAR(machine,oldPc);
        case 247:
            return insnSCAN_TABLE(machine, operandsVAR(machine));
        case 248:
            operandsVAR(machine);
            return insnNOT(machine);
        case 249:
            return insnCALL_VN(machine, operandsVAR(machine));
        case 250:
            return insnCALL_VN(machine, operandsVAR2(machine));
        case 251:
            return insnTOKENIZE(machine, operandsVAR(machine));
        case 252:
            return insnENCODE_TEXT(machine, operandsVAR(machine));
        case 253:
            return insnCOPY_TABLE(machine, operandsVAR(machine));
        case 254:
            return insnPRINT_TABLE(machine, operandsVAR(machine));
        case 255:
            operandsVAR(machine);
            return insnCHECK_ARG_COUNT(machine);
        default:
            throw new AssertionError();
        }
    }

    private static void operandsSS(Machine machine) {
        machine.operand[0] = machine.state.read8(machine.state.pc++);
        machine.operand[1] = machine.state.read8(machine.state.pc++);
    }

    private static void operandsSV(Machine machine) {
        machine.operand[0] = machine.state.read8(machine.state.pc++);
        machine.operand[1] = machine.state.readVar(machine.state.read8(machine.state.pc++));
    }

    private static void operandsVS(Machine machine) {
        machine.operand[0] = machine.state.readVar(machine.state.read8(machine.state.pc++));
        machine.operand[1] = machine.state.read8(machine.state.pc++);
    }

    private static void operandsVV(Machine machine) {
        machine.operand[0] = machine.state.readVar(machine.state.read8(machine.state.pc++));
        machine.operand[1] = machine.state.readVar(machine.state.read8(machine.state.pc++));
    }

    private static int operandL(Machine machine) {
        int pc = machine.state.pc;
        machine.state.pc = pc + 2;
        return machine.state.read16(pc);
    }

    private static int operandS(Machine machine) {
        return machine.state.read8(machine.state.pc++);
    }

    private static int operandV(Machine machine) {
        return machine.state.readVar(machine.state.read8(machine.state.pc++));
    }

    /* Return number of operands. */
    private static int operandsVAR(Machine machine) {
        int types = machine.state.read8(machine.state.pc++);
        for (int i = 0; i < 4; i++) {
            switch (types & 192) {
            case 192:
                return i;
            case 128:
                machine.operand[i] = machine.state.readVar(machine.state.read8(machine.state.pc++));
                break;
            case 64:
                machine.operand[i] = machine.state.read8(machine.state.pc++);
                break;
            case 0:
                machine.operand[i] = machine.state.read16(machine.state.pc);
                machine.state.pc += 2;
                break;
            default:
                throw new AssertionError();
            }
            types <<= 2;
        }
        return 4;
    }

    /* Return number of operands. */
    private static int operandsVAR2(Machine machine) {
        int pc = machine.state.pc;
        machine.state.pc += 2;
        int types = machine.state.read16(pc);
        for (int i = 0; i < 8; i++) {
            switch (types & 49152) {
            case 49152:
                return i;
            case 32768:
                machine.operand[i] = machine.state.readVar(machine.state.read8(machine.state.pc++));
                break;
            case 16384:
                machine.operand[i] = machine.state.read8(machine.state.pc++);
                break;
            case 0:
                machine.operand[i] = machine.state.read16(machine.state.pc);
                machine.state.pc += 2;
                break;
            default:
                throw new AssertionError();
            }
            types <<= 2;
        }
        return 8;
    }

    private static void store(State state, int value) {
        state.storeVar(state.read8(state.pc++), value);
    }

    private static Instruction.Result branch(Machine machine, boolean result) {
        int branch = machine.state.read8(machine.state.pc++);
        if (result == ((branch & 128) == 0)) {
            if ((branch & 64) == 0) {
                machine.state.pc++;
            }
            return Instruction.Result.Continue;
        }
        if ((branch & 64) != 0) {
            branch &= 63;
        } else {
            branch = ((branch & 63) << 8) | machine.state.read8(machine.state.pc++);
            if (branch >= 8192) {
                branch -= 16384;
            }
        }
        switch (branch) {
        case 0:
        case 1:
            return retVal(machine, branch);
        default:
            machine.state.pc += branch - 2;
            return Instruction.Result.Tick;
        }
    }

    private static void literalString(Machine machine) throws IOException {
        final State state = machine.state;
        machine.string.setLength(0);
        ZSCII.decode(machine.string, state, state.pc);
        while (state.read8(state.pc) < 128) {
            state.pc += 2;
        }
        state.pc += 2;
    }

    private static Instruction.Result retVal(Machine machine, int val) {
        StackFrame frame = machine.state.frame;
        machine.state.frame = frame.parent;
        machine.state.pc = frame.returnAddress;
        if (frame.result >= 0) {
            machine.state.storeVar(frame.result, val);
        }
        return Instruction.Result.Tick;
    }

    @SuppressWarnings("fallthrough")
    private static Instruction.Result doCall(Machine machine, int addr, int a1, int a2, int a3, int a4, int a5, int a6, int a7, int argc, int result) {
        if (addr == 0) {
            if (result >= 0) {
                machine.state.storeVar(result, 0);
            }
            return Instruction.Result.Tick;
        }
        addr = machine.state.unpack(addr, true);
        StackFrame frame = new StackFrame(machine.state.frame, machine.state.pc, result, 127 >> (7-argc), machine.state.read8(addr));
        addr++;
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
        return Instruction.Result.Tick;
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
            StringBuilder sb = machine.string;
            sb.setLength(0);
            ZSCII.decode(sb, machine.state, objProperties+1);
            if (machine.upperWindow == null) {
                machine.upperWindow = machine.glk.glk.windowOpen(machine.mainWindow, GlkWindowArrangement.MethodAbove | GlkWindowArrangement.MethodFixed | GlkWindowArrangement.MethodNoBorder, 1, GlkWindow.TypeTextGrid, 1);
                if (machine.upperWindow != null) {
                    machine.glk.add(machine.upperWindow);
                }
            }
            if (machine.upperWindow != null) {
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
        }
    }

    private static void updateWindowsPostInput(Machine machine) throws IOException {
        if (machine.state.version > 3) {
            if (machine.upperWindowCurrentHeight != machine.upperWindowTargetHeight) {
                resizeUpperWindow(machine, machine.upperWindowTargetHeight);
            }
            machine.upperWindowInitialHeight = machine.upperWindowCurrentHeight;
        }
    }

    private static void resizeUpperWindow(Machine machine, int height) throws IOException {
        machine.upperWindowCurrentHeight = height;
        if (height == 0) {
            if (machine.upperWindow != null) {
                machine.upperWindow.close();
                machine.upperWindow = null;
            }
        } else if (machine.upperWindow == null) {
            machine.upperWindow = machine.glk.glk.windowOpen(machine.mainWindow, GlkWindowArrangement.MethodAbove | GlkWindowArrangement.MethodFixed | GlkWindowArrangement.MethodNoBorder, height, GlkWindow.TypeTextGrid, 1);
            machine.glk.add(machine.upperWindow);
        } else {
            machine.upperWindow.getParent().setArrangement(GlkWindowArrangement.MethodAbove | GlkWindowArrangement.MethodFixed | GlkWindowArrangement.MethodNoBorder, height, machine.upperWindow);
            if (machine.state.version <= 3) {
                machine.upperWindow.clear();
            }
        }
    }

    private static Instruction.Result insnJE(Machine machine, int argc) {
        boolean result = false;
        for (int i = 1; i < argc; i++) {
            if (machine.operand[0] == machine.operand[i]) {
                result = true;
                break;
            }
        }
        return branch(machine, result);
    }

    private static Instruction.Result insnJL(Machine machine) {
        return branch(machine, (short) machine.operand[0] < (short) machine.operand[1]);
    }

    private static Instruction.Result insnJG(Machine machine) {
        return branch(machine, (short) machine.operand[0] > (short) machine.operand[1]);
    }

    private static Instruction.Result insnDEC_CHK(Machine machine) {
        short val = (short) (machine.state.readVar(machine.operand[0]) - 1);
        machine.state.storeVar(machine.operand[0], val&65535);
        return branch(machine, val < (short) machine.operand[1]);
    }

    private static Instruction.Result insnINC_CHK(Machine machine) {
        short val = (short) (machine.state.readVar(machine.operand[0]) + 1);
        machine.state.storeVar(machine.operand[0], val&65535);
        return branch(machine, val > (short) machine.operand[1]);
    }

    private static Instruction.Result insnJIN(Machine machine) {
        return branch(machine, machine.state.objParent(machine.operand[0]) == machine.operand[1]);
    }

    private static Instruction.Result insnTEST(Machine machine) {
        return branch(machine, (machine.operand[0] & machine.operand[1]) == machine.operand[1]);
    }

    private static Instruction.Result insnOR(Machine machine) {
        store(machine.state, machine.operand[0] | machine.operand[1]);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnAND(Machine machine) {
        store(machine.state, machine.operand[0] & machine.operand[1]);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnTEST_ATTR(Machine machine) {
        return branch(machine, machine.state.objAttr(machine.operand[0], machine.operand[1]));
    }

    private static Instruction.Result insnSET_ATTR(Machine machine) {
        machine.state.objSetAttr(machine.operand[0], machine.operand[1], true);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnCLEAR_ATTR(Machine machine) {
        machine.state.objSetAttr(machine.operand[0], machine.operand[1], false);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnSTORE(Machine machine) {
        machine.state.overwriteVar(machine.operand[0], machine.operand[1]);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnINSERT_OBJ(Machine machine) {
        machine.state.objMove(machine.operand[0], machine.operand[1]);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnLOADW(Machine machine) {
        store(machine.state, machine.state.read16((machine.operand[0] + 2*machine.operand[1])&65535));
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnLOADB(Machine machine) {
        store(machine.state, machine.state.read8((machine.operand[0] + machine.operand[1])&65535));
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnGET_PROP(Machine machine) {
        store(machine.state, machine.state.getProp(machine.operand[0], machine.operand[1]));
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnGET_PROP_ADDR(Machine machine) {
        store(machine.state, machine.state.getPropAddr(machine.operand[0], machine.operand[1]));
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnGET_NEXT_PROP(Machine machine) {
        store(machine.state, machine.state.getNextProp(machine.operand[0], machine.operand[1]));
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnADD(Machine machine) {
        store(machine.state, (machine.operand[0] + machine.operand[1])&65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnSUB(Machine machine) {
        store(machine.state, (machine.operand[0] - machine.operand[1])&65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnMUL(Machine machine) {
        store(machine.state, (((short) machine.operand[0]) * ((short) machine.operand[1]))&65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnDIV(Machine machine) {
        store(machine.state, (((short) machine.operand[0]) / ((short) machine.operand[1]))&65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnMOD(Machine machine) {
        store(machine.state, (((short) machine.operand[0]) % ((short) machine.operand[1]))&65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnCALL_2S(Machine machine) {
        int store = machine.state.read8(machine.state.pc++);
        return doCall(machine, machine.operand[0], machine.operand[1], 0, 0, 0, 0, 0, 0, 1, store);
    }

    private static Instruction.Result insnCALL_2N(Machine machine) {
        return doCall(machine, machine.operand[0], machine.operand[1], 0, 0, 0, 0, 0, 0, 1, -1);
    }

    private static final int[] colors = new int[] {
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

    private static Instruction.Result insnSET_COLOR(Machine machine, int argc) {
        setColors(machine, argc > 2 ? machine.operand[2] : -1, colors[machine.operand[0]], colors[machine.operand[1]]);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnTHROW(Machine machine) {
        while (machine.state.frame.index > machine.operand[1]) {
            machine.state.frame = machine.state.frame.parent;
        }
        return retVal(machine, machine.operand[0]);
    }

    private static Instruction.Result insnJZ(Machine machine, int operand) {
        return branch(machine, operand == 0);
    }

    private static Instruction.Result insnGET_SIBLING(Machine machine, int operand) {
        int result = machine.state.objSibling(operand);
        store(machine.state, result);
        return branch(machine, result != 0);
    }

    private static Instruction.Result insnGET_CHILD(Machine machine, int operand) {
        int result = machine.state.objChild(operand);
        store(machine.state, result);
        return branch(machine, result != 0);
    }

    private static Instruction.Result insnGET_PARENT(Machine machine, int operand) {
        store(machine.state, machine.state.objParent(operand));
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnGET_PROP_LEN(Machine machine, int operand) {
        store(machine.state, machine.state.getPropLen(operand));
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnINC(Machine machine, int operand) {
        machine.state.storeVar(operand, (machine.state.readVar(operand) + 1) & 65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnDEC(Machine machine, int operand) {
        machine.state.storeVar(operand, (machine.state.readVar(operand) - 1) & 65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnPRINT_ADDR(Machine machine, int operand) throws IOException {
        Stream3 stream3 = machine.getStream3();
        if (stream3 != null) {
            ZSCII.decode(stream3, machine.state, operand);
            return Instruction.Result.Continue;
        }
        GlkStream stream = machine.getOutputStream();
        if (stream != null) {
            machine.string.setLength(0);
            ZSCII.decode(machine.string, machine.state, operand);
            stream.putStringUni(new UnicodeString.US(machine.string));
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnCALL_1S(Machine machine, int operand) {
        int store = machine.state.read8(machine.state.pc++);
        return doCall(machine, operand, 0, 0, 0, 0, 0, 0, 0, 0, store);
    }

    private static Instruction.Result insnREMOVE_OBJ(Machine machine, int operand) {
        machine.state.objMove(operand, 0);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnPRINT_OBJ(Machine machine, int operand) throws IOException {
        Stream3 stream3 = machine.getStream3();
        if (stream3 != null) {
            ZSCII.decode(stream3, machine.state, machine.state.objProperties(operand) + 1);
            return Instruction.Result.Continue;
        }
        GlkStream stream = machine.getOutputStream();
        if (stream != null) {
            machine.string.setLength(0);
            ZSCII.decode(machine.string, machine.state, machine.state.objProperties(operand) + 1);
            stream.putStringUni(new UnicodeString.US(machine.string));
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnRET(Machine machine, int operand) {
        return retVal(machine, operand);
    }

    private static Instruction.Result insnJUMP(Machine machine, int operand) {
        short offset = (short) operand;
        machine.state.pc += (int) offset - 2;
        return Instruction.Result.Tick;
    }

    private static Instruction.Result insnPRINT_PADDR(Machine machine, int operand) throws IOException {
        Stream3 stream3 = machine.getStream3();
        if (stream3 != null) {
            ZSCII.decode(stream3, machine.state, machine.state.unpack(operand, false));
            return Instruction.Result.Continue;
        }
        GlkStream stream = machine.getOutputStream();
        if (stream != null) {
            machine.string.setLength(0);
            ZSCII.decode(machine.string, machine.state, machine.state.unpack(operand, false));
            stream.putStringUni(new UnicodeString.US(machine.string));
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnLOAD(Machine machine, int operand) {
        store(machine.state, machine.state.peekVar(operand));
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnCALL_1N(Machine machine, int operand) {
        return doCall(machine, operand, 0, 0, 0, 0, 0, 0, 0, 0, -1);
    }

    private static Instruction.Result insnRTRUE(Machine machine) {
        return retVal(machine, 1);
    }

    private static Instruction.Result insnRFALSE(Machine machine) {
        return retVal(machine, 0);
    }

    private static Instruction.Result insnPRINT(Machine machine) throws IOException {
        literalString(machine);
        Stream3 stream3 = machine.getStream3();
        if (stream3 != null) {
            stream3.append(machine.string);
            return Instruction.Result.Continue;
        }
        GlkStream stream = machine.getOutputStream();
        if (stream != null) {
            stream.putString(machine.string);
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnPRINT_RET(Machine machine) throws IOException {
        literalString(machine);
        Stream3 stream3 = machine.getStream3();
        if (stream3 != null) {
            stream3.append(machine.string);
            stream3.append('\n');
            return retVal(machine, 1);
        }
        GlkStream stream = machine.getOutputStream();
        if (stream != null) {
            stream.putString(machine.string.append('\n'));
        }
        return retVal(machine, 1);
    }

    private static Instruction.Result insnRESTART(Machine machine) throws IOException {
        machine.state.copyFrom(machine.load(), false, true);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnRET_POPPED(Machine machine) {
        return retVal(machine, machine.state.frame.pop());
    }

    private static Instruction.Result insnCATCH(Machine machine) {
        store(machine.state, machine.state.frame.index);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnQUIT(Machine machine) {
        return Instruction.Result.Quit;
    }

    private static Instruction.Result insnNEW_LINE(Machine machine) throws IOException {
        Stream3 stream3 = machine.getStream3();
        if (stream3 != null) {
            stream3.append('\n');
            return Instruction.Result.Continue;
        }
        GlkStream stream = machine.getOutputStream();
        if (stream != null) {
            stream.putChar('\n');
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnVERIFY(Machine machine) {
        return branch(machine, true);
    }

    private static Instruction.Result insnEXTENDED(Machine machine) throws IOException {
        switch (machine.state.read8(machine.state.pc++)) {
        case 0:
            operandsVAR(machine);
            return insnSAVE(machine);
        case 1:
            operandsVAR(machine);
            return insnRESTORE(machine);
        case 2:
            operandsVAR(machine);
            return insnLOG_SHIFT(machine);
        case 3:
            operandsVAR(machine);
            return insnART_SHIFT(machine);
        case 4:
            operandsVAR(machine);
            return insnSET_FONT(machine);
        case 9:
            operandsVAR(machine);
            return insnSAVE_UNDO(machine);
        case 10:
            operandsVAR(machine);
            return insnRESTORE_UNDO(machine);
        case 11:
            operandsVAR(machine);
            return insnPRINT_UNICODE(machine);
        case 12:
            operandsVAR(machine);
            return insnCHECK_UNICODE(machine);
        case 13:
            return insnSET_TRUE_COLOR(machine, operandsVAR(machine));
        default:
            throw new IllegalArgumentException();
        }
    }

    private static Instruction.Result insnPIRACY(Machine machine) {
        return branch(machine, true);
    }

    private static Instruction.Result insnCALL_VS(Machine machine, int argc) {
        int store = machine.state.read8(machine.state.pc++);
        return doCall(machine,
                      machine.operand[0], machine.operand[1],
                      machine.operand[2], machine.operand[3],
                      machine.operand[4], machine.operand[5],
                      machine.operand[6], machine.operand[7],
                      argc - 1, store);
    }

    private static Instruction.Result insnSTOREW(Machine machine) {
        machine.state.store16((machine.operand[0] + 2*machine.operand[1])&65535, machine.operand[2]);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnSTOREB(Machine machine) {
        machine.state.store8((machine.operand[0] + machine.operand[1])&65535, machine.operand[2]);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnPUT_PROP(Machine machine) {
        int addr = machine.state.getPropAddr(machine.operand[0], machine.operand[1]);
        switch (machine.state.getPropLen(addr)) {
        case 1:
            machine.state.store8(addr, machine.operand[2]);
            break;
        case 2:
            machine.state.store16(addr, machine.operand[2]);
            break;
        default:
            throw new IllegalArgumentException();
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnREAD(Machine machine, int argc, int oldPc) throws IOException {
        int a0 = machine.operand[0];
        int a1 = argc > 1 ? machine.operand[1] : 0;
        int a2 = argc > 2 ? machine.operand[2] : 0;
        int a3 = argc > 3 ? machine.operand[3] : 0;
        updateWindowsPreInput(machine);
        int bufferAddress = a0+2;
        machine.mainWindow.requestLineEvent(machine.state.getBuffer(bufferAddress, machine.state.read8(a0)), 0);
        GlkEvent event;
        for (;;) {
            event = machine.glk.glk.select();
            if (event.type == GlkEvent.TypeLineInput) {
                break;
            } else if (machine.suspending) {
                machine.state.pc = oldPc;
                return Instruction.Result.Tick;
            }
            machine.handleEvent(event);
        }
        machine.state.store8(a0+1, event.val1);
        for (int i = 0; i < event.val1; i++) {
            machine.state.store8(bufferAddress+i, Character.toLowerCase(machine.state.read8(bufferAddress+i)));
        }
        if (a1 != 0) {
            machine.state.getDictionary().parse(bufferAddress, event.val1, a1);
        }
        updateWindowsPostInput(machine);
        store(machine.state, 13);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnPRINT_CHAR(Machine machine) throws IOException {
        Stream3 stream3 = machine.getStream3();
        if (stream3 != null) {
            ZSCII.appendZSCII(stream3, machine.state, machine.operand[0]);
            return Instruction.Result.Continue;
        }
        GlkStream stream = machine.getOutputStream();
        if (stream != null) {
            machine.string.setLength(0);
            ZSCII.appendZSCII(machine.string, machine.state, machine.operand[0]);
            stream.putStringUni(new UnicodeString.US(machine.string));
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnPRINT_NUM(Machine machine) throws IOException {
        Stream3 stream3 = machine.getStream3();
        if (stream3 != null) {
            stream3.append(String.valueOf((short) machine.operand[0]));
            return Instruction.Result.Continue;
        }
        GlkStream stream = machine.getOutputStream();
        if (stream != null) {
            stream.putString(String.valueOf((short) machine.operand[0]));
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnRANDOM(Machine machine) {
        short a0 = (short) machine.operand[0];
        if (a0 == 0) {
            machine.random.setSeed(System.nanoTime());
            store(machine.state, 0);
        } else if (a0 < 0) {
            machine.random.setSeed(-a0);
            store(machine.state, 0);
        } else {
            store(machine.state, machine.random.nextInt(a0) + 1);
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnPUSH(Machine machine) {
        machine.state.frame.push(machine.operand[0]);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnPULL(Machine machine) {
        machine.state.overwriteVar(machine.operand[0], machine.state.frame.pop());
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnSPLIT_WINDOW(Machine machine) throws IOException {
        int a0 = machine.operand[0];
        if (a0 >= machine.upperWindowCurrentHeight) {
            resizeUpperWindow(machine, a0);
        }
        machine.upperWindowTargetHeight = a0;
        if (machine.upperWindow != null) {
            machine.upperWindow.moveCursor(0, 0);
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnSET_WINDOW(Machine machine) throws IOException {
        machine.currentWindow = machine.operand[0];
        if (machine.currentWindow != 0 && machine.upperWindow != null) {
            machine.upperWindow.moveCursor(0, 0);
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnERASE_WINDOW(Machine machine) throws IOException {
        int a0 = machine.operand[0];
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
                machine.upperWindow.moveCursor(0, 0);
            }
            machine.mainWindow.clear();
        } else if (machine.upperWindow != null) {
            machine.upperWindow.clear();
            machine.upperWindow.moveCursor(0, 0);
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnERASE_LINE(Machine machine) throws IOException {
        int a0 = machine.operand[0];
        if (a0 != 1 || machine.currentWindow == 0 || machine.upperWindow == null) {
            return Instruction.Result.Continue;
        }
        int x = machine.upperWindow.getCursorX();
        int y = machine.upperWindow.getCursorY();
        for (int i = y; i < machine.screenWidth; i++) {
            machine.upperWindow.getStream().putChar(' ');
        }
        machine.upperWindow.moveCursor(x, y);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnSET_CURSOR(Machine machine) throws IOException {
        int a0 = (short) machine.operand[0];
        int a1 = (short) machine.operand[1];
        if (machine.currentWindow != 0 && machine.upperWindow != null) {
            GlkWindowSize size = machine.upperWindow.getSize();
            int x = a1 > 0 ? a1 - 1 : a1 < 0 ? size.width + a1 : machine.upperWindow.getCursorX();
            int y = a0 > 0 ? a0 - 1 : a0 < 0 ? size.height - a0 : machine.upperWindow.getCursorY();
            machine.upperWindow.moveCursor(x, y);
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnGET_CURSOR(Machine machine) throws IOException {
        int a0 = machine.operand[0];
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
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnSET_TEXT_STYLE(Machine machine) throws IOException {
        int style;
        switch (machine.operand[0]) {
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
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnBUFFER_MODE(Machine machine) {
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnOUTPUT_STREAM(Machine machine) {
        short a0 = (short) machine.operand[0];
        switch (a0) {
        case 1:
            machine.stream1 = true;
            break;
        case -1:
            machine.stream1 = false;
            break;
        case 3:
            machine.stream3[machine.stream3Index] = new Stream3(machine.operand[1]);
            machine.stream3Index++;
            break;
        case -3:
            machine.stream3Index--;
            machine.stream3[machine.stream3Index].deselect(machine.state);
            break;
        default:
            break;
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnINPUT_STREAM(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnSOUND_EFFECT(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnREAD_CHAR(Machine machine, int oldPc) throws IOException {
        updateWindowsPreInput(machine);
        machine.mainWindow.requestCharEvent();
        GlkEvent event;
        for (;;) {
            event = machine.glk.glk.select();
            if (event.type == GlkEvent.TypeCharInput) {
                break;
            } else if (machine.suspending) {
                machine.state.pc = oldPc;
                return Instruction.Result.Tick;
            }
            machine.handleEvent(event);
        }
        store(machine.state, event.val1);
        updateWindowsPostInput(machine);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnSCAN_TABLE(Machine machine, int argc) {
        int a0 = argc > 0 ? machine.operand[0] : 0;
        int a1 = argc > 1 ? machine.operand[1] : 0;
        int a2 = argc > 2 ? machine.operand[2] : 0;
        int a3 = argc > 3 ? machine.operand[3] : 0x82;
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
        store(machine.state, result);
        return branch(machine, table != 0);
    }

    private static Instruction.Result insnNOT(Machine machine) {
        store(machine.state, machine.operand[0]^65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnCALL_VN(Machine machine, int argc) {
        return doCall(machine,
                      machine.operand[0], machine.operand[1],
                      machine.operand[2], machine.operand[3],
                      machine.operand[4], machine.operand[5],
                      machine.operand[6], machine.operand[7],
                      argc - 1, -1);
    }

    private static Instruction.Result insnTOKENIZE(Machine machine, int argc) {
        int a0 = argc > 0 ? machine.operand[0] : 0;
        int a1 = argc > 1 ? machine.operand[1] : 0;
        int a2 = argc > 2 ? machine.operand[2] : 0;
        int a3 = argc > 3 ? machine.operand[3] : 0;
        int bufferAddress = a0+2;
        int bufferLength = machine.state.read8(bufferAddress-1);
        machine.state.getDictionary().parse(a2, bufferAddress, bufferLength, a1, a3 != 0);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnENCODE_TEXT(Machine machine, int argc) throws IOException {
        int a0 = argc > 0 ? machine.operand[0] : 0;
        int a1 = argc > 1 ? machine.operand[1] : 0;
        int a2 = argc > 2 ? machine.operand[2] : 0;
        int a3 = argc > 3 ? machine.operand[3] : 0;
        machine.string.setLength(0);
        for (int i = a2; i < a1 + a2; i++) {
            ZSCII.appendZSCII(machine.string, machine.state, machine.state.read8(i));
        }
        long encoded = ZSCII.encode(machine.state, machine.string.toString());
        machine.state.store16(a3, (int) (encoded >> 32));
        machine.state.store16(a3 + 2, (int) (encoded >> 16));
        machine.state.store16(a3 + 4, (int) encoded);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnCOPY_TABLE(Machine machine, int argc) {
        int a0 = argc > 0 ? machine.operand[0] : 0;
        int a1 = argc > 1 ? machine.operand[1] : 0;
        int a2 = argc > 2 ? machine.operand[2] : 0;
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
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnPRINT_TABLE(Machine machine, int argc) throws IOException {
        int a0 = argc > 0 ? machine.operand[0] : 0;
        int a1 = argc > 1 ? machine.operand[1] : 0;
        int a2 = argc > 2 ? machine.operand[2] : 1;
        int a3 = argc > 3 ? machine.operand[3] : 0;
        GlkStream stream = machine.getOutputStream();
        if (stream == null) {
            return Instruction.Result.Continue;
        }
        GlkWindow window = machine.currentWindow == 0 || machine.upperWindow == null ? machine.mainWindow : machine.upperWindow;
        int x = window.getCursorX();
        int y = window.getCursorY();
        machine.string.setLength(0);
        StringBuilder sb = machine.string;
        for (int i = 0; i < a2; i++) {
            sb.setLength(0);
            for (int j = 0; j < a1; j++) {
                ZSCII.appendZSCII(sb, machine.state, machine.state.read8(a0));
                a0++;
            }
            window.moveCursor(x, y + i);
            stream.putStringUni(new UnicodeString.US(sb));
            a0 += a3;
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnCHECK_ARG_COUNT(Machine machine) {
        return branch(machine, (machine.state.frame.args & (1 << (machine.operand[0] - 1))) != 0);
    }

    private static Instruction.Result insnSAVE(Machine machine) throws IOException {
        GlkFile file = machine.glk.glk.fileCreateByPrompt(GlkFile.UsageSavedGame, GlkFile.ModeWrite, 0);
        GlkStream stream = null;
        if (file != null) {
            stream = machine.glk.glk.streamOpenFile(file, GlkFile.ModeWrite, 0);
        }
        if (stream == null) {
            store(machine.state, 0);
            return Instruction.Result.Continue;
        }
        try {
            State saveState = new State();
            saveState.copyFrom(machine.state, true, false);
            store(saveState, 2);
            saveState.writeSave(stream.getDataOutput());
            store(machine.state, 1);
        } finally {
            stream.close();
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnRESTORE(Machine machine) throws IOException {
        GlkFile file = machine.glk.glk.fileCreateByPrompt(GlkFile.UsageSavedGame, GlkFile.ModeRead, 0);
        GlkStream stream = null;
        if (file != null) {
            stream = machine.glk.glk.streamOpenFile(file, GlkFile.ModeRead, 0);
        }
        if (stream == null) {
            store(machine.state, 0);
            return Instruction.Result.Continue;
        }
        try {
            if (!machine.state.loadSave(stream.getDataInput(), true)) {
                store(machine.state, 0);
                return Instruction.Result.Continue;
            }
        } finally {
            stream.close();
        }
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnLOG_SHIFT(Machine machine) {
        int a0 = machine.operand[0];
        short a1 = (short) machine.operand[1];
        store(machine.state, (a1 > 0 ? a0 << a1 : a0 >>> -a1) & 65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnART_SHIFT(Machine machine) {
        short a0 = (short) machine.operand[0];
        short a1 = (short) machine.operand[1];
        store(machine.state, (a1 > 0 ? a0 << a1 : a0 >> -a1) & 65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnSET_FONT(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnSAVE_UNDO(Machine machine) {
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
        store(undoState, 2);
        store(machine.state, 1);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnRESTORE_UNDO(Machine machine) {
        if (machine.undoStateIndex <= 0) {
            store(machine.state, 0);
            return Instruction.Result.Continue;
        }
        machine.undoStateIndex--;
        machine.state.copyFrom(machine.undoStates[machine.undoStateIndex], true, false);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnPRINT_UNICODE(Machine machine) throws IOException {
        machine.glk.glk.putCharUni(machine.operand[0]);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnCHECK_UNICODE(Machine machine) {
        int a0 = machine.operand[0];
        int result = 0;
        if (machine.glk.glk.gestalt(GlkGestalt.CharOutput, a0) != 0) {
            result |= 1;
        }
        if (machine.glk.glk.gestalt(GlkGestalt.CharInput, a0) != 0) {
            result |= 2;
        }
        store(machine.state, result);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnSET_TRUE_COLOR(Machine machine, int argc) {
        int a0 = argc > 0 ? machine.operand[0] : 0;
        int a1 = argc > 1 ? machine.operand[1] : 0;
        int a2 = argc > 2 ? machine.operand[2] : -1;
        int fg = a0 < 0 ? a0 : (a0 & 31) << 19 | (a0 & 992) << 6 | (a0 & 31744) >>> 7;
        int bg = a1 < 0 ? a1 : (a1 & 31) << 19 | (a1 & 992) << 6 | (a1 & 31744) >>> 7;
        setColors(machine, a2, fg, bg);
        return Instruction.Result.Continue;
    }
}
