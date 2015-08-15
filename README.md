# StanfordAlgorithms
This repo contains all the codes for **Algorithms: design and analysis part I** and **Algorithms: design and analysis part II** by Stanford University. On the process of creating this repo, I refactored all the old codes that I wrote on courses. I have two reasons to create the repo. One is that Github is a great platform where everyone can learn and improve, I want to join the big family. The other reason is simply practicing my coding skill.

Happy to make friends here. We can talk codes, sports and everything in life.

## Test Data
- all the test data are placed in the **testdata** directory

## Algorithms
### KnapSack

Two methods have been used to solve the KnapSack problem. One is the traditional mehtod, that's Dynamic programming, the other is called Branch and Bound, which will be more space and time efficient than traditional DP method.

- When using DP, space compression trick has been used. Suppose that number of items is **n**, and the capacity of bag is **m**, without space compression, the total space and time complexity are both **mn**. After we use space compression, the time complexity remain unchanged, but the space complexity diminish to only **m**. For detail information, please refer to [KnapSack_Wiki](https://en.wikipedia.org/wiki/Knapsack_problem)

- Compared to traditional DP method, Branch and Bound is too much harder to implement. Fortunately, Branch and Bound can beat DP in both time and space usage. The test result on **knapsack_big.txt** shows that Branch and Bound can be 50 times faster than DP method. For detailed information abount Branch and Bound, please refer to [BranchAndBound_Wiki](https://en.wikipedia.org/wiki/Branch_and_bound)

### Minimum Spanning Tree 

Generally speaking, there are two algorithms solving Minimum Spanning Tree(MST) problem, they are **Kruskal** and **Prim** algorithms. Both algorithms adopt greedy strategies and can ensure to give the global optimal result.

- Prim's MST algorithm is much like the Dijkstra Single Source Shortest Path problem. Both Prim and Dijkstra algorithms need to use a priority queue to boost. **Minimum Cut** law ensures that correctness of these two algorithms.

- Kruskal's Minimum algorithm need to use a data structure called Union-Find to boost. Kruskal algorithm need to store all the edges of graph and then sort them. So the time complexity is **mlogm**. For more information about Union-Find, please refer to **Algorithms** by `Sedgewick`.

- The **MST** algorithm has another application called **Single-linkage clustering**, also called **Maximum Spacing clustering**, because it can keep the distance between clusters maximum when given number of clusters. **Single Link Clustering** is one of hierarchical cluster methods. For more detailed information, please refer to [Single-linkage clustering](https://en.wikipedia.org/wiki/Single-linkage_clustering)

## What to do 
- StrongConnectedComponent need to debug
