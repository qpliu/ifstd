package com.yrek.ifstd.glk;

public class GlkIntArrayString implements UnicodeString {
    public final GlkIntArray intArray;
    private final int startIndex;
    private final int endIndex;

    public GlkIntArrayString(GlkIntArray intArray) {
        this.intArray = intArray;
        this.startIndex = 0;
        int end = startIndex;
        while (intArray.getIntElementAt(end) != 0) {
            end++;
        }
        this.endIndex = end;
    }

    public GlkIntArrayString(GlkIntArray intArray, int startIndex, int endIndex) {
        this.intArray = intArray;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public char charAt(int index) {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int length() {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < endIndex; i++) {
            sb.append(Character.toChars(intArray.getIntElementAt(startIndex + i)));
        }
        return sb.toString();
    }

    @Override
    public int codePointCount() {
        return endIndex - startIndex;
    }

    @Override
    public int codePointAt(int index) {
        return intArray.getIntElementAt(startIndex + index);
    }
}
