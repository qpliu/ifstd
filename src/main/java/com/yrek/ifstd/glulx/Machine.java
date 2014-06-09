package com.yrek.ifstd.glulx;

import java.util.Random;

import com.yrek.ifstd.glk.Glk;

class Machine {
    State state = new State();
    State saveUndo = null;
    Random random = new Random();
    int protectStart = 0;
    int protectLength = 0;
    IOSys ioSys = new NullIOSys(0);
    int RAMStart = 256;
    int stringTable = 0;
    OutputState outputState = null;
    int outputIndex = 0;
    Glk glk = null;

    enum OutputState {
        Latin1, Compressed, Unicode, Number;
    }
}
