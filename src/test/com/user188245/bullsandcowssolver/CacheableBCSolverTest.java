package com.user188245.bullsandcowssolver;

import static org.junit.jupiter.api.Assertions.*;

class CacheableBCSolverTest extends BullsAndCowsSolverTest {

    @Override
    BullsAndCowsSolver build(int unitSize, int boxSize) {
        return new CacheableBCSolver(new BCSolverV3(10,4),new TreeBCSCache(GuessImpl.generate(0,1,2,3),4));
    }
}