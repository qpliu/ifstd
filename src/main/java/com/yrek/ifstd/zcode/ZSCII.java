package com.yrek.ifstd.zcode;

import java.io.IOException;
import java.util.Arrays;

class ZSCII {
    private static final byte[] ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ \n0123456789.,!?_#'\"/\\-:()".getBytes();
    private static final byte[] ALPHABET_V1 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789.,!?_#'\"/\\<-:()".getBytes();
    private static final byte[] EXTRA = new byte[] {
        (byte) 0x00, (byte) 0xe4, (byte) 0x00, (byte) 0xf6,
        (byte) 0x00, (byte) 0xfc, (byte) 0x00, (byte) 0xc4,
        (byte) 0x00, (byte) 0xd6, (byte) 0x00, (byte) 0xdc,
        (byte) 0x00, (byte) 0xdf, (byte) 0x00, (byte) 0xbb,
        (byte) 0x00, (byte) 0xab, (byte) 0x00, (byte) 0xeb,
        (byte) 0x00, (byte) 0xef, (byte) 0x00, (byte) 0xff,
        (byte) 0x00, (byte) 0xcb, (byte) 0x00, (byte) 0xcf,
        (byte) 0x00, (byte) 0xe1, (byte) 0x00, (byte) 0xe9,
        (byte) 0x00, (byte) 0xed, (byte) 0x00, (byte) 0xf3,
        (byte) 0x00, (byte) 0xfa, (byte) 0x00, (byte) 0xfd,
        (byte) 0x00, (byte) 0xc1, (byte) 0x00, (byte) 0xc9,
        (byte) 0x00, (byte) 0xcd, (byte) 0x00, (byte) 0xd3,
        (byte) 0x00, (byte) 0xda, (byte) 0x00, (byte) 0xdd,
        (byte) 0x00, (byte) 0xe0, (byte) 0x00, (byte) 0xe8,
        (byte) 0x00, (byte) 0xec, (byte) 0x00, (byte) 0xf2,
        (byte) 0x00, (byte) 0xf9, (byte) 0x00, (byte) 0xc0,
        (byte) 0x00, (byte) 0xc8, (byte) 0x00, (byte) 0xcc,
        (byte) 0x00, (byte) 0xd2, (byte) 0x00, (byte) 0xd9,
        (byte) 0x00, (byte) 0xe2, (byte) 0x00, (byte) 0xea,
        (byte) 0x00, (byte) 0xee, (byte) 0x00, (byte) 0xf4,
        (byte) 0x00, (byte) 0xfb, (byte) 0x00, (byte) 0xc2,
        (byte) 0x00, (byte) 0xca, (byte) 0x00, (byte) 0xce,
        (byte) 0x00, (byte) 0xd4, (byte) 0x00, (byte) 0xdb,
        (byte) 0x00, (byte) 0xe5, (byte) 0x00, (byte) 0xc5,
        (byte) 0x00, (byte) 0xf8, (byte) 0x00, (byte) 0xd8,
        (byte) 0x00, (byte) 0xe3, (byte) 0x00, (byte) 0xf1,
        (byte) 0x00, (byte) 0xf5, (byte) 0x00, (byte) 0xc3,
        (byte) 0x00, (byte) 0xd1, (byte) 0x00, (byte) 0xd5,
        (byte) 0x00, (byte) 0xe6, (byte) 0x00, (byte) 0xc6,
        (byte) 0x00, (byte) 0xe7, (byte) 0x00, (byte) 0xc7,
        (byte) 0x00, (byte) 0xfe, (byte) 0x00, (byte) 0xf0,
        (byte) 0x00, (byte) 0xde, (byte) 0x00, (byte) 0xd0,
        (byte) 0x00, (byte) 0xa3, (byte) 0x01, (byte) 0x53,
        (byte) 0x01, (byte) 0x52, (byte) 0x00, (byte) 0xa1,
        (byte) 0x00, (byte) 0xbf,
    };

    public static void decode(Appendable result, State state, int offset) throws IOException {
        byte[] alphabetTable;
        int alphabetTableOffset = 0;
        switch (state.version) {
        case 1:
            alphabetTable = ALPHABET_V1;
            break;
        case 2: case 3: case 4:
            alphabetTable = ALPHABET;
            break;
        default:
            alphabetTableOffset = state.read16(State.ALPHABET_TABLE);
            if (alphabetTableOffset == 0) {
                alphabetTable = ALPHABET;
            } else {
                alphabetTable = state.ram;
            }
            break;
        }
        int currentAlphabet = 0;
        int[] chunk = new int[3];
        int abbreviation = 0;
        int shiftLock = 0;
        int zsciiState = 0;
        int zsciiCode = 0;
        for (;;) {
            chunk[0] = (state.ram[offset] >> 2) & 31;
            chunk[1] = ((state.ram[offset] << 3) & 31) | ((state.ram[offset+1] >> 5) & 7);
            chunk[2] = state.ram[offset+1] & 31;
            for (int i = 0; i < 3; i++) {
                switch (zsciiState) {
                case 0:
                    break;
                case 1:
                    zsciiCode = chunk[i] << 5;
                    zsciiState = 2;
                    continue;
                case 2:
                    appendZSCII(result, state, zsciiCode | chunk[i]);
                    zsciiState = 0;
                    continue;
                }
                if (abbreviation != 0) {
                    decode(result, state, state.read16(State.ABBREVIATION_TABLE) + 32*(abbreviation - 1) + chunk[i]);
                    abbreviation = 0;
                    continue;
                }
                switch (chunk[i]) {
                case 0:
                    result.append(' ');
                    currentAlphabet = shiftLock;
                    break;
                case 1:
                    switch (state.version) {
                    case 1:
                        result.append('\n');
                        currentAlphabet = shiftLock;
                        break;
                    default:
                        abbreviation = 1;
                        currentAlphabet = shiftLock;
                        break;
                    }
                    break;
                case 2: case 3:
                    switch (state.version) {
                    case 1: case 2:
                        currentAlphabet = (currentAlphabet + chunk[i] - 1) % 3;
                        break;
                    default:
                        abbreviation = chunk[i];
                        currentAlphabet = shiftLock;
                        break;
                    }
                    break;
                case 4: case 5:
                    currentAlphabet = (currentAlphabet + chunk[i] - 3) % 3;
                    switch (state.version) {
                    case 1: case 2:
                        shiftLock = currentAlphabet;
                        break;
                    default:
                        break;
                    }
                    break;
                case 6:
                    if (currentAlphabet == 2) {
                        zsciiState = 1;
                        currentAlphabet = shiftLock;
                        break;
                    }
                    result.append((char) (alphabetTable[alphabetTableOffset + currentAlphabet*26 + chunk[i] - 6] & 255));
                    break;
                default:
                    result.append((char) (alphabetTable[alphabetTableOffset + currentAlphabet*26 + chunk[i] - 6] & 255));
                    break;
                }
            }
            if ((state.ram[offset] & 128) != 0) {
                break;
            }
            offset += 2;
        }
    }

    public static void appendZSCII(Appendable result, State state, int code) throws IOException {
        switch (code) {
        case 0:
            return;
        case 9: case 11:
            result.append(' ');
            return;
        case 13:
            result.append('\n');
            return;
        case 32: case 33: case 34: case 35: case 36: case 37: case 38: case 39:
        case 40: case 41: case 42: case 43: case 44: case 45: case 46: case 47:
        case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55:
        case 56: case 57: case 58: case 59: case 60: case 61: case 62: case 63:
        case 64: case 65: case 66: case 67: case 68: case 69: case 70: case 71:
        case 72: case 73: case 74: case 75: case 76: case 77: case 78: case 79:
        case 80: case 81: case 82: case 83: case 84: case 85: case 86: case 87:
        case 88: case 89: case 90: case 91: case 92: case 93: case 94: case 95:
        case 96: case 97: case 98: case 99:
        case 100: case 101: case 102: case 103: case 104: case 105: case 106:
        case 107: case 108: case 109: case 110: case 111: case 112: case 113:
        case 114: case 115: case 116: case 117: case 118: case 119: case 120:
        case 121: case 122: case 123: case 124: case 125: case 126:
            result.append((char) code);
            return;
        case 155: case 156: case 157: case 158: case 159: case 160: case 161:
        case 162: case 163: case 164: case 165: case 166: case 167: case 168:
        case 169: case 170: case 171: case 172: case 173: case 174: case 175:
        case 176: case 177: case 178: case 179: case 180: case 181: case 182:
        case 183: case 184: case 185: case 186: case 187: case 188: case 189:
        case 190: case 191: case 192: case 193: case 194: case 195: case 196:
        case 197: case 198: case 199: case 200: case 201: case 202: case 203:
        case 204: case 205: case 206: case 207: case 208: case 209: case 210:
        case 211: case 212: case 213: case 214: case 215: case 216: case 217:
        case 218: case 219: case 220: case 221: case 222: case 223:
            byte[] table;
            int tableOffset = 0;
            switch (state.version) {
            case 1: case 2: case 3: case 4:
                table = EXTRA;
                break;
            default:
                int ext = state.read16(State.EXTRA_HEADERS);
                if (ext == 0 || state.read16(ext + State.EXTRA_HEADERS_SIZE*2) < State.EXTRA_HEADERS_UNICODE_TABLE) {
                    table = EXTRA;
                } else {
                    tableOffset = state.read16(ext + State.EXTRA_HEADERS_UNICODE_TABLE*2);
                    if (tableOffset == 0) {
                        table = EXTRA;
                    } else {
                        table = state.ram;
                        if (code - 155 >= (state.ram[tableOffset]&255)) {
                            return;
                        }
                        tableOffset++;
                    }
                }
            }
            result.append((char) (((table[tableOffset + 2*(code - 155)]&255) << 8) | (table[tableOffset + 2*(code - 155) + 1]&255)));
            return;
        default:
            return;
        }
    }

    public long encode(State state, String string) {
        byte[] alphabetTable;
        int alphabetTableOffset = 0;
        switch (state.version) {
        case 1:
            alphabetTable = ALPHABET_V1;
            break;
        case 2: case 3: case 4:
            alphabetTable = ALPHABET;
            break;
        default:
            alphabetTableOffset = state.read16(State.ALPHABET_TABLE);
            if (alphabetTableOffset == 0) {
                alphabetTable = ALPHABET;
            } else {
                alphabetTable = state.ram;
            }
            break;
        }
        string = string.toLowerCase();
        int[] chars = new int[state.version < 4 ? 6 : 9];
        Arrays.fill(chars, 5);
        int shiftLock = 0;
        for (int i = 0, j = 0; i < string.length() && j < chars.length; i++) {
            int ch = (int) string.charAt(i);
            if (ch == ' ') {
                chars[j] = 0;
                j++;
                continue;
            }
            int zchar = -1;
            for (int k = 0; k < 76; k++) {
                if (ch == (alphabetTable[k+alphabetTableOffset]&255)) {
                    zchar = k;
                    break;
                }
            }
            if (zchar == -1) {
                //... try looking up in unicode table
                return 0;
            }
            int shift = zchar/26;
            zchar = zchar%26 + 6;
            if (shiftLock == shift) {
                chars[j] = zchar;
                j++;
                continue;
            }
            if (state.version >= 3) {
                chars[j] = shift + 1;
                j++;
                if (j >= chars.length) {
                    break;
                }
                chars[j] = zchar;
                j++;
                continue;
            } else {
                throw new RuntimeException("not implemented");
            }
        }
        long result = 0;
        for (int i = 0; i < chars.length; i += 3) {
            result <<= 8;
            result |= ((chars[i]&31) << 10) | ((chars[i+1]&31) << 5) | (chars[i+2]&31);
        }
        return result | 128;
    }
}
