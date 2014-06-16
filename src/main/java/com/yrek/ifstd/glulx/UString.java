package com.yrek.ifstd.glulx;

import com.yrek.ifstd.glk.UnicodeString;

class UString implements UnicodeString {
    final State state;
    final int addr;
    final int length;

    UString(State state, int addr, int length) {
        this.state = state;
        this.addr = addr;
        this.length = length;
    }

    UString(State state, int addr) {
        this.state = state;
        this.addr = addr;
        int end = addr;
        while (state.load32(end) != 0) {
            end += 4;
        }
        this.length = end - addr;
    }

    @Override
    public int length() {
        int len = 0;
        for (int i = 0; i < length; i++) {
            len += Character.charCount(codePointAt(i));
        }
        return len;
    }

    @Override
    public char charAt(int index) {
        int charIndex = 0;
        for (int i = 0; i < length; i++) {
            int codePoint = codePointAt(i);
            int codePointCount = Character.charCount(codePoint);
            if (charIndex + codePointCount < index) {
                charIndex += codePointCount;
            } else {
                return Character.toChars(codePointAt(charIndex))[index - charIndex];
            }
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(Character.toChars(codePointAt(i)));
        }
        return sb.toString();
    }

    @Override
    public int codePointCount() {
        return length;
    }

    @Override
    public int codePointAt(int index) {
        return state.load32(addr + 4*index);
    }
}
