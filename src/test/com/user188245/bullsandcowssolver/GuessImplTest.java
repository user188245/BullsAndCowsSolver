package com.user188245.bullsandcowssolver;

import com.user188245.bullsandcowssolver.Guess;
import com.user188245.bullsandcowssolver.GuessImpl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuessImplTest {

    @Test
    void equal() {
        Guess guess = GuessImpl.generate(1,2,3);
        Guess guess2 = GuessImpl.generate(1,2,3);
        assertEquals(guess,guess2);
    }

    @Test
    void size() {
        Guess guess = GuessImpl.generate(1,3,5);
        assertEquals(3, guess.size());
    }

    @Test
    void get() {
        Guess guess = GuessImpl.generate(1,3,5);
        assertEquals(5, guess.get(2));
    }

    @Test
    void set() {
        Guess guess = GuessImpl.generate(1,3,5,7,8,9);
        guess.set(1,10);
        assertEquals(10, guess.get(1));
    }

    @Test
    void contains() {
        Guess guess = GuessImpl.generate(1,3,5,7,8,9);
        assertTrue(guess.contains(3));
        assertFalse(guess.contains(11));
    }

    @Test
    void toArray() {
        Guess guess = GuessImpl.generate(1,3,5,7,8,9);
        Integer[] integers = guess.toArray();
        assertEquals(6,integers.length);
        assertEquals(5,integers[2]);
    }

    @Test
    void computeResult() {
        Guess guess1 = GuessImpl.generate(1,2,3,4);
        Guess guess2 = GuessImpl.generate(9,3,1,4);
        Guess guess3 = GuessImpl.generate(0,7,6,2);
        int[] check11 = guess1.computeResult(guess1);
        int[] check12 = guess1.computeResult(guess2);
        int[] check21 = guess2.computeResult(guess1);
        int[] check13 = guess1.computeResult(guess3);
        int[] check23 = guess2.computeResult(guess3);
        assertEquals(4,check11[0]);
        assertEquals(0,check11[1]);

        assertEquals(1,check12[0]);
        assertEquals(2,check12[1]);

        assertEquals(1,check21[0]);
        assertEquals(2,check21[1]);

        assertEquals(0,check13[0]);
        assertEquals(1,check13[1]);

        assertEquals(0,check23[0]);
        assertEquals(0,check23[1]);
    }
}