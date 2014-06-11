package com.yrek.ifstd.glk;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class GlkStream extends GlkObject {
    protected GlkStream(int rock) {
        super(rock);
    }

    public abstract GlkStreamResult close();
    public abstract void putChar(int ch);
    public abstract void putString(GlkArg string);
    public abstract void putBuffer(GlkArg buffer);
    public abstract void setStyle(int style);
    public abstract int getChar();
    public abstract int getLine(GlkArg buffer);
    public abstract int getBuffer(GlkArg buffer);
    
    public abstract InputStream getInputStream();
    public abstract OutputStream getOutputStream();
}
