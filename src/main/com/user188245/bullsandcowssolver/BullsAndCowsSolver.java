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
     * @return size of boxes. generally Container of numbers.
     */
    public int getBoxSize();

    /**
     * @purity : pure
     * @return an arbitrary solution. it can be determined value or random value, it depends on implementation.
     */
    public Guess getSolution();

    /**
     * @purity : pure
     * All of information is used for computation of solution by Solver
     */
    public void putClue(Trial trial);

    /**
     * @purity : pure
     * @return sequential history of trials.
     */
    public List<Trial> getHistory();

    /**
     * @purity : pure
     * Clear all data and trial.
     */
    public void reset();

}
