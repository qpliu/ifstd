package com.yrek.ifstd.glk;

import java.io.IOException;

// Implemented by the Glk provider, called by the Glk user
public interface Glk {
    public static final int GlkVersion = 0x00000704;

    public void main(Runnable main) throws IOException;
    public void exit();
    public void setInterruptHandler(Runnable handler);
    public void tick();

    public int gestalt(int selector, int value);
    public int gestaltExt(int selector, int value, GlkIntArray array);

    public GlkWindow windowGetRoot();
    public GlkWindow windowOpen(GlkWindow split, int method, int size, int winType, int rock);

    public void setWindow(GlkWindow window);

    public GlkStream streamOpenFile(GlkFile file, int mode, int rock) throws IOException;
    public GlkStream streamOpenMemory(GlkByteArray memory, int mode, int rock);
    public void streamSetCurrent(GlkStream stream);
    public GlkStream streamGetCurrent();
    public void putChar(int ch) throws IOException;
    public void putString(CharSequence string) throws IOException;
    public void putBuffer(GlkByteArray buffer) throws IOException;
    public void setStyle(int style);

    public void styleHintSet(int winType, int style, int hint, int value);
    public void styleHintClear(int winType, int style, int hint);

    public GlkFile fileCreateTemp(int usage, int rock) throws IOException;
    public GlkFile fileCreateByName(int usage, CharSequence name, int rock) throws IOException;
    public GlkFile fileCreateByPrompt(int usage, int mode, int rock) throws IOException;
    public GlkFile fileCreateFromFile(int usage, GlkFile file, int rock) throws IOException;

    public GlkEvent select() throws IOException;
    public GlkEvent selectPoll() throws IOException;

    public void requestTimerEvents(int millisecs);
}
