package com.yrek.ifstd.glulx;

import java.io.File;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Map;

import com.yrek.ifstd.glk.Glk;
import com.yrek.ifstd.glk.GlkDispatch;

public class Glulx implements Runnable, Serializable {
    private static final long serialVersionUID = 0L;
    public static final int GlulxVersion = 0x00030102;
    public static final int TerpVersion = 0x00000000;

    public static PrintStream trace = null;

    private final Machine machine;
    private transient boolean suspend = false;
    private transient boolean suspended = false;

    public Glulx(byte[] byteData, GlkDispatch glk) throws IOException {
        machine = new Machine(byteData, null, glk);
    }

    public Glulx(File fileData, GlkDispatch glk) throws IOException {
        machine = new Machine(null, fileData, glk);
    }

    public Glulx(byte[] byteData, Glk glk) throws IOException {
        machine = new Machine(byteData, null, new GlkDispatch(glk));
    }

    public Glulx(File fileData, Glk glk) throws IOException {
        machine = new Machine(null, fileData, new GlkDispatch(glk));
    }

    @Override
    public void run() {
        suspended = false;
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
                synchronized (machine) {
                    suspended = true;
                    machine.notifyAll();
                }
                return;
            }
        }
    }

    public void suspend(boolean wait) throws InterruptedException {
        suspend = true;
        if (wait) {
            synchronized (machine) {
                while (!suspended) {
                    machine.wait();
                }
            }
        }
    }

    public void resume(GlkDispatch glk) {
        assert suspended;
        machine.glk = glk;
    }

    public static void resetProfilingData() {
        Instruction.resetProfilingData();
    }

    public static Map<String,long[]> profilingData() {
        return Instruction.profilingData();
    }
}
