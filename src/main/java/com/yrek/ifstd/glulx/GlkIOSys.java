package com.yrek.ifstd.glulx;

import com.yrek.ifstd.glk.Glk;

class GlkIOSys extends IOSys {
    final Glk glk;

    GlkIOSys(Glk glk, int rock) {
        super(rock);
        this.glk = glk;
    }

    int getMode() {
        return 2;
    }
}
