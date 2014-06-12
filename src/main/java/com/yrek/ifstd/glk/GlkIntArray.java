package com.yrek.ifstd.glk;

// Implemented by the Glk user
public interface GlkIntArray {
    public int getIntElement();
    public void setIntElement(int element);
    public int getReadArrayIndex();
    public int setReadArrayIndex(int index);
    public int getWriteArrayIndex();
    public int setWriteArrayIndex(int index);
    public int getArrayLength();
    public void setArrayLength(int length);
}
