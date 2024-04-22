package zest;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SumOfTwoIntegersTest {
    private final SumOfTwoIntegers calculator = new SumOfTwoIntegers();
    //Testing contracts:
    @Test
    public void testSumPositiveNumbers() {
        assertEquals(3, calculator.getSum(1, 2));
    }

    @Test
    public void testSumNegativeNumbers() {
        assertEquals(1, calculator.getSum(-2, 3));
    }

    @Test
    public void testSumWithZero() {
        assertEquals(-1, calculator.getSum(-1, 0));
    }

    @Test
    public void testOverflowDetection() {
        SumOfTwoIntegers calculator = new SumOfTwoIntegers();
        assertThrows(RuntimeException.class, () -> calculator.getSum(Integer.MAX_VALUE, 1));
        assertThrows(RuntimeException.class, () -> calculator.getSum(Integer.MIN_VALUE, -1));
    }
    //Property-based testing:

    @Property
    boolean commutativityProperty(@ForAll int a, @ForAll int b) {
        try{
            return calculator.getSum(a, b) == calculator.getSum(b, a);
        } catch (RuntimeException e){
            assertEquals(e.getMessage(), "Overflow detected");
            return true;
        }
    }

    @Property
    boolean associativityProperty(@ForAll int a, @ForAll int b, @ForAll int c) {
        try {
            return calculator.getSum(calculator.getSum(a, b), c) == calculator.getSum(a, calculator.getSum(b, c));
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(), "Overflow detected");
            return true;
        }
    }

    @Property
    boolean identityElementProperty(@ForAll int a) {
        return calculator.getSum(a, 0) == a;
    }

    @Property
    boolean inverseElementProperty(@ForAll int a) {
        return calculator.getSum(a, -a) == 0;
    }

}