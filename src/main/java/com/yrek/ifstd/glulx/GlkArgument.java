package com.yrek.ifstd.glulx;

import com.yrek.ifstd.glk.GlkArg;
import com.yrek.ifstd.glk.GlkStreamResult;

class GlkArgument implements GlkArg {
    final Machine machine;
    final int value;

    GlkArgument(Machine machine, int value) {
        this.machine = machine;
        this.value = value;
    }

    @Override
    public int getInt() {
        return value;
    }

    @Override
    public void setInt(int result) {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public Runnable getRunnable() {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public void setStreamResult(GlkStreamResult streamResult) {
        throw new RuntimeException("unimplemented");
    }
}
