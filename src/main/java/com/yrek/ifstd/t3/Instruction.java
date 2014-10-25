package com.yrek.ifstd.t3;

class Instruction {
    static T3.Result executeNext(Machine machine) {
        switch (insn(machine)) {
        case 0x1: // PUSH_0
            machine.stack.push(T3Value.INT0);
            return T3.Result.Continue;
        case 0x2: // PUSH_1
            machine.stack.push(T3Value.INT1);
            return T3.Result.Continue;
        case 0x3: // PUSHINT8
            machine.stack.push(new T3ValueInt(operandSbyte(machine)));
            return T3.Result.Continue;
        case 0x4: // PUSHINT
            machine.stack.push(new T3ValueInt(operandInt4(machine)));
            return T3.Result.Continue;
        case 0x5: // PUSHSTR
            throw new RuntimeException("unimplemented");
        case 0x6: // PUSHLST
            throw new RuntimeException("unimplemented");
        case 0x7: // PUSHOBJ
            throw new RuntimeException("unimplemented");
        case 0x8: // PUSHNIL
            machine.stack.push(T3Value.NIL);
            return T3.Result.Continue;
        case 0x9: // PUSHTRUE
            machine.stack.push(T3Value.TRUE);
            return T3.Result.Continue;
        case 0xa: // PUSHPROPID
            throw new RuntimeException("unimplemented");
        case 0xb: // PUSHFNPTR
            throw new RuntimeException("unimplemented");
        case 0xc: // PUSHSTRI
            throw new RuntimeException("unimplemented");
        case 0xd: // PUSHPARLST
            throw new RuntimeException("unimplemented");
        case 0xe: // MAKELSTPAR
            throw new RuntimeException("unimplemented");
        case 0xf: // PUSHENUM
            throw new RuntimeException("unimplemented");
        case 0x10: // PUSHBIFPTR
            throw new RuntimeException("unimplemented");
        case 0x20: // NEG
            return pushResult(machine, machine.stack.removeLast().t3negate());
        case 0x21: // BNOT
            return pushResult(machine, machine.stack.removeLast().t3bnot());
        case 0x22: // ADD
            T3Value val2 = machine.stack.removeLast();
            T3Value val1 = machine.stack.removeLast();
            return pushResult(machine, val1.t3add(val2));
        case 0x23: // SUB
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            return pushResult(machine, val1.t3sub(val2));
        case 0x24: // MUL
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            return pushResult(machine, val1.t3mul(val2));
        case 0x25: // BAND
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            return pushResult(machine, val1.t3band(val2));
        case 0x26: // BOR
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            return pushResult(machine, val1.t3bor(val2));
        case 0x27: // SHL
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            return pushResult(machine, val1.t3shl(val2));
        case 0x28: // ASHR
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            return pushResult(machine, val1.t3ashr(val2));
        case 0x29: // XOR
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            return pushResult(machine, val1.t3xor(val2));
        case 0x2a: // DIV
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            return pushResult(machine, val1.t3div(val2));
        case 0x2b: // MOD
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            return pushResult(machine, val1.t3mod(val2));
        case 0x2c: // NOT
            return pushResult(machine, machine.stack.removeLast().t3not());
        case 0x2d: // BOOLIZE
            throw new RuntimeException("unimplemented");
        case 0x2e: // INC
            throw new RuntimeException("unimplemented");
        case 0x2f: // DEC
            throw new RuntimeException("unimplemented");
        case 0x30: // LSHR
            throw new RuntimeException("unimplemented");
        case 0x40: // EQ
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            machine.stack.push(val1.t3equals(val2) ? T3Value.TRUE : T3Value.NIL);
            return T3.Result.Continue;
        case 0x41: // NE
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            machine.stack.push(val1.t3equals(val2) ? T3Value.NIL : T3Value.TRUE);
            return T3.Result.Continue;
        case 0x42: // LT
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            T3Result result = val1.t3compare(val2);
            if (result.error != null) {
                machine.currentError = result.error;
                return T3.Result.Throw;
            }
            machine.stack.push(result.result.t3equals(T3Value.INTM1) ? T3Value.TRUE : T3Value.NIL);
            return T3.Result.Continue;
        case 0x43: // LE
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            result = val1.t3compare(val2);
            if (result.error != null) {
                machine.currentError = result.error;
                return T3.Result.Throw;
            }
            machine.stack.push(!result.result.t3equals(T3Value.INT1) ? T3Value.TRUE : T3Value.NIL);
            return T3.Result.Continue;
        case 0x44: // GT
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            result = val1.t3compare(val2);
            if (result.error != null) {
                machine.currentError = result.error;
                return T3.Result.Throw;
            }
            machine.stack.push(result.result.t3equals(T3Value.INT1) ? T3Value.TRUE : T3Value.NIL);
            return T3.Result.Continue;
        case 0x45: // GE
            val2 = machine.stack.removeLast();
            val1 = machine.stack.removeLast();
            result = val1.t3compare(val2);
            if (result.error != null) {
                machine.currentError = result.error;
                return T3.Result.Throw;
            }
            machine.stack.push(!result.result.t3equals(T3Value.INTM1) ? T3Value.TRUE : T3Value.NIL);
            return T3.Result.Continue;
        case 0x50: // RETVAL
            machine.r0 = machine.stack.removeLast();
            return ret(machine);
        case 0x51: // RETNIL
            machine.r0 = T3Value.NIL;
            return ret(machine);
        case 0x52: // RETTRUE
            machine.r0 = T3Value.TRUE;
            return ret(machine);
        case 0x54: // RET
            return ret(machine);
        case 0x56: // NAMEDARGPTR
            throw new RuntimeException("unimplemented");
        case 0x57: // NAMEDARGTAB
            throw new RuntimeException("unimplemented");
        case 0x58: // CALL
            throw new RuntimeException("unimplemented");
        case 0x59: // PTRCALL
            throw new RuntimeException("unimplemented");
        case 0x60: // GETPROP
            throw new RuntimeException("unimplemented");
        case 0x61: // CALLPROP
            throw new RuntimeException("unimplemented");
        case 0x62: // PTRCALLPROP
            throw new RuntimeException("unimplemented");
        case 0x63: // GETPROPSELF
            throw new RuntimeException("unimplemented");
        case 0x64: // CALLPROPSELF
            throw new RuntimeException("unimplemented");
        case 0x65: // PTRCALLPROPSELF
            throw new RuntimeException("unimplemented");
        case 0x66: // OBJGETPROP
            throw new RuntimeException("unimplemented");
        case 0x67: // OBJCALLPROP
            throw new RuntimeException("unimplemented");
        case 0x68: // GETPROPDATA
            throw new RuntimeException("unimplemented");
        case 0x69: // PTRGETPROPDATA
            throw new RuntimeException("unimplemented");
        case 0x6a: // GETPROPLCL1
            throw new RuntimeException("unimplemented");
        case 0x6b: // CALLPROPLCL1
            throw new RuntimeException("unimplemented");
        case 0x6c: // GETPROPR0
            throw new RuntimeException("unimplemented");
        case 0x6d: // CALLPROPR0
            throw new RuntimeException("unimplemented");
        case 0x72: // INHERIT
            throw new RuntimeException("unimplemented");
        case 0x73: // PTRINHERIT
            throw new RuntimeException("unimplemented");
        case 0x74: // EXPINHERIT
            throw new RuntimeException("unimplemented");
        case 0x75: // PTREXPINHERIT
            throw new RuntimeException("unimplemented");
        case 0x76: // VARARGC
            switch (insn(machine)) {
            case 0xb1: // BUILTIN_A
                throw new RuntimeException("unimplemented");
            case 0xb2: // BUILTIN_B
                throw new RuntimeException("unimplemented");
            case 0xb3: // BUILTIN_C
                throw new RuntimeException("unimplemented");
            case 0xb4: // BUILTIN_D
                throw new RuntimeException("unimplemented");
            case 0xb5: // BUILTIN1
                throw new RuntimeException("unimplemented");
            case 0xb6: // BUILTIN2
                throw new RuntimeException("unimplemented");
            case 0x58: // CALL
                throw new RuntimeException("unimplemented");
            case 0x61: // CALLPROP
                throw new RuntimeException("unimplemented");
            case 0x64: // CALLPROPSELF
                throw new RuntimeException("unimplemented");
            case 0x77: // DELEGATE
                throw new RuntimeException("unimplemented");
            case 0x74: // EXPINHERIT
                throw new RuntimeException("unimplemented");
            case 0x72: // INHERIT
                throw new RuntimeException("unimplemented");
            case 0xc0: // NEW1
                throw new RuntimeException("unimplemented");
            case 0xc1: // NEW2
                throw new RuntimeException("unimplemented");
            case 0x67: // OBJCALLPROP
                throw new RuntimeException("unimplemented");
            case 0x59: // PTRCALL
                throw new RuntimeException("unimplemented");
            case 0x62: // PTRCALLPROP
                throw new RuntimeException("unimplemented");
            case 0x65: // PTRCALLPROPSELF
                throw new RuntimeException("unimplemented");
            case 0x78: // PTRDELEGATE
                throw new RuntimeException("unimplemented");
            case 0x75: // PTREXPINHERIT
                throw new RuntimeException("unimplemented");
            case 0x73: // PTRINHERIT
                throw new RuntimeException("unimplemented");
            case 0xc2: // TRNEW1
                throw new RuntimeException("unimplemented");
            case 0xc3: // TRNEW2
                throw new RuntimeException("unimplemented");
            default:
                throw new IllegalArgumentException();
            }
        case 0x77: // DELEGATE
            throw new RuntimeException("unimplemented");
        case 0x78: // PTRDELEGATE
            throw new RuntimeException("unimplemented");
        case 0x79: // SWAP2
            throw new RuntimeException("unimplemented");
        case 0x7a: // SWAPN
            throw new RuntimeException("unimplemented");
        case 0x7c: // GETARGN0
            throw new RuntimeException("unimplemented");
        case 0x7d: // GETARGN1
            throw new RuntimeException("unimplemented");
        case 0x7e: // GETARGN2
            throw new RuntimeException("unimplemented");
        case 0x7f: // GETARGN3
            throw new RuntimeException("unimplemented");
        case 0x80: // GETLCL1
            throw new RuntimeException("unimplemented");
        case 0x81: // GETLCL2
            throw new RuntimeException("unimplemented");
        case 0x82: // GETARG1
            throw new RuntimeException("unimplemented");
        case 0x83: // GETARG2
            throw new RuntimeException("unimplemented");
        case 0x84: // PUSHSELF
            throw new RuntimeException("unimplemented");
        case 0x85: // GETDBLCL
            throw new RuntimeException("unimplemented");
        case 0x86: // GETDBARG
            throw new RuntimeException("unimplemented");
        case 0x87: // GETARGC
            throw new RuntimeException("unimplemented");
        case 0x88: // DUP
            throw new RuntimeException("unimplemented");
        case 0x89: // DISC
            throw new RuntimeException("unimplemented");
        case 0x8a: // DISC1
            throw new RuntimeException("unimplemented");
        case 0x8b: // GETR0
            throw new RuntimeException("unimplemented");
        case 0x8c: // GETDBARGC
            throw new RuntimeException("unimplemented");
        case 0x8d: // SWAP
            throw new RuntimeException("unimplemented");
        case 0x8e: // PUSHCTXELE
            throw new RuntimeException("unimplemented");
        case 0x8f: // DUP2
            throw new RuntimeException("unimplemented");
        case 0x90: // SWITCH
            throw new RuntimeException("unimplemented");
        case 0x91: // JMP
            throw new RuntimeException("unimplemented");
        case 0x92: // JT
            throw new RuntimeException("unimplemented");
        case 0x93: // JF
            throw new RuntimeException("unimplemented");
        case 0x94: // JE
            throw new RuntimeException("unimplemented");
        case 0x95: // JNE
            throw new RuntimeException("unimplemented");
        case 0x96: // JGT
            throw new RuntimeException("unimplemented");
        case 0x97: // JGE
            throw new RuntimeException("unimplemented");
        case 0x98: // JLT
            throw new RuntimeException("unimplemented");
        case 0x99: // JLE
            throw new RuntimeException("unimplemented");
        case 0x9a: // JST
            throw new RuntimeException("unimplemented");
        case 0x9b: // JSF
            throw new RuntimeException("unimplemented");
        case 0x9c: // LJSR
            throw new RuntimeException("unimplemented");
        case 0x9d: // LRET
            throw new RuntimeException("unimplemented");
        case 0x9e: // JNIL
            throw new RuntimeException("unimplemented");
        case 0x9f: // JNOTNIL
            throw new RuntimeException("unimplemented");
        case 0xa0: // JR0T
            throw new RuntimeException("unimplemented");
        case 0xa1: // JR0F
            throw new RuntimeException("unimplemented");
        case 0xa6: // GETSPN
            throw new RuntimeException("unimplemented");
        case 0xaa: // GETSPGETLCLN0
            throw new RuntimeException("unimplemented");
        case 0xab: // GETSPGETLCLN1
            throw new RuntimeException("unimplemented");
        case 0xac: // GETSPGETLCLN2
            throw new RuntimeException("unimplemented");
        case 0xad: // GETSPGETLCLN3
            throw new RuntimeException("unimplemented");
        case 0xae: // GETSPGETLCLN4
            throw new RuntimeException("unimplemented");
        case 0xaf: // GETSPGETLCLN5
            throw new RuntimeException("unimplemented");
        case 0xb0: // GETSPGETLCLNSAY
            throw new RuntimeException("unimplemented");
        case 0xb1: // BUILTIN_A
            throw new RuntimeException("unimplemented");
        case 0xb2: // BUILTIN_B
            throw new RuntimeException("unimplemented");
        case 0xb3: // BUILTIN_C
            throw new RuntimeException("unimplemented");
        case 0xb4: // BUILTIN_D
            throw new RuntimeException("unimplemented");
        case 0xb5: // BUILTIN1
            throw new RuntimeException("unimplemented");
        case 0xb6: // BUILTIN2
            throw new RuntimeException("unimplemented");
        case 0xb7: // CALLEXT
            throw new RuntimeException("unimplemented");
        case 0xb8: // THROW
            throw new RuntimeException("unimplemented");
        case 0xb9: // SAYVAL
            throw new RuntimeException("unimplemented");
        case 0xba: // INDEX
            throw new RuntimeException("unimplemented");
        case 0xbb: // IDXLCLINT8
            throw new RuntimeException("unimplemented");
        case 0xbc: // IDXINT8
            throw new RuntimeException("unimplemented");
        case 0xc0: // NEW1
            throw new RuntimeException("unimplemented");
        case 0xc1: // NEW2
            throw new RuntimeException("unimplemented");
        case 0xc2: // TRNEW1
            throw new RuntimeException("unimplemented");
        case 0xc3: // TRNEW2
            throw new RuntimeException("unimplemented");
        case 0xd0: // INCLCL
            throw new RuntimeException("unimplemented");
        case 0xd1: // DECLCL
            throw new RuntimeException("unimplemented");
        case 0xd2: // ADDILCL1
            throw new RuntimeException("unimplemented");
        case 0xd3: // ADDILCL4
            throw new RuntimeException("unimplemented");
        case 0xd4: // ADDTOLCL
            throw new RuntimeException("unimplemented");
        case 0xd5: // SUBFROMLCL
            throw new RuntimeException("unimplemented");
        case 0xd6: // ZEROLCL1
            throw new RuntimeException("unimplemented");
        case 0xd7: // ZEROLCL2
            throw new RuntimeException("unimplemented");
        case 0xd8: // NILLCL1
            throw new RuntimeException("unimplemented");
        case 0xd9: // NILLCL2
            throw new RuntimeException("unimplemented");
        case 0xda: // ONELCL1
            throw new RuntimeException("unimplemented");
        case 0xdb: // ONELCL2
            throw new RuntimeException("unimplemented");
        case 0xe0: // SETLCL1
            throw new RuntimeException("unimplemented");
        case 0xe1: // SETLCL2
            throw new RuntimeException("unimplemented");
        case 0xe2: // SETARG1
            throw new RuntimeException("unimplemented");
        case 0xe3: // SETARG2
            throw new RuntimeException("unimplemented");
        case 0xe4: // SETIND
            throw new RuntimeException("unimplemented");
        case 0xe5: // SETPROP
            throw new RuntimeException("unimplemented");
        case 0xe6: // PTRSETPROP
            throw new RuntimeException("unimplemented");
        case 0xe7: // SETPROPSELF
            throw new RuntimeException("unimplemented");
        case 0xe8: // OBJSETPROP
            throw new RuntimeException("unimplemented");
        case 0xe9: // SETDBLCL
            throw new RuntimeException("unimplemented");
        case 0xea: // SETDBARG
            throw new RuntimeException("unimplemented");
        case 0xeb: // SETSELF
            throw new RuntimeException("unimplemented");
        case 0xec: // LOADCTX
            throw new RuntimeException("unimplemented");
        case 0xed: // STORECTX
            throw new RuntimeException("unimplemented");
        case 0xee: // SETLCL1R0
            throw new RuntimeException("unimplemented");
        case 0xef: // SETINDLCL1I8
            throw new RuntimeException("unimplemented");
        case 0xf1: // BP
            throw new RuntimeException("unimplemented");
        case 0xf2: // NOP
            throw new RuntimeException("unimplemented");
        default:
            throw new IllegalArgumentException();
        }
    }                

    private static int insn(Machine machine) {
        return machine.currentFunction.code[machine.ip++]&255;
    }

    private static int operandSbyte(Machine machine) {
        return machine.currentFunction.code[machine.ip++];
    }

    private static int operandUbyte(Machine machine) {
        return machine.currentFunction.code[machine.ip++]&255;
    }

    private static int operandUint2(Machine machine) {
        machine.ip += 2;
        return (machine.currentFunction.code[machine.ip - 2]&255) | ((machine.currentFunction.code[machine.ip - 1]&255) << 8);
    }

    private static int operandInt4(Machine machine) {
        machine.ip += 4;
        return (machine.currentFunction.code[machine.ip - 4]&255) | ((machine.currentFunction.code[machine.ip - 3]&255) << 8) | ((machine.currentFunction.code[machine.ip - 2]&255) << 16) | (machine.currentFunction.code[machine.ip - 1] << 24);
    }

    private static T3.Result ret(Machine machine) {
        throw new RuntimeException("unimplemented");
    }

    private static T3.Result pushResult(Machine machine, T3Result result) {
        if (result.error != null) {
            machine.currentError = result.error;
            return T3.Result.Throw;
        }
        machine.stack.push(result.result);
        return T3.Result.Continue;
    }
}
