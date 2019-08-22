package com.user188245.bullsandcowssolver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AnswerProcessUnit implements Cloneable{

    protected List<ArrayList<Integer>> caa;
    protected int bullsCount;
    protected int cowsCount;
    protected ArrayList<Integer> unaccessedIndexes;
    protected final static NumberToListConvertor CONVERTOR = new NumberToListConvertor();

    AnswerProcessUnit(Guess guess, int bullsCount, int cowsCount) {
        caa = new ArrayList<>();
        for(int i = 0; i< guess.size(); i++){
            ArrayList<Integer> available = new ArrayList<>();
            for(Integer j: guess.toArray()){
                Integer a = guess.get(i);
                if(!a.equals(j)){
                    available.add(j);
                }
            }
            caa.add(available);
        }
        this.bullsCount = bullsCount;
        this.cowsCount = cowsCount;
        unaccessedIndexes = new ArrayList<>();
        for(int i=0; i<caa.size(); i++){
            unaccessedIndexes.add(i);
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        AnswerProcessUnit out = (AnswerProcessUnit)super.clone();
        out.unaccessedIndexes = (ArrayList<Integer>) this.unaccessedIndexes.clone();
        out.caa = new ArrayList<>();
        for(ArrayList<Integer> ca : this.caa){
            out.caa.add((ArrayList<Integer>) ca.clone());
        }
        return out;
    }

    public List<AnswerProcessUnit> reduceBulls(Guess guess) throws CloneNotSupportedException {
        List<AnswerProcessUnit> apuList = new LinkedList<>();
        for(int i=0; i<unaccessedIndexes.size(); i++) {
            AnswerProcessUnit apu2 = (AnswerProcessUnit) this.clone();
            int index = apu2.unaccessedIndexes.remove(i);
            apu2.caa.set(index,CONVERTOR.get(guess.get(index)));
            apu2.bullsCount--;
            for(int j: apu2.unaccessedIndexes){
                apu2.caa.get(j).removeIf(x->x.equals(guess.get(index)));
            }
            apuList.add(apu2);
        }
        return apuList;
    }

    public List<AnswerProcessUnit> reduceCows() throws CloneNotSupportedException {
        List<AnswerProcessUnit> apuList = new LinkedList<>();
        for(int i=0; i<unaccessedIndexes.size(); i++) {
            int index = unaccessedIndexes.get(i);
            ArrayList<Integer> ca = caa.get(index);
            for(int j=0; j<ca.size(); j++){
                AnswerProcessUnit apu2 = (AnswerProcessUnit) this.clone();
                apu2.unaccessedIndexes.remove(i);
                Integer number = ca.get(j);
                apu2.caa.set(index, CONVERTOR.get(number));
                for(int k : apu2.unaccessedIndexes){
                    apu2.caa.get(k).removeIf(x->x.equals(number));
                }
                apu2.cowsCount--;
                apuList.add(apu2);
            }
        }
        return apuList;
    }

    public void fillWildCard(List<Integer> wildCardList){
        if(bullsCount == 0 && cowsCount == 0){
            ArrayList<Integer> wc;
            if(wildCardList instanceof ArrayList){
                wc = (ArrayList<Integer>)wildCardList;
            }else{
                wc = new ArrayList<>(wildCardList);
            }
            bullsCount = unaccessedIndexes.size();
            cowsCount = 0;
            for(int i : unaccessedIndexes){
                caa.set(i, (ArrayList<Integer>)wc.clone());
            }
        }else{
            throw new RuntimeException("Both bullsCount and cowsCount must be 0");
        }
    }

    public Guess build(){
        Integer[] out = new Integer[caa.size()];
        for(int i=0; i<out.length; i++){
            out[i] = caa.get(i).get(0);
        }
        return GuessImpl.generate(out);
    }
}