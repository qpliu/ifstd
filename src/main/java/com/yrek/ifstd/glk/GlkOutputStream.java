package com.yrek.ifstd.glk;

import java.io.IOException;
import java.io.OutputStream;

public class GlkOutputStream extends OutputStream {
    private final GlkStream stream;

    public GlkOutputStream(GlkStream stream) {
        this.stream = stream;
    }

    @Override
    public void write(int b) throws IOException {
        stream.putChar(b);
    }
}
