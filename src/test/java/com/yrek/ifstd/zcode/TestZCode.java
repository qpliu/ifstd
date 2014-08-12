package com.yrek.ifstd.zcode;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import com.yrek.ifstd.glk.GlkDispatch;
import com.yrek.ifstd.test.glk.TestGlk;

public class TestZCode {
    private void testFile(String file, String[] intro, String[] outro, String[][] transcript, String replaceRegex, String replacement) throws Exception {
        StringBuilder output = new StringBuilder();
        StringBuilder input = new StringBuilder();
        for (String line : intro) {
            output.append(line).append('\n');
        }
        for (String[] line : transcript) {
            if (line.length == 1) {
                output.append(line[0]).append('\n');
            } else if (line.length == 2) {
                if (line[0] != null) {
                    output.append(line[0]);
                }
                if (line[1] != null) {
                    input.append(line[1]);
                }
            }
        }
        for (String line : outro) {
            output.append(line);
        }
        StringReader in = new StringReader(input.toString());
        StringWriter out = new StringWriter();
        TestGlk glk = new TestGlk(in, null, out);
        ZCode zcode = new ZCode(new File(getClass().getResource(file).toURI()), new GlkDispatch(glk));
        zcode.run();
        String result = out.toString();
        if (replaceRegex != null) {
            result = result.replaceAll(replaceRegex, replacement);
        }
        Assert.assertEquals(output.toString(), result);
    }

    private static final String[] praxixIntro = new String[] {
        "",
        "Praxix: A Z-code interpreter unit test",
        "Release 1 / Serial number 100404 / Inform v6.31, compiler options S",
        "",
        "A voice booooms out: Welcome to the test chamber.",
        "",
        "Type \"help\" to repeat this message, \"quit\" to exit, \"all\" to run all tests, or one of the following test options: \"operand\", \"arith\", \"comarith\", \"bitwise\", \"shift\", \"inc\", \"incchk\", \"array\", \"undo\", \"multiundo\".",
        "",
    };

    private static final String[] praxixOutro = new String[] {
        "\n",
        "Goodbye.\n",
    };

    @Test
    public void praxix() throws Exception {
        testFile("/praxix.z5", praxixIntro, praxixOutro, new String[][] {
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @org.junit.Ignore
    @Test
    public void praxixOperand() throws Exception {
        testFile("/praxix.z5", praxixIntro, praxixOutro, new String[][] {
            new String[] { ">", "operand\n" },
            new String[] { "Basic operand values:" },
            new String[] { "" },
            new String[] { "(1==1)=1, (1==1)=1, (1==1)=1, (1==1)=1" },
            new String[] { "(-2==-2)=1, (-2==-2)=1, (-2==-2)=1, (-2==-2)=1" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }
}
