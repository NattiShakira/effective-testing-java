package zest;

class MyAtoiTest {

    @Test
    void nullTest(){
        assertEquals(MyAtoi.myAtoi(null),0);

    }

    @Test
    void emptyTest(){
        assertEquals(MyAtoi.myAtoi(""),0);

    }
    @Test
    void startingWhitespace(){
        assertEquals(MyAtoi.myAtoi("  11"),11);
    }
    @Test
    void startWithMinus(){
        assertEquals(MyAtoi.myAtoi("-31"),-31);
    }
    @Test
    void startWithPlus(){
        assertEquals(MyAtoi.myAtoi("+31"),+31);
    }
    @Test
    void startWithLetter(){
        assertEquals(MyAtoi.myAtoi("a123"),0);
    }
    @Test
    void ignoreFollowingCharacterWithoutSign(){
        assertEquals(MyAtoi.myAtoi("123a"),123);
    }

    @Test
    void ignoreFollowingCharacterWithSign(){
        assertEquals(MyAtoi.myAtoi("-123 a"),-123);
    }

    @Test
    void outOfBoundMinus(){
        assertEquals(MyAtoi.myAtoi("-2147483649"),-2147483648);
    }

    @Test
    void outOfBoundPlus(){
        assertEquals(MyAtoi.myAtoi("2147483649"),2147483647);
    }
    @Test
    void StartingZeros(){
        assertEquals(MyAtoi.myAtoi("0000000000012345678"),12345678);
    }
    @Test
    void positiveInBound(){
        assertEquals(MyAtoi.myAtoi("2147483647"),2147483647);
    }
    @Test
    void negativeInBound(){
        assertEquals(MyAtoi.myAtoi("-2147483648"),-2147483648);
    }
    @Test
    void numberWithSpaces(){
        assertEquals(MyAtoi.myAtoi(" 12"),12);
    }
    // added after step 7 of specification-based testing
    @Test
    void startWithTab(){
        assertEquals(MyAtoi.myAtoi("\t99"),0);
    }
}