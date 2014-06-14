package com.yrek.ifstd.glulx;

abstract class IOSys {
    protected final int rock;

    protected IOSys(int rock) {
        this.rock = rock;
    }

    int getRock() {
        return rock;
    }

    abstract int getMode();
    abstract void streamChar(int ch);
    abstract void streamUnichar(int ch);
    abstract void streamNum(int num);
    abstract void streamStr(Machine machine, int addr);
}
