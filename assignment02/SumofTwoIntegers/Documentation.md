# FindDuplicate testing documentation by Markus Niemack
# 1. Line coverage
The Line and Branch coverage reaches 100% here, just with the contract and property tests.
# 2. Designing Contracts

The only Pre-condition I see in this method is:

- Integers must be 32-bit

 Post-conditions:
- Integers must be 32-bit still
- Result must be the sum of a and b
- Result shouldn't over-/underflow

Invariants:
- Integers must stay 32-bit
- No + or - operations can be used


The second invariant cannot really be checked by a test suite, so it is not tested here.

Technically, due to java type constraints, the a long cannot be outside of 32-bit Integer range.
The Checks for whether they are or not are commented out, since they are unnecessary.
I still added them as comments for completeness' sake. The post-condition
that the result shouldn't be an under-/ or overflow is checked via the conditional:
```java
if ((initialA > 0 && initialB > 0 && a < 0) || (initialA < 0 && initialB < 0 && a > 0)) {
    throw new RuntimeException("Overflow detected");
}
```

# 3. Testing Contracts

The tests marked with the comment "testing contracts" cover all listed pre- and post-conditions, but not the invariant "No + or -
operations can be used". This Invariant is not really possible to test via code.

# 4. Property-based testing

The Properties I identified are:

- Commutativity
- Associativity
- For every sum where one integer is 0, the sum should be equals to the other integer
- Subtracting an integer from itself should result in 0
- No Under-/Overflow

Addition and Subtraction in mathematics have the properties of being both commutative and associative,
hence the properties here. The other two properties are also simple mathematical properties of addition and subtraction,
namely the additive identity and the inverse element.
Since under-/overflow causes a runtimeException due to my checks, the commutativity/associativity
property tests have a try/catch block that handle this exception. 