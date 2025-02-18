# Task 1: Code Coverage

The class is designed to return a boolean value. It returns true if all the courses are completed given the
prerequisites.
This involves building a graph and detecting cycles. To reach 100% line coverage:

- Detection of cycle: The test 'noCourseDueToCycle' uses a scenario where two courses form a direct cycle ({1, 0}, {0,
  1}), testing the graph's capability to detect the impossibility of completing the courses since each course has as
  prerequisite the other one.
- Course completion with no cycles: The test 'twoCoursesNoCycle' provides prerequisites that do not form a cycle ({1,
  0}).
  This test confirms that the method can correctly identify a valid course completion path.
  No cycle is created since the only requirement is that course 1 has to be done after 0.

# Task 2: Designing Contracts

Following the initial setup, contracts were implemented to ensure robust method execution. They are enforced with
pre-conditions, that validate input
requirements, post-conditions, that verify the correctness of the method's outputs, and invariants, that ensure state
consistency
throughout the method’s lifecycle.
The following design contracts are incorporated into the source code:

- The total number of courses must be a positive integer. This case has been taken into consideration with a
  pre-condition that throws an exception when numCourses <= 0.
- No course can require itself as a prerequisite. This case was fulfilled by the pre-condition that throws an exception
  if (prerequisite[0] == prerequisite[1]) .
- Each prerequisite pair must refer to valid courses, meaning 0 ≤ a, b < numCourses. A pre-condition checks each index
  in the prerequisite pairs to ensure they are within the valid range.
- The method must return 'true' if all courses can be completed without encountering any cycles,
  otherwise 'false'. This is implicitly verified through the cycle detection logic within the hasCycle method.
- The size of the graph list must always match 'numCourses'. This is fulfilled in the invariant that throws an exception
  when the size of the graph it's different from numCourses.
- Every list in the graph should only contain valid course indices. An invariant throws a 'RuntimeException' if any list
  contains an invalid index, meaning (course < 0 || course >= numCourses).
- The 'visited' and 'onPath' arrays must correctly reflect the current state of each node's visitation and path inclusion
  status.
  An invariant checks the two conditions. The first one if (!visited[i]) throws this exception: "Node visitation status is
  incorrectly marked as not visited."
  The second one if (onPath[i]) throws this exception: "Node path status is incorrectly marked as still on
  path.".
- When checking for cycles recursively through neighbors, the node currently being checked should always be marked as
  being visited and as being on the current path.
  An invariant checks the two conditions. the first one if (!visited[current]) throws the exception: "Node should be marked
  as visited but is not."
  The second one if (!onPath[current]), throws the exception: "Node should be marked as being on the path but isn't."

# Task 3: Testing Contracts

To ensure contract enforcement, a suite of tests was developed:

*Cycle Detection*

- 'outOfBoundaryNumberOfCourses' combines tests for negative and zero numbers of courses to confirm an
  IllegalArgumentException is thrown.
- 'selfDependencyInCourses' checks if an exception is thrown when a course is its own prerequisite.
- 'courseIndicesOutOfBoundary' verifies that course indices outside the allowed range throw an IllegalArgumentException.

*Graph Structure and Integrity*

- 'graphSizeAndValidIndexInvariant' checks that the internal graph structure reflects the number of courses accurately
  and that all course indices in the graph are valid, ensuring the invariants are maintained throughout the method's
  execution.

*Edge Cases in Prerequisites*

- 'emptyPrerequisitesArray' confirms that the method can handle cases where no prerequisites are specified.

*Additional Boundary and Error Handling Tests*

- 'negativeCourseIndexInPrerequisites' this test checks the method's ability to handle prerequisites with negative
  course indices. Negative indices are invalid in the context of course numbering
- 'courseIndexExceedingNumCourses' verifies that the system correctly handles cases where the course indices in
  prerequisites exceed the specified number of courses (numCourses). Such indices are out of bounds.

The line coverage reaches 87% due to the invariants implemented in the code. Some conditions related to these
invariants are not feasible to trigger without modifying the internal logic or encountering severe logical errors that
current tests safeguard against. This limitation stems from the robustness and the defensive programming style used in
the class implementation.

# Task 4: Property-Based Testing
The following properties were then incorporated and finalized with Jqwik:

- 'noCoursesProperty' validates that the method returns true when there is exactly one course with no prerequisites. 
- 'emptyPrerequisiteArrayProperty' confirms that the method returns true for any number of courses provided there are no prerequisites. 
- 'successfulCourseCompletionProperty' validates that courses can be completed successfully given valid prerequisites, testing with a broad range of courses.
- 'invalidNegativeNumberOfCoursesProperty' checks for proper handling of negative numbers of courses, expecting an exception to enforce correct input validation.
- 'prerequisitesExceedingCourseCountProperty' tests that providing prerequisites with course indices that exceed the number of available courses results in an exception.
- 'selfReferencingPrerequisitesProperty' verifies that prerequisites in which a course is its own prerequisite lead to an exception, as this condition should logically prevent course completion.
- 'handlingEmptyPrerequisitesArraysProperty' examines the behavior when prerequisites are empty arrays for zero courses, expecting an exception due to invalid course count.

The jqwik output can be seen in the log.txt file. The line coverage is the same since the same invariants are kept.