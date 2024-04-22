package zest;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MergeKSortedListsTest {

    MergeKSortedLists mKSL = new MergeKSortedLists();

    @Test
    void test0constructor(){
        ListNode node = new ListNode();

        // verifying that the initial state is as expected
        assertEquals(0, node.val);
        assertNull(node.next);
    }
    @Test
    void test1correctOrder(){
        ListNode[] lists = new ListNode[2];
        lists[0] = new ListNode(1, new ListNode(2));
        lists[1] = new ListNode(3);

        ListNode result = mKSL.mergeKLists(lists);

        int[] expectedValues = {1,2,3};
        for (int expectedValue : expectedValues) {
            assertEquals(expectedValue, result.val);
            result = result.next;
        }
    }
    @Test
    void test2listNotInOrder(){
        ListNode[] lists = new ListNode[3];
        lists[0] = new ListNode(1, new ListNode(2));
        lists[1] = new ListNode(3);
        lists[2] = new ListNode(3, new ListNode(2));

        assertThrows(IllegalArgumentException.class, () -> {
            mKSL.mergeKLists(lists);
        });
    }
    @Test
    void test3listWithInvalidValue(){
        ListNode[] lists = new ListNode[2];
        lists[0] = new ListNode(1, new ListNode(10001));
        lists[1] = new ListNode(3);

        assertThrows(IllegalArgumentException.class, () -> {
            mKSL.mergeKLists(lists);
        });
    }
    @Test
    void test4listTooLarge(){
        ListNode[] lists = new ListNode[10001];
        for(int i = 0; i < 10001; i++){
            lists[i] = new ListNode(2);
        }

        assertThrows(IllegalArgumentException.class, () -> {
            mKSL.mergeKLists(lists);
        });
    }
    @Test
    void test5correctAmount(){
        ListNode[] lists = new ListNode[2];
        lists[0] = new ListNode(1, new ListNode(2));
        lists[1] = new ListNode(3);

        ListNode result = mKSL.mergeKLists(lists);
        int amount = 0;
        ListNode showMe = result;
        while(showMe != null){
            amount++;
            showMe = showMe.next;
        }

        assertEquals(3, amount);
    }


    @Property
    boolean test6alwaysSorted(@ForAll("sortedListNodeArrays") ListNode[] lists) {
        MergeKSortedLists mergeKSortedLists = new MergeKSortedLists();
        ListNode result = mergeKSortedLists.mergeKLists(lists);

        return isSorted(result);
    }

    @Provide
    Arbitrary<ListNode[]> sortedListNodeArrays() {
        Arbitrary<Integer> values = Arbitraries.integers().between(-10000, 10000);

        Arbitrary<List<List<Integer>>> listsOfIntegers = values.list().ofMinSize(0).ofMaxSize(1000)
            .map(list -> {
                Collections.sort(list);
                return list;
            }).list().ofMinSize(1).ofMaxSize(10);

        return listsOfIntegers.map(lists -> lists.stream().map(this::createLinkedListFromSortedValues)
            .toArray(ListNode[]::new));
    }

    private ListNode createLinkedListFromSortedValues(List<Integer> sortedValues) {
        ListNode dummyHead = new ListNode(0);
        ListNode current = dummyHead;
        for (int val : sortedValues) {
            current.next = new ListNode(val);
            current = current.next;
        }

        return dummyHead.next;
    }

    private boolean isSorted(ListNode node) {
        if (node == null || node.next == null) return true;

        int currentVal = node.val;
        node = node.next;

        while (node != null) {
            if (currentVal > node.val) return false;
            currentVal = node.val;
            node = node.next;
        }

        return true;
    }
}
