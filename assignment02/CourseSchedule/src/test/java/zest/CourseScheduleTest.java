package zest;


import net.jqwik.api.constraints.Negative;
import org.junit.jupiter.api.Test;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.Provide;


import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

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

}
