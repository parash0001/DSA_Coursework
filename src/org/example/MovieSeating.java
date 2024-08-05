//Question number 2b solution

package org.example;

public class MovieSeating {

    /**
     * Determines if there are two friends who can sit together based on seating distance and movie preference.
     *
     * @param nums Array representing the seating chart, where nums[i] is the seat number at row i.
     * @param indexDiff Maximum allowed difference in indices between two seats.
     * @param valueDiff Maximum allowed difference in seat numbers for movie preferences.
     * @return true if there are two seats satisfying both conditions, false otherwise.
     */
    public static boolean canSitTogether(int[] nums, int indexDiff, int valueDiff) {
        // Iterate through each seat
        for (int i = 0; i < nums.length; i++) {
            // Check subsequent seats within the allowed indexDiff
            for (int j = i + 1; j <= i + indexDiff && j < nums.length; j++) {
                // Check if the absolute difference in seat numbers is within the valueDiff
                if (Math.abs(nums[i] - nums[j]) <= valueDiff) {
                    return true; // Found a valid pair
                }
            }
        }
        return false; // No valid pair found
    }

    public static void main(String[] args) {
        // Test case 1: Expected output: true
        int[] nums1 = {2, 3, 5, 4, 9};
        int indexDiff1 = 2;
        int valueDiff1 = 1;
        System.out.println(canSitTogether(nums1, indexDiff1, valueDiff1));

        // Test case 2: Expected output: false
        int[] nums2 = {1, 5, 9, 13};
        int indexDiff2 = 2;
        int valueDiff2 = 3;
        System.out.println(canSitTogether(nums2, indexDiff2, valueDiff2));

        // Test case 3: Expected output: true
        int[] nums3 = {4, 6, 8, 10};
        int indexDiff3 = 1;
        int valueDiff3 = 2;
        System.out.println(canSitTogether(nums3, indexDiff3, valueDiff3));
    }
}

// Explanation:
// - For nums1 = [2, 3, 5, 4, 9], indexDiff = 2, valueDiff = 1:
//   Pairs satisfying the condition: (0, 1) and (3, 4) -> Result: true
// - For nums2 = [1, 5, 9, 13], indexDiff = 2, valueDiff = 3:
//   No pairs satisfy the condition -> Result: false
// - For nums3 = [4, 6, 8, 10], indexDiff = 1, valueDiff = 2:
//   Pairs satisfying the condition: (0, 1) and (1, 2) -> Result: true
