# Subarray Sum Equals K

Java solution for LeetCode 560 using prefix sums and a frequency map.

## Running

```sh
java SubarraySumEqualsK.java
```

Prints `Expected 2, got 2`. Uses Java's source-file mode, so there is no `javac` step and no `.class` file. Needs Java 11 or newer.

## Sliding window does not work here

The usual first attempt is two pointers: grow the window while the sum is too small, shrink it while the sum is too large. That assumes every number is positive. Here numbers can be negative, so adding an element can make the sum *smaller*, and the window has no valid move left.

## Prefix sums

`prefix[i]` is the sum of `nums[0]` through `nums[i]`, including index `i`.

The name says where the sum starts. A prefix of the array is its opening run of elements, and `prefix[i]` is the total of that run. Every prefix begins at index 0, and that shared starting point is what makes the subtraction below work: two prefixes agree on everything before the later one, so the shared part cancels and only the slice between them survives.

| index       | 0   | 1   | 2   | 3   | 4   |
| ----------- | --- | --- | --- | --- | --- |
| value       | 1   | 2   | 1   | 2   | 1   |
| `prefix[i]` | 1   | 3   | 4   | 6   | 7   |

Each prefix swallows everything up to and including its own index. So the sum of `nums[2..3]`, which is `[1, 2]`, is:

```text
prefix[3] = nums[0] + nums[1] + nums[2] + nums[3] = 1 + 2 + 1 + 2 = 6
prefix[1] = nums[0] + nums[1]                     = 1 + 2     = 3
────────────────────────────────────────────────────────────────────
difference                                                      = 3
```

`nums[0]` and `nums[1]` cancel because they appear in both sums. What remains is `nums[2] + nums[3]`. Since `prefix[1]` covered through index 1, the result starts at index 2.

The general rule, with the `a-1` you subtract being the prefix ending just before the slice:

```text
sum of nums[a..b] = prefix[b] - prefix[a-1]
```

Set that equal to `k` and rearrange:

```text
k = prefix[b] - prefix[a-1]
prefix[a-1] = prefix[b] - k
```

The left side is a total from earlier in the array. The right side you can compute right now. So each step only has to ask: **how many times has `sum - k` appeared before?** That is a map lookup, not a search.

## The frequency map

```java
frequencies.put(0, 1);
```

Before reading any numbers, the running total is `0`, and it has appeared once. This is the base case for slices starting at index 0. Without it there is nothing to subtract for a slice like `nums[0] == k`, and that slice would never be found.

A slice starting at index 0 needs `prefix[-1]` in the formula above — an index that does not exist. Defining that missing value as `0` is exactly what `put(0, 1)` does.

For `nums = [3, 4]`, `k = 3`: reading `3` makes `sum = 3`, and the lookup for `3 - 3 = 0` hits the seed, so `[3]` is counted. Delete the seed and the same lookup finds nothing, giving `0` instead of `1`.

The value is `1` because map values are counts, and exactly one position sits before the array.

Each loop does three things:

```java
sum += num;                                                  // extend the running total
count += frequencies.getOrDefault(sum - k, 0);               // how many earlier totals are k short?
frequencies.put(sum, frequencies.getOrDefault(sum, 0) + 1);  // record this total for later
```

If `sum = 7` and `k = 3`, we need an earlier total of `4`, because `7 - 4 = 3`. Each occurrence of that total is one matching subarray. The `0` in `getOrDefault` handles the common case where it was never seen.

> [!IMPORTANT]
> Count first, then record. Swapping those two lines lets `k = 0` match the current total against itself and count an empty slice.

## Example 1: `[1, 2, 1, 2, 1]`, `k = 3`

Start: `sum = 0`, `count = 0`, map = `{0=1}`.

| Number | New `sum` | Look for `sum - 3` | Frequency found | Total `count` | Remember |
|---|---:|---:|---:|---:|---|
| 1 | 1 | -2 | 0 | 0 | `1=1` |
| 2 | 3 | 0 | 1 | 1 | `3=1` |
| 1 | 4 | 1 | 1 | 2 | `4=1` |
| 2 | 6 | 3 | 1 | 3 | `6=1` |
| 1 | 7 | 4 | 1 | 4 | `7=1` |

The third iteration in detail:

```text
Numbers so far:      [1, 2, 1]
Current total:       4
Earlier total:       1       ← the first element
Difference:          4 - 1 = 3
Matching subarray:      [2, 1]
```

The four matches are the adjacent pairs:

| Subarray | Indices |
| -------- | ------- |
| `[1, 2]` | 0-1     |
| `[2, 1]` | 1-2     |
| `[1, 2]` | 2-3     |
| `[2, 1]` | 3-4     |

## Example 2: `[1, 2, 3, 4, 5]`, `k = 5`

Again, start with map = `{0=1}`.

| Number | New `sum` | Look for `sum - 5` | Frequency found | Total `count` | Matching subarray |
|---|---:|---:|---:|---:|---|
| 1 | 1 | -4 | 0 | 0 | — |
| 2 | 3 | -2 | 0 | 0 | — |
| 3 | 6 | 1 | 1 | 1 | `[2, 3]` |
| 4 | 10 | 5 | 0 | 1 | — |
| 5 | 15 | 10 | 1 | 2 | `[5]` |

In the last iteration the earlier total `10` represents `[1, 2, 3, 4]`. Subtracting it from `15` leaves just `[5]`.

The map holds totals, not subarrays, and a repeated total means several starting positions, so each one is a different subarray. That is why it stores counts rather than a set.

## Complexity

- **Time**: O(n) — one pass, one map lookup per element
- **Space**: O(n)

Comparing every start and end pair is O(n²), too slow for 20,000 elements. Running totals reach at most `20,000 × 1,000 = 20,000,000`, well inside a Java `int`.
