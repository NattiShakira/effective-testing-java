package zest;

import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ClimbingStairsTest {
    ClimbingStairs climber = new ClimbingStairs();

    @Test
    void zeroNumber() {
        assertEquals(0, climber.climbStairs(0));
    }

    @Test
    void lessThenTwoNumber() {
        assertEquals(1, climber.climbStairs(1));
    }

    @Test
    void twoNumber() {
        assertEquals(2, climber.climbStairs(2));
    }

    @Test
    void largerThanTwoNumber() {
        assertEquals(3, climber.climbStairs(3));
    }

    @Test
    void negativeInputThrowError() {
        ClimbingStairs climber = new ClimbingStairs();
        assertThrows(IllegalArgumentException.class, () -> climber.climbStairs(-1));
    }

    @Test
    void largeNumberTestInvariant() {
        assertEquals(89, climber.climbStairs(10));
    }

    @Test
    void resultsShouldAlwaysBeNonNegative() {
        for (int n = 0; n <= 30; n++) {
            long result = climber.climbStairs(n);
            assertTrue(result >= 0, "Post-condition failed");
        }
    }

    @Property
    void correctExecutionBelowTwo(@ForAll @IntRange(min = 0, max = 2) int n) {
        long result = climber.climbStairs(n);
        assertEquals(n, result);
    }

    @Property
    void correctExecutionInRange(@ForAll @IntRange(min = 3, max = 91) int n) {
        long result = climber.climbStairs(n);
        assertEquals(climber.climbStairs(n - 1) + climber.climbStairs(n - 2), result);
    }

    @Property
    void climbStairsInvalidCaseProperty(@ForAll int n) {
        Assume.that(n < 0 || n > 91);
        assertThrows(IllegalArgumentException.class, () -> climber.climbStairs(n));
    }

}


