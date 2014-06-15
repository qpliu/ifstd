package com.yrek.ifstd.glulx;

import java.io.File;

import java.io.IOException;
import java.io.PrintStream;

import com.yrek.ifstd.glk.Glk;
import com.yrek.ifstd.glk.GlkDispatch;

public class Glulx implements Runnable {
    public static final int GlulxVersion = 0x00030102;
    public static final int TerpVersion = 0x00000000;

    public static PrintStream trace = null;

    private final Machine machine;
    private transient boolean suspend = false;

    public Glulx(byte[] byteData, Glk glk) throws IOException {
        machine = new Machine(byteData, null, new GlkDispatch(glk));
    }

    public Glulx(File fileData, Glk glk) throws IOException {
        machine = new Machine(null, fileData, new GlkDispatch(glk));
    }

    @Override
    public void run() {
        suspend = false;
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
            if (suspend) {
                return;
            }
        }
    }

    public void suspend() {
        suspend = true;
    }
}
