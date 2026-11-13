# Longest Non-overlapping Prefix and Suffix

Custom LeetCode-style practice problem.

## Problem statement

Given a string `s`, return the length of its longest identical prefix and suffix that do not overlap. Return `0` if there is no nonempty match.

A prefix starts at index `0`. A suffix ends at the last index. The matching parts cannot share character positions, but they can touch or have a gap between them.

## Examples

| Input | Output | Explanation |
| ----- | ------ | ----------- |
| `"abcabc"` | `3` | The prefix and suffix are both `"abc"`. |
| `"aaaaa"` | `2` | Both parts can be `"aa"`; parts of length `3` would overlap. |
| `"abxyab"` | `2` | Both parts are `"ab"`, with `"xy"` between them. |
| `"abcdef"` | `0` | No nonempty prefix matches a suffix. |
| `"a"` | `0` | Two nonempty parts would overlap. |
| `""` | `0` | The string has no nonempty parts. |

## Constraints

- `0 <= s.length() <= 1,000`
- `s` contains only lowercase English letters.

## Java solution

See [LongestPrefixSuffix.java](LongestPrefixSuffix.java).

Try candidate lengths from `s.length() / 2` down to `1`. Starting at half the string length guarantees that the two parts cannot overlap. The first match is the longest.

```java
if (s.regionMatches(0, s, n - length, length)) {
    return length;
}
```

`regionMatches` compares `length` characters starting at index `0` with `length` characters starting at index `n - length`. It returns a boolean and avoids creating temporary substrings. The loop variable `length` is the number of matching characters to return.

- Time: O(n²), because there are O(n) candidate lengths and each comparison takes up to O(n) time.
- Extra space: O(1).

## Running

With Java 11 or newer:

```sh
java LongestPrefixSuffix.java
```

The runner checks the examples and throws an `AssertionError` if any result differs from the expected length.

## Follow-up

Can you solve this in O(n) time using the KMP prefix function?
