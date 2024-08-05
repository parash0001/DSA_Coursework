//Question number 5b solution

package org.example;

import java.util.Deque;
import java.util.LinkedList;

public class LongestHike {

    // Function to find the longest consecutive stretch within elevation gain limit k
    public static int longestStretch(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return 0;

        // Deques to maintain the min and max values within the current window
        Deque<Integer> minDeque = new LinkedList<>();
        Deque<Integer> maxDeque = new LinkedList<>();

        int left = 0; // Left pointer of the sliding window
        int longest = 0; // Longest valid stretch

        // Iterate through each element with the right pointer
        for (int right = 0; right < n; right++) {
            // Update minDeque with the current element
            while (!minDeque.isEmpty() && nums[minDeque.peekLast()] >= nums[right]) {
                minDeque.pollLast();
            }
            minDeque.addLast(right);

            // Update maxDeque with the current element
            while (!maxDeque.isEmpty() && nums[maxDeque.peekLast()] <= nums[right]) {
                maxDeque.pollLast();
            }
            maxDeque.addLast(right);

            // Check if the current window is valid
            while (nums[maxDeque.peekFirst()] - nums[minDeque.peekFirst()] > k) {
                // If not valid, shrink the window from the left
                if (minDeque.peekFirst() == left) minDeque.pollFirst();
                if (maxDeque.peekFirst() == left) maxDeque.pollFirst();
                left++;
            }

            // Update the longest stretch
            longest = Math.max(longest, right - left + 1);
        }

        return longest;
    }

    public static void main(String[] args) {
        // Example 1
        int[] nums1 = {4, 2, 1, 4, 3, 4, 5, 8, 15};
        int k1 = 3;
        System.out.println("Example 1: Longest stretch = " + longestStretch(nums1, k1)); // Expected Output: 5

        // Example 2
        int[] nums2 = {1, 3, 6, 7, 9, 2, 5, 8};
        int k2 = 3;
        System.out.println("Example 2: Longest stretch = " + longestStretch(nums2, k2)); // Expected Output: 4
    }
}

// Output:

