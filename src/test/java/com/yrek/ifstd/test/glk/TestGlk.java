package com.yrek.ifstd.test.glk;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

import com.yrek.ifstd.glk.Glk;
import com.yrek.ifstd.glk.GlkByteArray;
import com.yrek.ifstd.glk.GlkEvent;
import com.yrek.ifstd.glk.GlkFile;
import com.yrek.ifstd.glk.GlkGestalt;
import com.yrek.ifstd.glk.GlkStream;
import com.yrek.ifstd.glk.GlkIntArray;
import com.yrek.ifstd.glk.GlkStream;
import com.yrek.ifstd.glk.GlkWindow;

public class TestGlk implements Glk {
    final Reader reader;
    private final Writer writer;
    TestGlkWindow rootWindow = null;
    TestGlkStream currentStream = null;
    final ArrayList<String> outputQueue = new ArrayList<String>();
    final ArrayList<TestGlkEventRequest> eventRequestQueue = new ArrayList<TestGlkEventRequest>();
    private int nameCounter = 0;

    public TestGlk(Reader reader, Writer writer) {
        this.reader = reader;
        this.writer = writer;
    }

    private class Exit extends RuntimeException {
        private static final long serialVersionUID = 0L;
    }

    private String name(String type) {
        nameCounter++;
        return type + nameCounter;
    }

    @Override
    public void main(Runnable main) throws IOException {
        outputQueue.add("<test>");
        try {
            main.run();
        } catch (Exit e) {
        }
        writeOutputQueue();
        writer.append("</test>");
    }

    private void writeOutputQueue() throws IOException {
        if (rootWindow == null) {
            outputQueue.add("<windows/>");
        } else {
            outputQueue.add("<windows>");
            rootWindow.writeTree();
            outputQueue.add("</windows>");
        }
        for (String s : outputQueue) {
            writer.append(s);
        }
        writer.flush();
        outputQueue.clear();
    }

    @Override
    public void exit() {
        throw new Exit();
    }

    @Override
    public void setInterruptHandler(Runnable handler) {
    }

    @Override
    public void tick() {
    }

    @Override
    public int gestalt(int selector, int value) {
        switch (selector) {
        case GlkGestalt.Version:
            return Glk.GlkVersion;
        case GlkGestalt.CharInput:
            return 0;
        case GlkGestalt.LineInput:
            return 1;
        case GlkGestalt.CharOutput:
            return 1;
        case GlkGestalt.MouseInput:
        case GlkGestalt.Timer:
        case GlkGestalt.Graphics:
        case GlkGestalt.DrawImage:
        case GlkGestalt.Sound:
        case GlkGestalt.SoundVolume:
        case GlkGestalt.SoundNotify:
        case GlkGestalt.Hyperlinks:
        case GlkGestalt.HyperlinkInput:
        case GlkGestalt.SoundMusic:
        case GlkGestalt.GraphicsTransparency:
        case GlkGestalt.Unicode:
        case GlkGestalt.UnicodeNorm:
        case GlkGestalt.LineInputEcho:
        case GlkGestalt.LineTerminators:
        case GlkGestalt.LineTerminatorKey:
        case GlkGestalt.DateTime:
        case GlkGestalt.Sound2:
        case GlkGestalt.ResourceStream:
        default:
            return 0;
        }
    }

    @Override
    public int gestaltExt(int selector, int value, GlkIntArray array) {
        switch (selector) {
        case GlkGestalt.CharOutput:
        default:
            return gestalt(selector, value);
        }
    }

    @Override
    public GlkWindow windowGetRoot() {
        return rootWindow;
    }

    @Override
    public GlkWindow windowOpen(GlkWindow split, int method, int size, int winType, int rock) {
        TestGlkWindow newWindow = new TestGlkWindow(this, name("window"), name("windowStream"), winType, rock);
        if (rootWindow == null) {
            assert split == null;
            rootWindow = newWindow;
        } else {
            TestGlkWindow pairWindow = new TestGlkWindow(this, name("pairWindow"), null, GlkWindow.TypePair, 0);
            pairWindow.parent = ((TestGlkWindow) split).parent;
            pairWindow.child1 = (TestGlkWindow) split;
            pairWindow.child2 = newWindow;
            pairWindow.key = newWindow;
            pairWindow.method = method;
            pairWindow.size = size;
            if (pairWindow.parent == null) {
                assert rootWindow == split;
                rootWindow = pairWindow;
            }
            newWindow.parent = pairWindow;
            ((TestGlkWindow) split).parent = pairWindow;
        }
        return newWindow;
    }

    @Override
    public void setWindow(GlkWindow window) {
        streamSetCurrent(window.getStream());
    }

    @Override
    public GlkStream streamOpenFile(GlkFile file, int mode, int rock) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public GlkStream streamOpenMemory(GlkByteArray memory, int mode, int rock) {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public void streamSetCurrent(GlkStream stream) {
        currentStream = (TestGlkStream) stream;
    }

    @Override
    public GlkStream streamGetCurrent() {
        return currentStream;
    }

    @Override
    public void putChar(int ch) throws IOException {
        currentStream.putChar(ch);
    }

    @Override
    public void putString(GlkByteArray string) throws IOException {
        currentStream.putString(string);
    }

    @Override
    public void putBuffer(GlkByteArray buffer) throws IOException {
        currentStream.putBuffer(buffer);
    }

    @Override
    public void setStyle(int style) {
    }

    @Override
    public void styleHintSet(int winType, int style, int hint, int value) {
    }

    @Override
    public void styleHintClear(int winType, int style, int hint) {
    }

    @Override
    public GlkFile fileCreateTemp(int usage, int rock) throws IOException {
        return null;
    }

    @Override
    public GlkFile fileCreateByName(int usage, GlkByteArray name, int rock) throws IOException {
        return null;
    }

    @Override
    public GlkFile fileCreateByPrompt(int usage, int mode, int rock) throws IOException {
        return null;
    }

    @Override
    public GlkFile fileCreateFromFile(int usage, GlkFile file, int rock) throws IOException {
        return null;
    }

    @Override
    public GlkEvent select() throws IOException {
        writeOutputQueue();
        throw new RuntimeException("unimplemented");
    }

    @Override
    public GlkEvent selectPoll() throws IOException {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public void requestTimerEvents(int millisecs) {
        throw new RuntimeException("unimplemented");
    }
}
