/*
 * Batch driver for the experiment: runs 10 independent simulations per
 * jail-exit strategy, snapshots the landing counts at 1K, 10K, 100K and 1M
 * turns, and writes every snapshot plus a comparative summary to data/ as CSV
 * and TXT files for the report.
 */

package sim;

import board.Board;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Batch driver for the Monopoly landing-frequency experiment.
 *
 * <p>For each {@link Strategy} this runs 10 independent simulations, taking a
 * snapshot of the landing counts at 1K, 10K, 100K and 1M turns. Every snapshot
 * is written to a CSV file under {@code data/}, and the 1M snapshots are
 * retained so a final comparative summary can be produced across strategies.
 */
public class BatchRunner {

    /** Board size; index range for every landing-count array used here. */
    private static final int NUM_SQUARES = 40;

    /** Number of independent runs performed per strategy. */
    private static final int RUNS_PER_STRATEGY = 10;

    /** Turn count of the final checkpoint, used as the denominator for percentages. */
    private static final long FINAL_N = 1_000_000L;


    /**
     * Writes a single landing-count snapshot to a CSV file under {@code data/}.
     *
     * <p>The file name encodes the strategy label, run number and turn count so
     * the 80 snapshots produced by {@link #main(String[])} do not collide. Each
     * row reports a square's index, name, raw landing count and the percentage
     * of turns that ended on it.
     *
     * @param strategy  the strategy that produced the snapshot
     * @param runNumber the 1-based run number
     * @param n         the number of turns simulated when the snapshot was taken
     * @param seed      the RNG seed used for the run (recorded in the header)
     * @param counts    landing counts indexed by square, length {@value #NUM_SQUARES}
     * @throws IOException if the file cannot be written
     */
    private static void writeSnapshot(Strategy strategy, int runNumber, long n, long seed, long[] counts) throws IOException {
        String filename = String.format("data/strategy%s_run%02d_n%d.csv",
                label(strategy), runNumber, n);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {

            writer.write(String.format("# strategy=%s, run=%d, n=%d, seed=%d%n",
                    label(strategy), runNumber, n, seed));

            writer.write("index,name,count,percentage" + System.lineSeparator());

            for (int i = 0; i < NUM_SQUARES; i++) {
                writer.write(String.format("%d,%s,%d,%.4f%n",
                        i, Board.getName(i), counts[i], counts[i] * 100.0 / n));
            }
        }
    }

    /**
     * Returns the short label used in file names and reports for a strategy:
     * {@code "A"} for {@link Strategy#IMMEDIATE_EXIT}, {@code "B"} otherwise.
     *
     * @param s the strategy to label
     * @return {@code "A"} or {@code "B"}
     */
    private static String label(Strategy s) { return s == Strategy.IMMEDIATE_EXIT ? "A" : "B"; }

    /**
     * Runs the full experiment: 10 runs for each strategy, snapshotting at each
     * checkpoint and writing every snapshot to {@code data/}, then writes the
     * comparative summary.
     *
     * @param args ignored
     * @throws IOException if any snapshot or the summary cannot be written
     */
    public static void main(String[] args) throws IOException {
        long[] checkpoints = {1_000L, 10_000L, 100_000L, FINAL_N};

        // 1M snapshots retained per run so the Part 5 summary can compare strategies
        long[][] finalCountsA = new long[RUNS_PER_STRATEGY][];
        long[][] finalCountsB = new long[RUNS_PER_STRATEGY][];

        for (Strategy strategy : Strategy.values()) {
            long seedBase = (strategy == Strategy.IMMEDIATE_EXIT) ? 1000L : 2000L;

            for (int run = 1; run <= RUNS_PER_STRATEGY; run++) {
                long seed = seedBase + run;
                Simulation sim = new Simulation(strategy, seed);

                long turnsSoFar = 0;
                for (long target : checkpoints) {
                    sim.runTurns(target - turnsSoFar);   // run only the delta
                    turnsSoFar = target;

                    long[] snapshot = sim.getLandingCounts();
                    writeSnapshot(strategy, run, target, seed, snapshot);

                    if (target == FINAL_N) {
                        if (strategy == Strategy.IMMEDIATE_EXIT) {
                            finalCountsA[run - 1] = snapshot;
                        } else {
                            finalCountsB[run - 1] = snapshot;
                        }
                    }
                }

                System.out.printf("Finished strategy %s run %02d (seed %d)%n",
                        label(strategy), run, seed);
            }
        }

        System.out.println("All 80 datasets written to data/");

        writeSummary(finalCountsA, finalCountsB);
    }

    /**
     * Returns the indices of the five squares with the highest landing counts,
     * highest first.
     *
     * @param counts landing counts indexed by square, length {@value #NUM_SQUARES}
     * @return the five highest-count square indices, in descending order
     */
    private static int[] topN(long[] counts) {
        Integer[] idx = new Integer[NUM_SQUARES];
        for (int i = 0; i < NUM_SQUARES; i++) idx[i] = i;
        Arrays.sort(idx, (x, y) -> Long.compare(counts[y], counts[x]));

        int[] result = new int[5];
        for (int i = 0; i < 5; i++) result[i] = idx[i];
        return result;
    }

    /**
     * Returns the indices of the five squares with the highest values, highest
     * first. Used to rank squares by mean landing percentage.
     *
     * @param values per-square values indexed by square, length {@value #NUM_SQUARES}
     * @return the five highest-value square indices, in descending order
     */
    private static int[] topN(double[] values) {
        Integer[] idx = new Integer[NUM_SQUARES];
        for (int i = 0; i < NUM_SQUARES; i++) idx[i] = i;
        Arrays.sort(idx, (x, y) -> Double.compare(values[y], values[x]));

        int[] result = new int[5];
        for (int i = 0; i < 5; i++) result[i] = idx[i];
        return result;
    }

    /**
     * Mean landing percentage for each square, averaged across all runs.
     *
     * @param runs one landing-count array per run, each length {@value #NUM_SQUARES}
     * @return per-square mean landing percentages, length {@value #NUM_SQUARES}
     */
    private static double[] meanPercentages(long[][] runs) {
        double[] means = new double[NUM_SQUARES];
        for (long[] run : runs) {
            for (int i = 0; i < NUM_SQUARES; i++) {
                means[i] += run[i] * 100.0 / FINAL_N;
            }
        }
        for (int i = 0; i < NUM_SQUARES; i++) means[i] /= runs.length;
        return means;
    }

    /**
     * Spread (max minus min percentage) for each square across runs. This is the
     * within-strategy noise floor: any A-vs-B difference smaller than this is
     * indistinguishable from run-to-run variation.
     *
     * @param runs one landing-count array per run, each length {@value #NUM_SQUARES}
     * @return per-square percentage spread, length {@value #NUM_SQUARES}
     */
    private static double[] spreads(long[][] runs) {
        double[] spread = new double[NUM_SQUARES];
        for (int i = 0; i < NUM_SQUARES; i++) {
            double min = Double.MAX_VALUE;
            double max = -1;
            for (long[] run : runs) {
                double pct = run[i] * 100.0 / FINAL_N;
                min = Math.min(min, pct);
                max = Math.max(max, pct);
            }
            spread[i] = max - min;
        }
        return spread;
    }

    /**
     * Writes the comparative summary comparing the two strategies at 1M turns to
     * {@code data/summary.txt} (and echoes it to stdout).
     *
     * <p>The report covers the top-5 squares per strategy, the largest A-vs-B
     * differences flagged against the run-to-run noise floor, the jail-effect
     * squares called out by the assignment, and anomaly checks that the counts
     * sum correctly and that "Go to Jail" (square 30) is never landed on.
     *
     * @param runsA the ten 1M landing-count arrays for strategy A
     * @param runsB the ten 1M landing-count arrays for strategy B
     * @throws IOException if the summary file cannot be written
     */
    private static void writeSummary(long[][] runsA, long[][] runsB) throws IOException {
        double[] meanA = meanPercentages(runsA);
        double[] meanB = meanPercentages(runsB);
        double[] spreadA = spreads(runsA);
        double[] spreadB = spreads(runsB);

        StringBuilder out = new StringBuilder();
        out.append(String.format("COMPARATIVE RUN SUMMARY (n = %d, 10 runs per strategy)%n", FINAL_N));
        out.repeat("=", 70).append(System.lineSeparator());

        // which squares consistently rank in the top 5
        for (int s = 0; s < 2; s++) {
            long[][] runs = (s == 0) ? runsA : runsB;
            double[] mean = (s == 0) ? meanA : meanB;
            String name = (s == 0) ? "A (Immediate Exit)" : "B (Try for Doubles)";

            int[] appearances = new int[NUM_SQUARES];
            for (long[] run : runs) {
                for (int idx : topN(run)) appearances[idx]++;
            }

            out.append(String.format("%nTOP 5 SQUARES - STRATEGY %s%n", name));
            out.append(String.format("%-4s %-22s %10s %14s%n", "Idx", "Square", "Mean %", "Top-5 runs"));
            for (int idx : topN(mean)) {
                out.append(String.format("%-4d %-22s %10.4f %10d/10%n",
                        idx, Board.getName(idx), mean[idx], appearances[idx]));
            }
        }

        // do the two strategies converge to the same distribution?
        out.append(String.format("%n%s%nLARGEST A vs B DIFFERENCES%n", "=".repeat(70)));
        out.append(String.format("%-4s %-22s %9s %9s %9s %9s%n",
                "Idx", "Square", "A %", "B %", "Diff", "Noise"));

        Integer[] byDiff = new Integer[NUM_SQUARES];
        for (int i = 0; i < NUM_SQUARES; i++) byDiff[i] = i;
        final double[] fa = meanA, fb = meanB;
        Arrays.sort(byDiff, (x, y) ->
                Double.compare(Math.abs(fa[y] - fb[y]), Math.abs(fa[x] - fb[x])));

        for (int i = 0; i < 10; i++) {
            int idx = byDiff[i];
            double diff = meanA[idx] - meanB[idx];
            double noise = Math.max(spreadA[idx], spreadB[idx]);
            out.append(String.format("%-4d %-22s %9.4f %9.4f %9.4f %9.4f%s%n",
                    idx, Board.getName(idx), meanA[idx], meanB[idx], diff, noise,
                    Math.abs(diff) > noise ? "  <- exceeds noise" : ""));
        }

        // squares named in the assignment's jail-effect question
        out.append(String.format("%n%s%nJAIL-EFFECT SQUARES%n", "=".repeat(70)));
        out.append(String.format("%-4s %-22s %9s %9s %9s%n", "Idx", "Square", "A %", "B %", "Diff"));
        for (int idx : new int[]{10, 11, 24, 39}) {
            out.append(String.format("%-4d %-22s %9.4f %9.4f %9.4f%n",
                    idx, Board.getName(idx), meanA[idx], meanB[idx], meanA[idx] - meanB[idx]));
        }

        // sanity checks / anomalies
        out.append(String.format("%n%s%nANOMALY CHECKS%n", "=".repeat(70)));
        for (int s = 0; s < 2; s++) {
            long[][] runs = (s == 0) ? runsA : runsB;
            String label = (s == 0) ? "A" : "B";
            for (int r = 0; r < runs.length; r++) {
                long total = 0;
                for (long c : runs[r]) total += c;
                if (total != FINAL_N) {
                    out.append(String.format("FAIL: strategy %s run %d counts sum to %d, expected %d%n",
                            label, r + 1, total, FINAL_N));
                }
                if (runs[r][30] != 0) {
                    out.append(String.format("FAIL: strategy %s run %d has %d landings on Go to Jail%n",
                            label, r + 1, runs[r][30]));
                }
            }
        }
        out.append("All counts sum correctly and square 30 is empty unless noted above.")
                .append(System.lineSeparator());

        try (BufferedWriter w = new BufferedWriter(new FileWriter("data/summary.txt"))) {
            w.write(out.toString());
        }
        System.out.println(out);
    }

}
