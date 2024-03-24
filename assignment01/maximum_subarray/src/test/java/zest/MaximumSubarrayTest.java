package zest;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class MaximumSubarrayTest {
    // Exploratory tests (happy cases)
    @Test
    public void testAllElementsPositive() {
        int[] array = {1, 2, 3};
        int expected = 6;
        int actual = MaximumSubarray.maxSubArray(array);
        assertEquals(expected, actual);
    }

    @Test
    public void testAllElementsNegative() {
        int[] array = {-1, -2, -3};
        int expected = -1;
        int actual = MaximumSubarray.maxSubArray(array);
        assertEquals(expected, actual);
    }

    @Test
    public void testFirstPartPositiveSecondPartNegative() {
        int[] array = {1, 2, 3, -1, -2, -3};
        int expected = 6;
        int actual = MaximumSubarray.maxSubArray(array);
        assertEquals(expected, actual);
    }

    @Test
    public void testNegativeInTheMiddle() {
        int[] array = {2, -1, 3};
        int expected = 4;
        int actual = MaximumSubarray.maxSubArray(array);
        assertEquals(expected, actual);
    }

    // Devised test cases
    @Test
    public void testArrayIsNullOrEmpty() {
        int[] emptyArray = {};
        assertEquals(0, MaximumSubarray.maxSubArray(emptyArray));
        assertThrows(IllegalArgumentException.class, () -> MaximumSubarray.maxSubArray(null));
    }

    @Test
    public void testArrayLength1() {
        int[] arrayLength1 = {5};
        assertEquals(5, MaximumSubarray.maxSubArray(arrayLength1));
    }

    @Test
    public void testArrayOnlyPositive() {
        int[] arrayPositiveUnsorted = {5, 3, 6, 1, 8};
        int[] arrayPositiveAscending = {1, 2, 3, 4, 5};
        int[] arrayPositiveDescending = {5, 4, 3, 2, 1};
        assertEquals(23, MaximumSubarray.maxSubArray(arrayPositiveUnsorted));
        assertEquals(15, MaximumSubarray.maxSubArray(arrayPositiveAscending));
        assertEquals(15, MaximumSubarray.maxSubArray(arrayPositiveDescending));
    }

    @Test
    public void testArrayOnlyNegative() {
        int[] arrayNegativeUnsorted = {-5, -3, -6, -1, -8};
        int[] arrayNegativeAscending = {-5, -4, -3, -2, -1};
        int[] arrayNegativeDescending = {-1, -2, -3, -4, -5};
        assertEquals(-1, MaximumSubarray.maxSubArray(arrayNegativeUnsorted));
        assertEquals(-1, MaximumSubarray.maxSubArray(arrayNegativeAscending));
        assertEquals(-1, MaximumSubarray.maxSubArray(arrayNegativeDescending));
    }

    @Test
    public void testArrayTwoParts() {
        int[] arrayPositiveNegative = {4, 5, -3, -6};
        int[] arrayNegativePositive = {-5, -4, 3, 6};
        assertEquals(9, MaximumSubarray.maxSubArray(arrayPositiveNegative));
        assertEquals(9, MaximumSubarray.maxSubArray(arrayNegativePositive));
    }

    @Test
    public void testArrayMixed() {
        int[] arrayMixedFirstPositive = {4, -5, 6, -5};
        int[] arrayMixedFirstNegative = {-5, 4, -3, 6};
        assertEquals(6, MaximumSubarray.maxSubArray(arrayMixedFirstPositive));
        assertEquals(7, MaximumSubarray.maxSubArray(arrayMixedFirstNegative));
    }

    @Test
    public void testArrayTwoConsequentValues() {
        int[] arrayPositiveNegativeEqual = {1, 6, -6, 3};
        int[] arrayNegativePositiveEqual = {1, -6, 6, 3};
        int[] arrayPositiveNegativeFirstBigger = {1, 6, -5, 3};
        int[] arrayNegativePositiveFirstSmaller = {1, -5, 6, 3};
        int[] arrayPositiveNegativeFirstSmaller = {1, 5, -6, 3};
        int[] arrayNegativePositiveFirstBigger = {1, -6, 5, 3};
        assertEquals(7, MaximumSubarray.maxSubArray(arrayPositiveNegativeEqual));
        assertEquals(9, MaximumSubarray.maxSubArray(arrayNegativePositiveEqual));
        assertEquals(7, MaximumSubarray.maxSubArray(arrayPositiveNegativeFirstBigger));
        assertEquals(9, MaximumSubarray.maxSubArray(arrayNegativePositiveFirstSmaller));
        assertEquals(6, MaximumSubarray.maxSubArray(arrayPositiveNegativeFirstSmaller));
        assertEquals(8, MaximumSubarray.maxSubArray(arrayNegativePositiveFirstBigger));
    }
}
