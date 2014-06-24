package com.yrek.ifstd.test.glk;

import java.io.IOException;

import com.yrek.ifstd.glk.GlkByteArray;
import com.yrek.ifstd.glk.GlkIntArray;
import com.yrek.ifstd.glk.GlkStream;
import com.yrek.ifstd.glk.GlkStreamResult;
import com.yrek.ifstd.glk.GlkWindowStream;
import com.yrek.ifstd.glk.UnicodeString;

public class TestGlkStream extends GlkWindowStream {
    final TestGlk glk;
    final String name;
    private int style = 0;
    int inputCount = 0;
    int outputCount = 0;

    public TestGlkStream(TestGlk glk, String name, TestGlkWindow window, int rock) {
        super(window, rock);
        this.glk = glk;
        this.name = name;
    }

    @Override
    public void putChar(int ch) throws IOException {
        super.putChar(ch);
        outputCount++;
        output(escapeChar((char) ch));
        if (glk.output != null) {
            glk.output.append((char) ch);
        }
    }

    @Override
    public void putString(CharSequence string) throws IOException {
        super.putString(string);
        int length = string.length();
        outputCount += length;
        output(escapeString(string, length));
        if (glk.output != null) {
            glk.output.append(string);
        }
    }

    @Override
    public void putBuffer(GlkByteArray buffer) throws IOException {
        super.putBuffer(buffer);
        int length = buffer.getArrayLength();
        outputCount += length;
        output(escapeString(buffer, length));
        if (glk.output != null) {
            for (int i = 0; i < buffer.getArrayLength(); i++) {
                glk.output.append((char) (buffer.getByteElementAt(i) & 255));
            }
        }
    }

    @Override
    public void putCharUni(int ch) throws IOException {
        super.putCharUni(ch);
        outputCount++;
        String str = new String(Character.toChars(ch));
        output(escapeString(str, str.length()));
        if (glk.output != null) {
            glk.output.append(str);
        }
    }

    @Override
    public void putStringUni(UnicodeString string) throws IOException {
        super.putStringUni(string);
        int length = string.codePointCount();
        outputCount += length;
        output(escapeString(string, string.length()));
        if (glk.output != null) {
            glk.output.append(string);
        }
    }

    @Override
    public void putBufferUni(GlkIntArray buffer) throws IOException {
        super.putBufferUni(buffer);
        throw new RuntimeException("unimplemented");
    }

    @Override
    public void setStyle(int style) {
        super.setStyle(style);
        this.style = style;
    }

    @Override
    public int getChar() throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getLine(GlkByteArray buffer) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getBuffer(GlkByteArray buffer) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getCharUni() throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getLineUni(GlkIntArray buffer) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getBufferUni(GlkIntArray buffer) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public void setPosition(int position, int seekMode) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getPosition() throws IOException {
        throw new RuntimeException("unimplemented");
    }

    private void output(String value) {
        glk.outputQueue.add("<out s=\""+name+"\" style=\""+style+"\" v=\""+value+"\"/>");
    }

    private String escapeCh(int ch) {
        switch (ch) {
        case 0: return "&#00;";
        case 60: return "&lt;";
        case 62: return "&gt;";
        case 38: return "&amp;";
        case 34: return "&quot;";
        case 39: return "&apos;";
        default: return null;
        }
    }

    private String escapeChar(int ch) {
        String str = escapeCh(ch);
        if (str == null) {
            return String.valueOf((char) ch);
        } else {
            return str;
        }
    }

    private int stringLength(GlkByteArray buffer) {
        for (int i = 0;; i++) {
            if (buffer.getByteElementAt(i) == 0) {
                return i;
            }
        }
    }

    private String escapeString(GlkByteArray buffer, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int ch = buffer.getByteElementAt(i) & 255;
            String str = escapeChar(ch);
            if (str == null) {
                sb.append((char) ch);
            } else {
                sb.append(str);
            }
        }
        return sb.toString();
    }

    private String escapeString(CharSequence string, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int ch = (int) string.charAt(i);
            String str = escapeChar(ch);
            if (str == null) {
                sb.append((char) ch);
            } else {
                sb.append(str);
            }
        }
        return sb.toString();
    }
}
