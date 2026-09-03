package SortingAlgorithms;
import java.util.Random;

/**
 * This class generates data to be modified
 * by the sorting algorithms
 */
public class DataGenerator {

    private static final Random random = new Random();

    /**
     * Generates an array of the given size filled with random integers.
     * Values are drawn from the full int range using {@link Random#nextInt()}.
     *
     * @param size the number of elements in the returned array
     * @return a new int array of length {@code size} with random values
     */
    public static int[] generateRandomIntegerArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt();
        }
        return arr;
    }

    /**
     * Returns a predefined static array of the given size for repeatable testing.
     * The same input is returned every time for a given size, making it useful
     * for verifying sorting correctness against a known expected output.
     * <p>
     * Supported sizes and their corresponding arrays:
     * <ul>
     *   <li>4 → [3, 1, 4, 2]</li>
     *   <li>6 → [4, 1, 2, 5, 6, 3]</li>
     *   <li>8 → [2, 7, 1, 3, 8, 6, 4, 5]</li>
     * </ul>
     *
     * @param size the desired array size; must be 4, 6, or 8
     * @return a new int array of length {@code size} with predefined values
     * @throws IllegalArgumentException if {@code size} is not 4, 6, or 8
     */
    public static int[] generateStaticIntegerArray(int size) {
        if (size == 4) return new int[] { 3, 1, 4, 2 };
        if (size == 6) return new int[] { 4, 1, 2, 5, 6, 3 };
        if (size == 8) return new int[] { 2, 7, 1, 3, 8, 6, 4, 5 };
        else throw new IllegalArgumentException("Size has to be 4, 6, or 8.");
    }
}