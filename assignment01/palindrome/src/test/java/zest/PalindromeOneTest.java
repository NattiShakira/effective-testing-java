package zest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

    class PalindromeOneTest {

    @Test
    void testNullInput() {
        // As Java primitive types cannot be null, this test case is not applicable.
    }

    @Test
    void testLowerBoundaryConstraint(){

        assertFalse(PalindromeOne.isPalindrome(-(int)Math.pow(2,20)-1));
    }

    @Test
    void testLowerBoundary() {
        assertFalse(PalindromeOne.isPalindrome(-(int)Math.pow(2, 20)));
    }

    @Test
    void testNegativeOne() {

        assertFalse(PalindromeOne.isPalindrome(-1));
    }

    @Test
    void testZero() {

        assertTrue(PalindromeOne.isPalindrome(0));
    }

    @Test
    void testOne() {

        assertTrue(PalindromeOne.isPalindrome(1));
    }

    @Test
    void testUpperBoundary() {

        assertFalse(PalindromeOne.isPalindrome((int)Math.pow(2, 20) - 1));
    }
    @Test
    void testUpperBoundaryConstraint() {

        assertFalse(PalindromeOne.isPalindrome((int)Math.pow(2, 20)));
    }

    @Test
    void testJustBeforeTwoDigitPalindrome() {

        assertFalse(PalindromeOne.isPalindrome(10));
    }

    @Test
    void testSmallestTwoDigitPalindrome() {

        assertTrue(PalindromeOne.isPalindrome(11));
    }

    @Test
    void testJustAfterTwoDigitPalindrome() {

        assertFalse(PalindromeOne.isPalindrome(12));
    }
    // New Test Cases for In-point and Out-point
    @Test
    void testClosestSmallerPalindromeToUpperBoundary() {
        assertTrue(PalindromeOne.isPalindrome(1_048_401));
    }

    @Test
    void testClosestLargerPalindromeToUpperBoundary() {
        assertFalse(PalindromeOne.isPalindrome(1_049_401));
    }
}
