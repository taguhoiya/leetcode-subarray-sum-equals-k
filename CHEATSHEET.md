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

🧠 When: subarray / substring, "max / longest". Fixed-size windows work with **negative numbers** too. Growing / shrinking a window based on its sum needs **non-negative numbers** so the sum changes predictably.

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
- ✅ works with **negative numbers** (unlike the usual sum-based variable sliding window)
- rule: `sum of nums[a..b] = prefix[b] - prefix[a-1]`
- **broader family**: range sum queries, 2D prefix sums, difference arrays → no map needed
- count first, then record ← the order matters
- same trick for "longest subarray with sum k" → store the first index instead of a count

See [README.md](README.md) for the full walkthrough of this one.

---

## 5. Dynamic Programming (Bottom-Up)

**Problem: Coin Change** — `coins = [1,2,5]`, `amount = 11` → `3`

🔑 Idea: Solve smaller amounts first → reuse their best answers.

`dp[value]` = minimum coins needed to make `value`; `-1` means impossible.

```java
int[] dp = new int[amount + 1];
Arrays.fill(dp, -1);
dp[0] = 0;   // zero coins make amount zero

for (int value = 1; value <= amount; value++) {
    int best = Integer.MAX_VALUE;

    for (int coin : coins) {
        int remaining = value - coin;
        if (remaining >= 0 && dp[remaining] != -1) {
            best = Math.min(best, dp[remaining] + 1);
        }
    }

    dp[value] = best == Integer.MAX_VALUE ? -1 : best;
}

return dp[amount];
```

Time **O(amount × coins.length)**, space **O(amount)**. Uses `java.util.Arrays`.

🧠 When: repeated subproblems, "minimum / maximum / number of ways" with a reusable state.

- Define the **state**, **base case**, and **transition** before writing loops.
- Here: `dp[value] = min(dp[value - coin] + 1)` over reachable remainders.
- Coins must be **positive**, so each remainder is smaller and already computed.
- Coins can be reused without limit; each amount considers every denomination again.
- Skip impossible states before adding `1`; adding to `Integer.MAX_VALUE` overflows.
- Taking the largest coin first can fail: `[1,3,4]`, amount `6` → `3 + 3` uses `2` coins, but `4 + 1 + 1` uses `3`.
- For **counting combinations** instead of minimizing coins, the recurrence and loop order need separate care.

See [CoinChange.java](CoinChange.java) for the same recurrence using a map.

---

## 6. Dijkstra (Graph + Min-Heap)

**Problem: Network Delay Time** — `times = [[2,1,1],[2,3,1],[3,4,1]]`, `n = 4`, `k = 2` → `2`

🔑 Idea: Explore the earliest known arrival first → improve routes to its neighbors.

Nodes are numbered `1..n`. An edge `[u,v,w]` goes from `u` to `v` with travel time `w`.

```java
List<List<int[]>> graph = new ArrayList<>();
for (int i = 0; i <= n; i++) graph.add(new ArrayList<>());
for (int[] edge : times) {
    graph.get(edge[0]).add(new int[]{edge[1], edge[2]});
}

int[] arrival = new int[n + 1];
Arrays.fill(arrival, Integer.MAX_VALUE);
arrival[k] = 0;

PriorityQueue<int[]> queue = new PriorityQueue<>(
    Comparator.comparingInt((int[] pair) -> pair[0])
);
queue.offer(new int[]{0, k});   // [arrival time, node]

while (!queue.isEmpty()) {
    int[] current = queue.poll();
    int time = current[0], node = current[1];
    if (time > arrival[node]) continue;   // skip an outdated route

    for (int[] edge : graph.get(node)) {
        int next = edge[0], newTime = time + edge[1];
        if (newTime < arrival[next]) {
            arrival[next] = newTime;
            queue.offer(new int[]{newTime, next});
        }
    }
}

int answer = 0;
for (int node = 1; node <= n; node++) {
    if (arrival[node] == Integer.MAX_VALUE) return -1;
    answer = Math.max(answer, arrival[node]);
}
return answer;
```

Time **O(V + E log(E + 1))**, space **O(V + E)** for this heap implementation, where `V = n` and `E = times.length`. The heap can hold multiple entries for a node. Uses classes from `java.util`.

🧠 When: shortest paths from one source with **non-negative edge weights**.

- Ordinary BFS finds shortest paths when every edge has the same positive cost; differing non-negative costs call for Dijkstra.
- Improving a distance is called **relaxation**: `arrival[next] = min(arrival[next], time + weight)`.
- A route can improve after it is first discovered. Queue the better route and skip old entries when popped.
- Keep edge direction: `[u,v,w]` does **not** imply a route from `v` to `u`.
- Network Delay Time asks when **all** nodes are reached, so return the **maximum of the shortest arrival times**, or `-1` if any node is unreachable.
- Negative weights break Dijkstra's guarantee. The `int` distances above fit this problem's bounds; use `long` when path sums may overflow.

See [NetworkDelayTime.java](NetworkDelayTime.java) for the full solution.

---

## One-liner memory hack

- HashMap → **remember past values**
- Two pointers → **shrink space**
- Sliding window → **reuse range**
- Prefix sum → **remember past aggregates (running totals)**
- Dynamic programming → **reuse answers to smaller states**
- Dijkstra → **expand the earliest arrival, improve neighboring routes**
