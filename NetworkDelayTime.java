/*
 * LeetCode 743 — Network Delay Time
 * Priority: High
 * Topic: Graphs + Dijkstra
 *
 * A network has n nodes numbered from 1 to n. Each entry [u, v, w]
 * in times describes a directed edge from u to v that takes w units
 * of time to traverse.
 *
 * A signal starts at node k. Return the earliest time at which every
 * node has received the signal. If any node is unreachable from k,
 * return -1.
 *
 * Examples
 *   times = [[2, 1, 1], [2, 3, 1], [3, 4, 1]], n = 4, k = 2 -> 2
 *   times = [[1, 2, 1]], n = 2, k = 1 -> 1
 *   times = [[1, 2, 1]], n = 2, k = 2 -> -1
 *
 * Constraints
 *   1 <= k <= n <= 100
 *   1 <= times.length <= 6,000
 *   times[i].length == 3
 *   1 <= u, v <= n
 *   u != v
 *   0 <= w <= 100
 *   Each ordered pair (u, v) appears at most once.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class NetworkDelayTime {

    public static void main(String[] args) {
        int[][] times = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
        int result = new NetworkDelayTime().networkDelayTime(times, 4, 2);
        System.out.println("Expected 2, got " + result);
    }


    // The flow is:
    // group connections → start at k → process earliest arrivals → update faster routes → return the largest arrival time
    public int networkDelayTime(int[][] times, int n, int k) {
        // 1. Group connections — store outgoing routes for each node.
        List<int[]>[] graph = new ArrayList[n + 1]; // +1 to make accessing nodes simpler.

        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
       // times = [[2, 1, 1], [2, 3, 1], [3, 4, 1]], n = 4, k = 2 -> 2
        for (int[] edge : times) {
            graph[edge[0]].add(new int[]{edge[1], edge[2]});
        }

        // [destination, travel time]
        // graph[0] = null          // unused
        // graph[1] = []
        // graph[2] = [[1, 1], [3, 1]]
        // graph[3] = [[4, 1]]
        // graph[4] = []

        // 2. Start at k — mark every node as unreached,
        // then set the starting node’s arrival to 0 and add it to the queue.
        int[] arrival = new int[n + 1];
        Arrays.fill(arrival, Integer.MAX_VALUE); // mean “not reached yet”—effectively infinity.
        arrival[k] = 0;

        // Each pair is [arrival time, person].
        PriorityQueue<int[]> queue
                = new PriorityQueue<>(Comparator.comparingInt(pair -> pair[0]));
        queue.offer(new int[]{0, k});

        // 3. Process earliest arrivals — take the smallest arrival time from the queue.
        // Skip entries if a faster route has already been found.
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int time = current[0];
            int person = current[1];

            // Skip an old entry if we already found a faster route.
            if (time > arrival[person]) {
                continue;
            }

            // 4. Update faster routes — check outgoing connections.
            // If a route improves an arrival time, save it and add it to the queue.
            for (int[] connection : graph[person]) {
                int next = connection[0];
                int newTime = time + connection[1];

                if (newTime < arrival[next]) {
                    arrival[next] = newTime;
                    queue.offer(new int[]{newTime, next});
                }
            }
        }

        // Index:    0    1   2   3   4
        // arrival: [∞,   1,  0,  1,  2]
        int answer = 0;
        // 5. Return the largest arrival time — return -1 if any node is unreachable;
        // otherwise return when the last node receives the signal.
        for (int i = 1; i <= n; i++) {
            if (arrival[i] == Integer.MAX_VALUE) {
                return -1;
            }
            answer = Math.max(answer, arrival[i]);
        }
        return answer;
    }
}
