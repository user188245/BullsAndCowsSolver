package com.user188245.bullsandcowssolver;

import java.util.*;

@Deprecated
public class BCSolverV2 implements BullsAndCowsSolver {

    private int unitSize;

    private int boxSize;

    private final static Integer WILDCARD = -1;

    private List<Guess> answerableSet;

    private List<Integer> wildCardList;

    private List<Trial> history;

    public BCSolverV2(int unitSize, int boxSize) {
        this.unitSize = unitSize;
        this.boxSize = boxSize;
        this.wildCardList = new ArrayList<>();
        for (int i = 0; i < unitSize; i++) {
            this.wildCardList.add(i);
        }
        this.answerableSet = new ArrayList<>();
        this.history = new ArrayList<>();
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
        Guess solution = null;
        if(!answerableSet.isEmpty()){
            Collections.shuffle(wildCardList);
            solution = answerableSet.get(new Random().nextInt(answerableSet.size()));
            for(int i=0,j=0; i<solution.size(); i++){
                if(solution.get(i).equals(WILDCARD)){
                    solution.set(i,wildCardList.get(j++));
                }
            }
        }
        return solution;
    }

    @Override
    public void putClue(Trial trial) {
        Guess guess = trial.getGuess();
        int bulls = trial.getBulls();
        int cows = trial.getCows();
        List<Guess> clues = getClue(guess,bulls,cows);
        List<Integer> answer2 = (GuessImpl) guess;
        this.wildCardList.removeIf(answer2::contains);
        if(answerableSet.isEmpty()){
            answerableSet.addAll(clues);
        }else{
            List<Guess> newAnswerableSet = intersect(answerableSet, clues);
            answerableSet.clear();
            answerableSet = null;
            answerableSet = newAnswerableSet;
        }
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

    @Override
    public List<Trial> getHistory() {
        return history;
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

    private List<Guess> getClue(Guess guess, int bulls, int cows){
        return new ArrayList<>(getSeriesClue(guess, bulls, cows));
    }

    private Set<Guess> getSeriesClue(Guess guess, int bulls, int cows) {
        Set<Guess> result = new HashSet<>();
        if (guess.size() <= 0) {
            return result;
        }else if(guess.size() == 1 && cows == 1){
            result.add(guess);
            return result;
        }
        if (bulls > 0) {
            for (int i = 0; i < guess.size(); i++) {
                int item = guess.get(i);
                Guess guessS = guess.removeItem(i);
                Set<Guess> resultS = getSeriesClue(guessS, bulls - 1, cows);
                for (Guess resItem : resultS) {
                    result.add(resItem.insertItem(i,item));
                }
            }
        }else if(cows > 0) {
            for (int i = 0; i< guess.size(); i++){
                for(int j = 0; j< guess.size(); j++){
                    if(i==j)continue;
                    int item = guess.get(j);
                    Guess guessS = guess.removeItem(j);
                    Set<Guess> resultS = getSeriesClue(guessS, 0, cows-1);
                    for (Guess resItem : resultS) {
                        result.add(resItem.insertItem(i,item));
                    }
                    if(j>i && cows>=2 && guess.size()>=2){
                        int item2 = guess.get(i);
                        Guess guessS2 = guessS.removeItem(i);
                        Set<Guess> resultS2 = getSeriesClue(guessS2, 0, cows-2);
                        for (Guess resItem : resultS2) {
                            result.add(resItem.insertItem(i,item).insertItem(j,item2));
                        }
                    }
                }
            }
            for (int i = 0; i< guess.size(); i++){
                Guess guessS = guess.removeItem(i);
                Set<Guess> resultS = getSeriesClue(guessS, 0, cows);
                for (Guess resItem : resultS) {
                    result.add(resItem.insertItem(i,WILDCARD));
                }
            }
        } else {
            Integer[] s = new Integer[guess.size()];
            Arrays.fill(s,WILDCARD);
            Guess w = GuessImpl.generate(s);
            result.add(w);
            return result;
        }
        return result;
    }

    private static List<Guess> intersect(List<Guess> a, List<Guess> b){
        Set<Guess> result = new HashSet<>();
        a.forEach(x->{
            b.forEach(y->{
                GuessImpl out = new GuessImpl(x.size());
                boolean eq = true;
                for(int i=0; i<x.size(); i++){
                    int first = x.get(i);
                    int second = y.get(i);
                    if(first == second || second == WILDCARD){
                        out.add(i,first);
                    }else if(first == WILDCARD){
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
        List<Guess> result2 = new ArrayList<>(result);
        result2.removeIf(x->{
            for(int i=0; i<x.size()-1; i++){
                for(int j=i+1; j<x.size(); j++){
                    if(x.get(i).equals(x.get(j)))
                        return true;
                }
            }
            return false;
        });
        return result2;
    }
}
