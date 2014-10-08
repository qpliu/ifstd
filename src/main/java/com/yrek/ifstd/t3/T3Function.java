package com.yrek.ifstd.t3;

import java.io.Serializable;

class T3Function extends T3Value {
    private static final long serialVersionUID = 0L;

    final byte[] code;

    T3Function() {
        code = null;
    }

    @Override
    boolean t3equals(T3Value value) {
        return value == this;
    }
}
