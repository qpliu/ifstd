package com.yrek.ifstd.glulx;

class FilterIOSys extends IOSys {
    FilterIOSys(int rock) {
        super(rock);
    }

    @Override
    int getMode() {
        return 1;
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
