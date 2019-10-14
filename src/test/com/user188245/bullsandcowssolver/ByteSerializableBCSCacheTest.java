package com.user188245.bullsandcowssolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class ByteSerializableBCSCacheTest extends BCSCacheTest{

    ByteSerializableBCSCache byteSerializableBCSCache;

    @BeforeEach
    void before(){
        byteSerializableBCSCache = build();
        super.before();
    }

    @Test
    void serializeAndDeserialize() {
        byte[] serializedData = byteSerializableBCSCache.serialize();
        assertNotNull(serializedData);
        assertNotEquals(0, serializedData.length);
        ByteSerializableBCSCache byteSerializableBCSCache2 = ByteSerializableBCSCache.deserialize(serializedData);
        assertNotNull(byteSerializableBCSCache2);
        assertEquals(byteSerializableBCSCache.getBoxSize(), byteSerializableBCSCache2.getBoxSize());
        assertEquals(byteSerializableBCSCache.getUnitSize(), byteSerializableBCSCache2.getUnitSize());
    }

    abstract ByteSerializableBCSCache build();


}