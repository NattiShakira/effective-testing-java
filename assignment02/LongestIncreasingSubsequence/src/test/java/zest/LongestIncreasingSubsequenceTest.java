package zest;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;


class LongestIncreasingSubsequenceTest {

    LongestIncreasingSubsequence lisClass = new LongestIncreasingSubsequence();

    // Tests for Task 1. Code Coverage
    @Test
    void testArrayIsNullOrEmpty() {
        int[] emptyArray = {};
        assertEquals(0, lisClass.lengthOfLIS(emptyArray));
        assertEquals(0, lisClass.lengthOfLIS(null));
    }

    @Test
    void testArrayRandomSequence() {
        assertEquals(5, lisClass.lengthOfLIS(new int[]{2, 1, 5, 7, 2, 3, 8, 3, 5, 10, 0}));
    }

    // Tests for Task 3. Testing Contracts
    @Test
    void testResultSatisfiesConditions() {
        int[] array = {1, 2, 3, 4, 5};
        assertTrue(lisClass.resultSatisfiesConditions(5, array));
        assertThrows(RuntimeException.class, () -> lisClass.resultSatisfiesConditions(-1, array));
        assertThrows(RuntimeException.class, () -> lisClass.resultSatisfiesConditions(10, array));
    }

    @Test
    void testInvariant1SatisfiesConditions() {
        int[] dpArray1 = {1, 1, 1, 1, 1};
        int[] array1 = {1, 2, 3, 4, 5};
        assertTrue(lisClass.invariant1SatisfiesConditions(dpArray1, array1));

        int[] array2 = {1, 2, 3, 4};
        assertThrows(RuntimeException.class, () -> lisClass.invariant1SatisfiesConditions(dpArray1, array2));

        int[] dpArray2 = {1, 1, 1, 1};
        assertThrows(RuntimeException.class, () -> lisClass.invariant1SatisfiesConditions(dpArray2, array1));
    }

    @Test
    void testInvariant2SatisfiesConditions() {
        assertTrue(lisClass.invariant2SatisfiesConditions(1));
        assertTrue(lisClass.invariant2SatisfiesConditions(5));
        assertThrows(RuntimeException.class, () -> lisClass.invariant2SatisfiesConditions(0));
    }

    // Tests for Task 4. Property-Based Testing
    @Property
    void testAscendingSortedUniqueArray(@ForAll("ascendingSortedUniqueArray") int[] array) {
        int actual = lisClass.lengthOfLIS(array);
        int expected = array.length;
        assertEquals(expected, actual);
    }

    @Provide
    private Arbitrary<int[]> ascendingSortedUniqueArray() {
        return Arbitraries.integers()
                .between(Integer.MIN_VALUE, Integer.MAX_VALUE)
                .array(int[].class)
                .ofMinSize(1)
                .map(this::ensureUniqueness)
                .map(this::sortAscending);
    }

    private int[] sortAscending(int[] array) {
        Arrays.sort(array);
        return array;
    }

    @Property
    void testDescendingSortedArray(@ForAll("descendingSortedArray") int[] array) {
        int result = lisClass.lengthOfLIS(array);
        assertEquals(1, result);
    }

    @Provide
    private Arbitrary<int[]> descendingSortedArray() {
        return Arbitraries.integers()
                .between(Integer.MIN_VALUE, Integer.MAX_VALUE)
                .array(int[].class)
                .ofMinSize(1)
                .map(this::sortDescending);
    }

    private int[] ensureUniqueness(int[] array) {
        Set<Integer> uniqueSet = new HashSet<>();
        for (int num : array) {
            uniqueSet.add(num);
        }
        int[] uniqueArray = new int[uniqueSet.size()];
        int index = 0;
        for (int num : uniqueSet) {
            uniqueArray[index++] = num;
        }
        return uniqueArray;
    }

    private int[] sortDescending(int[] array) {
        Arrays.sort(array);
        return reverseArray(array);
    }

    private int[] reverseArray(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int num : array) {
            list.add(num);
        }
        Collections.reverse(list);
        int[] array2 = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array2[i] = list.get(i);
        }
        return array2;

    }

    @Property
    void testAscendingSortedArrayWithDuplicates(@ForAll("ascendingSortedArrayWithDuplicates") int[] array) {
        int actual = lisClass.lengthOfLIS(array);
        int expected = array.length - 1;
        assertEquals(expected, actual);
    }

    @Provide
    private Arbitrary<int[]> ascendingSortedArrayWithDuplicates() {
        return Arbitraries.integers()
                .between(Integer.MIN_VALUE, Integer.MAX_VALUE)
                .array(int[].class)
                .ofMinSize(3)
                .map(this::ensureUniqueness)
                .map(this::insertDuplicate)
                .map(this::sortAscending);
    }

    private int[] insertDuplicate(int[] array) {
        int length = array.length;
        int index = length / 2;
        int[] newArray = Arrays.copyOf(array, length + 1);
        System.arraycopy(array, index, newArray, index + 1, length - index);
        newArray[index] = array[index - 1];
        return newArray;
    }

    @Property
    void testMixedUniqueArraysAD(@ForAll("mixedUniqueArraysAscendingDescending") int[] array) {
        int actual = lisClass.lengthOfLIS(array);
        int expectedMaxValue = 25;
        assertTrue(actual <= expectedMaxValue, "Value is not less than or equal to max expected value");
    }

    @Provide
    private Arbitrary<int[]> mixedUniqueArraysAscendingDescending() {
        return Arbitraries.integers()
            .between(-999, 0)
            .array(int[].class)
            .ofSize(10)
            .map(this::sortAscending)
            .flatMap(array1 -> {
                Arbitrary<int[]> array2Arbitrary = Arbitraries.integers()
                        .between(Integer.MIN_VALUE, -1000)
                        .array(int[].class)
                        .ofSize(15)
                        .map(this::sortDescending);

                return array2Arbitrary.flatMap(array2 -> {
                    Arbitrary<int[]> array3Arbitrary = Arbitraries.integers()
                            .between(1000, Integer.MAX_VALUE)
                            .array(int[].class)
                            .ofSize(15)
                            .map(this::sortAscending);

                    return array3Arbitrary.flatMap(array3 -> {
                        Arbitrary<int[]> array4Arbitrary = Arbitraries.integers()
                                .between(1, 999)
                                .array(int[].class)
                                .ofSize(10)
                                .map(this::sortDescending);

                        return array4Arbitrary.map(array4 -> concatenateArrays(array1, array2, array3, array4));
                    });
                });
            });
    }

    @Property
    void testMixedUniqueArraysDA(@ForAll("mixedUniqueArraysDescendingAscending") int[] array) {
        int actual = lisClass.lengthOfLIS(array);
        int expectedMaxValue = 25;
        assertTrue(actual <= expectedMaxValue, "Value is not less than or equal to the maximum expected value");
    }

    @Provide
    private Arbitrary<int[]> mixedUniqueArraysDescendingAscending() {
        return Arbitraries.integers()
                .between(1000, Integer.MAX_VALUE)
                .array(int[].class)
                .ofSize(10)
                .map(this::sortDescending)
                .flatMap(array1 -> {
                    Arbitrary<int[]> array2Arbitrary = Arbitraries.integers()
                            .between(-999, 0)
                            .array(int[].class)
                            .ofSize(15)
                            .map(this::sortAscending);

                    return array2Arbitrary.flatMap(array2 -> {
                        Arbitrary<int[]> array3Arbitrary = Arbitraries.integers()
                                .between(Integer.MIN_VALUE, -1000)
                                .array(int[].class)
                                .ofSize(15)
                                .map(this::sortDescending);

                        return array3Arbitrary.flatMap(array3 -> {
                            Arbitrary<int[]> array4Arbitrary = Arbitraries.integers()
                                    .between(1, 999)
                                    .array(int[].class)
                                    .ofSize(10)
                                    .map(this::sortAscending);

                            return array4Arbitrary.map(array4 -> concatenateArrays(array1, array2, array3, array4));
                        });
                    });
                });
    }

    public int[] concatenateArrays(int[]... arrays) {
        int totalLength = 0;
        for (int[] array : arrays) {
            totalLength += array.length;
        }

        int[] result = new int[totalLength];
        int index = 0;
        for (int[] array : arrays) {
            for (int num : array) {
                result[index++] = num;
            }
        }
        return result;
    }

}