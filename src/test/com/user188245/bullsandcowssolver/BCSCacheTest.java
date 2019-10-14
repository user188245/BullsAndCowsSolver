package com.user188245.bullsandcowssolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class BCSCacheTest {

    BCSCache bcsCache;
    Guess rootGuess;

    abstract BCSCache build(Guess rootGuess, int unitSize);

    @BeforeEach
    void before(){
        rootGuess = GuessImpl.generate(1,2,3,4,5);
        bcsCache = build(rootGuess,12);
    };

    @Test
    void putAndGetAndSize() {
        List<Trial> history = new ArrayList<>();
        history.add(new Trial(rootGuess,1,2));
        history.add(new Trial(GuessImpl.generate(6,7,8,9,10),0,1));
        history.add(new Trial(GuessImpl.generate(1,3,5,7,9),2,1));
        history.add(new Trial(GuessImpl.generate(2,4,6,8,10),5,0));
        bcsCache.put(history, GuessImpl.generate(9,8,7,6,5));
        assertEquals(bcsCache.size(),4);
        Guess guess = bcsCache.get(history.subList(0,3));
        assertEquals(GuessImpl.generate(2,4,6,8,10), guess);
        guess = bcsCache.get(history.subList(0,4));
        assertEquals(GuessImpl.generate(9,8,7,6,5),guess);
        guess = bcsCache.get(history.subList(1,2));
        assertNull(guess);
        history.set(0,new Trial(rootGuess,0,2));
        history.set(1,new Trial(GuessImpl.generate(8,9,10,9,8),0,2));
        history.set(2,new Trial(GuessImpl.generate(8,1,7,2,6),2,2));
        history.remove(3);
        bcsCache.put(history, GuessImpl.generate(5,8,1,6,0));
        assertEquals(7, bcsCache.size());
        guess = bcsCache.get(history.subList(0,1));
        assertEquals(GuessImpl.generate(8,9,10,9,8),guess);
        guess = bcsCache.get(history);
        assertEquals(GuessImpl.generate(5,8,1,6,0),guess);
        bcsCache.put(history, GuessImpl.generate(2,7,3,8,4));
        guess = bcsCache.get(history);
        assertEquals(GuessImpl.generate(5,8,1,6,0),guess);
    }

    @Test
    void getBoxSize() {
        assertEquals(bcsCache.getBoxSize(),5);
    }

    @Test
    void getUnitSize() {
        assertEquals(bcsCache.getUnitSize(),12);
    }

    @Test
    void adaptable() {
        assertTrue(bcsCache.adaptable(bcsCache));
    }

}