package com.yrek.ifstd.t3;

import java.io.Serializable;

abstract class T3Value implements Serializable {
    private static final long serialVersionUID = 0L;

    static final T3Value NIL = new T3Value() {
        private static final long serialVersionUID = 0L;
        @Override boolean t3equals(T3Value value) {
            return value == NIL;
        }
        @Override T3Result t3xor(T3Value value) {
            if (value == NIL || (value instanceof T3ValueInt && ((T3ValueInt) value).value == 0)) {
                return T3Result.NIL;
            }
            if (value == TRUE || (value instanceof T3ValueInt && ((T3ValueInt) value).value != 0)) {
                return T3Result.TRUE;
            }
            return T3Result.ERROR_BAD_TYPE_XOR;
        }
        @Override T3Result t3not() { return T3Result.TRUE; }
        @Override T3Result t3boolize() { return T3Result.NIL; }
    };

    static final T3Value INT0 = new T3ValueInt(0) {
        private static final long serialVersionUID = 0L;
        @Override T3Result t3negate() { return T3Result.INT0; }
        @Override T3Result t3bnot() { return T3Result.INTM1; }
        @Override T3Result t3not() { return T3Result.TRUE; }
        @Override T3Result t3boolize() { return T3Result.NIL; }
        @Override T3Result t3inc() { return T3Result.INT1; }
        @Override T3Result t3dec() { return T3Result.INTM1; }
    };
    static final T3Value INT1 = new T3ValueInt(1) {
        private static final long serialVersionUID = 0L;
        @Override T3Result t3negate() { return T3Result.INTM1; }
        @Override T3Result t3not() { return T3Result.NIL; }
        @Override T3Result t3boolize() { return T3Result.TRUE; }
        @Override T3Result t3dec() { return T3Result.INT0; }
    };
    static final T3Value INTM1 = new T3ValueInt(-1) {
        private static final long serialVersionUID = 0L;
        @Override T3Result t3negate() { return T3Result.INT1; }
        @Override T3Result t3bnot() { return T3Result.INT0; }
        @Override T3Result t3not() { return T3Result.NIL; }
        @Override T3Result t3boolize() { return T3Result.TRUE; }
        @Override T3Result t3inc() { return T3Result.INT0; }
    };

    static final T3Value TRUE = new T3Value() {
        private static final long serialVersionUID = 0L;
        @Override boolean t3equals(T3Value value) {
            return value == TRUE;
        }
        @Override T3Result t3xor(T3Value value) {
            if (value == NIL || (value instanceof T3ValueInt && ((T3ValueInt) value).value == 0)) {
                return T3Result.TRUE;
            }
            if (value == TRUE || (value instanceof T3ValueInt && ((T3ValueInt) value).value != 0)) {
                return T3Result.NIL;
            }
            return T3Result.ERROR_BAD_TYPE_XOR;
        }
        @Override T3Result t3not() { return T3Result.NIL; }
        @Override T3Result t3boolize() { return T3Result.TRUE; }
    };

    abstract boolean t3equals(T3Value value);

    T3Result t3compare(T3Value value) {
        return T3Result.ERROR_INVALID_COMPARISON;
    }

    T3Result t3negate() {
        return T3Result.ERROR_NUM_VAL_REQD;
    }

    T3Result t3bnot() {
        return T3Result.ERROR_BAD_TYPE_BNOT;
    }

    T3Result t3add(T3Value value) {
        return T3Result.ERROR_BAD_TYPE_ADD;
    }

    T3Result t3sub(T3Value value) {
        return T3Result.ERROR_BAD_TYPE_SUB;
    }

    T3Result t3mul(T3Value value) {
        return T3Result.ERROR_BAD_TYPE_MUL;
    }

    T3Result t3band(T3Value value) {
        return T3Result.ERROR_BAD_TYPE_BAND;
    }

    T3Result t3bor(T3Value value) {
        return T3Result.ERROR_BAD_TYPE_BOR;
    }

    T3Result t3shl(T3Value value) {
        return T3Result.ERROR_BAD_TYPE_SHL;
    }

    T3Result t3ashr(T3Value value) {
        return T3Result.ERROR_BAD_TYPE_ASHR;
    }

    T3Result t3xor(T3Value value) {
        return T3Result.ERROR_BAD_TYPE_XOR;
    }

    T3Result t3div(T3Value value) {
        return T3Result.ERROR_BAD_TYPE_DIV;
    }

    T3Result t3mod(T3Value value) {
        return T3Result.ERROR_BAD_TYPE_MOD;
    }

    T3Result t3not() {
        return T3Result.ERROR_NO_LOG_CONV;
    }

    T3Result t3boolize() {
        return T3Result.ERROR_NO_LOG_CONV;
    }

    T3Result t3inc() {
        return T3Result.ERROR_BAD_TYPE_ADD;
    }

    T3Result t3dec() {
        return T3Result.ERROR_BAD_TYPE_SUB;
    }
}
