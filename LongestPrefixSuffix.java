/*
 * Longest Non-overlapping Prefix and Suffix (custom practice problem)
 *
 * Given a string s, return the maximum length of an identical prefix
 * and suffix that do not share any character positions. Return 0 if
 * there is no nonempty match.
 *
 * Examples
 *   "abcabc" -> 3
 *   "aaaaa"  -> 2
 *   "abcdef" -> 0
 *
 * Constraints
 *   0 <= s.length() <= 1,000
 *   s contains only lowercase English letters.
 */
public class LongestPrefixSuffix {

    public static void main(String[] args) {
        LongestPrefixSuffix solution = new LongestPrefixSuffix();
        String[] inputs = {"abcabc", "aaaaa", "abcdef", "a", "", "aa", "abxyab"};
        int[] expected = {3, 2, 0, 0, 0, 1, 2};

        for (int i = 0; i < inputs.length; i++) {
            int actual = solution.longestPrefixSuffixLength(inputs[i]);
            if (actual != expected[i]) {
                throw new AssertionError("Input: " + inputs[i]
                        + ", expected " + expected[i] + ", got " + actual);
            }
            System.out.println("Input: \"" + inputs[i] + "\", length: " + actual);
        }
    }

    public int longestPrefixSuffixLength(String s) {
        int n = s.length();

        for (int length = n / 2; length > 0; length--) {
            if (s.regionMatches(0, s, n - length, length)) {
                return length;
            }
        }

        return 0;
    }
}
