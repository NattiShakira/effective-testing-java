## Task 1: Code Coverage

Using the source code to guide testing, 100% line coverage and 100% branch coverage was achieved. 
I chose a scenario for test1 where all possible branches are visited.
The code was missing a check for the input size limit, but given the nature of this assignment I chose to 
incorporate this into the 2nd task. 

## Task 2: Designing Contracts

- Pre-conditions: There are several that all pertain to the linked-list(LL) type.
  - The LLs have to be in ascending order.
  - The values of the LL nodes have to be in the range of [-10000, 10000].
  - There may not be more than at most 10000 nodes in total, across all LLs.
  - It only accepts a list of LLs (this list can be empty, the max size is implicitly given by the node number).
- Post-conditions: It returns a single, sorted LL in ascending order.
- Invariants: Since the MergeKSortedLists class does not maintain mutable state, no class invariants are required.

To enforce these contracts:

- We add a type check in the beginning to throw an IllegalArgumentsException when triggered.
- Since the correct ordering of the LLs is integral to the expected execution of the PriorityQueue, we need to 
check our data. Currently, there is no check in place to filter the input data that is in the LLs. We delegate 
this task to a new private method that checks for the node amount, ordering and value range.


- For post-conditions, we add another method that checks the final ordering and that it is only one linked-list. 
We already know that we worked with clean data and do not need to check this again. We can leverage the fact 
that we counted the amount in the pre-condition, using it to check for the length of the returned list.

## Task 3: Testing Contracts

We added 3 new test-cases for the pre-conditions and 1 semi-test for the post-conditions. It is hard to test 
the post-condition since you would need to inject something into the method before returning (the method would 
be flawed if you could get to these checks and trigger them from outside).

## Task 4: Property-Based Testing

Given the number of tests run by jqwik and the size constraints of this task it was not feasible to test "all" 
properties that came to mind (namely if all input elements are present in the output one). 
But I did include an extensive case for ordering (null and empty input do not make sense to be tested with properties).
