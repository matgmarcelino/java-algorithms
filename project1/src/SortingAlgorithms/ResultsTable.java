package SortingAlgorithms;

/**
 * Part 4 - Results Table
 * Compares all four sorting algorithms across all permutations of n = 4, 6, and 8,
 * reporting minimum, maximum, and average comparison counts with interpretation.
 */
public class ResultsTable {

    /**
     * Functional interface representing a sorting algorithm that operates on an int array.
     * Used to pass any of the four sort methods into collectStats as a parameter.
     */
    @FunctionalInterface
    interface SortFn { void sort(int[] arr); }

    /**
     * Computes the factorial of n iteratively.
     *
     * @param n a non-negative integer
     * @return n! (e.g. factorial(4) returns 24)
     */
    private static int factorial(int n) {
        int f = 1;
        for (int i = 2; i <= n; i++) f *= i;
        return f;
    }

    /**
     * Generates all permutations of the given array using Heap's algorithm.
     * Heap's algorithm produces all n! orderings by swapping elements in a
     * specific pattern that guarantees every permutation is visited exactly once.
     *
     * @param arr the source array to permute
     * @return a 2D array where each row is one unique permutation of arr
     */
    private static int[][] allPermutations(int[] arr) {
        int n = arr.length, total = factorial(n), idx = 0;
        int[][] result = new int[total][];
        int[] a = arr.clone(), c = new int[n];
        result[idx++] = a.clone();
        int i = 0;
        while (i < n) {
            if (c[i] < i) {
                int tmp;
                if (i % 2 == 0) { tmp = a[0];    a[0]    = a[i]; }
                else             { tmp = a[c[i]]; a[c[i]] = a[i]; }
                a[i] = tmp;
                result[idx++] = a.clone();
                c[i]++;
                i = 0;
            } else { c[i] = 0; i++; }
        }
        return result;
    }

    /**
     * Runs the given sort over all permutations of the base array.
     *
     * @param base   the base array to permute
     * @param sortFn the sort to benchmark
     * @return long[] containing { min, max, total, count }
     */
    private static long[] collectStats(int[] base, SortFn sortFn) {
        int[][] perms = allPermutations(base);
        long min = Long.MAX_VALUE, max = Long.MIN_VALUE, total = 0;
        for (int[] perm : perms) {
            sortFn.sort(perm);
            long c = SortingAlgorithms.comparisons;
            if (c < min) min = c;
            if (c > max) max = c;
            total += c;
        }
        return new long[]{ min, max, total, perms.length };
    }

    /**
     * Prints a written interpretation of the results for a given n.
     * Identifies which algorithm performed best and worst on average,
     * and explains what the min/max spread reveals about each algorithm's
     * sensitivity to input ordering.
     *
     * @param n     the input size being interpreted
     * @param stats a 2D array of shape [4][4], one row per algorithm,
     *              each row being { min, max, total, count }
     */
    private static void printInterpretation(int n, long[][] stats) {
        String[] names = { "Merge sort", "Quick sort", "Heap sort", "Shaker sort" };

        // find best and worst average
        int bestIdx = 0, worstIdx = 0;
        double bestAvg = Double.MAX_VALUE, worstAvg = 0;
        for (int i = 0; i < 4; i++) {
            double avg = (double) stats[i][2] / stats[i][3];
            if (avg < bestAvg)  { bestAvg  = avg; bestIdx  = i; }
            if (avg > worstAvg) { worstAvg = avg; worstIdx = i; }
        }

        double mergeSpread  = stats[0][1] - stats[0][0];
        double quickSpread  = stats[1][1] - stats[1][0];
        double heapSpread   = stats[2][1] - stats[2][0];
        double shakerSpread = stats[3][1] - stats[3][0];

        System.out.println("Interpretation:");
        System.out.printf(
                "  Across all %d permutations, %s required the fewest comparisons on " +
                        "average (%.2f), while %s required the most (%.2f).%n",
                factorial(n), names[bestIdx], bestAvg, names[worstIdx], worstAvg
        );
        System.out.printf(
                "  Merge sort's min/max range was %.0f, reflecting its consistent " +
                        "O(n log n) behavior. Tt performs roughly the same amount of work " +
                        "regardless of input order.%n",
                mergeSpread
        );
        System.out.printf(
                "  Quick sort's range was %.0f. With a last-element pivot, sorted or " +
                        "reverse-sorted inputs produce its worst case since the pivot never " +
                        "splits the array evenly.%n",
                quickSpread
        );
        System.out.printf(
                "  Heap sort's range was %.0f. It always builds the full heap and " +
                        "extracts every element, so input order has little effect on its " +
                        "comparison count.%n",
                heapSpread
        );
        System.out.printf(
                "  Shaker sort's range was %.0f, the largest spread of the four. " +
                        "Its early exit gives it a very low minimum on nearly-sorted inputs, " +
                        "but it degrades toward O(n^2) on adversarial orderings.%n",
                shakerSpread
        );
        System.out.println();
    }

    /**
     * Entry point for the results table.
     * For each n in {4, 6, 8}, runs all four algorithms over every permutation
     * of the static base array, prints the comparison count table, then prints
     * a written interpretation of the results.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        String[] names = { "Merge sort", "Quick sort", "Heap sort", "Shaker sort" };
        SortFn[] fns = {
                SortingAlgorithms::mergeSort,
                SortingAlgorithms::quickSort,
                SortingAlgorithms::heapSort,
                SortingAlgorithms::shakerSort
        };

        for (int n : new int[]{ 4, 6, 8 }) {
            int[]    base  = DataGenerator.generateStaticIntegerArray(n);
            long[][] stats = new long[4][];
            for (int s = 0; s < 4; s++) {
                stats[s] = collectStats(base, fns[s]);
            }

            System.out.printf("n = %d  (%d permutations)%n", n, factorial(n));
            System.out.println("──────────────────────────────────────────────────────");
            System.out.printf("%-14s  %8s  %8s  %10s%n", "Algorithm", "Min", "Max", "Average");
            System.out.println("──────────────────────────────────────────────────────");
            for (int s = 0; s < 4; s++) {
                double avg = (double) stats[s][2] / stats[s][3];
                System.out.printf("%-14s  %8d  %8d  %10.2f%n",
                        names[s], stats[s][0], stats[s][1], avg);
            }
            System.out.println("──────────────────────────────────────────────────────");
            System.out.println();
            printInterpretation(n, stats);
        }
    }
}