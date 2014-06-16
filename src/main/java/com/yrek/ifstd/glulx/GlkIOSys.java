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
        try {
            glk.glk.putString(String.valueOf(num));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    boolean putChar(Machine machine, int ch, boolean resuming) {
        try {
            glk.glk.putChar(ch);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resuming;
    }

    @Override
    boolean putCharUnicode(Machine machine, int ch, boolean resuming) {
        throw new RuntimeException("unimplemented");
    }

    @Override
    boolean putString(Machine machine, int addr, boolean resuming) {
        try {
            glk.glk.putString(new CString(machine.state, addr));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resuming;
    }

    @Override
    boolean putStringUnicode(Machine machine, int addr, boolean resuming) {
        throw new RuntimeException("unimplemented");
    }
}
