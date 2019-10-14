package com.user188245.bullsandcowssolver;

import java.util.List;

import java.io.*;
import java.util.*;

/**
 * TreeBCSCache, The trie-based dynamic cache
 */
public class TreeBCSCache implements ByteSerializableBCSCache, Serializable {

    private int size;
    private int unitSize;
    private int boxSize;
    private Node root;

    private TreeBCSCache(){ }

    TreeBCSCache(Guess rootGuess, int unitSize){
        this.size = 0;
        this.root = new Node(rootGuess);
        this.unitSize = unitSize;
        this.boxSize = this.root.guess.size();
    }

    class Node implements Serializable {
        Guess guess;
        HashMap<Long, Node> child;

        Node(Guess guess) {
            this.guess = guess;
            this.child = new HashMap<>();
        }

        Node put(int bulls, int cows, Guess guess){
            long v = encode(bulls,cows);
            Node oldNode = this.child.get(v);
            if(oldNode == null){
                oldNode = new Node(guess);
                this.child.put(v,oldNode);
            }else{
                oldNode.guess = guess;
            }
            return oldNode;
        }

        public Node get(int bulls, int cows){
            return this.child.get(encode(bulls,cows));
        }
    }

    @Override
    public Guess get(List<Trial> history) {
        Node target = this.root;
        for(Trial trial : history){
            if(boxSize < trial.getBulls()+trial.getCows()){
                throw new RuntimeException("Invalid history.");
            }
            target = target.get(trial.getBulls(), trial.getCows());
            if(target == null){
                return null;
            }
        }
        return target.guess;
    }

    @Override
    public void put(List<Trial> history, Guess guess) {
        if(history.size() == 0){
            throw new RuntimeException("History must not be empty.");
        }else if(!history.get(0).getGuess().equals(root.guess)){
            throw new RuntimeException("The first of trial should be equivalent to reserved value : " + root.guess);
        }
        Node target = this.root;

        for(int i=0; i<history.size(); i++){
            int bulls = history.get(i).getBulls();
            int cows = history.get(i).getCows();
            if(boxSize < bulls+cows){
                throw new RuntimeException("Invalid Bulls and Cows values.");
            }
            Node tmpTarget = target.get(bulls,cows);
            if(tmpTarget == null){
                Guess newGuess;
                if(i == history.size()-1){
                    newGuess = guess;
                }else{
                    newGuess = history.get(i+1).getGuess();
                }
                if(newGuess.size() != this.boxSize){
                    throw new RuntimeException("Incorrect boxSize of current guess into trial.");
                }
                tmpTarget = target.put(bulls,cows,newGuess);
                this.size++;
            }
            target = tmpTarget;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int getBoxSize() {
        return boxSize;
    }

    @Override
    public int getUnitSize() {
        return unitSize;
    }

    @Override
    public boolean adaptable(BCSCache cache) {
        return cache.getBoxSize()==this.boxSize && cache.getUnitSize()==this.unitSize;
    }

    @Override
    public byte[] serialize(){
        byte[] data = null;
        try {
            ByteArrayOutputStream os1 = new ByteArrayOutputStream();
            ObjectOutputStream os2 = new ObjectOutputStream(os1);
            os2.writeObject(this);
            data = os1.toByteArray();
            os2.close();
            os1.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize.\nCause : " + e.getMessage());
        }
        return data;
    }

    private long encode(int bulls, int cows){
        return (boxSize+1)*bulls+cows;
    }

}
