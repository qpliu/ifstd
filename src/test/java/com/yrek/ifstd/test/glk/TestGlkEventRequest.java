package com.yrek.ifstd.test.glk;

import java.io.IOException;

import com.yrek.ifstd.glk.GlkEvent;

public abstract class TestGlkEventRequest {
    public abstract GlkEvent select() throws IOException;

    public GlkEvent poll() throws IOException {
        return select();
    }
}
