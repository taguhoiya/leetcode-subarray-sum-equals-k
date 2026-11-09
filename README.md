# Subarray Sum Equals K

Java solution for LeetCode 560 using prefix sums and a frequency map.

Run with:

```sh
javac SubarraySumEqualsK.java
java SubarraySumEqualsK
```

The map starts with:

```java
frequencies.put(0, 1);
```

This means: **“Before reading any numbers, we’ve seen a running sum of 0 once.”**

Each loop does three things:

```java
sum += num;
```

Add the current number to the running total.

```java
count += frequencies.getOrDefault(sum - k, 0);
```

Ask: **“How many earlier running totals can I subtract from this total to get `k`?”**

For example, if `sum = 7` and `k = 3`, we need an earlier total of `4`, because `7 - 4 = 3`. Each occurrence represents a matching subarray.

```java
frequencies.put(sum, frequencies.getOrDefault(sum, 0) + 1);
```

Remember the current running total for future iterations. If it already appeared, increase its frequency.

### Example 1: `[1, 2, 1, 2, 1]`, `k = 3`

Initially: `sum = 0`, `count = 0`, map = `{0=1}`.

| Number | New `sum` | Look for `sum - 3` | Frequency found | Total `count` | Remember |
|---|---:|---:|---:|---:|---|
| 1 | 1 | -2 | 0 | 0 | `1=1` |
| 2 | 3 | 0 | 1 | 1 | `3=1` |
| 1 | 4 | 1 | 1 | 2 | `4=1` |
| 2 | 6 | 3 | 1 | 3 | `6=1` |
| 1 | 7 | 4 | 1 | 4 | `7=1` |

Look closely at the third iteration:

```text
Numbers so far:      [1, 2, 1]
Current total:       4
Earlier total:       1       ← the first element
Difference:          4 - 1 = 3
Matching subarray:      [2, 1]
```

The four matches are adjacent pairs at different positions:

```text
[1, 2, 1, 2, 1]
 └──┘
    └──┘
       └──┘
          └──┘
```

### Example 2: `[1, 2, 3, 4, 5]`, `k = 5`

Again, start with map = `{0=1}`.

| Number | New `sum` | Look for `sum - 5` | Frequency found | Total `count` | Matching subarray |
|---|---:|---:|---:|---:|---|
| 1 | 1 | -4 | 0 | 0 | — |
| 2 | 3 | -2 | 0 | 0 | — |
| 3 | 6 | 1 | 1 | 1 | `[2, 3]` |
| 4 | 10 | 5 | 0 | 1 | — |
| 5 | 15 | 10 | 1 | 2 | `[5]` |

For the last iteration, the earlier total `10` represents `[1, 2, 3, 4]`. Subtracting it from `15` leaves just `[5]`.

**The map stores totals, not subarrays. Subtracting those past totals lets us count the subarrays.**
