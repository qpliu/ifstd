package com.yrek.ifstd.glulx;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import com.yrek.ifstd.test.glk.TestGlk;

public class TestGlulx {
    private String testFile(String file, String input) throws Exception {
        StringReader in = new StringReader(input);
        StringWriter out = new StringWriter();
        TestGlk glk = new TestGlk(in, out);
        glk.setWriteNewlines(true);
        Glulx glulx = new Glulx(new File(getClass().getResource(file).toURI()), glk);
        glulx.run();
        return out.toString();
    }

    @org.junit.Ignore
    @Test
    public void advent() throws Exception {
        System.out.println(testFile("/Advent.ulx", "quit\ny\n"));
    }

    @org.junit.Ignore
    @Test
    public void glulxercise() throws Exception {
        System.out.println(testFile("/glulxercise.ulx", "quit\ny\n"));
    }
}
