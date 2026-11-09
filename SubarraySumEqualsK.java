
import java.util.HashMap;
import java.util.Map;

/*
 * LeetCode 560 — Subarray Sum Equals K
 *
 * Given an integer array nums and a target k, count the nonempty,
 * contiguous slices of nums that add up to k.
 *
 * Examples
 *   nums = [1, 1, 1], k = 3 -> 1
 *   nums = [1, 2, 3], k = 3 -> 2
 *   nums = [1, 2, 1, 2, 1], k = 3 -> 4
 *   nums = [1, 2, 3, 4, 5], k = 5 -> 2
 *
 * Constraints
 *   1 <= nums.length <= 20,000
 *   -1,000 <= nums[i] <= 1,000
 *   -10,000,000 <= k <= 10,000,000
 */
public class SubarraySumEqualsK {

    public static void main(String[] args) {
        int result = new SubarraySumEqualsK().subarraySum(new int[]{1, 1, 1}, 2);
        System.out.println("Expected 2, got " + result);
    }

    public int subarraySum(int[] nums, int k) {
        // int sum = 0;
        // int count = 0;
        // int left = 0;
        // int right = 1;
        // while (right < nums.length) {
        //     if (right != 1) {
        //         sum = nums[left] + nums[right];
        //     } else {
        //         sum = sum + nums[right];
        //     }
        //     while (right == )

        //     if (sum == k) {
        //         count++;
        //         left++;
        //         right++;
        //         sum = 0;
        //         continue;
        //     }
        //     if (right == nums.length - 1) {
        //         left++;
        //     } else {
        //         right++;
        //     }
        // }
        // if (nums[0] == k) {
        //     return 1;
        // }
        // return count;

        Map<Integer, Integer> frequencies = new HashMap<>();
        frequencies.put(0, 1);

        int sum = 0;
        int count = 0;
//  *   nums = [1, 2, 1, 2, 1], k = 3 -> 4
//  *   nums = [1, 2, 3, 4, 5], k = 5 -> 2
        for (int num : nums) {
            sum += num;
            count += frequencies.getOrDefault(sum - k, 0);
            frequencies.put(sum, frequencies.getOrDefault(sum, 0) + 1);
        }

        return count;
    }
}
