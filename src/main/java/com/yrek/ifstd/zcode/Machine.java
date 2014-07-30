package com.yrek.ifstd.zcode;

import java.io.Serializable;
import java.util.Random;

class Machine implements Serializable {
    private static final long serialVersionUID = 0L;

    int version;
    State state;
    State undoState = null;
    Random random = new Random();
}
