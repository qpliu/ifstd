package com.yrek.ifstd.glk;

import java.io.IOException;

public class GlkStreamMemory extends GlkStream {
    final GlkByteArray memory;
    int inputCount = 0;
    int outputCount = 0;

    public GlkStreamMemory(GlkByteArray memory, int rock) {
        super(rock);
        this.memory = memory;
    }

    @Override
    public GlkStreamResult close() throws IOException {
        return new GlkStreamResult(inputCount, outputCount);
    }

    @Override
    public void putChar(int ch) throws IOException {
        outputCount++;
        if (memory != null && memory.getWriteArrayIndex() < memory.getArrayLength()) {
            memory.setByteElement(ch);
        }
    }

    @Override
    public void putString(CharSequence string) throws IOException {
        outputCount += string.length();
        if (memory != null) {
            for (int i = 0; i < string.length() && memory.getWriteArrayIndex() < memory.getArrayLength(); i++) {
                memory.setByteElement(string.charAt(i) & 255);
            }
        }
    }

    @Override
    public void putBuffer(GlkByteArray buffer) throws IOException {
        outputCount += buffer.getArrayLength();
        if (memory != null) {
            for (int i = 0; i < buffer.getArrayLength() && memory.getWriteArrayIndex() < memory.getArrayLength(); i++) {
                memory.setByteElement(buffer.getByteElementAt(i));
            }
        }
    }

    @Override
    public void putCharUni(int ch) throws IOException {
        outputCount++;
        memory.setByteElement(ch & 255);
    }

    @Override
    public void putStringUni(UnicodeString string) throws IOException {
        for (int i = 0; i < string.codePointCount(); i++) {
            putCharUni(string.codePointAt(i));
        }
    }

    @Override
    public void putBufferUni(GlkIntArray buffer) throws IOException {
        for (int i = 0; i < buffer.getArrayLength(); i++) {
            putCharUni(buffer.getIntElementAt(i));
        }
    }

    @Override
    public void setStyle(int style) {
    }

    @Override
    public void setHyperlink(int linkVal) {
    }

    @Override
    public int getChar() throws IOException {
        if (memory == null || memory.getReadArrayIndex() >= memory.getArrayLength()) {
            return -1;
        }
        inputCount++;
        return memory.getByteElement();
    }

    @Override
    public int getLine(GlkByteArray buffer) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getBuffer(GlkByteArray buffer) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getCharUni() throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getLineUni(GlkIntArray buffer) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getBufferUni(GlkIntArray buffer) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public void setPosition(int position, int seekMode) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getPosition() throws IOException {
        throw new RuntimeException("unimplemented");
    }
}
