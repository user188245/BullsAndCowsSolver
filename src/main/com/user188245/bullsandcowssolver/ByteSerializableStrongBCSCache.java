package com.user188245.bullsandcowssolver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

interface ByteSerializableStrongBCSCache extends StrongBCSCache {

    /**
     * Serialize current instance as byteArray.
     * @return Serialized ByteArray
     */
    public byte[] serialize();

    /**
     * Deserialize byteArray as current instance.
     * @return Serialized ByteArray
     */
    public static ByteSerializableBCSCache deserialize(byte[] data){
        try {
            ByteArrayInputStream is1 = new ByteArrayInputStream(data);
            ObjectInputStream is2 = new ObjectInputStream(is1);
            ByteSerializableBCSCache byteSerializableBCSCache = (ByteSerializableBCSCache)is2.readObject();
            is2.close();
            is1.close();
            return byteSerializableBCSCache;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to deserialize.\nCause : " + e.getMessage());
        }
    };

}


