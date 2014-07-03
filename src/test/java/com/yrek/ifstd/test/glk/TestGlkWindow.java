package com.yrek.ifstd.test.glk;

import java.io.EOFException;
import java.io.IOException;

import com.yrek.ifstd.glk.GlkByteArray;
import com.yrek.ifstd.glk.GlkEvent;
import com.yrek.ifstd.glk.GlkIntArray;
import com.yrek.ifstd.glk.GlkStreamResult;
import com.yrek.ifstd.glk.GlkWindow;
import com.yrek.ifstd.glk.GlkWindowArrangement;
import com.yrek.ifstd.glk.GlkWindowSize;
import com.yrek.ifstd.glk.GlkWindowStream;

public class TestGlkWindow extends GlkWindow {
    final TestGlk glk;
    final int winType;
    final String name;
    TestGlkWindow parent;
    TestGlkWindow child1;
    TestGlkWindow child2;
    TestGlkWindow key;
    int method;
    int size;
    private TestGlkEventRequest eventRequest = null;
    TestGlkStream stream;

    public TestGlkWindow(TestGlk glk, String name, String streamName, int winType, int rock) {
        super(rock);
        this.glk = glk;
        this.winType = winType;
        this.name = name;
        this.stream = new TestGlkStream(glk, streamName, this);
    }

    public void writeTree() {
        if (winType != GlkWindow.TypePair) {
            assert child1 == null;
            assert child2 == null;
            glk.outputQueue.add("<leaf w=\""+name+"\"/>");
        } else {
            assert child1 != null;
            assert child2 != null;
            glk.outputQueue.add("<pair w=\""+name+"\" "+(key==null?"":"k=\""+key.name+"\"")+">");
            child1.writeTree();
            child2.writeTree();
            glk.outputQueue.add("</pair>");
        }
    }

    @Override
    public GlkWindowStream getStream() {
        return stream;
    }

    @Override
    public GlkStreamResult close() throws IOException {
        if (child1 != null) {
            assert child2 != null;
            child1._close();
            child2._close();
        } else {
            assert child2 == null;
        }
        _close();
        TestGlkWindow sibling = (TestGlkWindow) getSibling();
        if (sibling == null) {
            assert this == glk.rootWindow;
            glk.rootWindow = null;
        } else {
            parent.destroy();
            if (parent.parent == null) {
                assert parent == glk.rootWindow;
                glk.rootWindow = sibling;
            } else if (parent.parent.child1 == parent) {
                parent.parent.child1 = sibling;
            } else {
                assert parent.parent.child2 == parent;
                parent.parent.child2 = sibling;
            }
        }
        return new GlkStreamResult(stream.inputCount, stream.outputCount);
    }

    protected void _close() {
        for (TestGlkWindow p = parent; p != null; p = p.parent) {
            if (p.key == this) {
                p.key = null;
            }
        }
        destroy();
    }

    @Override
    public GlkWindowSize getSize() {
        return new GlkWindowSize(80, 24);
    }

    @Override
    public void setArrangement(int method, int size, GlkWindow key) {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public GlkWindowArrangement getArrangement() {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public int getType() {
        return winType;
    }

    @Override
    public GlkWindow getParent() {
        return parent;
    }

    @Override
    public GlkWindow getSibling() {
        if (parent == null) {
            return null;
        }
        if (this == parent.child1) {
            return parent.child2;
        } else {
            assert this == parent.child2;
            return parent.child1;
        }
    }

    @Override
    public void clear() {
        glk.outputQueue.add("<clear w=\""+name+"\"/>");
    }

    @Override
    public void moveCursor(int x, int y) {
        glk.outputQueue.add("<moveCursor w=\""+name+"\" x=\""+x+"\" y=\""+y+"\"/>");
    }

    @Override
    public int getCursorX() {
        return 0;
    }

    @Override
    public int getCursorY() {
        return 0;
    }

    @Override
    public boolean styleDistinguish(int style1, int style2) {
        return style1 != style2;
    }

    @Override
    public Integer styleMeasure(int style, int hint) {
        return null;
    }

    @Override
    public void flowBreak() {
    }

    @Override
    public void requestLineEvent(final GlkByteArray buffer, final int initLength) {
        assert eventRequest == null;
        eventRequest = new TestGlkEventRequest() {
            @Override public GlkEvent select() throws IOException {
                eventRequest = null;
                int count = 0;
                for (;;) {
                    int ch = glk.reader.read();
                    if (ch < 0) {
                        if (count == 0) {
                            throw new EOFException();
                        }
                        break;
                    } else if (ch == 10) {
                        break;
                    }
                    if (count < buffer.getArrayLength()) {
                        buffer.setByteElementAt(count, ch);
                    }
                    count++;
                }
                return new GlkEvent(GlkEvent.TypeLineInput, TestGlkWindow.this, Math.min(count, buffer.getArrayLength()), 0);
            }

            @Override public GlkEvent poll() {
                return null;
            }
        };
        glk.eventRequestQueue.add(eventRequest);
    }

    @Override
    public void requestLineEventUni(final GlkIntArray buffer, final int initLength) {
    }

    @Override
    public void requestCharEvent() {
        assert eventRequest == null;
        eventRequest = new TestGlkEventRequest() {
            @Override public GlkEvent select() throws IOException {
                eventRequest = null;
                int ch = glk.reader.read();
                if (ch < 0) {
                    throw new EOFException();
                }
                return new GlkEvent(GlkEvent.TypeCharInput, TestGlkWindow.this, ch & 255, 0);
            }

            @Override public GlkEvent poll() {
                return null;
            }
        };
        glk.eventRequestQueue.add(eventRequest);
    }

    @Override
    public void requestCharEventUni() {
    }

    @Override
    public void requestMouseEvent() {
    }

    @Override
    public void requestHyperlinkEvent() {
    }

    @Override
    public GlkEvent cancelLineEvent() {
        glk.eventRequestQueue.remove(eventRequest);
        eventRequest = null;
        return new GlkEvent(GlkEvent.TypeLineInput, this, 0, 0);
    }

    @Override
    public void cancelCharEvent() {
        glk.eventRequestQueue.remove(eventRequest); 
       eventRequest = null;
    }

    @Override
    public void cancelMouseEvent() {
    }

    @Override
    public void cancelHyperlinkEvent() {
    }

    @Override
    public boolean drawImage(int resourceId, int val1, int val2) throws IOException {
        return false;
    }

    @Override
    public boolean drawScaledImage(int resourceId, int val1, int val2, int width, int height) throws IOException {
        return false;
    }

    @Override
    public void eraseRect(int left, int top, int width, int height) {
    }

    @Override
    public void fillRect(int color, int left, int top, int width, int height) {
    }

    @Override
    public void setBackgroundColor(int color) {
    }

    @Override
    public void setEchoLineEvent(boolean echoLineEvent) {
    }

    @Override
    public void setTerminatorsLineEvent(int[] keycodes) {
    }
}
