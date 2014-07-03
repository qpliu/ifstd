package com.yrek.ifstd.glk;

import java.io.IOException;

public abstract class GlkSChannel extends GlkObject {
    protected GlkSChannel(int rock) {
        super(rock);
    }

    public abstract void destroyChannel() throws IOException;
    public abstract boolean play(int resourceId) throws IOException;
    public abstract boolean playExt(int resourceId, int repeats, boolean notify) throws IOException;
    public abstract void stop() throws IOException;
    public abstract void pause() throws IOException;
    public abstract void unpause() throws IOException;
    public abstract void setVolume(int volume) throws IOException;
    public abstract void setVolumeExt(int volume, int duration, boolean notify) throws IOException;
}
