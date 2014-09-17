package com.yrek.ifstd.t3;

class Instruction {
    static void executeNext(Machine machine) {
        int insn = 0;
        switch (insn) {
        case 0x1: // PUSH_0
            throw new RuntimeException("unimplemented");
        case 0x2: // PUSH_1
            throw new RuntimeException("unimplemented");
        case 0x3: // PUSHINT8
            throw new RuntimeException("unimplemented");
        case 0x4: // PUSHINT
            throw new RuntimeException("unimplemented");
        case 0x5: // PUSHSTR
            throw new RuntimeException("unimplemented");
        case 0x6: // PUSHLST
            throw new RuntimeException("unimplemented");
        case 0x7: // PUSHOBJ
            throw new RuntimeException("unimplemented");
        case 0x8: // PUSHNIL
            throw new RuntimeException("unimplemented");
        case 0x9: // PUSHTRUE
            throw new RuntimeException("unimplemented");
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
            throw new RuntimeException("unimplemented");
        case 0x21: // BNOT
            throw new RuntimeException("unimplemented");
        case 0x22: // AND
            throw new RuntimeException("unimplemented");
        case 0x23: // SUB
            throw new RuntimeException("unimplemented");
        case 0x24: // MUL
            throw new RuntimeException("unimplemented");
        case 0x25: // BAND
            throw new RuntimeException("unimplemented");
        case 0x26: // BOR
            throw new RuntimeException("unimplemented");
        case 0x27: // SHL
            throw new RuntimeException("unimplemented");
        case 0x28: // ASHR
            throw new RuntimeException("unimplemented");
        case 0x29: // XOR
            throw new RuntimeException("unimplemented");
        case 0x2a: // DIV
            throw new RuntimeException("unimplemented");
        case 0x2b: // MOD
            throw new RuntimeException("unimplemented");
        case 0x2c: // NOT
            throw new RuntimeException("unimplemented");
        case 0x2d: // BOOLIZE
            throw new RuntimeException("unimplemented");
        case 0x2e: // INC
            throw new RuntimeException("unimplemented");
        case 0x2f: // DEC
            throw new RuntimeException("unimplemented");
        case 0x30: // LSHR
            throw new RuntimeException("unimplemented");
        case 0x40: // EQ
            throw new RuntimeException("unimplemented");
        case 0x41: // NE
            throw new RuntimeException("unimplemented");
        case 0x42: // LT
            throw new RuntimeException("unimplemented");
        case 0x43: // LE
            throw new RuntimeException("unimplemented");
        case 0x44: // GT
            throw new RuntimeException("unimplemented");
        case 0x45: // GE
            throw new RuntimeException("unimplemented");
        case 0x50: // RETVAL
            throw new RuntimeException("unimplemented");
        case 0x51: // RETNIL
            throw new RuntimeException("unimplemented");
        case 0x52: // RETTRUE
            throw new RuntimeException("unimplemented");
        case 0x54: // RET
            throw new RuntimeException("unimplemented");
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
            throw new RuntimeException("unimplemented");
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
        default:
            throw new IllegalArgumentException();
        }
    }                
}
