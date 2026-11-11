/*
 * LeetCode 322 — Coin Change
 * Priority: High
 * Topic: Dynamic Programming
 * Difficulty: Medium
 * https://leetcode.com/problems/coin-change/
 *
 * Given an integer array coins containing coin denominations and an
 * integer amount representing a total value, find the minimum number
 * of coins whose values sum to amount. Return -1 if no combination
 * can produce that value. Each denomination is available in unlimited
 * quantities.
 *
 * Examples
 *   coins = [1, 2, 5], amount = 11 -> 3
 *     11 = 5 + 5 + 1
 *   coins = [2], amount = 3 -> -1
 *   coins = [1], amount = 0 -> 0
 *
 * Constraints
 *   1 <= coins.length <= 12
 *   1 <= coins[i] <= 2^31 - 1
 *   0 <= amount <= 10,000
 */
public class CoinChange {

    public static void main(String[] args) {
        int result = new CoinChange().coinChange(new int[]{1, 2, 5}, 11);
        System.out.println("Expected 3, got " + result);
    }

    public int coinChange(int[] coins, int amount) {
        // TODO: Implement your solution.
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
