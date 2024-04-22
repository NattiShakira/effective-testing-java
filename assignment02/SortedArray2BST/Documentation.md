## Task 1: Code Coverage

Using the source code to guide testing, 100% line coverage and 100% branch coverage was achieved.
The code was missing a check for the input size limit, correct ordering and uniqueness, but given the nature 
of this assignment I chose to handle this in the 2nd task.

## Task 2: Designing Contracts

- Pre-conditions: There are several that all pertain to input array:
    - The values have to be in ascending order.
    - The length of the array may not be more than 10000.
    - The values must be unique.
    - The array cannot be null.
- Post-conditions: It returns an array in level-order traversal (starting at the root). Null nodes are not displayed 
but represent missing child nodes. Since there are actually 2 methods used here we would need pre- & post-conditions 
for both but the level-order traversal method is not intended to be used alone, which is why I won't consider it.
- Invariants: Since the SortedArrayToBST class does not maintain mutable state, no class invariants are required.

To enforce these contracts:

- We add a type check in the beginning to throw an IllegalArgumentsException for null input arrays.
- Since the correct ordering of the input is integral to the correct execution of the BST distribution, we need to
  check our data. Currently, there is no check in place to filter the input data. We delegate this task to a new 
private method that checks for the array size, ordering and uniqueness.


- For post-conditions, we can add another check that the size has not changed and thus all elements still unique.
- The level-order traversal structure will be hard to test/enforce (explained in task 3).

## Task 3: Testing Contracts

We added 4 new test-cases (tests 3-6) for the pre-conditions and 1 test for the post-conditions. It is hard to test
the level-order structure because binary trees can have different shapes for the same height when you balance them. 
This may lead to test cases that can break easily while factually being correct.

## Task 4: Property-Based Testing

One property that comes to mind here is the correct tree height, which can be deduced from the input size and 
the fact that it needs to be balanced.
