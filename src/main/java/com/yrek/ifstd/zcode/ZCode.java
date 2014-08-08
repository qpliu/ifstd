package com.yrek.ifstd.zcode;

import java.io.File;

import java.io.IOException;
import java.io.Serializable;

import com.yrek.ifstd.glk.GlkDispatch;

public class ZCode implements Runnable, Serializable {
    private static final long serialVersionUID = 0L;
    private final Machine machine;
    private boolean suspending = false;
    private boolean suspended = false;

    public ZCode(byte[] byteData, GlkDispatch glk) throws IOException {
        machine = new Machine(byteData, null, glk);
    }

    public ZCode(File fileData, GlkDispatch glk) throws IOException {
        machine = new Machine(null, fileData, glk);
    }

    @Override
    public void run() {
        suspended = false;
        suspending = false;
        try {
            for (;;) {
                switch (Instruction.executeNext(machine)) {
                case Continue:
                    break;
                case Tick:
                    machine.glk.glk.tick();
                    if (suspending) {
                        synchronized (machine) {
                            suspended = true;
                            machine.notifyAll();
                        }
                        return;
                    }
                    break;
                case Quit:
                    return;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void suspend(boolean wait) throws InterruptedException {
        suspending = true;
        if (wait) {
            synchronized (machine) {
                while (!suspended) {
                    machine.wait();
                }
            }
        }
    }

    public void resume(GlkDispatch glk) throws IOException {
        assert suspended;
        machine.setGlk(glk);
    }

    public boolean suspending() {
        return suspending;
    }

    public boolean suspended() {
        return suspended;
    }
}
