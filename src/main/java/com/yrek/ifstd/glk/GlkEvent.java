package com.yrek.ifstd.glk;

public class GlkEvent {
    public static final int typeNone = 0;
    public static final int typeTimer = 1;
    public static final int typeCharInput = 2;
    public static final int typeLineInput = 3;
    public static final int typeMouseInput = 4;
    public static final int typeArrange = 5;
    public static final int typeRedraw = 6;
    public static final int typeSoundNotify = 7;
    public static final int typeHyperlink = 8;
    public static final int typeVolumeNotify = 9;

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
