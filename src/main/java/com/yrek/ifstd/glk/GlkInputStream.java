package com.yrek.ifstd.glk;

import java.io.InputStream;
import java.io.IOException;

public class GlkInputStream extends InputStream {
    private final GlkStream stream;

    public GlkInputStream(GlkStream stream) {
        this.stream = stream;
    }

    @Override
    public int read() throws IOException {
        return stream.getChar();
    }
}
