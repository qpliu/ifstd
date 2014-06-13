package com.yrek.ifstd.glk;

public class GlkEvent {
    public static final int TypeNone = 0;
    public static final int TypeTimer = 1;
    public static final int TypeCharInput = 2;
    public static final int TypeLineInput = 3;
    public static final int TypeMouseInput = 4;
    public static final int TypeArrange = 5;
    public static final int TypeRedraw = 6;
    public static final int TypeSoundNotify = 7;
    public static final int TypeHyperlink = 8;
    public static final int TypeVolumeNotify = 9;

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
