package zest;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UniquePathsTest {
    UniquePaths uniquePaths = new UniquePaths();

    // Tests for Task 1. Code Coverage
    @Test
    void testSimpleSquareGrid() {
        assertEquals(6, uniquePaths.uniquePaths(3, 3));
    }

    @Test
    void testSimpleNonSquareGrid() {
        assertEquals(28,  uniquePaths.uniquePaths(3, 7));
    }

    // Tests for Task 3. Testing Contracts
    @Test
    void testPostConditionsAreSatisfied() {
        assertEquals(28, uniquePaths.uniquePaths(3, 7));
        assertThrows(IllegalArgumentException.class, () -> uniquePaths.uniquePaths(0, 7));
        assertThrows(IllegalArgumentException.class, () -> uniquePaths.uniquePaths(-1, 7));
        assertThrows(IllegalArgumentException.class, () -> uniquePaths.uniquePaths(101, 7));
        assertThrows(IllegalArgumentException.class, () -> uniquePaths.uniquePaths(7, 0));
        assertThrows(IllegalArgumentException.class, () -> uniquePaths.uniquePaths(7, -1));
        assertThrows(IllegalArgumentException.class, () -> uniquePaths.uniquePaths(7, 101));
    }

    @Test
    void testInvariantSatisfiesConditions() {
        assertTrue(uniquePaths.invariantSatisfiesConditions(2, 1, 1));
        assertThrows(RuntimeException.class, () -> uniquePaths.invariantSatisfiesConditions(2, 2, 1));
        assertThrows(RuntimeException.class, () -> uniquePaths.invariantSatisfiesConditions(2, 1, 2));
    }

    @Test
    void testResultSatisfiesConditions() {
        assertTrue(uniquePaths.resultSatisfiesConditions(1));
        assertThrows(RuntimeException.class, () -> uniquePaths.resultSatisfiesConditions(0));
        assertThrows(RuntimeException.class, () -> uniquePaths.resultSatisfiesConditions(-1));
    }

    // Tests for Task 4. Property-Based Testing
    @Property
    void testOnlyOneRow(@ForAll @IntRange(min = 1, max = 100) int n) {
        int result = uniquePaths.uniquePaths(1, n);
        assertEquals(1, result);
    }

    @Property
    void testOnlyOneColumn(@ForAll @IntRange(min = 1, max = 100) int m) {
        int result = uniquePaths.uniquePaths(m, 1);
        assertEquals(1, result);
    }

    @Property
    void testInvalidM(@ForAll int m, @ForAll @IntRange(min = 1, max = 100) int n) {
        Assume.that(m < 1 || m > 100);
        assertThrows(IllegalArgumentException.class, () -> uniquePaths.uniquePaths(m, n));
    }

    @Property
    void testInvalidN(@ForAll int n, @ForAll @IntRange(min = 1, max = 100) int m) {
        Assume.that(n < 1 || n > 100);
        assertThrows(IllegalArgumentException.class, () -> uniquePaths.uniquePaths(m, n));
    }

    @Property
    void testvValidM(@ForAll @IntRange(min = 1, max = 100) int m) {
        int actualResult = uniquePaths.uniquePaths(m, 5);
        int expectedResult = calculatePaths(m, 5);
        assertEquals(expectedResult, actualResult);
    }

    @Property
    void testvValidN(@ForAll @IntRange(min = 1, max = 100) int n) {
        int actualResult = uniquePaths.uniquePaths(5, n);
        int expectedResult = calculatePaths(5, n);
        assertEquals(expectedResult, actualResult);
    }

    // Calculate binomial coefficient: n choose k
    private int binomialCoeff(int n, int k) {
        if (k > n - k) {
            k = n - k;
        }
        int result = 1;
        for (int i = 0; i < k; ++i) {
            result *= (n - i);
            result /= (i + 1);
        }
        return result;
    }

    // Calculate number of paths in an n x m grid
    private int calculatePaths(int m, int n) {
        return binomialCoeff(m + n - 2, m - 1); // Binomial coefficient of (n + m - 2 choose n - 1)
    }

}