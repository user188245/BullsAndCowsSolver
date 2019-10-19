package com.user188245.bullsandcowssolver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * TreeBCSCache, The trie-based dynamic cache
 *
 *
 */
public class TreeBCSCacheV2 implements ByteSerializableStrongBCSCache, Serializable {

    private int size;
    private int unitSize;
    private int boxSize;
    private Node root;

    private TreeBCSCacheV2(){ }

    public TreeBCSCacheV2(Guess rootGuess, int unitSize){
        this.size = 0;
        this.root = new Node(null,rootGuess,0L);
        this.unitSize = unitSize;
        this.boxSize = this.root.guess.size();
    }

    class Node implements Serializable, Comparable<Node> {
        Guess guess;
        Node parent;
        long code;
        HashMap<Long, SortedSet<Node>> child;
        float expected;

        Node(Node parent, Guess guess, long code) {
            this.guess = guess;
            this.parent = parent;
            this.code = code;
            this.child = new HashMap<>();
            this.expected = Float.POSITIVE_INFINITY;
        }

        Node put(int bulls, int cows, Guess guess){
            long v = encode(bulls,cows);
            SortedSet<Node> oldNodeSet = this.child.computeIfAbsent(v, k -> new TreeSet<>());
            Node newNode = new Node(this,guess,v);
            oldNodeSet.add(newNode);
            if(bulls == getBoxSize()){
                newNode.backPropagation(0);
            }
            return newNode;
        }

        SortedSet<Node> getAll(int bulls, int cows){
            return this.child.get(encode(bulls,cows));
        }

        Node get(int bulls, int cows){
            return getAll(bulls,cows).first();
        }

        void backPropagation(float expected){
            this.expected = expected--;
            if(this.parent != null){
                if(this.parent.expected != Float.POSITIVE_INFINITY){
                    int n;
                    SortedSet<Node> nodeSet = this.parent.child.get(code);
                    if(nodeSet == null){
                        throw new RuntimeException("Internal Error.");
                    }else{
                        n = nodeSet.size();
                    }
                    expected = ((n-1)*this.parent.expected+expected)/n;
                }
                this.parent.backPropagation(expected);
            }
        }

        @Override
        public int compareTo(Node o) {
            if(o.expected == Float.POSITIVE_INFINITY){
                return Float.compare(this.expected,root.expected);
            }else if(this.expected == Float.POSITIVE_INFINITY){
                return Float.compare(root.expected,o.expected);
            }
            return Float.compare(this.expected,o.expected);
        }
    }

    @Override
    public List<Guess> getAll(List<Trial> history) {
        Node target = this.root;
        SortedSet<Node> nodes = null;
        for(Trial trial : history){
            if(boxSize < trial.getBulls()+trial.getCows()){
                throw new RuntimeException("Invalid history.");
            }
            nodes = target.getAll(trial.getBulls(), trial.getCows());
            target = nodes.first();
            if(target == null){
                return null;
            }
        }
        if(nodes != null){
            List<Guess> result = new ArrayList<>(nodes.size());
            for(Node node: nodes){
                result.add(node.guess);
            }
            return result;
        }else{
            return null;
        }
    }

    @Override
    public Guess get(List<Trial> history) {
        List<Guess> guess = getAll(history);
        if (guess != null) {
            return guess.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void putAll(List<Trial> history, List<Guess> guesses) {
        for(Guess guess:guesses){
            put(history,guess);
        }
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
