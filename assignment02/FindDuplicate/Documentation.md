# FindDuplicate testing documentation by Markus Niemack
# 1. Line coverage
The Line coverage achieved is 96% total. The Branch coverage is only 95%. This is fine though,
as there are 20 branches, meaning only one is missed. The reason for this is that the conditional:
```java
if (occurrences < 2) {
    throw new IllegalStateException("The returned number is not a duplicate.");
}
```
Can never be true, and therefore the line inside cannot be reached. The conditional cannot be true
as the constraints ensure that there must be at least one duplicate, so occurences are always >=2.
This is already checked in the pre-condition checking.
# 2. Contracts

From the README.md file I constructed the following contracts:

Pre-conditions:

- nums must not be null

- nums must have 2 or more elements

- elements in nums must be in range [1, n] (inclusive) where n is nums.length - 1


Post-conditions:

- method returns an integer that is a duplicate number in nums

- returned duplicate must exist and occur at least 2 times in nums

Invariants:

- Array structure must stay the same.
- Array contents must stay the same.

For checking the pre-conditions and the invariants, I wrote a helper function in the java
solution:

```java
private static void checkInvariants(int[] nums) {
        if (nums == null) {
            throw new IllegalStateException("The array must not be null.");
        }
        if (nums.length < 2) {
            throw new IllegalStateException("Array must have at least two elements.");
        }
        for (int num : nums) {
            if (num < 1 || num > nums.length - 1) {
            throw new IllegalStateException("Array elements must be within the range 1 to " + (nums.length - 1));
            }
        }
    }
```
The post-condition checks are done with this snippet:
```java
final int duplicate = hare;
int occurrences = 0;
for (int num : nums) {
    if (num == duplicate) {
        occurrences++;
    }
}
if (occurrences < 2) {
    throw new IllegalStateException("The returned number is not a duplicate.");
}
```


# 3. Testing Contracts

The Invariants in this case only need to be tested at the start and end of the method, as the state
of the Array does not change during execution. The tests after the comment "testing contracts" 
were made to test whether the pre- and postconditions, as well as the invariants hold.

# 4. Property testing

The properties I found were:
- Every result must be a duplicate number
- Array must be unchanged after method call
- The Result must be the same if the method is called with the same array.



Since validity of the array is handled and tested for correctness, I can provide the jqwik test
with a valid Array (correct size and range). Whether there are duplicates or not does not have to
be handled in the arbitrary generator, as any array of size n-1 with elements of range [1,n] (inclusive)
MUST have at least one duplicate.