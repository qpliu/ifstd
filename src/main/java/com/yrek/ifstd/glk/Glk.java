package com.yrek.ifstd.glk;

// Implemented by the Glk provider, called by the Glk user
public interface Glk {
    public void exit();
    public void setInterruptHandler(Runnable handler);
    public void tick();

    public int gestalt(int selector, int value);
    public int gestaltExt(int selector, int value, GlkIntArray array);

    public GlkWindow windowGetRoot();
    public GlkWindow windowOpen(GlkWindow split, int method, int size, int winType, int rock);

    public void setWindow(GlkWindow window);

    public GlkStream streamOpenFile(GlkFile file, int mode, int rock);
    public GlkStream streamOpenMemory(GlkByteArray memory, int mode, int rock);
    public void streamSetCurrent(GlkStream stream);
    public GlkStream streamGetCurrent();
    public void putChar(int ch);
    public void putString(GlkByteArray string);
    public void putBuffer(GlkByteArray buffer);
    public void setStyle(int style);

    public void styleHintSet(int winType, int style, int hint, int value);
    public void styleHintClear(int winType, int style, int hint);

    public GlkFile fileCreateTemp(int usage, int rock);
    public GlkFile fileCreateByName(int usage, GlkByteArray name, int rock);
    public GlkFile fileCreateByPrompt(int usage, int mode, int rock);
    public GlkFile fileCreateFromFile(int usage, GlkFile file, int rock);

    public GlkEvent select();
    public GlkEvent selectPoll();

    public void requestTimerEvents(int millisecs);
}
