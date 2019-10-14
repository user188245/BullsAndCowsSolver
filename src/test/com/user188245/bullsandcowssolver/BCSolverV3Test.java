package com.user188245.bullsandcowssolver;

class BCSolverV3Test extends BullsAndCowsSolverTest {

    @Override
    BullsAndCowsSolver build(int unitSize, int boxSize) {
        return new BCSolverV3(unitSize,boxSize);
    }

}