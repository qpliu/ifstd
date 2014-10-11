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
}
