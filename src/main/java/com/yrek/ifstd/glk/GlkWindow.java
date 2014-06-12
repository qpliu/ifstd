package com.yrek.ifstd.glk;

public abstract class GlkWindow extends GlkObject {
    public static final int TypeAll = 0;
    public static final int TypePair = 1;
    public static final int TypeBlank = 2;
    public static final int TypeTextBuffer = 3;
    public static final int TypeTextGrid = 4;
    public static final int TypeGraphics = 5;

    protected GlkWindow(int rock) {
        super(rock);
    }

    public abstract GlkStreamResult close();
    public abstract GlkWindowSize getSize();
    public abstract void setArrangement(int method, int size, GlkWindow key);
    public abstract GlkWindowArrangement getArrangement();
    public abstract int getType();
    public abstract GlkWindow getParent();
    public abstract GlkWindow getSibling();
    public abstract void clear();
    public abstract void moveCursor(int x, int y);
    public abstract boolean styleDistinguish(int style1, int style2);
    public abstract Integer styleMeasure(int style, int hint);
    public abstract GlkWindowStream getStream();
    public abstract void requestLineEvent(GlkByteArray buffer, int initLength);
    public abstract void requestCharEvent();
    public abstract void requestMouseEvent();
    public abstract GlkEvent cancelLineEvent();
    public abstract void cancelCharEvent();
    public abstract void cancelMouseEvent();
}
