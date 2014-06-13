package com.yrek.ifstd.glk;

public class GlkByteArrayString implements GlkString {
    public final GlkByteArray byteArray;
    private final int maxWriteIndex;
    private final int startIndex;
    private int endIndex;

    public GlkByteArrayString(GlkByteArray byteArray, int maxWriteIndex) {
        this.byteArray = byteArray;
        this.maxWriteIndex = maxWriteIndex;
        this.startIndex = 0;
        this.endIndex = startIndex;
        while (byteArray.getByteElementAt(endIndex) != 0) {
            endIndex++;
        }
    }

    public GlkByteArrayString(GlkByteArray byteArray, int maxWriteIndex, int startIndex, int endIndex) {
        this.byteArray = byteArray;
        this.maxWriteIndex = maxWriteIndex;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public char charAt(int index) {
        return (char) (byteArray.getByteElementAt(startIndex + index));
    }

    @Override
    public int length() {
        return endIndex - startIndex;
    }

    @Override
    public GlkByteArrayString subSequence(int start, int end) {
        return new GlkByteArrayString(byteArray, 0, startIndex + start, startIndex + end);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < endIndex; i++) {
            sb.append((char) (byteArray.getByteElementAt(startIndex + i)));
        }
        return sb.toString();
    }

    @Override
    public GlkByteArrayString append(CharSequence csq) {
        return append(csq, 0, csq.length());
    }

    @Override
    public GlkByteArrayString append(CharSequence csq, int start, int end) {
        for (int i = start; i < end && endIndex < maxWriteIndex; ) {
            int codePoint = Character.codePointAt(csq, i);
            i += Character.charCount(codePoint);
            byteArray.setByteElementAt(endIndex, codePoint & 255);
            endIndex++;
        }
        return this;
    }

    @Override
    public GlkByteArrayString append(char ch) {
        if (endIndex < maxWriteIndex) {
            byteArray.setByteElementAt(endIndex, ch & 255);
            endIndex++;
        }
        return this;
    }
}
