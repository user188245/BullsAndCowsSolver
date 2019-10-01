package com.user188245.bullsandcowssolver;

import java.util.*;

/**
 * Greedy-Searching Algorithm
 */

public class BCSolverV3 implements BullsAndCowsSolver {

    protected int unitSize;

    protected int boxSize;

    protected final static Integer WILDCARD = -1;

    protected List<Guess> answerableSet;

    protected List<Integer> wildCardList;

    protected List<Trial> history;

    public class WCAnswerProcessUnit extends AnswerProcessUnit {

        WCAnswerProcessUnit(Guess guess, int bullsCount, int cowsCount) {
            super(guess, bullsCount, cowsCount);
        }

        void fillWildCard(){
            super.fillWildCard(WCAnswerProcessUnit.CONVERTOR.get(WILDCARD));
            this.bullsCount =0;
        }
    }

    public BCSolverV3(int unitSize, int boxSize) {
        this.unitSize = unitSize;
        this.boxSize = boxSize;
        this.wildCardList = new ArrayList<>();
        for (int i = 0; i < unitSize; i++) {
            this.wildCardList.add(i);
        }
        this.answerableSet = new ArrayList<>();
        history = new ArrayList<>();
    }

    @Override
    public int getUnitSize() {
        return unitSize;
    }

    @Override
    public int getBoxSize() {
        return boxSize;
    }

    @Override
    public Guess getSolution() {
        Guess solution;
        Collections.shuffle(wildCardList);
        if(answerableSet.isEmpty()){
            Integer[] integers = new Integer[boxSize];
            Arrays.fill(integers,WILDCARD);
            solution = GuessImpl.generate(integers);
        }else{
            solution = answerableSet.get(new Random().nextInt(answerableSet.size()));
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

    @Override
    public void putClue(Trial trial) {
        Guess guess = trial.getGuess();
        int bulls = trial.getBulls();
        int cows = trial.getCows();
        Integer[] sortedInput = guess.toArray();
        Arrays.sort(sortedInput);
        for(int i=0; i<sortedInput.length-1; i++){
            if(sortedInput[i].equals(sortedInput[i+1])){
                throw new IllegalArgumentException("Each items must be unique.");
            }
        }
        List<Integer> answer2 = (GuessImpl) guess;
        if(bulls > 0 || cows > 0){
            Iterable<Guess> clues = getClue(guess,bulls,cows);
            if(answerableSet.isEmpty()){
                clues.forEach(answerableSet::add);
            }else{
                Set<Integer> wcLeft = new HashSet<>(this.wildCardList);
                List<Integer> wcr = new ArrayList<>();
                for (int i = 0; i < unitSize; i++) {
                    wcr.add(i);
                }
                wcr.removeIf(answer2::contains);
                Set<Integer> wcRight = new HashSet<>(wcr);
                List<Guess> newAnswerableSet = intersect(answerableSet, clues, wcLeft, wcRight);
                answerableSet.clear();
                answerableSet = null;
                answerableSet = newAnswerableSet;
            }
        }else{
            for(Integer item : sortedInput){
                answerableSet.removeIf(x->x.contains(item));
            }
        }
        this.wildCardList.removeIf(answer2::contains);
        int wcSize = this.wildCardList.size();
        if(wcSize < boxSize){
            for(int i=0; i<answerableSet.size(); i++){
                int count = 0;
                Integer[] answer = answerableSet.get(i).toArray();
                for(Integer integer : answer){
                    if(integer.equals(WILDCARD)){
                        count++;
                    }
                }
                if(count > wcSize){
                    answerableSet.remove(i--);
                }
            }
        }
        history.add(trial);
    }

    @Override
    public List<Trial> getHistory() {
        return history;
    }

    @Override
    public void reset() {
        this.wildCardList.clear();
        for (int i = 0; i < unitSize; i++) {
            this.wildCardList.add(i);
        }
        this.answerableSet.clear();
        this.history.clear();
    }

    void printAnswerable(){
        System.out.println("wc : " + wildCardList);
        answerableSet.forEach(x->{
            for(Integer i : (GuessImpl)x){
                System.out.print(i + " ");
            }
            System.out.println();
        });
    }

    private Iterable<Guess> getClue(Guess guess, int bulls, int cows){
        Set<Guess> result = new HashSet<>();
        Queue<AnswerProcessUnit> queue = new LinkedList<>();
        queue.add(new WCAnswerProcessUnit(guess,bulls,cows));
        while(!queue.isEmpty()){
            try {
                WCAnswerProcessUnit apu = (WCAnswerProcessUnit)queue.remove();
                if (apu.bullsCount > 0) {
                    queue.addAll(apu.reduceBulls(guess));
                }else if(apu.cowsCount > 0){
                    queue.addAll(apu.reduceCows());
                }else{
                    apu.fillWildCard();
                    result.add(apu.build());
                }
            }catch(CloneNotSupportedException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    private static List<Guess> intersect(Iterable<Guess> a, Iterable<Guess> b, Set<Integer> wildcardListLeft, Set<Integer> wildcardListRight){
        List<Guess> result = new ArrayList<>();
        a.forEach(x->{
            b.forEach(y->{
                GuessImpl out = new GuessImpl(x.size());
                boolean eq = true;
                for(int i=0; i<x.size(); i++){
                    int first = x.get(i);
                    int second = y.get(i);
                    if(first == second) {
                        out.add(i, first);
                    }else if(second == WILDCARD && wildcardListRight.contains(first)){
                        out.add(i, first);
                    }else if(first == WILDCARD && wildcardListLeft.contains(second)){
                        out.add(i,second);
                    }else{
                        eq = false;
                        break;
                    }
                }
                if(eq){
                    result.add(out);
                }
            });
        });

        return result;
    }
}
