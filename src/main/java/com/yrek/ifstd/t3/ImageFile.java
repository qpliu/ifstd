package com.yrek.ifstd.t3;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

class ImageFile {
    private final RandomAccessFile file;
    private final long startPosition;
    final int version;
    final String timestamp;
    ArrayList<DataBlockStaticInitializer> staticInitializers = null;
    DataBlockEntrypoint entrypoint = null;
    DataBlockConstantPoolDefinition codePoolDef = null;
    DataBlockConstantPoolDefinition constantPoolDef = null;
    DataBlockMetaclassDependency metaclassDependency = null;
    DataBlockFunctionSetDependency functionSetDependency = null;
    DataBlockMultimediaResource multimediaResource = null;

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
        while (nextDataBlock() != null) {
        }
        if (entrypoint == null) {
            throw new IOException("ENTP block missing");
        }
        if (codePoolDef == null) {
            throw new IOException("Byte-code pool missing");
        }
        if (constantPoolDef == null) {
            throw new IOException("Constant pool missing");
        }
        if (metaclassDependency == null) {
            throw new IOException("MCLD block missing");
        }
        if (functionSetDependency == null) {
            throw new IOException("FNSD block missing");
        }
    }

    private DataBlock nextDataBlock() throws IOException {
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
                return new DataBlockStaticObjects(size, flags);
            case 0x43504446: // CPDF
                return new DataBlockConstantPoolDefinition(size, flags);
            case 0x43505047: // CPPG
                return new DataBlockConstantPoolPage(size, flags);
            case 0x4d524553: // MRES
                if (multimediaResource == null) {
                    multimediaResource = new DataBlockMultimediaResource(size, flags);
                }
                multimediaResource.readBlock(size);
                return multimediaResource;
            case 0x4d52454c: // MREL
                throw new RuntimeException("unimplemented");
            case 0x4d434c44: // MCLD
                return new DataBlockMetaclassDependency(size, flags);
            case 0x464e5244: // FNSD
                return new DataBlockFunctionSetDependency(size, flags);
            case 0x53594d44: // SYMD
                return new DataBlockSymbolicNames(size, flags);
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

    private int readSint2() throws IOException {
        int i = file.readUnsignedByte();
        return i | (file.readByte() << 8);
    }

    private int readUint2() throws IOException {
        int i = file.readUnsignedByte();
        return i | (file.readUnsignedByte() << 8);
    }

    private int readInt4() throws IOException {
        int i = file.readUnsignedByte();
        i |= file.readUnsignedByte() << 8;
        i |= file.readUnsignedByte() << 16;
        return i | (file.readUnsignedByte() << 24);
    }

    private byte[] asciiBuffer = new byte[256];
    private String readAscii() throws IOException {
        int length = file.readUnsignedByte();
        file.readFully(asciiBuffer, 0, length);
        return new String(asciiBuffer, 0, length, "US-ASCII");
    }
    private String readAscii(int xor) throws IOException {
        int length = file.readUnsignedByte();
        file.readFully(asciiBuffer, 0, length);
        for (int i = 0; i < length; i++) {
            asciiBuffer[i] ^= xor;
        }
        return new String(asciiBuffer, 0, length, "US-ASCII");
    }

    private T3Value readDataHolder() throws IOException {
        int typeID = file.readUnsignedByte();
        int value = readInt4();
        switch (typeID) {
        case 1:
            return T3Value.NIL;
        case 2:
            return T3Value.TRUE;
        case 5:
        case 6:
            throw new RuntimeException("unimplemented");
        case 7:
            switch (value) {
            case 0:
                return T3Value.INT0;
            case 1:
                return T3Value.INT1;
            default:
                return new T3ValueInt(value);
            }
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
            throw new RuntimeException("unimplemented");
        case 13:
            return null;
        case 15:
        case 16:
            throw new RuntimeException("unimplemented");
        default:
            throw new IOException("Unrecognized typeID="+typeID);
        }
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

    class DataBlockStaticObjects extends DataBlock {
        final boolean transientObjects;
        final StaticObject[] objects;

        DataBlockStaticObjects(int size, int flags) throws IOException {
            super(size, flags);
            int count = readUint2();
            int index = readUint2();
            int objsFlags = readUint2();
            boolean largeObjs = (objsFlags & 1) != 0;
            this.transientObjects = (objsFlags & 2) != 0;
            this.objects = new StaticObject[count];
            for (int i = 0; i < count; i++) {
                objects[i] = new StaticObject(largeObjs);
            }
            metaclassDependency.entries[index].staticObjects.add(this);
        }

        class StaticObject {
            final int objectID;
            final byte[] data;

            StaticObject(boolean largeObjs) throws IOException {
                this.objectID = readInt4();
                this.data = new byte[largeObjs ? readInt4() : readUint2()];
                file.readFully(data, 0, data.length);
            }
        }
    }

    class DataBlockConstantPoolDefinition extends DataBlock {
        final int pageCount;
        final int pageSize;
        final DataBlockConstantPoolPage[] pages;

        DataBlockConstantPoolDefinition(int size, int flags) throws IOException {
            super(size, flags);
            int identifier = readUint2();
            this.pageCount = readInt4();
            this.pageSize = readInt4();
            this.pages = new DataBlockConstantPoolPage[this.pageCount];
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

    class DataBlockConstantPoolPage extends DataBlock {
        final int index;
        final byte[] bytes;

        DataBlockConstantPoolPage(int size, int flags) throws IOException {
            super(size, flags);
            int identifier = readUint2();
            this.index = readInt4();
            int mask = file.readUnsignedByte();
            this.bytes = new byte[size - 5];
            file.readFully(this.bytes);
            if (mask != 0) {
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] ^= (byte) mask;
                }
            }
            switch (identifier) {
            case 1:
                if (codePoolDef.pages[index] != null) {
                    throw new IOException("Duplicate code pool page:"+index);
                }
                codePoolDef.pages[index] = this;
                break;
            case 2:
                if (constantPoolDef.pages[index] != null) {
                    throw new IOException("Duplicate constant pool page:"+index);
                }
                constantPoolDef.pages[index] = this;
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
            final ArrayList<DataBlockStaticObjects> staticObjects;

            Entry() throws IOException {
                file.readShort();
                this.name = readAscii();
                int count = readUint2();
                file.readShort();
                this.propertyIDs = new int[count];
                for (int i = 0; i < count; i++) {
                    this.propertyIDs[i] = readUint2();
                }
                this.staticObjects = new ArrayList<DataBlockStaticObjects>();
            }
        }
    }

    class DataBlockFunctionSetDependency extends DataBlock {
        final String[] entries;

        DataBlockFunctionSetDependency(int size, int flags) throws IOException {
            super(size, flags);
            int count = readUint2();
            this.entries = new String[count];
            for (int i = 0; i < count; i++) {
                this.entries[i] = readAscii();
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

            if (staticInitializers == null) {
                staticInitializers = new ArrayList<DataBlockStaticInitializer>();
            }
            staticInitializers.add(this);
        }
    }

    class DataBlockSymbolicNames extends DataBlock {
        final HashMap<String,T3Value> entries = new HashMap<String,T3Value>();

        DataBlockSymbolicNames(int size, int flags) throws IOException {
            super(size, flags);
            int count = readUint2();
            for (int i = 0; i < count; i++) {
                T3Value value = readDataHolder();
                entries.put(readAscii(), value);
            }
        }
    }

    class DataBlockMultimediaResource extends DataBlock {
        final HashMap<String,Entry> entries;

        DataBlockMultimediaResource(int size, int flags) throws IOException {
            super(size, flags);
            this.entries = new HashMap<String,Entry>();
        }

        void readBlock(int size) throws IOException {
            long blockStart = file.getFilePointer();
            int count = readUint2();
            for (int i = 0; i < count; i++) {
                int offset = readInt4();
                int entrySize = readInt4();
                entries.put(readAscii(255), new Entry(offset + blockStart, entrySize));
            }
            file.seek(blockStart + size);
        }

        class Entry {
            final long offset;
            final int size;

            Entry(long offset, int size) {
                this.offset = offset;
                this.size = size;
            }
        }
    }
}
