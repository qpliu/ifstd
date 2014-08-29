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

    transient GlkDispatch glk;
    transient GlkWindow mainWindow;
    transient GlkWindow upperWindow;
    transient int screenWidth;
    transient int screenHeight;
    transient boolean suspending;

    State state;
    State[] undoStates = new State[2];
    int undoStateIndex = 0;
    Random random = new Random();
    int currentWindow;
    boolean stream1 = true;
    int stream3Index = 0;
    Stream3[] stream3 = new Stream3[16];
    int upperWindowInitialHeight = 0;
    int upperWindowCurrentHeight = 0;
    int upperWindowTargetHeight = 0;

    transient Instruction.Operand[] operands;
    transient int noperands;
    transient StringBuilder string;

    Machine(byte[] byteData, File fileData, GlkDispatch glk) throws IOException {
        this.byteData = byteData;
        this.fileData = fileData;
        this.glk = glk;
        state = load();
    }

    void initGlk(int foregroundColor, int backgroundColor) throws IOException {
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
        GlkWindowSize windowSize = mainWindow.getSize();
        screenWidth = windowSize.width;
        screenHeight = windowSize.height;
        if (upperWindow != null) {
            windowSize = upperWindow.getSize();
            screenWidth = windowSize.width;
            screenHeight += windowSize.height;
        }
        if (currentWindow == 0 || upperWindow == null) {
            glk.glk.setWindow(mainWindow);
        } else {
            glk.glk.setWindow(upperWindow);
        }
        if (state.version <= 3) {
            int[] styles = new int[] {
                GlkStream.StyleNormal,
                GlkStream.StyleEmphasized,
                GlkStream.StylePreformatted,
                GlkStream.StyleHeader,
                GlkStream.StyleSubheader,
                GlkStream.StyleAlert,
                GlkStream.StyleNote,
                GlkStream.StyleBlockQuote,
                GlkStream.StyleInput,
                GlkStream.StyleUser1,
                GlkStream.StyleUser2,
            };
            for (int style : styles) {
                glk.glk.styleHintSet(GlkWindow.TypeTextGrid, style, GlkStream.StyleHintTextColor, backgroundColor);
                glk.glk.styleHintSet(GlkWindow.TypeTextGrid, style, GlkStream.StyleHintBackColor, foregroundColor);
            }
        }
        // translate Glk-colors to Z-colors
        state.setScreen(screenWidth, screenHeight, foregroundColor, backgroundColor);
    }

    void initForRun() {
        suspending = false;
        operands = new Instruction.Operand[8];
        for (int i = 0; i < operands.length; i++) {
            operands[i] = new Instruction.Operand(this);
        }
        string = new StringBuilder();
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

    Stream3 getStream3() {
        return stream3Index > 0 ? stream3[stream3Index-1] : null;
    }

    GlkStream getOutputStream() {
        if (!stream1) {
            return null;
        }
        if (currentWindow == 0) {
            return mainWindow.getStream();
        }
        if (upperWindow != null) {
            return upperWindow.getStream();
        }
        return null;
    }
}
