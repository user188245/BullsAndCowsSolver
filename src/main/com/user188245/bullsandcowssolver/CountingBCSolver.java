package com.user188245.bullsandcowssolver;

import java.util.*;


/**
 * it use countingNode method to filter answerable set instead of intersectNode,
 */
public class CountingBCSolver implements BullsAndCowsSolver{

    protected int unitSize;

    protected int boxSize;

    protected List<Guess> answerableSet;

    protected ArrayList<Integer> wildCardList;

    protected List<Trial> history;

    public class WCAnswerProcessUnit extends AnswerProcessUnit {

        WCAnswerProcessUnit(Guess guess, int bullsCount, int cowsCount) {
            super(guess, bullsCount, cowsCount);
        }

        void buildWildcardAvailable(){
            //not yet
            if(bullsCount == 0 && cowsCount == 0){
                for(int unusedIndex : unusedIndexes){
                    caa.set(unusedIndex, (ArrayList<Integer>)wildCardList.clone());
                }
                bullsCount = -1;
            }else{
                throw new RuntimeException("both bulls and cows must be 0");
            }
        }

        List<AnswerProcessUnit> reduceWildcard(){
            if(bullsCount == -1){
                List<AnswerProcessUnit> result = new ArrayList<>();
                if(unusedIndexes.size() > 0){
                    int unusedIndex = unusedIndexes.remove(0);
                    ArrayList<Integer> available = caa.get(unusedIndex);
                    if(available.size() > 0){
                        for(Integer integer : available){
                            AnswerProcessUnit apu = null;
                            try {
                                apu = (AnswerProcessUnit) this.clone();
                                apu.caa.set(unusedIndex,AnswerProcessUnit.CONVERTER.get(integer));
                                for(int unused2 : apu.unusedIndexes){
                                    apu.caa.get(unused2).removeIf(x->x.equals(integer));
                                }
                                result.add(apu);
                            } catch (CloneNotSupportedException e) {
                                throw new Error("Internal Error");
                            }
                        }
                    }
                }
                return result;
            }else{
                throw new RuntimeException("WildcardAvailable was not built yet.");
            }
        }

        boolean isReadyForWCReduce(){
            return bullsCount==-1;
        }
    }

    public CountingBCSolver(int unitSize, int boxSize) {
        this.unitSize = unitSize;
        this.boxSize = boxSize;
        if(this.unitSize < this.boxSize){
            throw new IllegalArgumentException("unitSize must be greater than boxSize.");
        }
        if(this.boxSize < 2){
            throw new IllegalArgumentException("boxSize must be 2 as least.");
        }
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
            solution = GuessImpl.generate(wildCardList.subList(0,boxSize));
        }else{
            solution = answerableSet.get(new Random().nextInt(answerableSet.size()));
        }
        return (Guess)solution.clone();
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
        this.wildCardList.removeIf(answer2::contains);
        if(bulls > 0 || cows > 0){
            if(answerableSet.isEmpty()){
                Iterable<Guess> clues = getClue(guess,bulls,cows);
                clues.forEach(answerableSet::add);
            }else{
                List<Guess> newAnswerableSet = countingGuess(guess,bulls,cows,answerableSet);
                answerableSet.clear();
                answerableSet = null;
                answerableSet = newAnswerableSet;
            }
        }else{
            for(Integer item : sortedInput){
                answerableSet.removeIf(x->x.contains(item));
            }
        }
        history.add(trial);
    }

    private List<Guess> countingGuess(Guess guess, int bulls, int cows, List<Guess> countableGuessList){
        List<Guess> result = new ArrayList<>();
        for(Guess g:countableGuessList){
            int[] bc = guess.computeResult(g);
            if(bc[0]==bulls && bc[1]==cows){
                result.add(g);
            }
        }
        return result;
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
        System.gc();
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
        queue.add(new CountingBCSolver.WCAnswerProcessUnit(guess,bulls,cows));
        while(!queue.isEmpty()){
            try {
                CountingBCSolver.WCAnswerProcessUnit apu = (CountingBCSolver.WCAnswerProcessUnit)queue.remove();
                if (apu.bullsCount > 0) {
                    queue.addAll(apu.reduceBulls(guess));
                }else if(apu.cowsCount > 0){
                    queue.addAll(apu.reduceCows());
                }else if(apu.unusedIndexes.size() > 0) {
                    if(apu.isReadyForWCReduce()){
                        queue.addAll(apu.reduceWildcard());
                    }else{
                        apu.buildWildcardAvailable();
                        queue.add(apu);
                    }
                }else{
                    result.add(apu.build());
                }
            }catch(CloneNotSupportedException e){
                e.printStackTrace();
            }
        }
        return result;
    }
}
