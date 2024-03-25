package zest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MedianOfArraysTest {
    private MedianOfArrays medianOfArrays;

    @BeforeEach
    public void setUp() {
        medianOfArrays = new MedianOfArrays();
    }

    // Exploratory tests (happy cases)
    @Test
    public void testOneArrayIsEmpty() {
        int[] nums1 = new int[0];
        int[] nums2 = {2, 3, 5};
        double expected = 3.0;
        assertEquals(expected, medianOfArrays.findMedianSortedArrays(nums1, nums2));
    }
    @Test
    public void testArrayOneAfterAnother() {
        int[] nums1 = {1, 2, 3};
        int[] nums2 = {4, 5, 6};
        double expected = 3.5;
        assertEquals(expected, medianOfArrays.findMedianSortedArrays(nums1, nums2));
    }
    @Test
    public void testTwoEqualOddArrays() {
        int[] nums1 = {1, 2, 3};
        int[] nums2 = {1, 2, 3};
        double expected = 2.0;
        assertEquals(expected, medianOfArrays.findMedianSortedArrays(nums1, nums2));
    }

    // Devised test cases
    @Test
    public void testExceptionalCases1() {
        //nums1 is null
        assertEquals(0, medianOfArrays.findMedianSortedArrays(null, new int[]{1, 2, 3, 4, 5}));
    }
    @Test
    public void testExceptionalCases2() {
        //nums2 is null
        assertEquals(0, medianOfArrays.findMedianSortedArrays(new int[]{1, 2, 3, 4, 5}, null));
    }
    @Test
    public void testExceptionalCases3() {
        //both are empty
        assertEquals(-1, medianOfArrays.findMedianSortedArrays(new int[0], new int[0]));
    }
    @Test
    public void testExceptionalCases4() {
        //nums1 is empty
        assertEquals(3, medianOfArrays.findMedianSortedArrays(new int[0], new int[]{1, 2, 3, 4, 5}));
    }
    @Test
    public void testExceptionalCases5() {
        //nums2 is empty
        assertEquals(3, medianOfArrays.findMedianSortedArrays(new int[]{1, 2, 3, 4, 5}, new int[0]));
    }
    @Test
    public void testExceptionalCases6() {
        //nums1 is sorted descending
        assertEquals(0, medianOfArrays.findMedianSortedArrays(new int[]{5, 4, 3, 2, 1}, new int[]{1, 2, 3, 4, 5}));
    }
    @Test
    public void testExceptionalCases7() {
        //nums2 is sorted descending
        assertEquals(0, medianOfArrays.findMedianSortedArrays(new int[]{1, 2, 3, 4, 5}, new int[]{5, 4, 3, 2, 1}));
    }
    @Test
    public void testExceptionalCases8() {
        //nums1 is not sorted at all
        assertEquals(0, medianOfArrays.findMedianSortedArrays(new int[]{5, 6, 3, 2, 1}, new int[]{1, 2, 3, 4, 5}));
    }
    @Test
    public void testExceptionalCases9() {
        //nums2 is not sorted at all
        assertEquals(0, medianOfArrays.findMedianSortedArrays(new int[]{1, 2, 3, 4, 5}, new int[]{5, 6, 3, 2, 1}));
    }

    @Test
    public void testNoMergerOdd1() {
        //nums1 goes first, nums1's length is 1 bigger
        assertEquals(3, medianOfArrays.findMedianSortedArrays(new int[]{1, 2, 3}, new int[]{4, 5}));
    }
    @Test
    public void testNoMergerOdd2() {
        //nums1 goes first, nums2's length is 1 bigger
        assertEquals(3, medianOfArrays.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4, 5}));
    }
    @Test
    public void testNoMergerOdd3() {
        //nums1 goes first, nums1's length is more than 1 bigger
        assertEquals(4, medianOfArrays.findMedianSortedArrays(new int[]{1, 2, 3, 4, 5}, new int[]{6, 7}));
    }
    @Test
    public void testNoMergerOdd4() {
        //nums1 goes first, nums2's length is more than 1 bigger
        assertEquals(4, medianOfArrays.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4, 5, 6, 7}));
    }
    @Test
    public void testNoMergerOdd5() {
        //reversed
        assertEquals(3, medianOfArrays.findMedianSortedArrays(new int[]{4, 5}, new int[]{1, 2, 3}));
    }
    @Test
    public void testNoMergerOdd6() {
        //nums2 goes first, nums1's length is 1 bigger
        assertEquals(3, medianOfArrays.findMedianSortedArrays(new int[]{3, 4, 5}, new int[]{1, 2}));
    }
    @Test
    public void testNoMergerOdd7() {
        //nums2 goes first, nums2's length is more than 1 bigger
        assertEquals(4, medianOfArrays.findMedianSortedArrays(new int[]{6, 7}, new int[]{1, 2, 3, 4, 5}));
    }
    @Test
    public void testNoMergerOdd8() {
        //nums2 goes first, nums1's length is more than 1 bigger
        assertEquals(4, medianOfArrays.findMedianSortedArrays(new int[]{3, 4, 5, 6, 7}, new int[]{1, 2}));
    }

    @Test
    public void testNoMergerEven1() {
        //nums1 goes first, nums1's and nums2 length is equal
        assertEquals(2.5, medianOfArrays.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4}));
    }
    @Test
    public void testNoMergerEven2() {
        ///nums2 goes first, nums1's and nums2 length is equal
        assertEquals(2.5, medianOfArrays.findMedianSortedArrays(new int[]{3, 4}, new int[]{1, 2}));
    }
    @Test
    public void testNoMergerEven3() {
        //nums1 goes first, nums1's length is more than 1 bigger
        assertEquals(3.5, medianOfArrays.findMedianSortedArrays(new int[]{1, 2, 3, 4}, new int[]{5, 6}));
    }
    @Test
    public void testNoMergerEven4() {
        //nums2 goes first, nums1's length is more than 1 bigger
        assertEquals(3.5, medianOfArrays.findMedianSortedArrays(new int[]{3, 4, 5, 6}, new int[]{1, 2}));
    }

    @Test
    public void testMergerOdd1() {
        //nums1's length is bigger
        assertEquals(3, medianOfArrays.findMedianSortedArrays(new int[]{1, 3, 5}, new int[]{2, 4}));
    }
    @Test
    public void testMergerOdd2() {
        //nums2's length is bigger
        assertEquals(3, medianOfArrays.findMedianSortedArrays(new int[]{2, 4}, new int[]{1, 3, 5}));
    }

    @Test
    public void testMergerEven1() {
        //nums1's length is even, returns an average of values belonging to 1 array
        assertEquals(3.5, medianOfArrays.findMedianSortedArrays(new int[]{1, 3, 4}, new int[]{2, 5, 6}));
    }
    @Test
    public void testMergerEven2() {
        //nums1's length is even, returns an average of values belonging to different arrays
        assertEquals(4, medianOfArrays.findMedianSortedArrays(new int[]{1, 2, 5}, new int[]{3, 6, 7}));
    }
    @Test
    public void testMergerEven3() {
        //nums1's length is bigger, returns an average of values belonging to 1 array
        assertEquals(5, medianOfArrays.findMedianSortedArrays(new int[]{1, 2, 4, 6, 7}, new int[]{3, 8, 9}));
    }
    @Test
    public void testMergerEven4() {
        //nums1's length is bigger, returns an average of values belonging to different arrays
        assertEquals(5, medianOfArrays.findMedianSortedArrays(new int[]{1, 2, 4, 7, 8}, new int[]{3, 6, 9}));
    }
    @Test
    public void testMergerEven5() {
        //nums2's length is bigger, returns an average of values belonging to 1 array
        assertEquals(5, medianOfArrays.findMedianSortedArrays(new int[]{3, 8, 9}, new int[]{1, 2, 4, 6, 7}));
    }
    @Test
    public void testMergerEven6() {
        //nums2's length is bigger, returns an average of values belonging to different arrays
        assertEquals(5, medianOfArrays.findMedianSortedArrays(new int[]{3, 6, 9}, new int[]{1, 2, 4, 7, 8}));
    }

    @Test
    public void testDuplicateValues1() {
        //nums1 has duplicate values
        assertEquals(3, medianOfArrays.findMedianSortedArrays(new int[]{1, 3, 3, 4}, new int[]{2, 5, 6}));
    }
    @Test
    public void testDuplicateValues2() {
        //nums2 has duplicate values
        assertEquals(4, medianOfArrays.findMedianSortedArrays(new int[]{1, 3, 4}, new int[]{2, 5, 5, 6}));
    }

}
