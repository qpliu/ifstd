package com.yrek.ifstd.glulx;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Random;

import com.yrek.ifstd.glk.GlkDispatch;

class Machine {
    final byte[] byteData;
    final File fileData;
    final GlkDispatch glk;

    State state;
    State saveUndo = null;
    Random random = new Random();
    int protectStart = 0;
    int protectLength = 0;
    IOSys ioSys = new NullIOSys(0);
    int RAMStart = 256;
    int stringTable = 0;
    OutputState outputState = null;
    int outputIndex = 0;

    Machine(byte[] byteData, File fileData, GlkDispatch glk) throws IOException {
        this.byteData = byteData;
        this.fileData = fileData;
        this.glk = glk;
        state = load();
        stringTable = state.load32(28);
    }

    State load() throws IOException {
        State newState = new State();
        newState.readFile(getData(), 0, 0);
        Instruction.call(newState, newState.load32(24), new int[0]);
        return newState;
    }

    InputStream getData() throws IOException {
        if (byteData != null) {
            return new ByteArrayInputStream(byteData);
        } else {
            return new FileInputStream(fileData);
        }
    }

    enum OutputState {
        Latin1, Compressed, Unicode, Number;
    }
}
