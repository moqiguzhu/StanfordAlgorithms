# StanfordAlgorithms
This repo contains all the codes for <Algorithms: design and analysis part I> and <Algorithms: design and analysis part II> by Stanford University. On the process of creating this repo, I refactored all the old codes that I wrote on courses. I have two reasons to create the repo. One is that Github is a great platform where everyone can learn and improve, I want to join the big family. The other reason is simply practicing my coding skill.

Happy to make friends here. We can talk codes, sports and everything in life.

## Test Data
- all the test data are placed in the **testdata** directory

## Algorithms
### KnapSack
Two methods have been used to solve the KnapSack problem. One is the traditional mehtod, that's Dynamic programming, the other is called Branch and Bound, which will be more space and time efficient than traditional DP method.
- When using DP, space compression trick has been used. Suppose that number of items is **n**, and the capacity of bag is **m**, without space compression, the total space and time complexity are both **mn**. After we use space compression, the time complexity remain unchanged, but the space complexity diminish to only **m**. For detail information, please refer to [KnapSack_Wiki](https://en.wikipedia.org/wiki/Knapsack_problem)


- Compared to traditional DP method, Branch and Bound is too much harder to implement. Fortunately, Branch and Bound can beat DP in both time and space complexity. The test result on **knapsack_big.txt** shows that Branch and Bound can be 50 times faster than DP method. For detailed information abount Branch and Bound, please refer to [BranchAndBound_Wiki](https://en.wikipedia.org/wiki/Branch_and_bound)

## What to do 
- StrongConnectedComponent need to debug
