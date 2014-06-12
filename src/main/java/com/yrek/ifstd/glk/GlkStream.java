package com.yrek.ifstd.glk;

public abstract class GlkStream extends GlkObject {
    public static final int seekModeStart = 0;
    public static final int seekModeCurrent = 1;
    public static final int seekModeEnd = 2;

    public static final int styleNormal = 0;
    public static final int styleEmphasized = 1;
    public static final int stylePreformatted = 2;
    public static final int styleHeader = 3;
    public static final int styleSubheader = 4;
    public static final int styleAlert = 5;
    public static final int styleNote = 6;
    public static final int styleBlockQuote = 7;
    public static final int styleInput = 8;
    public static final int styleUser1 = 9;
    public static final int styleUser2 = 10;

    protected GlkStream(int rock) {
        super(rock);
    }

    public abstract GlkStreamResult close();
    public abstract void putChar(int ch);
    public abstract void putString(GlkByteArray string);
    public abstract void putBuffer(GlkByteArray buffer);
    public abstract void setStyle(int style);
    public abstract int getChar();
    public abstract int getLine(GlkByteArray buffer);
    public abstract int getBuffer(GlkByteArray buffer);
    public abstract void setPosition(int position, int seekMode);
    public abstract int getPosition();
}
