package com.yrek.ifstd.glk;

public class GlkStreamResult {
    public final int readCount;
    public final int writeCount;

    public GlkStreamResult(int readCount, int writeCount) {
        this.readCount = readCount;
        this.writeCount = writeCount;
    }
}
