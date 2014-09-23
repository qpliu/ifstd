package com.yrek.ifstd.t3;

import java.io.Serializable;

class T3ValueInt extends T3Value {
    private static final long serialVersionUID = 0L;

    final int value;

    T3ValueInt(int value) {
        this.value = value;
    }
}
