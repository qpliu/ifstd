package com.yrek.ifstd.test.glk;

import com.yrek.ifstd.glk.GlkByteArray;
import com.yrek.ifstd.glk.GlkIntArray;

public class TestGlkArray implements GlkByteArray, GlkIntArray {
    public final int[] elements;
    private int readIndex;
    private int writeIndex;

    public TestGlkArray(int[] elements) {
        this.elements = elements;
    }

    public TestGlkArray(int size) {
        this.elements = new int[size];
    }

    @Override
    public int getByteElement() {
        readIndex++;
        return elements[readIndex - 1] & 255;
    }

    @Override
    public void setByteElement(int element) {
        elements[writeIndex] = element & 255;
        writeIndex++;
    }

    @Override
    public int getByteElementAt(int index) {
        return elements[index] & 255;
    }

    @Override
    public void setByteElementAt(int index, int element) {
        elements[index] = element & 255;
    }

    
    @Override
    public int getIntElement() {
        readIndex++;
        return elements[readIndex - 1];
    }

    @Override
    public void setIntElement(int element) {
        elements[writeIndex] = element;
        writeIndex++;
    }

    @Override
    public int getIntElementAt(int index) {
        return elements[index];
    }

    @Override
    public void setIntElementAt(int index, int element) {
        elements[index] = element;
    }


    @Override
    public int getReadArrayIndex() {
        return readIndex;
    }

    @Override
    public int setReadArrayIndex(int index) {
        readIndex = index;
        return readIndex;
    }

    @Override
    public int getWriteArrayIndex() {
        return writeIndex;
    }

    @Override
    public int setWriteArrayIndex(int index) {
        writeIndex = index;
        return writeIndex;
    }

    @Override
    public int getArrayLength() {
        return elements.length;
    }

    @Override
    public void setArrayLength(int length) {
        throw new AssertionError();
    }
}
