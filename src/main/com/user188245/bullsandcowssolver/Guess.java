package com.user188245.bullsandcowssolver;

import java.io.Serializable;

public interface Guess extends Cloneable, Serializable{

    /**
     * @purity : pure
     * @return size of Guess.
     */
    public int size();

    /**
     * @purity : pure
     * @return an item into Guess.
     */
    public Integer get(int index);

    /**
     * @purity : not pure
     * @param index
     * @param item
     * @return item, equivalent with second parameter
     * Change item into given index of the Guess.
     */
    public Integer set(int index, Integer item);

    /**
     * @purity : pure
     * @return returns true if Guess contains the item.
     */
    public boolean contains(Integer item);


    /**
     * @purity : pure
     * @return an array form of answer.
     */
    public Integer[] toArray();

    /**
     * @purity : pure
     * @return new Integer[2], result[0] = number of bulls, reuslt[1] = number of cows
     */
    public int[] computeResult(Guess realGuess) throws IndexOutOfBoundsException;


    public Object clone();



}
