# Sorting Algorithm Comparison

**Author:** Matheus Marcelino

---

## Overview

This project implements four classic sorting algorithms in Java and measures the exact element-to-element comparison count across every permutation of several small input sizes. The goal is to see how theoretical complexity plays out on small, concrete inputs, where constant factors and best/worst-case behavior matter as much as the asymptotic bound.

**Algorithms implemented:**

- Merge sort
- Quick sort
- Heap sort
- Shaker sort (bidirectional bubble sort)

---

## Project Structure

```
src/SortingAlgorithms/
├── SortingAlgorithms.java   | All four sorting algorithm implementations
├── DataGenerator.java       | Generates static and random integer arrays
├── testDriver.java          | Runs all algorithms and verifies correctness
└── ResultsTable.java        | Generates a result table based on real execution
```

---

### Comparison Counting

All four sorting algorithms share a `public static long comparisons` count that gets reset after each call. Every direct element-to-element comparison increments this counter by one, and the caller can read the counter to get the exact count for each run.

### Test Driver (`testDriver.java`)

Runs all four algorithms on:

- Static arrays of size 4, 6, and 8 (predefined arrays)
- Random arrays of size 10

For each run it prints the algorithm name, n value, input array, sorted output, comparison count, and correctness.

### Results Table (`ResultsTable.java`)

For each input size (n = 4, 6, 8), generates all n! permutations of the static base array using Heap's algorithm, runs all four sorts on every permutation, and reports the minimum, maximum, average, and the range from minimum to maximum across all permutations.

---

## Running the Project

Open in IntelliJ IDEA and run either main class:

```
testDriver.java     -- correctness verification output
ResultsTable.java   -- statistical comparison table
```

---

## Sample Output

**testDriver** (one block per algorithm per size):

```
Algorithm   : Merge sort
n           : 4
Input       : [3, 1, 4, 2]
Output      : [1, 2, 3, 4]
Comparisons : 5
Status      : PASS
```

**ResultsTable** (one table per input size):

```
n = 4  (24 permutations)
──────────────────────────────────────────────────────
Algorithm        Min       Max      Average
──────────────────────────────────────────────────────
Merge sort         4         5         4.67
Quick sort         3         6         4.58
Heap sort          6         8         7.08
Shaker sort        1         6         3.75
──────────────────────────────────────────────────────
```

---

## Key Findings

- **Merge sort** is the most consistent of the four. Its comparison count varies only by a small amount regardless of input. It guarantees O(n log n) behavior.
- **Quick sort** has the widest range with large n as the current pivot strategy degrades on sorted or reversed sorted input.
- **Heap sort** consistently has the highest average comparison count despite being O(n log n), as heapify always makes two comparisons per node regardless of whether a swap happens.
- **Shaker sort** achieves the lowest average with a small n due to its slim overhead and early-exit optimization. However, since it is an O(n²) algorithm, as n grows it would quickly fall behind the other algorithms.

---
