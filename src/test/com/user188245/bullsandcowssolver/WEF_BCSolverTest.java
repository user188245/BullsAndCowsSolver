package com.user188245.bullsandcowssolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WEF_BCSolverTest extends BullsAndCowsSolverTest {

    @Override
    BullsAndCowsSolver build(int unitSize, int boxSize) {
        return new WEF_BCSolver(unitSize,boxSize);
    }
}