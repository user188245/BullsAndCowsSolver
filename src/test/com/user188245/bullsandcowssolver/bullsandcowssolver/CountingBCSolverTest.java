package com.user188245.bullsandcowssolver.bullsandcowssolver;

import com.user188245.bullsandcowssolver.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountingBCSolverTest {

    private BullsAndCowsSolver solver;

    @BeforeEach
    void before() {
        solver = new CountingBCSolver(10,4);
    }

    @Test
    void getUnitSize() {
        assertEquals(10,solver.getUnitSize());
    }

    @Test
    void getBoxSize() {
        assertEquals(4, solver.getBoxSize());
    }

    @Test
    void firstGame(){
        /* Guess : 1382 */
        solver.putClue(new Trial(GuessImpl.generate(1,2,3,4),1,2));
        solver.putClue(new Trial(GuessImpl.generate(5,6,7,8),0,1));
        solver.putClue(new Trial(GuessImpl.generate(2,1,7,4),0,2));
        Guess guess1 = solver.getSolution();
        List<Trial> history = solver.getHistory();
        assertEquals(new Trial(GuessImpl.generate(1,2,3,4),1,2), history.get(0));
        assertEquals(new Trial(GuessImpl.generate(5,6,7,8),0,1), history.get(1));
        assertEquals(new Trial(GuessImpl.generate(2,1,7,4),0,2), history.get(2));
        history.forEach(x->
                assertTrue(checkTrialFine(x, guess1))
        );
    }

    @Test
    void secondGame(){
        /* Guess : 9017 */
        solver.putClue(new Trial(GuessImpl.generate(1,2,3,4),0,1));
        solver.putClue(new Trial(GuessImpl.generate(5,6,7,8),0,1));
        solver.putClue(new Trial(GuessImpl.generate(2,1,7,4),0,2));
        Guess guess1 = solver.getSolution();
        List<Trial> history = solver.getHistory();
        assertEquals(new Trial(GuessImpl.generate(1,2,3,4),0,1), history.get(0));
        assertEquals(new Trial(GuessImpl.generate(5,6,7,8),0,1), history.get(1));
        assertEquals(new Trial(GuessImpl.generate(2,1,7,4),0,2), history.get(2));
        history.forEach(x->
                assertTrue(checkTrialFine(x, guess1))
        );
    }

    @Test
    void thirdGame(){
        /* Guess : 1492 */
        solver.putClue(new Trial(GuessImpl.generate(0,5,1,4),0,2));
        solver.putClue(new Trial(GuessImpl.generate(3,1,0,8),0,1));
        solver.putClue(new Trial(GuessImpl.generate(4,7,8,5),0,1));
        solver.putClue(new Trial(GuessImpl.generate(1,6,5,9),1,1));
        solver.putClue(new Trial(GuessImpl.generate(1,4,2,6),2,1));
        Guess guess1 = solver.getSolution();
        List<Trial> history = solver.getHistory();
        history.forEach(x->
                assertTrue(checkTrialFine(x, guess1))
        );
    }

    @Test
    void forthGame(){
        /* Guess : 2895 */
        solver.putClue(new Trial(GuessImpl.generate(0,1,2,3),0,1));
        solver.putClue(new Trial(GuessImpl.generate(2,5,7,9),1,2));
        solver.putClue(new Trial(GuessImpl.generate(6,2,5,9),0,3));
        solver.putClue(new Trial(GuessImpl.generate(4,5,9,2),1,2));
        Guess guess1 = solver.getSolution();
        List<Trial> history = solver.getHistory();
        history.forEach(x->
                assertTrue(checkTrialFine(x, guess1))
        );
    }

    boolean checkTrialFine(Trial trial, Guess realGuess){
        Guess trialGuess = trial.getGuess();
        if(trialGuess.size() == realGuess.size()){
            int[] bullsAndCows = trialGuess.computeResult(realGuess);
            return trial.getBulls()==bullsAndCows[0] && trial.getCows()==bullsAndCows[1];
        }else{
            return false;
        }
    }
}