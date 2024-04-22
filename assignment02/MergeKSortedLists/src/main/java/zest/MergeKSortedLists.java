package zest;

import java.util.PriorityQueue;

class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

public class MergeKSortedLists {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        if(lists.getClass().getComponentType() != ListNode.class){
            throw new IllegalArgumentException();
        }

        int inputAmount = checkData(lists);

        PriorityQueue<ListNode> queue = new PriorityQueue<>(lists.length, (a, b) -> a.val - b.val);

        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        for (ListNode node : lists) {
            if (node != null) {
                queue.add(node);
            }
        }

        while (!queue.isEmpty()) {
            tail.next = queue.poll();
            tail = tail.next;

            if (tail.next != null) {
                queue.add(tail.next);
            }
        }

        checkReturnData(inputAmount, dummy.next);

        return dummy.next;
    }

    private int checkData(ListNode[] lists) {
        int totalCount = 0;

        for(ListNode node: lists){
            ListNode currentNode = node;
            Integer lastValue = null;
            while(currentNode != null){
                if (currentNode.val < -10000 || currentNode.val > 10000){
                    throw new IllegalArgumentException("One of the values was out of the allowed range");
                }
                if (lastValue != null && currentNode.val < lastValue){
                    throw new IllegalArgumentException("One of the linked-lists is not in ascending order");
                }
                lastValue = currentNode.val;
                totalCount++;
                if (totalCount > 10000){
                    throw new IllegalArgumentException("The amount of nodes is larger than allowed");
                }
                currentNode = currentNode.next;
            }
        }

        return totalCount;
    }

    private void checkReturnData(int length, ListNode finalList){
        int amount = 0;
        ListNode currentNode = finalList;
        Integer lastVal = null;

        while(currentNode != null){
            if(lastVal != null && currentNode.val < lastVal){
                throw new IllegalArgumentException("The ordering of the returned list was wrong!");
            }
            lastVal = currentNode.val;
            amount++;
            currentNode = currentNode.next;
        }

        if (length != amount){
            throw new IllegalArgumentException("The returned list is not a single one");
        }
    }
}
