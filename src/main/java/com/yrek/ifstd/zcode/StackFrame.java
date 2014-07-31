package com.yrek.ifstd.zcode;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    private StackFrame(StackFrame parent, InputStream in) throws IOException {
        this.parent = parent;
        this.index = parent == null ? 0 : parent.index + 1;
        this.returnAddress = read24(in);
        int flags = read8(in);
        this.locals = new int[flags & 15];
        int result = read8(in);
        if ((flags & 16) != 0) {
            result = -1;
        }
        this.result = result;
        this.args = read8(in);
        this.sp = read16(in);
        for (int i = 0; i < locals.length; i++) {
            locals[i] = read16(in);
        }
        if (sp > 0) {
            stack = new int[sp];
            for (int i = 0; i < sp; i++) {
                stack[i] = read16(in);
            }
        }
    }

    static StackFrame read(InputStream in, int size) throws IOException {
        StackFrame frame = null;
        while (size > 0) {
            frame = new StackFrame(frame, in);
            size -= frame.saveSize();
        }
        return frame;
    }

    private int read24(InputStream in) throws IOException {
        int b = in.read();
        if (b < 0) {
            throw new IOException();
        }
        int result = b << 16;
        b = in.read();
        if (b < 0) {
            throw new IOException();
        }
        result |= b << 8;
        b = in.read();
        if (b < 0) {
            throw new IOException();
        }
        return result | b;
    }

    private int read16(InputStream in) throws IOException {
        int b = in.read();
        if (b < 0) {
            throw new IOException();
        }
        int result = b << 8;
        b = in.read();
        if (b < 0) {
            throw new IOException();
        }
        return result | b;
    }

    private int read8(InputStream in) throws IOException {
        int b = in.read();
        if (b < 0) {
            throw new IOException();
        }
        return b;
    }

    private int saveSize() {
        return 8 + 2*locals.length + 2*sp;
    }

    private int totalSaveSize() {
        if (parent != null) {
            return saveSize() + parent.totalSaveSize();
        }
        return saveSize();
    }

    void save(OutputStream out) throws IOException {
        out.write(0x53); out.write(0x74); out.write(0x6b); out.write(0x73); // Stks
        int size = totalSaveSize();
        out.write(size >> 24);
        out.write(size >> 16);
        out.write(size >> 8);
        out.write(size);
        saveFrames(out);
    }

    private void saveFrames(OutputStream out) throws IOException {
        if (parent != null) {
            parent.saveFrames(out);
        }
        out.write(returnAddress >> 16);
        out.write(returnAddress >> 8);
        out.write(returnAddress);
        out.write((result < 0 ? 16 : 0) | locals.length);
        out.write(result < 0 ? 0 : result);
        out.write(args);
        out.write(sp >> 8);
        out.write(sp);
        for (int i = 0; i < locals.length; i++) {
            out.write(locals[i] >> 8);
            out.write(locals[i]);
        }
        for (int i = 0; i < sp; i++) {
            out.write(stack[i] >> 8);
            out.write(stack[i]);
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
}
