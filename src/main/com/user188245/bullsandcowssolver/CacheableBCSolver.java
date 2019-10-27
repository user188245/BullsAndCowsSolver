package com.user188245.bullsandcowssolver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Cacheable BCSolver
 *
 * Technically, "BCSolver with Tabulation" is exact wording rather than "Cacheable BCSolver" in case.
 *
 */
public class CacheableBCSolver implements BullsAndCowsSolver{

    private BullsAndCowsSolver solver;
    private ByteSerializableBCSCache cache;
    private List<Trial> history;
    private Queue<Trial> trialBuffer;

    public CacheableBCSolver(BullsAndCowsSolver solver, ByteSerializableBCSCache cache) {
        this.solver = solver;
        this.cache = cache;
        this.history = new ArrayList<>();
        this.trialBuffer = new LinkedList<>();
    }

    @Override
    public int getUnitSize() {
        return solver.getUnitSize();
    }

    @Override
    public int getBoxSize() {
        return solver.getBoxSize();
    }

    @Override
    public Guess getSolution() {
        Guess guess = cache.get(history);
        if(guess == null){
            while(!trialBuffer.isEmpty()){
                solver.putClue(trialBuffer.remove());
            }
            guess = solver.getSolution();
        }
        return guess;
    }

    @Override
    public void putClue(Trial trial) {
        history.add(trial);
        if(trial.getBulls() == solver.getBoxSize()){
            cache.put(history,trial.getGuess());
        }
        trialBuffer.add(trial);
    }

    @Override
    public List<Trial> getHistory() {
        return history;
    }

    @Override
    public void reset() {
        trialBuffer.clear();
        history.clear();
        solver.reset();
    }

    public ByteSerializableBCSCache getCache(){
        return this.cache;
    }
}
