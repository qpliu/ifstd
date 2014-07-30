package com.yrek.ifstd.zcode;

import java.io.Serializable;

class StackFrame implements Serializable {
    private static final long serialVersionUID = 0L;

    StackFrame parent;
    int index;
    int returnAddress;
    int result;
    int[] locals;
    int[] stack;
    int sp;

    StackFrame(StackFrame parent, int returnAddress, int result, int locals) {
        this.parent = parent;
        this.index = parent == null ? 0 : parent.index + 1;
        this.returnAddress = returnAddress;
        this.result = result;
        this.locals = new int[locals];
        this.stack = null;
        this.sp = 0;
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
