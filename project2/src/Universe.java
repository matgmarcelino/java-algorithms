/*
 * Defines the fixed universe of colleges shared by BitStringSet and MultiSet.
 * Both set classes read their element list from here, so the universe lives
 * in exactly one place.
 */

/**
 * Defines the fixed universe of colleges shared by the set classes in this
 * project. Each index in the returned array corresponds to the same bit
 * position in a {@link BitStringSet}'s membership bit string.
 */
public class Universe {

    /** The fixed, ordered list of colleges that make up the universe. */
    private static final String[] COLLEGES = {
            "SLCC", "University of Utah", "Utah State", "Weber State",
            "Utah Tech", "Brigham Young", "Westminster", "Ensign",
            "Utah Valley", "Southern Utah", "Snow College", "Western Governors",
    };

    /**
     * Returns the colleges that make up the universe, in their canonical order.
     *
     * @return the array of college names
     */
    public static String[] getCOLLEGES() {
        return COLLEGES;
    }
}
