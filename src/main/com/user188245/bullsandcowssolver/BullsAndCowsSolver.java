package com.user188245.bullsandcowssolver;

import java.util.List;

public interface BullsAndCowsSolver {

    /**
     * @purity : pure
     * @return size of units. generally Numbers.
     */
    public int getUnitSize();

    /**
     * @purity : pure
     * @return size of units. generally Container of numbers.
     */
    public int getBoxSize();

    /**
     * @purity : pure
     * @return an arbitrary solution. it can be determined value or random value, it depends on implementation.
     */
    public Guess getSolution();

    /**
     * @purity : pure
     * @param trial - The result of trial.
     * All of information is used for computation of solution by Solver
     */
    public void putClue(Trial trial);

    /**
     * @purity : pure
     * @return history of trials.
     */
    public List<Trial> getHistory();

    /**
     * @purity : pure
     * @return clear all data and trial.
     */
    public void reset();

}
