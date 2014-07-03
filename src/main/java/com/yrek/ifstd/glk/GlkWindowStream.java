package com.yrek.ifstd.glk;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class GlkWindowStream extends GlkStream {
    protected final GlkWindow window;
    protected GlkStream echoStream = null;

    protected GlkWindowStream(GlkWindow window) {
        super(0);
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

    @Override
    public void setHyperlink(int linkVal) {
        if (echoStream != null) {
            echoStream.setHyperlink(linkVal);
        }
    }

    @Override
    public int getChar() throws IOException {
        throw new IllegalArgumentException();
    }

    @Override
    public int getLine(GlkByteArray buffer) throws IOException {
        throw new IllegalArgumentException();
    }

    @Override
    public int getBuffer(GlkByteArray buffer) throws IOException {
        throw new IllegalArgumentException();
    }

    @Override
    public int getCharUni() throws IOException {
        throw new IllegalArgumentException();
    }

    @Override
    public int getLineUni(GlkIntArray buffer) throws IOException {
        throw new IllegalArgumentException();
    }

    @Override
    public int getBufferUni(GlkIntArray buffer) throws IOException {
        throw new IllegalArgumentException();
    }

    @Override
    public void setPosition(int position, int seekMode) throws IOException {
    }

    @Override
    public int getPosition() throws IOException {
        return 0;
    }

    @Override
    public DataOutput getDataOutput() {
        throw new IllegalArgumentException();
    }

    @Override
    public DataInput getDataInput() {
        throw new IllegalArgumentException();
    }
}
