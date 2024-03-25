# combination_sum
By Markus Niemack
## 1. Specification testing

I applied the seven step principle from chapter 2 in the book. Meaning first I read the README.md, then read the code to
understand what's going on. Then I identified some partitions.

The partitions are:

- candidates contains no entries in list.

- candidates contain multiple entries in list.

- candidates contain multiple valid combinations in list.

- candidates contain only negative integers.

- candidates contain only positive integers.

- candidates contain only 0.

- candidates contain a list of mixed positive and negative integers, as well as 0.

- target is 0

- target is positive

- target is negative

- target is smaller than smallest candidate

- target is greater than all candidates

- candidates contain duplicates

- for a target, more than 150 combinations are listed.

the boundaries I identified for this problem are:

- empty candidate list

- target smaller than smallest candidate

- target larger than all candidates

- negative candidates

I then implemented test cases based on these assumptions.
The tests, testNegativeCandidates and testMixedCandidates both provoke an out of bounds error,
as there is no input sanitation for negative numbers. line 20 infinitely recurs.
The reason for this is, that the condition target >= candidates[i] will always be true
if the target is positive, and some of the candidates negative.
The fix for this is to skip negative candidates, if that is the intended behaviour.
This could be done by adding:

(this fix will be fixed in part 3, mutation testing)
```java
if (candidates[i] < 0){
    continue;
}
```

at the start of the for loop.


The test testDuplicateCandidates reveals another bug in the code, namely that
duplicate candidates lead to duplicate combinations in the result.
This is easily fixed by adding the condition:

```java
if(i > 0 && candidates[i] == candidates[i-1]){
    continue;
}
```

At the start of the for loop. you could combine this with the above condition, but I didn't, as to  make it easier to realize what fixes what.
The test testTooManyCombinations also fails, as there's no check to see whether there are 150 or more combinations in the result.
to fix this, at the very start of the for loop, I added:

```java
if(result.size() >= 149){
    break;
}
```

Note that this does not check if a target only has 149 or less combinations,
but this is hard to know before computation, therefore this just breaks out
of the loop on the 150th iteration. If the wanted behaviour was to send an error, you would have to change the break statement, into a -1 return, or similar.
The test negativeCandidatesAndTarget fails too, but it's unclear to me
whether this is a bug or not, as negative integers are not mentioned in the requirements.
I'm assuming negative integers are unwanted inputs, therefore no fix is needed, as it returns
an empty list.



## 2. Structural testing

This test suite already gives a very good coverage, only missing line 7, which is the
Class initiation. All the structural testing I did, was just one test, which tests
said Initiation by testing if an instance of the class is created. With this
I have a suite that has 100% line and branch coverage.

## 3. Mutation testing

The mutation tests uncovered an issue with the bugfix I provided above. It missed
cases where the candidate is 0.

```java
if (candidates[i] <= 0)
    {
        continue;
    }
```

This is the fixed fix.
It also uncovered another problem, removing line 2 didn't break any tests,
as all my tests had a pre-sorted candidate list. I fixed this by adding a
test case with an unsorted list.
With these fixes, the mutation tests let no mutants survive.


# frac2dec

By Markus Niemack

## 1. Specification testing

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

## 2. Structural testing

The only line not covered is the initiation of the class. To achieve coverage for this line, I added a test "testConstructor"
which just checks whether an instance of Frac2Dec gets created on call. With this 100% Branch and Line coverage is achieved.

## 3. Mutation testing

The first mutation test report revealed some mutants that survive, namely changing the conditional boundaries in this line:

```Java
res.append(((numerator > 0) ^ (denominator > 0)) ? "-" : "");
```

I added extenisve tests in testNumDenAroundZero, but the mutants survive. This leads me to conclude that these are equivalent mutants,
as changing > to >= or similar has no impact on the code. This is due to the first two conditional checks in the code, that handle
the case where either or both the numerator or denominator are 0, therefore the above line will never even get to check cases where either
numerator or denominator are 0.

All other mutants are killed.




