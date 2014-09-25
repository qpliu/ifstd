package com.yrek.ifstd.t3;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

class ImageFile {
    private final RandomAccessFile file;
    private final long startPosition;
    final int version;
    final String timestamp;

    ImageFile(File file) throws IOException {
        this(new RandomAccessFile(file, "r"));
    }

    ImageFile(RandomAccessFile file) throws IOException {
        this.file = file;
        this.startPosition = this.file.getFilePointer();

        if (file.readLong() != 0x54332d696d616765L) {
            throw new IOException("bad magic: T3-image");
        }
        if (file.readShort() != 0x0d0a) {
            throw new IOException("bad magic: CRLF");
        }
        if (file.read() != 0x1a) {
            throw new IOException("bad magic: 0x1a");
        }
        this.version = file.read();
        if (version != 1 && version != 2) {
            throw new IOException("unknown version: " + version);
        }
        file.seek(startPosition + 45);
        byte[] buffer = new byte[24];
        file.readFully(buffer);
        this.timestamp = new String(buffer, "US-ASCII");
    }

    DataBlock nextDataBlock() throws IOException {
        for (;;) {
            int id = file.readInt();
            int size = file.readInt();
            size += file.readInt() << 8;
            size += file.readInt() << 16;
            size += file.readInt() << 24;
            int flags = file.readShort();
            switch (id) {
            case 0x454f4620: // EOF
                return null;
            case 0x454e5450: // ENTP
            case 0x4f424a53: // OBJS
            case 0x43504446: // CPDF
            case 0x43505047: // CPPG
            case 0x4d524553: // MRES
            case 0x4d52454c: // MREL
            case 0x4d434c44: // MCLD
            case 0x464e5244: // FNSD
            case 0x53594d44: // SYMD
            case 0x53494e49: // SINI
                throw new RuntimeException("unimplemented");
            default:
                if ((flags & 1) != 0) {
                    throw new IOException(String.format("unknown block type: %08x", id));
                }
                file.seek(file.getFilePointer() + size);
            }
        }
    }

    class DataBlock {
    }
}
