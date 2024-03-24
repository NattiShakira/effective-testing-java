package zest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Frac2DecTest {
    @Test
    void testNormalCase(){
        assertEquals(("0.5"), Frac2Dec.fractionToDecimal(1, 2));
    }

    @Test
    void testRepeatFraction(){
        assertEquals(("0.(3)"), Frac2Dec.fractionToDecimal(1, 3));
    }

    @Test
    void testMixedNormalAndRepeatParts(){
        assertEquals(("0.1(6)"), Frac2Dec.fractionToDecimal(1, 6));
    }

    @Test
    void testEqualNumDenFraction(){
        assertEquals(("1"), Frac2Dec.fractionToDecimal(1, 1));
    }

    @Test
    void testNegativeNumerator(){
        assertEquals(("-0.5"), Frac2Dec.fractionToDecimal(-1, 2));
    }

    @Test
    void testNegativeDenominator(){
        assertEquals(("-0.5"), Frac2Dec.fractionToDecimal(1, -2));
    }

    @Test
    void testNegativeNumDen(){
        assertEquals(("0.5"), Frac2Dec.fractionToDecimal(-1, -2));
    }

    @Test
    void testNumeratorIsZero() {
        assertEquals(("0"), Frac2Dec.fractionToDecimal(0, 1));
    }

    @Test
    void testDenominatorIsZero() {
        assertEquals((null), Frac2Dec.fractionToDecimal(1, 0));
    }

    @Test
    void testVeryLargeNumerator(){
        assertEquals("123469875.9" ,Frac2Dec.fractionToDecimal(1234698759, 10));
    }

    @Test
    void testVeryLargeDenominator(){
        assertEquals("0.00000000809914153319400882300554737983663916519737912849072524256096705123520740495050582617456085091",
                Frac2Dec.fractionToDecimal(10, 1234698759));
    }

    @Test
    void testConstructor() {
        Frac2Dec testInstance = new Frac2Dec();
        assertTrue(testInstance instanceof Frac2Dec);
    }

    @Test
    void testNumDenAroundZero() {
        assertEquals("-1", Frac2Dec.fractionToDecimal(-1, 1));
        assertEquals("-1", Frac2Dec.fractionToDecimal(1, -1));
        assertEquals("0", Frac2Dec.fractionToDecimal(0, 1));
        assertEquals("0", Frac2Dec.fractionToDecimal(0, -1));
        assertEquals("1", Frac2Dec.fractionToDecimal(1, 1));
        assertEquals("1", Frac2Dec.fractionToDecimal(-1, -1));
        assertNull(Frac2Dec.fractionToDecimal(-1, 0));
        assertNull(Frac2Dec.fractionToDecimal(1, 0));
    }
}
