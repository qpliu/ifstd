package com.yrek.ifstd.test.glk;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Iterator;

import com.yrek.ifstd.glk.Glk;
import com.yrek.ifstd.glk.GlkByteArray;
import com.yrek.ifstd.glk.GlkEvent;
import com.yrek.ifstd.glk.GlkFile;
import com.yrek.ifstd.glk.GlkGestalt;
import com.yrek.ifstd.glk.GlkStream;
import com.yrek.ifstd.glk.GlkIntArray;
import com.yrek.ifstd.glk.GlkStream;
import com.yrek.ifstd.glk.GlkWindow;
import com.yrek.ifstd.glk.UnicodeString;

public class TestGlk implements Glk {
    final Reader reader;
    final Writer writer;
    final Writer output;
    TestGlkWindow rootWindow = null;
    GlkStream currentStream = null;
    final ArrayList<String> outputQueue = new ArrayList<String>();
    final ArrayDeque<TestGlkEventRequest> eventRequestQueue = new ArrayDeque<TestGlkEventRequest>();
    private int nameCounter = 0;
    private int timerInterval = 0;
    private long lastTimer = 0L;
    private boolean writeNewlines = false;

    public TestGlk(Reader reader, Writer writer, Writer output) {
        this.reader = reader;
        this.writer = writer;
        this.output = output;
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
        if (writer != null) {
            writer.append("</test>");
            if (writeNewlines) {
                writer.append('\n');
            }
            writer.flush();
        }
    }

    private void writeOutputQueue() throws IOException {
        if (rootWindow == null) {
            outputQueue.add("<windows/>");
        } else {
            outputQueue.add("<windows>");
            rootWindow.writeTree();
            outputQueue.add("</windows>");
        }
        if (writer != null) {
            for (String s : outputQueue) {
                writer.append(s);
                if (writeNewlines) {
                    writer.append('\n');
                }
            }
            writer.flush();
        }
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
            return 1;
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
            if (value < 256 && (value == 10 || !Character.isISOControl(value))) {
                array.setIntElement(1);
                return GlkGestalt.CharOutput_ExactPrint;
            } else {
                array.setIntElement(0);
                return GlkGestalt.CharOutput_CannotPrint;
            }
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
        currentStream = stream;
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
    public void putString(CharSequence string) throws IOException {
        currentStream.putString(string);
    }

    @Override
    public void putBuffer(GlkByteArray buffer) throws IOException {
        currentStream.putBuffer(buffer);
    }

    @Override
    public void putCharUni(int ch) throws IOException {
        currentStream.putCharUni(ch);
    }

    @Override
    public void putStringUni(UnicodeString string) throws IOException {
        currentStream.putStringUni(string);
    }

    @Override
    public void putBufferUni(GlkIntArray buffer) throws IOException {
        currentStream.putBufferUni(buffer);
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
    public GlkFile fileCreateByName(int usage, CharSequence name, int rock) throws IOException {
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
        for (;;) {
            GlkEvent event = selectTimer();
            if (event != null) {
                return outputEvent(event);
            }
            for (Iterator<TestGlkEventRequest> i = eventRequestQueue.iterator(); i.hasNext(); ) {
                TestGlkEventRequest eventRequest = i.next();
                event = eventRequest.select();
                if (event != null) {
                    i.remove();
                    return outputEvent(event);
                }
            }
            if (timerInterval > 0) {
                try {
                    Thread.sleep(Math.max(1L, lastTimer + timerInterval - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new IllegalArgumentException("No requested events");
            }
        }
    }

    @Override
    public GlkEvent selectPoll() throws IOException {
        GlkEvent event = selectTimer();
        if (event != null) {
            return outputEvent(event);
        }
        for (Iterator<TestGlkEventRequest> i = eventRequestQueue.iterator(); i.hasNext(); ) {
            TestGlkEventRequest eventRequest = i.next();
            event = eventRequest.poll();
            if (event != null) {
                i.remove();
                return outputEvent(event);
            }
        }
        return outputEvent(new GlkEvent(GlkEvent.TypeNone, null, 0, 0));
    }

    private GlkEvent selectTimer() {
        if (timerInterval <= 0) {
            return null;
        }
        long now = System.currentTimeMillis();
        if (now < lastTimer + timerInterval) {
            return null;
        }
        lastTimer = now;
        return new GlkEvent(GlkEvent.TypeTimer, null, 0, 0);
    }

    private GlkEvent outputEvent(GlkEvent event) {
        outputQueue.add("<event t=\""+event.type+"\""+(event.window==null?"":" w=\""+((TestGlkWindow) event.window).name+"\"")+" val1=\""+event.val1+"\" val2=\""+event.val2+"\"/>");
        return event;
    }

    @Override
    public void requestTimerEvents(int millisecs) {
        timerInterval = millisecs;
    }

    public void setWriteNewlines(boolean writeNewlines) {
        this.writeNewlines = writeNewlines;
    }
}
