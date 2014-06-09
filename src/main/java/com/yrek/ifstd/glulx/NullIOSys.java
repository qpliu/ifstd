package com.yrek.ifstd.glulx;

class NullIOSys extends IOSys {
    NullIOSys(int rock) {
        super(rock);
    }

    int getMode() {
        return 0;
    }
}
