package com.yrek.ifstd.glk;

// Implemented by the Glk provider, called by the Glk user
public interface Glk {
    public int dispatch(int selector, GlkArg[] args);
}
