package com.user188245.bullsandcowssolver;

import java.util.ArrayList;
import java.util.List;

class TreeBCSCacheTest extends ByteSerializableBCSCacheTest{


    @Override
    ByteSerializableBCSCache build() {
        Guess rootGuess = GuessImpl.generate(1,2,3,4);
        TreeBCSCache treeBCSCache = new TreeBCSCache(rootGuess,10);
        List<Trial> history = new ArrayList<>();
        history.add(new Trial(rootGuess,1,2));
        history.add(new Trial(GuessImpl.generate(6,7,8,9),0,1));
        history.add(new Trial(GuessImpl.generate(1,3,5,7),2,1));
        history.add(new Trial(GuessImpl.generate(2,4,6,8),4,0));
        treeBCSCache.put(history, GuessImpl.generate(9,8,7,6));
        return treeBCSCache;
    }

    @Override
    BCSCache build(Guess rootGuess, int unitSize) {
        return new TreeBCSCache(rootGuess,unitSize);
    }
}