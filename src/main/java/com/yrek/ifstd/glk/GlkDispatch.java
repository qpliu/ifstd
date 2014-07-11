package com.yrek.ifstd.glk;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class GlkDispatch {
    public final Glk glk;
    private final GlkObjectPool<GlkWindow> windows;
    private final GlkObjectPool<GlkStream> streams;
    private final GlkObjectPool<GlkFile> files;
    private final GlkObjectPool<GlkSChannel> schannels;

    public GlkDispatch(Glk glk) {
        this(glk, null, null, null, null);
    }

    public GlkDispatch(Glk glk, Map<Integer,GlkWindow> windows, Map<Integer,GlkStream> streams, Map<Integer,GlkFile> files, Map<Integer,GlkSChannel> schannels) {
        this.glk = glk;
        this.windows = new GlkObjectPool<GlkWindow>(windows);
        this.streams = new GlkObjectPool<GlkStream>(streams);
        this.files = new GlkObjectPool<GlkFile>(files);
        this.schannels = new GlkObjectPool<GlkSChannel>(schannels);
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
            GlkWindow window = windows.get(args[0].getInt());
            if (window != null) {
                return window.getRock();
            }
            return 0;
        case 0x0022: // windowGetRoot
            return windows.getPointer(glk.windowGetRoot());
        case 0x0023: // windowOpen
            window = glk.windowOpen(windows.get(args[0].getInt()), args[1].getInt(), args[2].getInt(), args[3].getInt(), args[4].getInt());
            if (window != null) {
                windows.add(window.getParent());
                streams.add(window.getStream());
            }
            return windows.getPointer(window);
        case 0x0024: // windowClose
            pointer = args[0].getInt();
            window = windows.get(pointer);
            setStreamResult(args[1], window.close());
            GlkStream stream = window.getStream();
            if (stream != null) {
                streams.destroy(stream.getPointer());
            }
            windows.destroy(pointer);
            return 0;
        case 0x0025: // windowGetSize
            window = windows.get(args[0].getInt());
            if (window != null) {
                GlkWindowSize windowSize = window.getSize();
                args[1].setInt(windowSize.width);
                args[2].setInt(windowSize.height);
            } else {
                args[1].setInt(0);
                args[2].setInt(0);
            }
            return 0;
        case 0x0026: // windowSetArrangement
            window = windows.get(args[0].getInt());
            if (window != null) {
                window.setArrangement(args[1].getInt(), args[2].getInt(), windows.get(args[3].getInt()));
            }
            return 0;
        case 0x0027: // windowGetArrangement
            window = windows.get(args[0].getInt());
            if (window != null) {
                GlkWindowArrangement windowArrangement = window.getArrangement();
                args[1].setInt(windowArrangement.method);
                args[2].setInt(windowArrangement.size);
                args[3].setInt(windows.getPointer(windowArrangement.key));
            } else {
                args[1].setInt(0);
                args[2].setInt(0);
                args[3].setInt(0);
            }
            return 0;
        case 0x0028: // windowGetType
            window = windows.get(args[0].getInt());
            if (window != null) {
                return window.getType();
            }
            return 0;
        case 0x0029: // windowGetParent
            window = windows.get(args[0].getInt());
            if (window != null) {
                return windows.getPointer(window.getParent());
            }
            return 0;
        case 0x002a: // windowClear
            window = windows.get(args[0].getInt());
            if (window != null) {
                window.clear();
            }
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
            window = windows.get(args[0].getInt());
            if (window != null) {
                return streams.getPointer(window.getStream());
            }
            return 0;
        case 0x002d: // windowSetEchoStream
            window = windows.get(args[0].getInt());
            if (window != null) {
                window.getStream().setEchoStream(streams.get(args[1].getInt()));
            }
            return 0;
        case 0x002e: // windowGetEchoStream
            window = windows.get(args[0].getInt());
            if (window != null) {
                return streams.getPointer(window.getStream().getEchoStream());
            }
            return 0;
        case 0x002f: // setWindow
            glk.setWindow(windows.get(args[0].getInt()));
            return 0;
        case 0x0030: // windowGetSibling
            window = windows.get(args[0].getInt());
            if (window != null) {
                return windows.getPointer(window.getSibling());
            }
            return 0;
        case 0x0040: // streamIterate
            pointer = streams.iterate(args[0].getInt());
            args[1].setInt(pointer == 0 ? 0 : streams.get(pointer).getRock());
            return pointer;
        case 0x0041: // streamGetRock
            stream = streams.get(args[0].getInt());
            if (stream != null) {
                return stream.getRock();
            }
            return 0;
        case 0x0042: // streamOpenFile
            GlkFile file = files.get(args[0].getInt());
            int mode = args[1].getInt();
            int rock = args[2].getInt();
            if (file != null) {
                return streams.add(glk.streamOpenFile(file, mode, rock));
            }
            return 0;
        case 0x0043: // streamOpenMemory
            return streams.add(glk.streamOpenMemory(withLength(args[0].getByteArray(), args[1].getInt()), args[2].getInt(), args[3].getInt()));
        case 0x0044: // streamClose
            pointer = args[0].getInt();
            stream = streams.get(pointer);
            if (stream != null) {
                setStreamResult(args[1], stream.close());
                streams.destroy(pointer);
            } else {
                setStreamResult(args[1], new GlkStreamResult(0, 0));
            }
            return 0;
        case 0x0045: // streamSetPosition
            stream = streams.get(args[0].getInt());
            int position = args[1].getInt();
            mode = args[2].getInt();
            if (stream != null) {
                stream.setPosition(position, mode);
            }
            return 0;
        case 0x0046: // streamGetPosition
            stream = streams.get(args[0].getInt());
            if (stream != null) {
                return stream.getPosition();
            }
            return 0;
        case 0x0047: // streamSetCurrent
            glk.streamSetCurrent(streams.get(args[0].getInt()));
            return 0;
        case 0x0048: // streamGetCurrent
            return streams.getPointer(glk.streamGetCurrent());
        case 0x0049: // streamOpenResource
            return streams.add(glk.streamOpenResource(args[0].getInt(), args[1].getInt()));
        case 0x0060: // filerefCreateTemp
            return files.add(glk.fileCreateTemp(args[0].getInt(), args[1].getInt()));
        case 0x0061: // filerefCreateByName
            return files.add(glk.fileCreateByName(args[0].getInt(), new GlkByteArrayString(args[1].getString()), args[2].getInt()));
        case 0x0062: // filerefCreateByPrompt
            return files.add(glk.fileCreateByPrompt(args[0].getInt(), args[1].getInt(), args[2].getInt()));
        case 0x0063: // filerefDestroy
            pointer = args[0].getInt();
            file = files.get(pointer);
            if (file != null) {
                file.destroy();
                files.destroy(pointer);
            }
            return 0;
        case 0x0064: // filerefIterate
            pointer = files.iterate(args[0].getInt());
            args[1].setInt(pointer == 0 ? 0 : files.get(pointer).getRock());
            return pointer;
        case 0x0065: // filerefGetRock
            file = files.get(args[0].getInt());
            if (file != null) {
                return file.getRock();
            }
            return 0;
        case 0x0066: // filerefDeleteFile
            file = files.get(args[0].getInt());
            if (file != null) {
                file.delete();
            }
            return 0;
        case 0x0067: // filerefDoesFileExist
            file = files.get(args[0].getInt());
            if (file != null) {
                return file.exists() ? 1 : 0;
            }
            return 0;
        case 0x0068: // filerefCreateFromFileref
            return files.add(glk.fileCreateFromFile(args[0].getInt(), files.get(args[1].getInt()), args[2].getInt()));
        case 0x0080: // putChar
            glk.putChar(args[0].getInt());
            return 0;
        case 0x0081: // putCharStream
            stream = streams.get(args[0].getInt());
            int ch = args[1].getInt();
            if (stream != null) {
                stream.putChar(ch);
            }
            return 0;
        case 0x0082: // putString
            glk.putString(new GlkByteArrayString(args[0].getString()));
            return 0;
        case 0x0083: // putStringStream
            stream = streams.get(args[0].getInt());
            GlkByteArray string = args[1].getString();
            if (stream != null) {
                stream.putString(new GlkByteArrayString(string));
            }
            return 0;
        case 0x0084: // putBuffer
            glk.putBuffer(withLength(args[0].getByteArray(), args[1].getInt()));
            return 0;
        case 0x0085: // putBufferStream
            stream = streams.get(args[0].getInt());
            GlkByteArray byteArray = withLength(args[1].getByteArray(), args[2].getInt());
            if (stream != null) {
                stream.putBuffer(byteArray);
            }
            return 0;
        case 0x0086: // setStyle
            glk.setStyle(args[0].getInt());
            return 0;
        case 0x0087: // setStyleStream
            stream = streams.get(args[0].getInt());
            int style = args[1].getInt();
            if (stream != null) {
                stream.setStyle(style);
            }
            return 0;
        case 0x0090: // getCharStream
            stream = streams.get(args[0].getInt());
            if (stream != null) {
                return stream.getChar();
            }
            return 0;
        case 0x0091: // getLineStream
            stream = streams.get(args[0].getInt());
            byteArray = withLength(args[1].getByteArray(), args[2].getInt());
            if (stream != null) {
                return stream.getLine(byteArray);
            }
            return 0;
        case 0x0092: // getBufferStream
            stream = streams.get(args[0].getInt());
            byteArray = withLength(args[1].getByteArray(), args[2].getInt());
            if (stream != null) {
                return stream.getBuffer(byteArray);
            }
            return 0;
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
            window = windows.get(args[0].getInt());
            int style1 = args[1].getInt();
            int style2 = args[2].getInt();
            if (window != null) {
                return window.styleDistinguish(style1, style2) ? 1 : 0;
            }
            return 0;
        case 0x00b3: // styleMeasure
            window = windows.get(args[0].getInt());
            style = args[1].getInt();
            int hint = args[2].getInt();
            Integer integer = null;
            if (window != null) {
                integer = window.styleMeasure(style, hint);
            }
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
            window = windows.get(args[0].getInt());
            byteArray = withLength(args[1].getByteArray(), args[2].getInt());
            int initLength = args[3].getInt();
            if (window != null) {
                window.requestLineEvent(byteArray, initLength);
            }
            return 0;
        case 0x00d1: // cancelLineEvent
            window = windows.get(args[0].getInt());
            if (window != null) {
                setEvent(args[1], window.cancelLineEvent());
            } else {
                setEvent(args[1], new GlkEvent(GlkEvent.TypeNone, null, 0, 0));
            }
            return 0;
        case 0x00d2: // requestCharEvent
            window = windows.get(args[0].getInt());
            if (window != null) {
                window.requestCharEvent();
            }
            return 0;
        case 0x00d3: // cancelCharEvent
            window = windows.get(args[0].getInt());
            if (window != null) {
                window.cancelCharEvent();
            }
            return 0;
        case 0x00d4: // requestMouseEvent
            window = windows.get(args[0].getInt());
            if (window != null) {
                window.requestMouseEvent();
            }
            return 0;
        case 0x00d5: // cancelMouseEvent
            window = windows.get(args[0].getInt());
            if (window != null) {
                window.cancelMouseEvent();
            }
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
            GlkSChannel schannel = schannels.get(args[0].getInt());
            if (schannel != null) {
                return schannel.getRock();
            }
            return 0;
        case 0x00f2: // sChannelCreate
            return schannels.add(glk.sChannelCreate(args[0].getInt()));
        case 0x00f3: // sChannelDestroy
            pointer = args[0].getInt();
            schannel = schannels.get(pointer);
            if (schannel != null) {
                schannel.destroyChannel();
                schannels.destroy(pointer);
            }
            return 0;
        case 0x00f4: // sChannelCreateExt
            return schannels.add(glk.sChannelCreateExt(args[0].getInt(), args[1].getInt()));
        case 0x00f7: // sChannelPlayMulti
            GlkIntArray intArray = args[0].getIntArray();
            GlkSChannel[] channels = new GlkSChannel[args[1].getInt()];
            for (int i = 0; i < channels.length; i++) {
                channels[i] = schannels.get(intArray.getIntElementAt(i));
            }
            glk.sChannelPlayMulti(channels, toIntArray(args[2].getIntArray(), args[3].getInt()), args[4].getInt() != 0);
            return 0;
        case 0x00f8: // sChannelPlay
            schannel = schannels.get(args[0].getInt());
            if (schannel != null) {
                return schannel.play(args[1].getInt()) ? 1 : 0;
            }
            return 0;
        case 0x00f9: // sChannelPlayExt
            schannel = schannels.get(args[0].getInt());
            resourceId = args[1].getInt();
            int repeats = args[2].getInt();
            boolean notify = args[3].getInt() != 0;
            if (schannel != null) {
                return schannel.playExt(resourceId, repeats, notify) ? 1 : 0;
            }
            return 0;
        case 0x00fa: // sChannelStop
            schannel = schannels.get(args[0].getInt());
            if (schannel != null) {
                schannel.stop();
            }
            return 0;
        case 0x00fb: // sChannelSetVolume
            schannel = schannels.get(args[0].getInt());
            int volume = args[1].getInt();
            if (schannel != null) {
                schannel.setVolume(volume);
            }
            return 0;
        case 0x00fc: // soundLoadHint
            glk.soundLoadHint(args[0].getInt(), args[1].getInt() != 0);
            return 0;
        case 0x00fd: // sChannelSetVolumeExt
            schannel = schannels.get(args[0].getInt());
            volume = args[1].getInt();
            int duration = args[2].getInt();
            notify = args[3].getInt() != 0;
            if (schannel != null) {
                schannel.setVolumeExt(volume, duration, notify);
            }
            return 0;
        case 0x00fe: // sChannelPause
            schannel = schannels.get(args[0].getInt());
            if (schannel != null) {
                schannel.pause();
            }
            return 0;
        case 0x00ff: // sChannelUnpause
            schannel = schannels.get(args[0].getInt());
            if (schannel != null) {
                schannel.unpause();
            }
            return 0;
        case 0x0100: // setHyperlink
            glk.setHyperlink(args[0].getInt());
            return 0;
        case 0x0101: // setHyperlinkStream
            stream = streams.get(args[0].getInt());
            int linkVal = args[1].getInt();
            if (stream != null) {
                stream.setHyperlink(linkVal);
            }
            return 0;
        case 0x0102: // requestHyperlinkEvent
            window = windows.get(args[0].getInt());
            if (window != null) {
                window.requestHyperlinkEvent();
            }
            return 0;
        case 0x0103: // cancelHyperlinkEvent
            window = windows.get(args[0].getInt());
            if (window != null) {
                window.cancelHyperlinkEvent();
            }
            return 0;
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
            stream = streams.get(args[0].getInt());
            ch = args[1].getInt();
            if (stream != null) {
                stream.putCharUni(ch);
            }
            return 0;
        case 0x012c: // putStringStreamUni
            stream = streams.get(args[0].getInt());
            intArray = args[1].getStringUnicode();
            if (stream != null) {
                stream.putStringUni(new GlkIntArrayString(intArray));
            }
            return 0;
        case 0x012d: // putBufferStreamUni
            stream = streams.get(args[0].getInt());
            buffer = withLength(args[1].getIntArray(), args[2].getInt());
            if (stream != null) {
                stream.putBufferUni(buffer);
            }
            return 0;
        case 0x0130: // getCharStreamUni
            stream = streams.get(args[0].getInt());
            if (stream != null) {
                return stream.getCharUni();
            }
            return 0;
        case 0x0131: // getBufferStreamUni
            stream = streams.get(args[0].getInt());
            buffer = withLength(args[1].getIntArray(), args[2].getInt());
            if (stream != null) {
                return stream.getBufferUni(buffer);
            }
            return 0;
        case 0x0132: // getLineStreamUni
            stream = streams.get(args[0].getInt());
            buffer = withLength(args[1].getIntArray(), args[2].getInt());
            if (stream != null) {
                return stream.getLineUni(buffer);
            }
            return 0;
        case 0x0138: // streamOpenFileUni
            file = files.get(args[0].getInt());
            mode = args[1].getInt();
            rock = args[2].getInt();
            if (file != null) {
                return streams.add(glk.streamOpenFileUni(file, mode, rock));
            }
            return 0;
        case 0x0139: // streamOpenMemoryUni
            return streams.add(glk.streamOpenMemoryUni(withLength(args[0].getIntArray(), args[1].getInt()), args[2].getInt(), args[3].getInt()));
        case 0x013a: // streamOpenResourceUni
            return streams.add(glk.streamOpenResourceUni(args[0].getInt(), args[1].getInt()));
        case 0x0140: // requestCharEventUni
            window = windows.get(args[0].getInt());
            if (window != null) {
                window.requestCharEventUni();
            }
            return 0;
        case 0x0141: // requestLineEventUni
            window = windows.get(args[0].getInt());
            intArray = withLength(args[1].getIntArray(), args[2].getInt());
            initLength = args[3].getInt();
            if (window != null) {
                window.requestLineEventUni(intArray, initLength);
            }
            return 0;
        case 0x0150: // setEchoLineEvent
            window = windows.get(args[0].getInt());
            flag = args[1].getInt() != 0;
            if (window != null) {
                window.setEchoLineEvent(flag);
            }
            return 0;
        case 0x0151: // setTerminatorsLineEvent
            window = windows.get(args[0].getInt());
            int[] keycodes = toIntArray(args[1].getIntArray(), args[2].getInt());
            if (window != null) {
                window.setTerminatorsLineEvent(keycodes);
            }
            return 0;
        case 0x0160: // currentTime
            setTimeValStruct(args[0], System.currentTimeMillis());
            return 0;
        case 0x0161: // currentSimpleTime
            return (int) (System.currentTimeMillis()/(args[0].getInt()*1000L));
        case 0x0168: // timeToDateUtc
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.setTimeInMillis(getTimeValStruct(args[0]));
            setDateStruct(args[1], cal);
            return 0;
        case 0x0169: // timeToDateLocal
            cal = Calendar.getInstance();
            cal.setTimeInMillis(getTimeValStruct(args[0]));
            setDateStruct(args[1], cal);
            return 0;
        case 0x016a: // simpleTimeToDateUtc
            cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.setTimeInMillis(1000L*args[0].getInt()*args[1].getInt());
            setDateStruct(args[2], cal);
            return 0;
        case 0x016b: // simpleTimeToDateLocal
            cal = Calendar.getInstance();
            cal.setTimeInMillis(1000L*args[0].getInt()*args[1].getInt());
            setDateStruct(args[2], cal);
            return 0;
        case 0x016c: // dateToTimeUtc
            cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            getDateStruct(args[0], cal);
            setTimeValStruct(args[0], cal.getTimeInMillis());
            return 0;
        case 0x016d: // dateToTimeLocal
            cal = Calendar.getInstance();
            getDateStruct(args[0], cal);
            setTimeValStruct(args[0], cal.getTimeInMillis());
            return 0;
        case 0x016e: // dateToSimpleTimeUtc
            cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            getDateStruct(args[0], cal);
            return (int) (cal.getTimeInMillis()/(1000L*args[1].getInt()));
        case 0x016f: // dateToSimpleTimeLocal
            cal = Calendar.getInstance();
            getDateStruct(args[0], cal);
            return (int) (cal.getTimeInMillis()/(1000L*args[1].getInt()));
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

    public void saveToMap(Map<Integer,GlkWindow> windows, Map<Integer,GlkStream> streams, Map<Integer,GlkFile> files, Map<Integer,GlkSChannel> schannels) {
        this.windows.saveToMap(windows);
        this.streams.saveToMap(streams);
        this.files.saveToMap(files);
        this.schannels.saveToMap(schannels);
    }

    public List<GlkWindow> windowList() {
        return windows.toList();
    }

    public List<GlkStream> streamList() {
        return streams.toList();
    }

    public List<GlkFile> fileList() {
        return files.toList();
    }

    public List<GlkSChannel> sChannelList() {
        return schannels.toList();
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

    private static int[] toIntArray(GlkIntArray arg, int length) {
        if (arg == null) {
            return null;
        }
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = arg.getIntElementAt(i);
        }
        return result;
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

    private void setTimeValStruct(GlkDispatchArgument arg, long time) {
        GlkIntArray intArray = arg.getIntArray();
        if (intArray != null) {
            intArray.setIntElement((int) ((time/1000L) >> 32));
            intArray.setIntElement((int) (time/1000L));
            intArray.setIntElement((int) (time%1000L)*1000);
        }
    }

    private long getTimeValStruct(GlkDispatchArgument arg) {
        GlkIntArray intArray = arg.getIntArray();
        long time = 0;
        if (intArray != null) {
            time |= intArray.getIntElement() << 32;
            time |= intArray.getIntElement() & 0xffffffffL;
            time *= 1000L;
            time += intArray.getIntElement() / 1000;
        }
        return time;
    }

    private void setDateStruct(GlkDispatchArgument arg, Calendar cal) {
        GlkIntArray intArray = arg.getIntArray();
        if (intArray != null) {
            intArray.setIntElement(cal.get(Calendar.YEAR));
            intArray.setIntElement(cal.get(Calendar.MONTH)+1);
            intArray.setIntElement(cal.get(Calendar.DAY_OF_MONTH));
            intArray.setIntElement(cal.get(Calendar.DAY_OF_WEEK)-1);
            intArray.setIntElement(cal.get(Calendar.HOUR_OF_DAY));
            intArray.setIntElement(cal.get(Calendar.MINUTE));
            intArray.setIntElement(cal.get(Calendar.SECOND));
            intArray.setIntElement(cal.get(Calendar.MILLISECOND)*1000);
        }
    }

    private void getDateStruct(GlkDispatchArgument arg, Calendar cal) {
        GlkIntArray intArray = arg.getIntArray();
        if (intArray != null) {
            cal.set(Calendar.YEAR, intArray.getIntElement());
            cal.set(Calendar.MONTH, intArray.getIntElement()-1);
            cal.set(Calendar.DAY_OF_MONTH, intArray.getIntElement());
            intArray.getIntElement(); // DAY_OF_WEEK
            cal.set(Calendar.HOUR_OF_DAY, intArray.getIntElement());
            cal.set(Calendar.MINUTE, intArray.getIntElement());
            cal.set(Calendar.SECOND, intArray.getIntElement());
            cal.set(Calendar.MILLISECOND, intArray.getIntElement()/1000);
        }
    }
}
