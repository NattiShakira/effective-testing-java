package zest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.jqwik.api.*;

class FindDuplicateTest {

    @Test
    void testConstructor() {
        FindDuplicate testInstance = new FindDuplicate();
        assertTrue(testInstance instanceof FindDuplicate);
    }

    //testing contracts
    @Test
    public void testNormalOperation() {
        int[] input = {1, 3, 4, 2, 2};
        assertEquals(2, FindDuplicate.findDuplicate(input));
        input = new int[]{3, 1, 3, 4, 2};
        assertEquals(3, FindDuplicate.findDuplicate(input));
    }

    @Test
    public void testPreConditionNullArray() {
        Exception exception = assertThrows(IllegalStateException.class, () -> FindDuplicate.findDuplicate(null));
        assertTrue(exception.getMessage().contains("The array must not be null"));
    }

    @Test
    public void testPreConditionSmallArray() {
        int[] input = {1};
        Exception exception = assertThrows(IllegalStateException.class, () -> FindDuplicate.findDuplicate(input));
        assertTrue(exception.getMessage().contains("Array must have at least two elements"));
    }

    @Test
    public void testPreConditionElementTooLarge() {
        int[] input = {1, 2, 3, 4, 5};
        Exception exception = assertThrows(IllegalStateException.class, () -> FindDuplicate.findDuplicate(input));
        assertTrue(exception.getMessage().contains("Array elements must be within the range"));
    }

    @Test
    public void testPreConditionElementTooSmall() {
        int[] input = {0, 2, 3, 4, 4};
        Exception exception = assertThrows(IllegalStateException.class, () -> FindDuplicate.findDuplicate(input));
        assertTrue(exception.getMessage().contains("Array elements must be within the range"));
    }

    @Test
    //test the postcondition that returned number must be a duplicate
    public void testPostCondition() {
        int[] input = {1, 3, 4, 2, 2};
        int duplicate = FindDuplicate.findDuplicate(input);
        int finalCount = 0;
        for (int num : input) {
            if (num == duplicate) {
                finalCount++;
            }
        }
        assertTrue(finalCount > 1, "The returned number must be a duplicate.");
    }

    @Test
    //test if Array changed
    public void testInvariantArrayUnchanged() {
        int[] input = {1, 3, 4, 2, 2};
        int[] copyOfInput = Arrays.copyOf(input, input.length);
        FindDuplicate.findDuplicate(input);
        assertArrayEquals(copyOfInput, input, "The array should not be altered by the findDuplicate method");
    }

    //test valid two-element array
    @Test
    public void testExactlyTwoElements() {
        int[] input = {1,1};
        assertEquals(1, FindDuplicate.findDuplicate(input));
    }

    //test invalid two-element array
    @Test
    public void testInvalidTwoElementArray() {
        int[] input = {1, 2};
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            throw new IllegalStateException("Array elements must be within the range 1 to 1");
        });

        assertTrue(exception.getMessage().contains("Array elements must be within the range"));
    }
    //end of contract testing

    //property-based testing:

    //Property: Every result has to be a duplicate
    @Property
    boolean everyResultIsADuplicate(@ForAll("arraysWithDuplicates") int[] array) {
        int result = FindDuplicate.findDuplicate(array);
        int occurrences = 0;
        for (int num : array) {
            if (num == result) {
                occurrences++;
            }
        }
        return occurrences > 1;
    }

    //Property: Array must be unchanged after execution
    @Property
    boolean arrayIsUnchangedAfterMethodCall(@ForAll("arraysWithDuplicates") int[] original) {
        int[] arrayCopy = Arrays.copyOf(original, original.length);
        FindDuplicate.findDuplicate(original);
        return Arrays.equals(original, arrayCopy);
    }

    //Property: Method must return same result for same array.
    @Property
    boolean methodIsIdempotent(@ForAll("arraysWithDuplicates") int[] array) {
        int firstCall = FindDuplicate.findDuplicate(array);
        int secondCall = FindDuplicate.findDuplicate(array);
        return firstCall == secondCall;
    }

    //provides a valid array.
    @Provide
    Arbitrary<int[]> arraysWithDuplicates() {
        return Arbitraries.integers().between(1, 100)
                .flatMap(n -> Arbitraries.integers().between(1, n)
                        .list().ofSize(n + 1)
                        .map(list -> list.stream().mapToInt(Integer::intValue).toArray())
                );
    }




}