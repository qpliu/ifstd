package com.yrek.ifstd.glulx;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

import com.yrek.ifstd.glk.GlkDispatch;

class Machine implements Serializable {
    private static final long serialVersionUID = 0L;

    final byte[] byteData;
    final File fileData;
    transient GlkDispatch glk;

    State state;
    State[] saveUndo = new State[3];
    Random random = new Random();
    int protectStart = 0;
    int protectLength = 0;
    IOSys ioSys = new NullIOSys(0);
    StringTable stringTable;
    Acceleration acceleration = new Acceleration();
    transient int operandL0;
    transient int operandL1;
    transient int operandL2;
    transient int operandL3;
    transient int operandL4;
    transient int operandL5;
    transient int operandL6;
    transient Insn.Operand operandS0;
    transient Insn.Operand operandS1;

    Machine(byte[] byteData, File fileData, GlkDispatch glk) throws IOException {
        this.byteData = byteData;
        this.fileData = fileData;
        state = load();
        stringTable = StringTable.create(state, state.load32(28));
        resume(glk);
    }

    State load() throws IOException {
        State newState = new State();
        newState.readFile(getData(), 0, 0);
        Insn.resumeCallf(newState, newState.load32(24), 0, 0, 0, 0);
        return newState;
    }

    DataInputStream getData() throws IOException {
        if (byteData != null) {
            return new DataInputStream(new ByteArrayInputStream(byteData));
        } else {
            return new DataInputStream(new FileInputStream(fileData));
        }
    }

    void resume(GlkDispatch glk) {
        this.glk = glk;
        operandS0 = new Insn.Operand(this);
        operandS1 = new Insn.Operand(this);
    }
}
