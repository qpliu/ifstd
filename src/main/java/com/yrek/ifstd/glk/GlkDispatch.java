package com.yrek.ifstd.glk;

public class GlkDispatch {
    public final Glk glk;
    private final GlkObjectPool<GlkWindow> windows = new GlkObjectPool<GlkWindow>();
    private final GlkObjectPool<GlkStream> streams = new GlkObjectPool<GlkStream>();
    private final GlkObjectPool<GlkFile> files = new GlkObjectPool<GlkFile>();
    private final GlkObjectPool<GlkSChannel> schannels = new GlkObjectPool<GlkSChannel>();

    public GlkDispatch(Glk glk) {
        this.glk = glk;
    }

    public int dispatch(int selector, GlkArg[] args) {
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
            throw new RuntimeException("unimplemented");
        case 0x0020: // windowIterate
            return windows.iterate(args[0].getInt());
        case 0x0021: // windowGetRock
            return windows.get(args[0].getInt()).getRock();
        case 0x0022: // windowGetRoot
            throw new RuntimeException("unimplemented");
        case 0x0023: // windowOpen
            throw new RuntimeException("unimplemented");
        case 0x0024: // windowClose
            int pointer = args[0].getInt();
            GlkWindow win = windows.get(pointer);
            args[1].setStreamResult(win.close());
            streams.destroy(win.getStream().getPointer());
            windows.destroy(pointer);
            return 0;
        case 0x0025: // windowGetSize
            throw new RuntimeException("unimplemented");
        case 0x0026: // windowSetArrangement
            throw new RuntimeException("unimplemented");
        case 0x0027: // windowGetArrangement
            throw new RuntimeException("unimplemented");
        case 0x0028: // windowGetType
            throw new RuntimeException("unimplemented");
        case 0x0029: // windowGetParent
            return windows.getPointer(windows.get(args[0].getInt()).getParent());
        case 0x002a: // windowClear
            windows.get(args[0].getInt()).clear();
            return 0;
        case 0x002b: // windowMoveCursor
            windows.get(args[0].getInt()).moveCursor(args[1].getInt(), args[2].getInt());
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
            return streams.iterate(args[0].getInt());
        case 0x0041: // streamGetRock
            return streams.get(args[0].getInt()).getRock();
        case 0x0042: // streamOpenFile
            return streams.add(glk.streamOpenFile(files.get(args[0].getInt()), args[1].getInt(), args[2].getInt()));
        case 0x0043: // streamOpenMemory
            throw new RuntimeException("unimplemented");
        case 0x0044: // streamClose
            pointer = args[0].getInt();
            GlkStream str = streams.get(pointer);
            args[1].setStreamResult(str.close());
            streams.destroy(pointer);
            return 0;
        case 0x0045: // streamSetPosition
            throw new RuntimeException("unimplemented");
        case 0x0046: // streamGetPosition
            throw new RuntimeException("unimplemented");
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
            return files.add(glk.fileCreateByName(args[0].getInt(), args[1], args[2].getInt()));
        case 0x0062: // filerefCreateByPrompt
            return files.add(glk.fileCreateByPrompt(args[0].getInt(), args[1].getInt(), args[2].getInt()));
        case 0x0063: // filerefDestroy
            pointer = args[0].getInt();
            files.get(pointer).destroy();
            files.destroy(pointer);
            return 0;
        case 0x0064: // filerefIterate
            return files.iterate(args[0].getInt());
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
            throw new RuntimeException("unimplemented");
        case 0x0081: // putCharStream
            throw new RuntimeException("unimplemented");
        case 0x0082: // putString
            throw new RuntimeException("unimplemented");
        case 0x0083: // putStringStream
            throw new RuntimeException("unimplemented");
        case 0x0084: // putBuffer
            throw new RuntimeException("unimplemented");
        case 0x0085: // putBufferStream
            throw new RuntimeException("unimplemented");
        case 0x0086: // setStyle
            throw new RuntimeException("unimplemented");
        case 0x0087: // setStyleStream
            throw new RuntimeException("unimplemented");
        case 0x0090: // getCharStream
            throw new RuntimeException("unimplemented");
        case 0x0091: // getLineStream
            throw new RuntimeException("unimplemented");
        case 0x0092: // getBufferStream
            throw new RuntimeException("unimplemented");
        case 0x00a0: // charToLower
            throw new RuntimeException("unimplemented");
        case 0x00a1: // charToUpper
            throw new RuntimeException("unimplemented");
        case 0x00b0: // stylehintSet
            throw new RuntimeException("unimplemented");
        case 0x00b1: // stylehintClear
            throw new RuntimeException("unimplemented");
        case 0x00b2: // styleDistinguish
            throw new RuntimeException("unimplemented");
        case 0x00b3: // styleMeasure
            throw new RuntimeException("unimplemented");
        case 0x00c0: // select
            throw new RuntimeException("unimplemented");
        case 0x00c1: // selectPoll
            throw new RuntimeException("unimplemented");
        case 0x00d0: // requestLineEvent
            throw new RuntimeException("unimplemented");
        case 0x00d1: // cancelLineEvent
            throw new RuntimeException("unimplemented");
        case 0x00d2: // requestCharEvent
            throw new RuntimeException("unimplemented");
        case 0x00d3: // cancelCharEvent
            throw new RuntimeException("unimplemented");
        case 0x00d4: // requestMouseEvent
            throw new RuntimeException("unimplemented");
        case 0x00d5: // cancelMouseEvent
            throw new RuntimeException("unimplemented");
        case 0x00d6: // requestTimerEvents
            throw new RuntimeException("unimplemented");
        case 0x00e0: // imageGetInfo
            throw new RuntimeException("unimplemented");
        case 0x00e1: // imageDraw
            throw new RuntimeException("unimplemented");
        case 0x00e2: // imageDrawScaled
            throw new RuntimeException("unimplemented");
        case 0x00e8: // windowFlowBreak
            throw new RuntimeException("unimplemented");
        case 0x00e9: // windowEraseRect
            throw new RuntimeException("unimplemented");
        case 0x00ea: // windowFillRect
            throw new RuntimeException("unimplemented");
        case 0x00eb: // windowSetBackgroundColor
            throw new RuntimeException("unimplemented");
        case 0x00f0: // sChannelIterate
            throw new RuntimeException("unimplemented");
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
            throw new RuntimeException("unimplemented");
        case 0x0121: // bufferToUpperCaseUni
            throw new RuntimeException("unimplemented");
        case 0x0122: // bufferToTitleCaseUni
            throw new RuntimeException("unimplemented");
        case 0x0123: // bufferCanonDecomposeUni
            throw new RuntimeException("unimplemented");
        case 0x0124: // bufferCanonNormalizeUni
            throw new RuntimeException("unimplemented");
        case 0x0128: // putCharUni
            throw new RuntimeException("unimplemented");
        case 0x0129: // putStringUni
            throw new RuntimeException("unimplemented");
        case 0x012a: // putBufferUni
            throw new RuntimeException("unimplemented");
        case 0x012b: // putCharStreamUni
            throw new RuntimeException("unimplemented");
        case 0x012c: // putStringStreamUni
            throw new RuntimeException("unimplemented");
        case 0x012d: // putBufferStreamUni
            throw new RuntimeException("unimplemented");
        case 0x0130: // getCharStreamUni
            throw new RuntimeException("unimplemented");
        case 0x0131: // getBufferStreamUni
            throw new RuntimeException("unimplemented");
        case 0x0132: // getLineStreamUni
            throw new RuntimeException("unimplemented");
        case 0x0138: // streamOpenFileUni
            throw new RuntimeException("unimplemented");
        case 0x0139: // streamOpenMemoryUni
            throw new RuntimeException("unimplemented");
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
}
