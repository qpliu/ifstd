package com.yrek.ifstd.glk;

import java.io.IOException;

public abstract class GlkStream extends GlkObject {
    public static final int SeekModeStart = 0;
    public static final int SeekModeCurrent = 1;
    public static final int SeekModeEnd = 2;

    public static final int StyleNormal = 0;
    public static final int StyleEmphasized = 1;
    public static final int StylePreformatted = 2;
    public static final int StyleHeader = 3;
    public static final int StyleSubheader = 4;
    public static final int StyleAlert = 5;
    public static final int StyleNote = 6;
    public static final int StyleBlockQuote = 7;
    public static final int StyleInput = 8;
    public static final int StyleUser1 = 9;
    public static final int StyleUser2 = 10;

    protected GlkStream(int rock) {
        super(rock);
    }

    public abstract GlkStreamResult close() throws IOException;
    public abstract void putChar(int ch) throws IOException;
    public abstract void putString(CharSequence string) throws IOException;
    public abstract void putBuffer(GlkByteArray buffer) throws IOException;
    public abstract void setStyle(int style);
    public abstract int getChar() throws IOException;
    public abstract int getLine(GlkByteArray buffer) throws IOException;
    public abstract int getBuffer(GlkByteArray buffer) throws IOException;
    public abstract void setPosition(int position, int seekMode) throws IOException;
    public abstract int getPosition() throws IOException;
}
