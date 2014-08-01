package com.yrek.ifstd.zcode;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

import com.yrek.ifstd.glk.GlkDispatch;
import com.yrek.ifstd.glk.GlkWindow;

class Machine implements Serializable {
    private static final long serialVersionUID = 0L;

    final byte[] byteData;
    final File fileData;
    transient GlkDispatch glk;
    transient GlkWindow mainWindow;
    transient GlkWindow upperWindow;

    State state;
    State undoState = null;
    Random random = new Random();

    Machine(byte[] byteData, File fileData, GlkDispatch glk) throws IOException {
        this.byteData = byteData;
        this.fileData = fileData;
        setGlk(glk);
        state = load();
    }

    void setGlk(GlkDispatch glk) {
        this.glk = glk;
        //... get existing windows (by rock) or create new windows and close extraneous window
        //... close extraneous files, schannels, streams
    }

    State load() throws IOException {
        State newState = new State();
        newState.load(getData());
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
