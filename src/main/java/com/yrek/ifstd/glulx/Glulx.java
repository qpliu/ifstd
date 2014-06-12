package com.yrek.ifstd.glulx;

import java.io.File;

import java.io.IOException;

import com.yrek.ifstd.glk.Glk;
import com.yrek.ifstd.glk.GlkDispatch;

public class Glulx implements Runnable {
    public static final int GlulxVersion = 0x00030102;
    public static final int TerpVersion = 0x00000000;

    private final Machine machine;

    public Glulx(byte[] byteData, Glk glk) throws IOException {
        machine = new Machine(byteData, null, new GlkDispatch(glk));
    }

    public Glulx(File fileData, Glk glk) throws IOException {
        machine = new Machine(null, fileData, new GlkDispatch(glk));
    }

    public void run() {
        for (;;) {
            switch (Instruction.executeNext(machine)) {
            case Continue:
                break;
            case Tick:
                machine.glk.glk.tick();
                break;
            case Quit:
                return;
            }
        }
    }
}
