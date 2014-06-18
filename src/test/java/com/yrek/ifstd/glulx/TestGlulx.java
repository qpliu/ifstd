package com.yrek.ifstd.glulx;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.yrek.ifstd.glk.GlkWindow;
import com.yrek.ifstd.test.glk.TestGlk;
import com.yrek.ifstd.test.glk.TestGlkStream;
import com.yrek.ifstd.test.glk.TestGlkWindow;

public class TestGlulx {
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
        Glulx glulx = new Glulx(new File(getClass().getResource(file).toURI()), glk);
        glulx.run();
        String result = out.toString();
        if (replaceRegex != null) {
            result = result.replaceAll(replaceRegex, replacement);
        }
        Assert.assertEquals(output.toString(), result);
    }

    @Before
    public void setup() {
        Glulx.trace = System.out;
    }

    @AfterClass
    public static void printProfilingData() {
        Map<String,long[]> data = Glulx.profilingData();
        if (data != null) {
            ArrayList<Map.Entry<String,long[]>> list = new ArrayList<Map.Entry<String,long[]>>(data.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<String,long[]>>() {
                @Override public int compare(Map.Entry<String,long[]> e1, Map.Entry<String,long[]> e2) {
                    // return Long.compare(e2.getValue()[0], e1.getValue()[0]);
                    long diff = e2.getValue()[0] - e1.getValue()[0];
                    return diff == 0 ? 0 : diff > 0 ? 1 : -1;
                }
                @Override public boolean equals(Object obj) {
                    return this == obj;
                }
            });
            for (Map.Entry<String,long[]> entry : list) {
                long[] numbers = entry.getValue();
                System.out.println(String.format("%14s c=%8d t=%6dms avg=%.3fus", entry.getKey(), numbers[0], numbers[1], numbers[0] == 0 ? 0.0 : 1000.0 * (double) numbers[1]/(double) numbers[0]));
            }
        }
    }

    private static final String[] adventIntro = new String[] {
        "",
        "",
        "",
        "",
        "",
        "",
        "Welcome to Adventure!",
        "",
        "ADVENTURE",
        "The Interactive Original",
        "By Will Crowther (1973) and Don Woods (1977)",
        "Reconstructed in three steps by:",
        "Donald Ekman, David M. Baggett (1993) and Graham Nelson (1994)",
        "[In memoriam Stephen Bishop (1820?-1857): GN]",
        "Release 5 / Serial number 961209 / Inform v6.21(G0.33) Library 6/10 ",
        "",
        "At End Of Road",
        "You are standing at the end of a road before a small brick building. Around you is a forest. A small stream flows out of the building and down a gully.",
        "",
    };

    private static final String[] adventOutro = new String[] {
        "Are you sure you want to quit? ",
    };

    @Test
    public void advent() throws Exception {
        testFile("/Advent.ulx", adventIntro, adventOutro, new String[][] {
            new String[] { ">At End Of RoadScore: 36Moves: 1", "e\n" },
            new String[] { "" },
            new String[] { "Inside Building" },
            new String[] { "You are inside a building, a well house for a large spring." },
            new String[] { "" },
            new String[] { "There are some keys on the ground here." },
            new String[] { "" },
            new String[] { "There is tasty food here." },
            new String[] { "" },
            new String[] { "There is a shiny brass lamp nearby." },
            new String[] { "" },
            new String[] { "There is an empty bottle here." },
            new String[] { "" },
            new String[] { ">Inside BuildingScore: 36Moves: 2", "quit\ny\n" },
        }, null, null);
    }

    private static final String[] glulxerciseIntro = new String[] {
        "",
        "Glulxercise: A Glulx interpreter unit test",
        "Release 5 / Serial number 120501 / Inform v6.32, compiler options S",
        "Interpreter version 0.0.0 / VM 3.1.2 / game file format 3.1.2",
        "",
        "A voice booooms out: Welcome to the test chamber.",
        "",
        "Type \"help\" to repeat this message, \"quit\" to exit, \"all\" to run all tests, or one of the following test options: \"operand\", \"arith\", \"comvar\", \"comarith\", \"bitwise\", \"shift\", \"trunc\", \"extend\", \"aload\", \"astore\", \"arraybit\", \"call\", \"callstack\", \"jump\", \"jumpform\", \"compare\", \"stack\", \"gestalt\", \"throw\", \"strings\", \"ramstring\", \"iosys\", \"iosys2\", \"filter\", \"nullio\", \"glk\", \"gidispa\", \"random\", \"nonrandom\", \"search\", \"mzero\", \"mcopy\", \"undo\", \"multiundo\", \"verify\", \"protect\", \"memsize\", \"undomemsize\", \"undorestart\", \"heap\", \"undoheap\", \"acceleration\", \"floatconv\", \"floatarith\", \"floatmod\", \"floatround\", \"floatexp\", \"floattrig\", \"floatatan2\", \"fjumpform\", \"fjump\", \"fcompare\", \"fprint\", \"safari5\".",
        "",
    };

    private static final String[] glulxerciseOutro = new String[] {
        "\n",
        "Exiting via return. (Try \"opquit\" for @quit, \"glkquit\" for glk_exit().)\n",
        "\n",
        "Goodbye.\n",
    };

    @Test
    public void glulxercise() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseOperand() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "operand\n" },
            new String[] { "Basic operand access:" },
            new String[] { "" },
            new String[] { "stkcount=0" },
            new String[] { "Constants: zero=0, -1=-1, 16=16, -$81=$FFFFFF7F, $100=$100, -$8000=$FFFF8000, $10000=$10000, $7FFFFFFF=$7FFFFFFF, $80000000=$80000000, $CDEF1234=$CDEF1234" },
            new String[] { "Constants: zero=0, -1=-1, 16=16, -$81=$FFFFFF7F, $100=$100, -$8000=$FFFF8000, $10000=$10000, $7FFFFFFF=$7FFFFFFF, $80000000=$80000000, $CDEF1234=$CDEF1234" },
            new String[] { "Global to local 123=123=123, local to global 321=321=321" },
            new String[] { "Stack: 456=456, Stack: 933=933" },
            new String[] { "Global to stack: 123=123" },
            new String[] { "Stack to stack: 789=789, Stack to stack: 1234=1234" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseArith() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "arith\n" },
            new String[] { "Integer arithmetic:" },
            new String[] { "" },
            new String[] { "2+2=4, -2+-3=-5, 3+-4=-1, -4+5=1, $7FFFFFFF+$7FFFFFFE=-3, $80000000+$80000000=0" },
            new String[] { "Globals 6+8=14" },
            new String[] { "2-2=0, -2-3=-5, 3-4=-1, -4-(-5)=1, $7FFFFFFF-$7FFFFFFE=1, $80000000-$80000001=-1, $7FFFFFFF-$80000001=-2" },
            new String[] { "Globals 6-8=-2" },
            new String[] { "2*2=4, -2*-3=6, 3*-4=-12, -4*5=-20, $10000*$10000 (trunc)=0, 311537*335117 (trunc)=$4ECE193D" },
            new String[] { "Globals -6*-8=48" },
            new String[] { "12/3=4, 11/2=5, -11/2=-5, 11/-2=-5, -11/-2=5, $7fffffff/2=$3FFFFFFF, $7fffffff/-2=$C0000001, -$7fffffff/2=$C0000001, -$7fffffff/-2=$3FFFFFFF, $80000000/2=$C0000000, $80000000/(-2)=$40000000, $80000000/1=$80000000" },
            new String[] { "Globals -48/-8=6, 48/7=6, 48/-7=-6, -48/7=-6, -48/-7=6" },
            new String[] { "12%3=0, 13%5=3, -13%5=-3, 13%-5=3, -13%-5=-3, $7fffffff%7=1, -$7fffffff%7=-1, $7fffffff%-7=1, -$7fffffff%-7=-1, $80000000%7=-2, $80000000%-7=-2, $80000000%2=0, $80000000%-2=0, $80000000%1=0" },
            new String[] { "Globals 49%8=1, 49%-8=1, -49%8=-1, -49%-8=-1" },
            new String[] { "-(0)=0, -(5)=-5, -(-5)=5, -($7FFFFFFF)=-2147483647, -($80000001)=2147483647, -($80000000)=$80000000" },
            new String[] { "global -($80000001)=2147483647" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseComvar() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "comvar\n" },
            new String[] { "Compound variable juggling:" },
            new String[] { "" },
            new String[] { "6=6, 5=5" },
            new String[] { "12=12, 16=16, 36=36, 32=32" },
            new String[] { "7=7, 6=6, 6=6" },
            new String[] { "7=7, 7=7" },
            new String[] { "8=8, 8=8" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseComarith() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "comarith\n" },
            new String[] { "Compound arithmetic expressions:" },
            new String[] { "" },
            new String[] { "(7+2)*-4=-36, (7+2)*-4=-36" },
            new String[] { "($10000*$10000)/16+1=1, ($10000*$10000)/16+1=1" },
            new String[] { "($7FFFFFFF+2)/16=-134217727, ($7FFFFFFF+2)/16=-134217727" },
            new String[] { "(-$7FFFFFFF-2)/16=134217727, (-$7FFFFFFF-2)/16=134217727" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseBitwise() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "bitwise\n" },
            new String[] { "Bitwise arithmetic:" },
            new String[] { "" },
            new String[] { "0&0=$0, $FFFFFFFF&0=$0, $FFFFFFFF&$FFFFFFFF=$FFFFFFFF, $0137FFFF&$FFFF7310=$1377310, $35&56=$14" },
            new String[] { "0|0=$0, $FFFFFFFF|0=$FFFFFFFF, $FFFFFFFF|$FFFFFFFF=$FFFFFFFF, $01370000|$00007310=$1377310, $35|56=$77" },
            new String[] { "0^0=$0, $FFFFFFFF^0=$FFFFFFFF, $FFFFFFFF^$FFFFFFFF=$0, $0137FFFF^$00007310=$1378CEF, $35^56=$63" },
            new String[] { "!0=$FFFFFFFF, !1=$FFFFFFFE, !$F=$FFFFFFF0, !$80000000=$7FFFFFFF" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseShift() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "shift\n" },
            new String[] { "Bit shifts:" },
            new String[] { "" },
            new String[] { "$1001<<0=$1001, $1001<<1=$2002, $1001<<4=$10010, $1001<<10=$400400, $1001<<16=$10010000, $1001<<24=$1000000, $1001<<31=$80000000, $1001<<32=$0, $1001<<-1=$0" },
            new String[] { "-2<<0=-2, -2<<1=-4, -2<<7=-256, -2<<31=0" },
            new String[] { "1<<0=$1, 1<<1=$2, 1<<2=$4, 1<<3=$8, 1<<4=$10, 1<<5=$20, 1<<6=$40, 1<<7=$80, 1<<8=$100, 1<<9=$200, 1<<10=$400, 1<<11=$800, 1<<12=$1000, 1<<13=$2000, 1<<14=$4000, 1<<15=$8000, 1<<16=$10000, 1<<17=$20000, 1<<18=$40000, 1<<19=$80000, 1<<20=$100000, 1<<21=$200000, 1<<22=$400000, 1<<23=$800000, 1<<24=$1000000, 1<<25=$2000000, 1<<26=$4000000, 1<<27=$8000000, 1<<28=$10000000, 1<<29=$20000000, 1<<30=$40000000, 1<<31=$80000000, 1<<32=0, 1<<-1=0" },
            new String[] { "$1001u>>0=$1001, $1001u>>1=$800, $1001u>>2=$400, $1001u>>6=$40, $1001u>>12=$1, $1001u>>13=$0, $1001u>>31=$0, $1001u>>32=$0" },
            new String[] { "$7FFFFFFFu>>0=$7FFFFFFF, $7FFFFFFFu>>1=$3FFFFFFF, $7FFFFFFFu>>2=$1FFFFFFF, $7FFFFFFFu>>6=$1FFFFFF, $7FFFFFFFu>>12=$7FFFF, $7FFFFFFFu>>13=$3FFFF, $7FFFFFFFu>>30=$1, $7FFFFFFFu>>31=$0, $7FFFFFFFu>>32=$0" },
            new String[] { "-1u>>0=$FFFFFFFF, -1u>>1=$7FFFFFFF, -1u>>2=$3FFFFFFF, -1u>>6=$3FFFFFF, -1u>>12=$FFFFF, -1u>>13=$7FFFF, -1u>>30=$3, -1u>>31=$1, -1u>>32=$0, -1u>>33=$0, -1u>>-1=$0" },
            new String[] { "-1u>>1=$7FFFFFFF, -1u>>2=$3FFFFFFF, -1u>>3=$1FFFFFFF, -1u>>4=$FFFFFFF, -1u>>5=$7FFFFFF, -1u>>6=$3FFFFFF, -1u>>7=$1FFFFFF, -1u>>8=$FFFFFF, -1u>>9=$7FFFFF, -1u>>10=$3FFFFF, -1u>>11=$1FFFFF, -1u>>12=$FFFFF, -1u>>13=$7FFFF, -1u>>14=$3FFFF, -1u>>15=$1FFFF, -1u>>16=$FFFF, -1u>>17=$7FFF, -1u>>18=$3FFF, -1u>>19=$1FFF, -1u>>20=$FFF, -1u>>21=$7FF, -1u>>22=$3FF, -1u>>23=$1FF, -1u>>24=$FF, -1u>>25=$7F, -1u>>26=$3F, -1u>>27=$1F, -1u>>28=$F, -1u>>29=$7, -1u>>30=$3, -1u>>31=$1, -1u>>32=0, -1u>>-1=0" },
            new String[] { "$1001s>>0=$1001, $1001s>>1=$800, $1001s>>2=$400, $1001s>>6=$40, $1001s>>12=$1, $1001s>>13=$0, $1001s>>31=$0, $1001s>>32=$0" },
            new String[] { "$7FFFFFFFs>>0=$7FFFFFFF, $7FFFFFFFs>>1=$3FFFFFFF, $7FFFFFFFs>>2=$1FFFFFFF, $7FFFFFFFs>>6=$1FFFFFF, $7FFFFFFFs>>12=$7FFFF, $7FFFFFFFs>>13=$3FFFF, $7FFFFFFFs>>30=$1, $7FFFFFFFs>>31=$0, $7FFFFFFFs>>32=$0" },
            new String[] { "-1s>>0=-1, -1s>>1=-1, -1s>>31=-1, -1s>>32=-1, -1s>>33=-1, -1s>>-1=-1" },
            new String[] { "-1000s>>0=-1000, -1000s>>1=-500, -1000s>>2=-250, -1000s>>4=-63, -1000s>>6=-16, -1000s>>9=-2, -1000s>>31=-1, -1000s>>32=-1, -1000s>>33=-1, -1000s>>-1=-1" },
            new String[] { "-1s>>0=-1, -1s>>1=-1, -1s>>2=-1, -1s>>3=-1, -1s>>4=-1, -1s>>5=-1, -1s>>6=-1, -1s>>7=-1, -1s>>8=-1, -1s>>9=-1, -1s>>10=-1, -1s>>11=-1, -1s>>12=-1, -1s>>13=-1, -1s>>14=-1, -1s>>15=-1, -1s>>16=-1, -1s>>17=-1, -1s>>18=-1, -1s>>19=-1, -1s>>20=-1, -1s>>21=-1, -1s>>22=-1, -1s>>23=-1, -1s>>24=-1, -1s>>25=-1, -1s>>26=-1, -1s>>27=-1, -1s>>28=-1, -1s>>29=-1, -1s>>30=-1, -1s>>31=-1, -1s>>32=-1, -1s>>-1=-1" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseTrunc() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "trunc\n" },
            new String[] { "Truncating copies:" },
            new String[] { "" },
            new String[] { "$12345678 s:> glob $01020304=$56780304, $80818283 s:> stack=$8283, glob $fedcba98 s:> glob $02030405=$FEDC0405, glob $fedcba98 s:> stack=$FEDC, stack $7654321f s:> glob $03040506=$321F0506, stack $654321fe s:> glob $04050607=$21FE0607, stack $674523f1 s:> stack=$23F1, stack $67452301 s:> stack=$2301" },
            new String[] { "$12345678 b:> glob $01020304=$78020304, $80818283 b:> stack=$83, glob $fedcba98 b:> glob $02030405=$FE030405, glob $fedcba98 b:> stack=$FE, stack $7654321f b:> glob $03040506=$1F040506, stack $654321fe b:> glob $04050607=$FE050607, stack $674523f1 b:> stack=$F1, stack $67452301 b:> stack=$1" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseExtend() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "extend\n" },
            new String[] { "Sign-extend:" },
            new String[] { "" },
            new String[] { "sexb($00)=$0, sexb($01)=$1, sexb($7f)=$7F, sexb($80)=$FFFFFF80, sexb($fe)=$FFFFFFFE, sexb($ff)=$FFFFFFFF, sexb($100)=$0, sexb($ffffff01)=$1, sexb($7f0f0ff0)=$FFFFFFF0" },
            new String[] { "sexb($02)=$2, sexb($0f)=$F, sexb($ff)=$FFFFFFFF, sexb($100)=$0, sexb($ffffff01)=$1, sexb($7f1f1ff1)=$FFFFFFF1" },
            new String[] { "sexs($00)=$0, sexs($01)=$1, sexs($7fff)=$7FFF, sexs($8000)=$FFFF8000, sexs($fffe)=$FFFFFFFE, sexs($ffff)=$FFFFFFFF, sexs($10000)=$0, sexs($ffff0001)=$1, sexs($7f0ff00f)=$FFFFF00F" },
            new String[] { "sexs($102)=$102, sexs($0fffff)=$FFFFFFFF, sexs($fffe)=$FFFFFFFE, sexs($10000)=$0, sexs($ffff0001)=$1, sexs($7f1ffff1)=$FFFFFFF1" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseAload() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "aload\n" },
            new String[] { "Array loads:" },
            new String[] { "" },
            new String[] { "Array sequence: $C" },
            new String[] { "arr-->0=$1020304, arr-->1=$FFFEFDFC, arr-->2=$F" },
            new String[] { "arr+1-->0=$20304FF, arr+1-->1=$FEFDFC00, arr+2-->0=$304FFFE, arr+2-->1=$FDFC0000, arr+3-->0=$4FFFEFD, arr+3-->1=$FC000000, arr+4-->-1=$1020304, arr+4-->0=$FFFEFDFC, arr+4-->1=$F" },
            new String[] { "arr2-->-1=$F, arr2-->-1=$F, arr2-->-1=$F, arr2-->-1=$F, arr2-->-1=$F, arr2-->-1=$F, arr2-->-1=$F, arr2-->-1=$F" },
            new String[] { "arr2-->0=$7F008002, arr2-->0=$7F008002, arr2-->0=$7F008002, arr2-->0=$7F008002, arr2-->0=$7F008002, arr2-->0=$7F008002, arr2-->0=$7F008002, arr2-->0=$7F008002" },
            new String[] { "arr2-->1=$100FFFF, arr2-->1=$100FFFF, arr2-->1=$100FFFF, arr2-->1=$100FFFF, arr2-->1=$100FFFF, arr2-->1=$100FFFF, arr2-->1=$100FFFF, arr2-->1=$100FFFF" },
            new String[] { "arr=>0=$102, arr=>1=$304, arr=>2=$FFFE, arr=>3=$FDFC, arr=>4=$0, arr=>5=$F" },
            new String[] { "arr+1=>0=$203, arr+1=>1=$4FF, arr+2=>0=$304, arr+2=>1=$FFFE, arr+3=>0=$4FF, arr+3=>1=$FEFD, arr+4=>-1=$304, arr+4=>0=$FFFE, arr+4=>1=$FDFC" },
            new String[] { "arr2=>-1=$F, arr2=>-1=$F, arr2=>-1=$F, arr2=>-1=$F, arr2=>-1=$F, arr2=>-1=$F, arr2=>-1=$F, arr2=>-1=$F" },
            new String[] { "arr2=>0=$7F00, arr2=>0=$7F00, arr2=>0=$7F00, arr2=>0=$7F00, arr2=>0=$7F00, arr2=>0=$7F00, arr2=>0=$7F00, arr2=>0=$7F00" },
            new String[] { "arr2=>1=$8002, arr2=>1=$8002, arr2=>1=$8002, arr2=>1=$8002, arr2=>1=$8002, arr2=>1=$8002, arr2=>1=$8002, arr2=>1=$8002" },
            new String[] { "arr->0=$1, arr->1=$2, arr->4=$FF, arr->5=$FE, arr->11=$F" },
            new String[] { "arr+1->0=$2, arr+1->1=$3, arr+2->0=$3, arr+2->1=$4, arr+3->0=$4, arr+3->1=$FF, arr+4->-1=$4, arr+4->0=$FF, arr+4->1=$FE" },
            new String[] { "arr2->-1=$F, arr2->-1=$F, arr2->-1=$F, arr2->-1=$F, arr2->-1=$F, arr2->-1=$F, arr2->-1=$F, arr2->-1=$F" },
            new String[] { "arr2->0=$7F, arr2->0=$7F, arr2->0=$7F, arr2->0=$7F, arr2->0=$7F, arr2->0=$7F, arr2->0=$7F, arr2->0=$7F" },
            new String[] { "arr2->2=$80, arr2->2=$80, arr2->2=$80, arr2->2=$80, arr2->2=$80, arr2->2=$80, arr2->2=$80, arr2->2=$80" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseAstore() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "astore\n" },
            new String[] { "Array stores:" },
            new String[] { "" },
            new String[] { "Array sequence: $C" },
            new String[] { "arr-->0=$2030405, arr-->1=$FEFDFCDB, arr-->2=$E0F" },
            new String[] { "arr+1-->0=$12131415, $2121314/$15FDFCDB, arr+1-->1=$E0E1E2E3, $2121314/$15E0E1E2/$E3000E0F, arr+2-->0=$12345678, $2121234/$5678E1E2, arr+2-->1=$FEDCBA99, arr+3-->0=$44556677, $2121244/$556677DC, arr+3-->1=$51413121, $2121244/$55667751/$4131210F, arr+4-->-1=$21436587, arr+4-->0=$31425364, arr+4-->1=$41526374, $21436587/$31425364/$41526374" },
            new String[] { "arr2-->-1=$D0000001, arr2-->-1=$D1000002, arr2-->-1=$D2000003, arr2-->-1=$D3000004, arr2-->-1=$E0000001, arr2-->-1=$E1000011, arr2-->-1=$E2000021, arr2-->-1=$E3000031" },
            new String[] { "arr2-->0=$F1223310, arr2-->0=$F2223311, arr2-->0=$F3223312, arr2-->0=$F4223313, arr2-->0=$F5223315, arr2-->0=$F6223316, arr2-->0=$F7223317, arr2-->0=$F8223318" },
            new String[] { "arr2-->1=$1, arr2-->1=$2, arr2-->1=$3, arr2-->1=$4, arr2-->1=$5, arr2-->1=$6, arr2-->1=$7, arr2-->1=$8" },
            new String[] { "arr=>0=$4050000, arr=>1=$405FCDB, arr=>2=$E0F0000" },
            new String[] { "arr+1=>0=$1415, $41415DB/$E0F0000, arr+1=>1=$E2E3, $41415E2/$E30F0000, arr+2=>0=$5678, $4145678/$E30F0000, arr+2=>1=$BA99, $4145678/$BA990000, arr+3=>0=$6677, $4145666/$77990000, arr+3=>1=$3121, $4145666/$77312100, arr+4=>-1=$6587, arr+4=>0=$5364, arr+4=>1=$6374, $4146587/$53646374" },
            new String[] { "arr2=>-1=$F001, arr2=>-1=$E002, arr2=>-1=$D003, arr2=>-1=$C004, arr2=>-1=$F001, arr2=>-1=$E011, arr2=>-1=$D021, arr2=>-1=$C031" },
            new String[] { "pre-guard=$9999C031, post-guard=$98979695" },
            new String[] { "arr2=>0=$3310, arr2=>0=$3311, arr2=>0=$3312, arr2=>0=$3313, arr2=>0=$3315, arr2=>0=$3316, arr2=>0=$3317, arr2=>0=$3318" },
            new String[] { "post-guard=$33189695" },
            new String[] { "arr2=>2=$1, arr2=>2=$2, arr2=>2=$3, arr2=>2=$4, arr2=>2=$5, arr2=>2=$6, arr2=>2=$7, arr2=>2=$8" },
            new String[] { "post-guard=$89695" },
            new String[] { "arr=>0=$5000000, arr=>1=$5DB0000, arr=>2=$5DB0F00, arr=>3=$5DB0F63" },
            new String[] { "arr+1=>0=$15, $5150F63/$0, arr+1=>1=$E3, $515E363/$0, arr+2=>0=$78, $5157863/$0, arr+2=>1=$99, $5157899/$0, arr+3=>0=$77, $5157877/$0, arr+3=>1=$21, $5157877/$21000000, arr+4=>-1=$87, arr+4=>0=$64, arr+4=>1=$74, $5157887/$64740000" },
            new String[] { "arr2=>-1=$1, arr2=>-1=$2, arr2=>-1=$3, arr2=>-1=$4, arr2=>-1=$1, arr2=>-1=$11, arr2=>-1=$21, arr2=>-1=$31" },
            new String[] { "pre-guard=$99999931, post-guard=$98979695" },
            new String[] { "arr2=>0=$10, arr2=>0=$11, arr2=>0=$12, arr2=>0=$13, arr2=>0=$15, arr2=>0=$16, arr2=>0=$17, arr2=>0=$18" },
            new String[] { "post-guard=$18979695" },
            new String[] { "arr2=>2=$1, arr2=>2=$2, arr2=>2=$3, arr2=>2=$4, arr2=>2=$5, arr2=>2=$6, arr2=>2=$7, arr2=>2=$8" },
            new String[] { "post-guard=$8979695" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseArraybit() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "arraybit\n" },
            new String[] { "Aloadbit and astorebit:" },
            new String[] { "" },
            new String[] { "bit 0=1, bit 1=0, bit 2=0, bit 3=0, bit 4=0, bit 5=0, bit 6=0, bit 7=0, bit 8=0, bit 9=1, bit 10=0, bit 11=0, bit 12=0, bit 13=0, bit 14=0, bit 15=0, " },
            new String[] { "bit -8=1, bit -7=1, bit -6=1, bit -5=1, bit -4=0, bit -3=0, bit -2=0, bit -1=0, bit 0=1, bit 1=1, bit 2=1, bit 3=1, bit 4=1, bit 5=1, bit 6=1, bit 7=0, bit 8=0, bit 9=0, bit 10=0, bit 11=0, bit 12=0, bit 13=0, bit 14=0, bit 15=0, " },
            new String[] { "bit -8=1, bit -7=1, bit -6=1, bit -5=1, bit -4=0, bit -3=0, bit -2=0, bit -1=0, bit 0=1, bit 1=1, bit 2=1, bit 3=1, bit 4=1, bit 5=1, bit 6=1, bit 7=0, bit 8=0, bit 9=0, bit 10=0, bit 11=0, bit 12=0, bit 13=0, bit 14=0, bit 15=0, " },
            new String[] { "bit 22=0, bit 23=1, bit 24=0, bit 25=1" },
            new String[] { "bit -31=0, bit -32=0, bit -33=1, bit -34=1" },
            new String[] { "bit 22=0, bit 23=1, bit 24=0, bit 25=1" },
            new String[] { "bit -31=0, bit -32=0, bit -33=1, bit -34=1" },
            new String[] { "bit 1 on=$2, bit 6 on=$42, bit 3 on=$4A, bit 0 off=$4A, bit 6 off=$A" },
            new String[] { "bit 15 off=$7F, bit 12 on=$7F, bit 8 off=$7E" },
            new String[] { "bit -1 on=$80, bit -8 on=$81" },
            new String[] { "$1000000, $3000000, $7000000, $F000000, $1F000000, $3F000000, $7F000000, $FF000000, $FF010000, $FF030000, $FF070000, $FF0F0000, $FF1F0000, $FF3F0000, $FF7F0000, $FFFF0000, $FFFF0100, $FFFF0300, $FFFF0700, $FFFF0F00, $FFFF1F00, $FFFF3F00, $FFFF7F00, $FFFFFF00, $FFFFFF01, $FFFFFF03, $FFFFFF07, $FFFFFF0F, $FFFFFF1F, $FFFFFF3F, $FFFFFF7F, $FFFFFFFF, " },
            new String[] { "$FEFFFFFF, $FCFFFFFF, $F8FFFFFF, $F0FFFFFF, $E0FFFFFF, $C0FFFFFF, $80FFFFFF, $FFFFFF, $FEFFFF, $FCFFFF, $F8FFFF, $F0FFFF, $E0FFFF, $C0FFFF, $80FFFF, $FFFF, $FEFF, $FCFF, $F8FF, $F0FF, $E0FF, $C0FF, $80FF, $FF, $FE, $FC, $F8, $F0, $E0, $C0, $80, $0, " },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseCall() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "call\n" },
            new String[] { "Call and tailcall:" },
            new String[] { "" },
            new String[] { "arg2adder()=0, arg2adder(4)=4, arg2adder(4,6)=10, arg2adder(4,6,1)=10" },
            new String[] { "hash(4,6,1)=19" },
            new String[] { "tailcalltest(2,3,4)=18, testglobal=2" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseCallstack() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "callstack\n" },
            new String[] { "Call with various stack arrangements:" },
            new String[] { "" },
            new String[] { "hash(6,3,5,4,2)=53" },
            new String[] { "hash(4,5,2,3,1)=37, guard value=99" },
            new String[] { "hash(6,3,5,4,2)=53, guard value=98" },
            new String[] { "hash(4,5,2,3,1)=37" },
            new String[] { "hash(6,3,5,4,2)=53" },
            new String[] { "hash()=0" },
            new String[] { "hash(7)=7, hash(8)=8" },
            new String[] { "hash(9)=9, guard value=99" },
            new String[] { "guard value=98" },
            new String[] { "hash(6,7)=20, hash(5,7,2)=25" },
            new String[] { "hash(4,5,2,3,1)=37, guard value=99" },
            new String[] { "hash(6,3,5,4,2)=53, guard value=98" },
            new String[] { "hash(4,5,2,3,1)=37" },
            new String[] { "hash(6,3,5,4,2)=53" },
            new String[] { "hash()=0" },
            new String[] { "hash(7)=7, hash(8)=8" },
            new String[] { "hash(9)=9, guard value=99" },
            new String[] { "guard value=98" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseJump() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "jump\n" },
            new String[] { "Jumps and branches:" },
            new String[] { "" },
            new String[] { "Jump loop 5=5" },
            new String[] { "jz 0=1, jz 1=0, jz -1=0" },
            new String[] { "jnz 0=0, jnz $1000000=1, jnz 1=1, jnz -1=1" },
            new String[] { "jumpabs test=33, 44" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseJumpform() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "jumpform\n" },
            new String[] { "Jump with various operand forms:" },
            new String[] { "" },
            new String[] { "Test A0=2" },
            new String[] { "Test A1=5, guard val=91" },
            new String[] { "Test B0=0, B1=1" },
            new String[] { "Test C0=0, C1=1" },
            new String[] { "Test D0=0, D1=1" },
            new String[] { "Test E0=2, E1=3, E2=4, E3=5, E4=6" },
            new String[] { "Test F0=2, F1=3, F2=9" },
            new String[] { "" },
            new String[] { "Jump-if-zero with various operand forms:" },
            new String[] { "" },
            new String[] { "Test B0=0, B1=1, B2=99, B3=99" },
            new String[] { "Test F0=2, F1=3, F2=9, F3=5, F4=2, F5=0, F6=1" },
            new String[] { "" },
            new String[] { "Jump-if-equal with various operand forms:" },
            new String[] { "" },
            new String[] { "Test B0=0, B1=1, B2=99" },
            new String[] { "Test F0=2, F1=3, F2=9, F3=5, F4=2, F5=1, F6=0" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseCompare() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "compare\n" },
            new String[] { "Compare branches:" },
            new String[] { "" },
            new String[] { "jgt 2: 1, 1, 1, 0, 0, 0, 1" },
            new String[] { "jgt -2: 1, 0, 0, 0, 0, 0, 0, 1" },
            new String[] { "jge 2: 1, 1, 1, 1, 0, 0, 1" },
            new String[] { "jge -2: 1, 1, 0, 0, 0, 0, 0, 1" },
            new String[] { "jlt 2: 0, 0, 0, 0, 1, 1, 0" },
            new String[] { "jlt -2: 0, 0, 1, 1, 1, 1, 1, 0" },
            new String[] { "jle 2: 0, 0, 0, 1, 1, 1, 0" },
            new String[] { "jle -2: 0, 1, 1, 1, 1, 1, 1, 0" },
            new String[] { "jgtu 2: 0, 1, 1, 0, 0, 0, 0" },
            new String[] { "jgtu -2: 1, 0, 0, 1, 1, 1, 1, 1" },
            new String[] { "jgeu 2: 0, 1, 1, 1, 0, 0, 0" },
            new String[] { "jgeu -2: 1, 1, 0, 1, 1, 1, 1, 1" },
            new String[] { "jltu 2: 1, 0, 0, 0, 1, 1, 1" },
            new String[] { "jltu -2: 0, 0, 1, 0, 0, 0, 0, 0" },
            new String[] { "jleu 2: 1, 0, 0, 1, 1, 1, 1" },
            new String[] { "jleu -2: 0, 1, 1, 0, 0, 0, 0, 0" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseStack() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "stack\n" },
            new String[] { "Stack operations:" },
            new String[] { "" },
            new String[] { "stkcount=0, stkcount=1, stkcount=2, stkcount=3, stkcount=3, stkcount=3, stkcount=3" },
            new String[] { "sp-sp=-1, sp-sp=3, sp-sp=1" },
            new String[] { "peek 0=9, peek 1=8, peek 2=7, peek 0=9, peek 1=8, peek 2=7, peek 0=9, peek 1=8, peek 2=7" },
            new String[] { "count=0" },
            new String[] { "peek 0=9, peek 1=8, peek 2=7, peek 0=9, peek 1=8, peek 2=7, peek 0=9, peek 1=8, peek 2=7" },
            new String[] { "count=0" },
            new String[] { "peek 0=9, peek 1=8, peek 2=7, peek 0=9, peek 1=8, peek 2=7, peek 0=9, peek 1=8, peek 2=7" },
            new String[] { "count=0" },
            new String[] { "peek 0=9, peek 1=8, peek 2=7, peek 0=9, peek 1=8, peek 2=7, peek 0=9, peek 1=8, peek 2=7" },
            new String[] { "count=0" },
            new String[] { "peek 1=8, peek 1=8, peek 1=8, peek 0=9, peek 0=9, peek 0=9, peek 2=7, peek 2=7, peek 2=7" },
            new String[] { "count=0" },
            new String[] { "hash(4,3,2)=16, hash(5,3,2,5,3,2)=64, hash(4,3,2,4,3,2)=59, hash(5,3,2,5,3,2)=64" },
            new String[] { "stkcount=0" },
            new String[] { "hash(5,3,2,5,3,2)=64, hash(4,3,2,4,3,2)=59, hash(5,3,2,5,3,2)=64" },
            new String[] { "stkcount=0" },
            new String[] { "hash(3,4,3,4,5)=61, hash(3,4,3,4,5)=61, hash(3,4,3,4,5)=61, hash(3,4,3,4,5)=61" },
            new String[] { "stkcount=0" },
            new String[] { "hash(6,4,3,2,1)=36, hash(6,4,3,2,1)=36, hash(6,4,3,2,1)=36, hash(4,3,6,2,1)=41, hash(3,6,4,2,1)=40, hash(4,3,6,2,1)=41, hash(3,6,4,2,1)=40, hash(6,4,3,2,1)=36, hash(6,4,3,2,1)=36" },
            new String[] { "hash(4,3,2,6,1)=45, hash(2,6,4,3,1)=43" },
            new String[] { "hash(4,3,2,6,1)=45, hash(2,6,4,3,1)=43" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseGestalt() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "gestalt\n" },
            new String[] { "Gestalt:" },
            new String[] { "" },
            new String[] { "gestalt(4,0)=1, gestalt(4,1)=1, gestalt(4,99)=0" },
            new String[] { "gestalt(1234,5678)=0" },
            new String[] { "gestalt(4,1)=1" },
            new String[] { "gestalt(4,1)=1" },
            new String[] { "guard=99" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseThrow() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "throw\n" },
            new String[] { "Catch/throw:" },
            new String[] { "" },
            new String[] { "catch 0=0, token=248" },
            new String[] { "catch 1=1, token=248" },
            new String[] { "catch 1 sp=1, token=248" },
            new String[] { "catch discard=100, token=252, guard=77" },
            new String[] { "catch computed=99, thrown=97, token=256" },
            new String[] { "global catch=105, token=220, guard=88" },
            new String[] { "global catch=106, token=220, guard=87" },
            new String[] { "global catch=107, token=220, guard=86" },
            new String[] { "local catch=105, token=220, guard=88" },
            new String[] { "local catch=104, token=220, count=1, guard=87" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseStrings() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "strings\n" },
            new String[] { "String table decoding:" },
            new String[] { "" },
            new String[] { "Basic strings: \"hello\" len 5 is len 5, \"bye\" len 3, \"\" len 0, \"abcdefghijklmnopqrstuvwxyz\" len 26, \"\u00e0\u00e8\u00ec\u00f2\u00f9\" len 5, \"" },
            new String[] { "\" len 1, \"This contains several node types." },
            new String[] { "\" len 34" },
            new String[] { "Unicode strings: \"hello\" len 5, \"abcdefghijklmnopqrstuvwxyz\" len 26, \"a\u00e0\u03b1\u30a9\" len 4" },
            new String[] { "C-style strings: \"C string.\" len 9" },
            new String[] { "\"C \u00dcn\u00efco\u03b4e \u201c\u30a9\u201d\" len 13" },
            new String[] { "Substrings: \"\"substring\"\" len 11, \"\"substring\"\" len 11" },
            new String[] { "References: \"[hello]\" len 7, \"[hello]\" len 7, \"[]\" len 2, \"[1]\" len 3, \"[1 2]\" len 5, \"[foo bar]\" len 9" },
            new String[] { "Indirect references: \"{0:bye:0}\" len 9, \"{0:\"substring\":0}\" len 17, \"{1:{0:hello:0}:1}\" len 17, \"{1:{0:bye hello:0}:1}\" len 21, \"{0:1:0}\" len 7, \"{1:2 3:1}\" len 9, \"{2:hello bye:2}\" len 15, \"{\"'``'\"}\" len 8" },
            new String[] { "Multiple references: \"{hello...bye...C string.}\" len 25, \"{{1:+0:1}...+1...bye hello}\" len 27, counter=2" },
            new String[] { "Indirect references with unicode: \"{2:a\u00e0\u03b1\u30a9:2}\" len 10" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseRamstring() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "ramstring\n" },
            new String[] { "String table decoding in RAM:" },
            new String[] { "" },
            new String[] { "\"Decode test.\" len 12" },
            new String[] { "\"Another test.\" len 13" },
            new String[] { "\"Third test.\" len 11" },
            new String[] { "\"\" len 0" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseIosys() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "iosys\n" },
            new String[] { "I/O mode switching:" },
            new String[] { "" },
            new String[] { "\"\" len 0, \"static glk\" len 10, \"=s=t=a=t=i=c= =f=i=l=t=e=r\" len 26" },
            new String[] { "\"=C= =\u00dc=n=\u00ef=c=o=\u03b4=e= =\u201c=\u30a9=\u201d\" len 26" },
            new String[] { "\"\" len 0, guard=99" },
            new String[] { "\"string, chr 0, -100 -2\" len 22, guard=99" },
            new String[] { "\"<s><t><r><i><n><g><,>< ><c><h><r>< ><0><,>< ><-><1><0><0>< ><-><2>\" len 66, guard=99" },
            new String[] { "\"<s><t><r><i><n><g><,>< ><c><h><r>< ><0><,>< ><-><1><0><0>< ><-><2>\" len 66, guard=99" },
            new String[] { "current=2, 0" },
            new String[] { "current=1, 39469" },
            new String[] { "Changing in mid-string: current=2, 0, \"<#>.\" len 4, current=2, 0, \"<a>bcde\" len 7, current=2, 0, \"<1>23\" len 5" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseIosys2() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "iosys2\n" },
            new String[] { "I/O mode with different store operands:" },
            new String[] { "This tests for a bug in older Glulxe (version 0.4.5 and earlier). Calling @getiosys with two different store operands (e.g., a local variable and a global variable) did the wrong thing in those releases. Because the bug has been around for so long, the spec recommends not doing what this test does." },
            new String[] { "" },
            new String[] { "current=2, 0" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseFilter() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "filter\n" },
            new String[] { "Filter iosys mode:" },
            new String[] { "" },
            new String[] { "Basic strings: \"=h=e=l=l=o\" len 10, \"b=y=e=\" len 6, \"=T=h=i=s= =c=o=n=t=a=i=n=s= =s=e=v=e=r=a=l= =n=o=d=e= =t=y=p=e=s=.=" },
new String[] { "\" len 68, \"=C= =\u00dc=n=\u00ef=c=o=\u00b4=e= =\u001c=\u00a9=\u001d\" len 26" },
            new String[] { "References: \"=[=h=e=l=l=o=]\" len 14, \"=[=1= =2=]\" len 10, \"=[=f=o=o= =b=a=r=]\" len 18" },
            new String[] { "Multiple references: \"={=h=e=l=l=o=.=.=.=b=y=e=.=.=.=C= =s=t=r=i=n=g=.=}\" len 50, \"={<.><.><.><[><h><e><l><l><o><]><.><.><.><[><1>< ><2><]><}>\" len 59, \"={={=1=:=+=0=:=1=}=.=.=.=+=1=.=.=.=b=y=e= =h=e=l=l=o=}\" len 54, counter=2, \"={=C= =s=t=r=i=n=g=.=.=.=.=+=0=.=.=.=C= =\u00dc=n=\u00ef=c=o=\u00b4=e= =\u001c=\u00a9=\u001d=}\" len 64, counter=1" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseNullio() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "nullio\n" },
            new String[] { "Null iosys mode:" },
            new String[] { "" },
            new String[] { "Basic strings: \"\" len 0, \"\" len 0, \"\" len 0, \"\" len 0" },
            new String[] { "References: \"\" len 0, \"\" len 0, \"\" len 0" },
            new String[] { "Multiple references: \"\" len 0, \"<.><.><.><[><1>< ><2><]><}>\" len 27, \"\" len 0, counter=2, \"\" len 0, counter=1" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @org.junit.Ignore
    @Test
    public void glulxerciseGlk() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "glk\n" },

            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @org.junit.Ignore
    @Test
    public void glulxerciseGidispa() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "gidispa\n" },

            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseRandom() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "random\n" },
            new String[] { "Random-number generator:" },
            new String[] { "NOTE: Tests may, very occasionally, fail through sheer bad luck. If so, try this test again." },
            new String[] { "" },
            new String[] { "Random 4: 0=?, 1=?, 2=?, 3=?, " },
            new String[] { "Random -5: 0=?, 1=?, 2=?, 3=?, 4=?, " },
            new String[] { "Random 0: 0=?, 1=?, 2=?, 3=?, lobit=?, hibit=?, " },
            new String[] { "Random 4 global: 0=?, 1=?, 2=?, 3=?, " },
            new String[] { "Random -5 global: 0=?, 1=?, 2=?, 3=?, 4=?, " },
            new String[] { "Random 0 global: 0=?, 1=?, 2=?, 3=?, lobit=?, hibit=?, " },
            new String[] { "Accumulated bits: hi=-1, lo=?, " },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, "=[0-9]+", "=?");
    }

    @Test
    public void glulxerciseNonrandom() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "nonrandom\n" },
            new String[] { "Random numbers in deterministic mode:" },
            new String[] { "" },
            new String[] { "setrandom 1: -1155869325, 431529176, 1761283695, 1749940626, 892128508, 155629808, 1429008869, -1465154083, -138487339, -1242363800, 26273138, 655996946, -155886662, 685382526, -258276172, -1915244828, " },
            new String[] { "setrandom 100: -1193959466, -1139614796, 837415749, -1220615319, -1429538713, 118249332, -951589224, 1301674577, -1638067850, -1279751870, -1618786051, 1068497434, 1016840512, 1147896915, 2092624704, 464282119, " },
            new String[] { "Sequences different: 1" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseSearch() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "search\n" },
            new String[] { "Search opcodes:" },
            new String[] { "" },
            new String[] { "Linear:" },
            new String[] { "got 160798, got 13, got 0, got -1" },
            new String[] { "got 160798, got 13" },
            new String[] { "got 160800, got 15, guard=999" },
            new String[] { "got 160798, got 13, got 0, got -1" },
            new String[] { "got 0, got -1, got 160802, got 17" },
            new String[] { "got 0, got -1, got 160795, got 10" },
            new String[] { "got 160797, got 6, got 6, got -1, got 160797, got 3" },
            new String[] { "got 160797, got 6, got 160797, got 3" },
            new String[] { "got 160809, got 6, got 0, got -1" },
            new String[] { "got 160789, got 1, got 0, got -1" },
            new String[] { "got 160793, got 2, got 0, got -1" },
            new String[] { "got 160810, got 5, got 160810, got 5" },
            new String[] { "got 160809, got 3, got 0, got -1, got 0, got -1, got 0, got -1" },
            new String[] { "got 160785, got 0, got 0, got -1, got 160785, got 0" },
            new String[] { "Binary:" },
            new String[] { "got 160830, got 13, got 0, got -1" },
            new String[] { "got 160830, got 13" },
            new String[] { "got 160832, got 15, guard=999" },
            new String[] { "got 160817, 160818, 160819, 160820, 160821, 160822, 160823, 160824, 160825, 160826, 160827, 160828, 160829, 160830, 160831, 160832, 160833, 160834, 160835, 160836, 160837, 160838, 160839, 160840, 160841, 160842, 160843, 160844, 160845, 160846, 160847, 160848, " },
            new String[] { "got 160817, 160818, 160819, 160820, 160821, 160822, 160823, 160824, 160825, 160826, 160827, 160828, 160829, 160830, 160831, 160832, 160833, 160834, 160835, 160836, 160837, 160838, 160839, 160840, 160841, 160842, 160843, 160844, 160845, 160846, 160847, " },
            new String[] { "got 160830, got 13, got 0, got -1" },
            new String[] { "got 160829, got 6, got 6, got -1, got 160829, got 3" },
            new String[] { "got 160829, got 6, got 160829, got 3" },
            new String[] { "got 160841, got 6" },
            new String[] { "got 160821, got 1, got 0, got -1" },
            new String[] { "got 160825, got 2, got 0, got -1" },
            new String[] { "got 160842, got 5, got 160842, got 5" },
            new String[] { "got 160841, got 3, got 0, got -1, got 0, got -1, got 0, got -1" },
            new String[] { "got 160817, got 0, got 0, got -1, got 160817, got 0" },
            new String[] { "Linked:" },
            new String[] { "got 160849, got 160849, got 160897, got 160905, got 160857, got 0" },
            new String[] { "got 160849, got 0, got 160905, got 0, got 0" },
            new String[] { "got 160849, got 160849, got 160897, got 160905, got 160857, got 0, got 0, got 0" },
            new String[] { "got 160849, got 0, got 160905, got 0, got 0" },
            new String[] { "got 160849, got 160905, got 160897, got 160873, got 0" },
            new String[] { "got 160849, got 160905, got 160897, got 0, got 0" },
            new String[] { "got 160849, got 160897, got 160905, got 160857, got 0" },
            new String[] { "got 160849, got 160897, got 160905, got 160857, got 0" },
            new String[] { "got 160849, got 160849, got 160897, got 160905, got 160857, got 0" },
            new String[] { "got 160849, got 0, got 160905, got 0, got 0" },
            new String[] { "got 160849, got 160849, got 160897, got 160905, got 160857, got 0, got 0, got 0" },
            new String[] { "got 160849, got 0, got 160905, got 0, got 0" },
            new String[] { "got 160849, got 160905, got 160897, got 160873, got 0" },
            new String[] { "got 160849, got 160905, got 160897, got 0, got 0" },
            new String[] { "got 160849, got 160897, got 160905, got 160857, got 0" },
            new String[] { "got 160849, got 160897, got 160905, got 160857, got 0" },
            new String[] { "got 160897, got 160857, guard=999" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseMzero() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "mzero\n" },
            new String[] { "mzero opcode:" },
            new String[] { "" },
            new String[] { "0, arr+4: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12" },
            new String[] { "1, arr+4: 1, 2, 3, 4, 0, 6, 7, 8, 9, 10, 11, 12" },
            new String[] { "6, arr+0: 0, 0, 0, 0, 0, 0, 7, 8, 9, 10, 11, 12" },
            new String[] { "3, arr+2: 1, 2, 0, 0, 0, 6, 7, 8, 9, 10, 11, 12" },
            new String[] { "4, arr+3: 1, 2, 3, 0, 0, 0, 0, 8, 9, 10, 11, 12" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseMcopy() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "mcopy\n" },
            new String[] { "mcopy opcode:" },
            new String[] { "" },
            new String[] { "0, arr+4, arr+6: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12" },
            new String[] { "0, arr+8, arr+6: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12" },
            new String[] { "4, arr+4, arr+4: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12" },
            new String[] { "5, arr+4, arr+6: 1, 2, 3, 4, 5, 6, 5, 6, 7, 8, 9, 12" },
            new String[] { "5, arr+6, arr+4: 1, 2, 3, 4, 7, 8, 9, 10, 11, 10, 11, 12" },
            new String[] { "3, arr+1, arr+8: 1, 2, 3, 4, 5, 6, 7, 8, 2, 3, 4, 12" },
            new String[] { "3, arr+8, arr+1: 1, 9, 10, 11, 5, 6, 7, 8, 9, 10, 11, 12" },
            new String[] { "2, arr+8, arr+1: 1, 9, 10, 4, 5, 6, 7, 8, 9, 10, 11, 12" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
         }, null, null);
    }

    @Test
    public void glulxerciseUndo() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "undo\n" },
            new String[] { "Undo:" },
            new String[] { "" },
            new String[] { "Interpreter claims to support undo." },
            new String[] { "" },
            new String[] { "Restore without saveundo: 1" },
            new String[] { "Restore without saveundo: 1" },
            new String[] { "Restore without saveundo: 1" },
            new String[] { "Undo saved..." },
            new String[] { "Restoring undo..." },
            new String[] { "Undo succeeded, return value -1." },
            new String[] { "loc=99 glob=999" },
            new String[] { "Undo saved..." },
            new String[] { "Restoring undo..." },
            new String[] { "Undo succeeded, return value -1." },
            new String[] { "loc=98 glob=998" },
            new String[] { "Undo saved..." },
            new String[] { "Restoring undo..." },
            new String[] { "Undo succeeded, return value -1." },
            new String[] { "loc=97 glob=997" },
            new String[] { "Undo saved..." },
            new String[] { "Restoring undo..." },
            new String[] { "Undo succeeded, return value -1." },
            new String[] { "loc=98 glob=998" },
            new String[] { "Undo saved..." },
            new String[] { "guard=9" },
            new String[] { "Restoring undo..." },
            new String[] { "Undo succeeded, return value -1." },
            new String[] { "loc=99 glob=999 glob2=-999" },
            new String[] { "guard=9" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseMultiundo() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "multiundo\n" },
            new String[] { "Multi-level undo:" },
            new String[] { "" },
            new String[] { "Interpreter claims to support undo." },
            new String[] { "" },
            new String[] { "Undo 1 saved..." },
            new String[] { "Undo 2 saved..." },
            new String[] { "Restoring undo 2..." },
            new String[] { "Undo 2 succeeded, return value -1." },
            new String[] { "loc=77 glob=777" },
            new String[] { "Restoring undo 1..." },
            new String[] { "Undo 1 succeeded, return value -1." },
            new String[] { "loc=99 glob=999" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseVerify() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "verify\n" },
            new String[] { "Verify:" },
            new String[] { "" },
            new String[] { "verify=0" },
            new String[] { "verify=0" },
            new String[] { "verify=0" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseProtect() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "protect\n" },
            new String[] { "Protect:" },
            new String[] { "" },
            new String[] { "Undo saved..." },
            new String[] { "Restoring undo..." },
            new String[] { "Undo succeeded, return value -1." },
            new String[] { "Protected 3,6: 1, 2, 3, 99, 99, 99, 99, 99, 99, 10, 11, 12" },
            new String[] { "Undo saved..." },
            new String[] { "Restoring undo..." },
            new String[] { "Undo succeeded, return value -1." },
            new String[] { "Protected 6,1: 1, 2, 3, 4, 5, 6, 99, 8, 9, 10, 11, 12" },
            new String[] { "Undo saved..." },
            new String[] { "Restoring undo..." },
            new String[] { "Undo succeeded, return value -1." },
            new String[] { "Unprotected: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseMemsize() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "memsize\n" },
            new String[] { "Memory-size extension:" },
            new String[] { "" },
            new String[] { "Interpreter claims to not support memory resizing." },
            new String[] { "" },
            new String[] { "Initial memsize=$29B00" },
            new String[] { "Trying @setmemsize, which should fail..." },
            new String[] { "@setmemsize=1" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseUndomemsize() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "undomemsize\n" },
            new String[] { "Undo of memory-size extension:" },
            new String[] { "" },
            new String[] { "Interpreter claims to not support memory resizing. Skipping test." },
            new String[] { "" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseUndorestart() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "undorestart\n" },
            new String[] { "Undo of restart:" },
            new String[] { "" },
            new String[] { "Undo saved..." },
            new String[] { "" },
            new String[] { "A voice booooms out: You've been here before!" },
            new String[] { "" },
            new String[] { "Restoring undo..." },
            new String[] { "Undo succeeded, return value -1." },
            new String[] { "Magic number 1234, 3, 1" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseHeap() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "heap\n" },
            new String[] { "Heap:" },
            new String[] { "" },
            new String[] { "Interpreter claims to not support heap allocation. Skipping test." },
            new String[] { "" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseUndoheap() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "undoheap\n" },
            new String[] { "Heap:" },
            new String[] { "" },
            new String[] { "Interpreter claims to not support heap allocation. Skipping test." },
            new String[] { "" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseAcceleration() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "acceleration\n" },
            new String[] { "Acceleration:" },
            new String[] { "(This tests only the operands. For a complete test of the accelfunc and accelparam opcodes, see accelfunctest.ulx.)" },
            new String[] { "" },
            new String[] { "guard=987" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseFloatconv() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "floatconv\n" },
            new String[] { "Floating-point conversion:" },
            new String[] { "" },
            new String[] { "0=$0, -0=$80000000, 1=$3F800000, 9.2e-41=$10000, 2.9e-39=$200001, 1.2e-38=$7FFFFF, 1.2e-38=$800000, 3.8e-34=$8000000, 3.4e+38=$7F7FFFFF, Inf=$7F800000, -Inf=$FF800000" },
            new String[] { "" },
            new String[] { "numtof 0=$0, numtof 1=$3F800000, numtof -1=$BF800000, numtof 2=$40000000, numtof -2=$C0000000, numtof 33=$42040000, numtof -33=$C2040000, numtof 100=$42C80000, numtof -100=$C2C80000, numtof 12345=$4640E400, numtof -12345=$C640E400, numtof 9876543=$4B16B43F, numtof -9876543=$CB16B43F, numtof $1000000=$4B800000, numtof -$1000000=$CB800000, numtof $1000001=$4B800000, numtof -$1000001=$CB800000, numtof $1234CDEF=$4D91A66F, numtof -$1234CDEF=$CD91A66F, numtof $7FFFFFFF=$4F000000, numtof -$7FFFFFFF=$CF000000, numtof $80000000=$CF000000" },
            new String[] { "numtof 0=$0, numtof 1=$3F800000, numtof -1=$BF800000, numtof 2=$40000000, numtof -2=$C0000000, numtof 33=$42040000, numtof -33=$C2040000, numtof 100=$42C80000, numtof -100=$C2C80000, numtof 12345=$4640E400, numtof -12345=$C640E400, numtof 9876543=$4B16B43F, numtof -9876543=$CB16B43F, numtof $1000000=$4B800000, numtof -$1000000=$CB800000, numtof $1000001=$4B800000, numtof -$1000001=$CB800000, numtof $1234CDEF=$4D91A66F, numtof -$1234CDEF=$CD91A66F, numtof $7FFFFFFF=$4F000000, numtof -$7FFFFFFF=$CF000000, numtof $80000000=$CF000000" },
            new String[] { "numtof 0=$0, numtof 1=$3F800000, numtof -1=$BF800000, numtof 2=$40000000, numtof -2=$C0000000, numtof 33=$42040000, numtof -33=$C2040000, numtof 100=$42C80000, numtof -100=$C2C80000, numtof 12345=$4640E400, numtof -12345=$C640E400, numtof 9876543=$4B16B43F, numtof -9876543=$CB16B43F, numtof $1000000=$4B800000, numtof -$1000000=$CB800000, numtof $1000001=$4B800000, numtof -$1000001=$CB800000, numtof $1234CDEF=$4D91A66F, numtof -$1234CDEF=$CD91A66F, numtof $7FFFFFFF=$4F000000, numtof -$7FFFFFFF=$CF000000, numtof $80000000=$CF000000" },
            new String[] { "numtof 0=$0, numtof 1=$3F800000, numtof -1=$BF800000, numtof 2=$40000000, numtof -2=$C0000000, numtof 33=$42040000, numtof -33=$C2040000, numtof 100=$42C80000, numtof -100=$C2C80000, numtof 12345=$4640E400, numtof -12345=$C640E400, numtof 9876543=$4B16B43F, numtof -9876543=$CB16B43F, numtof $1000000=$4B800000, numtof -$1000000=$CB800000, numtof $1000001=$4B800000, numtof -$1000001=$CB800000, numtof $1234CDEF=$4D91A66F, numtof -$1234CDEF=$CD91A66F, numtof $7FFFFFFF=$4F000000, numtof -$7FFFFFFF=$CF000000, numtof $80000000=$CF000000" },
            new String[] { "" },
            new String[] { "ftonumz 0.0=$0, ftonumz -0.0=$0, ftonumz 0.9=$0, ftonumz -0.9=$0, ftonumz 1.0=$1, ftonumz -1.0=$FFFFFFFF, ftonumz 1.75=$1, ftonumz -1.75=$FFFFFFFF, ftonumz 2.0=$2, ftonumz -2.0=$FFFFFFFE, ftonumz 10.1=$A, ftonumz -10.1=$FFFFFFF6, ftonumz 999.99995=$3E7, ftonumz -999.99995=$FFFFFC19, ftonumz $1000000=$1000000, ftonumz -$1000000=$FF000000, ftonumz $7FFFFF00=$7FFFFF00, ftonumz -$7FFFFF00=$80000100, ftonumz $80000000=$7FFFFFFF, ftonumz -$80000000=$80000000, ftonumz $90000000=$7FFFFFFF, ftonumz -$90000000=$80000000, ftonumz $C1234500=$7FFFFFFF, ftonumz -$C1234500=$80000000, ftonumz $100000000=$7FFFFFFF, ftonumz -$100000000=$80000000, ftonumz 3.4e+34=$7FFFFFFF, ftonumz -3.4e+34=$80000000, ftonumz +Inf=$7FFFFFFF, ftonumz -Inf=$80000000, ftonumz +NaN=$7FFFFFFF, ftonumz -NaN=$80000000" },
            new String[] { "ftonumz 0.0=$0, ftonumz -0.0=$0, ftonumz 0.9=$0, ftonumz -0.9=$0, ftonumz 1.0=$1, ftonumz -1.0=$FFFFFFFF, ftonumz 1.75=$1, ftonumz -1.75=$FFFFFFFF, ftonumz 2.0=$2, ftonumz -2.0=$FFFFFFFE, ftonumz 10.1=$A, ftonumz -10.1=$FFFFFFF6, ftonumz 999.99995=$3E7, ftonumz -999.99995=$FFFFFC19, ftonumz $1000000=$1000000, ftonumz -$1000000=$FF000000, ftonumz $7FFFFF00=$7FFFFF00, ftonumz -$7FFFFF00=$80000100, ftonumz $80000000=$7FFFFFFF, ftonumz -$80000000=$80000000, ftonumz $90000000=$7FFFFFFF, ftonumz -$90000000=$80000000, ftonumz $C1234500=$7FFFFFFF, ftonumz -$C1234500=$80000000, ftonumz $100000000=$7FFFFFFF, ftonumz -$100000000=$80000000, ftonumz 3.4e+34=$7FFFFFFF, ftonumz -3.4e+34=$80000000, ftonumz +Inf=$7FFFFFFF, ftonumz -Inf=$80000000, ftonumz +NaN=$7FFFFFFF, ftonumz -NaN=$80000000" },
            new String[] { "ftonumz 0.0=$0, ftonumz -0.0=$0, ftonumz 0.9=$0, ftonumz -0.9=$0, ftonumz 1.0=$1, ftonumz -1.0=$FFFFFFFF, ftonumz 1.75=$1, ftonumz -1.75=$FFFFFFFF, ftonumz 2.0=$2, ftonumz -2.0=$FFFFFFFE, ftonumz 10.1=$A, ftonumz -10.1=$FFFFFFF6, ftonumz 999.99995=$3E7, ftonumz -999.99995=$FFFFFC19, ftonumz $1000000=$1000000, ftonumz -$1000000=$FF000000, ftonumz $7FFFFF00=$7FFFFF00, ftonumz -$7FFFFF00=$80000100, ftonumz $80000000=$7FFFFFFF, ftonumz -$80000000=$80000000, ftonumz $90000000=$7FFFFFFF, ftonumz -$90000000=$80000000, ftonumz $C1234500=$7FFFFFFF, ftonumz -$C1234500=$80000000, ftonumz $100000000=$7FFFFFFF, ftonumz -$100000000=$80000000, ftonumz 3.4e+34=$7FFFFFFF, ftonumz -3.4e+34=$80000000, ftonumz +Inf=$7FFFFFFF, ftonumz -Inf=$80000000, ftonumz +NaN=$7FFFFFFF, ftonumz -NaN=$80000000" },
            new String[] { "ftonumz 0.0=$0, ftonumz -0.0=$0, ftonumz 0.9=$0, ftonumz -0.9=$0, ftonumz 1.0=$1, ftonumz -1.0=$FFFFFFFF, ftonumz 1.75=$1, ftonumz -1.75=$FFFFFFFF, ftonumz 2.0=$2, ftonumz -2.0=$FFFFFFFE, ftonumz 10.1=$A, ftonumz -10.1=$FFFFFFF6, ftonumz 999.99995=$3E7, ftonumz -999.99995=$FFFFFC19, ftonumz $1000000=$1000000, ftonumz -$1000000=$FF000000, ftonumz $7FFFFF00=$7FFFFF00, ftonumz -$7FFFFF00=$80000100, ftonumz $80000000=$7FFFFFFF, ftonumz -$80000000=$80000000, ftonumz $90000000=$7FFFFFFF, ftonumz -$90000000=$80000000, ftonumz $C1234500=$7FFFFFFF, ftonumz -$C1234500=$80000000, ftonumz $100000000=$7FFFFFFF, ftonumz -$100000000=$80000000, ftonumz 3.4e+34=$7FFFFFFF, ftonumz -3.4e+34=$80000000, ftonumz +Inf=$7FFFFFFF, ftonumz -Inf=$80000000, ftonumz +NaN=$7FFFFFFF, ftonumz -NaN=$80000000" },
            new String[] { "" },
            new String[] { "ftonumn 0.0=$0, ftonumn -0.0=$0, ftonumn 0.9=$1, ftonumn -0.9=$FFFFFFFF, ftonumn 1.0=$1, ftonumn -1.0=$FFFFFFFF, ftonumn 1.75=$2, ftonumn -1.75=$FFFFFFFE, ftonumn 2.0=$2, ftonumn -2.0=$FFFFFFFE, ftonumn 10.1=$A, ftonumn -10.1=$FFFFFFF6, ftonumn 999.99995=$3E8, ftonumn -999.99995=$FFFFFC18, ftonumn $1000000=$1000000, ftonumn -$1000000=$FF000000, ftonumn $7FFFFF00=$7FFFFF00, ftonumn -$7FFFFF00=$80000100, ftonumn $80000000=$7FFFFFFF, ftonumn -$80000000=$80000000, ftonumn $90000000=$7FFFFFFF, ftonumn -$90000000=$80000000, ftonumn $C1234500=$7FFFFFFF, ftonumn -$C1234500=$80000000, ftonumn $100000000=$7FFFFFFF, ftonumn -$100000000=$80000000, ftonumn 3.4e+34=$7FFFFFFF, ftonumn -3.4e+34=$80000000, ftonumn +Inf=$7FFFFFFF, ftonumn -Inf=$80000000, ftonumn +NaN=$7FFFFFFF, ftonumn -NaN=$80000000" },
            new String[] { "ftonumn 0.0=$0, ftonumn -0.0=$0, ftonumn 0.9=$1, ftonumn -0.9=$FFFFFFFF, ftonumn 1.0=$1, ftonumn -1.0=$FFFFFFFF, ftonumn 1.75=$2, ftonumn -1.75=$FFFFFFFE, ftonumn 2.0=$2, ftonumn -2.0=$FFFFFFFE, ftonumn 10.1=$A, ftonumn -10.1=$FFFFFFF6, ftonumn 999.99995=$3E8, ftonumn -999.99995=$FFFFFC18, ftonumn $1000000=$1000000, ftonumn -$1000000=$FF000000, ftonumn $7FFFFF00=$7FFFFF00, ftonumn -$7FFFFF00=$80000100, ftonumn $80000000=$7FFFFFFF, ftonumn -$80000000=$80000000, ftonumn -$90000000=$80000000, ftonumn $C1234500=$7FFFFFFF, ftonumn -$C1234500=$80000000, ftonumn $100000000=$7FFFFFFF, ftonumn -$100000000=$80000000, ftonumn 3.4e+34=$7FFFFFFF, ftonumn -3.4e+34=$80000000, ftonumn +Inf=$7FFFFFFF, ftonumn -Inf=$80000000, ftonumn +NaN=$7FFFFFFF, ftonumn -NaN=$80000000" },
            new String[] { "ftonumn 0.0=$0, ftonumn -0.0=$0, ftonumn 0.9=$1, ftonumn -0.9=$FFFFFFFF, ftonumn 1.0=$1, ftonumn -1.0=$FFFFFFFF, ftonumn 1.75=$2, ftonumn -1.75=$FFFFFFFE, ftonumn 2.0=$2, ftonumn -2.0=$FFFFFFFE, ftonumn 10.1=$A, ftonumn -10.1=$FFFFFFF6, ftonumn 999.99995=$3E8, ftonumn -999.99995=$FFFFFC18, ftonumn $1000000=$1000000, ftonumn -$1000000=$FF000000, ftonumn $7FFFFF00=$7FFFFF00, ftonumn -$7FFFFF00=$80000100, ftonumn $80000000=$7FFFFFFF, ftonumn -$80000000=$80000000, ftonumn $90000000=$7FFFFFFF, ftonumn -$90000000=$80000000, ftonumn $C1234500=$7FFFFFFF, ftonumn -$C1234500=$80000000, ftonumn $100000000=$7FFFFFFF, ftonumn -$100000000=$80000000, ftonumn 3.4e+34=$7FFFFFFF, ftonumn -3.4e+34=$80000000, ftonumn +Inf=$7FFFFFFF, ftonumn -Inf=$80000000, ftonumn +NaN=$7FFFFFFF, ftonumn -NaN=$80000000" },
            new String[] { "ftonumn 0.0=$0, ftonumn -0.0=$0, ftonumn 0.9=$1, ftonumn -0.9=$FFFFFFFF, ftonumn 1.0=$1, ftonumn -1.0=$FFFFFFFF, ftonumn 1.75=$2, ftonumn -1.75=$FFFFFFFE, ftonumn 2.0=$2, ftonumn -2.0=$FFFFFFFE, ftonumn 10.1=$A, ftonumn -10.1=$FFFFFFF6, ftonumn 999.99995=$3E8, ftonumn -999.99995=$FFFFFC18, ftonumn $1000000=$1000000, ftonumn -$1000000=$FF000000, ftonumn $7FFFFF00=$7FFFFF00, ftonumn -$7FFFFF00=$80000100, ftonumn $80000000=$7FFFFFFF, ftonumn -$80000000=$80000000, ftonumn $90000000=$7FFFFFFF, ftonumn -$90000000=$80000000, ftonumn $C1234500=$7FFFFFFF, ftonumn -$C1234500=$80000000, ftonumn $100000000=$7FFFFFFF, ftonumn -$100000000=$80000000, ftonumn 3.4e+34=$7FFFFFFF, ftonumn -3.4e+34=$80000000, ftonumn +Inf=$7FFFFFFF, ftonumn -Inf=$80000000, ftonumn +NaN=$7FFFFFFF, ftonumn -NaN=$80000000" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseFloatarith() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "floatarith\n" },
            new String[] { "Floating-point arithmetic:" },
            new String[] { "" },
            new String[] { "add(1,1.5)=2.50000, add(0.5,-1.5)=-1.00000, add(-0.5,-1.5)=-2.00000, add(-0.5,1.5)=1.00000, add(0,2.5)=2.50000" },
            new String[] { "sub(1,1.5)=-0.50000, sub(0.5,-1.5)=2.00000, sub(-0.5,-1.5)=1.00000, sub(-0.5,1.5)=-2.00000, sub(0,2.5)=-2.50000" },
            new String[] { "mul(1.25,1.5)=1.87500, mul(0.5,-1.5)=-0.75000, mul(-0.75,-1.5)=1.12500, mul(-0.5,2)=-1.00000, mul(4,2.5)=10.00000" },
            new String[] { "div(1.25,1.5)=0.83333, div(0.5,-1.5)=-0.33333, div(-0.75,-1.5)=0.50000, div(-0.5,2)=-0.25000, div(4,2.5)=1.60000" },
            new String[] { "" },
            new String[] { "add(1,1)=2.00000, add(-1,1)=0.00000, add(-1,-1)=-2.00000, add(1,0)=1.00000, add(-0,1)=1.00000, add(-0,0)=0.00000, add(123,-0)=123.00000, add(0,123)=123.00000, add(1.0000001,-1)=1.19209e-07, add(3.4e38,3.4e38)=Inf, add(-3.4e38,-3.4e38)=-Inf, add(3.4e38,-3.4e38)=0.00000, add(Inf,123)=Inf, add(-Inf,123)=-Inf, add(Inf,Inf)=Inf, add(-Inf,Inf)=NaN" },
            new String[] { "add(1,NaN)=NaN, add(NaN,-0)=NaN, add(Inf,NaN)=NaN, add(-Inf,NaN)=NaN, add(NaN,NaN)=NaN" },
            new String[] { "" },
            new String[] { "sub(1,1)=0.00000, sub(-1,1)=-2.00000, sub(-1,-1)=0.00000, sub(1,0)=1.00000, sub(-0,1)=-1.00000, sub(123,-0)=123.00000, sub(0,123)=-123.00000, sub(1.0000001,1)=1.19209e-07, sub(3.4e38,3.4e38)=0.00000, sub(-3.4e38,-3.4e38)=0.00000, sub(3.4e38,-3.4e38)=Inf, sub(-3.4e38,3.4e38)=-Inf, sub(Inf,123)=Inf, sub(-Inf,123)=-Inf, sub(123,Inf)=-Inf, sub(123,-Inf)=Inf, sub(Inf,-Inf)=Inf, sub(-Inf,Inf)=-Inf, sub(-Inf,-Inf)=NaN, sub(Inf,Inf)=NaN" },
            new String[] { "sub(1,NaN)=NaN, sub(NaN,-0)=NaN, sub(Inf,NaN)=NaN, sub(-Inf,NaN)=NaN, sub(NaN,NaN)=NaN" },
            new String[] { "" },
            new String[] { "mul(1,1)=1.00000, mul(-1,1)=-1.00000, mul(-1,-1)=1.00000, mul(1,0)=0.00000, mul(-0,1)=-0.00000, mul(-0,-1)=0.00000, mul(123,-1)=-123.00000, mul(1,123)=123.00000, mul(3.4e38,2.9e-39)=1.00000, mul(2.9e-39,2.9e-39)=0.00000, mul(-2.9e-39,2.9e-39)=-0.00000, mul(1e20,1e20)=Inf, mul(1e20,-1e20)=-Inf, mul(-1e20,-1e20)=Inf, mul(Inf,0.0001)=Inf, mul(-Inf,0.0001)=-Inf, mul(Inf,Inf)=Inf, mul(-Inf,Inf)=-Inf, mul(-Inf,-Inf)=Inf, mul(Inf,0)=NaN, mul(-0,Inf)=NaN" },
            new String[] { "mul(1,NaN)=NaN, mul(NaN,-0)=NaN, mul(Inf,NaN)=NaN, mul(-Inf,NaN)=NaN, mul(NaN,NaN)=NaN" },
            new String[] { "" },
            new String[] { "div(1,1)=1.00000, div(-1,1)=-1.00000, div(-1,-1)=1.00000, div(1,0)=Inf, div(1,-0)=-Inf, div(-0,1)=-0.00000, div(-0,-1)=0.00000, div(123,-1)=-123.00000, div(123,1)=123.00000, div(3.4e38,2.9e-39)=Inf, div(2.9e-39,2.9e-39)=1.00000, div(-2.9e-39,2.9e-39)=-1.00000, div(1e20,1e20)=1.00000, div(1e20,-1e20)=-1.00000, div(Inf,10000)=Inf, div(-Inf,10000)=-Inf, div(Inf,0)=Inf, div(Inf,-0)=-Inf, div(Inf,Inf)=NaN, div(-Inf,Inf)=NaN, div(0,0)=NaN, div(-0,0)=NaN" },
            new String[] { "div(1,NaN)=NaN, div(NaN,-0)=NaN, div(Inf,NaN)=NaN, div(-Inf,NaN)=NaN, div(NaN,NaN)=NaN" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseFloatmod() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "floatmod\n" },
            new String[] { "Floating-point modulo:" },
            new String[] { "" },
            new String[] { "mod(4.125,2)=rem 0.12500 quo 2.00000, mod(5,1.5)=rem 0.50000 quo 3.00000, mod(7.125,1)=rem 0.12500 quo 7.00000, mod(6,1.75)=rem 0.75000 quo 3.00000, mod(5.125,0.5)=rem 0.12500 quo 10.00000, mod(4,0.75)=rem 0.25000 quo 5.00000" },
            new String[] { "" },
            new String[] { "mod(2.5,1)=rem 0.50000 quo 2.00000, mod(2.5,-1)=rem 0.50000 quo -2.00000, mod(-2.5,1)=rem -0.50000 quo -2.00000, mod(-2.5,-1)=rem -0.50000 quo 2.00000, mod(0,1)=rem 0.00000 quo 0.00000, mod(0,-1)=rem 0.00000 quo -0.00000, mod(-0,1)=rem -0.00000 quo -0.00000, mod(-0,-1)=rem -0.00000 quo 0.00000" },
            new String[] { "" },
            new String[] { "mod(5.125,2)=rem 1.12500 quo 2.00000, mod(5.125,-2)=rem 1.12500 quo -2.00000, mod(-5.125,2)=rem -1.12500 quo -2.00000, mod(-5.125,-2)=rem -1.12500 quo 2.00000" },
            new String[] { "mod(5.125,1)=rem 0.12500 quo 5.00000, mod(5.125,-1)=rem 0.12500 quo -5.00000, mod(-5.125,1)=rem -0.12500 quo -5.00000, mod(-5.125,-1)=rem -0.12500 quo 5.00000" },
            new String[] { "mod(1.5,0.75)=rem 0.00000 quo 2.00000, mod(1.5,-0.75)=rem 0.00000 quo -2.00000, mod(-1.5,0.75)=rem -0.00000 quo -2.00000, mod(-1.5,-0.75)=rem -0.00000 quo 2.00000" },
            new String[] { "" },
            new String[] { "mod(1e-20,1)=rem 1.00000e-20 quo 0.00000, mod(1e20,1)=rem 0.00000 quo 1.00000e+20, mod(8388607.5,1)=rem 0.50000 quo 8.38861e+06, mod(-8388607.5,1)=rem -0.50000 quo -8.38861e+06" },
            new String[] { "mod(2.5e11,1e10)=rem 15360.00000 quo 25.00000, mod(2.5e10,1e10)=rem 5.00000e+09 quo 2.00000, mod(2.5e10,0.0123)=rem 0.00301 quo 2.03252e+12" },
            new String[] { "" },
            new String[] { "mod(0,0)=rem NaN quo NaN, mod(-0,0)=rem NaN quo NaN, mod(1,0)=rem NaN quo NaN, mod(Inf,1)=rem NaN quo NaN, mod(-Inf,1)=rem NaN quo NaN, mod(Inf,Inf)=rem NaN quo NaN, mod(Inf,-Inf)=rem NaN quo NaN, mod(-Inf,Inf)=rem NaN quo NaN, mod(0,1)=rem 0.00000 quo 0.00000, mod(-0,1)=rem -0.00000 quo -0.00000, mod(0,-1)=rem 0.00000 quo -0.00000, mod(-0,-1)=rem -0.00000 quo 0.00000, mod(1,Inf)=rem 1.00000 quo 0.00000, mod(1,-Inf)=rem 1.00000 quo -0.00000, mod(-2,Inf)=rem -2.00000 quo -0.00000, mod(-0.125,Inf)=rem -0.12500 quo -0.00000" },
            new String[] { "" },
            new String[] { "mod(1,NaN)=NaN quo NaN, mod(NaN,-1)=NaN quo NaN, mod(0,NaN)=NaN quo NaN, mod(N0,NaN)=NaN quo NaN, mod(Inf,NaN)=NaN quo NaN, mod(NaN,Inf)=NaN quo NaN, mod(NaN,-Inf)=NaN quo NaN, mod(-Inf,NaN)=NaN quo NaN, mod(NaN,NaN)=NaN quo NaN" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseFloatround() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "floatround\n" },
            new String[] { "Floating-point rounding:" },
            new String[] { "" },
            new String[] { "floor 3.5=3.00000, floor -3.5=-4.00000, floor 3.5=3.00000, floor -3.5=-4.00000, floor 3.5=3.00000, floor -3.5=-4.00000, floor 3.5=3.00000, floor -3.5=-4.00000, floor 3.5=3.00000, floor -3.5=-4.00000" },
            new String[] { "" },
            new String[] { "ceil 3.5=4.00000, ceil -3.5=-3.00000, ceil 3.5=4.00000, ceil -3.5=-3.00000, ceil 3.5=4.00000, ceil -3.5=-3.00000, ceil 3.5=4.00000, ceil -3.5=-3.00000, ceil 3.5=4.00000, ceil -3.5=-3.00000" },
            new String[] { "" },
            new String[] { "floor 0.0=0.00000, floor -0.0=-0.00000, floor 0.9=0.00000, floor -0.9=-1.00000, floor 1.0=1.00000, floor -1.0=-1.00000, floor 1.75=1.00000, floor -1.75=-2.00000, floor 2.0=2.00000, floor -2.0=-2.00000, floor 10.1=10.00000, floor -10.1=-11.00000, floor 999.99995=999.00000, floor -999.99995=-1000.00000, floor $1000000=1.67772e+07, floor -$1000000=-1.67772e+07, floor $7FFFFF00=2.14748e+09, floor -$7FFFFF00=-2.14748e+09, floor $80000000=2.14748e+09, floor -$80000000=-2.14748e+09, floor +Inf=Inf, floor -Inf=-Inf, floor +NaN=NaN, floor -NaN=NaN" },
            new String[] { "" },
            new String[] { "ceil 0.0=0.00000, ceil -0.0=-0.00000, ceil 0.9=1.00000, ceil -0.9=-0.00000, ceil 1.0=1.00000, ceil -1.0=-1.00000, ceil 1.75=2.00000, ceil -1.75=-1.00000, ceil 2.0=2.00000, ceil -2.0=-2.00000, ceil 10.1=11.00000, ceil -10.1=-10.00000, ceil 999.99995=1000.00000, ceil -999.99995=-999.00000, ceil $1000000=1.67772e+07, ceil -$1000000=-1.67772e+07, ceil $7FFFFF00=2.14748e+09, ceil -$7FFFFF00=-2.14748e+09, ceil $80000000=2.14748e+09, ceil -$80000000=-2.14748e+09, ceil +Inf=Inf, ceil -Inf=-Inf, ceil +NaN=NaN, ceil -NaN=NaN" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseFloatexp() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "floatexp\n" },
            new String[] { "Floating-point exponent functions:" },
            new String[] { "" },
            new String[] { "sqrt 2.25=1.50000, sqrt -2.25=NaN, sqrt 2.25=1.50000, sqrt -2.25=NaN, sqrt 2.25=1.50000, sqrt -2.25=NaN, sqrt 2.25=1.50000, sqrt -2.25=NaN" },
            new String[] { "" },
            new String[] { "log e^2=2.00000, log -1.0=NaN, log e^2=2.00000, log -1.0=NaN, log e^2=2.00000, log -1.0=NaN, log e^2=2.00000, log -1.0=NaN" },
            new String[] { "" },
            new String[] { "exp 2.0=7.38906, exp -2.0=0.13534, exp 2.0=7.38906, exp -2.0=0.13534, exp 2.0=7.38906, exp -2.0=0.13534, exp 2.0=7.38906, exp -2.0=0.13534" },
            new String[] { "" },
            new String[] { "pow(1.75,1.5)=2.31503, pow(1.75,-1.5)=0.43196, pow(-1.75,2)=3.06250, pow(-1.75,1.5)=NaN" },
            new String[] { "pow(2.25,2.0)=5.06250, pow(2.25,-2.0)=0.19753, pow(-2.25,3.0)=-11.39063, pow(-2.25,-3.0)=-0.08779" },
            new String[] { "" },
            new String[] { "sqrt 0=0.00000, sqrt -0=-0.00000, sqrt 1=1.00000, sqrt -1=NaN, sqrt 0.6=0.77460, sqrt 100.0000076=10.00000, sqrt 123456789.0=11111.11133, sqrt 9.8765e+35=9.93805e+17, sqrt Inf=Inf, sqrt -Inf=NaN, sqrt +NaN=NaN, sqrt -NaN=NaN" },
            new String[] { "" },
            new String[] { "exp 0=1.00000, exp -0=1.00000, exp 1=2.71828, exp -1=0.36788, exp 0.6=1.82212, exp -0.6=0.54881, exp 88.0=1.65164e+38, exp 100.0=Inf, exp -100.0=3.78352e-44, exp -104.0=0.00000, exp Inf=Inf, exp -Inf=0.00000, exp +NaN=NaN, exp -NaN=NaN" },
            new String[] { "" },
            new String[] { "log 0=-Inf, log -0=-Inf, log 1=0.00000, log -1=NaN, log e=~0.99999, log 0.6=~-0.51083, log 65536=~11.09035, log 123456789.0=~18.63140, log 9.8765e+37=~87.48581, log Inf=Inf, log -Inf=NaN, log +NaN=NaN, log -NaN=NaN" },
            new String[] { "" },
            new String[] { "pow(-1,1)=-1.00000, pow(-1,-1)=-1.00000, pow(-1,1.5)=NaN, pow(0,1)=0.00000, pow(-0,1)=-0.00000, pow(2,127)=1.70141e+38, pow(2,128)=Inf, pow(2,-149)=1.40130e-45, pow(2,-150)=0.00000, pow(2,NaN)=NaN, pow(NaN,2)=NaN, pow(NaN,NaN)=NaN" },
            new String[] { "pow(0,-1)=Inf, pow(-0,-1)=-Inf, pow(0,-2)=Inf, pow(-0,-2)=Inf, pow(0,-1.5)=Inf, pow(-0,-1.5)=Inf, pow(0,1)=0.00000, pow(-0,1)=-0.00000, pow(0,2)=0.00000, pow(-0,2)=0.00000, pow(0,1.5)=0.00000, pow(-0,1.5)=0.00000" },
            new String[] { "pow(-1,Inf)=1.00000, pow(-1,-Inf)=1.00000, pow(1,1)=1.00000, pow(1,-1)=1.00000, pow(1,0)=1.00000, pow(1,Inf)=1.00000, pow(1,-Inf)=1.00000, pow(1,NaN)=1.00000, pow(1,-NaN)=1.00000" },
            new String[] { "pow(4,0)=1.00000, pow(-4,0)=1.00000, pow(0,0)=1.00000, pow(-0,0)=1.00000, pow(Inf,0)=1.00000, pow(-Inf,0)=1.00000, pow(NaN,0)=1.00000, pow(-NaN,0)=1.00000" },
            new String[] { "pow(-1,1.5)=NaN, pow(0.5,-Inf)=Inf, pow(-0.5,-Inf)=Inf, pow(1.5,-Inf)=0.00000, pow(-1.5,-Inf)=0.00000, pow(0.5,Inf)=0.00000, pow(-0.5,Inf)=0.00000, pow(1.5,Inf)=Inf, pow(-1.5,Inf)=Inf, pow(-Inf,-1)=-0.00000, pow(-Inf,-2)=0.00000, pow(-Inf,-1.5)=0.00000, pow(-Inf,1)=-Inf, pow(-Inf,2)=Inf, pow(-Inf,1.5)=Inf, pow(Inf,2)=Inf, pow(Inf,1.5)=Inf, pow(Inf,-2)=0.00000, pow(Inf,-1.5)=0.00000" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseFloattrig() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "floattrig\n" },
            new String[] { "Floating-point trig functions:" },
            new String[] { "" },
            new String[] { "sin(pi/6)=~0.50000, sin(-pi/3)=~-0.86603, sin(pi/4)=~0.70711" },
            new String[] { "cos(pi/6)=~0.86603, cos(-pi/3)=~0.50000, cos(pi/4)=~0.70711" },
            new String[] { "tan(pi/6)=~0.57735, tan(-pi/3)=~-1.73205, tan(pi/4)=1.00000" },
            new String[] { "asin(1/2)=~0.52360, asin(-sqrt(3)/2)=~-1.04720, asin(sqrt(2)/2)=~0.78540" },
            new String[] { "acos(sqrt(3)/2)=~0.52360, acos(-0.5)=~2.09440, acos(sqrt(2)/2)=~0.78540" },
            new String[] { "atan(sqrt(3)/3)=~0.52360, atan(-sqrt(3))=~-1.04720, atan(1)=~0.78540" },
            new String[] { "" },
            new String[] { "sin(0)=0.00000, sin(-0)=-0.00000, sin(pi)=~-8.74228e-08, sin(2pi)=~1.74846e-07, sin(Inf)=NaN, sin(-Inf)=NaN, sin(NaN)=NaN" },
            new String[] { "cos(0)=1.00000, cos(-0)=1.00000, cos(pi)=~-1.00000, cos(2pi)=~1.00000, cos(Inf)=NaN, cos(-Inf)=NaN, cos(NaN)=NaN" },
            new String[] { "tan(0)=0.00000, tan(-0)=-0.00000, tan(pi)=~8.74228e-08, tan(2pi)=~1.74846e-07, tan(Inf)=NaN, tan(-Inf)=NaN, tan(NaN)=NaN" },
            new String[] { "asin(0)=0.00000, asin(-0)=-0.00000, asin(1)=~1.57080, asin(-1)=~-1.57080, asin(2)=NaN, asin(-2)=NaN, asin(Inf)=NaN, asin(-Inf)=NaN, asin(NaN)=NaN" },
            new String[] { "acos(1)=0.00000, acos(-1)=~3.14159, acos(0)=~1.57080, acos(-0)=~1.57080, acos(2)=NaN, acos(-2)=NaN, acos(Inf)=NaN, acos(-Inf)=NaN, acos(NaN)=NaN" },
            new String[] { "atan(0)=0.00000, atan(-0)=-0.00000, atan(1)=~0.78540, atan(-1)=~-0.78540, atan(Inf)=~1.57080, atan(-Inf)=~-1.57080, atan(NaN)=NaN" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseFloatatan2() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "floatatan2\n" },
            new String[] { "Floating-point atan2 function:" },
            new String[] { "" },
            new String[] { "atan2(1,1)=~0.78540, atan2(1,-1)=~2.35619, atan2(-1,-1)=~-2.35619, atan2(-1,1)=~-0.78540" },
            new String[] { "" },
            new String[] { "atan2(1,2)=~0.46365, atan2(2,-0.5)=~1.81578, atan2(-0.125,-8)=~-3.12597, atan2(-2,3)=~-0.58800" },
            new String[] { "" },
            new String[] { "atan2(0,0)=0.00000, atan2(-0,0)=-0.00000, atan2(0,-0)=~3.14159, atan2(-0,-0)=~-3.14159, atan2(0,1)=0.00000, atan2(-0,1)=-0.00000, atan2(0,-1)=~3.14159, atan2(-0,-1)=~-3.14159, atan2(1,0)=~1.57080, atan2(1,-0)=~1.57080, atan2(-1,0)=~-1.57080, atan2(-1,-0)=~-1.57080" },
            new String[] { "atan2(1,Inf)=0.00000, atan2(-1,Inf)=-0.00000, atan2(1,-Inf)=~3.14159, atan2(-1,-Inf)=~-3.14159, atan2(Inf,Inf)=~0.78540, atan2(-Inf,Inf)=~-0.78540, atan2(Inf,-Inf)=~2.35619, atan2(-Inf,-Inf)=~-2.35619" },
            new String[] { "atan2(1,NaN)=NaN, atan2(NaN,-0)=NaN, atan2(Inf,NaN)=NaN, atan2(-Inf,NaN)=NaN, atan2(NaN,NaN)=NaN" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseFjumpform() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "fjumpform\n" },
            new String[] { "Floating-point jump with various operand forms:" },
            new String[] { "" },
            new String[] { "Test A0=33, Test A1=44, Test A2=33, Test A3=44, Test A4=33, Test A5=44" },
            new String[] { "Test B0=11, Test B1=22, Test B2=11, Test B3=22, Test B4=11, Test B5=22" },
            new String[] { "Test C0=55, Test C1=66, Test C2=55, Test C3=66, Test C4=55, Test C5=66" },
            new String[] { "Test E0=0, E1=1, E2=99" },
            new String[] { "Test F0=2, F1=3, F2=9, F3=5, F4=2, F5=1, F6=0" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseFjump() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "fjump\n" },
            new String[] { "Floating-point equality comparisons:" },
            new String[] { "" },
            new String[] { "jisnan(0)=0, jisnan(-0)=0, jisnan(1)=0, jisnan(3.4e38)=0, jisnan(Inf)=0, jisnan(-Inf)=0, jisnan(NaN)=1, jisnan(-NaN)=1, jisnan(other NaN)=1, jisnan(other -NaN)=1" },
            new String[] { "jisinf(0)=0, jisinf(-0)=0, jisinf(1)=0, jisinf(3.4e+38)=0, jisinf(Inf)=1, jisinf(-Inf)=1, jisinf(NaN)=0, jisinf(-NaN)=0, jisinf(other NaN)=0, jisinf(other -NaN)=0" },
            new String[] { "jfeq(0,0,0)=1, jfeq(0,-0,0)=1, jfeq(0,0,-0)=1, jfeq(0,1.4e-45,0)=0, jfeq(0,-1.4e-45,0)=0, jfeq(3.4e+38,3.4e+38,0)=1, jfeq(3.4e+38,3.4e+38,0)=0, jfeq(Inf,Inf,0)=1, jfeq(-Inf,-Inf,0)=1, jfeq(Inf,-Inf,0)=0" },
            new String[] { "jfeq(0,0,1.4e-45)=1, jfeq(0,-0,1.4e-45)=1, jfeq(0,0,-1.4e-45)=1, jfeq(0,1.4e-45,1.4e-45)=1, jfeq(0,-1.4e-45,1.4e-45)=1, jfeq(0,1.4e-45,-1.4e-45)=1, jfeq(0,2.8e-45,1.4e-45)=0, jfeq(3.4e+38,3.4e+38,1.4e-45)=1, jfeq(3.4e+38,3.4e+38,1.4e-45)=0, jfeq(Inf,Inf,1.4e-45)=1, jfeq(-Inf,-Inf,1.4e-45)=1, jfeq(Inf,-Inf,1.4e-45)=0" },
            new String[] { "jfeq(0,0,1)=1, jfeq(0,-2,1)=0, jfeq(0,-2,1.5)=0, jfeq(0,-2,2)=1, jfeq(0,-2,-2)=1, jfeq(1.5,2,1.5)=1, jfeq(1.5,3,1.5)=1, jfeq(1.5,3+,1.5)=0" },
            new String[] { "jfeq(0,3.4e+38,3.4e+38-)=0, jfeq(0,3.4e+38,3.4e+38)=1, jfeq(-1,3.4e+38,3.4e+38)=1, jfeq(-3.4e+38,3.4e+38,3.4e+38)=0,jfeq(Inf,3.4e+38,3.4e+38)=0, jfeq(-Inf,-3.4e+38,3.4e+38)=0, jfeq(Inf,Inf,3.4e+38)=1, jfeq(-Inf,Inf,3.4e+38)=0" },
            new String[] { "jfeq(0,0,Inf)=1, jfeq(0,3.4e+38,Inf)=1, jfeq(0,3.4e+38,-Inf)=1, jfeq(0,-3.4e+38,-Inf)=1, jfeq(-3.4e+38,3.4e+38,Inf)=1, jfeq(-3.4e+38,3.4e+38,-Inf)=1, jfeq(0,Inf,Inf)=1, jfeq(-3.4e+38,-Inf,Inf)=1, jfeq(0,-Inf,Inf)=1, jfeq(-Inf,-Inf,Inf)=1, jfeq(Inf,-Inf,Inf)=0" },
            new String[] { "jfeq(NaN,0,0)=0, jfeq(0,NaN,0)=0, jfeq(0,0,NaN)=0, jfeq(0,NaN,NAN)=0, jfeq(NaN,0,NaN)=0, jfeq(NaN,NaN,0)=0, jfeq(NaN,NaN,NaN)=0, jfeq(Inf,Inf,NaN)=0, jfeq(Inf,-Inf,NaN)=0, jfeq(Inf,0,NaN)=0, jfeq(0,NaN,Inf)=0, jfeq(NaN,NaN,Inf)=0" },
            new String[] { "jfne(0,0,0)=0, jfne(0,-0,0)=0, jfne(0,0,-0)=0, jfne(0,1.4e-45,0)=1, jfne(0,-1.4e-45,0)=1, jfne(3.4e+38,3.4e+38,0)=0, jfne(3.4e+38,3.4e+38,0)=1, jfne(Inf,Inf,0)=0, jfne(-Inf,-Inf,0)=0, jfne(Inf,-Inf,0)=1" },
            new String[] { "jfne(0,0,1.4e-45)=0, jfne(0,-0,1.4e-45)=0, jfne(0,0,-1.4e-45)=0, jfne(0,1.4e-45,1.4e-45)=0, jfne(0,-1.4e-45,1.4e-45)=0, jfne(0,1.4e-45,-1.4e-45)=0, jfne(0,2.8e-45,1.4e-45)=1, jfne(3.4e+38,3.4e+38,1.4e-45)=0, jfne(3.4e+38,3.4e+38,1.4e-45)=1, jfne(Inf,Inf,1.4e-45)=0, jfne(-Inf,-Inf,1.4e-45)=0, jfne(Inf,-Inf,1.4e-45)=1" },
            new String[] { "jfne(0,0,1)=0, jfne(0,-2,1)=1, jfne(0,-2,1.5)=1, jfne(0,-2,2)=0, jfne(0,-2,-2)=0, jfne(1.5,2,1.5)=0, jfne(1.5,3,1.5)=0, jfne(1.5,3+,1.5)=1" },
            new String[] { "jfne(0,3.4e+38,3.4e+38-)=1, jfne(0,3.4e+38,3.4e+38)=0, jfne(-1,3.4e+38,3.4e+38)=0, jfne(-3.4e+38,3.4e+38,3.4e+38)=1,jfne(Inf,3.4e+38,3.4e+38)=1, jfne(-Inf,-3.4e+38,3.4e+38)=1, jfne(Inf,Inf,3.4e+38)=0, jfne(-Inf,Inf,3.4e+38)=1" },
            new String[] { "jfne(0,0,Inf)=0, jfne(0,3.4e+38,Inf)=0, jfne(0,3.4e+38,-Inf)=0, jfne(0,-3.4e+38,-Inf)=0, jfne(-3.4e+38,3.4e+38,Inf)=0, jfne(-3.4e+38,3.4e+38,-Inf)=0, jfne(0,Inf,Inf)=0, jfne(-3.4e+38,-Inf,Inf)=0, jfne(0,-Inf,Inf)=0, jfne(-Inf,-Inf,Inf)=0, jfne(Inf,-Inf,Inf)=1" },
            new String[] { "jfne(NaN,0,0)=1, jfne(0,NaN,0)=1, jfne(0,0,NaN)=1, jfne(0,NaN,NAN)=1, jfne(NaN,0,NaN)=1, jfne(NaN,NaN,0)=1, jfne(NaN,NaN,NaN)=1, jfne(Inf,Inf,NaN)=1, jfne(Inf,-Inf,NaN)=1, jfne(Inf,0,NaN)=1, jfne(0,NaN,Inf)=1, jfne(NaN,NaN,Inf)=1" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseFcompare() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "fcompare\n" },
            new String[] { "Floating-point inequality comparisons:" },
            new String[] { "" },
            new String[] { "jflt(0,0)=0, jflt(0,1)=1, jflt(0,-1)=0, jflt(-0,0)=0, jflt(-0,1)=1, jflt(-0,-1)=0, jflt(-0,-0)=0, jflt(1,1)=0, jflt(pi,pi)=0, jflt(0,1.4e-45)=1, jflt(0,-1.4e-45)=0, jflt(-1.4e-45,-0)=1, jflt(1.4e-45,3.4e+38)=1, jflt(1.4e-45,-3.4e+38)=0" },
            new String[] { "jflt(0,Inf)=1, jflt(0,-Inf)=0, jflt(3.4e+38,Inf)=1, jflt(3.4e+38,-Inf)=0, jflt(-Inf,-3.4e+38)=1, jflt(-Inf,Inf)=1, jflt(Inf,-Inf)=0, jflt(Inf,Inf)=0, jflt(-Inf,-Inf)=0" },
            new String[] { "jflt(0,NaN)=0, jflt(NaN,0)=0, jflt(Inf,NaN)=0, jflt(-Inf,NaN)=0, jflt(NaN,Inf)=0, jflt(NaN,-Inf)=0, jflt(NaN,NaN)=0, jflt(-NaN,NaN)=0" },
            new String[] { "jfle(0,0)=1, jfle(0,1)=1, jfle(0,-1)=0, jfle(-0,0)=1, jfle(-0,1)=1, jfle(-0,-1)=0, jfle(-0,-0)=1, jfle(1,1)=1, jfle(pi,pi)=1, jfle(0,1.4e-45)=1, jfle(0,-1.4e-45)=0, jfle(-1.4e-45,-0)=1, jfle(1.4e-45,3.4e+38)=1, jfle(1.4e-45,-3.4e+38)=0" },
            new String[] { "jfle(0,Inf)=1, jfle(0,-Inf)=0, jfle(3.4e+38,Inf)=1, jfle(3.4e+38,-Inf)=0, jfle(-Inf,-3.4e+38)=1, jfle(-Inf,Inf)=1, jfle(Inf,-Inf)=0, jfle(Inf,Inf)=1, jfle(-Inf,-Inf)=1" },
            new String[] { "jfle(0,NaN)=0, jfle(NaN,0)=0, jfle(Inf,NaN)=0, jfle(-Inf,NaN)=0, jfle(NaN,Inf)=0, jfle(NaN,-Inf)=0, jfle(NaN,NaN)=0, jfle(-NaN,NaN)=0" },
            new String[] { "jfgt(0,0)=0, jfgt(0,1)=0, jfgt(0,-1)=1, jfgt(-0,0)=0, jfgt(-0,1)=0, jfgt(-0,-1)=1, jfgt(-0,-0)=0, jfgt(1,1)=0, jfgt(pi,pi)=0, jfgt(0,1.4e-45)=0, jfgt(0,-1.4e-45)=1, jfgt(-1.4e-45,-0)=0, jfgt(1.4e-45,3.4e+38)=0, jfgt(1.4e-45,-3.4e+38)=1" },
            new String[] { "jfgt(0,Inf)=0, jfgt(0,-Inf)=1, jfgt(3.4e+38,Inf)=0, jfgt(3.4e+38,-Inf)=1, jfgt(-Inf,-3.4e+38)=0, jfgt(-Inf,Inf)=0, jfgt(Inf,-Inf)=1, jfgt(Inf,Inf)=0, jfgt(-Inf,-Inf)=0" },
            new String[] { "jfgt(0,NaN)=0, jfgt(NaN,0)=0, jfgt(Inf,NaN)=0, jfgt(-Inf,NaN)=0, jfgt(NaN,Inf)=0, jfgt(NaN,-Inf)=0, jfgt(NaN,NaN)=0, jfgt(-NaN,NaN)=0" },
            new String[] { "jfge(0,0)=1, jfge(0,1)=0, jfge(0,-1)=1, jfge(-0,0)=1, jfge(-0,1)=0, jfge(-0,-1)=1, jfge(-0,-0)=1, jfge(1,1)=1, jfge(pi,pi)=1, jfge(0,1.4e-45)=0, jfge(0,-1.4e-45)=1, jfge(-1.4e-45,-0)=0, jfge(1.4e-45,3.4e+38)=0, jfge(1.4e-45,-3.4e+38)=1" },
            new String[] { "jfge(0,Inf)=0, jfge(0,-Inf)=1, jfge(3.4e+38,Inf)=0, jfge(3.4e+38,-Inf)=1, jfge(-Inf,-3.4e+38)=0, jfge(-Inf,Inf)=0, jfge(Inf,-Inf)=1, jfge(Inf,Inf)=1, jfge(-Inf,-Inf)=1" },
            new String[] { "jfge(0,NaN)=0, jfge(NaN,0)=0, jfge(Inf,NaN)=0, jfge(-Inf,NaN)=0, jfge(NaN,Inf)=0, jfge(NaN,-Inf)=0, jfge(NaN,NaN)=0, jfge(-NaN,NaN)=0" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseFprint() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "fprint\n" },
            new String[] { "Print floating-point numbers:" },
            new String[] { "Note: this does not test an opcode. It tests the FloatExp function, which is included in this test suite. You are welcome to use that function in your Glulx program or library." },
            new String[] { "" },
            new String[] { "\"0.00000e+00\" len 11, \"-0.00000e+00\" len 12, \"1.00000e+00\" len 11, \"-1.00000e+00\" len 12" },
            new String[] { "\"1.00000e-01\" len 11, \"3.33333e-02\" len 11, \"2.00000e+00\" len 11, \"1.00000e+02\" len 11, \"1.00000e+02\" len 11, \"1.00000e+02\" len 11, \"9.99999e+01\" len 11, \"1.25000e+02\" len 11" },
            new String[] { "\"3.00000e-30\" len 11, \"6.99998e+30\" len 11, \"3.00004e-40\" len 11, \"6.99998e+34\" len 11" },
            new String[] { "\"1.0e+00\" len 7, \"1.0000e+00\" len 10, \"1.00000024e+00\" len 14, \"6.8e+00\" len 7, \"6.7898e+00\" len 10, \"6.78979520e+00\" len 14" },
            new String[] { "\"Inf\" len 3, \"-Inf\" len 4, \"NaN\" len 3, \"-NaN\" len 4" },
            new String[] { "" },
            new String[] { "\"0.00000\" len 7, \"-0.00000\" len 8, \"1.00000\" len 7, \"-1.00000\" len 8" },
            new String[] { "\"0.10000\" len 7, \"0.02000\" len 7, \"0.03333\" len 7, \"12.34568\" len 8, \"-120.34568\" len 10, \"100000.34375\" len 12, \"1000000.37500\" len 13, \"10000000.00000\" len 14" },
            new String[] { "\"0.00000\" len 7, \"-0.00000\" len 8, \"4294965440.00000\" len 16, \"1000000240000000000000000000000.00000\" len 37" },
            new String[] { "\"1.0\" len 3, \"1.0000\" len 6, \"1.00000000\" len 10, \"6.8\" len 3, \"6.7898\" len 6, \"6.78978832\" len 10" },
            new String[] { "\"Inf\" len 3, \"-Inf\" len 4, \"NaN\" len 3, \"-NaN\" len 4" },
            new String[] { "" },
            new String[] { "\"1.00\" len 4, \"1.00000\" len 7, \"999999.00000\" len 12, \"-999999.00000\" len 13, \"1.00001e+06\" len 11, \"-1.00001e+06\" len 12, \"0.00010\" len 7, \"-0.00010\" len 8, \"9.0000e-05\" len 10, \"-9.0000e-05\" len 11" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void glulxerciseSafari5() throws Exception {
        testFile("/glulxercise.ulx", glulxerciseIntro, glulxerciseOutro, new String[][] {
            new String[] { ">", "safari5\n" },
            new String[] { "Safari 5 bug:" },
            new String[] { "This tests for a known Javascript bug in Safari 5, MacOS 10.5.8, Intel (not PPC). You should only see this fail on Quixe on that browser setup. This failure does not represent a Glulx error. I just want to be able to track it." },
            new String[] { "" },
            new String[] { "Folded: $FFFFFF" },
            new String[] { "Stack: $FFFFFF" },
            new String[] { "Locals: $FFFFFF" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }
}
