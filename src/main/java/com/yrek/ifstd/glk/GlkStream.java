package com.yrek.ifstd.glk;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class GlkStream extends GlkObject {
    public static final int SeekModeStart = 0;
    public static final int SeekModeCurrent = 1;
    public static final int SeekModeEnd = 2;

    public static final int StyleNormal = 0;
    public static final int StyleEmphasized = 1;
    public static final int StylePreformatted = 2;
    public static final int StyleHeader = 3;
    public static final int StyleSubheader = 4;
    public static final int StyleAlert = 5;
    public static final int StyleNote = 6;
    public static final int StyleBlockQuote = 7;
    public static final int StyleInput = 8;
    public static final int StyleUser1 = 9;
    public static final int StyleUser2 = 10;

    public static final int StyleHintIndentation = 0;
    public static final int StyleHintParaIndentation = 1;
    public static final int StyleHintJustification = 2;
    public static final int StyleHintSize = 3;
    public static final int StyleHintWeight = 4;
    public static final int StyleHintOblique = 5;
    public static final int StyleHintProportional = 6;
    public static final int StyleHintTextColor = 7;
    public static final int StyleHintBackColor = 8;
    public static final int StyleHintReverseColor = 9;

    public static final int StyleHintJustLeftFlush = 0;
    public static final int StyleHintJustLeftRight = 1;
    public static final int StyleHintJustCentered = 2;
    public static final int StyleHintJustRightFlush = 3;

    protected GlkStream(int rock) {
        super(rock);
    }

    public abstract GlkStreamResult close() throws IOException;
    public abstract void putChar(int ch) throws IOException;
    public abstract void putString(CharSequence string) throws IOException;
    public abstract void putBuffer(GlkByteArray buffer) throws IOException;
    public abstract void putCharUni(int ch) throws IOException;
    public abstract void putStringUni(UnicodeString string) throws IOException;
    public abstract void putBufferUni(GlkIntArray buffer) throws IOException;
    public abstract void setStyle(int style);
    public abstract int getChar() throws IOException;
    public abstract int getLine(GlkByteArray buffer) throws IOException;
    public abstract int getBuffer(GlkByteArray buffer) throws IOException;
    public abstract int getCharUni() throws IOException;
    public abstract int getLineUni(GlkIntArray buffer) throws IOException;
    public abstract int getBufferUni(GlkIntArray buffer) throws IOException;
    public abstract void setPosition(int position, int seekMode) throws IOException;
    public abstract int getPosition() throws IOException;

    public DataOutput getDataOutput() {
        throw new RuntimeException("unimplemented");
    }

    public DataInput getDataInput() {
        throw new RuntimeException("unimplemented");
    }
}
