package com.user188245.bullsandcowssolver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Cacheable BCSolver
 */
public interface CacheableBCSolver extends BullsAndCowsSolver {

    public BCSCache getCache();

    public void bulkLoad(BCSCache cache);

    public void bulkLoad(FileInputStream fis);

    default void bulkLoad(String path){
        try {
            bulkLoad(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("failed to load");
        }
    }

    public void dump(String path);

}
