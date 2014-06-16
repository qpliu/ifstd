package com.yrek.ifstd.glk;

public class GlkByteArrayString implements CharSequence {
    public final GlkByteArray byteArray;
    private final int startIndex;
    private int endIndex;

    public GlkByteArrayString(GlkByteArray byteArray) {
        this.byteArray = byteArray;
        this.startIndex = 0;
        this.endIndex = startIndex;
        while (byteArray.getByteElementAt(endIndex) != 0) {
            endIndex++;
        }
    }

    public GlkByteArrayString(GlkByteArray byteArray, int startIndex, int endIndex) {
        this.byteArray = byteArray;
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
        return new GlkByteArrayString(byteArray, startIndex + start, startIndex + end);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < endIndex; i++) {
            sb.append((char) (byteArray.getByteElementAt(startIndex + i)));
        }
        return sb.toString();
    }
}
