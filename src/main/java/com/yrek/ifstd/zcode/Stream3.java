package com.yrek.ifstd.zcode;

class Stream3 implements Appendable {
    final StringBuilder sb = new StringBuilder();
    final int table;

    Stream3(int table) {
        this.table = table;
    }

    @Override
    public Stream3 append(CharSequence cs) {
        sb.append(cs);
        return this;
    }

    @Override
    public Stream3 append(CharSequence cs, int start, int end) {
        sb.append(cs, start, end);
        return this;
    }

    @Override
    public Stream3 append(char ch) {
        sb.append(ch);
        return this;
    }

    void deselect(State state) {
        state.store16(table, ZSCII.writeToRam(state, sb, table+2));
    }
}
