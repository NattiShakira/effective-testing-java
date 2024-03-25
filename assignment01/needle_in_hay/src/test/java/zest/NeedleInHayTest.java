package zest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NeedleInHayTest {
    @Test
    void test1Null(){
        assertEquals(-1, NeedleInHay.find("hehe", null));
        assertEquals(-1, NeedleInHay.find(null, "hihi"));
        assertEquals(-1, NeedleInHay.find(null, null));
    }

    @Test
    void test2BothEmpty(){
        assertEquals(0, NeedleInHay.find("",""));
    }

    @Test
    void test3OneEmpty(){
        assertEquals(-1, NeedleInHay.find("", "Donde"));
        assertEquals(-1, NeedleInHay.find("Dante", ""));
    }

    @Test
    void test4Size1(){
        assertEquals(-1, NeedleInHay.find("f", "s"));
        assertEquals(0, NeedleInHay.find("f", "f"));
    }

    @Test
    void test5NeedleTwice(){
        assertEquals(5, NeedleInHay.find("stackhaydayafterhayday", "hay"));
    }

    @Test
    void test6CaseSensitivity(){
        assertEquals(3, NeedleInHay.find("duRdAmA","dA"));
    }

    @Test
    void test7Constructor() {
        NeedleInHay testInstance = new NeedleInHay();
        assertInstanceOf(NeedleInHay.class, testInstance);
    }
}