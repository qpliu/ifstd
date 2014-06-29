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

    public static final int KeycodeUnknown = -1;
    public static final int KeycodeLeft = -2;
    public static final int KeycodeRight = -3;
    public static final int KeycodeUp = -4;
    public static final int KeycodeDown = -5;
    public static final int KeycodeReturn = -6;
    public static final int KeycodeDelete = -7;
    public static final int KeycodeEscape = -8;
    public static final int KeycodeTab = -9;
    public static final int KeycodePageUp = -10;
    public static final int KeycodePageDown = -11;
    public static final int KeycodeHome = -12;
    public static final int KeycodeEnd = -13;
    public static final int KeycodeFunc1 = -17;
    public static final int KeycodeFunc2 = -18;
    public static final int KeycodeFunc3 = -19;
    public static final int KeycodeFunc4 = -20;
    public static final int KeycodeFunc5 = -21;
    public static final int KeycodeFunc6 = -22;
    public static final int KeycodeFunc7 = -23;
    public static final int KeycodeFunc8 = -24;
    public static final int KeycodeFunc9 = -25;
    public static final int KeycodeFunc10 = -26;
    public static final int KeycodeFunc11 = -27;
    public static final int KeycodeFunc12 = -28;

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
