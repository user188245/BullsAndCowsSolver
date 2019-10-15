package com.user188245.bullsandcowssolver;

import java.util.*;

/**
 * @param <E> the custom unit digit.
 */
public class CustomUnitBCSolver<E>{

    private BullsAndCowsSolver solver;
    private final Map<Integer, E> itoe;
    private final Map<E, Integer> etoi;

    public CustomUnitBCSolver(BullsAndCowsSolver solver, List<E> unitSet) {
        this.solver = solver;
        this.itoe = new HashMap<>();
        this.etoi = new HashMap<>();
        for(int i=0; i<unitSet.size(); i++){
            this.itoe.put(i, unitSet.get(i));
            this.etoi.put(unitSet.get(i), i);
        }
    }

    public List<E> getCustomUnitSolution() {
        List<E> result = new ArrayList<>();
        Guess solution = solver.getSolution();
        for(int i=0; i< solution.size(); i++){
            result.add(itoe.get(solution.get(i)));
        }
        return result;
    }

    public void putCustomUnitClue(List<E> trial, int bulls, int cows) {
        if(trial.size() != solver.getBoxSize()){
            throw new RuntimeException("Trial box size must be " + solver.getBoxSize() +".");
        }
        Integer[] integers = new Integer[solver.getUnitSize()];
        for(int i=0; i< integers.length; i++){
            Integer j = etoi.get(trial.get(i));
            if(j != null){
                integers[i] = etoi.get(trial.get(i));
            }else{
                throw new RuntimeException("Invalid unit character : " + trial.get(i));
            }
        }
        solver.putClue(new Trial(GuessImpl.generate(integers),bulls,cows));
    }

    public void reset(){
        solver.reset();
    }

    public int getUnitSize() {
        return solver.getUnitSize();
    }

    public int getBoxSize() {
        return solver.getBoxSize();
    }

    public List<Trial> getHistory(){
        return solver.getHistory();
    }

}
