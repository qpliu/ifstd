package com.yrek.ifstd.test.glk;

import java.io.IOException;

import com.yrek.ifstd.glk.GlkByteArray;
import com.yrek.ifstd.glk.GlkIntArray;
import com.yrek.ifstd.glk.GlkStream;
import com.yrek.ifstd.glk.GlkStreamResult;

public class TestGlkStream extends GlkStream {
    final TestGlk glk;
    final String name;

    public TestGlkStream(TestGlk glk, String name, int rock) {
        super(rock);
        this.glk = glk;
        this.name = name;
    }

    @Override
    public GlkStreamResult close() throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public void putChar(int ch) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public void putString(GlkByteArray string) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public void putBuffer(GlkByteArray buffer) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public void setStyle(int style) {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getChar() throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getLine(GlkByteArray buffer) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getBuffer(GlkByteArray buffer) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public void setPosition(int position, int seekMode) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getPosition() throws IOException {
        throw new RuntimeException("unimplemented");
    }
}
