package com.yrek.ifstd.test.glk;

import java.io.IOException;

import com.yrek.ifstd.glk.GlkByteArray;
import com.yrek.ifstd.glk.GlkEvent;
import com.yrek.ifstd.glk.GlkStreamResult;
import com.yrek.ifstd.glk.GlkWindow;
import com.yrek.ifstd.glk.GlkWindowArrangement;
import com.yrek.ifstd.glk.GlkWindowSize;

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

    public TestGlkWindow(TestGlk glk, String name, String streamName, int winType, int rock) {
        super(new TestGlkStream(glk,streamName,0), rock);
        this.glk = glk;
        this.winType = winType;
        this.name = name;
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
        return getStream().close();
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
    public boolean styleDistinguish(int style1, int style2) {
        return style1 != style2;
    }

    @Override
    public Integer styleMeasure(int style, int hint) {
        return null;
    }

    @Override
    public void requestLineEvent(GlkByteArray buffer, int initLength) {
        assert eventRequest == null;
        throw new RuntimeException("unimplemented");
    }

    @Override
    public void requestCharEvent() {
        assert eventRequest == null;
        throw new RuntimeException("unimplemented");
    }

    @Override
    public void requestMouseEvent() {
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
}