package com.yrek.ifstd.glulx;

class NullIOSys extends IOSys {
    NullIOSys(int rock) {
        super(rock);
    }

    @Override
    int getMode() {
        return 0;
    }

    @Override
    void streamChar(int ch) {
    }

    @Override
    void streamUnichar(int ch) {
    }

    @Override
    void streamNum(int num) {
    }

    @Override
    void streamStr(Machine machine, int addr) {
    }
}
