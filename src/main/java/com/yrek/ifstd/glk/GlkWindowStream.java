package com.yrek.ifstd.glk;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class GlkWindowStream extends GlkStream {
    protected final GlkWindow window;
    protected GlkStream echoStream = null;

    protected GlkWindowStream(GlkWindow window, int rock) {
        super(rock);
        this.window = window;
    }

    public GlkStream getEchoStream() {
        return echoStream;
    }

    public void setEchoStream(GlkStream echoStream) {
        this.echoStream = echoStream;
    }
 
    @Override
    public GlkStreamResult close() throws IOException {
        throw new IllegalStateException();
    }

    @Override
    public void putChar(int ch) throws IOException {
        if (echoStream != null) {
            echoStream.putChar(ch);
        }
    }

    @Override
    public void putString(CharSequence string) throws IOException {
        if (echoStream != null) {
            echoStream.putString(string);
        }
    }

    @Override
    public void putBuffer(GlkByteArray buffer) throws IOException {
        if (echoStream != null) {
            echoStream.putBuffer(buffer);
        }
    }

    @Override
    public void putCharUni(int ch) throws IOException {
        if (echoStream != null) {
            echoStream.putCharUni(ch);
        }
    }

    @Override
    public void putStringUni(UnicodeString string) throws IOException {
        if (echoStream != null) {
            throw new RuntimeException("unimplemented");
        }
    }

    @Override
    public void putBufferUni(GlkIntArray buffer) throws IOException {
        if (echoStream != null) {
            throw new RuntimeException("unimplemented");
        }
    }

    @Override
    public void setStyle(int style) {
        if (echoStream != null) {
            echoStream.setStyle(style);
        }
    }
}
