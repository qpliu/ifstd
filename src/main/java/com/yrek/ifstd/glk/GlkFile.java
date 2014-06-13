package com.yrek.ifstd.glk;

public abstract class GlkFile extends GlkObject {
    public static final int UsageData = 0x00;
    public static final int UsageSavedGame = 0x01;
    public static final int UsageTranscript = 0x02;
    public static final int UsageInputRecord = 0x03;
    public static final int UsageTypeMask = 0x0f;

    public static final int UsageTextMode = 0x100;
    public static final int UsageBinaryMode = 0x000;

    public static final int ModeWrite = 0x01;
    public static final int ModeRead = 0x02;
    public static final int ModeReadWrite = 0x03;
    public static final int ModeWriteAppend = 0x05;

    public GlkFile(int rock) {
        super(rock);
    }

    public abstract void delete();

    public abstract boolean exists();
}
