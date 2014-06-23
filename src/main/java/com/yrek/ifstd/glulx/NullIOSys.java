package com.yrek.ifstd.glulx;

class NullIOSys extends IOSys {
    private static final long serialVersionUID = 0L;
    private static final boolean TRACE = false;

    NullIOSys(int rock) {
        super(0, rock);
    }

    @Override
    void resumePrintNumber(Machine machine, int number, int pos) {
        if (TRACE && Glulx.trace != null) {
            Glulx.trace.print(String.valueOf(number).substring(pos));
        }
    }

    @Override
    void streamChar(Machine machine, int ch) {
        if (TRACE && Glulx.trace != null) {
            Glulx.trace.print((char) ch);
        }
    }

    @Override
    void streamUnichar(Machine machine, int ch) {
        if (TRACE && Glulx.trace != null) {
            Glulx.trace.print((char) ch);
        }
    }

    @Override
    void streamNum(Machine machine, int num) {
        if (TRACE && Glulx.trace != null) {
            Glulx.trace.print(num);
        }
    }

    @Override
    void streamString(Machine machine, int addr) {
        if (TRACE && Glulx.trace != null) {
            Glulx.trace.print(new CString(machine.state, addr));
        }
    }

    @Override
    void streamStringUnicode(Machine machine, int addr) {
        if (TRACE && Glulx.trace != null) {
            Glulx.trace.print(new UString(machine.state, addr));
        }
    }
}
