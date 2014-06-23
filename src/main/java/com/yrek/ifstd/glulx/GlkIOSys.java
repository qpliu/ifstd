package com.yrek.ifstd.glulx;

import java.io.IOException;

import com.yrek.ifstd.glk.GlkDispatch;

class GlkIOSys extends IOSys {
    private static final long serialVersionUID = 0L;

    GlkIOSys(int rock) {
        super(2, rock);
    }

    @Override
    void resumePrintNumber(Machine machine, int number, int pos) {
        try {
            machine.glk.glk.putString(String.valueOf(number).substring(pos));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void streamChar(Machine machine, int ch) {
        try {
            machine.glk.glk.putChar(ch);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void streamUnichar(Machine machine, int ch) {
        try {
            machine.glk.glk.putCharUni(ch);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void streamNum(Machine machine, int num) {
        try {
            machine.glk.glk.putString(String.valueOf(num));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    void streamString(Machine machine, int addr) {
        try {
            machine.glk.glk.putString(new CString(machine.state, addr));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void streamStringUnicode(Machine machine, int addr) {
        try {
            machine.glk.glk.putStringUni(new UString(machine.state, addr));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
