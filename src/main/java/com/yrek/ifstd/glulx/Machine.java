package com.yrek.ifstd.glulx;

import java.util.Random;

class Machine {
    State state = new State();
    State saveUndo = null;
    Random random = new Random();
    int protectStart = 0;
    int protectLength = 0;
    int RAMStart = 256;
}
