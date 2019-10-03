package com.user188245.bullsandcowssolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuessImpl extends ArrayList<Integer> implements Guess{

    GuessImpl(){
        super();
    }

    GuessImpl(int capacity) {
        super(capacity);
    }

    private GuessImpl(List<Integer> list){
        super(list);
    }

    public static GuessImpl generate(Integer... input) {
        return new GuessImpl(Arrays.asList(input));
    }

    public static GuessImpl generate(List<Integer> input) {
        return new GuessImpl(input);
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public Integer get(int index) {
        return super.get(index);
    }

    @Override
    public Integer set(int index, Integer item) {
        return super.set(index, item);
    }

    @Override
    public boolean contains(Integer item) {
        return super.contains(item);
    }

    @Override
    public Integer[] toArray(){
        return super.toArray(new Integer[0]);
    }


    @Override
    public int[] computeResult(Guess realGuess) throws IndexOutOfBoundsException{
        if(realGuess.size() != this.size()){
            throw new IndexOutOfBoundsException();
        }else{
            int strike = 0, ball = 0;
            int[] result = new int[2];
            for(int i=0; i<size(); i++){
                if(get(i).equals(realGuess.get(i))){
                    strike++;
                }
            }
            Integer[] a = this.toArray();
            Integer[] b = realGuess.toArray();
            Arrays.sort(a);
            Arrays.sort(b);
            int i=0, j=0;
            while(i<a.length && j<b.length ){
                if(a[i] < b[j]){
                    i++;
                }else if(a[i] > b[j]){
                    j++;
                }else{
                    i++;
                    j++;
                    ball++;
                }
            }
            ball -= strike;
            result[0] = strike;
            result[1] = ball;
            return result;
        }
    }
}
