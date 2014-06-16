package com.yrek.ifstd.glulx;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import com.yrek.ifstd.glk.GlkWindow;
import com.yrek.ifstd.test.glk.TestGlk;
import com.yrek.ifstd.test.glk.TestGlkStream;
import com.yrek.ifstd.test.glk.TestGlkWindow;

public class TestGlulx {
    private String testFile(String file, String input) throws Exception {
        StringReader in = new StringReader(input);
        StringWriter out = new StringWriter();
        TestGlk glk = new TestGlk(in, null, out);
        Glulx glulx = new Glulx(new File(getClass().getResource(file).toURI()), glk);
        glulx.run();
        return out.toString();
    }

    @Test
    public void advent() throws Exception {
        Glulx.trace = System.out;
        Assert.assertEquals("\n\n\n\n\n\nWelcome to Adventure!\n\nADVENTURE\nThe Interactive Original\nBy Will Crowther (1973) and Don Woods (1977)\nReconstructed in three steps by:\nDonald Ekman, David M. Baggett (1993) and Graham Nelson (1994)\n[In memoriam Stephen Bishop (1820?-1857): GN]\nRelease 5 / Serial number 961209 / Inform v6.21(G0.33) Library 6/10 \n\nAt End Of Road\nYou are standing at the end of a road before a small brick building. Around you is a forest. A small stream flows out of the building and down a gully.\n\n>At End Of RoadScore: 36Moves: 1\nInside Building\nYou are inside a building, a well house for a large spring.\n\nThere are some keys on the ground here.\n\nThere is tasty food here.\n\nThere is a shiny brass lamp nearby.\n\nThere is an empty bottle here.\n\n>Inside BuildingScore: 36Moves: 2Are you sure you want to quit? ", testFile("/Advent.ulx", "e\nquit\ny\n"));
    }

    @Test
    public void glulxercise() throws Exception {
        Glulx.trace = System.out;
        Assert.assertEquals("\nGlulxercise: A Glulx interpreter unit test\nRelease 5 / Serial number 120501 / Inform v6.32, compiler options S\nInterpreter version 0.0.0 / VM 3.1.2 / game file format 3.1.2\n\nA voice booooms out: Welcome to the test chamber.\n\nType \"help\" to repeat this message, \"quit\" to exit, \"all\" to run all tests, or one of the following test options: \"operand\", \"arith\", \"comvar\", \"comarith\", \"bitwise\", \"shift\", \"trunc\", \"extend\", \"aload\", \"astore\", \"arraybit\", \"call\", \"callstack\", \"jump\", \"jumpform\", \"compare\", \"stack\", \"gestalt\", \"throw\", \"strings\", \"ramstring\", \"iosys\", \"iosys2\", \"filter\", \"nullio\", \"glk\", \"gidispa\", \"random\", \"nonrandom\", \"search\", \"mzero\", \"mcopy\", \"undo\", \"multiundo\", \"verify\", \"protect\", \"memsize\", \"undomemsize\", \"undorestart\", \"heap\", \"undoheap\", \"acceleration\", \"floatconv\", \"floatarith\", \"floatmod\", \"floatround\", \"floatexp\", \"floattrig\", \"floatatan2\", \"fjumpform\", \"fjump\", \"fcompare\", \"fprint\", \"safari5\".\n\n>\nExiting via return. (Try \"opquit\" for @quit, \"glkquit\" for glk_exit().)\n\nGoodbye.\n", testFile("/glulxercise.ulx", "quit\n"));
    }
}
