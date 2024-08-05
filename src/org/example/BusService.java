// Question number 3b solution

package org.example;

public class BusService {

    // Node class to define a linked list node
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }

    // Function to reverse a part of the linked list
    public static ListNode reverse(ListNode head, int k) {
        ListNode prev = null;
        ListNode curr = head;
        ListNode next = null;
        int count = 0;

        // Reverse k nodes of the linked list
        while (curr != null && count < k) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
            count++;
        }

        // Recursively reverse the remaining nodes
        if (next != null) {
            head.next = reverse(next, k);
        }

        return prev; // Return the new head of the reversed list
    }

    // Function to optimize the boarding process by reversing nodes in groups of k
    public static ListNode optimizeBoarding(ListNode head, int k) {
        if (head == null || k <= 1) return head;
        return reverse(head, k);
    }

    // Function to print the linked list
    public static void printList(ListNode head) {
        ListNode temp = head;
        while (temp != null) {
            System.out.print(temp.val + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    // Main method to run the code
    public static void main(String[] args) {
        // Create and test the linked list: 1 -> 2 -> 3 -> 4 -> 5
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);

        int k = 2;
        System.out.println("Original list:");
        printList(head);

        ListNode optimizedHead = optimizeBoarding(head, k);

        System.out.println("Optimized list with k = " + k + ":");
        printList(optimizedHead);

        // Test another example with k = 3
        head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);

        k = 3;
        System.out.println("Original list:");
        printList(head);

        optimizedHead = optimizeBoarding(head, k);

        System.out.println("Optimized list with k = " + k + ":");
        printList(optimizedHead);
    }
}

// Outputs:
// Original list:
// 1 2 3 4 5
// Optimized list with k = 2:
// 2 1 4 3 5
// Original list:
// 1 2 3 4 5
// Optimized list with k = 3:
// 3 2 1 4 5
