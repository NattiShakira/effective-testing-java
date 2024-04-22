package zest;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class SortedArrayToBSTTest {

    SortedArrayToBST sAtBST = new SortedArrayToBST();

    @Test
    void test1balancedInput(){
        int[] numbers = {1,2,3};
        TreeNode result = sAtBST.sortedArrayToBST(numbers);

        List<Integer> fancy = sAtBST.levelOrderTraversal(result);
        List<Integer> expected = Arrays.asList(2,1,3);

        assertEquals(expected, fancy);
    }
    @Test
    void test2emptyInput(){
        int[] nums = {};
        TreeNode result = sAtBST.sortedArrayToBST(nums);

        List<Integer> fancy = sAtBST.levelOrderTraversal(result);
        List<Integer> expected = List.of();

        assertEquals(expected, fancy);
    }
    @Test
    void test3inputTooBig(){
        int[] nums = new int[10001];
        for(int i = 0; i < 10001; i++){
            nums[i] = i;
        }

        assertThrows(IllegalArgumentException.class, () -> {
            sAtBST.sortedArrayToBST(nums);
        });
    }
    @Test
    void test4duplicates(){
        int[] nums = {2, 2};

        assertThrows(IllegalArgumentException.class, () -> {
            sAtBST.sortedArrayToBST(nums);
        });
    }
    @Test
    void test5unordered(){
        int[] nums = {2, 1, 4};

        assertThrows(IllegalArgumentException.class, () -> {
            sAtBST.sortedArrayToBST(nums);
        });
    }
    @Test
    void test6nullArray(){
        int[] nums = null;

        assertThrows(IllegalArgumentException.class, () -> {
            sAtBST.sortedArrayToBST(nums);
        });
    }
    @Test
    void test7sameSizeAfter(){
        int[] nums = {2,4,6,7,8,9,13};
        TreeNode result = sAtBST.sortedArrayToBST(nums);

        List<Integer> fancy = sAtBST.levelOrderTraversal(result);

        assertEquals(nums.length, fancy.size());
    }

    @Provide
    Arbitrary<List<Integer>> sortedUniqueLists() {
        return Arbitraries.integers().between(Integer.MIN_VALUE, Integer.MAX_VALUE).list().ofMinSize(0).ofMaxSize(10000).uniqueElements().map(this::sortList);
    }
    private List<Integer> sortList(List<Integer> input) {
        input.sort(Comparator.naturalOrder());
        return input;
    }

    @Property
    void test8correctHeight(@ForAll("sortedUniqueLists") List<Integer> list) {
        for(int size = 0; size <= 10000; size += 1000) {
            SortedArrayToBST tree = new SortedArrayToBST();
            int[] array = list.stream().mapToInt(i -> i).toArray();

            TreeNode root = tree.sortedArrayToBST(array);
            List<Integer> traversalResult = tree.levelOrderTraversal(root);

            for (Integer number : list) {
                assertTrue(traversalResult.contains(number));
            }

            assertTrue(isBalanced(root));
        }
    }

    private boolean isBalanced(zest.TreeNode node) {
        if (node == null) {
            return true;
        }

        int leftHeight = height(node.left);
        int rightHeight = height(node.right);
        return Math.abs(leftHeight - rightHeight) <= 1 && isBalanced(node.left) && isBalanced(node.right);
    }
    private int height(zest.TreeNode node) {
        if (node == null) {
            return 0;
        }
        return Math.max(height(node.left), height(node.right)) + 1;
    }
}
