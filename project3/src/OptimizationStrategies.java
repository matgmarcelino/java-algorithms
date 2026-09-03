/*
 * Contains the driver and the various strategies (greedy, brute-force, and
 * dynamic programming) for selecting the highest-rated subset of experiments
 * that fits within the weight budget.
 */

import java.util.*;

/**
 * Explores several strategies for selecting a subset of experiments that
 * maximizes total rating without exceeding a fixed weight budget of 700 kg.
 * <p>
 * The strategies range from greedy heuristics (sorting by rating, weight, or
 * rating-to-weight ratio) to an exhaustive brute-force search and an optimal
 * dynamic-programming solution to the underlying 0/1 knapsack problem.
 */
public class OptimizationStrategies {

    private static final Experiment[] EXPERIMENTS = {
            new Experiment("Cloud Patterns", 36, 5),
            new Experiment("Solar Flares", 264, 9),
            new Experiment("Solar Power", 188, 6),
            new Experiment("Binary Stars", 203, 8),
            new Experiment("Relativity", 104, 8),
            new Experiment("Seed Viability", 7, 4),
            new Experiment("Sun Spots", 90, 2),
            new Experiment("Mice Tumors", 65, 8),
            new Experiment("Microgravity Plant Growth", 75, 5),
            new Experiment("Micrometeorites", 170, 9),
            new Experiment("Cosmic Rays", 80, 7),
            new Experiment("Yeast Fermentation", 27, 4)
    };

    /**
     * Greedily adds experiments from the given (already sorted) array into the
     * set, skipping any experiment that would push the total weight above the
     * 700 kg budget.
     *
     * @param sortedExperiments the experiments in the order they should be considered
     * @param set               the set to populate
     */
    private static void addIntoSet(Experiment[] sortedExperiments, Set<Experiment> set) {
        int sumWeight = 0;

        for (Experiment e : sortedExperiments) {
            if (sumWeight + e.getWeight() > 700)
                continue;
            set.add(e);
            sumWeight += e.getWeight();
        }
    }

    /**
     * Greedy strategy that considers experiments from the highest rating to lowest,
     * adding each one that fits within the weight budget.
     *
     * @return the selected set of experiments
     */
    public static Set<Experiment> highestRatingFirst() {
        Experiment[] sortedExperiments = EXPERIMENTS.clone();
        Arrays.sort(sortedExperiments, Comparator.comparing(Experiment::getRating).reversed());

        Set<Experiment> set = new HashSet<>();
        addIntoSet(sortedExperiments, set);

        return set;
    }

    /**
     * Greedy strategy that considers experiments from lightest to heaviest,
     * adding each one that fits within the weight budget.
     *
     * @return the selected set of experiments
     */
    public static Set<Experiment> lightestFirst() {
        Experiment[] sortedExperiments = EXPERIMENTS.clone();
        Arrays.sort(sortedExperiments, Comparator.comparingInt(Experiment::getWeight));

        Set<Experiment> set = new HashSet<>();
        addIntoSet(sortedExperiments, set);

        return set;
    }

    /**
     * Greedy strategy that considers experiments by descending rating-to-weight
     * ratio, adding each one that fits within the weight budget.
     *
     * @return the selected set of experiments
     */
    public static Set<Experiment> weightedScore() {
        Experiment[] sortedExperiments = EXPERIMENTS.clone();
        Arrays.sort(sortedExperiments,
                Comparator.comparingDouble((Experiment a) -> (double) a.getRating() / a.getWeight()).reversed());

        Set<Experiment> set = new HashSet<>();
        addIntoSet(sortedExperiments, set);

        return set;
    }

    /**
     * Brute-force strategy that generates every possible subset of weighted experiments,
     * keeps those within the 700 kg budget, and returns the three with the
     * highest total rating.
     * <p>
     * This runs in O(2<sup>n</sup>) time.
     *
     * @return the three highest-rated valid subsets, in descending order of rating
     */
    public static ArrayList<Set<Experiment>> exhaustiveSearch() {
        List<Set<Experiment>> allSubsets = SubsetGenerator.allSubsets(EXPERIMENTS);
        List<Set<Experiment>> validSubsets = new ArrayList<>();

        for (Set<Experiment> subset : allSubsets) {
            int sumWeight = 0;

            for (Experiment e : subset) sumWeight += e.getWeight();

            if (sumWeight <= 700)
                validSubsets.add(subset);
        }

        validSubsets.sort(Comparator.comparingInt(OptimizationStrategies::sumRating).reversed());

        ArrayList<Set<Experiment>> result = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            result.add(validSubsets.get(i));

        return result;
    }

    /**
     * Optimal strategy that solves the selection as a 0/1 knapsack problem using
     * dynamic programming, maximizing total rating without exceeding the 700 kg
     * budget.
     * <p>
     * A table {@code dp[i][c]} holds the best rating achievable using the first
     * {@code i} experiments within a weight budget of {@code c}. After the table
     * is filled, the chosen experiments are recovered by backtracking. This runs
     * in O(n &times; capacity) time.
     *
     * @return the optimal set of experiments
     */
    public static Set<Experiment> dynamicProgramming() {
        int n = EXPERIMENTS.length;
        int CAPACITY = 700;
        int[][] dp = new int[n + 1][CAPACITY + 1];

        for (int i = 1; i <= n; i++) {
            int weight = EXPERIMENTS[i - 1].getWeight();
            int rating = EXPERIMENTS[i - 1].getRating();

            for (int c = 0; c <= CAPACITY; c++) {
                dp[i][c] = dp[i - 1][c];

                if (weight <= c)
                    dp[i][c] = Math.max(dp[i][c], dp[i - 1][c - weight] + rating);
            }
        }

        Set<Experiment> set = new HashSet<>();
        int c = CAPACITY;

        for (int i = n; i > 0; i--) {
            if (dp[i][c] != dp[i - 1][c]) {
                Experiment e = EXPERIMENTS[i - 1];
                set.add(e);
                c -= e.getWeight();
            }
        }

        return set;
    }

    /**
     * Computes the total rating of all experiments in the given subset.
     *
     * @param subset the experiments to sum over
     * @return the sum of the ratings
     */
    private static int sumRating(Set<Experiment> subset) {
        int sumRating = 0;
        for (Experiment e : subset) sumRating += e.getRating();
        return sumRating;
    }

    /**
     * Runs every selection strategy and prints their results along with a
     * summary comparing the approaches.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {

        System.out.println("------------- Part 1 -------------");

        System.out.println("Highest Rating");
        printSet(highestRatingFirst());

        System.out.println("Lightest First");
        printSet(lightestFirst());

        System.out.println("Weighed Score");
        printSet(weightedScore());

        System.out.println("------------- Part 2 -------------");

        System.out.println("Brute Force Approach; The three highest-rated subsets:" + "\n");
        for (Set<Experiment> s : exhaustiveSearch()) {
            printSet(s);
        }

        System.out.println("------------- Part 4 -------------");
        printSet(dynamicProgramming());

        System.out.println("------------- SUMMARY -------------");
        System.out.println("""
                    For the highest rating first strategy, it is not optimal as it completely disregards the weight.
                Choosing an experiment solely based on its rating causes really poor rating-to-weight experiments
                to be chosen.
                    With the lightest weight first strategy, although the score was higher than the previous strategy,
                it is also not optimal as some lightweight experiments have poor ratings. This also causes really
                poor rating-to-weight experiments to be chosen.
                    Using a weighed score strategy is the best way of going about this problem. Comparing each
                experiment to their rating-to-weight ratio makes use of all data we have, and only the best scores
                are regarded.
                    However, all of the strategies above are greedy algorithms, which choose the locally optimal choice
                at each step in hope of finding an optimal solution. This often leads to the algorithm not finding the
                most optimal choice possible. The way to get through that is to just check every possible answer and
                pick the best one.
                    With a brute force approach, where we find all possible subsets of experiments and calculate their
                overall rating if the weight does not exceed 700kg, we can sort this list to find the best possible
                solution, although at the cost of O(2^n) time.
                    Expanding on the strategy above, we can use dynamic programming to find the best possible subset
                in O(n * capacity) time. We can create a table dp[i][c] that represents the best total rating achievable
                using the first i experiments restricted to the weight budget c. For each experiment, we compare how
                it would affect the rating and weight later on and if it is optimal, then we choose to skip it or take it,
                and if we take it we gain its value and spend its weight. Then we keep doing this until the most optimal
                subset is found. This is the best strategy as it finds the best possible subset while also being
                O(n * capacity) time.
               """);
    }

    /**
     * Prints each experiment in the set followed by the set's total rating and
     * total weight.
     *
     * @param set the set of experiments to print
     */
    private static void printSet(Set<Experiment> set) {
        int sumRating = 0;
        int sumWeight = 0;

        for (Experiment e : set) {
            sumRating += e.getRating();
            sumWeight += e.getWeight();

            System.out.println(e);
        }

        System.out.println("Sum of ratings: " + sumRating);
        System.out.println("Sum of weight: " + sumWeight + "\n");
    }
}
