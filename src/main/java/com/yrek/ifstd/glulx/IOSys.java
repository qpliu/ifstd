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
}
