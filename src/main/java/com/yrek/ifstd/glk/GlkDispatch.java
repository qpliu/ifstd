package com.yrek.ifstd.glk;

import java.io.IOException;

public class GlkDispatch {
    public final Glk glk;
    private final GlkObjectPool<GlkWindow> windows = new GlkObjectPool<GlkWindow>();
    private final GlkObjectPool<GlkStream> streams = new GlkObjectPool<GlkStream>();
    private final GlkObjectPool<GlkFile> files = new GlkObjectPool<GlkFile>();
    private final GlkObjectPool<GlkSChannel> schannels = new GlkObjectPool<GlkSChannel>();

    public GlkDispatch(Glk glk) {
        this.glk = glk;
    }

    public int dispatch(int selector, GlkDispatchArgument[] args) throws IOException {
        switch (selector) {
        case 0x0001: // exit
            glk.exit();
            return 0;
        case 0x0002: // setInterruptHandler
            glk.setInterruptHandler(args[0].getRunnable());
            return 0;
        case 0x0003: // tick
            glk.tick();
            return 0;
        case 0x0004: // gestalt
            return glk.gestalt(args[0].getInt(), args[1].getInt());
        case 0x0005: // gestaltExt
            return glk.gestaltExt(args[0].getInt(), args[1].getInt(), withLength(args[2].getIntArray(), args[3].getInt()));
        case 0x0020: // windowIterate
            int pointer = windows.iterate(args[0].getInt());
            args[1].setInt(pointer == 0 ? 0 : windows.get(pointer).getRock());
            return pointer;
        case 0x0021: // windowGetRock
            return windows.get(args[0].getInt()).getRock();
        case 0x0022: // windowGetRoot
            return windows.getPointer(glk.windowGetRoot());
        case 0x0023: // windowOpen
            GlkWindow window = glk.windowOpen(windows.get(args[0].getInt()), args[1].getInt(), args[2].getInt(), args[3].getInt(), args[4].getInt());
            if (window != null) {
                windows.add(window.getParent());
                streams.add(window.getStream());
            }
            return windows.getPointer(window);
        case 0x0024: // windowClose
            pointer = args[0].getInt();
            window = windows.get(pointer);
            setStreamResult(args[1], window.close());
            streams.destroy(window.getStream().getPointer());
            windows.destroy(pointer);
            return 0;
        case 0x0025: // windowGetSize
            GlkWindowSize windowSize = windows.get(args[0].getInt()).getSize();
            args[1].setInt(windowSize.width);
            args[2].setInt(windowSize.height);
            return 0;
        case 0x0026: // windowSetArrangement
            windows.get(args[0].getInt()).setArrangement(args[1].getInt(), args[2].getInt(), windows.get(args[3].getInt()));
            return 0;
        case 0x0027: // windowGetArrangement
            GlkWindowArrangement windowArrangement = windows.get(args[0].getInt()).getArrangement();
            args[1].setInt(windowArrangement.method);
            args[2].setInt(windowArrangement.size);
            args[3].setInt(windows.getPointer(windowArrangement.key));
            return 0;
        case 0x0028: // windowGetType
            return windows.get(args[0].getInt()).getType();
        case 0x0029: // windowGetParent
            return windows.getPointer(windows.get(args[0].getInt()).getParent());
        case 0x002a: // windowClear
            windows.get(args[0].getInt()).clear();
            return 0;
        case 0x002b: // windowMoveCursor
            window = windows.get(args[0].getInt());
            int x = args[1].getInt();
            int y = args[2].getInt();
            if (window != null) {
                window.moveCursor(x, y);
            }
            return 0;
        case 0x002c: // windowGetStream
            return streams.getPointer(windows.get(args[0].getInt()).getStream());
        case 0x002d: // windowSetEchoStream
            windows.get(args[0].getInt()).getStream().setEchoStream(streams.get(args[1].getInt()));
            return 0;
        case 0x002e: // windowGetEchoStream
            return streams.getPointer(windows.get(args[0].getInt()).getStream().getEchoStream());
        case 0x002f: // setWindow
            glk.setWindow(windows.get(args[0].getInt()));
            return 0;
        case 0x0030: // windowGetSibling
            return windows.getPointer(windows.get(args[0].getInt()).getSibling());
        case 0x0040: // streamIterate
            pointer = streams.iterate(args[0].getInt());
            args[1].setInt(pointer == 0 ? 0 : streams.get(pointer).getRock());
            return pointer;
        case 0x0041: // streamGetRock
            return streams.get(args[0].getInt()).getRock();
        case 0x0042: // streamOpenFile
            return streams.add(glk.streamOpenFile(files.get(args[0].getInt()), args[1].getInt(), args[2].getInt()));
        case 0x0043: // streamOpenMemory
            return streams.add(glk.streamOpenMemory(withLength(args[0].getByteArray(), args[1].getInt()), args[2].getInt(), args[3].getInt()));
        case 0x0044: // streamClose
            pointer = args[0].getInt();
            GlkStream str = streams.get(pointer);
            setStreamResult(args[1], str.close());
            streams.destroy(pointer);
            return 0;
        case 0x0045: // streamSetPosition
            streams.get(args[0].getInt()).setPosition(args[1].getInt(), args[2].getInt());
            return 0;
        case 0x0046: // streamGetPosition
            return streams.get(args[0].getInt()).getPosition();
        case 0x0047: // streamSetCurrent
            glk.streamSetCurrent(streams.get(args[0].getInt()));
            return 0;
        case 0x0048: // streamGetCurrent
            return streams.getPointer(glk.streamGetCurrent());
        case 0x0049: // streamOpenResource
            throw new RuntimeException("unimplemented");
        case 0x0060: // filerefCreateTemp
            return files.add(glk.fileCreateTemp(args[0].getInt(), args[1].getInt()));
        case 0x0061: // filerefCreateByName
            return files.add(glk.fileCreateByName(args[0].getInt(), new GlkByteArrayString(args[1].getString()), args[2].getInt()));
        case 0x0062: // filerefCreateByPrompt
            return files.add(glk.fileCreateByPrompt(args[0].getInt(), args[1].getInt(), args[2].getInt()));
        case 0x0063: // filerefDestroy
            pointer = args[0].getInt();
            files.get(pointer).destroy();
            files.destroy(pointer);
            return 0;
        case 0x0064: // filerefIterate
            pointer = files.iterate(args[0].getInt());
            args[1].setInt(pointer == 0 ? 0 : files.get(pointer).getRock());
            return pointer;
        case 0x0065: // filerefGetRock
            return files.get(args[0].getInt()).getRock();
        case 0x0066: // filerefDeleteFile
            files.get(args[0].getInt()).delete();
            return 0;
        case 0x0067: // filerefDoesFileExist
            return files.get(args[0].getInt()).exists() ? 1 : 0;
        case 0x0068: // filerefCreateFromFileref
            return files.add(glk.fileCreateFromFile(args[0].getInt(), files.get(args[1].getInt()), args[2].getInt()));
        case 0x0080: // putChar
            glk.putChar(args[0].getInt());
            return 0;
        case 0x0081: // putCharStream
            streams.get(args[0].getInt()).putChar(args[1].getInt());
            return 0;
        case 0x0082: // putString
            glk.putString(new GlkByteArrayString(args[0].getString()));
            return 0;
        case 0x0083: // putStringStream
            streams.get(args[0].getInt()).putString(new GlkByteArrayString(args[1].getString()));
            return 0;
        case 0x0084: // putBuffer
            glk.putBuffer(withLength(args[0].getByteArray(), args[1].getInt()));
            return 0;
        case 0x0085: // putBufferStream
            streams.get(args[0].getInt()).putBuffer(withLength(args[1].getByteArray(), args[2].getInt()));
            return 0;
        case 0x0086: // setStyle
            glk.setStyle(args[0].getInt());
            return 0;
        case 0x0087: // setStyleStream
            streams.get(args[0].getInt()).setStyle(args[1].getInt());
            return 0;
        case 0x0090: // getCharStream
            return streams.get(args[0].getInt()).getChar();
        case 0x0091: // getLineStream
            return streams.get(args[0].getInt()).getLine(withLength(args[1].getByteArray(), args[2].getInt()));
        case 0x0092: // getBufferStream
            return streams.get(args[0].getInt()).getBuffer(withLength(args[1].getByteArray(), args[2].getInt()));
        case 0x00a0: // charToLower
            return Character.toLowerCase(args[0].getInt() & 255) & 255;
        case 0x00a1: // charToUpper
            return Character.toUpperCase(args[0].getInt() & 255) & 255;
        case 0x00b0: // stylehintSet
            glk.styleHintSet(args[0].getInt(), args[1].getInt(), args[2].getInt(), args[3].getInt());
            return 0;
        case 0x00b1: // stylehintClear
            glk.styleHintClear(args[0].getInt(), args[1].getInt(), args[2].getInt());
            return 0;
        case 0x00b2: // styleDistinguish
            return windows.get(args[0].getInt()).styleDistinguish(args[1].getInt(), args[2].getInt()) ? 1 : 0;
        case 0x00b3: // styleMeasure
            Integer integer = windows.get(args[0].getInt()).styleMeasure(args[1].getInt(), args[2].getInt());
            if (integer == null) {
                return 0;
            } else {
                args[3].setInt(integer);
                return 1;
            }
        case 0x00c0: // select
            setEvent(args[0], glk.select());
            return 0;
        case 0x00c1: // selectPoll
            setEvent(args[0], glk.selectPoll());
            return 0;
        case 0x00d0: // requestLineEvent
            windows.get(args[0].getInt()).requestLineEvent(withLength(args[1].getByteArray(), args[2].getInt()), args[3].getInt());
            return 0;
        case 0x00d1: // cancelLineEvent
            setEvent(args[1], windows.get(args[0].getInt()).cancelLineEvent());
            return 0;
        case 0x00d2: // requestCharEvent
            windows.get(args[0].getInt()).requestCharEvent();
            return 0;
        case 0x00d3: // cancelCharEvent
            windows.get(args[0].getInt()).cancelCharEvent();
            return 0;
        case 0x00d4: // requestMouseEvent
            windows.get(args[0].getInt()).requestMouseEvent();
            return 0;
        case 0x00d5: // cancelMouseEvent
            windows.get(args[0].getInt()).cancelMouseEvent();
            return 0;
        case 0x00d6: // requestTimerEvents
            glk.requestTimerEvents(args[0].getInt());
            return 0;
        case 0x00e0: // imageGetInfo
            int resourceId = args[0].getInt();
            int[] size = new int[] { args[1].getIndirectInt(), args[2].getIndirectInt() };
            boolean flag = glk.imageGetInfo(resourceId, size);
            args[1].setInt(size[0]);
            args[2].setInt(size[1]);
            return flag ? 1 : 0;
        case 0x00e1: // imageDraw
            window = windows.get(args[0].getInt());
            resourceId = args[1].getInt();
            int val1 = args[2].getInt();
            int val2 = args[3].getInt();
            if (window == null) {
                return 0;
            }
            return window.drawImage(resourceId, val1, val2) ? 1 : 0;
        case 0x00e2: // imageDrawScaled
            window = windows.get(args[0].getInt());
            resourceId = args[1].getInt();
            val1 = args[2].getInt();
            val2 = args[3].getInt();
            int width = args[4].getInt();
            int height = args[5].getInt();
            if (window == null) {
                return 0;
            }
            return window.drawScaledImage(resourceId, val1, val2, width, height) ? 1 : 0;
        case 0x00e8: // windowFlowBreak
            window = windows.get(args[0].getInt());
            if (window != null) {
                window.flowBreak();
            }
            return 0;
        case 0x00e9: // windowEraseRect
            window = windows.get(args[0].getInt());
            int left = args[1].getInt();
            int top = args[2].getInt();
            width = args[3].getInt();
            height = args[4].getInt();
            if (window != null) {
                window.eraseRect(left, top, width, height);
            }
            return 0;
        case 0x00ea: // windowFillRect
            window = windows.get(args[0].getInt());
            int color = args[1].getInt();
            left = args[2].getInt();
            top = args[3].getInt();
            width = args[4].getInt();
            height = args[5].getInt();
            if (window != null) {
                window.fillRect(color, left, top, width, height);
            }
            return 0;
        case 0x00eb: // windowSetBackgroundColor
            window = windows.get(args[0].getInt());
            color = args[1].getInt();
            if (window != null) {
                window.setBackgroundColor(color);
            }
            return 0;
        case 0x00f0: // sChannelIterate
            pointer = schannels.iterate(args[0].getInt());
            args[1].setInt(pointer == 0 ? 0 : schannels.get(pointer).getRock());
            return pointer;
        case 0x00f1: // sChannelGetRock
            throw new RuntimeException("unimplemented");
        case 0x00f2: // sChannelCreate
            throw new RuntimeException("unimplemented");
        case 0x00f3: // sChannelDestroy
            throw new RuntimeException("unimplemented");
        case 0x00f4: // sChannelCreateExt
            throw new RuntimeException("unimplemented");
        case 0x00f7: // sChannelPlayMulti
            throw new RuntimeException("unimplemented");
        case 0x00f8: // sChannelPlay
            throw new RuntimeException("unimplemented");
        case 0x00f9: // sChannelPlayExt
            throw new RuntimeException("unimplemented");
        case 0x00fa: // sChannelStop
            throw new RuntimeException("unimplemented");
        case 0x00fb: // sChannelSetVolume
            throw new RuntimeException("unimplemented");
        case 0x00fc: // soundLoadHint
            throw new RuntimeException("unimplemented");
        case 0x00fd: // sChannelSetVolumeExt
            throw new RuntimeException("unimplemented");
        case 0x00fe: // sChannelPause
            throw new RuntimeException("unimplemented");
        case 0x00ff: // sChannelUnpause
            throw new RuntimeException("unimplemented");
        case 0x0100: // setHyperlink
            throw new RuntimeException("unimplemented");
        case 0x0101: // setHyperlinkStream
            throw new RuntimeException("unimplemented");
        case 0x0102: // requestHyperlinkEvent
            throw new RuntimeException("unimplemented");
        case 0x0103: // cancelHyperlinkEvent
            throw new RuntimeException("unimplemented");
        case 0x0120: // bufferToLowerCaseUni
            GlkIntArray buffer = withLength(args[0].getIntArray(), args[1].getInt());
            int length = args[2].getInt();
            for (int i = 0; i < length; i++) {
                buffer.setIntElementAt(i, Character.toLowerCase(buffer.getIntElementAt(i)));
            }
            return length;
        case 0x0121: // bufferToUpperCaseUni
            buffer = withLength(args[0].getIntArray(), args[1].getInt());
            length = args[2].getInt();
            for (int i = 0; i < length; i++) {
                buffer.setIntElementAt(i, Character.toUpperCase(buffer.getIntElementAt(i)));
            }
            return length;
        case 0x0122: // bufferToTitleCaseUni
            buffer = withLength(args[0].getIntArray(), args[1].getInt());
            length = args[2].getInt();
            boolean toLower = args[3].getInt() != 0;
            if (length > 0) {
                buffer.setIntElementAt(0, Character.toTitleCase(buffer.getIntElementAt(0)));
            }
            if (toLower) {
                for (int i = 1; i < length; i++) {
                    buffer.setIntElementAt(i, Character.toLowerCase(buffer.getIntElementAt(i)));
                }
            }
            return length;
        case 0x0123: // bufferCanonDecomposeUni
            throw new RuntimeException("unimplemented");
        case 0x0124: // bufferCanonNormalizeUni
            throw new RuntimeException("unimplemented");
        case 0x0128: // putCharUni
            glk.putCharUni(args[0].getInt());
            return 0;
        case 0x0129: // putStringUni
            glk.putStringUni(new GlkIntArrayString(args[0].getStringUnicode()));
            return 0;
        case 0x012a: // putBufferUni
            glk.putBufferUni(withLength(args[0].getIntArray(), args[1].getInt()));
            return 0;
        case 0x012b: // putCharStreamUni
            streams.get(args[0].getInt()).putCharUni(args[1].getInt());
            return 0;
        case 0x012c: // putStringStreamUni
            streams.get(args[0].getInt()).putStringUni(new GlkIntArrayString(args[1].getStringUnicode()));
            return 0;
        case 0x012d: // putBufferStreamUni
            streams.get(args[0].getInt()).putBufferUni(withLength(args[1].getIntArray(), args[2].getInt()));
            return 0;
        case 0x0130: // getCharStreamUni
            return streams.get(args[0].getInt()).getCharUni();
        case 0x0131: // getBufferStreamUni
            return streams.get(args[0].getInt()).getBufferUni(withLength(args[1].getIntArray(), args[2].getInt()));
        case 0x0132: // getLineStreamUni
            return streams.get(args[0].getInt()).getLineUni(withLength(args[1].getIntArray(), args[2].getInt()));
        case 0x0138: // streamOpenFileUni
            return streams.add(glk.streamOpenFileUni(files.get(args[0].getInt()), args[1].getInt(), args[2].getInt()));
        case 0x0139: // streamOpenMemoryUni
            return streams.getPointer(glk.streamOpenMemoryUni(withLength(args[0].getIntArray(), args[1].getInt()), args[2].getInt(), args[3].getInt()));
        case 0x013a: // streamOpenResourceUni
            throw new RuntimeException("unimplemented");
        case 0x0140: // requestCharEventUni
            throw new RuntimeException("unimplemented");
        case 0x0141: // requestLineEventUni
            throw new RuntimeException("unimplemented");
        case 0x0150: // setEchoLineEvent
            throw new RuntimeException("unimplemented");
        case 0x0151: // setTerminatorsLineEvent
            throw new RuntimeException("unimplemented");
        case 0x0160: // currentTime
            throw new RuntimeException("unimplemented");
        case 0x0161: // currentSimpleTime
            throw new RuntimeException("unimplemented");
        case 0x0168: // timeToDateUtc
            throw new RuntimeException("unimplemented");
        case 0x0169: // timeToDateLocal
            throw new RuntimeException("unimplemented");
        case 0x016a: // simpleTimeToDateUtc
            throw new RuntimeException("unimplemented");
        case 0x016b: // simpleTimeToDateLocal
            throw new RuntimeException("unimplemented");
        case 0x016c: // dateToTimeUtc
            throw new RuntimeException("unimplemented");
        case 0x016d: // dateToTimeLocal
            throw new RuntimeException("unimplemented");
        case 0x016e: // dateToSimpleTimeUtc
            throw new RuntimeException("unimplemented");
        case 0x016f: // dateToSimpleTimeLocal
            throw new RuntimeException("unimplemented");
        default:
            throw new IllegalArgumentException("Unrecognized glk selector");
        }
    }

    public GlkWindow getWindow(int pointer) {
        return windows.get(pointer);
    }

    public GlkStream getStream(int pointer) {
        return streams.get(pointer);
    }

    public GlkFile getFile(int pointer) {
        return files.get(pointer);
    }

    public GlkSChannel getSChannel(int pointer) {
        return schannels.get(pointer);
    }

    private static GlkByteArray withLength(GlkByteArray arg, int length) {
        if (arg != null) {
            arg.setArrayLength(length);
        }
        return arg;
    }

    private static GlkIntArray withLength(GlkIntArray arg, int length) {
        if (arg != null) {
            arg.setArrayLength(length);
        }
        return arg;
    }

    private static void setStreamResult(GlkDispatchArgument arg, GlkStreamResult streamResult) {
        GlkIntArray intArray = arg.getIntArray();
        if (intArray != null) {
            intArray.setIntElement(streamResult.readCount);
            intArray.setIntElement(streamResult.writeCount);
        }
    }

    private void setEvent(GlkDispatchArgument arg, GlkEvent event) {
        GlkIntArray intArray = arg.getIntArray();
        if (intArray != null) {
            intArray.setIntElement(event.type);
            intArray.setIntElement(windows.getPointer(event.window));
            intArray.setIntElement(event.val1);
            intArray.setIntElement(event.val2);
        }
    }
}
