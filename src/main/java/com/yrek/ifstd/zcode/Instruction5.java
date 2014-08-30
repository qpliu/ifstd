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
            return insnSET_COLOR(machine);
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
            return insnSET_COLOR(machine);
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
            return insnSET_COLOR(machine);
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
            return insnSET_COLOR(machine);
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
            operandsVAR(machine);
            return insnSET_COLOR(machine);
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
            return insnCALL_VS(machine, operandsVAR(machine));
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
            operandsVAR(machine);
            return insnSCAN_TABLE(machine);
        case 248:
            operandsVAR(machine);
            return insnNOT(machine);
        case 249:
            return insnCALL_VN(machine, operandsVAR(machine));
        case 250:
            return insnCALL_VN(machine, operandsVAR2(machine));
        case 251:
            operandsVAR(machine);
            return insnTOKENIZE(machine);
        case 252:
            operandsVAR(machine);
            return insnENCODE_TEXT(machine);
        case 253:
            operandsVAR(machine);
            return insnCOPY_TABLE(machine);
        case 254:
            operandsVAR(machine);
            return insnPRINT_TABLE(machine);
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
        return machine.state.read8(pc);
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
                machine.operand[i] = machine.state.read16(machine.state.pc);
                machine.state.pc += 2;
                break;
            case 0:
                machine.operand[i] = machine.state.read8(machine.state.pc++);
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
                machine.operand[i] = machine.state.read16(machine.state.pc);
                machine.state.pc += 2;
                break;
            case 0:
                machine.operand[i] = machine.state.read8(machine.state.pc++);
                break;
            default:
                throw new AssertionError();
            }
            types <<= 2;
        }
        return 8;
    }

    private static void store(Machine machine, int value) {
        machine.state.storeVar(machine.state.read8(machine.state.pc++), value);
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
        store(machine, machine.operand[0] | machine.operand[1]);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnAND(Machine machine) {
        store(machine, machine.operand[0] & machine.operand[1]);
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
        store(machine, machine.state.read16((machine.operand[0] + 2*machine.operand[1])&65535));
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnLOADB(Machine machine) {
        store(machine, machine.state.read8((machine.operand[0] + machine.operand[1])&65535));
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnGET_PROP(Machine machine) {
        store(machine, machine.state.getProp(machine.operand[0], machine.operand[1]));
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnGET_PROP_ADDR(Machine machine) {
        store(machine, machine.state.getPropAddr(machine.operand[0], machine.operand[1]));
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnGET_NEXT_PROP(Machine machine) {
        store(machine, machine.state.getNextProp(machine.operand[0], machine.operand[1]));
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnADD(Machine machine) {
        store(machine, (machine.operand[0] + machine.operand[1])&65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnSUB(Machine machine) {
        store(machine, (machine.operand[0] - machine.operand[1])&65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnMUL(Machine machine) {
        store(machine, (((short) machine.operand[0]) * ((short) machine.operand[1]))&65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnDIV(Machine machine) {
        store(machine, (((short) machine.operand[0]) / ((short) machine.operand[1]))&65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnMOD(Machine machine) {
        store(machine, (((short) machine.operand[0]) % ((short) machine.operand[1]))&65535);
        return Instruction.Result.Continue;
    }

    private static Instruction.Result insnCALL_2S(Machine machine) {
        int store = machine.state.read8(machine.state.pc++);
        return doCall(machine, machine.operand[0], machine.operand[1], 0, 0, 0, 0, 0, 0, 1, store);
    }

    private static Instruction.Result insnCALL_2N(Machine machine) {
        return doCall(machine, machine.operand[0], machine.operand[1], 0, 0, 0, 0, 0, 0, 1, -1);
    }

    private static Instruction.Result insnSET_COLOR(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnTHROW(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnJZ(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnGET_SIBLING(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnGET_CHILD(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnGET_PARENT(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnGET_PROP_LEN(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnINC(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnDEC(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnPRINT_ADDR(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnCALL_1S(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnREMOVE_OBJ(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnPRINT_OBJ(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnRET(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnJUMP(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnPRINT_PADDR(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnLOAD(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnCALL_1N(Machine machine, int operand) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnRTRUE(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnRFALSE(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnPRINT(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnPRINT_RET(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnRESTART(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnRET_POPPED(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnCATCH(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnQUIT(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnNEW_LINE(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnVERIFY(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnEXTENDED(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnPIRACY(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnCALL_VS(Machine machine, int argc) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnSTOREW(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnSTOREB(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnPUT_PROP(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnREAD(Machine machine, int argc, int oldPc) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnPRINT_CHAR(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnPRINT_NUM(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnRANDOM(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnPUSH(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnPULL(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnSPLIT_WINDOW(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnSET_WINDOW(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnERASE_WINDOW(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnERASE_LINE(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnSET_CURSOR(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnGET_CURSOR(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnSET_TEXT_STYLE(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnBUFFER_MODE(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnOUTPUT_STREAM(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnINPUT_STREAM(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnSOUND_EFFECT(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnREAD_CHAR(Machine machine, int oldPc) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnSCAN_TABLE(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnNOT(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnCALL_VN(Machine machine, int argc) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnTOKENIZE(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnENCODE_TEXT(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnCOPY_TABLE(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnPRINT_TABLE(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnCHECK_ARG_COUNT(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnSAVE(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnRESTORE(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnLOG_SHIFT(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnART_SHIFT(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnSET_FONT(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnSAVE_UNDO(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnRESTORE_UNDO(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnPRINT_UNICODE(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnCHECK_UNICODE(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static Instruction.Result insnSET_TRUE_COLOR(Machine machine) {
        throw new RuntimeException("unimplemented");
    }
}
