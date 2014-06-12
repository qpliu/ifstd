package com.yrek.ifstd.glk;

import java.io.InputStream;
import java.io.OutputStream;

public class GlkWindowStream extends GlkStream {
    private final GlkStream windowStream;
    private GlkStream echoStream = null;

    protected GlkWindowStream(GlkStream windowStream) {
        super(windowStream.getRock());
        this.windowStream = windowStream;
    }

    public GlkStream getEchoStream() {
        return echoStream;
    }

    public void setEchoStream(GlkStream echoStream) {
        this.echoStream = echoStream;
    }

    @Override
    public GlkStreamResult close() {
        return windowStream.close();
    }

    @Override
    public void putChar(int ch) {
        windowStream.putChar(ch);
        if (echoStream != null) {
            echoStream.putChar(ch);
        }
    }

    @Override
    public void putString(GlkByteArray string) {
        windowStream.putString(string);
        if (echoStream != null) {
            throw new RuntimeException("unimplemented");
        }
    }

    @Override
    public void putBuffer(GlkByteArray buffer) {
        windowStream.putBuffer(buffer);
        if (echoStream != null) {
            throw new RuntimeException("unimplemented");
        }
    }

    @Override
    public void setStyle(int style) {
        windowStream.setStyle(style);
        if (echoStream != null) {
            echoStream.setStyle(style);
        }
    }

    @Override
    public int getChar() {
        return windowStream.getChar();
    }

    @Override
    public int getLine(GlkByteArray buffer) {
        return windowStream.getLine(buffer);
    }

    @Override
    public int getBuffer(GlkByteArray buffer) {
        return windowStream.getBuffer(buffer);
    }

    @Override
    public void setPosition(int position, int seekMode) {
        windowStream.setPosition(position, seekMode);
    }

    @Override
    public int getPosition() {
        return windowStream.getPosition();
    }
}
