package com.yrek.ifstd.t3;

import java.io.Serializable;

abstract class T3Value implements Serializable {
    private static final long serialVersionUID = 0L;

    static final T3Value NIL = new T3Value() {
        private static final long serialVersionUID = 0L;
    };
    static final T3Value INT0 = new T3Value() {
        private static final long serialVersionUID = 0L;
    };
    static final T3Value INT1 = new T3Value() {
        private static final long serialVersionUID = 0L;
    };
    static final T3Value TRUE = new T3Value() {
        private static final long serialVersionUID = 0L;
    };
}
