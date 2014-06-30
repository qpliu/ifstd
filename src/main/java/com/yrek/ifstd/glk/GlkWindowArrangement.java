package com.yrek.ifstd.glk;

public class GlkWindowArrangement {
    public static final int MethodLeft = 0x0;
    public static final int MethodRight = 0x1;
    public static final int MethodAbove = 0x2;
    public static final int MethodBelow = 0x3;
    public static final int MethodDirMask = 0xf;
    public static final int MethodFixed = 0x10;
    public static final int MethodProportional = 0x20;
    public static final int MethodDivisionMask = 0xf0;
    public static final int MethodBorder = 0x000;
    public static final int MethodNoBorder = 0x100;
    public static final int MethodBorderMask = 0x100;

    public final int method;
    public final int size;
    public final GlkWindow key;

    public GlkWindowArrangement(int method, int size, GlkWindow key) {
        this.method = method;
        this.size = size;
        this.key = key;
    }
}
