package com.yrek.ifstd.glk;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import com.yrek.ifstd.test.glk.TestGlk;
import com.yrek.ifstd.test.glk.TestGlkArray;

public class Test1 {
    @Test
    public void test() throws Exception {
        StringWriter out = new StringWriter();
        TestGlk glk = new TestGlk(null, out);
        glk.main(new Runnable() {
            @Override public void run() {
            }
        });
        Assert.assertEquals("<test><windows/></test>", out.toString());
    }

    @Test
    public void testWindowOpen() throws Exception {
        {
            StringWriter out = new StringWriter();
            final TestGlk glk = new TestGlk(null, out);
            glk.main(new Runnable() {
                @Override public void run() {
                    glk.windowOpen(null, 0, 0, GlkWindow.TypeBlank, 0);
                }
            });
            Assert.assertEquals("<test><windows><leaf w=\"window1\"/></windows></test>", out.toString());
        }

        {
            StringWriter out = new StringWriter();
            final TestGlk glk = new TestGlk(null, out);
            glk.main(new Runnable() {
                @Override public void run() {
                    GlkWindow w = glk.windowOpen(null, 0, 0, GlkWindow.TypeBlank, 0);
                    glk.windowOpen(w, GlkWindowArrangement.MethodAbove | GlkWindowArrangement.MethodFixed | GlkWindowArrangement.MethodNoBorder, 20, GlkWindow.TypeTextBuffer, 0);
                }
            });
            Assert.assertEquals("<test><windows><pair w=\"pairWindow5\" k=\"window3\"><leaf w=\"window1\"/><leaf w=\"window3\"/></pair></windows></test>", out.toString());
        }
    }

    @Test
    public void testWindowClose() throws Exception {
        StringWriter out = new StringWriter();
        final TestGlk glk = new TestGlk(null, out);
        glk.main(new Runnable() {
            @Override public void run() {
                try {
                    GlkWindow w = glk.windowOpen(null, 0, 0, GlkWindow.TypeBlank, 0);
                    w.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Assert.assertEquals("<test><windows/></test>", out.toString());
    }

    @Test
    public void testOutput() throws Exception {
        {
            StringWriter out = new StringWriter();
            final TestGlk glk = new TestGlk(null, out);
            glk.main(new Runnable() {
                @Override public void run() {
                    try {
                        GlkWindow w = glk.windowOpen(null, 0, 0, GlkWindow.TypeBlank, 0);
                        glk.setWindow(w);
                        glk.putChar(48);
                        w.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            Assert.assertEquals("<test><out s=\"windowStream2\" v=\"0\"/><windows/></test>", out.toString());
        }

        {
            StringWriter out = new StringWriter();
            final TestGlk glk = new TestGlk(null, out);
            glk.main(new Runnable() {
                @Override public void run() {
                    try {
                        GlkWindow w = glk.windowOpen(null, 0, 0, GlkWindow.TypeBlank, 0);
                        glk.setWindow(w);
                        glk.putString(new TestGlkArray(new int[] { 116, 101, 115, 116, 0 }));
                        w.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            Assert.assertEquals("<test><out s=\"windowStream2\" v=\"test\"/><windows/></test>", out.toString());
        }
    }

    @Test
    public void testInput() throws Exception {
        StringReader in = new StringReader("0test\n");
        StringWriter out = new StringWriter();
        final TestGlk glk = new TestGlk(in, out);
        glk.main(new Runnable() {
            @Override public void run() {
                try {
                    GlkWindow w = glk.windowOpen(null, 0, 0, GlkWindow.TypeBlank, 0);
                    glk.setWindow(w);
                    glk.putChar(48);
                    w.requestCharEvent();
                    GlkEvent event = glk.select();
                    Assert.assertEquals(GlkEvent.TypeCharInput, event.type);
                    Assert.assertEquals(w, event.window);
                    Assert.assertEquals(48, event.val1);
                    Assert.assertEquals(0, event.val2);
                    TestGlkArray array = new TestGlkArray(6);
                    w.requestLineEvent(array, 0);
                    event = glk.select();
                    Assert.assertEquals(GlkEvent.TypeLineInput, event.type);
                    Assert.assertEquals(w, event.window);
                    Assert.assertEquals(4, event.val1);
                    Assert.assertEquals(0, event.val2);
                    Assert.assertArrayEquals(new int[] { 116, 101, 115, 116, 0, 0 }, array.elements);
                    w.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Assert.assertEquals("<test><out s=\"windowStream2\" v=\"0\"/><windows><leaf w=\"window1\"/></windows><event t=\"2\" w=\"window1\" val1=\"48\" val2=\"0\"/><windows><leaf w=\"window1\"/></windows><event t=\"3\" w=\"window1\" val1=\"4\" val2=\"0\"/><windows/></test>", out.toString());
    }
}
