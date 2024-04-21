package zest;

import java.util.ArrayList;
import java.util.List;

public class CourseSchedule {
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        if (numCourses <= 0) {
            throw new IllegalArgumentException("Cannot have a negative number of courses.");
        }
        for (int[] prerequisite : prerequisites) {
            if (prerequisite.length == 0) {
                throw new IllegalArgumentException("Prerequisite pairs must not be empty.");
            }
            if (prerequisite[0] == prerequisite[1]) {
                throw new IllegalArgumentException("A course cannot be a prerequisite of itself.");
            }
            if (prerequisite[0] < 0 || prerequisite[0] >= numCourses || prerequisite[1] < 0 || prerequisite[1] >= numCourses) {
                throw new IllegalArgumentException("Course indices must be in the valid range");
            }
        }


        // Create a graph from prerequisites
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        if (graph.size() != numCourses) {
            throw new RuntimeException("Graph size should be exactly equal to the number of courses.");
        }

        // this checks that course b must finish before course a
        for (int[] prerequisite : prerequisites) {
            graph.get(prerequisite[1]).add(prerequisite[0]);
        }
        for (List<Integer> courseList : graph) {
            for (int course : courseList) {
                if (course < 0 || course >= numCourses) {
                    throw new RuntimeException("Graph contains an invalid course index");
                }
            }
        }

        boolean[] visited = new boolean[numCourses];
        boolean[] onPath = new boolean[numCourses];

        for (int i = 0; i < numCourses; i++) {
            if (!visited[i] && hasCycle(graph, i, visited, onPath)) {
                return false; // Cycle detected
            }
        }
        for (int i = 0; i < numCourses; i++) {
            if (!visited[i]) throw new RuntimeException("Node visitation status is incorrectly marked as not visited.");
            if (onPath[i]) throw new RuntimeException("Node path status is incorrectly marked as still on path.");
        }
        return true; // No cycle detected
    }

    private static boolean hasCycle(List<List<Integer>> graph, int current, boolean[] visited, boolean[] onPath) {
        if (onPath[current]) return true; // Cycle detected
        if (visited[current]) return false; // Already visited

        visited[current] = true;
        onPath[current] = true;

        for (int neighbor : graph.get(current)) {
            if (hasCycle(graph, neighbor, visited, onPath)) {
                return true;
            }
        }
        for (int neighbor : graph.get(current)) {
            if (hasCycle(graph, neighbor, visited, onPath)) {
                return true;  // Cycle detected, return immediately
            }
            if (!visited[current]) {
                throw new RuntimeException("Node should be marked as visited but is not.");
            }
            if (!onPath[current]) {
                throw new RuntimeException("Node should be marked as being on the path but is not.");
            }
        }

        onPath[current] = false; // Backtrack
        return false;
    }
}
