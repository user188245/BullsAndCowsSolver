package com.user188245.bullsandcowssolver;

import java.util.List;

public interface StrongBCSCache extends BCSCache{

    /**
     * Select All guess by key
     * @param history as key
     * @return Guess as Value
     */
    public List<Guess> getAll(List<Trial> history);

    /**
     * Select one guess among values with same key randomly
     * @param history as key
     * @return Guess as Value
     */
    @Override
    Guess get(List<Trial> history);

    /**
     * Put all history-guess pairs into the cache.
     * @param history as Key
     * @param guess as Value
     */
    public void putAll(List<Trial> history, List<Guess> guess);

    /**
     * Put history-guess pair into the cache.
     * @param history as Key
     * @param guess as Value
     */
    @Override
    void put(List<Trial> history, Guess guess);
}
