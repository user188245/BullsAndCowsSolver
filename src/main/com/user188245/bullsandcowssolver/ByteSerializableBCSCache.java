package com.user188245.bullsandcowssolver;

import java.io.*;

/**
 * ByteSerializable
 */
interface ByteSerializableBCSCache extends BCSCache{

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
    }

    public static ByteSerializableBCSCache deserialize(FileInputStream fis){
        try {
            int available = fis.available();
            byte[] bytes = new byte[available];
            if(fis.read(bytes)<0){
                throw new IOException();
            }
            return ByteSerializableBCSCache.deserialize(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize.\nCause : " + e.getMessage());
        }
    }

    public static ByteSerializableBCSCache deserialize(String filepath){
        try {
            return ByteSerializableBCSCache.deserialize(new FileInputStream(filepath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to deserialize.\nCause : " + e.getMessage());
        }
    }

}
