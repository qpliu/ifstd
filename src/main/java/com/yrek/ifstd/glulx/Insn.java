package com.yrek.ifstd.glulx;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.yrek.ifstd.glk.GlkDispatchArgument;
import com.yrek.ifstd.glulx.Glulx.Result;

class Insn {
    public static Result executeNext(Machine machine) {
        int opcode = machine.state.advancePC8() & 255;
        for (;;) {
            switch (opcode) {
            case 0: // nop
                return Result.Continue;
            case 16: // add
                operandsL2S(machine);
                machine.operandS0.store(machine.operandL0 + machine.operandL1);
                return Result.Continue;
            case 17: // sub
                operandsL2S(machine);
                machine.operandS0.store(machine.operandL0 - machine.operandL1);
                return Result.Continue;
            case 18: // mul
                operandsL2S(machine);
                machine.operandS0.store(machine.operandL0 * machine.operandL1);
                return Result.Continue;
            case 19: // div
                operandsL2S(machine);
                machine.operandS0.store(machine.operandL0 / machine.operandL1);
                return Result.Continue;
            case 20: // mod
                operandsL2S(machine);
                machine.operandS0.store(machine.operandL0 % machine.operandL1);
                return Result.Continue;
            case 21: // neg
                operandsLS(machine);
                machine.operandS0.store(-machine.operandL0);
                return Result.Continue;
            case 24: // bitand
                operandsL2S(machine);
                machine.operandS0.store(machine.operandL0 & machine.operandL1);
                return Result.Continue;
            case 25: // bitor
                operandsL2S(machine);
                machine.operandS0.store(machine.operandL0 | machine.operandL1);
                return Result.Continue;
            case 26: // bitxor
                operandsL2S(machine);
                machine.operandS0.store(machine.operandL0 ^ machine.operandL1);
                return Result.Continue;
            case 27: // bitnot
                operandsLS(machine);
                machine.operandS0.store(~machine.operandL0);
                return Result.Continue;
            case 28: // shiftl
                operandsL2S(machine);
                int a1 = machine.operandL1;
                machine.operandS0.store(a1 == (a1 & 31) ? machine.operandL0 << a1 : 0);
                return Result.Continue;
            case 29: // sshiftr
                operandsL2S(machine);
                a1 = machine.operandL1;
                machine.operandS0.store(a1 == (a1 & 31) ? machine.operandL0 >> a1 : machine.operandL0 < 0 ? -1 : 0);
                return Result.Continue;
            case 30: // ushiftr
                operandsL2S(machine);
                a1 = machine.operandL1;
                machine.operandS0.store(a1 == (a1 & 31) ? machine.operandL0 >>> a1 : 0);
                return Result.Continue;
            case 32: // jump
                operandsL(machine);
                return branch(machine, machine.operandL0);
            case 34: // jz
                operandsL2(machine);
                if (machine.operandL0 == 0) {
                    return branch(machine, machine.operandL1);
                }
                return Result.Continue;
            case 35: // jnz
                operandsL2(machine);
                if (machine.operandL0 != 0) {
                    return branch(machine, machine.operandL1);
                }
                return Result.Continue;
            case 36: // jeq
                operandsL3(machine);
                if (machine.operandL0 == machine.operandL1) {
                    return branch(machine, machine.operandL2);
                }
                return Result.Continue;
            case 37: // jne
                operandsL3(machine);
                if (machine.operandL0 != machine.operandL1) {
                    return branch(machine, machine.operandL2);
                }
                return Result.Continue;
            case 38: // jlt
                operandsL3(machine);
                if (machine.operandL0 < machine.operandL1) {
                    return branch(machine, machine.operandL2);
                }
                return Result.Continue;
            case 39: // jge
                operandsL3(machine);
                if (machine.operandL0 >= machine.operandL1) {
                    return branch(machine, machine.operandL2);
                }
                return Result.Continue;
            case 40: // jgt
                operandsL3(machine);
                if (machine.operandL0 > machine.operandL1) {
                    return branch(machine, machine.operandL2);
                }
                return Result.Continue;
            case 41: // jle
                operandsL3(machine);
                if (machine.operandL0 <= machine.operandL1) {
                    return branch(machine, machine.operandL2);
                }
                return Result.Continue;
            case 42: // jltu
                operandsL3(machine);
                long la1 = machine.operandL0 & 0xfffffffffL;
                long la2 = machine.operandL1 & 0xfffffffffL;
                if (la1 < la2) {
                    return branch(machine, machine.operandL2);
                }
                return Result.Continue;
            case 43: // jgeu
                operandsL3(machine);
                la1 = machine.operandL0 & 0xfffffffffL;
                la2 = machine.operandL1 & 0xfffffffffL;
                if (la1 >= la2) {
                    return branch(machine, machine.operandL2);
                }
                return Result.Continue;
            case 44: // jgtu
                operandsL3(machine);
                la1 = machine.operandL0 & 0xfffffffffL;
                la2 = machine.operandL1 & 0xfffffffffL;
                if (la1 > la2) {
                    return branch(machine, machine.operandL2);
                }
                return Result.Continue;
            case 45: // jleu
                operandsL3(machine);
                la1 = machine.operandL0 & 0xfffffffffL;
                la2 = machine.operandL1 & 0xfffffffffL;
                if (la1 <= la2) {
                    return branch(machine, machine.operandL2);
                }
                return Result.Continue;
            case 48: // call
                operandsL2S(machine);
                return call(machine, machine.operandL0, machine.operandL1, machine.operandS0, false);
            case 49: // return
                operandsL(machine);
                if (machine.state.fp == 0) {
                    return Result.Quit;
                }
                machine.state.sp = machine.state.fp;
                return returnValue(machine, machine.operandL0);
            case 50: // catch
                operandsSL(machine);
                pushCallStub(machine.state, machine.operandS0.getDestType(), machine.operandS0.getDestAddr());
                machine.operandS0.store(machine.state.sp);
                return branch(machine, machine.operandL0);
            case 51: // throw
                operandsL2(machine);
                machine.state.sp = machine.operandL1;
                return returnValue(machine, machine.operandL0);
            case 52: // tailcall
                operandsL2(machine);
                return call(machine, machine.operandL0, machine.operandL1, null, true);
            case 64: // copy
                operandsLS(machine);
                machine.operandS0.store(machine.operandL0);
                return Result.Continue;
            case 65: // copys
                operands16LS(machine);
                machine.operandS0.store(machine.operandL0);
                return Result.Continue;
            case 66: // copyb
                operands8LS(machine);
                machine.operandS0.store(machine.operandL0);
                return Result.Continue;
            case 68: // sexs
                operandsLS(machine);
                a1 = machine.operandL0 & 65535;
                if ((a1 & 32768) != 0) {
                    a1 |= 0xffff0000;
                }
                machine.operandS0.store(a1);
                return Result.Continue;
            case 69: // sexb
                operandsLS(machine);
                a1 = machine.operandL0 & 255;
                if ((a1 & 128) != 0) {
                    a1 |= 0xffffff00;
                }
                machine.operandS0.store(a1);
                return Result.Continue;
            case 72: // aload
                operandsL2S(machine);
                machine.operandS0.store(machine.state.load32(machine.operandL0 + 4*machine.operandL1));
                return Result.Continue;
            case 73: // aloads
                operandsL2S(machine);
                machine.operandS0.store(machine.state.load16(machine.operandL0 + 2*machine.operandL1) & 65535);
                return Result.Continue;
            case 74: // aloadb
                operandsL2S(machine);
                machine.operandS0.store(machine.state.load8(machine.operandL0 + machine.operandL1) & 255);
                return Result.Continue;
            case 75: // aloadbit
                operandsL2S(machine);
                a1 = machine.operandL0;
                int a2 = machine.operandL1;
                while (a2 < 0) {
                    a2 += 8;
                    a1--;
                }
                while (a2 >= 8) {
                    a2 -= 8;
                    a1++;
                }
                machine.operandS0.store((machine.state.load8(a1) >> a2) & 1);
                return Result.Continue;
            case 76: // astore
                operandsL3(machine);
                machine.state.store32(machine.operandL0 + 4*machine.operandL1, machine.operandL2);
                return Result.Continue;
            case 77: // astores
                operandsL3(machine);
                machine.state.store16(machine.operandL0 + 2*machine.operandL1, machine.operandL2);
                return Result.Continue;
            case 78: // astoreb
                operandsL3(machine);
                machine.state.store8(machine.operandL0 + machine.operandL1, machine.operandL2);
                return Result.Continue;
            case 79: // astorebit
                operandsL3(machine);
                a1 = machine.operandL0;
                a2 = machine.operandL1;
                while (a2 < 0) {
                    a2 += 8;
                    a1--;
                }
                while (a2 >= 8) {
                    a2 -= 8;
                    a1++;
                }
                int a3 = machine.state.load8(a1);
                if (machine.operandL2 == 0) {
                    a3 &= ~(1 << a2);
                } else {
                    a3 |= 1 << a2;
                }
                machine.state.store8(a1, a3);
                return Result.Continue;
            case 80: // stkcount
                operandsS(machine);
                machine.operandS0.store((machine.state.sp - machine.state.fp - machine.state.sload32(machine.state.fp))/4);
                return Result.Continue;
            case 81: // stkpeek
                operandsLS(machine);
                machine.operandS0.store(machine.state.sload32(machine.state.sp - 4 - 4*machine.operandL0));
                return Result.Continue;
            case 82: // stkswap
                machine.state.roll(2, 1);
                return Result.Continue;
            case 83: // stkroll
                operandsL2(machine);
                machine.state.roll(machine.operandL0, machine.operandL1);
                return Result.Continue;
            case 84: // stkcopy
                operandsL(machine);
                a1 = machine.operandL0;
                a2 = machine.state.sp - 4*a1;
                for (int i = 0; i < a1; i++) {
                    machine.state.push32(machine.state.sload32(a2));
                    a2 += 4;
                }
                return Result.Continue;
            case 112: // streamchar
                operandsL(machine);
                machine.ioSys.streamChar(machine, machine.operandL0&255);
                return Result.Continue;
            case 113: // streamnum
                operandsL(machine);
                machine.ioSys.streamNum(machine, machine.operandL0);
                return Result.Continue;
            case 114: // streamstr
                operandsL(machine);
                machine.ioSys.streamStringObject(machine, machine.operandL0);
                return Result.Continue;
            case 115: // streamunichar
                operandsL(machine);
                machine.ioSys.streamUnichar(machine, machine.operandL0);
                return Result.Continue;
            case 128: case 129: case 130: case 131: case 132: case 133:
            case 134: case 135: case 136: case 137: case 138: case 139:
            case 140: case 141: case 142: case 143: case 144: case 145:
            case 146: case 147: case 148: case 149: case 150: case 151:
            case 152: case 153: case 154: case 155: case 156: case 157:
            case 158: case 159: case 160: case 161: case 162: case 163:
            case 164: case 165: case 166: case 167: case 168: case 169:
            case 170: case 171: case 172: case 173: case 174: case 175:
            case 176: case 177: case 178: case 179: case 180: case 181:
            case 182: case 183: case 184: case 185: case 186: case 187:
            case 188: case 189: case 190: case 191:
                opcode = (opcode & 0x3f) << 8 | (machine.state.advancePC8() & 255);
                break;
            case 192: case 193: case 194: case 195: case 196: case 197:
            case 198: case 199: case 200: case 201: case 202: case 203:
            case 204: case 205: case 206: case 207: case 208: case 209:
            case 210: case 211: case 212: case 213: case 214: case 215:
            case 216: case 217: case 218: case 219: case 220: case 221:
            case 222: case 223:
                opcode = (opcode & 0x1f) << 24 | (machine.state.advancePC8() & 255) << 16 | (machine.state.advancePC8() & 255) << 8 | (machine.state.advancePC8() & 255);
                break;
            default:
                throw new IllegalArgumentException(String.format("pc=%x,opcode=%x",machine.state.pc,opcode));
            }
            if (opcode < 128) {
                continue;
            }
            switch (opcode) {
            case 256: // gestalt
                operandsL2S(machine);
                switch (machine.operandL0) {
                case Gestalt.GlulxVersion:
                    machine.operandS0.store(Glulx.GlulxVersion);
                    break;
                case Gestalt.TerpVersion:
                    machine.operandS0.store(Glulx.TerpVersion);
                    break;
                case Gestalt.ResizeMem:
                    machine.operandS0.store(1);
                    break;
                case Gestalt.Undo:
                    machine.operandS0.store(1);
                    break;
                case Gestalt.IOSystem:
                    switch (machine.operandL1) {
                    case Gestalt.IOSystem_null:
                    case Gestalt.IOSystem_filter:
                    case Gestalt.IOSystem_Glk:
                        machine.operandS0.store(1);
                        break;
                    default:
                        machine.operandS0.store(0);
                        break;
                    }
                    break;
                case Gestalt.Unicode:
                    machine.operandS0.store(1);
                    break;
                case Gestalt.MemCopy:
                    machine.operandS0.store(1);
                    break;
                case Gestalt.MAlloc:
                    machine.operandS0.store(0);
                    break;
                case Gestalt.MAllocHeap:
                    machine.operandS0.store(0);
                    break;
                case Gestalt.Acceleration:
                    machine.operandS0.store(1);
                    break;
                case Gestalt.AccelFunc:
                    machine.operandS0.store(machine.acceleration.gestalt(machine.operandL1) ? 1 : 0);
                    break;
                case Gestalt.Float:
                    machine.operandS0.store(1);
                    break;
                default:
                    machine.operandS0.store(0);
                    break;
                }
                return Result.Continue;
            case 257: // debugtrap
                operandsL(machine);
                throw new IllegalArgumentException("debugtrap");
            case 258: // getmemsize
                operandsS(machine);
                machine.operandS0.store(machine.state.memorySize());
                return Result.Continue;
            case 259: // setmemsize
                operandsLS(machine);
                machine.operandS0.store(machine.state.setMemorySize(machine.operandL0));
                return Result.Continue;
            case 260: // jumpabs
                operandsL(machine);
                machine.state.pc = machine.operandL0;
                return Result.Tick;
            case 272: // random
                operandsLS(machine);
                int a1 = machine.operandL0;
                if (a1 == 0) {
                    machine.operandS0.store(machine.random.nextInt());
                } else if (a1 > 0) {
                    machine.operandS0.store(machine.random.nextInt(a1));
                } else {
                    machine.operandS0.store(-machine.random.nextInt(-a1));
                }
                return Result.Continue;
            case 273: // setrandom
                operandsL(machine);
                a1 = machine.operandL0;
                if (a1 == 0) {
                    machine.random.setSeed(System.nanoTime());
                } else {
                    machine.random.setSeed((long) a1);
                }
                return Result.Continue;
            case 288: // quit
                return Result.Quit;
            case 289: // verify
                operandsS(machine);
                machine.operandS0.store(0);
                return Result.Continue;
            case 290: // restart
                try {
                    machine.state.readFile(machine.getData(), machine.protectStart, machine.protectLength);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                machine.operandS0.setStore(0);
                return call(machine, machine.state.load32(24), 0, machine.operandS0, false);
            case 291: // save
                operandsLS(machine);
                pushCallStub(machine.state, machine.operandS0.getDestType(), machine.operandS0.getDestAddr());
                int result = 1;
                DataInputStream dataInputStream = null;
                try {
                    dataInputStream = machine.getData();
                    machine.state.writeSave(dataInputStream, machine.glk.getStream(machine.operandL0).getDataOutput());
                    result = 0;
                } catch (IOException e) {
                    throw new RuntimeException("unimplemented", e);
                } finally {
                    if (dataInputStream != null) {
                        try {
                            dataInputStream.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                return returnValue(machine, result);
            case 292: // restore
                operandsLS(machine);
                dataInputStream = null;
                try {
                    dataInputStream = machine.getData();
                    machine.state.readSave(dataInputStream, machine.glk.getStream(machine.operandL0).getDataInput(), machine.protectStart, machine.protectLength);
                } catch (IOException e) {
                    throw new RuntimeException("unimplemented", e);
                } finally {
                    if (dataInputStream != null) {
                        try {
                            dataInputStream.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                return returnValue(machine, -1);
            case 293: // saveundo
                operandsS(machine);
                if (machine.saveUndo[0] == null || machine.saveUndo[0].pc == 0) {
                    machine.saveUndo[0] = machine.state.copyTo(machine.saveUndo[0]);
                } else {
                    State undoState = null;
                    for (int i = 1; i < machine.saveUndo.length; i++) {
                        if (machine.saveUndo[i] == null || machine.saveUndo[i].pc == 0) {
                            undoState = machine.state.copyTo(machine.saveUndo[i]);
                            System.arraycopy(machine.saveUndo, 0, machine.saveUndo, 1, i-1);
                        }
                    }
                    if (undoState == null) {
                        undoState = machine.state.copyTo(machine.saveUndo[machine.saveUndo.length-1]);
                        System.arraycopy(machine.saveUndo, 0, machine.saveUndo, 1, machine.saveUndo.length-1);
                    }
                    machine.saveUndo[0] = undoState;
                }
                machine.operandS0.store(0);
                machine.operandS0.store(machine.saveUndo[0], -1);
                return Result.Continue;
            case 294: // restoreundo
                operandsS(machine);
                for (int i = 0; i < machine.saveUndo.length; i++) {
                    if (machine.saveUndo[i] != null && machine.saveUndo[i].pc != 0) {
                        machine.state.copyFrom(machine.saveUndo[i], machine.protectStart, machine.protectLength);
                        machine.saveUndo[i].pc = 0;
                        return Result.Continue;
                    }
                }
                machine.operandS0.store(1);
                return Result.Continue;
            case 295: // protect
                operandsL2(machine);
                machine.protectStart = machine.operandL0;
                machine.protectLength = machine.operandL1;
                return Result.Continue;
            case 304: // glk
                operandsL2S(machine);
                GlkDispatchArgument[] args = new GlkDispatchArgument[machine.operandL1];
                for (int i = 0; i < args.length; i++) {
                    args[i] = new GlkArgument(machine, machine.state.pop32());
                }
                try {
                    machine.operandS0.store(machine.glk.dispatch(machine.operandL0, args));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return Result.Continue;
            case 320: // getstringtbl
                operandsS(machine);
                machine.operandS0.store(machine.stringTable.table);
                return Result.Continue;
            case 321: // setstringtbl
                operandsL(machine);
                machine.stringTable = StringTable.create(machine.state, machine.operandL0);
                return Result.Continue;
            case 328: // getiosys
                operandsS2(machine);
                machine.operandS0.store(machine.ioSys.mode);
                machine.operandS1.store(machine.ioSys.rock);
                return Result.Continue;
            case 329: // setiosys
                operandsL2(machine);
                switch (machine.operandL0) {
                case 1:
                    machine.ioSys = new FilterIOSys(machine.operandL1);
                    break;
                case 2:
                    machine.ioSys = new GlkIOSys(machine.operandL1);
                    break;
                case 0:
                default:
                    machine.ioSys = new NullIOSys(machine.operandL1);
                    break;
                }
                return Result.Continue;
            case 336: // linearsearch
                operandsL7S(machine);
                machine.operandS0.store(linearSearch(machine.state, machine.operandL0, machine.operandL1, machine.operandL2, machine.operandL3, machine.operandL4, machine.operandL5, machine.operandL6));
                return Result.Continue;
            case 337: // binarysearch
                operandsL7S(machine);
                machine.operandS0.store(binarySearch(machine.state, machine.operandL0, machine.operandL1, machine.operandL2, machine.operandL3, machine.operandL4, machine.operandL5, machine.operandL6));
                return Result.Continue;
            case 338: // linkedsearch
                operandsL6S(machine);
                machine.operandS0.store(linkedSearch(machine.state, machine.operandL0, machine.operandL1, machine.operandL2, machine.operandL3, machine.operandL4, machine.operandL5));
                return Result.Continue;
            case 352: // callf
                operandsLS(machine);
                return callf(machine, machine.operandL0, 0, 0, 0, 0, machine.operandS0);
            case 353: // callfi
                operandsL2S(machine);
                return callf(machine, machine.operandL0, machine.operandL1, 0, 0, 1, machine.operandS0);
            case 354: // callfii
                operandsL3S(machine);
                return callf(machine, machine.operandL0, machine.operandL1, machine.operandL2, 0, 2, machine.operandS0);
            case 355: // callfiii
                operandsL4S(machine);
                return callf(machine, machine.operandL0, machine.operandL1, machine.operandL2, machine.operandL3, 3, machine.operandS0);
            case 368: // mzero
                operandsL2(machine);
                Arrays.fill(machine.state.memory, machine.operandL1, machine.operandL0 + machine.operandL1, (byte) 0);
                return Result.Continue;
            case 369: // mcopy
                operandsL3(machine);
                System.arraycopy(machine.state.memory, machine.operandL1, machine.state.memory, machine.operandL2, machine.operandL0);
                return Result.Continue;
            case 376: // malloc
                operandsLS(machine);
                machine.operandS0.store(0);
                return Result.Continue;
            case 377: // mfree
                operandsL(machine);
                return Result.Continue;
            case 384: // accelfunc
                operandsL2(machine);
                machine.acceleration.accelerate(machine.operandL0, machine.operandL1);
                return Result.Continue;
            case 385: // accelparam
                operandsL2(machine);
                machine.acceleration.setParameter(machine.operandL0, machine.operandL1);
                return Result.Continue;
            case 400: // numtof
                operandsLS(machine);
                machine.operandS0.store(Float.floatToIntBits((float) machine.operandL0));
                return Result.Continue;
            case 401: // ftonumz
                operandsLS(machine);
                float f1 = Float.intBitsToFloat(machine.operandL0);
                if (Float.isNaN(f1)) {
                    machine.operandS0.store((machine.operandL0 & 0x80000000) == 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE);
                } else if (f1 < 0.0f) {
                    machine.operandS0.store((int) Math.ceil(f1));
                } else {
                    machine.operandS0.store((int) Math.floor(f1));
                }
                return Result.Continue;
            case 402: // ftonumn
                operandsLS(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                if (Float.isNaN(f1)) {
                    machine.operandS0.store((machine.operandL0 & 0x80000000) == 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE);
                } else {
                    machine.operandS0.store(Math.round(f1));
                }
                return Result.Continue;
            case 408: // ceil
                operandsLS(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                machine.operandS0.store(Float.floatToIntBits((float) Math.ceil((double) f1)));
                return Result.Continue;
            case 409: // floor
                operandsLS(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                machine.operandS0.store(Float.floatToIntBits((float) Math.floor((double) f1)));
                return Result.Continue;
            case 416: // fadd
                operandsL2S(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                float f2 = Float.intBitsToFloat(machine.operandL1);
                machine.operandS0.store(Float.floatToIntBits(f1 + f2));
                return Result.Continue;
            case 417: // fsub
                operandsL2S(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                f2 = Float.intBitsToFloat(machine.operandL1);
                machine.operandS0.store(Float.floatToIntBits(f1 - f2));
                return Result.Continue;
            case 418: // fmul
                operandsL2S(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                f2 = Float.intBitsToFloat(machine.operandL1);
                machine.operandS0.store(Float.floatToIntBits(f1 * f2));
                return Result.Continue;
            case 419: // fdiv
                operandsL2S(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                f2 = Float.intBitsToFloat(machine.operandL1);
                machine.operandS0.store(Float.floatToIntBits(f1 / f2));
                return Result.Continue;
            case 420: // fmod
                operandsL2S2(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                f2 = Float.intBitsToFloat(machine.operandL1);
                float f3 = f1 % f2;
                machine.operandS0.store(Float.floatToIntBits(f3));
                machine.operandS1.store(Float.floatToIntBits(Math.copySign(Math.abs(f1 - f3)/Math.abs(f2), Math.signum(f1)*Math.signum(f2))));
                return Result.Continue;
            case 424: // sqrt
                operandsLS(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                machine.operandS0.store(Float.floatToIntBits((float) Math.sqrt((double) f1)));
                return Result.Continue;
            case 425: // exp
                operandsLS(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                machine.operandS0.store(Float.floatToIntBits((float) Math.exp((double) f1)));
                return Result.Continue;
            case 426: // log
                operandsLS(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                machine.operandS0.store(Float.floatToIntBits((float) Math.log((double) f1)));
                return Result.Continue;
            case 427: // pow
                operandsL2S(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                f2 = Float.intBitsToFloat(machine.operandL1);
                if (f1 == 1.0f || (f1 == -1.0f && Float.isInfinite(f2))) {
                    machine.operandS0.store(Float.floatToIntBits(1.0f));
                } else {
                    machine.operandS0.store(Float.floatToIntBits((float) Math.pow((double) f1, (double) f2)));
                }
                return Result.Continue;
            case 432: // sin
                operandsLS(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                machine.operandS0.store(Float.floatToIntBits((float) Math.sin((double) f1)));
                return Result.Continue;
            case 433: // cos
                operandsLS(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                machine.operandS0.store(Float.floatToIntBits((float) Math.cos((double) f1)));
                return Result.Continue;
            case 434: // tan
                operandsLS(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                machine.operandS0.store(Float.floatToIntBits((float) Math.tan((double) f1)));
                return Result.Continue;
            case 435: // asin
                operandsLS(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                machine.operandS0.store(Float.floatToIntBits((float) Math.asin((double) f1)));
                return Result.Continue;
            case 436: // acos
                operandsLS(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                machine.operandS0.store(Float.floatToIntBits((float) Math.acos((double) f1)));
                return Result.Continue;
            case 437: // atan
                operandsLS(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                machine.operandS0.store(Float.floatToIntBits((float) Math.atan((double) f1)));
                return Result.Continue;
            case 438: // atan2
                operandsL2S(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                f2 = Float.intBitsToFloat(machine.operandL1);
                machine.operandS0.store(Float.floatToIntBits((float) Math.atan2((double) f1, (double) f2)));
                return Result.Continue;
            case 448: // jfeq
                operandsL4(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                f2 = Float.intBitsToFloat(machine.operandL1);
                f3 = Float.intBitsToFloat(machine.operandL2);
                if (Float.isNaN(f1) || Float.isNaN(f2) || Float.isNaN(f3)) {
                } else if (Float.isInfinite(f3)) {
                    if (!Float.isInfinite(f1) || !Float.isInfinite(f2) || Math.signum(f1)*Math.signum(f2) >= 0.0f) {
                        return branch(machine, machine.operandL3);
                    }
                } else if (Float.isInfinite(f1) && Float.isInfinite(f2)) {
                    if (Math.signum(f1)*Math.signum(f2) >= 0.0f) {
                        return branch(machine, machine.operandL3);
                    }
                } else if (Math.abs(f1 - f2) <= Math.abs(f3)) {
                    return branch(machine, machine.operandL3);
                }
                return Result.Continue;
            case 449: // jfne
                operandsL4(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                f2 = Float.intBitsToFloat(machine.operandL1);
                f3 = Float.intBitsToFloat(machine.operandL2);
                if (Float.isNaN(f1) || Float.isNaN(f2) || Float.isNaN(f3)) {
                } else if (Float.isInfinite(f3)) {
                    if (!Float.isInfinite(f1) || !Float.isInfinite(f2) || Math.signum(f1)*Math.signum(f2) >= 0.0f) {
                        return Result.Continue;
                    }
                } else if (Float.isInfinite(f1) && Float.isInfinite(f2)) {
                    if (Math.signum(f1)*Math.signum(f2) >= 0.0f) {
                        return Result.Continue;
                    }
                } else if (Math.abs(f1 - f2) <= Math.abs(f3)) {
                    return Result.Continue;
                }
                return branch(machine, machine.operandL3);
            case 450: // jflt
                operandsL3(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                f2 = Float.intBitsToFloat(machine.operandL1);
                if (f1 < f2) {
                    return branch(machine, machine.operandL2);
                }
                return Result.Continue;
            case 451: // jfle
                operandsL3(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                f2 = Float.intBitsToFloat(machine.operandL1);
                if (f1 <= f2) {
                    return branch(machine, machine.operandL2);
                }
                return Result.Continue;
            case 452: // jfgt
                operandsL3(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                f2 = Float.intBitsToFloat(machine.operandL1);
                if (f1 > f2) {
                    return branch(machine, machine.operandL2);
                }
                return Result.Continue;
            case 453: // jfge
                operandsL3(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                f2 = Float.intBitsToFloat(machine.operandL1);
                if (f1 >= f2) {
                    return branch(machine, machine.operandL2);
                }
                return Result.Continue;
            case 456: // jisnan
                operandsL2(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                if (Float.isNaN(f1)) {
                    return branch(machine, machine.operandL1);
                }
                return Result.Continue;
            case 457: // jisinf
                operandsL2(machine);
                f1 = Float.intBitsToFloat(machine.operandL0);
                if (Float.isInfinite(f1)) {
                    return branch(machine, machine.operandL1);
                }
                return Result.Continue;
            default:
                throw new IllegalArgumentException(String.format("pc=%x,opcode=%x",machine.state.pc,opcode));
            }
        }
    }

    static class Operand {
        final Machine machine;
        int val;
        int mode;

        Operand(Machine machine) {
            this.machine = machine;
        }

        void store(int result) {
            store(machine.state, result);
        }

        void store(State state, int result) {
            switch (mode) {
            case 0:
                break;
            case 5:
                state.store32(val, result);
                break;
            case 8:
                state.push32(result);
                break;
            case 9:
                state.sstore32(machine.state.localsp + val, result);
                break;
            case 21:
                state.store8(val, result);
                break;
            case 37:
                state.store16(val, result);
                break;
            default:
                throw new AssertionError();
            }
        }

        void setStore(int mode) {
            switch (mode) {
            case 0: case 8:
                this.mode = mode;
                break;
            case 5:
                this.mode = 5;
                val = machine.state.advancePC8() & 255;
                break;
            case 6:
                this.mode = 5;
                val = machine.state.advancePC16() & 65535;
                break;
            case 7:
                this.mode = 5;
                val = machine.state.advancePC32();
                break;
            case 9:
                this.mode = 9;
                val = machine.state.advancePC8() & 255;
                break;
            case 10:
                this.mode = 9;
                val = machine.state.advancePC16() & 65535;
                break;
            case 11:
                this.mode = 9;
                val = machine.state.advancePC32();
                break;
            case 13:
                this.mode = 5;
                val = machine.state.ramStart + (machine.state.advancePC8() & 255);
                break;
            case 14:
                this.mode = 5;
                val = machine.state.ramStart + (machine.state.advancePC16() & 65535);
                break;
            case 15:
                this.mode = 5;
                val = machine.state.ramStart + machine.state.advancePC32();
                break;
            default:
                throw new IllegalArgumentException(String.format("pc=%x,mode=%d", machine.state.pc, mode));
            }
        }

        void setStore8(int mode) {
            setStore(mode);
            if (this.mode == 5) {
                this.mode = 21;
            }
        }

        void setStore16(int mode) {
            setStore(mode);
            if (this.mode == 5) {
                this.mode = 37;
            }
        }

        int getDestType() {
            switch (mode) {
            case 0:
                return 0;
            case 5:
                return 1;
            case 8:
                return 3;
            case 9:
                return 2;
            default:
                throw new AssertionError();
            }
        }

        int getDestAddr() {
            switch (mode) {
            case 0:
                return 0;
            case 5:
                return val;
            case 8:
                return 0;
            case 9:
                return val;
            default:
                throw new AssertionError();
            }
        }
    }

    private static int loadOperand(Machine machine, int mode) {
        switch (mode) {
        case 0:
            return 0;
        case 1:
            return machine.state.advancePC8();
        case 2:
            return machine.state.advancePC16();
        case 3:
            return machine.state.advancePC32();
        case 5:
            return machine.state.load32(machine.state.advancePC8() & 255);
        case 6:
            return machine.state.load32(machine.state.advancePC16() & 65535);
        case 7:
            return machine.state.load32(machine.state.advancePC32());
        case 8:
            return machine.state.pop32();
        case 9:
            return machine.state.sload32(machine.state.localsp + (machine.state.advancePC8() & 255));
        case 10:
            return machine.state.sload32(machine.state.localsp + (machine.state.advancePC16() & 65535));
        case 11:
            return machine.state.sload32(machine.state.localsp + machine.state.advancePC32());
        case 13:
            return machine.state.load32(machine.state.ramStart + (machine.state.advancePC8() & 255));
        case 14:
            return machine.state.load32(machine.state.ramStart + (machine.state.advancePC16() & 65535));
        case 15:
            return machine.state.load32(machine.state.ramStart + machine.state.advancePC32());
        default:
            throw new IllegalArgumentException(String.format("pc=%x,mode=%d", machine.state.pc, mode));
        }
    }

    private static int loadOperand8(Machine machine, int mode) {
        switch (mode) {
        case 0:
            return 0;
        case 1:
            return machine.state.advancePC8() & 255;
        case 2:
            return machine.state.advancePC16() & 255;
        case 3:
            return machine.state.advancePC32() & 255;
        case 5:
            return machine.state.load8(machine.state.advancePC8() & 255) & 255;
        case 6:
            return machine.state.load8(machine.state.advancePC16() & 65535) & 255;
        case 7:
            return machine.state.load8(machine.state.advancePC32()) & 255;
        case 8:
            return machine.state.pop32() & 255;
        case 9:
        case 10:
        case 11:
            throw new IllegalArgumentException("deprecated");
        case 13:
            return machine.state.load8(machine.state.ramStart + (machine.state.advancePC8() & 255)) & 255;
        case 14:
            return machine.state.load8(machine.state.ramStart + (machine.state.advancePC16() & 65535)) & 255;
        case 15:
            return machine.state.load8(machine.state.ramStart + machine.state.advancePC32()) & 255;
        default:
            throw new IllegalArgumentException(String.format("pc=%x,mode=%d", machine.state.pc, mode));
        }
    }

    private static int loadOperand16(Machine machine, int mode) {
        switch (mode) {
        case 0:
            return 0;
        case 1:
            return machine.state.advancePC8() & 255;
        case 2:
            return machine.state.advancePC16() & 65535;
        case 3:
            return machine.state.advancePC32() & 65535;
        case 5:
            return machine.state.load16(machine.state.advancePC8() & 255) & 65535;
        case 6:
            return machine.state.load16(machine.state.advancePC16() & 65535) & 65535;
        case 7:
            return machine.state.load16(machine.state.advancePC32()) & 65535;
        case 8:
            return machine.state.pop32() & 65535;
        case 9:
        case 10:
        case 11:
            throw new IllegalArgumentException("deprecated");
        case 13:
            return machine.state.load16(machine.state.ramStart + (machine.state.advancePC8() & 255)) & 65535;
        case 14:
            return machine.state.load16(machine.state.ramStart + (machine.state.advancePC16() & 65535)) & 65535;
        case 15:
            return machine.state.load16(machine.state.ramStart + machine.state.advancePC32()) & 65535;
        default:
            throw new IllegalArgumentException(String.format("pc=%x,mode=%d", machine.state.pc, mode));
        }
    }

    private static void operandsL(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++);
        machine.operandL0 = loadOperand(machine, modes & 15);
    }

    private static void operandsL2(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        machine.operandL0 = loadOperand(machine, modes & 15);
        machine.operandL1 = loadOperand(machine, modes >> 4);
    }

    private static void operandsL3(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        int modes2 = machine.state.load8(machine.state.pc++);
        machine.operandL0 = loadOperand(machine, modes & 15);
        machine.operandL1 = loadOperand(machine, modes >> 4);
        machine.operandL2 = loadOperand(machine, modes2 & 15);
    }

    private static void operandsL4(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        int modes2 = machine.state.load8(machine.state.pc++) & 255;
        machine.operandL0 = loadOperand(machine, modes & 15);
        machine.operandL1 = loadOperand(machine, modes >> 4);
        machine.operandL2 = loadOperand(machine,modes2 & 15);
        machine.operandL3 = loadOperand(machine, modes2 >> 4);
    }

    private static void operandsLS(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        machine.operandL0 = loadOperand(machine, modes & 15);
        machine.operandS0.setStore(modes >> 4);
    }

    private static void operands8LS(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        machine.operandL0 = loadOperand8(machine, modes & 15);
        machine.operandS0.setStore8(modes >> 4);
    }

    private static void operands16LS(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        machine.operandL0 = loadOperand16(machine, modes & 15);
        machine.operandS0.setStore16(modes >> 4);
    }

    private static void operandsL2S(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        int modes2 = machine.state.load8(machine.state.pc++);
        machine.operandL0 = loadOperand(machine, modes & 15);
        machine.operandL1 = loadOperand(machine, modes >> 4);
        machine.operandS0.setStore(modes2 & 15);
    }

    private static void operandsL3S(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        int modes2 = machine.state.load8(machine.state.pc++) & 255;
        machine.operandL0 = loadOperand(machine, modes & 15);
        machine.operandL1 = loadOperand(machine, modes >> 4);
        machine.operandL2 = loadOperand(machine, modes2 & 15);
        machine.operandS0.setStore(modes2 >> 4);
    }

    private static void operandsL4S(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        int modes2 = machine.state.load8(machine.state.pc++) & 255;
        int modes3 = machine.state.load8(machine.state.pc++);
        machine.operandL0 = loadOperand(machine, modes & 15);
        machine.operandL1 = loadOperand(machine, modes >> 4);
        machine.operandL2 = loadOperand(machine, modes2 & 15);
        machine.operandL3 = loadOperand(machine, modes2 >> 4);
        machine.operandS0.setStore(modes3 & 15);
    }

    private static void operandsL5S(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        int modes2 = machine.state.load8(machine.state.pc++) & 255;
        int modes3 = machine.state.load8(machine.state.pc++) & 255;
        machine.operandL0 = loadOperand(machine, modes & 15);
        machine.operandL1 = loadOperand(machine, modes >> 4);
        machine.operandL2 = loadOperand(machine, modes2 & 15);
        machine.operandL3 = loadOperand(machine, modes2 >> 4);
        machine.operandL4 = loadOperand(machine, modes3 & 15);
        machine.operandS0.setStore(modes3 >> 4);
    }

    private static void operandsL6S(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        int modes2 = machine.state.load8(machine.state.pc++) & 255;
        int modes3 = machine.state.load8(machine.state.pc++) & 255;
        int modes4 = machine.state.load8(machine.state.pc++);
        machine.operandL0 = loadOperand(machine, modes & 15);
        machine.operandL1 = loadOperand(machine, modes >> 4);
        machine.operandL2 = loadOperand(machine, modes2 & 15);
        machine.operandL3 = loadOperand(machine, modes2 >> 4);
        machine.operandL4 = loadOperand(machine, modes3 & 15);
        machine.operandL5 = loadOperand(machine, modes3 >> 4);
        machine.operandS0.setStore(modes4 & 15);
    }

    private static void operandsL7S(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        int modes2 = machine.state.load8(machine.state.pc++) & 255;
        int modes3 = machine.state.load8(machine.state.pc++) & 255;
        int modes4 = machine.state.load8(machine.state.pc++) & 255;
        machine.operandL0 = loadOperand(machine, modes & 15);
        machine.operandL1 = loadOperand(machine, modes >> 4);
        machine.operandL2 = loadOperand(machine, modes2 & 15);
        machine.operandL3 = loadOperand(machine, modes2 >> 4);
        machine.operandL4 = loadOperand(machine, modes3 & 15);
        machine.operandL5 = loadOperand(machine, modes3 >> 4);
        machine.operandL6 = loadOperand(machine, modes4 & 15);
        machine.operandS0.setStore(modes4 >> 4);
    }

    private static void operandsL2S2(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        int modes2 = machine.state.load8(machine.state.pc++) & 255;
        machine.operandL0 = loadOperand(machine, modes & 15);
        machine.operandL1 = loadOperand(machine, modes >> 4);
        machine.operandS0.setStore(modes2 & 15);
        machine.operandS1.setStore(modes2 >> 4);
    }

    private static void operandsS(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++);
        machine.operandS0.setStore(modes & 15);
    }

    private static void operandsSL(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        machine.operandS0.setStore(modes & 15);
        machine.operandL0 = loadOperand(machine, modes >> 4);
    }

    private static void operandsS2(Machine machine) {
        int modes = machine.state.load8(machine.state.pc++) & 255;
        machine.operandS0.setStore(modes & 15);
        machine.operandS1.setStore(modes >> 4);
    }

    private static Result branch(Machine machine, int offset) {
        switch (offset) {
        case 0: case 1:
            if (machine.state.fp == 0) {
                return Result.Quit;
            }
            machine.state.sp = machine.state.fp;
            returnValue(machine, offset);
            return Result.Tick;
        default:
            machine.state.pc += offset - 2;
            return Result.Tick;
        }
    }

    // arg0, arg1, arg2 must be 0 if they are not encompassed by argc
    private static Result callf(Machine machine, int addr, int arg0, int arg1, int arg2, int argc, Operand dest) {
        Acceleration.Function accelerated = machine.acceleration.get(addr);
        if (accelerated != null) {
            dest.store(accelerated.call(machine, arg0, arg1, machine.acceleration.parameters));
            return Result.Tick;
        }
        final State state = machine.state;
        pushCallStub(state, dest.getDestType(), dest.getDestAddr());
        state.fp = state.sp;
        boolean pushArgs;
        switch (state.load8(addr)) {
        case -64: pushArgs = true; break;
        case -63: pushArgs = false; break;
        default: throw new IllegalArgumentException(String.format("Invalid call %x", addr));
        }
        pushCallFrameHeader(state, addr);
        if (!pushArgs) {
            pushCallFrameLocals(state, addr, arg0, arg1, arg2);
        } else {
            pushCallFrameLocals(state, addr);
            switch (argc) {
            case 3: state.push32(arg2); state.push32(arg1); state.push32(arg0); break;
            case 2: state.push32(arg1); state.push32(arg0); break;
            case 1: state.push32(arg0); break;
            case 0: break;
            default: throw new AssertionError();
            }
            state.push32(argc);
        }
        return Result.Tick;
    }

    private static Result call(Machine machine, int addr, int argc, Operand dest, boolean tailcall) {
        final State state = machine.state;
        final int[] args = new int[argc];
        for (int i = 0; i < argc; i++) {
            args[i] = state.pop32();
        }
        if (tailcall) {
            state.sp = state.fp;
        } else {
            pushCallStub(state, dest.getDestType(), dest.getDestAddr());
        }
        Acceleration.Function accelerated = machine.acceleration.get(addr);
        if (accelerated != null) {
            return returnValue(machine, accelerated.call(machine, argc > 0 ? args[0] : 0, argc > 1 ? args[1] : 0, machine.acceleration.parameters));
        }
        state.fp = state.sp;
        boolean pushArgs;
        switch (state.load8(addr)) {
        case -64: pushArgs = true; break;
        case -63: pushArgs = false; break;
        default: throw new IllegalArgumentException(String.format("Invalid call %x", addr));
        }
        pushCallFrameHeader(state, addr);
        if (!pushArgs) {
            pushCallFrameLocals(state, addr, args);
        } else {
            pushCallFrameLocals(state, addr);
            for (int i = argc - 1; i >= 0; i--) {
                state.push32(args[i]);
            }
            state.push32(argc);
        }
        return Result.Tick;
    }

    // arg0, arg1, arg2 must be 0 if they are not encompassed by argc
    static void resumeCallf(State state, int addr, int arg0, int arg1, int arg2, int argc) {
        state.fp = state.sp;
        boolean pushArgs;
        switch (state.load8(addr)) {
        case -64: pushArgs = true; break;
        case -63: pushArgs = false; break;
        default: throw new IllegalArgumentException(String.format("Invalid call %x", addr));
        }
        pushCallFrameHeader(state, addr);
        if (!pushArgs) {
            pushCallFrameLocals(state, addr, arg0, arg1, arg2);
        } else {
            pushCallFrameLocals(state, addr);
            switch (argc) {
            case 3: state.push32(arg2); state.push32(arg1); state.push32(arg0); break;
            case 2: state.push32(arg1); state.push32(arg0); break;
            case 1: state.push32(arg0); break;
            case 0: break;
            default: throw new AssertionError();
            }
            state.push32(argc);
        }
    }

    static void resumeCall(State state, int addr, int[] args) {
        state.fp = state.sp;
        boolean pushArgs;
        switch (state.load8(addr)) {
        case -64: pushArgs = true; break;
        case -63: pushArgs = false; break;
        default: throw new IllegalArgumentException(String.format("Invalid call %x", addr));
        }
        pushCallFrameHeader(state, addr);
        if (!pushArgs) {
            pushCallFrameLocals(state, addr, args);
        } else {
            pushCallFrameLocals(state, addr);
            for (int i = args.length - 1; i >= 0; i--) {
                state.push32(args[i]);
            }
            state.push32(args.length);
        }
    }

    private static void pushCallFrameHeader(State state, int addr) {
        int localsPos = 8;
        int localsSize = 0;
        loop:
        for (int i = 1;; i += 2) {
            int localType = state.load8(addr + i) & 255;
            int localCount = state.load8(addr + i + 1) & 255;
            localsPos += 2;
            switch (localType) {
            case 1: case 2:
                throw new RuntimeException("unimplemented");
            case 4:
                localsSize = align(localsSize, localType);
                localsSize += localType * localCount;
                break;
            case 0:
                if (localCount != 0) {
                    throw new IllegalArgumentException("Invalid locals format");
                }
                state.pc = addr + i + 2;
                break loop;
            default:
                throw new IllegalArgumentException("Invalid locals format");
            }
        }
        localsPos = align(localsPos, 4);
        state.localsp = state.fp + localsPos;
        localsSize = align(localsSize, 4);
        state.push32(localsPos + localsSize);
        state.push32(localsPos);
        assert localsPos >= 12;
        for (int i = 0; i < localsPos - 12; i += 4) {
            state.push32(state.load32(addr + 1 + i));
        }
        state.push32(state.load32(addr + 1 + localsPos - 12) & 0xffff0000);
    }

    private static void pushCallFrameLocals(State state, int addr) {
        for (int i = 1;; i += 2) {
            int localType = state.load8(addr + i);
            int localCount = state.load8(addr + 1 + i) & 255;
            switch (localType) {
            case 4:
                for (int j = 0; j < localCount; j++) {
                    state.push32(0);
                }
                break;
            case 0:
                return;
            default:
                throw new AssertionError();
            }
        }
    }

    private static void pushCallFrameLocals(State state, int addr, int arg0, int arg1, int arg2) {
        int argIndex = 0;
        for (int i = 1;; i += 2) {
            int localType = state.load8(addr + i);
            int localCount = state.load8(addr + 1 + i) & 255;
            switch (localType) {
            case 4:
                for (int j = 0; j < localCount; j++) {
                    switch (argIndex) {
                    case 0: state.push32(arg0); argIndex++; break;
                    case 1: state.push32(arg1); argIndex++; break;
                    case 2: state.push32(arg2); argIndex++; break;
                    default: state.push32(0); break;
                    }
                }
                break;
            case 0:
                return;
            default:
                throw new AssertionError();
            }
        }
    }

    private static void pushCallFrameLocals(State state, int addr, int[] args) {
        int argIndex = 0;
        for (int i = 1;; i += 2) {
            int localType = state.load8(addr + i);
            int localCount = state.load8(addr + 1 + i) & 255;
            switch (localType) {
            case 4:
                for (int j = 0; j < localCount; j++) {
                    if (argIndex < args.length) {
                        state.push32(args[argIndex]);
                        argIndex++;
                    } else {
                        state.push32(0);
                    }
                }
                break;
            case 0:
                return;
            default:
                throw new AssertionError();
            }
        }
    }

    static void pushCallStub(State state, int destType, int destAddr) {
        state.push32(destType);
        state.push32(destAddr);
        state.push32(state.pc);
        state.push32(state.fp);
    }

    static Result returnValue(Machine machine, int result) {
        for (;;) {
            int fp = machine.state.sload32(machine.state.sp-4);
            int pc = machine.state.sload32(machine.state.sp-8);
            int destAddr = machine.state.sload32(machine.state.sp-12);
            int destType = machine.state.sload32(machine.state.sp-16);
            machine.state.sp -= 16;
            switch (destType) {
            case 0:
                machine.state.fp = fp;
                machine.state.pc = pc;
                machine.state.localsp = fp + machine.state.sload32(fp + 4);
                return Result.Tick;
            case 1:
                machine.state.fp = fp;
                machine.state.pc = pc;
                machine.state.localsp = fp + machine.state.sload32(fp + 4);
                machine.state.store32(destAddr, result);
                return Result.Tick;
            case 2:
                machine.state.fp = fp;
                machine.state.pc = pc;
                machine.state.localsp = fp + machine.state.sload32(fp + 4);
                machine.state.sstore32(machine.state.localsp + destAddr, result);
                return Result.Tick;
            case 3:
                machine.state.fp = fp;
                machine.state.pc = pc;
                machine.state.localsp = fp + machine.state.sload32(fp + 4);
                machine.state.push32(result);
                return Result.Tick;
            case 10:
                machine.state.fp = fp;
                machine.state.localsp = fp + machine.state.sload32(fp + 4);
                machine.ioSys.resumePrintCompressed(machine, pc, destAddr);
                break;
            case 11:
                assert machine.state.fp == fp;
                machine.state.pc = pc;
                return Result.Tick;
            case 12:
                machine.state.fp = fp;
                machine.state.localsp = fp + machine.state.sload32(fp + 4);
                machine.ioSys.resumePrintNumber(machine, pc, destAddr);
                break;
            case 13:
                machine.state.fp = fp;
                machine.state.localsp = fp + machine.state.sload32(fp + 4);
                machine.ioSys.resumePrint(machine, pc);
                break;
            case 14:
                machine.state.fp = fp;
                machine.state.localsp = fp + machine.state.sload32(fp + 4);
                machine.ioSys.resumePrintUnicode(machine, pc);
                break;
            default:
                throw new IllegalArgumentException("stack corruption");
            }
        }
    }

    private static int linearSearch(State state, int key, int keySize, int start, int structSize, int numStructs, int keyOffset, int options) {
        byte[] searchKey = searchKey(state, key, keySize, options);
        boolean returnIndex = (options & 4) != 0;
        boolean zeroKeyTerminates = (options & 2) != 0;
        for (int i = 0; numStructs == -1 || i < numStructs; i++) {
            if (searchKeyCompare(state, start + i*structSize + keyOffset, searchKey) == 0) {
                return returnIndex ? i : start + i*structSize;
            } else if (zeroKeyTerminates && searchKeyZero(state, start + i*structSize + keyOffset, keySize)) {
                break;
            }
        }
        return returnIndex ? -1 : 0;
    }

    static int binarySearch(State state, int key, int keySize, int start, int structSize, int numStructs, int keyOffset, int options) {
        byte[] searchKey = searchKey(state, key, keySize, options);
        boolean returnIndex = (options & 4) != 0;
        int lo = 0;
        int hi = numStructs;
        for (;;) {
            int i = (lo + hi) / 2;
            int c = searchKeyCompare(state, start + i*structSize + keyOffset, searchKey);
            if (c == 0) {
                return returnIndex ? i : start + i*structSize;
            }
            if (c < 0 && i != hi) {
                hi = i;
            } else if (c > 0 && i != lo) {
                lo = i;
            } else {
                return returnIndex ? -1 : 0;
            }
        }
    }

    private static int linkedSearch(State state, int key, int keySize, int start, int keyOffset, int nextOffset, int options) {
        byte[] searchKey = searchKey(state, key, keySize, options);
        boolean zeroKeyTerminates = (options & 2) != 0;
        for (;;) {
            if (start == 0) {
                return 0;
            } else if (searchKeyCompare(state, start + keyOffset, searchKey) == 0) {
                return start;
            } else if (zeroKeyTerminates && searchKeyZero(state, start + keyOffset, keySize)) {
                return 0;
            }
            start = state.load32(start + nextOffset);
        }
    }

    private static byte[] searchKey(State state, int key, int keySize, int options) {
        byte[] searchKey = new byte[keySize];
        if ((options & 1) == 0) {
            switch (keySize) {
            case 1: searchKey[0] = (byte) key; break;
            case 2: State.store16(searchKey, 0, key); break;
            case 4: State.store32(searchKey, 0, key); break;
            default: throw new IllegalArgumentException("invalid search KeySize");
            }
        } else {
            for (int i = 0; i < keySize; i++) {
                searchKey[i] = (byte) state.load8(key + i);
            }
        }
        return searchKey;
    }

    private static int searchKeyCompare(State state, int addr, byte[] searchKey) {
        for (int i = 0; i < searchKey.length; i++) {
            int c = (searchKey[i] & 255) - (state.load8(addr + i) & 255);
            if (c != 0) {
                return c;
            }
        }
        return 0;
    }

    private static boolean searchKeyZero(State state, int addr, int keySize) {
        for (int i = 0; i < keySize; i++) {
            if (state.load8(addr + i) != 0) {
                return false;
            }
        }
        return true;
    }

    private static int align(int n, int alignment) {
        while (n % alignment != 0) {
            n++;
        }
        return n;
    }
}
