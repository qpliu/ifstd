package com.yrek.ifstd.glulx;

import com.yrek.ifstd.glk.GlkDispatch;

class GlkIOSys extends IOSys {
    final GlkDispatch glk;

    GlkIOSys(GlkDispatch glk, int rock) {
        super(rock);
        this.glk = glk;
    }

    @Override
    int getMode() {
        return 2;
    }

    @Override
    void streamChar(int ch) {
        throw new RuntimeException("unimplemented");
    }

    @Override
    void streamUnichar(int ch) {
        throw new RuntimeException("unimplemented");
    }

    @Override
    void streamNum(int num) {
        throw new RuntimeException("unimplemented");
    }

    @Override
    void streamStr(Machine machine, int addr) {
        throw new RuntimeException("unimplemented");
    }
}
