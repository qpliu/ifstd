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
    State[] saveUndo = new State[3];
    Random random = new Random();
    int protectStart = 0;
    int protectLength = 0;
    IOSys ioSys = new NullIOSys(0);
    StringTable stringTable;

    Machine(byte[] byteData, File fileData, GlkDispatch glk) throws IOException {
        this.byteData = byteData;
        this.fileData = fileData;
        this.glk = glk;
        state = load();
        stringTable = StringTable.create(state, state.load32(28));
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
}
