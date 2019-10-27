package com.user188245.bullsandcowssolver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * TreeBCSCacheV2, The trie-based dynamic cache
 *
 * Node.expectedAverageHeight = ∑c.expectedAverageHeight/|this.child| + 1 for all c ∈ this.child
 *
 * Node Selection = high expectedAverageHeight (expected) first, wrong node will be selected by certain probability(trembling hand)
 *
 * Node BackPropagation = if the visited node is leaf-Node, to recalculate expectedAverageHeight, use backPropagation,
 *      parent.expectedAverageHeight = ((|parent.child|-1)*parent.expectedAverageHeight+this.expectedAverageHeight)/|parent.child|;
 *
 */
public class TreeBCSCacheV2 implements ByteSerializableBCSCache, Serializable {

    private int size;
    private int unitSize;
    private int boxSize;
    private float tremblingHandRate;
    private Node root;

    private TreeBCSCacheV2(){ }

    public TreeBCSCacheV2(Guess rootGuess, int unitSize, float tremblingHandRate){
        this.size = 0;
        this.root = new Node(null,rootGuess,0L);
        this.unitSize = unitSize;
        this.tremblingHandRate = tremblingHandRate;
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
            SortedSet<Node> nodes = getAll(bulls,cows);
            if(nodes != null){
                return nodes.first();
            }else{
                return null;
            }
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return code == node.code &&
                    Float.compare(node.expected, expected) == 0 &&
                    guess.equals(node.guess) &&
                    parent.equals(node.parent) &&
                    child.equals(node.child);
        }

        @Override
        public int hashCode() {
            return Objects.hash(guess, parent, code, child, expected);
        }
    }

    /**
     * get guess using the key
     * @param history as key
     * @return Guess as Value or Null if not exist in cache. depending on trembling hand rate, obsolete guess (Second best guess or and so on) also can be returned.
     */
    @Override
    public Guess get(List<Trial> history) {
        Node target = this.root;
        SortedSet<Node> nodes = null;
        for(Trial trial : history){
            if(boxSize < trial.getBulls()+trial.getCows()){
                throw new RuntimeException("Invalid history.");
            }
            nodes = target.getAll(trial.getBulls(), trial.getCows());
            if(nodes == null || (target = nodes.first()) == null){
                return null;
            }
        }
        if(nodes != null){
            for(Node node: nodes){
                target = node;
                if(Math.random() > tremblingHandRate){
                    break;
                }
            }
        }
        return target.guess;
    }

    /**
     * Put history-guess pair into the cache, Even if the value with current key already exists, it always keeps previous value.
     * @param history as Key
     * @param guess as Value
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeBCSCacheV2 that = (TreeBCSCacheV2) o;
        return size == that.size &&
                unitSize == that.unitSize &&
                boxSize == that.boxSize &&
                Float.compare(that.tremblingHandRate, tremblingHandRate) == 0 &&
                root.equals(that.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, unitSize, boxSize, tremblingHandRate, root);
    }
}
