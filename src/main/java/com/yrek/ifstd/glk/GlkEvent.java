package com.yrek.ifstd.glk;

public class GlkEvent {
    public static final int None = 0;
    public static final int Timer = 1;
    public static final int CharInput = 2;
    public static final int LineInput = 3;
    public static final int MouseInput = 4;
    public static final int Arrange = 5;
    public static final int Redraw = 6;
    public static final int SoundNotify = 7;
    public static final int Hyperlink = 8;
    public static final int VolumeNotify = 9;

    public final int type;
    public final GlkWindow window;
    public final int val1;
    public final int val2;

    public GlkEvent(int type, GlkWindow window, int val1, int val2) {
        this.type = type;
        this.window = window;
        this.val1 = val1;
        this.val2 = val2;
    }
}
