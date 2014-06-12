package com.yrek.ifstd.glk;

// Implemented by the Glk user, passed into GlkDispatch.dispatch()
public interface GlkDispatchArgument {
    public int getInt();
    public void setInt(int result);
    public GlkByteArray getByteArray();
    public GlkByteArray getString();
    public GlkIntArray getIntArray();
    public GlkIntArray getStringUnicode();
    public Runnable getRunnable();
}
