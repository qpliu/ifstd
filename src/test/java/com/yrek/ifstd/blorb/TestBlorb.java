package com.yrek.ifstd.blorb;

import java.io.File;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

public class TestBlorb {
    @Test
    public void testFileBlorb() throws Exception {
        testBlorb(Blorb.from(new File(getClass().getResource("/BeingSteve.zblorb").toURI())));
    }

    @Test
    public void testByteArrayBlorb() throws Exception {
        byte[] bytes = new byte[124810];
        getClass().getResourceAsStream("/BeingSteve.zblorb").read(bytes);
        testBlorb(Blorb.from(bytes));
    }

    private void testBlorb(Blorb blorb) throws Exception {
        int count = 0;
        HashMap<Integer,Integer> chunks = new HashMap<Integer,Integer>();
        for (Blorb.Chunk chunk : blorb.chunks()) {
            count++;
            chunks.put(chunk.getId(), chunk.getLength());
        }
        Assert.assertEquals(3, count);
        Assert.assertEquals(3, chunks.size());
        Assert.assertEquals(16, chunks.get(Blorb.RIdx).intValue());
        Assert.assertEquals(123392, chunks.get(Blorb.ZCOD).intValue());
        Assert.assertEquals(1365, chunks.get(Blorb.IFmd).intValue());
        count = 0;
        for (Blorb.Resource resource : blorb.resources()) {
            count++;
            Assert.assertEquals(Blorb.Exec, resource.getUsage());
            Assert.assertEquals(0, resource.getNumber());
            Assert.assertEquals(Blorb.ZCOD, resource.getChunk().getId());
            Assert.assertEquals(123392, resource.getChunk().getLength());
        }
        Assert.assertEquals(1, count);
    }
}
