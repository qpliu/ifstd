package com.yrek.ifstd.zcode;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

class StackFrame implements Serializable {
    private static final long serialVersionUID = 0L;

    StackFrame parent;
    int index;
    int args;
    int returnAddress;
    int result;
    int[] locals;
    int[] stack;
    int sp;

    StackFrame(StackFrame parent, int returnAddress, int result, int args, int locals) {
        this.parent = parent;
        this.index = parent == null ? 0 : parent.index + 1;
        this.args = args;
        this.returnAddress = returnAddress;
        this.result = result;
        this.locals = new int[locals&15];
        this.stack = null;
        this.sp = 0;
    }

    StackFrame(StackFrame original) {
        if (original.parent == null) {
            this.parent = null;
            this.index = 0;
        } else {
            this.parent = new StackFrame(original.parent);
            this.index = this.parent.index + 1;
        }
        this.args = original.args;
        this.returnAddress = original.returnAddress;
        this.result = original.result;
        this.locals = new int[original.locals.length];
        System.arraycopy(original.locals, 0, locals, 0, locals.length);
        if (original.stack == null) {
            this.stack = null;
        } else {
            this.stack = new int[original.stack.length];
            System.arraycopy(original.stack, 0, stack, 0, stack.length);
        }
        this.sp = original.sp;
    }

    private StackFrame(StackFrame parent, DataInput in) throws IOException {
        this.parent = parent;
        this.index = parent == null ? 0 : parent.index + 1;
        this.returnAddress = (in.readByte() & 255) << 16;
        this.returnAddress |= in.readShort() & 65535;
        int flags = in.readByte()&255;
        this.locals = new int[flags & 15];
        int result = in.readByte()&255;
        if ((flags & 16) != 0) {
            result = -1;
        }
        this.result = result;
        this.args = in.readByte()&255;
        this.sp = in.readShort()&65535;
        for (int i = 0; i < locals.length; i++) {
            locals[i] = in.readShort()&65535;
        }
        if (sp > 0) {
            stack = new int[sp];
            for (int i = 0; i < sp; i++) {
                stack[i] = in.readShort()&65535;
            }
        }
    }

    static StackFrame read(DataInput in, int size) throws IOException {
        StackFrame frame = null;
        while (size > 0) {
            frame = new StackFrame(frame, in);
            size -= frame.saveSize();
        }
        return frame;
    }

    private int saveSize() {
        return 8 + 2*locals.length + 2*sp;
    }

    int totalSaveSize() {
        if (parent != null) {
            return saveSize() + parent.totalSaveSize();
        }
        return saveSize();
    }

    void save(DataOutput out) throws IOException {
        out.writeInt(0x53746b73); // Stks
        out.writeInt(totalSaveSize());
        saveFrames(out);
    }

    private void saveFrames(DataOutput out) throws IOException {
        if (parent != null) {
            parent.saveFrames(out);
        }
        out.write(returnAddress >> 16);
        out.writeShort(returnAddress);
        out.write((result < 0 ? 16 : 0) | locals.length);
        out.write(result < 0 ? 0 : result);
        out.write(args);
        out.writeShort(sp);
        for (int i = 0; i < locals.length; i++) {
            out.writeShort(locals[i]);
        }
        for (int i = 0; i < sp; i++) {
            out.writeShort(stack[i]);
        }
    }

    void push(int val) {
        if (stack == null) {
            stack = new int[32];
            sp = 0;
        } else if (sp >= stack.length) {
            int[] newStack = new int[stack.length*2];
            System.arraycopy(stack, 0, newStack, 0, stack.length);
            stack = newStack;
        }
        stack[sp] = val;
        sp++;
    }

    int pop() {
        if (sp <= 0 || stack == null) {
            return 0;
        }
        sp--;
        return stack[sp];
    }

    int peek() {
        if (sp <= 0 || stack == null) {
            return 0;
        }
        return stack[sp-1];
    }
}
