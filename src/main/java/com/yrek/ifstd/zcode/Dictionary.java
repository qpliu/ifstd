package com.yrek.ifstd.zcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class Dictionary {
    private final State state;
    private final HashMap<Long,Integer> table = new HashMap<Long,Integer>();
    private final HashSet<Integer> wordChars = new HashSet<Integer>();

    Dictionary(State state) {
        this.state = state;
        init(state, state.read16(State.DICTIONARY), table, wordChars);
    }

    private static void init(State state, int address, HashMap<Long,Integer> table, HashSet<Integer> wordChars) {
        for (int i = state.read8(address); i > 0; i--) {
            address++;
            wordChars.add(state.read8(address));
        }
        address++;
        int entryLength = state.read8(address);
        int entryCount = state.read16(address+1);
        if (entryCount > 32767) {
            entryCount = 65536 - entryCount;
        }
        address += 3;
        if (state.version < 4) {
            for (int i = 0; i < entryCount; i++) {
                table.put(((long) state.read16(address)) << 16 | (long) state.read16(address+2), address);
                address += entryLength;
            }
        } else {
            for (int i = 0; i < entryCount; i++) {
                table.put(((long) state.read16(address)) << 32 | ((long) state.read16(address+2)) << 16 | (long) state.read16(address+4), address);
                address += entryLength;
            }
        }
    }

    void parse(int bufferAddress, int bufferLength, int parseAddress) {
        parse(state, table, wordChars, bufferAddress, bufferLength, parseAddress, false);
    }

    void parse(int userDictionary, int bufferAddress, int bufferLength, int parseAddress, boolean flag) {
        HashMap<Long,Integer> table = new HashMap<Long,Integer>();
        HashSet<Integer> wordChars = new HashSet<Integer>();
        init(state, userDictionary, table, wordChars);
        parse(state, table, wordChars, bufferAddress, bufferLength, parseAddress, flag);
    }

    private static void parse(final State state, HashMap<Long,Integer> table, HashSet<Integer> wordChars, final int bufferAddress, int bufferLength, int parseAddress, boolean flag) {
        ArrayList<Integer> starts = new ArrayList<Integer>();
        ArrayList<Integer> ends = new ArrayList<Integer>();
        for (int i = 0; i < bufferLength; i++) {
            int ch = state.read8(bufferAddress+i);
            if (starts.size() == ends.size()) {
                if (ch == 32) {
                } else if (wordChars.contains(ch)) {
                    starts.add(i);
                    ends.add(i+1);
                } else {
                    starts.add(i);
                }
            } else {
                if (ch == 32) {
                    ends.add(i);
                } else if (wordChars.contains(ch)) {
                    ends.add(i);
                    starts.add(i);
                    ends.add(i+1);
                }
            }
        }
        if (starts.size() > ends.size()) {
            ends.add(bufferLength);
        }
        int count = Math.min(starts.size(), state.read8(parseAddress));
        state.store8(parseAddress+1,count);
        for (int i = 0; i < count; i++) {
            final int start = starts.get(i);
            final int end = ends.get(i);
            long encoded = ZSCII.encode(state, new CharSequence() {
                @Override public int length() {
                    return end - start;
                }
                @Override public char charAt(int index) {
                    return (char) state.read8(bufferAddress+start+index);
                }
                @Override public CharSequence subSequence(int i, int j) {
                    throw new AssertionError();
                }
                @Override public String toString() {
                    StringBuilder sb = new StringBuilder();
                    for (int i = start; i < end; i++) {
                        sb.append((char) state.read8(bufferAddress+i));
                    }
                    return sb.toString();
                }
            });
            Integer entry = table.get(encoded);
            if (entry != null) {
                state.store16(parseAddress+2+4*i, entry);
                state.store8(parseAddress+4+4*i, start);
                state.store8(parseAddress+5+4*i, end);
            } else if (!flag) {
                state.store16(parseAddress+2+4*i, 0);
                state.store8(parseAddress+4+4*i, start);
                state.store8(parseAddress+5+4*i, end);
            }
        }
    }
}
