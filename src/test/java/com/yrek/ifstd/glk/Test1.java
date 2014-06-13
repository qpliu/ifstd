package com.yrek.ifstd.glk;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import com.yrek.ifstd.test.glk.TestGlk;

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
}
