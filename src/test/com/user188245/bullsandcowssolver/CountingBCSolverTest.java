package com.user188245.bullsandcowssolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountingBCSolverTest extends BullsAndCowsSolverTest {

    @Override
    BullsAndCowsSolver build(int unitSize, int boxSize) {
        return new CountingBCSolver(unitSize,boxSize);
    }

}