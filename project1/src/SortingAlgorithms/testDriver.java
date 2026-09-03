package SortingAlgorithms;

/**
 * Runs all four sorting algorithms on static and random arrays,
 * displaying input, sorted output, algorithm name, n value, and comparison count.
 */
public class testDriver {

    /**
     * Checks whether the given array is sorted in non-decreasing order.
     *
     * @param arr the array to check
     * @return true if sorted, false otherwise
     */
    private static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++)
            if (arr[i] > arr[i + 1]) return false;
        return true;
    }

    /**
     * Returns a string representation of an integer array.
     *
     * @param arr the array to format
     * @return formatted string, e.g. "[3, 1, 4, 2]"
     */
    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    /**
     * Runs a single sorting test and prints the results.
     * Captures the input before sorting, runs the algorithm, then prints
     * the algorithm name, n value, input, output, comparison count, and pass/fail status.
     *
     * @param name      the name of the sorting algorithm
     * @param arr       the array to sort (modified in place by the algorithm)
     * @param algorithm the sorting algorithm to run, wrapped as a Runnable
     */
    private static void runTest(String name, int[] arr, Runnable algorithm) {
        String before = arrayToString(arr);
        algorithm.run();
        System.out.println("Algorithm   : " + name);
        System.out.println("n           : " + arr.length);
        System.out.println("Input       : " + before);
        System.out.println("Output      : " + arrayToString(arr));
        System.out.println("Comparisons : " + SortingAlgorithms.comparisons);
        System.out.println("Status      : " + (isSorted(arr) ? "PASS" : "FAIL"));
        System.out.println();
    }

    /**
     * Entry point for the test driver.
     * Runs all four sorting algorithms on static arrays of size 4, 6, and 8,
     * then on random arrays of size 10, printing results for each run.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        String[] names = { "Merge sort", "Quick sort", "Heap sort", "Shaker sort" };
        Runnable[] sorts;

        for (int size : new int[]{ 4, 6, 8 }) {
            System.out.println("======================================");
            System.out.println(" Static arrays  n = " + size);
            System.out.println("======================================");

            int[] arr1 = DataGenerator.generateStaticIntegerArray(size);
            int[] arr2 = DataGenerator.generateStaticIntegerArray(size);
            int[] arr3 = DataGenerator.generateStaticIntegerArray(size);
            int[] arr4 = DataGenerator.generateStaticIntegerArray(size);

            sorts = new Runnable[]{
                    () -> SortingAlgorithms.mergeSort(arr1),
                    () -> SortingAlgorithms.quickSort(arr2),
                    () -> SortingAlgorithms.heapSort(arr3),
                    () -> SortingAlgorithms.shakerSort(arr4)
            };

            for (int i = 0; i < 4; i++) {
                runTest(names[i], i == 0 ? arr1 : i == 1 ? arr2 : i == 2 ? arr3 : arr4, sorts[i]);
            }
        }

        System.out.println("======================================");
        System.out.println(" Random arrays  n = 10");
        System.out.println("======================================");

        int[] r1 = DataGenerator.generateRandomIntegerArray(10);
        int[] r2 = DataGenerator.generateRandomIntegerArray(10);
        int[] r3 = DataGenerator.generateRandomIntegerArray(10);
        int[] r4 = DataGenerator.generateRandomIntegerArray(10);

        sorts = new Runnable[]{
                () -> SortingAlgorithms.mergeSort(r1),
                () -> SortingAlgorithms.quickSort(r2),
                () -> SortingAlgorithms.heapSort(r3),
                () -> SortingAlgorithms.shakerSort(r4)
        };

        for (int i = 0; i < 4; i++) {
            runTest(names[i], i == 0 ? r1 : i == 1 ? r2 : i == 2 ? r3 : r4, sorts[i]);
        }
    }
}