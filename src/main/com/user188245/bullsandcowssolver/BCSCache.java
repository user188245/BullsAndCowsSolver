package com.user188245.bullsandcowssolver;

import java.util.List;

/**
 * BCSCache
 */
interface BCSCache{

    /**
     * get guess using the key
     * @param history as key
     * @return Guess as Value or Null if not exist in cache.
     */
    public Guess get(List<Trial> history);

    /**
     * Put history-guess pair into the cache, If the value with current key already exists, it will be ignored.
     * @param history as Key
     * @param guess as Value
     */
    public void put(List<Trial> history, Guess guess);

    /**
     *
     * @return the number of items into cache
     */
    public int size();

    /**
     *
     * @return box size.
     */
    public int getBoxSize();

    /**
     *
     * @return unit size.
     */
    public int getUnitSize();

    /**
     *
     * @param cache source for comparison
     * @return true if the cache consist of equivalent game parameter to this instance. otherwise false.
     */
    public boolean adaptable(BCSCache cache);

}
