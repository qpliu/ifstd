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

    public ZCode initGlk(int foregroundColor, int backgroundColor) throws IOException {
        machine.initGlk(foregroundColor, backgroundColor);
        return this;
    }

    @Override
    public void run() {
        suspended = false;
        suspending = false;
        machine.initForRun();
        try {
            switch (machine.state.version) {
            case 5: case 7: case 8:
                for (;;) {
                    switch (Instruction5.executeNext(machine)) {
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
            default:
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
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void suspend(boolean wait) throws InterruptedException {
        suspending = true;
        machine.suspending = true;
        if (machine.mainWindow != null) {
            machine.mainWindow.cancelCharEvent();
            machine.mainWindow.cancelLineEvent();
        }
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
        machine.glk = glk;
    }

    public boolean suspending() {
        return suspending;
    }

    public boolean suspended() {
        return suspended;
    }
}
