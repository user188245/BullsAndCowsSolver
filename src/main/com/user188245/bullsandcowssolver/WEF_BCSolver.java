package com.user188245.bullsandcowssolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Wildcard Exhaustion First Algorithm
 */
public class WEF_BCSolver extends BCSolverV3{

    public WEF_BCSolver(int unitSize, int boxSize) {
        super(unitSize, boxSize);
    }

    @Override
    public Guess getSolution() {
        Guess solution;
        Collections.shuffle(wildCardList);
        int highestWcCount = 0;
        if(answerableSet.isEmpty()){
            if(wildCardList.size()<boxSize){
                return null;
            }
            Integer[] integers = new Integer[boxSize];
            Arrays.fill(integers,WILDCARD);
            solution = GuessImpl.generate(integers);
        }else{
            if(wildCardList.size() > 1){
                solution = answerableSet.get(0);
                for(Guess guess : answerableSet){
                    int WcCount = 0;
                    for(int i=0; i<guess.size(); i++){
                        if(guess.get(i).equals(WILDCARD)) {
                            WcCount++;
                        }
                    }
                    if(WcCount>highestWcCount){
                        highestWcCount = WcCount;
                        solution = guess;
                    }
                }
            }else{
                solution = answerableSet.get(new Random().nextInt(answerableSet.size()));
            }
        }
        Guess newSolution;
        newSolution = (Guess)solution.clone();
        for(int i=0,j=0; i<solution.size(); i++){
            if(newSolution.get(i).equals(WILDCARD)){
                newSolution.set(i,wildCardList.get(j++));
            }
        }
        return newSolution;
    }

}
