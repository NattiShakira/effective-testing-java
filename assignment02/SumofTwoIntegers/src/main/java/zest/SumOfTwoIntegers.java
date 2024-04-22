package zest;

public class SumOfTwoIntegers {
    public int getSum(int a, int b) {

        //pre-condition:
        //assert Integer.MIN_VALUE <= a && a <= Integer.MAX_VALUE : "Input a is out of 32-bit integer range";
        //assert Integer.MIN_VALUE <= b && b <= Integer.MAX_VALUE : "Input b is out of 32-bit integer range";
        long initialA = a;
        long initialB = b;
        while (b != 0) {
            int carry = (a & b) << 1;  // Carry now contains common set bits of a and b
            a = a ^ b;  // Sum of bits of a and b where at least one of the bits is not set
            b = carry;  // Carry is shifted by one so that adding it to a gives the required sum


            //invariants
            //assert Integer.MIN_VALUE <= a && a <= Integer.MAX_VALUE : "Variable a is out of 32-bit integer range during computation";
            //assert Integer.MIN_VALUE <= b && b <= Integer.MAX_VALUE : "Variable b is out of 32-bit integer range during computation";
        }
        if ((initialA > 0 && initialB > 0 && a < 0) || (initialA < 0 && initialB < 0 && a > 0)) {
            throw new RuntimeException("Overflow detected");
        }
        //post-condition
        //assert Integer.MIN_VALUE <= a && a <= Integer.MAX_VALUE : "Result is out of 32-bit integer range";
        return a;
    }
}
