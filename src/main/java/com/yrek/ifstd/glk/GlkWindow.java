package com.yrek.ifstd.glk;

import java.io.IOException;

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

    public abstract GlkWindowStream getStream();
    public abstract GlkStreamResult close() throws IOException;
    public abstract GlkWindowSize getSize();
    public abstract void setArrangement(int method, int size, GlkWindow key);
    public abstract GlkWindowArrangement getArrangement();
    public abstract int getType();
    public abstract GlkWindow getParent();
    public abstract GlkWindow getSibling();
    public abstract void clear() throws IOException;
    public abstract void moveCursor(int x, int y) throws IOException;
    public abstract boolean styleDistinguish(int style1, int style2);
    public abstract Integer styleMeasure(int style, int hint);
    public abstract void requestLineEvent(GlkByteArray buffer, int initLength);
    public abstract void requestCharEvent();
    public abstract void requestMouseEvent();
    public abstract GlkEvent cancelLineEvent();
    public abstract void cancelCharEvent();
    public abstract void cancelMouseEvent();
    public abstract boolean drawImage(int resourceId, int val1, int val2) throws IOException;
    public abstract boolean drawScaledImage(int resourceId, int val1, int val2, int width, int height) throws IOException;
    public abstract void flowBreak();
    public abstract void eraseRect(int left, int top, int width, int height);
    public abstract void fillRect(int color, int left, int top, int width, int height);
    public abstract void setBackgroundColor(int color);
}
