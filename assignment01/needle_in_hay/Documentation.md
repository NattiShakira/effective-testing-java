## Specification Testing
### 1. Understanding the requirements
#### Business rules
Given 2 strings, one being called the needle, we search for the first occurrence of the needle in the other string.\
There are rules for empty words in both cases.

#### Input
2 strings that could be empty or null.

#### Output
The index of the **first** occurrence or a predefined integer.
- -1 for no match, or at least 1 parameter being null
- 0 when both parameters are empty

### 2. Exploring the program
Tested some different strings and types.

### 3. Identifying partitions
Given the task description the partitions are as follows:
- Input
  - strings with length > 0
  - empty strings
  - null
  - (other invalid types are not in the description and do not need to be covered since the parameter restricts the types to string)

### 4. Analyzing boundaries
The boundary testing should include:
- Normal to empty strings
  - On point: length 1
  - Off point: length 0
- Multiple occurrences of needle
  - On point: once
  - Off point: twice
- Case-sensitive strings
  - On point: with alternating cases
  - Off point: all lowercase
- Strings with whitespaces or special characters will be caught since we compare characters. 
If anything this is a sub-category of the case-sensitive boundary.
- Needle being bigger than the haystack, but this will terminate normally with -1

### 5. Devising test cases
Let's design test cases according to the facts at hand.
- T1: `null` for one & both
- T2: both are `empty`
- T3: 1 is `empty`
- T4: both size `1`
- T5: needle appears twice
- T6: case switching

### 6. Automating test cases
Leveraging JUnit 5 we create the following test cases.
```java
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
}
```


### 7. Augmenting with creativity and experience
The following changes to the code have happened during the development of the test cases.
1. T3 initially returned as faulty with a StringIndexOutOfBoundsException. 
Upon further inspection of the code I found this passage that needed work:
```java
if (haystack.isEmpty() && needle.isEmpty()) return 0;
```
in combination with
```java
for(int i = 0; i < (lenHay-lenNed + 1); i++) {
  if(haystack.charAt(i) == needle.charAt(0)) {
```

I tackled it at the beginning with an additional check for one empty parameter.
```java
if (haystack.isEmpty() || needle.isEmpty()) return -1;
```


## Structural Testing
Using the JaCoCo plugin to measure condition+branch coverage has lead
to the following discovery:\
I have 100% condition+branch coverage, not counting the class declaration line, for which I added another test T7.
```java
@Test
void test7Constructor() {
  NeedleInHay testInstance = new NeedleInHay();
  assertInstanceOf(NeedleInHay.class, testInstance);
}
```

## Mutation Testing
The mutation testing showed no surviving mutants (100% mutation coverage).\
I finish the test suite development at this point .