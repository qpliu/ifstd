package com.yrek.ifstd.glk;

public abstract class GlkWindow extends GlkObject {
    protected GlkWindow(int rock) {
        super(rock);
    }

    public abstract GlkStreamResult close();
    public abstract void getSize(GlkArg width, GlkArg height);
    public abstract void setArrangement(int method, int size, GlkWindow keyWin);
    public abstract GlkWindow getArrangement(GlkArg method, GlkArg size);
    public abstract int getWinType();
    public abstract GlkWindow getParent();
    public abstract GlkWindow getSibling();
    public abstract void clear();
    public abstract void moveCursor(int x, int y);

    public abstract GlkWindowStream getStream();
}
