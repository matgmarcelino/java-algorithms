# Java Algorithms

**Author:** Matheus Marcelino
**Repository:** https://github.com/matgmarcelino/CS2430

## Overview

A collection of algorithm and data-structure implementations in Java, each built as a small, self-contained project with its own experiment, correctness checks, and written analysis of the results. The focus throughout is on measuring and explaining algorithmic behavior rather than just implementing it: comparison counts, complexity classes, and empirical results are treated as first-class output alongside the code.

### [Project 1 — Sorting Algorithm Comparison](project1/docs/README.md)

Implements and empirically compares four classic sorting algorithms in Java: merge sort, quick sort, heap sort, and shaker sort. Each algorithm tracks an exact element-to-element comparison count, verified for correctness across all n! permutations of static arrays of size n = 4, 6, and 8. Minimum, maximum, and average comparison counts are reported in a formatted table (`ResultsTable.java`), exposing how each algorithm's real-world comparison behavior diverges from its asymptotic complexity class.

### [Project 2 — Sets and Multisets](project2/docs/README.md)

Implements standard collection operations on two data structures in Java. Ordinary sets are represented as a bit-string over a `boolean[]`, where each bit corresponds to an element of a shared universe, supporting complement, union, intersection, difference, and symmetric difference in O(1) per operation. Multisets (bags) are implemented with a `HashMap<String, Integer>` mapping each element to its count, supporting union (max counts), intersection (min counts), difference (floored at zero), and sum. A `Main` driver exercises both implementations across a range of edge cases with labeled output.

### [Project 3 — Optimal Selection (0/1 Knapsack)](project3/docs/README.md)

Compares several strategies for solving an instance of the 0/1 knapsack problem: selecting the subset of items that maximizes total value without exceeding a fixed weight budget. Implements three greedy heuristics (highest value first, lightest weight first, best value-to-weight ratio), an exhaustive brute-force search over all 2^n subsets, and an optimal dynamic-programming solution using a `dp[i][c]` table with O(n · capacity) time, including backtracking to recover the chosen items. A driver runs every strategy and prints a side-by-side comparison of the results and their tradeoffs.

### [Project 4 — Monte Carlo Board Simulation](project4/docs/README.md)

A Monte Carlo simulation in Java modeling a player moving around a standard 40-square board game, measuring how landing frequency shifts under different rule-based strategies. Dice movement, doubles, a "go to jail" mechanic, and two card decks are all modeled explicitly. The `board` package defines the board layout, `cards` implements the two decks and their effects, and `sim` contains the player state, two competing strategy implementations, and a `BatchRunner` that performs 10 runs per strategy with checkpoints at 1,000 / 10,000 / 100,000 / 1,000,000 turns, producing 80 CSV snapshots and a statistical summary written to `data/`.

## Repository Structure

```
.
├── README.md
├── .gitignore
├── project1/
│   ├── src/SortingAlgorithms/
│   │   ├── SortingAlgorithms.java
│   │   ├── DataGenerator.java
│   │   ├── testDriver.java
│   │   └── ResultsTable.java
│   └── docs/
│       └── README.md
├── project2/
│   ├── src/
│   │   ├── Universe.java
│   │   ├── BitStringSet.java
│   │   ├── MultiSet.java
│   │   └── Main.java
│   └── docs/
│       └── README.md
├── project3/
│   ├── src/
│   │   ├── Experiment.java
│   │   ├── SubsetGenerator.java
│   │   └── OptimizationStrategies.java
│   └── docs/
│       └── README.md
└── project4/
    ├── src/
    │   ├── board/
    │   │   ├── Board.java
    │   │   └── SquareType.java
    │   ├── cards/
    │   │   ├── Card.java
    │   │   ├── CardType.java
    │   │   ├── Deck.java
    │   │   └── DeckType.java
    │   └── sim/
    │       ├── Player.java
    │       ├── Strategy.java
    │       ├── Simulation.java
    │       └── BatchRunner.java
    ├── data/
    └── docs/
        └── README.md
```

Each `project#/` folder holds the source for that implementation. The `docs/` subfolder inside each project holds its detailed README, including project structure, design notes, and results.
