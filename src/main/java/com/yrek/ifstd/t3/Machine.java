package com.yrek.ifstd.t3;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

class Machine implements Serializable {
    private static final long serialVersionUID = 0L;

    LinkedList<T3Value> stack = new LinkedList<T3Value>();
    T3Function currentFunction;
    T3Value r0 = T3Value.NIL;
    int ip;
    int fp;
}
