/*
 * Driver that runs all Part 1 (ordinary set) and Part 2 (multiset)
 * operations across multiple test cases and prints labeled results.
 */

import java.util.HashMap;

/**
 * Entry point for the sets-and-multisets project.
 *
 * <p>Runs three Part 1 test cases (ordinary sets via bit strings / boolean
 * arrays) and two Part 2 test cases (multisets / bags), printing clearly
 * labeled output for every operation in both parts.</p>
 *
 * <p>Test cases:
 * <ul>
 *   <li>Part 1 Case 1 – typical overlap: A and B share some colleges</li>
 *   <li>Part 1 Case 2 – nested: B is a strict subset of A</li>
 *   <li>Part 1 Case 3 – edge: B is the empty set</li>
 *   <li>Part 2 Case 1 – representative: both bags have multiple high-count elements</li>
 *   <li>Part 2 Case 2 – edge: B's count exceeds A's for some elements,
 *       verifying the floor-at-zero behavior in difference</li>
 * </ul>
 * </p>
 */
public class Main {

    public static void main(String[] args) {
        runPart1();
        runPart2();
    }

    /**
     * Runs all three Part 1 test cases and prints every set operation.
     */
    private static void runPart1() {
        printBanner("PART 1 - ORDINARY SETS (Bit String / Boolean Array)");

        // Case 1: typical – A and B overlap on several colleges
        printSubHeader("Case 1: Typical Overlap");
        BitStringSet a1 = new BitStringSet(new String[]{
                "SLCC", "University of Utah", "Brigham Young",
                "Utah Tech", "Snow College"
        });
        BitStringSet b1 = new BitStringSet(new String[]{
                "University of Utah", "Weber State",
                "SLCC", "Ensign", "Snow College"
        });
        runSetOperations(a1, b1);

        // Case 2: nested – B is a strict subset of A
        printSubHeader("Case 2: Nested (B is a Subset of A)");
        BitStringSet a2 = new BitStringSet(new String[]{
                "SLCC", "University of Utah", "Brigham Young",
                "Weber State", "Westminster"
        });
        BitStringSet b2 = new BitStringSet(new String[]{
                "SLCC", "Brigham Young"
        });
        runSetOperations(a2, b2);

        // Case 3: edge – B is the empty set
        // Expected highlights: NOT(A) = full complement, A union B = A,
        // A intersect B = empty, A - B = A, symmetric diff = A
        printSubHeader("Case 3: Edge – B is the Empty Set");
        BitStringSet a3 = new BitStringSet(new String[]{
                "SLCC", "University of Utah", "Brigham Young"
        });
        BitStringSet b3 = new BitStringSet(); // empty
        runSetOperations(a3, b3);
    }

    /**
     * Prints both set inputs and all five operations for a given A and B.
     *
     * @param a the first operand set
     * @param b the second operand set
     */
    private static void runSetOperations(BitStringSet a, BitStringSet b) {
        System.out.println("Set A:");
        printSet(a);

        System.out.println("Set B:");
        printSet(b);

        System.out.println("NOT(A) - Complement of A:");
        printSet(a.complement());

        System.out.println("A \u222A B - Union:");
        printSet(a.union(b));

        System.out.println("A \u2229 B - Intersection:");
        printSet(a.intersection(b));

        System.out.println("A - B - Difference:");
        printSet(a.difference(b));

        System.out.println("A \u2295 B - Symmetric Difference:");
        printSet(a.symmetricDifference(b));
    }

    /**
     * Prints a single set's bit string and element listing.
     *
     * @param set the set to display
     */
    private static void printSet(BitStringSet set) {
        System.out.println("  Bit String : " + set.toBitString());
        System.out.println("  Elements   : " + set.toElementListing());
        System.out.println();
    }

    /**
     * Runs both Part 2 test cases and prints every multiset operation.
     */
    private static void runPart2() {
        printBanner("PART 2 - MULTISETS (Bags)");

        // Case 1: representative – both bags have at least two elements
        // with multiplicity > 1, and A and B share some colleges.
        // A: University of Utah x3, SLCC x2, Weber State x2, Brigham Young x1
        // B: SLCC x3, Snow College x2, University of Utah x1, Western Governors x1
        printSubHeader("Case 1: Representative (Shared Elements, High Counts)");
        MultiSet ma1 = new MultiSet(new String[]{
                "University of Utah", "University of Utah", "University of Utah",
                "SLCC", "SLCC",
                "Weber State", "Weber State",
                "Brigham Young"
        });
        MultiSet mb1 = new MultiSet(new String[]{
                "SLCC", "SLCC", "SLCC",
                "Snow College", "Snow College",
                "University of Utah",
                "Western Governors"
        });
        runMultiSetOperations(ma1, mb1);

        /*
        Case 2: edge – for some elements B's count exceeds A's count,
        verifying that difference floors at zero rather than going negative,
        and that those elements are excluded from the result entirely.
        A: SLCC x2, Weber State x2, Brigham Young x1
        B: SLCC x4, Weber State x2, Snow College x2
        Expected in A-B: SLCC excluded (2-4<0), Weber State excluded (2-2=0),
        Brigham Young kept at 1 (1-0=1)
         */
        printSubHeader("Case 2: Edge – B Count Exceeds A Count (Floor-at-Zero in Difference)");
        MultiSet ma2 = new MultiSet(new String[]{
                "SLCC", "SLCC",
                "Weber State", "Weber State",
                "Brigham Young"
        });
        MultiSet mb2 = new MultiSet(new String[]{
                "SLCC", "SLCC", "SLCC", "SLCC",
                "Weber State", "Weber State",
                "Snow College", "Snow College"
        });
        runMultiSetOperations(ma2, mb2);
    }

    /**
     * Prints both multiset inputs and all four operations for a given A and B.
     *
     * @param a the first operand multiset
     * @param b the second operand multiset
     */
    private static void runMultiSetOperations(MultiSet a, MultiSet b) {
        System.out.println("Multiset A:");
        printMultiSet(a);

        System.out.println("Multiset B:");
        printMultiSet(b);

        System.out.println("A \u222A B - Multiset Union (max counts):");
        printMultiSet(a.union(b));

        System.out.println("A \u2229 B - Multiset Intersection (min counts):");
        printMultiSet(a.intersection(b));

        System.out.println("A - B - Multiset Difference (floor at 0):");
        printMultiSet(a.difference(b));

        System.out.println("A + B - Multiset Sum (add counts):");
        printMultiSet(a.sum(b));
    }

    /**
     * Prints each college and its count from the given multiset.
     *
     * @param ms the multiset to display
     */
    private static void printMultiSet(MultiSet ms) {
        System.out.print(ms.toString().indent(2));
        System.out.println();
    }

    /**
     * Prints a full-width section banner.
     * */
    private static void printBanner(String title) {
        String line = "=".repeat(60);
        System.out.println("\n" + line);
        System.out.println("  " + title);
        System.out.println(line + "\n");
    }

    /**
     * Prints a lighter sub-section header.
     * */
    private static void printSubHeader(String title) {
        System.out.println("--- " + title + " ---\n");
    }
}