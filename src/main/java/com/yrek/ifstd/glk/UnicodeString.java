package com.yrek.ifstd.glk;

public interface UnicodeString extends CharSequence {
    public int codePointCount();
    public int codePointAt(int index);

    public class US implements UnicodeString {
        private final CharSequence cs;

        public US(CharSequence cs) {
            this.cs = cs;
        }

        @Override
        public char charAt(int index) {
            return cs.charAt(index);
        }

        @Override
        public int length() {
            return cs.length();
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return toString().subSequence(start, end);
        }

        @Override
        public String toString() {
            return cs.toString();
        }

        @Override
        public int codePointCount() {
            return Character.codePointCount(cs, 0, cs.length());
        }

        @Override
        public int codePointAt(int index) {
            int i = 0;
            for (;;) {
                if (i > index) {
                    throw new IllegalArgumentException();
                }
                int codePoint = Character.codePointAt(cs, i);
                if (i == index) {
                    return codePoint;
                }
                i += Character.charCount(codePoint);
            }
        }
    }
}
