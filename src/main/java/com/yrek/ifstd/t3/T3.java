package com.yrek.ifstd.t3;

import java.io.Serializable;

public class T3 implements Runnable, Serializable {
    enum Result {
        Continue, Tick, Quit;
    }

    @Override
    public void run() {
        throw new RuntimeException("unimplemented");
    }
}
