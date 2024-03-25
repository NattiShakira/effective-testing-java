## Specification Testing
### 1. Understanding the requirements
#### Business rules
By giving a number, we generate all possible combinations of well-formed parenthesis with the length * 2 of said number.\
Well-formed means that for every opening bracket, there is a closing bracket further down the line.\
We do not accept negative numbers or 0, as well as numbers greater than 8.

#### Input
Non-negative number between 1 and 8 (inclusive).

#### Output
An array with all permutations (or an empty one for invalid inputs).

### 2. Exploring the program
Tested some basic inputs.

### 3. Identifying partitions
Given the task description the partitions are as follows:
- Input
    - positive integers in range
    - positive integers out of range (empty array)
    - negative integers (empty array)
    - 0 (empty array)
    - (invalid types are not in the description and do not need to be covered since the parameter restricts the type to int)

### 4. Analyzing boundaries
The boundary testing should include:
- Going from positive to 0
    - On point: 1
    - Off point: 0
- Leaving the allowed range
    - On point: 8
    - Off point: 9

### 5. Devising test cases
Let's combine partitions with some sense.
- T1: Empty array from `0`
- T2: Empty array from `-1`
- T3: Lower boundary `1`
- T4: Upper boundary `8`
- T5: Invalid number out of boundary `9`

The 8 allowed integers can be tested against their corresponding solution arrays.
- T6: Integers `2-7` (1 & 8 being already covered before)

### 6. Automating test cases
Leveraging JUnit 5 we create the following test cases.

```java
class GenerateParenthesesTest {
    @Test
    void test1Zero() {
        List<String> expected = List.of();
        List<String> actual = GenerateParentheses.generateParentheses(0);
        assertEquals(expected, actual);
    }

    @Test
    void test2Negative() {
        List<String> expected = List.of();
        List<String> actual = GenerateParentheses.generateParentheses(-1);
        assertEquals(expected, actual);
    }

    @Test
    void test3LowerBoundary() {
        List<String> expected = List.of("()");
        List<String> actual = GenerateParentheses.generateParentheses(1);
        assertEquals(expected, actual);
    }

    @Test
    void test4UpperBoundary() {
        List<String> expected = List.of("(((((((())))))))", "((((((()()))))))", "...");
        List<String> actual = GenerateParentheses.generateParentheses(8);
        assertIterableEquals(expected, actual);
    }

    @Test
    void test5OutOfBoundary() {
        List<String> expected = List.of();
        List<String> actual = GenerateParentheses.generateParentheses(9);
        assertEquals(expected, actual);
    }

    @Test
    void test6ValidIntegers() {
        List<List<String>> solveArray = List.of(
            List.of("(())", "()()"),
            List.of("((()))", "(()())", "(())()", "()(())", "()()()"),
            List.of("(((())))", "((()()))", "((())())", "((()))()", "(()(()))", "(()()())", "(()())()", "(())(())", "..."),
            List.of("((((()))))", "(((()())))", "(((())()))", "(((()))())", "(((())))()", "((()(())))", "((()()()))", "..."),
            List.of("(((((())))))", "((((()()))))", "((((())())))", "((((()))()))", "((((())))())", "((((()))))()", "..."), 
            List.of("((((((()))))))", "(((((()())))))", "(((((())()))))", "(((((()))())))", "(((((())))()))", "...")
        ;
        for (int i = 2; i <= 7; i++) {
            assertIterableEquals((solveArray.get(i - 2)), GenerateParentheses.generateParentheses(i));
        }
    }
}
```


### 7. Augmenting with creativity and experience
The following changes to the code have happened during the development of the test cases.
1. There was no upper boundary check in place, which was revealed by T5

Leading to this code change:
```java
if (n<=0) return combinations;
```
to
```java
if (n<=0 || n>8) return combinations;
```

## Structural Testing
Using the JaCoCo plugin to measure condition+branch coverage has lead 
to the following discoveries:\
I have 100% condition+branch coverage, not counting the class declaration line, 
thus I will not add any new tests at this stage.

## Mutation Testing
The first iteration of the mutation testing revealed only 1 surviving mutant which 
was related to a changed return value (95% mutation coverage).\
I added a dedicated test for the class initialization (T7) and ran it a 2nd time. 
The mutant persisted on the 2nd run too, but I achieved 100% line coverage now and the mutant 
is in the same category as before (EmptyObjectReturnValsMutator), where I cannot gain anything from it.\
Because of this I concluded the test suite development.