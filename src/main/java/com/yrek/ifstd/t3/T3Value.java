package com.yrek.ifstd.t3;

import java.io.Serializable;

abstract class T3Value implements Serializable {
    private static final long serialVersionUID = 0L;

    static final T3Value NIL = new T3Value() {
        private static final long serialVersionUID = 0L;
        @Override boolean t3equals(T3Value value) {
            return value == NIL;
        }
    };

    static final T3Value INT0 = new T3ValueInt(0) {
        private static final long serialVersionUID = 0L;
        @Override T3Result t3negate() { return T3Result.INT0; }
        @Override T3Result t3bnot() { return T3Result.INTM1; }
    };
    static final T3Value INT1 = new T3ValueInt(1) {
        private static final long serialVersionUID = 0L;
        @Override T3Result t3negate() { return T3Result.INTM1; }
    };
    static final T3Value INTM1 = new T3ValueInt(-1) {
        private static final long serialVersionUID = 0L;
        @Override T3Result t3negate() { return T3Result.INT1; }
        @Override T3Result t3bnot() { return T3Result.INT0; }
    };

    static final T3Value TRUE = new T3Value() {
        private static final long serialVersionUID = 0L;
        @Override boolean t3equals(T3Value value) {
            return value == TRUE;
        }
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
}
