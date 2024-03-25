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


# median_of_arrays
By Natalia Carrion Gimenez
## 1. Specification-based testing

First, I read the requirements to understand the goal of the program and its inputs and outputs:
1. The goal of the program is to find a median of the two sorted in ascending order arrays.
2. The program receives two parameters: a sorted in ascending order array of integers of length m and a sorted in ascending order array of integers of length n.
3. The program returns a double representing the median of the two sorted arrays.

Second, I explored what the program does for various inputs and wrote several tests for happy cases:
1. Given arrays [1, 2, 3] and [4, 5, 6], the program should return 3.5.
2. Given arrays [1, 2, 3] and [1, 2, 3], the program should return 2.
3. Given arrays [] and [2, 3, 5], the program should return 3.

These tests passed!

Next, I explored possible inputs and outputs and identified partitions and boundaries.
1. First input is an array of integers of length m, thus we need to check the following cases:
- array is null;
- array is empty;
- array's length = 1;
- array's length > 1. In this case, there should be checked several options: 
- an array is sorted in ascending order;
- an array is sorted in descending order;
- an array is sorted but has duplicate values;
- an array is not sorted.

Second input is an array of integers of length n. Partitions for this input is similar to the case above.

2. Speaking about input combinations, the two arrays have a relationship:
- the sum of the lengths of the arrays can be odd or even. In case it is odd, the program should return a certain 
value that belongs to one of the arrays. In case it is even, the program should return an average of two values that 
can belong to the same array or to different arrays. 
- the lengths of the arrays can be the same, or one of the arrays can be larger than the other. The program should handle these cases.
- values of the arrays should be also considered: there can be cases when the last value of one array is smaller that the first value of another array, in this case a "merger" is not required; in the opposite case, a "merger" can be necessary.

3. Since the program outputs a single double, there are not so many options to check but the program should check when the output is 0 (either array is null or empty) or -1 (both arrays are empty).

Taking the above discussion into account, the following test cases were devised:
1. Exceptional cases
- T1: the first array is null
- T2: the second array is null
- T3: the first array is empty
- T4: the second array is empty
- T5: both arrays are empty
- T6: the first array is sorted in descending order
- T7: the second array is sorted in descending order
- T8: the first array is not sorted
- T9: the second array is not sorted

2. Cases that don't require to "merge" arrays (the last value of the first arrays if smaller than the first value of the second array):

2.1. The sum of lengths of arrays is odd:
- T10: first array goes first, and it's length is exactly 1 bigger than the length of the second array
- T11: first array goes first, but the length of the second array is 1 bigger than the length of the first array
- T12: first array goes first, and it's length is more than 1 element bigger than the length of the second array
- T13: first array goes first, but the length of the second array is more than 1 element bigger than the length of the first array
- T14-T17: similar to T10-13, but now the second array goes first.

2.2. The sum of lengths of arrays is even:
- T18: first array goes first, lengths of both arrays are equal
- T19: first array goes first, and it's length is 2 elements bigger that the length of the second array
- T20-T21: similar to T18-19, but now the second array goes first

3. Cases that require to "merge" arrays (their values are mixed):

3.1. The sum of lengths of arrays is odd:
- T22: the length of the first array is bigger
- T23: the length of the second array is bigger

3.2. The sum of the lengths of arrays is even:
- T24: length of each array is the same (and odd), the program returns an average of values belonging to the same array
- T25: length of each array is the same (and odd), the program returns an average of values belonging to different arrays
- T26: length of the first array is bigger, the program returns an average of values belonging to the same array
- T27: length of the first array is bigger, the program returns an average of values belonging to different arrays
- T28-29: similar to T26-27, but now the second array's length is bigger

4. An array has duplicate values:
- T30: first array has duplicate values
- T31: second array has duplicate values

After all the tests were implemented, 29 of them passed, but 2 broke. T30 and T31 didn't pass, because the program didn't consider an 
array containing duplicate values being properly sorted. To solve this problem, the following fix was made to a method isArraySortedAscending:

a conditional boundary 
```java
if (array[i] >= array[i + 1]) {
    return false;
}
```
was changed to
```java
if (array[i] > array[i + 1]) {
    return false;
}
```

## 2. Structural testing

Jacoco coverage report showed that the implemented test suite has a 100% line, condition and branch coverage.

## 3. Mutation testing

Pitest ran 130 tests with 46 generated mutations out of which 44 were killed (96%).
Inspection of the survived mutants revealed the following:
1. The first mutant replaced integer addition with subtraction in the conditional statement of the method findMedianSortedArrays:

The condition
```java
if ((m + n) % 2 == 0) {...}
```
was changed to
```java
if ((m - n) % 2 == 0) {...}
```

Since both versions of the conditional statements evaluate to truth or false given the same values of m and n, 
this mutant does not affect the program. Thus, it was decided to leave it as is, instead of writing a specific test that would check that the program performs addition and not subtraction in the conditional statement.

2. The second mutant changed a conditional boundary in the method getMin:

Supposedly, the conditional boundary 
```java
return nums1[p1] < nums2[p2] ? nums1[p1++] : nums2[p2++];
```
was changed to
```java
return nums1[p1] <= nums2[p2] ? nums1[p1++] : nums2[p2++];
```

Thorough analysis of the program showed that it doesn't matter what conditional boundary is used. In both cases, the program 
works correctly. The only aspect affected by this change, is the internal mechanics of the method getMin. More precisely, it affects what pointer (p1 or p2) moves first 
when the values to which these pointers point are equal. If we use <, then p2 moves first; if we use <=, then p1 moves first. For this reason, it was decided to let this mutant live.




