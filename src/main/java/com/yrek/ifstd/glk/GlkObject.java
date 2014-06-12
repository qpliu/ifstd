package com.yrek.ifstd.glk;

public abstract class GlkObject {
    private final int rock;
    private int pointer = 0;
    private boolean destroyed = false;

    protected GlkObject(int rock) {
        this.rock = rock;
    }

    public int getRock() {
        return rock;
    }

    public int getPointer() {
        return pointer;
    }

    void setPointer(int pointer) {
        this.pointer = pointer;
    }

    boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
    }

    public static int getPointer(GlkObject obj) {
        return obj == null || obj.isDestroyed() ? 0 : obj.getPointer();
    }
}
