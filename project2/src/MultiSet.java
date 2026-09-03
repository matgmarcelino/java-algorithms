/*
 * Implements multiset (bag) operations (union, intersection, difference,
 * sum) over a collection where elements may appear more than once, using a
 * HashMap to track the count of each element.
 */

import java.util.HashMap;

/**
 * Represents a multiset (bag) of college names, where each college may appear
 * more than once. The number of times a college appears is called its
 * multiplicity or count.
 *
 * <p>Unlike an ordinary set, a multiset tracks how many times each element
 * occurs. For example, a multiset built from
 * {@code {"SLCC", "SLCC", "BYU"}} stores SLCC with count 2 and BYU with
 * count 1.</p>
 *
 * <p>Provides the standard multiset operations: union, intersection,
 * difference, and sum.</p>
 */
public class MultiSet {

    /**
     * Maps each college name to its count (multiplicity) in this multiset.
     * A college that is absent from the map has an implied count of zero.
     */
    private final HashMap<String, Integer> membership;

    /**
     * Constructs an empty multiset containing no colleges.
     */
    public MultiSet() {
        this.membership = new HashMap<>();
    }

    /**
     * Constructs a multiset from an array of college names. Duplicate entries
     * in {@code names} increase the count of that college rather than being
     * ignored.
     *
     * <p>For example, passing {@code {"SLCC", "SLCC", "BYU"}} produces a
     * multiset where SLCC has count 2 and BYU has count 1.</p>
     *
     * @param names the college names to add; duplicates increase the count
     */
    public MultiSet(String[] names) {
        this.membership = new HashMap<>(names.length);
        for (String n : names) {
            // merge: if name absent, insert with value 1;
            // if present, apply Integer::sum to add 1 to the existing count.
            this.membership.merge(n, 1, Integer::sum);
        }
    }

    /**
     * Constructs a multiset as a shallow copy of an existing membership map.
     * Used internally by operations that start from a copy of one of the
     * operands.
     *
     * @param otherMembership the map to copy counts from
     */
    public MultiSet(HashMap<String, Integer> otherMembership) {
        this.membership = new HashMap<>(otherMembership);
    }

    /**
     * Returns the multiset union of this multiset and {@code other}: a new
     * multiset where each college's count is the maximum of its count in the
     * two operands.
     *
     * <p>Example: if A = {SLCC: 3, BYU: 1} and B = {SLCC: 1, Weber: 2},
     * then A union B = {SLCC: 3, BYU: 1, Weber: 2}.</p>
     *
     * @param other the multiset to union with this multiset
     * @return the multiset union of the two multisets
     */
    public MultiSet union(MultiSet other) {
        // start with a copy of this multiset, then merge each entry from other
        MultiSet unionSet = new MultiSet(this.membership);
        other.membership.forEach((name, count) -> {
            // merge: if name absent, insert other's count;
            // if present, keep the larger of the two counts.
            unionSet.membership.merge(name, count, Integer::max);
        });
        return unionSet;
    }

    /**
     * Returns the multiset intersection of this multiset and {@code other}: a
     * new multiset containing only colleges present in both, with each count
     * set to the minimum of the two counts.
     *
     * <p>Example: if A = {SLCC: 3, BYU: 1} and B = {SLCC: 1, Weber: 2},
     * then A intersection B = {SLCC: 1}.</p>
     *
     * @param other the multiset to intersect with this multiset
     * @return the multiset intersection of the two multisets
     */
    public MultiSet intersection(MultiSet other) {
        MultiSet intersectionSet = new MultiSet();
        // only visit colleges in this multiset; colleges only in other are
        // automatically excluded since we never add them.
        membership.forEach((name, count) -> {
            if (other.membership.containsKey(name)) {
                int min = Math.min(count, other.membership.get(name));
                // a zero min means neither set truly contains this college,
                // so skip it to keep the result clean.
                if (min > 0)
                    intersectionSet.membership.put(name, min);
            }
        });
        return intersectionSet;
    }

    /**
     * Returns the multiset difference of this multiset and {@code other}
     * (A minus B): a new multiset where each college's count is this
     * multiset's count minus {@code other}'s count, but never below zero.
     * Colleges whose count would reach zero are omitted entirely.
     *
     * <p>Example: if A = {SLCC: 3, BYU: 1} and B = {SLCC: 1, Weber: 2},
     * then A difference B = {SLCC: 2, BYU: 1}.</p>
     *
     * @param other the multiset to subtract from this multiset
     * @return the multiset difference (A minus B), floored at zero
     */
    public MultiSet difference(MultiSet other) {
        MultiSet diffSet = new MultiSet();
        this.membership.forEach((name, count) -> {
            int otherCount = other.membership.getOrDefault(name, 0);
            int result = count - otherCount;   // A's count minus B's count
            // only include colleges with a positive remaining count
            if (result > 0)
                diffSet.membership.put(name, result);
        });
        return diffSet;
    }

    /**
     * Returns the multiset sum of this multiset and {@code other}: a new
     * multiset where each college's count is the sum of its counts in both
     * operands. Colleges present in only one operand appear with that
     * operand's count.
     *
     * <p>Example: if A = {SLCC: 3, BYU: 1} and B = {SLCC: 1, Weber: 2},
     * then A sum B = {SLCC: 4, BYU: 1, Weber: 2}.</p>
     *
     * @param other the multiset to add to this multiset
     * @return the multiset sum of the two multisets
     */
    public MultiSet sum(MultiSet other) {
        MultiSet sumSet = new MultiSet();
        // first pass: add counts for every college in this multiset,
        // contributing other's count (defaulting to 0 if absent).
        membership.forEach((name, thisCount) -> {
            int otherCount = other.membership.getOrDefault(name, 0);
            sumSet.membership.put(name, thisCount + otherCount);
        });
        // second pass: add colleges that are only in other, since the first
        // pass only visited colleges in this multiset.
        other.membership.forEach((name, count) -> {
            if (!sumSet.membership.containsKey(name))
                sumSet.membership.put(name, count);
        });
        return sumSet;
    }

    /**
     * Returns a human-readable listing of this multiset's contents, one
     * college per line formatted as {@code "name count"}.
     *
     * @return a multi-line string of college names and their counts
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.membership.forEach((name, count) -> {
            sb.append(name).append(' ').append(count).append("\n");
        });
        return sb.toString();
    }
}