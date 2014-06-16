package com.yrek.ifstd.glulx;

class CString implements CharSequence {
    final State state;
    final int addr;
    final int length;

    CString(State state, int addr, int length) {
        this.state = state;
        this.addr = addr;
        this.length = length;
    }

    CString(State state, int addr) {
        this.state = state;
        this.addr = addr;
        int end = addr;
        while (state.load8(end) != 0) {
            end++;
        }
        this.length = end - addr;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        return (char) (state.load8(addr + index) & 255);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new CString(state, addr+start, end-start);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) (state.load8(addr + i) & 255));
        }
        return sb.toString();
    }
}
