package zest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class TreeNode {
    int val;
    zest.TreeNode left;
    zest.TreeNode right;
    TreeNode(int x) { val = x; }
}

public class SortedArrayToBST {

    public zest.TreeNode sortedArrayToBST(int[] nums) {
        if(nums == null){
            throw new IllegalArgumentException();
        }
        cleanData(nums);

        return constructBSTRecursive(nums, 0, nums.length - 1);
    }

    private void cleanData(int[] suspect){
        if(suspect.length > 10000){
            throw new IllegalArgumentException("The input array is too big!");
        }
        for(int i = 0; i < suspect.length - 1; i++){
            if (suspect[i] > suspect[i + 1]) {
                throw new IllegalArgumentException("The input array is not sorted in ascending order!");
            }
            if (suspect[i] == suspect[i + 1]) {
                throw new IllegalArgumentException("The input array contains duplicate elements!");
            }
        }
    }

    private zest.TreeNode constructBSTRecursive(int[] nums, int left, int right) {
        if (left > right) {
            return null;
        }

        int mid = left + (right - left) / 2;
        zest.TreeNode node = new zest.TreeNode(nums[mid]);
        node.left = constructBSTRecursive(nums, left, mid - 1);
        node.right = constructBSTRecursive(nums, mid + 1, right);
        return node;
    }

    public List<Integer> levelOrderTraversal(zest.TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<zest.TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            zest.TreeNode current = queue.poll();
            result.add(current.val);

            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }

        return result;
    }
}
