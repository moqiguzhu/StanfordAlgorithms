# StanfordAlgorithms
This repo contains all the codes for **Algorithms: design and analysis part I** and **Algorithms: design and analysis part II** by Stanford University. On the process of creating this repo, I refactored all the old codes that I wrote on courses. I have two reasons to create the repo. One is that Github is a great platform where everyone can learn and improve, I want to join the big family. The other reason is simply practicing my coding skill.

Happy to make friends here. We can talk codes, sports and everything in life.

## Test Data
- all the test data are placed in the **testdata** directory

## Algorithms
### Rules
- n means number of nodes in a grapg
- m means number of edges in a graph

### KnapSack

Two methods have been used to solve the KnapSack problem. One is the traditional mehtod, that's Dynamic programming, the other is called Branch and Bound, which will be more space and time efficient than traditional DP method.

- When using DP, space compression trick has been used. Suppose that number of items is **n**, and the capacity of bag is **m**, without space compression, the total space and time complexity are both **mn**. After we use space compression, the time complexity remain unchanged, but the space complexity diminish to only **m**. For detail information, please refer to [KnapSack_Wiki](https://en.wikipedia.org/wiki/Knapsack_problem)

- Compared to traditional DP method, Branch and Bound is too much harder to implement. Fortunately, Branch and Bound can beat DP in both time and space usage. The test result on **knapsack_big.txt** shows that Branch and Bound can be 50 times faster than DP method. For detailed information abount Branch and Bound, please refer to [BranchAndBound_Wiki](https://en.wikipedia.org/wiki/Branch_and_bound)

### Minimum Spanning Tree 

Generally speaking, there are two algorithms solving Minimum Spanning Tree(MST) problem, they are Kruskal and Prim algorithms. Both algorithms adopt greedy strategies and can ensure to give the global optimal result.

- Prim's MST algorithm is much like the Dijkstra Single Source Shortest Path problem. Both Prim and Dijkstra algorithms need to use a priority queue to boost. Minimum Cut law ensures that correctness of these two algorithms.

- Kruskal's Minimum algorithm need to use a data structure called Union-Find to boost. Kruskal algorithm need to store all the edges of graph and then sort them. So the time complexity is **mlogm**. For more information about Union-Find, please refer to **Algorithms** by `Sedgewick`.

- The **MST** algorithm has another application called Single-linkage clustering, also called Maximum Spacing clustering, because it can keep the distance between clusters maximum when given number of clusters. Single Link Clustering is one of hierarchical cluster methods. For more detailed information, please refer to [Single-linkage clustering](https://en.wikipedia.org/wiki/Single-linkage_clustering)

### Single Path Shortest Path
Generally speaking, there are two algorithms to solve Single Source Shortest Path problem(SSSP), they are Dijkstra algorithm and Bellman-Ford algorithm.

- Dijkstra algorithm is much similar to Prim's MST algorithm, both need a heap to boost. The time complexity of Dijkstra algorithm is **nlogn+m**. Dijkstra algorithm suppose that there is no negative cycle in the graph, in those cases SSSP problem is undefined. Dijkstra's algorithm is indispensable for Johnson's algorithm. For more detailed information, please refer to [Dijkstra](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm)

- Different from Dijkstra's algorithm, Bellman and Ford proposed a new DP based algorithm to solve SSSP problem. There are two advantages when compared with Dijkstra algorithm. One is that Bellman-Ford algorithm is easier to understand and implement, the other is that Bellman-Ford algorithm can detect whether existing a negative cycle in a graph while Dijkstra's algorithm can not. Unfortunately, the time complexity of Bellman-Ford algorithm is **mn**. The test result on **Dijkstra.txt** shows that Dijkstra's algorithm is about 10 times faster than Bellman-Ford algorithm. For more detailed information, please refer to [Bellman-Forman](https://en.wikipedia.org/wiki/Bellman%E2%80%93Ford_algorithm)

### All Pairs Shortest Path 
Generally speaking, there are algorithms to solve All Pairs Shortest Path problem, they are Floyd-Warshall and Johnson algorithms.

- Floyd-Warshall algorithm is a DP based problem which has a **$n^3$** time complexity. The advantage of Floyd-Warshall is easy to understand and implement.

- Compared with Floyd-Warshall algorithm, Johnson's algorithm has a smaller asympotic time complexity, it's **n^2logn+mn**. It is quite a complicated algorithm and hard to implement. Although it has a smaller asympotic time complexity over Floyd-Warshall algorithm, the test result on **apsp3.txt** shows that it is much slower than Floyd-Warshall algorithm.

### Strong Connected Components
Strong Connected Components is an application of DFS. It is can be with recursive or without recursive. Recursive implementation will consume so much memory space but easy to implement. No recursive implementation will save a lot space and time, however, it is hard to implement and debug.

### Travelling Salesman Problem
TSP is to find the shortest Hanmilton cycle given some cities and distances between them. This is a NPC problem. There is a Dynamic Programming algorithm which can always find the optimal answer, however, time complexity is **n^22^n**.


## Some tips when implementing graph based algorithms
- view all the edges as directed edges. one undirected edge can be see as two directed edges with same weight
- pay much attention to file format when creating graph from file, such as whether this is a directed graph or undirected graph. If it is a undirected graph, will one edge appears two times in the file? In one sentence, specify requirements and what data structures that we need.
- all the nodes must be considered, especially those nodes which have no input degree or output degree

We can see all the graphs as directed graphs. All the information about a graph including:
- edge information, including head node and tail node and weight
- node information, including node label
- node link information, including all the output degree information and input degree information


## What to do 
- StrongConnectedComponent need to debug
