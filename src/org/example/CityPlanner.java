//Question number 4a solution

package org.example;

import java.util.*;

public class CityPlanner {

    // Define a class to represent edges in the graph
    static class Edge {
        int to, weight;
        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    public static void main(String[] args) {
        int n = 5; // Number of locations
        int[][] roads = {
                {4, 1, -1},
                {2, 0, -1},
                {0, 3, -1},
                {4, 3, -1}
        }; // Roads with travel times, -1 indicates under construction
        int source = 0;
        int destination = 1;
        int targetTime = 5;

        List<int[]> result = findValidModification(n, roads, source, destination, targetTime);
        if (result.isEmpty()) {
            System.out.println("No valid modification found.");
        } else {
            for (int[] road : result) {
                System.out.println(Arrays.toString(road));
            }
        }
    }

    public static List<int[]> findValidModification(int n, int[][] roads, int source, int destination, int targetTime) {
        // Initialize graph with lists of edges
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        // List of edges under construction
        List<int[]> underConstruction = new ArrayList<>();

        // Build the graph and identify under-construction edges
        for (int[] road : roads) {
            if (road[2] == -1) {
                underConstruction.add(road);
            } else {
                graph.get(road[0]).add(new Edge(road[1], road[2]));
                graph.get(road[1]).add(new Edge(road[0], road[2]));
            }
        }

        // Set all under-construction edges to weight 1 initially
        for (int[] road : underConstruction) {
            road[2] = 1;
            graph.get(road[0]).add(new Edge(road[1], road[2]));
            graph.get(road[1]).add(new Edge(road[0], road[2]));
        }

        // Calculate the initial shortest path
        int initialDistance = dijkstra(graph, source, destination, n);

        if (initialDistance == targetTime) {
            // If the initial path already matches the target time, return the roads as they are
            return Arrays.asList(roads);
        } else {
            // Adjust weights of under-construction roads
            for (int[] road : underConstruction) {
                // Reset the graph with weight 1 for all edges
                graph = resetGraph(n, roads, graph);
                // Try to adjust current road weight
                road[2] = targetTime - initialDistance;
                if (road[2] > 0) {
                    graph.get(road[0]).add(new Edge(road[1], road[2]));
                    graph.get(road[1]).add(new Edge(road[0], road[2]));
                }
                int modifiedDistance = dijkstra(graph, source, destination, n);
                if (modifiedDistance == targetTime) {
                    return Arrays.asList(roads);
                }
            }

            // If no valid modification found, return an empty list
            return Collections.emptyList();
        }
    }

    // Helper function to reset the graph with all edge weights set to 1
    private static List<List<Edge>> resetGraph(int n, int[][] roads, List<List<Edge>> graph) {
        graph.clear();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] road : roads) {
            if (road[2] > 0) {
                graph.get(road[0]).add(new Edge(road[1], road[2]));
                graph.get(road[1]).add(new Edge(road[0], road[2]));
            }
        }
        return graph;
    }

    // Dijkstra's algorithm to find the shortest path from source to destination
    public static int dijkstra(List<List<Edge>> graph, int source, int destination, int n) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(edge -> edge.weight));
        pq.add(new Edge(source, 0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            int u = current.to;
            int d = current.weight;

            if (d > dist[u]) continue;

            for (Edge edge : graph.get(u)) {
                int v = edge.to;
                int weight = edge.weight;
                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.add(new Edge(v, dist[v]));
                }
            }
        }
        return dist[destination];
    }
}

// Output:
//[4, 1, 2]
//[2, 0, 2]
//[0, 3, 2]
//[4, 3, 2]
