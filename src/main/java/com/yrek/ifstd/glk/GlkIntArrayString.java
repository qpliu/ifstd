package com.yrek.ifstd.glk;

public class GlkIntArrayString implements GlkString {
    public final GlkIntArray intArray;
    private final int maxWriteIndex;
    private final int startIndex;
    private int endIndex;

    public GlkIntArrayString(GlkIntArray intArray, int maxWriteIndex) {
        this.intArray = intArray;
        this.maxWriteIndex = maxWriteIndex;
        this.startIndex = 0;
        this.endIndex = startIndex;
        while (intArray.getIntElementAt(endIndex) != 0) {
            endIndex++;
        }
    }

    public GlkIntArrayString(GlkIntArray intArray, int maxWriteIndex, int startIndex, int endIndex) {
        this.intArray = intArray;
        this.maxWriteIndex = maxWriteIndex;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public char charAt(int index) {
        return (char) (intArray.getIntElementAt(startIndex + index));
    }

    @Override
    public int length() {
        return endIndex - startIndex;
    }

    @Override
    public GlkIntArrayString subSequence(int start, int end) {
        return new GlkIntArrayString(intArray, 0, startIndex + start, startIndex + end);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < endIndex; i++) {
            for (char c : Character.toChars(intArray.getIntElementAt(startIndex + i))) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Override
    public GlkIntArrayString append(CharSequence csq) {
        return append(csq, 0, csq.length());
    }

    @Override
    public GlkIntArrayString append(CharSequence csq, int start, int end) {
        for (int i = start; i < end && endIndex < maxWriteIndex; ) {
            int codePoint = Character.codePointAt(csq, i);
            i += Character.charCount(codePoint);
            intArray.setIntElementAt(endIndex, codePoint);
            endIndex++;
        }
        return this;
    }

    @Override
    public GlkIntArrayString append(char ch) {
        if (endIndex < maxWriteIndex) {
            intArray.setIntElementAt(endIndex, ch);
            endIndex++;
        }
        return this;
    }
}
