<!-- PROJECT LOGO -->
<br />
<p align="center">
    <img src="src/res/logoTitle.png" alt="Logo" >
</p>

<!--TABLE OF CONTENTS-->
## Table Of Contents
  1. [Our Team](#our-team)
  2. [Problem Statement](#problem-statement)
  3. [About The Project](#about-the-project)
  4. [Algorithms](#algorithms)
  5. [Input](#input)
  6. [External Libraries](#external-libraries)
  7. [Sample Outputs](#sample-outputs)


<!--OUR TEAM-->
## Our Team
No. | Team Member
--- | --- | 
1 | PANG CHONG WEN
2 | ELGENE EE DING REN
3 | TAN CHIAO YUN
4 | LIM BOON GUAN

## Problem Statement
We as professional engineer are requested to simulate the delivery process and planning in a country to help our friend shorten their delivery time using different searching algorithms.

<!--ABOUT THE PROJECT-->
## About The Project
Our team has created a JavaFX project that helps to simulate the route of each vehicle depending on different algorithms. We chose JavaFX simply because we want to visualise the outcome of different paths taken by each vehicle. 

## Algorithms
Some searching algorithms are applied in this system to benchmark the efficiency and outcome of each algorithm.

- [Basic Simulation (DFS)](https://www.geeksforgeeks.org/depth-first-search-or-dfs-for-a-graph/)
- [Greedy Search](https://brilliant.org/wiki/greedy-algorithm/)
- [A* Search](https://brilliant.org/wiki/a-star-search/)
- [Dijkstra's Algorithm](https://brilliant.org/wiki/dijkstras-short-path-finder/)
- [Monte Carlo Tree Search (MCTS)](https://www.ijcai.org/Proceedings/11/Papers/115.pdf)

## Input
A `.txt` file, where the first row indicates the **number of customers** (including depot), N and **maximum capacity** of all vehicles, C. After this, starting from second row onwards are the N rows of information. In particular, every row of information contains 3 data, which are **x-coordinate**, **y-coordinate** and lastly **demand size** of a customer. The second row represents the depot and its demand size is always 0 while the rest of the rows show customer information. An example of input is given below.

```
7 27
176 139 0
119 154 23
47 73 3
73 133 3
170 18 24
80 93 9
34 61 3
```

## External Libraries
Before setting up the project, there are 2 external libraries you need to add into your project. 

1. [JavaFX 16](https://gluonhq.com/products/javafx/)
2. [JFoeniX 9.x.x](https://github.com/sshahine/JFoenix)

Click [here](https://www.youtube.com/watch?v=DPjmIn0rWY0) for more information regarding how to setup external libraries in your project.

## Sample Outputs
<p align="center">
    <img src="src/res/demo.gif" width="800" height="665.82" />
</p>

