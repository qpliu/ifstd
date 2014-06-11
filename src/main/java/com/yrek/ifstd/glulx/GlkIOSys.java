package com.yrek.ifstd.glulx;

import com.yrek.ifstd.glk.GlkDispatch;

class GlkIOSys extends IOSys {
    final GlkDispatch glk;

    GlkIOSys(GlkDispatch glk, int rock) {
        super(rock);
        this.glk = glk;
    }

    int getMode() {
        return 2;
    }
}
