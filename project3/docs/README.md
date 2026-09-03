# Optimal Selection (0/1 Knapsack)

**Author:** Matheus Marcelino

This project explores several strategies for the _optimal selection_ problem: finding the subset of experiments that maximizes total rating without exceeding a 700kg weight limit — an instance of the 0/1 knapsack problem. It compares greedy strategies, an exhaustive brute-force approach, and an optimal dynamic-programming solution.

---

## Language and Version

- **Language:** Java
- **Version:** Java 21

---

## Files

- **`Experiment.java`** — The immutable data type modeling a single experiment as a name, weight (in kilograms), and scientific rating.
- **`SubsetGenerator.java`** — A helper class that generates the power set (all 2<sup>n</sup> subsets) of the experiments. Used by the brute-force strategy.
- **`OptimizationStrategies.java`** — The driver and main entry point. It holds the fixed list of 12 experiments and implements the selection strategies, then runs and prints all of them along with a written summary comparing the approaches.

---

## Strategies

### Greedy strategies

Each greedy strategy sorts the experiments by some key and then adds them one at a time, skipping any experiment that would push the total weight over 700 kg.

- **Highest rating first** — considers experiments from the highest rating to lowest. Not optimal as it completely disregards the weight.
- **Lightest first** — considers experiments from lightest to heaviest. Finds a better solution than the previous strategy, but is still not optimal as it disregards rating-to-weight ratio.
- **Weighted score** — considers experiments by descending rating-to-weight ratio. The best of the greedy strategies as it uses all data from each experiment to calculate a score.

### Exhaustive and optimal

- **Brute-force / exhaustive search** — generates every possible subset, keeps those within the weight limit, and chooses the highest-rated one. Finds the optimal subset but runs in O(2<sup>n</sup>) time.
- **Dynamic programming** — solves the 0/1 knapsack optimally in O(n · capacity) time. A table `dp[i][c]` holds the best rating achievable using the first `i` experiments within a weight budget of `c`. After the table is filled, the chosen experiments are recovered by backtracking. This is the best strategy as it finds the optimal subset without the cost of a brute-force search.

---

## How to Run

From the `project3/src` directory:

```
javac *.java
java OptimizationStrategies
```

The program runs every strategy and prints each selected set with its total rating and total weight, followed by a summary comparing the approaches.
