package com.yrek.ifstd.glk;

// Implemented by the Glk user, passed into Glk.dispatch()
public interface GlkArg {
    public int getInt();
    public void setInt(int result);
    public Runnable getRunnable();
    public void setStreamResult(GlkStreamResult streamResult);
}
