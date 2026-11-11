# Big-O and Pattern Cheat Sheet

## What is Big-O (O(…))?

Big-O describes how fast an algorithm grows as input size `n` increases.

👉 Think: "If input gets bigger, how much slower does my code get?"

It ignores constants and focuses on the **growth trend**.

## Common time complexities

### O(1) — Constant

Always same speed, no matter input size.

```java
int x = arr[0];
```

👉 Accessing an array index = instant.

### O(log n) — Logarithmic

You cut the problem in half each time.

```java
// Binary Search
```

👉 Super efficient. Used in: **binary search, trees**.

### O(n) — Linear

You go through all elements once.

```java
for (int i = 0; i < n; i++) {
    // do something
}
```

👉 If `n` doubles → time doubles.

### O(n log n) — Linearithmic

Slightly worse than linear. Common in merge sort, heap sort, efficient sorting.

### O(n²) — Quadratic

Nested loops.

```java
for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
        // do something
    }
}
```

👉 If `n` doubles → time becomes **4x slower** 💀

### O(2ⁿ) — Exponential

Explodes fast. Seen in brute force recursion, subsets.

### O(n!) — Factorial (worst 💀💀)

All permutations.

## How to think (VERY IMPORTANT)

When you see code, ask:

1. **Loop?** → O(n)
2. **Nested loop?** → O(n²)
3. **Halving each time?** → O(log n)
4. **Loop + binary search / divide?** → O(n log n)

## Quick intuition table

| Input size  | O(1) | O(log n) | O(n) | O(n²) |
| ----------- | ---- | -------- | ---- | ----- |
| 10          | fast | fast     | fast | ok    |
| 1,000       | fast | fast     | ok   | slow  |
| 1,000,000   | fast | ok       | slow | 💀    |

## LeetCode mindset

- Aim for **O(n)** or **O(log n)** if possible
- Avoid **O(n²)** unless `n` is small (< 1000)
- If brute force is O(n²), think: HashMap? Two pointers? Sliding window?

---

## 1. HashMap

**Problem: Two Sum (unsorted)** — `nums = [2,7,11,15]`, `target = 9` → `[0,1]`

🔑 Idea: "Have I seen the number I need?"

```java
Map<Integer, Integer> map = new HashMap<>();

for (int i = 0; i < nums.length; i++) {
    int need = target - nums[i];

    if (map.containsKey(need)) {
        return new int[]{map.get(need), i};
    }

    map.put(nums[i], i);
}
```

O(n).

🧠 When: complement lookup, duplicates / frequency.

---

## 2. Two Pointers

**Problem: Container With Most Water** — `[1,8,6,2,5,4,8,3,7]` → `49`

🔑 Idea: Start wide → move the shorter side inward.

```java
int l = 0, r = height.length - 1;
int max = 0;

while (l < r) {
    int area = Math.min(height[l], height[r]) * (r - l);
    max = Math.max(max, area);

    if (height[l] < height[r]) l++;
    else r--;
}
```

O(n).

🧠 When: pair optimization, shrinking the search space. The container problem needs **non-negative heights**; two pointers on a sorted array is fine with negatives.

---

## 3. Sliding Window

**Problem: Max Sum Subarray (size k)** — `[2,1,5,1,3,2]`, `k=3` → `9`

🔑 Idea: Add new → remove old (don't recompute).

```java
int sum = 0, max = Integer.MIN_VALUE;

for (int i = 0; i < nums.length; i++) {
    sum += nums[i];

    if (i >= k - 1) {
        max = Math.max(max, sum);
        sum -= nums[i - (k - 1)];
    }
}
```

O(n).

🧠 When: subarray / substring, "max / longest". Needs **non-negative numbers** when the window itself grows and shrinks to chase a condition.

---

## 4. Prefix Sum

**Problem: Subarray Sum Equals K** — `[1,2,1,2,1]`, `k=3` → `4`

🔑 Idea: "Have I seen the running total I need?"

```java
Map<Integer, Integer> freq = new HashMap<>();
freq.put(0, 1);   // before reading anything, total 0 was seen once
int sum = 0, count = 0;

for (int num : nums) {
    sum += num;
    count += freq.getOrDefault(sum - k, 0);
    freq.put(sum, freq.getOrDefault(sum, 0) + 1);
}
```

O(n).

🧠 When:

- "count subarrays with sum = k"
- ✅ works with **negative numbers** (unlike sliding window)
- rule: `sum of nums[a..b] = prefix[b] - prefix[a-1]`
- **broader family**: range sum queries, 2D prefix sums, difference arrays → no map needed
- count first, then record ← the order matters
- same trick for "longest subarray with sum k" → store the first index instead of a count

See [README.md](README.md) for the full walkthrough of this one.

---

## One-liner memory hack

- HashMap → **remember past values**
- Two pointers → **shrink space**
- Sliding window → **reuse range**
- Prefix sum → **remember past aggregates (running totals)**
