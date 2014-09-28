package com.yrek.ifstd.t3;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

class ImageFile {
    private final RandomAccessFile file;
    private final long startPosition;
    final int version;
    final String timestamp;
    DataBlockEntrypoint entrypoint = null;
    DataBlockConstantPoolDefinition codePoolDef = null;
    DataBlockConstantPoolDefinition constantPoolDef = null;
    DataBlockMetaclassDependency metaclassDependency = null;
    DataBlockFunctionSetDependency functionSetDependency = null;

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
            int size = readInt4();
            int flags = readUint2();
            switch (id) {
            case 0x454f4620: // EOF
                return null;
            case 0x454e5450: // ENTP
                return new DataBlockEntrypoint(size, flags);
            case 0x4f424a53: // OBJS
                throw new RuntimeException("unimplemented");
            case 0x43504446: // CPDF
                return new DataBlockConstantPoolDefinition(size, flags);
            case 0x43505047: // CPPG
            case 0x4d524553: // MRES
            case 0x4d52454c: // MREL
                throw new RuntimeException("unimplemented");
            case 0x4d434c44: // MCLD
                return new DataBlockMetaclassDependency(size, flags);
            case 0x464e5244: // FNSD
                return new DataBlockFunctionSetDependency(size, flags);
            case 0x53594d44: // SYMD
                throw new RuntimeException("unimplemented");
            case 0x53494e49: // SINI
                return new DataBlockStaticInitializer(size, flags);
            default:
                if ((flags & 1) != 0) {
                    throw new IOException(String.format("unknown block type: %08x", id));
                }
                file.seek(file.getFilePointer() + size);
            }
        }
    }

    int readSint2() throws IOException {
        int i = file.readUnsignedByte();
        return i | (file.readByte() << 8);
    }

    int readUint2() throws IOException {
        int i = file.readUnsignedByte();
        return i | (file.readUnsignedByte() << 8);
    }

    int readInt4() throws IOException {
        int i = file.readUnsignedByte();
        i |= file.readUnsignedByte() << 8;
        i |= file.readUnsignedByte() << 16;
        return i | (file.readUnsignedByte() << 24);
    }

    class DataBlock {
        final int size;
        final int flags;
        final long blockStart;

        DataBlock(int size, int flags) throws IOException {
            this.size = size;
            this.flags = flags;
            this.blockStart = file.getFilePointer();
        }

        void seekToEnd() throws IOException {
            file.seek(blockStart + size);
        }
    }

    class DataBlockEntrypoint extends DataBlock {
        final int codePoolOffset;
        final int exceptionTableSize;

        DataBlockEntrypoint(int size, int flags) throws IOException {
            super(size, flags);
            this.codePoolOffset = readInt4();
            this.exceptionTableSize = readUint2();
            seekToEnd();
            if (entrypoint != null) {
                throw new IOException("multiple ENTP blocks");
            }
            entrypoint = this;
        }
    }

    class DataBlockConstantPoolDefinition extends DataBlock {
        final int pageCount;
        final int pageSize;

        DataBlockConstantPoolDefinition(int size, int flags) throws IOException {
            super(size, flags);
            int identifier = readUint2();
            this.pageCount = readInt4();
            this.pageSize = readInt4();
            seekToEnd();
            switch (identifier) {
            case 1:
                if (codePoolDef != null) {
                    throw new IOException("Multiple CPDF byte code pool blocks");
                }
                codePoolDef = this;
                break;
            case 2:
                if (constantPoolDef != null) {
                    throw new IOException("Multiple CPDF constant pool blocks");
                }
                constantPoolDef = this;
                break;
            default:
                break;
            }
        }
    }

    class DataBlockMetaclassDependency extends DataBlock {
        final Entry[] entries;

        DataBlockMetaclassDependency(int size, int flags) throws IOException {
            super(size, flags);
            int count = readUint2();
            this.entries = new Entry[count];
            for (int i = 0; i < count; i++) {
                this.entries[i] = new Entry();
            }
            if (metaclassDependency != null) {
                throw new IOException("Multiple MCLD blocks");
            }
            metaclassDependency = this;
        }

        class Entry {
            final String name;
            final int[] propertyIDs;

            Entry() throws IOException {
                file.readShort();
                byte[] name = new byte[file.readUnsignedByte()];
                file.readFully(name);
                this.name = new String(name, "US-ASCII");
                int count = readUint2();
                file.readShort();
                this.propertyIDs = new int[count];
                for (int i = 0; i < count; i++) {
                    this.propertyIDs[i] = readUint2();
                }
            }
        }
    }

    class DataBlockFunctionSetDependency extends DataBlock {
        final String[] entries;

        DataBlockFunctionSetDependency(int size, int flags) throws IOException {
            super(size, flags);
            int count = readUint2();
            this.entries = new String[count];
            byte[] buffer = new byte[256];
            for (int i = 0; i < count; i++) {
                int length = file.readUnsignedByte();
                file.readFully(buffer, 0, length);
                this.entries[i] = new String(buffer, 0, length, "US-ASCII");
            }
            if (functionSetDependency != null) {
                throw new IOException("Multiple FNSD blocks");
            }
            functionSetDependency = this;
        }
    }

    class DataBlockStaticInitializer extends DataBlock {
        final long[] objectPropertyIDs; // upper 32 bits is object ID, lower 16 bits is property ID
        final int offset;

        DataBlockStaticInitializer(int size, int flags) throws IOException {
            super(size, flags);
            int headerSize = readInt4();
            this.offset = readInt4();
            int count = readInt4();
            file.seek(blockStart + headerSize);
            this.objectPropertyIDs = new long[count];
            for (int i = 0; i < count; i++) {
                this.objectPropertyIDs[i] = ((long) readInt4()) << 32;
                this.objectPropertyIDs[i] |= readUint2();
            }
        }
    }
}
