package com.yrek.ifstd.glk;

public abstract class GlkFile extends GlkObject {
    public GlkFile(int rock) {
        super(rock);
    }

    public abstract void delete();

    public abstract boolean exists();
}
