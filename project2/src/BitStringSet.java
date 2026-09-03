/*
 * Implements ordinary set operations (complement, union, intersection,
 * difference, symmetric difference) over a fixed universe of colleges, using
 * a boolean array as the bit-string representation.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a set of colleges drawn from a fixed universe (the colleges
 * supplied by {@link Universe}), implemented as a bit string in which each
 * position records whether the corresponding college is a member of the set.
 *
 * <p>Provides the standard set operations: complement, union, intersection,
 * difference, and symmetric difference.</p>
 */
public class BitStringSet {
    /** The fixed universe of colleges, obtained from {@link Universe#getCOLLEGES()}. */
    private static final String[] COLLEGES = Universe.getCOLLEGES();

    /** Membership flags; {@code membership[i]} is true when {@code COLLEGES[i]} is in this set. */
    private final boolean[] membership;

    /**
     * Constructs an empty set containing none of the colleges.
     */
    public BitStringSet() {
        this.membership = new boolean[COLLEGES.length];
    }

    /**
     * Constructs a set containing the given colleges.
     *
     * <p>Each entry in {@code names} that exactly matches a college in the
     * {@linkplain Universe#getCOLLEGES() universe} (case-sensitive) is added to
     * the set; entries that do not match any known college are ignored.</p>
     *
     * @param names the names of the colleges to include in the set
     */
    public BitStringSet(String[] names) {
        this.membership = new boolean[COLLEGES.length];
        for (int i = 0; i < COLLEGES.length; i++) {
            for (String c : names) {
                if (Objects.equals(c, COLLEGES[i])) {
                    membership[i] = true;
                    break;
                }
            }
        }
    }

    /**
     * Returns the bit string representation of this set, one character per
     * college in the {@linkplain Universe#getCOLLEGES() universe}: {@code '1'}
     * if the college is a member, {@code '0'} otherwise.
     *
     * @return a string of {@code '0'} and {@code '1'} characters
     */
    public String toBitString() {
        StringBuilder sb = new StringBuilder(membership.length);
        for (boolean b : membership) {
            if (b) sb.append('1');
            else sb.append('0');
        }
        return sb.toString();
    }

    /**
     * Returns a human-readable listing of the colleges in this set, formatted as
     * a comma-separated list enclosed in braces, e.g. {@code "{ SLCC, Utah Tech }"}.
     *
     * @return the set's members listed in {@linkplain Universe#getCOLLEGES() universe} order
     */
    public String toElementListing() {
        List<String> present = new ArrayList<>();
        for (int i = 0; i < membership.length; i++) {
            if (membership[i]) {
                present.add(COLLEGES[i]);
            }
        }
        return "{ " + String.join(", ", present) + " }";
    }

    /**
     * Returns the complement of this set: a new set containing exactly the
     * colleges that are <em>not</em> members of this set.
     *
     * @return the complement of this set
     */
    public BitStringSet complement() {
        BitStringSet complement = new BitStringSet();
        for (int i = 0; i < membership.length; i++) {
            complement.membership[i] = !membership[i];
        }
        return complement;
    }

    /**
     * Returns the union of this set and {@code other}: a new set containing every
     * college that is a member of either set.
     *
     * @param other the set to union with this set
     * @return the union of the two sets
     */
    public BitStringSet union(BitStringSet other) {
        BitStringSet union = new BitStringSet();
        for (int i = 0; i < membership.length; i++) {
            union.membership[i] = membership[i] || other.membership[i];
        }
        return union;
    }

    /**
     * Returns the intersection of this set and {@code other}: a new set
     * containing only the colleges that are members of both sets.
     *
     * @param other the set to intersect with this set
     * @return the intersection of the two sets
     */
    public BitStringSet intersection(BitStringSet other) {
        BitStringSet intersection = new BitStringSet();
        for (int i = 0; i < membership.length; i++) {
            intersection.membership[i] = membership[i] && other.membership[i];
        }
        return intersection;
    }

    /**
     * Returns the difference of this set and {@code other}: a new set containing
     * the colleges that are members of this set but not of {@code other}.
     *
     * @param other the set whose members are removed from this set
     * @return the difference of the two sets
     */
    public BitStringSet difference(BitStringSet other) {
        BitStringSet diff = new BitStringSet();
        for (int i = 0; i < membership.length; i++) {
            diff.membership[i] = membership[i] && !other.membership[i];
        }
        return diff;
    }

    /**
     * Returns the symmetric difference of this set and {@code other}: a new set
     * containing the colleges that are members of exactly one of the two sets.
     *
     * @param other the set to compute the symmetric difference with
     * @return the symmetric difference of the two sets
     */
    public BitStringSet symmetricDifference(BitStringSet other) {
        BitStringSet symmetricDiff = new BitStringSet();
        for (int i = 0; i < membership.length; i++) {
            symmetricDiff.membership[i] = membership[i] ^ other.membership[i];
        }
        return symmetricDiff;
    }
}
