package com.yrek.ifstd.zcode;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

import com.yrek.ifstd.glk.GlkDispatch;
import com.yrek.ifstd.glk.GlkEvent;
import com.yrek.ifstd.glk.GlkFile;
import com.yrek.ifstd.glk.GlkGestalt;
import com.yrek.ifstd.glk.GlkSChannel;
import com.yrek.ifstd.glk.GlkStream;
import com.yrek.ifstd.glk.GlkWindow;
import com.yrek.ifstd.glk.GlkWindowSize;
import com.yrek.ifstd.glk.GlkWindowStream;

class Machine implements Serializable {
    private static final long serialVersionUID = 0L;

    final byte[] byteData;
    final File fileData;
    int currentWindow;
    transient GlkDispatch glk;
    transient GlkWindow mainWindow;
    transient GlkWindow upperWindow;
    transient int screenWidth;
    transient int screenHeight;

    State state;
    State undoState = null;
    Random random = new Random();

    Machine(byte[] byteData, File fileData, GlkDispatch glk) throws IOException {
        this.byteData = byteData;
        this.fileData = fileData;
        setGlk(glk);
        state = load();
    }

    void setGlk(GlkDispatch glk) throws IOException {
        this.glk = glk;
        for (GlkWindow window : glk.windowList()) {
            switch (window.getType()) {
            case GlkWindow.TypePair:
                break;
            case GlkWindow.TypeTextBuffer:
                mainWindow = window;
                break;
            case GlkWindow.TypeTextGrid:
                upperWindow = window;
                break;
            default:
                window.close();
                break;
            }
        }
        if (mainWindow == null) {
            mainWindow = glk.glk.windowOpen(null, 0, 0, GlkWindow.TypeTextBuffer, 0);
            glk.add(mainWindow);
        }
        for (GlkStream stream : glk.streamList()) {
            if (!(stream instanceof GlkWindowStream)) {
                stream.close();
            }
        }
        for (GlkFile file : glk.fileList()) {
            file.destroy();
        }
        for (GlkSChannel schannel : glk.sChannelList()) {
            schannel.destroyChannel();
        }
        if (glk.glk.gestalt(GlkGestalt.Timer, 0) != 0) {
            glk.glk.requestTimerEvents(1);
            handleEvent(glk.glk.select());
            glk.glk.requestTimerEvents(0);
        }
        GlkWindowSize windowSize = mainWindow.getSize();
        screenWidth = windowSize.width;
        screenHeight = windowSize.height;
        if (upperWindow != null) {
            windowSize = upperWindow.getSize();
            screenWidth = windowSize.width;
            screenHeight += windowSize.height;
        }
    }

    State load() throws IOException {
        State newState = new State();
        newState.load(getData());
        newState.init(screenWidth, screenHeight);
        return newState;
    }

    InputStream getData() throws IOException {
        if (byteData != null) {
            return new ByteArrayInputStream(byteData);
        } else {
            return new FileInputStream(fileData);
        }
    }

    void handleEvent(GlkEvent event) {
        if (event.type == GlkEvent.TypeArrange) {
            GlkWindowSize windowSize = mainWindow.getSize();
            screenWidth = windowSize.width;
            screenHeight = windowSize.height;
            if (upperWindow != null) {
                windowSize = upperWindow.getSize();
                screenWidth = windowSize.width;
                screenHeight += windowSize.height;
            }
            state.store8(State.SCREEN_HEIGHT_CHARS, screenHeight);
            state.store8(State.SCREEN_WIDTH_CHARS, screenWidth);
            state.store16(State.SCREEN_WIDTH, screenWidth);
            state.store16(State.SCREEN_HEIGHT, screenHeight);
        }
    }
}
