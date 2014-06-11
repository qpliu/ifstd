package com.yrek.ifstd.glk;

// Implemented by the Glk provider, called by the Glk user
public interface Glk {
    public void exit();

    public void setInterruptHandler(Runnable handler);

    public void tick();

    public static final int gestaltVersion = 0;
    public static final int gestaltCharInput = 1;
    public static final int gestaltLineInput = 2;
    public static final int gestaltCharOutput = 3;
    public static final int gestaltCharOutput_CannotPrint = 0;
    public static final int gestaltCharOutput_ApproxPrint = 1;
    public static final int gestaltCharOutput_ExactPrint = 2;
    public static final int gestaltMouseInput = 4;
    public static final int gestaltTimer = 5;
    public static final int gestaltGraphics = 6;
    public static final int gestaltDrawImage = 7;
    public static final int gestaltSound = 8;
    public static final int gestaltSoundVolume = 9;
    public static final int gestaltSoundNotify = 10;
    public static final int gestaltHyperlinks = 11;
    public static final int gestaltHyperlinkInput = 12;
    public static final int gestaltSoundMusic = 13;
    public static final int gestaltGraphicsTransparency = 14;
    public static final int gestaltUnicode = 15;
    public static final int gestaltUnicodeNorm = 16;
    public static final int gestaltLineInputEcho = 17;
    public static final int gestaltLineTerminators = 18;
    public static final int gestaltLineTerminatorKey = 19;
    public static final int gestaltDateTime = 20;
    public static final int gestaltSound2 = 21;
    public static final int gestaltResourceStream = 22;

    public int gestalt(int selector, int value);
    public int gestaltExt(int selector, int value, GlkArg arg);

    public static final int winTypeAll = 0;
    public static final int winTypePair = 1;
    public static final int winTypeBlank = 2;
    public static final int winTypeTextBuffer = 3;
    public static final int winTypeTextGrid = 4;
    public static final int winTypeGraphics = 5;

    public static final int winMethodLeft = 0x0;
    public static final int winMethodRight = 0x1;
    public static final int winMethodAbove = 0x2;
    public static final int winMethodBelow = 0x3;
    public static final int winMethodDirMask = 0xf;
    public static final int winMethodFixed = 0x10;
    public static final int winMethodProportional = 0x11;
    public static final int winMethodDivisionMask = 0xf0;
    public static final int winMethodBorder = 0x000;
    public static final int winMethodNoBorder = 0x100;
    public static final int winMethodBorderMask = 0x100;

    public GlkWindow windowGetRoot();
    public GlkWindow windowOpen(GlkWindow split, int method, int size, int winType, int rock);

    public void setWindow(GlkWindow window);

    public GlkStream streamOpenFile(GlkFile file, int mode, int rock);
    public GlkStream streamOpenMemory(GlkArg arg, int length, int mode, int rock);
    public void streamSetCurrent(GlkStream stream);
    public GlkStream streamGetCurrent();
    public void putChar(int ch);
    public void putString(GlkArg string);
    public void putBuffer(GlkArg buffer);

    public void styleHintSet(int winType, int style, int hint, int value);
    public void styleHintClear(int winType, int style, int hint);

    public GlkFile fileCreateTemp(int usage, int rock);
    public GlkFile fileCreateByName(int usage, GlkArg name, int rock);
    public GlkFile fileCreateByPrompt(int usage, int mode, int rock);
    public GlkFile fileCreateFromFile(int usage, GlkFile file, int rock);

    public GlkEvent select();
    public GlkEvent selectPoll();

    public void requestTimerEvents(int millisecs);
    public void requestLineEvent(GlkWindow window, GlkArg buffer, int initLength);
    public void requestCharEvent(GlkWindow window);
    public void requestMouseEvent(GlkWindow window);
    public void cancelLineEvent(GlkWindow window, GlkArg event);
    public void cancelCharEvent(GlkWindow window);
    public void cancelMouseEvent(GlkWindow window);
}
