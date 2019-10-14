package com.user188245.bullsandcowssolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NumberToListConverter {

    private Map<Integer, ArrayList<Integer>> converter;

    public NumberToListConverter() {
        this.converter = new HashMap<>();
    }

    public ArrayList<Integer> get(Integer number){
        ArrayList<Integer> out = converter.get(number);
        if(out == null){
            out = new ArrayList<>();
            out.add(number);
            converter.put(number, out);
        }
        return out;
    }

}
