package zest;


import net.jqwik.api.constraints.Negative;
import org.junit.jupiter.api.Test;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.Provide;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CourseScheduleTest {
    @Test
    void noCourseDueToCycle() {
        int numCourses = 2;
        int[][] prerequisites = new int[][]{{1, 0}, {0, 1}};
        assertFalse(CourseSchedule.canFinish(numCourses, prerequisites));
    }

    @Test
    void twoCoursesNoCycle() {
        int numCourses = 2;
        int[][] prerequisites = new int[][]{{1, 0}};
        assertTrue(CourseSchedule.canFinish(numCourses, prerequisites));
    }


    @Test
    void outOfBoundaryNumberOfCourses() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            CourseSchedule.canFinish(-1, new int[][]{{1, 0}});
        });
        assertTrue(exception1.getMessage().contains("Cannot have a negative number of courses."));

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            CourseSchedule.canFinish(0, new int[][]{{}});
        });
        assertTrue(exception2.getMessage().contains("Cannot have a negative number of courses."));
    }

    @Test
    void selfDependencyInCourses() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CourseSchedule.canFinish(2, new int[][]{{0, 0}});
        });
        assertTrue(exception.getMessage().contains("A course cannot be a prerequisite of itself."));
    }

    @Test
    void courseIndicesOutOfBoundary() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CourseSchedule.canFinish(2, new int[][]{{2, 1}, {1, 3}});
        });
        assertTrue(exception.getMessage().contains("Course indices must be in the valid range"));
    }

    @Test
    void graphSizeAndValidIndexInvariant() {
        int numCourses = 3;
        int[][] prerequisites = {{1, 0}, {2, 1}, {0, 2}};
        assertDoesNotThrow(() -> CourseSchedule.canFinish(numCourses, prerequisites));
    }


    @Test
    void negativeCourseIndexInPrerequisites() {
        assertThrows(IllegalArgumentException.class, () ->
                        CourseSchedule.canFinish(2, new int[][]{{-1, 0}, {1, 2}}),
                "Should throw IllegalArgumentException for negative course indices.");
    }

    @Test
    void courseIndexExceedingNumCourses() {
        assertThrows(IllegalArgumentException.class, () ->
                        CourseSchedule.canFinish(2, new int[][]{{2, 1}, {1, 3}}),
                "Should throw IllegalArgumentException for course indices exceeding the number of courses.");
    }

    @Test
    void emptyPrerequisitesArray() {
        assertTrue(CourseSchedule.canFinish(2, new int[][]{}),
                "An empty prerequisites array should not impede the ability to finish courses.");
    }
    @Property
    void noCoursesProperty(@ForAll @IntRange(min = 1, max = 1) int numCourses) {
        assertTrue(CourseSchedule.canFinish(numCourses, new int[][]{}));
    }

    @Property
    void emptyPrerequisiteArrayProperty(@ForAll @IntRange(min = 1, max = 100) int numCourses) {
        assertTrue(CourseSchedule.canFinish(numCourses, new int[][]{}));
    }
    @Property
    void successfulCourseCompletionProperty(@ForAll @IntRange(min = 10, max = 100000) int numCourses, @ForAll("validCoursePairs") int[][] prerequisites) {
        assertTrue(CourseSchedule.canFinish(numCourses, prerequisites));
    }

    @Property
    void invalidNegativeNumberOfCoursesProperty(@ForAll @Negative int numCourses) {
        assertThrows(IllegalArgumentException.class, () -> CourseSchedule.canFinish(numCourses, new int[][]{{0, 1}}));
    }

    @Property
    void invalidNegativeIndicesInPrerequisitesProperty(@ForAll("negativeIndices") int[][] prerequisites) {
        assertThrows(IllegalArgumentException.class, () -> CourseSchedule.canFinish(3, prerequisites));
    }

    @Property
    void prerequisitesExceedingCourseCountProperty(@ForAll("excessivePrerequisites") int[][] prerequisites) {
        assertThrows(IllegalArgumentException.class, () -> CourseSchedule.canFinish(4, prerequisites));
    }

    @Property
    void selfReferencingPrerequisitesProperty(@ForAll("selfReferencingCourses") int[][] prerequisites) {
        assertThrows(IllegalArgumentException.class, () -> CourseSchedule.canFinish(5, prerequisites));
    }

    @Property
    void handlingEmptyPrerequisitesArraysProperty(@ForAll("emptyPrerequisites") int[][] prerequisites) {
        assertThrows(IllegalArgumentException.class, () -> CourseSchedule.canFinish(0, prerequisites));
    }

    @Provide
    Arbitrary<int[][]> validCoursePairs() {
        return Arbitraries.integers().between(1, 5)
                .flatMap(size -> {
                    Stack<Integer> stack = new Stack<>();
                    for (int i = 0; i < 10; i++) {
                        stack.push(i);
                    }
                    Collections.shuffle(stack);
                    int[][] arrays = new int[size][2];
                    for (int i = 0; i < size; i++) {
                        arrays[i][0] = stack.pop();
                        arrays[i][1] = stack.pop();
                    }
                    return Arbitraries.just(arrays);
                });
    }


    @Provide
    Arbitrary<int[][]> negativeIndices() {
        return Arbitraries.integers().between(1, 5)
                .flatMap(size -> {
                    int[][] result = new int[size][2];
                    for (int i = 0; i < size; i++) {
                        result[i][0] = -Arbitraries.integers().between(1, 5).sample();
                        result[i][1] = -Arbitraries.integers().between(1, 5).sample();
                    }
                    return Arbitraries.just(result);
                });
    }

    @Provide
    Arbitrary<int[][]> excessivePrerequisites() {
        return Arbitraries.integers().between(1, 5)
                .flatMap(size -> {
                    int[][] result = new int[size][2];
                    for (int i = 0; i < size; i++) {
                        result[i][0] = Arbitraries.integers().between(5, 10).sample();
                        result[i][1] = Arbitraries.integers().between(5, 10).sample();
                    }
                    return Arbitraries.just(result);
                });
    }

    @Provide
    Arbitrary<int[][]> selfReferencingCourses() {
        return Arbitraries.integers().between(1, 5)
                .flatMap(size -> {
                    int[][] result = new int[size][2];
                    for (int i = 0; i < size; i++) {
                        int val = Arbitraries.integers().between(0, 4).sample();
                        result[i][0] = val;
                        result[i][1] = val;
                    }
                    return Arbitraries.just(result);
                });
    }

    @Provide
    Arbitrary<int[][]> emptyPrerequisites() {
        return Arbitraries.integers().between(1, 10)
                .map(size -> new int[size][0]);
    }

}
