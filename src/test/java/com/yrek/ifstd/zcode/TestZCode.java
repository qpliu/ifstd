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
        ZCode zcode = new ZCode(new File(getClass().getResource(file).toURI()), new GlkDispatch(glk)).initGlk(0,0);
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

    @Test
    public void praxixXyzzy() throws Exception {
        testFile("/praxix.z5", praxixIntro, praxixOutro, new String[][] {
            new String[] { ">", "xyzzy\n" },
            new String[] { "I don't understand that command." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void praxixLook() throws Exception {
        testFile("/praxix.z5", praxixIntro, praxixOutro, new String[][] {
            new String[] { ">", "look\n" },
            new String[] { "A voice booooms out: Welcome to the test chamber." },
            new String[] { "" },
            new String[] { "Type \"help\" to repeat this message, \"quit\" to exit, \"all\" to run all tests, or one of the following test options: \"operand\", \"arith\", \"comarith\", \"bitwise\", \"shift\", \"inc\", \"incchk\", \"array\", \"undo\", \"multiundo\"." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

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

    @Test
    public void praxixArith() throws Exception {
        testFile("/praxix.z5", praxixIntro, praxixOutro, new String[][] {
            new String[] { ">", "arith\n" },
            new String[] { "Integer arithmetic:" },
            new String[] { "" },
            new String[] { "2+2=4, -2+-3=-5, 3+-4=-1, -4+5=1, $7FFF+$7FFE=-3, $8000+$8000=0" },
            new String[] { "Globals 6+8=14, $7FFE+$7FFD=-5" },
            new String[] { "2-2=0, -2-3=-5, 3-4=-1, -4-(-5)=1, $7FFF-$7FFE=1, $8000-$8001=-1, $7FFF-$8001=-2" },
            new String[] { "Globals 6-8=-2, $7FFD-$7FFE=-1" },
            new String[] { "2*2=4, -2*-3=6, 3*-4=-12, -4*5=-20, $100*$100 (trunc)=0, 311*373 (trunc)=$C523" },
            new String[] { "Globals -6*-8=48, Globals -311*373=15069" },
            new String[] { "12/3=4, 11/2=5, -11/2=-5, 11/-2=-5, -11/-2=5, $7fff/2=$3FFF, $7fff/-2=$C001, -$7fff/2=$C001, -$7fff/-2=$3FFF, $8000/2=$C000, $8000/(-2)=$4000, $8000/1=$8000" },
            new String[] { "Globals -48/-8=6, 48/7=6, 48/-7=-6, -48/7=-6, -48/-7=6" },
            new String[] { "12%3=0, 13%5=3, -13%5=-3, 13%-5=3, -13%-5=-3, $7fff%11=9, -$7fff%11=-9, $7fff%-11=9, -$7fff%-11=-9, $8000%7=-1, $8000%-7=-1, $8000%2=0, $8000%-2=0, $8000%1=0" },
            new String[] { "Globals 49%8=1, 49%-8=1, -49%8=-1, -49%-8=-1" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void praxixComarith() throws Exception {
        testFile("/praxix.z5", praxixIntro, praxixOutro, new String[][] {
            new String[] { ">", "comarith\n" },
            new String[] { "Compound arithmetic expressions:" },
            new String[] { "" },
            new String[] { "(7+2)*-4=-36" },
            new String[] { "($7FFF+2)/16=-2047" },
            new String[] { "(-$7FFF+-2)/16=2047" },
            new String[] { "(-26103+-32647)/9=754" },
            new String[] { "(-$7FFF-2)/16=2047" },
            new String[] { "($7FFF--2)/16=-2047" },
            new String[] { "(-26103-32647)/9=754" },
            new String[] { "($100*$100)/16+1=1" },
            new String[] { "(311*373)/16=-941" },
            new String[] { "(311*-373)/16=941" },
            new String[] { "(111*373)/16=-1508" },
            new String[] { "(111*-373)/16=1508" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void praxixBitwise() throws Exception {
        testFile("/praxix.z5", praxixIntro, praxixOutro, new String[][] {
            new String[] { ">", "bitwise\n" },
            new String[] { "Bitwise arithmetic:" },
            new String[] { "" },
            new String[] { "0&0=$0, $FFFF&0=$0, $FFFF&$FFFF=$FFFF, $013F&$F310=$110, $F731&$137F=$1331, $35&56=$14" },
            new String[] { "0|0=$0, $FFFF|0=$FFFF, $FFFF|$FFFF=$FFFF, $3700|$0012=$3712, $35|56=$77" },
            new String[] { "!0=$FFFF, !1=$FFFE, !$F=$FFF0, !$7FFF=$8000, !$8000=$7FFF, !$FFFD=$2" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void praxixShift() throws Exception {
        testFile("/praxix.z5", praxixIntro, praxixOutro, new String[][] {
            new String[] { ">", "shift\n" },
            new String[] { "Bit shifts:" },
            new String[] { "" },
            new String[] { "$11u<<0=$11, $11u<<1=$22, $11u<<4=$110, $11u<<10=$4400, $11u<<15=$8000, $11u<<16=$0, -2u<<0=-2, -2u<<1=-4, -2u<<7=-256, -2u<<15=0" },
            new String[] { "1u<<0=$1, 1u<<1=$2, 1u<<2=$4, 1u<<3=$8, 1u<<4=$10, 1u<<5=$20, 1u<<6=$40, 1u<<7=$80, 1u<<8=$100, 1u<<9=$200, 1u<<10=$400, 1u<<11=$800, 1u<<12=$1000, 1u<<13=$2000, 1u<<14=$4000, 1u<<15=$8000, 1u<<16=0" },
            new String[] { "$4001u>>-0=$4001, $4001u>>-1=$2000, $4001u>>-6=$100, $4001u>>-11=$8, $4001u>>-15=$0, $4001u>>-16=$0" },
            new String[] { "$7FFFu>>-0=$7FFF, $7FFFu>>-1=$3FFF, $7FFFu>>-2=$1FFF, $7FFFu>>-6=$1FF, $7FFFu>>-12=$7, $7FFFu>>-15=$0, $7FFFu>>-16=$0" },
            new String[] { "-1u>>-0=$FFFF, -1u>>-1=$7FFF, -1u>>-2=$3FFF, -1u>>-6=$3FF, -1u>>-12=$F, -1u>>-13=$7, -1u>>-15=$1, -1u>>-16=$0, -1u>>-17=$0" },
            new String[] { "-1u>>-1=$7FFF, -1u>>-2=$3FFF, -1u>>-3=$1FFF, -1u>>-4=$FFF, -1u>>-5=$7FF, -1u>>-6=$3FF, -1u>>-7=$1FF, -1u>>-8=$FF, -1u>>-9=$7F, -1u>>-10=$3F, -1u>>-11=$1F, -1u>>-12=$F, -1u>>-13=$7, -1u>>-14=$3, -1u>>-15=$1, -1u>>-16=0" },
            new String[] { "$11s<<0=$11, $11s<<1=$22, $11s<<4=$110, $11s<<10=$4400, $11s<<15=$8000, $11s<<16=$0, -2s<<0=-2, -2s<<1=-4, -2s<<7=-256, -2s<<15=0" },
            new String[] { "1s<<0=$1, 1s<<1=$2, 1s<<2=$4, 1s<<3=$8, 1s<<4=$10, 1s<<5=$20, 1s<<6=$40, 1s<<7=$80, 1s<<8=$100, 1s<<9=$200, 1s<<10=$400, 1s<<11=$800, 1s<<12=$1000, 1s<<13=$2000, 1s<<14=$4000, 1s<<15=$8000, 1s<<16=0" },
            new String[] { "$4001s>>-0=$4001, $4001s>>-1=$2000, $4001s>>-6=$100, $4001s>>-11=$8, $4001s>>-15=$0, $4001s>>-16=$0" },
            new String[] { "$7FFFs>>-0=$7FFF, $7FFFs>>-1=$3FFF, $7FFFs>>-2=$1FFF, $7FFFs>>-6=$1FF, $7FFFs>>-12=$7, $7FFFs>>-13=$3, $7FFFs>>-14=$1, $7FFFs>>-15=$0, $7FFFs>>-16=$0" },
            new String[] { "-1s>>-0=-1, -1s>>-1=-1, -1s>>-15=-1, -1s>>-16=-1, -1s>>-17=-1" },
            new String[] { "-1000s>>-0=-1000, -1000s>>-1=-500, -1000s>>-2=-250, -1000s>>-4=-63, -1000s>>-6=-16, -1000s>>-9=-2, -1000s>>-15=-1, -1000s>>-16=-1, -1000s>>-17=-1" },
            new String[] { "-1s>>0=-1, -1s>>-1=-1, -1s>>-2=-1, -1s>>-3=-1, -1s>>-4=-1, -1s>>-5=-1, -1s>>-6=-1, -1s>>-7=-1, -1s>>-8=-1, -1s>>-9=-1, -1s>>-10=-1, -1s>>-11=-1, -1s>>-12=-1, -1s>>-13=-1, -1s>>-14=-1, -1s>>-15=-1, -1s>>-16=-1" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void praxixInc() throws Exception {
        testFile("/praxix.z5", praxixIntro, praxixOutro, new String[][] {
            new String[] { ">", "inc\n" },
            new String[] { "Increment/decrement:" },
            new String[] { "" },
            new String[] { "0++=1, 1++=2, -1++=0, -10++=-9, $7FFF++=$8000, $C000++=$C001" },
            new String[] { "0++=1, 1++=2, -1++=0, -10++=-9, $7FFF++=$8000, $C000++=$C001" },
            new String[] { "0++=1, 1++=2, -1++=0, -10++=-9, $7FFF++=$8000, $C000++=$C001" },
            new String[] { "0--=-1, 1--=0, -1--=-2, 10--=9, $8000--=$7FFF, $C000--=$BFFF" },
            new String[] { "0--=-1, 1--=0, -1--=-2, 10--=9, $8000--=$7FFF, $C000--=$BFFF" },
            new String[] { "0--=-1, 1--=0, -1--=-2, 10--=9, $8000--=$7FFF, $C000--=$BFFF" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void praxixIncchk() throws Exception {
        testFile("/praxix.z5", praxixIntro, praxixOutro, new String[][] {
            new String[] { ">", "incchk\n" },
            new String[] { "Increment/decrement and branch:" },
            new String[] { "" },
            new String[] { "++0=1,1, ++1=2,1, ++-1=0,0, ++100=101,1, ++-10=-9,0, ++$7FFF=$8000,0, ++$C000=$C001,0" },
            new String[] { "++0=1,1, ++1=2,1, ++-1=0,0, ++100=101,1, ++-10=-9,0, ++$7FFF=$8000,0, ++$C000=$C001,0" },
            new String[] { "++0=1,1, ++1=2,1, ++-1=0,0, ++100=101,1, ++-10=-9,0, ++$7FFF=$8000,0, ++$C000=$C001,0" },
            new String[] { "--0=-1,1, --1=0,0, ---1=-2,1, --100=99,0, ---10=-11,1, --$8000=$7FFF,0, --$C000=$BFFF,1" },
            new String[] { "--0=-1,1, --1=0,0, ---1=-2,1, --100=99,0, ---10=-11,1, --$8000=$7FFF,0, --$C000=$BFFF,1" },
            new String[] { "--0=-1,1, --1=0,0, ---1=-2,1, --100=99,0, ---10=-11,1, --$8000=$7FFF,0, --$C000=$BFFF,1" },
            new String[] { "++50=51,0, ++70=71,1, ++-50=-49,1, ++-70=-69,0, ++-50=-49,0, ++50=51,1" },
            new String[] { "--50=49,1, --70=69,0, ---50=-51,0, ---70=-71,1, ---50=-51,1, --50=49,0" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void praxixArray() throws Exception {
        testFile("/praxix.z5", praxixIntro, praxixOutro, new String[][] {
            new String[] { ">", "array\n" },
            new String[] { "Array loads and stores:" },
            new String[] { "" },
            new String[] { "a-->0=$1357, a-->0=$1357, a-->1=$FDB9, a-->1=$FDB9, a-->2=$11, a-->2=$11, a-->3=$FFEE, a-->3=$FFEE" },
            new String[] { "a+3-->-1=$57FD, a+3-->0=$B900, a+3-->1=$11FF, a+3-->-1=$57FD, a+3-->0=$B900, a+3-->1=$11FF" },
            new String[] { "a->0=$13, a->0=$13, a->1=$57, a->1=$57, a->2=$FD, a->2=$FD, a->3=$B9, a->3=$B9" },
            new String[] { "a+3->-1=$FD, a+3->0=$B9, a+3->1=$0, a+3->-1=$FD, a+3->0=$B9, a+3->1=$0" },
            new String[] { "a-->0=$1201, a-->0=$2302, a-->1=$3403, a-->1=$4504, a-->2=$5605, a-->2=$6706, a-->3=$7807, a-->3=$8908" },
            new String[] { "a-->-1=$AB0A, a-->0=$BC0B, a-->1=$CD0C, a-->-1=$BA1B, a-->0=$CB1C, a-->1=$DC1D" },
            new String[] { "a->0=$12, a->0=$23, a->1=$34, a->1=$45, a->2=$56, a->2=$67, a->3=$78, a->3=$89" },
            new String[] { "a->-1=$AB, a->0=$BC, a->1=$CD, a->-1=$BA, a->0=$CB, a->1=$DC" },
            new String[] { "$F1 concat $E2 = $F1E2" },
            new String[] { "$9876 = $98 concat $76" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void praxixUndo() throws Exception {
        testFile("/praxix.z5", praxixIntro, praxixOutro, new String[][] {
            new String[] { ">", "undo\n" },
            new String[] { "Undo:" },
            new String[] { "" },
            new String[] { "Interpreter claims to support undo." },
            new String[] { "" },
            new String[] { "Undo saved..." },
            new String[] { "Restoring undo..." },
            new String[] { "Undo succeeded, return value 2." },
            new String[] { "loc=99 glob=999" },
            new String[] { "Undo saved..." },
            new String[] { "Restoring undo..." },
            new String[] { "Undo succeeded, return value 2." },
            new String[] { "loc=98 glob=998" },
            new String[] { "Undo saved..." },
            new String[] { "Restoring undo..." },
            new String[] { "Undo succeeded, return value 2." },
            new String[] { "loc=97 glob=997" },
            new String[] { "Undo saved..." },
            new String[] { "Restoring undo..." },
            new String[] { "Undo succeeded, return value 2." },
            new String[] { "loc=98 glob=998" },
            new String[] { "Undo saved..." },
            new String[] { "guard=9" },
            new String[] { "Restoring undo..." },
            new String[] { "Undo succeeded, return value 2." },
            new String[] { "loc=99 glob=999 glob2=-999" },
            new String[] { "guard=9" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void praxixMultiundo() throws Exception {
        testFile("/praxix.z5", praxixIntro, praxixOutro, new String[][] {
            new String[] { ">", "multiundo\n" },
            new String[] { "Multi-level undo:" },
            new String[] { "(Note: this capability is not required by the spec.)" },
            new String[] { "" },
            new String[] { "Interpreter claims to support undo." },
            new String[] { "" },
            new String[] { "Undo 1 saved..." },
            new String[] { "Undo 2 saved..." },
            new String[] { "Restoring undo 2..." },
            new String[] { "Undo 2 succeeded, return value 2." },
            new String[] { "loc=77 glob=777" },
            new String[] { "Restoring undo 1..." },
            new String[] { "Undo 1 succeeded, return value 2." },
            new String[] { "loc=99 glob=999" },
            new String[] { "" },
            new String[] { "Passed." },
            new String[] { "" },
            new String[] { ">", "quit\n" },
        }, null, null);
    }

    @Test
    public void czech() throws Exception {
        testFile("/czech.z5", new String[] {
            "CZECH: the Comprehensive Z-machine Emulation CHecker, version 0.8",
            "Test numbers appear in [brackets].",
            "",
            "print works or you wouldn't be seeing this.",
            "",
            "Jumps [2]: jump.je..........jg.......jl.......jz...offsets..",
            "Variables [32]: push/pull..store.load.dec.......inc.......",
            "    dec_chk...........inc_chk.........",
            "Arithmetic ops [70]: add.......sub.......",
            "    mul........div...........mod...........",
            "Logical ops [114]: not....and.....or.....art_shift........log_shift........",
            "Memory [144]: loadw.loadb..storeb..storew...",
            "Subroutines [152]: call_1s.call_2s..call_vs2...call_vs.....ret.",
            "    call_1n.call_2n..call_vn..call_vn2..",
            "    rtrue.rfalse.ret_popped.",
            "    Computed call...",
            "    check_arg_count................",
            "Objects [193]: get_parent....get_sibling.......get_child......jin.......",
            "    test_attr......set_attr....clear_attr....set/clear/test_attr..",
            "    get_next_prop......get_prop_len/get_prop_addr....",
            "    get_prop..........put_prop ..........",
            "    remove..insert.......",
            "    Spec1.0 length-64 props...........",
            "Indirect Opcodes [283]: load..................store.........................",
            "    pull...............inc...............dec...............",
            "    inc_chk...............dec_chk...............",
            "Misc [401]: test...random.verify.piracy.",
            "Header (No tests)",
            "    standard 1.1 ",
            "    interpreter 1 1 (DECSystem-20)",
            "    Flags on: ",
            "    Flags off: color, pictures, boldface, italic, fixed-space, sound, timer, transcripting on, fixed-pitch on, redraw pending, using pictures, using undo, using mouse, using colors, using sound, using menus, ",
            "    Screen size: 80x24; in 1x1 units: 80x24",
            "    Default color: current on current",
            "",
            "",
            "",
            "Print opcodes [407]: Tests should look like... '[Test] opcode (stuff): stuff'",
            "print_num (0, 1, -1, 32767,-32768, -1): 0, 1, -1, 32767, -32768, -1",
            "[413] print_char (abcd): abcd",
            "[417] new_line:",
            "",
            "There should be an empty line above this line.",
            "print_ret (should have newline after this)",
            ".",
            "print_addr (Hello.): Hello.",
            "",
            "print_paddr (A long string that Inform will put in high memory):",
            "A long string that Inform will put in high memory",
            "Abbreviations (I love 'xyzzy' [two times]): I love 'xyzzy'  I love 'xyzzy'",
            "",
            "[424] print_obj (Test Object #1Test Object #2): Test Object #1Test Object #2",
            "",
            "",
            "Performed 425 tests.",
            "Passed: 406, Failed: 0, Print tests: 19",
            "Didn't crash: hooray!",
            "Last test: quit!",
        }, new String[0], new String[0][], null, null);
    }
}
