package com.yrek.ifstd.glulx;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

class State {
    int pc;
    int sp;
    int fp;
    byte[] memory;
    byte[] stack;

    void readFile(InputStream in, int protectStart, int protectLength) throws IOException {
    }

    void readSave(InputStream in, int protectStart, int protectLength) throws IOException {
    }

    void writeSave(OutputStream out) throws IOException {
    }

    State copyTo(State saveState) {
        if (saveState == null) {
            saveState = new State();
        }
        saveState.copyFrom(this, 0, 0);
        return saveState;
    }

    void copyFrom(State saveState, int protectStart, int protectLength) {
        if (memory == null) {
            memory = Arrays.copyOf(saveState.memory, saveState.memory.length);
        } else if (memory.length != saveState.memory.length) {
            byte[] oldMemory = memory;
            memory = Arrays.copyOf(saveState.memory, saveState.memory.length);
            System.arraycopy(oldMemory, protectStart, memory, protectStart, protectLength);
        } else {
            System.arraycopy(saveState.memory, 0, memory, 0, protectStart);
            System.arraycopy(saveState.memory, protectStart + protectLength, memory, protectStart + protectLength, memory.length - protectStart - protectLength);
        }
    }

    int advancePC8() {
        return memory[pc++];
    }

    int advancePC16() {
        pc += 2;
        return load16(memory, pc - 2);
    }

    int advancePC32() {
        pc += 4;
        return load32(memory, pc - 2);
    }

    int load8(int addr) {
        return memory[addr];
    }

    int load16(int addr) {
        return load16(memory, addr);
    }

    int load32(int addr) {
        return load32(memory, addr);
    }

    void store8(int addr, int value) {
        memory[addr] = (byte) value;
    }

    void store16(int addr, int value) {
        store16(memory, addr, value);
    }

    void store32(int addr, int value) {
        store32(memory, addr, value);
    }

    int pop32() {
        sp -= 4;
        return load32(stack, sp);
    }

    void push32(int value) {
        store32(stack, sp, value);
        sp += 4;
    }

    int sload8(int addr) {
        return stack[addr];
    }

    int sload16(int addr) {
        return load16(stack, addr);
    }

    int sload32(int addr) {
        return load32(stack, addr);
    }

    void sstore8(int addr, int value) {
        stack[addr] = (byte) value;
    }

    void sstore16(int addr, int value) {
        store16(stack, addr, value);
    }

    void sstore32(int addr, int value) {
        store32(stack, addr, value);
    }

    static int load16(byte[] bytes, int index) {
        return (bytes[index] << 8) | (bytes[index+1] & 255);
    }

    static int load32(byte[] bytes, int index) {
        return (bytes[index] << 24)
            | ((bytes[index+1] & 255) << 16)
            | ((bytes[index+2] & 255) << 8)
            | (bytes[index+3] & 255);
    }

    static void store16(byte[] bytes, int index, int value) {
        bytes[index] = (byte) (value >> 8);
        bytes[index+1] = (byte) value;
    }

    static void store32(byte[] bytes, int index, int value) {
        bytes[index] = (byte) (value >> 24);
        bytes[index+1] = (byte) (value >> 16);
        bytes[index+2] = (byte) (value >> 8);
        bytes[index+3] = (byte) value;
    }
}
