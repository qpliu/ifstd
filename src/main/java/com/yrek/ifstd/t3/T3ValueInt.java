package com.yrek.ifstd.t3;

import java.io.Serializable;

class T3ValueInt extends T3Value {
    private static final long serialVersionUID = 0L;

    final int value;

    T3ValueInt(int value) {
        this.value = value;
    }

    @Override
    boolean t3equals(T3Value value) {
        return value instanceof T3ValueInt && ((T3ValueInt) value).value == this.value;
    }

    @Override
    T3Result t3compare(T3Value value) {
        if (value instanceof T3ValueInt) {
            if (this.value > ((T3ValueInt) value).value) {
                return T3Result.INT1;
            } else if (this.value < ((T3ValueInt) value).value) {
                return T3Result.INTM1;
            } else {
                return T3Result.INT0;
            }
        }
        return T3Result.ERROR_INVALID_COMPARISON;
    }

    @Override
    T3Result t3negate() {
        return new T3Result(new T3ValueInt(-value));
    }

    @Override
    T3Result t3bnot() {
        return new T3Result(new T3ValueInt(~value));
    }

    @Override
    T3Result t3add(T3Value value) {
        if (value instanceof T3ValueInt) {
            return new T3Result(new T3ValueInt(this.value + ((T3ValueInt) value).value));
        }
        return T3Result.ERROR_NUM_VAL_REQD;
    }

    @Override
    T3Result t3sub(T3Value value) {
        if (value instanceof T3ValueInt) {
            return new T3Result(new T3ValueInt(this.value - ((T3ValueInt) value).value));
        }
        return T3Result.ERROR_NUM_VAL_REQD;
    }

    @Override
    T3Result t3mul(T3Value value) {
        if (value instanceof T3ValueInt) {
            return new T3Result(new T3ValueInt(this.value * ((T3ValueInt) value).value));
        }
        return T3Result.ERROR_BAD_TYPE_MUL; // not NUM_VAL_REQD: http://www.tads.org/t3doc/doc/techman/t3spec/opcode.htm#opc_MUL
    }

    @Override
    T3Result t3band(T3Value value) {
        if (value instanceof T3ValueInt) {
            return new T3Result(new T3ValueInt(this.value & ((T3ValueInt) value).value));
        }
        return T3Result.ERROR_BAD_TYPE_BAND;
    }

    @Override
    T3Result t3bor(T3Value value) {
        if (value instanceof T3ValueInt) {
            return new T3Result(new T3ValueInt(this.value | ((T3ValueInt) value).value));
        }
        return T3Result.ERROR_BAD_TYPE_BOR;
    }

    @Override
    T3Result t3shl(T3Value value) {
        if (value instanceof T3ValueInt) {
            return new T3Result(new T3ValueInt(this.value << ((T3ValueInt) value).value));
        }
        return T3Result.ERROR_BAD_TYPE_SHL;
    }

    @Override
    T3Result t3ashr(T3Value value) {
        if (value instanceof T3ValueInt) {
            return new T3Result(new T3ValueInt(this.value >> ((T3ValueInt) value).value));
        }
        return T3Result.ERROR_BAD_TYPE_ASHR;
    }

    @Override
    T3Result t3xor(T3Value value) {
        if (this.value == 0) {
            return T3Value.NIL.t3xor(value);
        } else {
            return T3Value.TRUE.t3xor(value);
        }
    }

    @Override
    T3Result t3div(T3Value value) {
        if (value instanceof T3ValueInt) {
            if (((T3ValueInt) value).value == 0) {
                return T3Result.ERROR_DIVIDE_BY_ZERO;
            }
            return new T3Result(new T3ValueInt(this.value / ((T3ValueInt) value).value));
        }
        return T3Result.ERROR_BAD_TYPE_DIV;
    }

    @Override
    T3Result t3mod(T3Value value) {
        if (value instanceof T3ValueInt) {
            if (((T3ValueInt) value).value == 0) {
                return T3Result.ERROR_DIVIDE_BY_ZERO;
            }
            return new T3Result(new T3ValueInt(this.value % ((T3ValueInt) value).value));
        }
        return T3Result.ERROR_NUM_VAL_REQD;
    }

    @Override
    T3Result t3not() {
        return value == 0 ? T3Result.TRUE : T3Result.NIL;
    }

    @Override
    T3Result t3boolize() {
        return value == 0 ? T3Result.NIL : T3Result.TRUE;
    }

    @Override
    T3Result t3inc() {
        return new T3Result(new T3ValueInt(this.value + 1));
    }
}
