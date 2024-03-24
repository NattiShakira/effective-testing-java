package zest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PalindromeTwoTest {

    @Test
    void testNullInput() {
        // As Java primitive types cannot be null, this test case is not applicable.
    }

    @Test
    void testLowerBoundaryConstraint(){
        assertFalse(PalindromeTwo.isPalindrome(-(int)Math.pow(2,20)-1));
    }

    @Test
    void testLowerBoundary() {
        assertFalse(PalindromeTwo.isPalindrome(-(int)Math.pow(2, 20)));
    }
    @Test
    void testNegativeOne() {
        assertFalse(PalindromeTwo.isPalindrome(-1));
    }
    @Test
    void testZero() {
        assertTrue(PalindromeTwo.isPalindrome(0));
    }

    @Test
    void testOne() {
        assertTrue(PalindromeTwo.isPalindrome(1));
    }

    @Test
    void testUpperBoundary() {
        assertFalse(PalindromeTwo.isPalindrome((int)Math.pow(2, 20) - 1));
    }
    @Test
    void testUpperBoundaryConstraint() {
        assertFalse(PalindromeTwo.isPalindrome((int)Math.pow(2, 20)));
    }

    @Test
    void testJustBeforeTwoDigitPalindrome() {
        assertFalse(PalindromeTwo.isPalindrome(10));
    }

    @Test
    void testSmallestTwoDigitPalindrome() {
        assertTrue(PalindromeTwo.isPalindrome(11));
    }

    @Test
    void testJustAfterTwoDigitPalindrome() {
        assertFalse(PalindromeTwo.isPalindrome(12));
    }
    // New Test Cases for In-point and Out-point
    @Test
    void testClosestSmallerPalindromeToUpperBoundary() {
        assertTrue(PalindromeTwo.isPalindrome(1_048_401));
    }

    @Test
    void testClosestLargerPalindromeToUpperBoundary() {
        assertFalse(PalindromeTwo.isPalindrome(1_049_401));
    }
    // New test for branch coverage of line 19
    @Test
    void testThreeDigitNumberNPalindromeAndDivisibleBy11() {
        assertTrue(PalindromeTwo.isPalindrome(202));
    }
    // New test for branch coverage of line 27
    @Test
    void testEvenLengthPalindrome(){
        assertTrue(PalindromeTwo.isPalindrome(2222));
    }
    // New test condition coverage
    @Test
    void testThreeDigitNumberNotPalindrome() {
        assertFalse(PalindromeTwo.isPalindrome(231)); //
    }
}