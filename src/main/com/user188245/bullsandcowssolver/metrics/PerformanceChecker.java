package com.user188245.bullsandcowssolver.metrics;

import com.user188245.bullsandcowssolver.BullsAndCowsSolver;
import com.user188245.bullsandcowssolver.Guess;
import com.user188245.bullsandcowssolver.GuessImpl;
import com.user188245.bullsandcowssolver.Trial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PerformanceChecker {

    private BullsAndCowsSolver bcSolver;

    private int count;

    private float averageTrial;

    private int maximumTrial;

    private List<Trial> maximumTrialExample;

    private long elapsedTime;

    List<Integer> unitList;

    PerformanceChecker(BullsAndCowsSolver bcSolver){
        unitList = new ArrayList<>();
        for(int i=0; i<bcSolver.getUnitSize(); i++){
            unitList.add(i);
        }
        this.bcSolver = bcSolver;
        this.maximumTrialExample = new ArrayList<>(10);
        count = 0;
        averageTrial = 0.0f;
        maximumTrial = 0;
        elapsedTime = 0;
    }

    void test(int count){
        long time = System.currentTimeMillis();
        int box = bcSolver.getBoxSize();
        Guess answer;
        Guess guess;
        int[] guessResult;
        long totalTrial = 0;
        int totalCount = 0;

        while(totalCount < count){
            bcSolver.reset();
            Collections.shuffle(unitList);
            answer = GuessImpl.generate(unitList.subList(0,box));
            guess = bcSolver.getSolution();
            while((guessResult = answer.computeResult(guess))[0] != box){
                bcSolver.putClue(new Trial(guess,guessResult[0],guessResult[1]));
                guess = bcSolver.getSolution();
            }
            bcSolver.putClue(new Trial(guess,guessResult[0],guessResult[1]));
            int trialSize = bcSolver.getHistory().size();
            if(trialSize > this.maximumTrial){
                this.maximumTrial = trialSize;
                this.maximumTrialExample.clear();
                for(Trial trial : bcSolver.getHistory()){
                    this.maximumTrialExample.add(trial);
                }
            }
            totalTrial += trialSize;
            totalCount++;
        }
        elapsedTime += (System.currentTimeMillis() - time);
        this.averageTrial = (this.averageTrial*this.count+totalTrial)/(totalCount+this.count);
        this.count += totalCount;
    }

    void showResult(){
        Collections.sort(unitList);
        System.out.println("Size of Blank : " + bcSolver.getBoxSize());
        System.out.println("Digit : " + unitList);
        System.out.println("test count : " + count);
        System.out.println("averageTrial per Game: " + averageTrial);
        System.out.println("maximumTrial on game: " + maximumTrial);
        System.out.println("maximumTrial example: " + maximumTrialExample);
        System.out.println("total elapsed time: " + elapsedTime + "ms");

    }





}
