package com.yrek.ifstd.glk;

public abstract class GlkFile extends GlkObject {
    public static final int usageData = 0x00;
    public static final int usageSavedGame = 0x01;
    public static final int usageTranscript = 0x02;
    public static final int usageInputRecord = 0x03;
    public static final int usageTypeMask = 0x0f;

    public static final int usageTextMode = 0x100;
    public static final int usageBinaryMode = 0x000;

    public static final int modeWrite = 0x01;
    public static final int modeRead = 0x02;
    public static final int modeReadWrite = 0x03;
    public static final int modeWriteAppend = 0x05;

    public GlkFile(int rock) {
        super(rock);
    }

    public abstract void delete();

    public abstract boolean exists();
}
