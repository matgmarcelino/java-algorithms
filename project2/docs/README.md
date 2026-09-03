# Sets and Multisets

**Author:** Matheus Marcelino

This project implements standard collection operations on two related data structures: ordinary sets and multisets. Sets are represented as a bit-string over a `boolean[]`, and multisets (bags), where the number of occurrences of an element matters, are backed by a `HashMap`.

---

## Language and Version

- **Language:** Java
- **Version:** Java 21

---

## Files

- **`Universe.java`** — The shared universe of 12 colleges. Both set classes read their elements from this, so having a separate class reduces code duplication.
- **`BitStringSet.java`** — Ordinary set operations (complement, union, intersection, difference, symmetric difference) implemented over a `boolean[]` and represented as a bit-string where each bit corresponds to a college in the universe.
- **`MultiSet.java`** — Multiset (bag) operations (union with max counts, intersection with min counts, difference floored at zero, and sum) implemented using a `HashMap<String, Integer>` mapping each college to its count.
- **`Main.java`** — The driver and primary entry point. It builds hard-coded test sets, runs every operation through multiple cases including edge cases, and prints labeled output.

---
