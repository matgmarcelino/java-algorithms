/*
 * Provides a helper for generating the power set (all subsets) of a group of
 * experiments, used by the brute-force selection strategy.
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utility for generating the power set (all subsets) of a collection of experiments.
 */
public class SubsetGenerator {

    /**
     * Generates every possible subset of the given experiments, including the
     * empty set and the full set.
     * <p>
     * The result contains 2<sup>n</sup> subsets, where {@code n} is the number of
     * experiments, so this is only practical for small inputs.
     *
     * @param experiments the experiments to build subsets from
     * @return a list of all subsets
     */
    public static List<Set<Experiment>> allSubsets(Experiment[] experiments) {

        List<Set<Experiment>> result = new ArrayList<>();
        result.add(new HashSet<>());

        for (Experiment experiment : experiments) {

            List<Set<Experiment>> subsetsWithExperiment = new ArrayList<>();
            for (Set<Experiment> subset : result) {
                Set<Experiment> copy = new HashSet<>(subset);
                copy.add(experiment);
                subsetsWithExperiment.add(copy);
            }

            result.addAll(subsetsWithExperiment);
        }

        return result;
    }

}