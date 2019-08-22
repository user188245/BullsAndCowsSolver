package com.user188245.bullsandcowssolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumberToListConvertor {

    private Map<Integer, ArrayList<Integer>> convertor;

    public NumberToListConvertor() {
        this.convertor = new HashMap<>();
    }

    public ArrayList<Integer> get(Integer number){
        ArrayList<Integer> out = convertor.get(number);
        if(out == null){
            out = new ArrayList<>();
            out.add(number);
            convertor.put(number, out);
        }
        return out;
    }

}
