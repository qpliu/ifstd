package com.yrek.ifstd.glulx;

import java.io.IOException;

import com.yrek.ifstd.glk.GlkDispatch;

class GlkIOSys extends IOSys {
    final GlkDispatch glk;

    GlkIOSys(GlkDispatch glk, int rock) {
        super(2, rock);
        this.glk = glk;
    }

    @Override
    void streamChar(Machine machine, int ch) {
        try {
            glk.glk.putChar(ch);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void streamUnichar(Machine machine, int ch) {
        throw new RuntimeException("unimplemented");
    }

    @Override
    void streamNum(Machine machine, int num) {
        throw new RuntimeException("unimplemented");
    }

    @Override
    void putString(Machine machine, int addr) {
        throw new RuntimeException("unimplemented");
    }

    @Override
    void putStringUnicode(Machine machine, int addr) {
        throw new RuntimeException("unimplemented");
    }
}
