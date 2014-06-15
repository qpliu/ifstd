package com.yrek.ifstd.glulx;

class NullIOSys extends IOSys {
    NullIOSys(int rock) {
        super(0, rock);
    }

    @Override
    void streamChar(Machine machine, int ch) {
    }

    @Override
    void streamUnichar(Machine machine, int ch) {
    }

    @Override
    void streamNum(Machine machine, int num) {
    }

    @Override
    void putString(Machine machine, int addr) {
    }

    @Override
    void putStringUnicode(Machine machine, int addr) {
    }
}
