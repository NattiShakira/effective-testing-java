# 1. Specification testing

From the README.md file i gathered the following partitions:

- Numerator and Denominator positive

- Numerator and Denominator negative

- Numerator 0

- Denominator 0

- Numerator and Denominator are of different signs

- goal fraction includes a repeating part

Possible boundaries include:

- Very large Denominator

- Very large Numerator

- Numerator and Denominator around 0 (both negative and positive)

I then devised test cases for these, and implemented them. The test "testDenominatorIsZero"
reveals a bug. There is no safeguard against dividing by zero in the code. This is easily fixed
by inserting this line at the start of the method:
```java
if (denominator == 0) return null;
```
The test testVeryLargeDenumerator reveals another bug, namely that the answer string is way longer than the first 104 digits.
To fix this I changed the conditional of the while loop to:
```java
while (num != 0 && res.length() < 103)
```
This doesn't round the last digit, but I think 101 digits after the decimal points are precise enough.

2. Structural testing

The only line not covered is the initiation of the class. To achieve coverage for this line, I added a test "testConstructor"
which just checks whether an instance of Frac2Dec gets created on call. With this 100% Branch and Line coverage is achieved.

3. Mutation testing

The first mutation test report revealed some mutants that survive, namely changing the conditional boundaries in this line:

```Java
res.append(((numerator > 0) ^ (denominator > 0)) ? "-" : "");
```

I added extenisve tests in testNumDenAroundZero, but the mutants survive. This leads me to conclude that these are equivalent mutants,
as changing > to >= or similar has no impact on the code. This is due to the first two conditional checks in the code, that handle
the case where either or both the numerator or denominator are 0, therefore the above line will never even get to check cases where either
numerator or denominator are 0.

All other mutants are killed.