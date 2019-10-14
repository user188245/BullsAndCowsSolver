package com.user188245.bullsandcowssolver;

import java.io.Serializable;

public class Trial implements Serializable {

    private Guess guess;
    private int bulls;
    private int cows;

    public int getBulls() {
        return bulls;
    }

    public int getCows() {
        return cows;
    }

    public Guess getGuess() {
        return guess;
    }

    public Trial(Guess guess, int bulls, int cows) {
        if(guess.size() < bulls + cows){
            throw new RuntimeException("Illegal Trial.");
        }
        this.guess = guess;
        this.bulls = bulls;
        this.cows = cows;
    }

    @Override
    public String toString() {
        return guess + ", [" + bulls + "A" + cows + "B]";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Trial){
            Trial t = (Trial)obj;
            return t.bulls == bulls && t.cows == cows && guess.equals(t.guess);
        }else{
            return false;
        }
    }
}
