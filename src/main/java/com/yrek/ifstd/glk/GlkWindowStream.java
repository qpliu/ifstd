package com.yrek.ifstd.glk;

import java.io.InputStream;
import java.io.IOException;
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
    public GlkStreamResult close() throws IOException {
        return windowStream.close();
    }

    @Override
    public void putChar(int ch) throws IOException {
        windowStream.putChar(ch);
        if (echoStream != null) {
            echoStream.putChar(ch);
        }
    }

    @Override
    public void putString(CharSequence string) throws IOException {
        windowStream.putString(string);
        if (echoStream != null) {
            throw new RuntimeException("unimplemented");
        }
    }

    @Override
    public void putBuffer(GlkByteArray buffer) throws IOException {
        windowStream.putBuffer(buffer);
        if (echoStream != null) {
            throw new RuntimeException("unimplemented");
        }
    }

    @Override
    public void putCharUni(int ch) throws IOException {
        windowStream.putCharUni(ch);
        if (echoStream != null) {
            echoStream.putCharUni(ch);
        }
    }

    @Override
    public void putStringUni(UnicodeString string) throws IOException {
        windowStream.putStringUni(string);
        if (echoStream != null) {
            throw new RuntimeException("unimplemented");
        }
    }

    @Override
    public void putBufferUni(GlkIntArray buffer) throws IOException {
        windowStream.putBufferUni(buffer);
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
    public int getChar() throws IOException {
        return windowStream.getChar();
    }

    @Override
    public int getLine(GlkByteArray buffer) throws IOException {
        return windowStream.getLine(buffer);
    }

    @Override
    public int getBuffer(GlkByteArray buffer) throws IOException {
        return windowStream.getBuffer(buffer);
    }

    @Override
    public int getCharUni() throws IOException {
        return windowStream.getCharUni();
    }

    @Override
    public int getLineUni(GlkIntArray buffer) throws IOException {
        return windowStream.getLineUni(buffer);
    }

    @Override
    public int getBufferUni(GlkIntArray buffer) throws IOException {
        return windowStream.getBufferUni(buffer);
    }

    @Override
    public void setPosition(int position, int seekMode) throws IOException {
        windowStream.setPosition(position, seekMode);
    }

    @Override
    public int getPosition() throws IOException {
        return windowStream.getPosition();
    }
}
