package zest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.Test;




class CombinationSumTest {

    @Test
    void testEmptyCandidates() {
        assertTrue(CombinationSum.combinationSum(new int[]{}, 7).isEmpty());
    }

    @Test
    void testSingleElementEqualsTarget() {
        assertEquals(List.of(List.of(7)), CombinationSum.combinationSum(new int[]{7}, 7));
    }

    @Test
    void testAllElementsGreaterThanTarget() {
        assertTrue(CombinationSum.combinationSum(new int[]{8, 9, 10}, 7).isEmpty());
    }

    @Test
    void testTargetIsZero() {
        assertEquals(Collections.singletonList(Collections.emptyList()), CombinationSum.combinationSum(new int[]{2, 3, 6, 7}, 0));
    }

    @Test
    void testTargetLessThanSmallestCandidate() {
        assertEquals(Collections.emptyList(), CombinationSum.combinationSum(new int[]{2, 3, 6, 7}, 1));
    }

    @Test
    void testMultipleCombinationsPossible() {
        assertEquals(List.of(List.of(2, 2, 3), List.of(7)), CombinationSum.combinationSum(new int[]{2, 3, 6, 7}, 7));
    }

    @Test
    void testNegativeTarget() {
        assertTrue(CombinationSum.combinationSum(new int[]{2, 3, 4}, -1).isEmpty());
    }

    @Test
    void testCandidateIsZero() {
        assertTrue(CombinationSum.combinationSum(new int[]{0}, 7).isEmpty());
    }

    @Test
    void testDuplicateCandidates(){
        assertEquals(List.of(List.of(2, 2, 3), List.of(7)), CombinationSum.combinationSum(new int[]{2, 2, 3, 7, 7}, 7));
    }

    @Test
    void testTooManyCombinations(){
        assertTrue(((CombinationSum.combinationSum(new int[]{1, 2, 3, 4, 6, 8, 12, 16, 24}, 48)).size()) < 150);
    }


    @Test
    void testNegativeCandidates() {
        assertTrue((CombinationSum.combinationSum(new int[]{-2, -3}, 7)).isEmpty());
    }

    @Test
    void testMixedCandidates() {
        assertEquals(List.of(List.of(2, 2, 3), List.of(7)), CombinationSum.combinationSum(new int[]{-2, -3, 2, 3, 7}, 7));
    }

    /*
    @Test
    void testNegativeCandidatesAndTarget() {
        assertEquals(List.of(List.of(-2, -2, -3), List.of(-7)), CombinationSum.combinationSum(new int[]{-2, -3, -7}, -7));
    }
    */



    //test just asserts the class gets instantiated, makes it so lines and branches are 100% covered by testing the constructor
    @Test
    public void testConstructor() {
        CombinationSum testInstance = new CombinationSum();
        assertTrue(testInstance instanceof CombinationSum);
    }

    @Test
    void testUnsortedCandidates(){
        assertEquals(List.of(List.of(2, 2, 3), List.of(7)), CombinationSum.combinationSum(new int[]{7, 3, 6, 2}, 7));
    }

    @Test
    void testCandidatesNearZero(){
        assertEquals(List.of(List.of(1, 1, 1), List.of(1, 2)), CombinationSum.combinationSum(new int[]{-2, -1, 0, 1, 2}, 3));
    }


}
