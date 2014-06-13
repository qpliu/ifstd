package com.yrek.ifstd.glulx;

import com.yrek.ifstd.glk.GlkByteArray;
import com.yrek.ifstd.glk.GlkIntArray;
import com.yrek.ifstd.glk.GlkDispatchArgument;
import com.yrek.ifstd.glk.GlkStreamResult;

class GlkArgument implements GlkDispatchArgument, GlkByteArray, GlkIntArray {
    final Machine machine;
    final int value;
    private int readArrayIndex = 0;
    private int writeArrayIndex = 0;
    private int arrayLength = 0;
    private int arrayOffset = 0;

    GlkArgument(Machine machine, int value) {
        this.machine = machine;
        this.value = value;
    }

    @Override
    public int getInt() {
        return value;
    }

    @Override
    public void setInt(int element) {
        if (value != 0) {
            setIntElement(element);
        }
    }

    @Override
    public GlkByteArray getByteArray() {
        return value == 0 ? null : this;
    }

    @Override
    public GlkByteArray getString() {
        if (value == 0) {
            return null;
        } else if (value == -1) {
            throw new IllegalArgumentException("String from stack");
        } else if (machine.state.load8(value) != 0xe0) {
            throw new IllegalArgumentException("Not a string object");
        }
        arrayOffset = 1;
        return this;
    }

    @Override
    public GlkIntArray getIntArray() {
        return value == 0 ? null : this;
    }

    @Override
    public GlkIntArray getStringUnicode() {
        if (value == 0) {
            return null;
        } else if (value == -1) {
            throw new IllegalArgumentException("String from stack");
        } else if (machine.state.load32(value) != 0xe2000000) {
            throw new IllegalArgumentException("Not a unicode string object");
        }
        arrayOffset = 4;
        return this;
    }

    @Override
    public Runnable getRunnable() {
        return new Runnable() {
            @Override public void run() {
                int fp = machine.state.fp;
                Instruction.pushCallStub(machine.state, 0, 0);
                Instruction.call(machine.state, value, new int[0]);
                while (machine.state.fp > fp) {
                    switch (Instruction.executeNext(machine)) {
                    case Quit:
                        throw new IllegalArgumentException("Illegal quit");
                    default:
                        break;
                    }
                }
            }
        };
    }

    @Override
    public int getByteElement() {
        int result;
        if (value == -1) {
            result = machine.state.pop32() & 255;
        } else {
            result = machine.state.load8(value + arrayOffset + readArrayIndex);
        }
        readArrayIndex++;
        return result;
    }

    @Override
    public void setByteElement(int element) {
        if (value == -1) {
            machine.state.push32(element & 255);
        } else {
            machine.state.store8(value + arrayOffset + writeArrayIndex, element);
        }
        writeArrayIndex++;
    }

    @Override
    public int getByteElementAt(int index) {
        int result;
        if (value == -1) {
            throw new IllegalArgumentException("Random access of stack");
        }
        return machine.state.load8(value + arrayOffset + index);
    }

    @Override
    public void setByteElementAt(int index, int element) {
        if (value == -1) {
            throw new IllegalArgumentException("Random access of stack");
        }
        machine.state.store8(value + arrayOffset + index, element);
    }

    @Override
    public int getIntElement() {
        int result;
        if (value == -1) {
            result = machine.state.pop32();
        } else {
            result = machine.state.load32(value + arrayOffset + 4*readArrayIndex);
        }
        readArrayIndex++;
        return result;
    }

    @Override
    public void setIntElement(int element) {
        if (value == -1) {
            machine.state.push32(element);
        } else {
            machine.state.store32(value + arrayOffset + 4*writeArrayIndex, element);
        }
        writeArrayIndex++;
    }

    @Override
    public int getIntElementAt(int index) {
        int result;
        if (value == -1) {
            throw new IllegalArgumentException("Random access of stack");
        }
        return machine.state.load32(value + arrayOffset + 4*index);
    }

    @Override
    public void setIntElementAt(int index, int element) {
        if (value == -1) {
            throw new IllegalArgumentException("Random access of stack");
        }
        machine.state.store32(value + arrayOffset + 4*index, element);
    }

    @Override
    public int getReadArrayIndex() {
        return readArrayIndex;
    }

    @Override
    public int setReadArrayIndex(int index) {
        if (value != -1) {
            readArrayIndex = index;
        }
        return readArrayIndex;
    }

    @Override
    public int getWriteArrayIndex() {
        return writeArrayIndex;
    }

    @Override
    public int setWriteArrayIndex(int index) {
        if (value != -1) {
            writeArrayIndex = index;
        }
        return writeArrayIndex;
    }

    @Override
    public int getArrayLength() {
        return arrayLength;
    }

    @Override
    public void setArrayLength(int arrayLength) {
        this.arrayLength = arrayLength;
    }
}
