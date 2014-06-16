package com.yrek.ifstd.glulx;

class NullIOSys extends IOSys {
    private static final boolean TRACE = false;

    NullIOSys(int rock) {
        super(0, rock);
    }

    @Override
    void streamChar(Machine machine, int ch) {
        if (TRACE) {
            System.out.print((char) ch);
        }
    }

    @Override
    void streamUnichar(Machine machine, int ch) {
        if (TRACE) {
            System.out.print((char) ch);
        }
    }

    @Override
    void streamNum(Machine machine, int num) {
        if (TRACE) {
            System.out.print(num);
        }
    }

    @Override
    boolean putChar(Machine machine, int ch, boolean resuming) {
        if (TRACE) {
            System.out.print((char) ch);
        }
        return resuming;
    }

    @Override
    boolean putCharUnicode(Machine machine, int ch, boolean resuming) {
        if (TRACE) {
            System.out.print((char) ch);
        }
        return resuming;
    }

    @Override
    boolean putString(Machine machine, int addr, boolean resuming) {
        if (TRACE) {
            System.out.print(new CString(machine.state, addr));
        }
        return resuming;
    }

    @Override
    boolean putStringUnicode(Machine machine, int addr, boolean resuming) {
        return resuming;
    }
}
