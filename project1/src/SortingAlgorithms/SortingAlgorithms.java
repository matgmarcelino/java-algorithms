package SortingAlgorithms;

/**
 *  This class contains implementations of multiple sorting algorithms
 *  including Merge sort, Quick sort, Heap Sort, and Shaker sort.
 */
public class SortingAlgorithms {

    /**
     * Tracks the number of element comparisons made during the most recent sort.
     * */
    public static long comparisons = 0;

    /**
     * Resets the comparison counter to zero.
     * Call this before each sort to get an accurate count for that run.
     */
    public static void resetComparisons() {
        comparisons = 0;
    }

    /**
     * Sorts the given array in ascending order using merge sort.
     * This is the public entry point, and callers don't need to know
     * about indices.
     *
     * @param arr the array of integers to sort (modified in place)
     */
    public static void mergeSort(int[] arr) {
        resetComparisons();
        mergeSort(arr, 0, arr.length - 1);
    }

    /**
     * Recursively divides the subarray into two halves,
     * sorts each half, then merges them back together in sorted order.
     *
     * @param arr   the array containing the subarray to sort
     * @param left  the starting index of the subarray (inclusive)
     * @param right the ending index of the subarray (inclusive)
     */
    private static void mergeSort(int[] arr, int left, int right) {
        // base case: if the section is of size 0 or 1
        if (left >= right) return;

        // find midpoint and split into two halves
        int mid = (left + right) / 2;

        // recursively sort left half
        mergeSort(arr, left, mid);

        // recursively sort right half
        mergeSort(arr, mid + 1, right);

        // both halves sorted, merge them back
        merge(arr, left, mid, right);
    }

    /**
     * Merges two adjacent sorted subarrays into a single sorted subarray in place.
     * Uses a temporary array to hold the merged result before copying back.
     *
     * @param arr   the array containing both sorted subarrays
     * @param left  the starting index of the left subarray (inclusive)
     * @param mid   the ending index of the left subarray (inclusive)
     * @param right the ending index of the right subarray (inclusive)
     */
    private static void merge(int[] arr, int left, int mid, int right) {
        // temp array to hold merged result
        int[] temp = new int[right - left + 1];

        int i = left;    // pointer for the left half
        int j = mid + 1; // pointer for the right half
        int k = 0;       // pointer for temp arr

        // compare ints from both halves and place smaller into temp arr
        while (i <= mid && j <= right) {
            comparisons++;
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++]; // left int is smaller
            } else {
                temp[k++] = arr[j++]; // right int is smaller
            }
        }

        // if any elements remain in the left half, dump into temp
        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        // if any elements remain in the right half, dump into temp
        while (j <= right) {
            temp[k++] = arr[j++];
        }

        // copy the sorted temp array back into the original array
        System.arraycopy(temp, 0, arr, left, temp.length);
    }

    /**
     * Sorts the given array in ascending order using quick sort.
     * Public entry point, callers don't need to know about indices.
     *
     * @param arr the array of integers to sort
     */
    public static void quickSort(int[] arr) {
        resetComparisons();
        quickSort(arr, 0, arr.length - 1);
    }

    /**
     * Recursively partitions and sorts the subarray.
     * The pivot is placed in its final sorted position,
     * then both sides are sorted independently.
     *
     * @param arr  the array containing the subarray to sort
     * @param low  the starting index of the subarray
     * @param high the ending index of the subarray
     */
    private static void quickSort(int[] arr, int low, int high) {
        // base case: subarray of size 0 or 1 is already sorted
        if (low >= high) return;

        // partition the array and get the pivot's final index
        int pivotIndex = partition(arr, low, high);

        // recursively sort everything left of the pivot
        quickSort(arr, low, pivotIndex - 1);

        // recursively sort everything right of the pivot
        quickSort(arr, pivotIndex + 1, high);
    }

    /**
     * Partitions array around the last element as pivot.
     * All elements smaller than or equal to the pivot end up on the left,
     * all elements bigger than pivot end up on the right.
     * The pivot is placed in its final sorted position.
     *
     * @param arr  the array to partition
     * @param low  the starting index of the subarray
     * @param high the ending index of the subarray
     * @return the final index of the pivot after partitioning
     */
    private static int partition(int[] arr, int low, int high) {
        // use last element as pivot
        int pivot = arr[high];

        // i tracks the boundary between smaller than pivot and larger than pivot
        int i = low - 1;

        // j scans every element except the pivot itself
        for (int j = low; j < high; j++) {
            comparisons++;
            if (arr[j] <= pivot) {
                // found something that belongs on the left
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // place the pivot after the last smaller element
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    /**
     * Sorts the given array in ascending order using heap sort.
     * Operates in two phases: build a max-heap, then repeatedly
     * extract the maximum element into its final sorted position.
     *
     * @param arr the array of integers to sort (modified in place)
     */
    public static void heapSort(int[] arr) {
        resetComparisons();
        int n = arr.length;

        // phase 1: build max-heap
        // start at the last non-leaf node and heapify downward to index 0
        // leaves are already valid heaps so we skip them
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // phase 2: extract elements from the heap one by one
        for (int i = n - 1; i > 0; i--) {
            // arr[0] is the current max: swap it to its final sorted position
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // heap is now one element smaller; restore the heap property from root
            heapify(arr, i, 0);
        }
    }

    /**
     * Restores the max-heap property at index i in arr[].
     * Assumes both subtrees rooted at the children of i are already valid
     * max-heaps. Pushes arr[i] downward until it is larger than both children.
     *
     * @param arr      the array representing the heap
     * @param heapSize the number of elements currently in the heap
     * @param i        the index to heapify downward from
     */
    private static void heapify(int[] arr, int heapSize, int i) {
        // assume the current node is the largest
        int largest = i;

        // index of left and right children using heap index math
        int left  = 2 * i + 1;
        int right = 2 * i + 2;

        // check if left child exists and is larger than current largest
        if (left < heapSize) {
            comparisons++;
            if (arr[left] > arr[largest]) largest = left;
        }

        // check if right child exists and is larger than current largest
        if (right < heapSize) {
            comparisons++;
            if (arr[right] > arr[largest]) largest = right;
        }

        // if the largest isn't the current node, swap and continue pushing down
        if (largest != i) {
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;

            // recursively heapify the subtree that was affected by the swap
            heapify(arr, heapSize, largest);
        }
    }

    /**
     * Sorts the given array in ascending order using shaker sort.
     * Each cycle makes one left-to-right pass to place the largest unsorted element,
     * then one right-to-left pass to place the smallest unsorted element.
     * Boundaries shrink from both ends after each half-pass.
     * Exits early if a full cycle completes with no swaps, meaning the array is already sorted.
     *
     * @param arr the array of integers to sort (modified in place)
     */
    public static void shakerSort(int[] arr) {
        resetComparisons();
        int left  = 0;
        int right = arr.length - 1;
        boolean swapped;

        while (left < right) {
            swapped = false;

            // --- left to right pass ---
            // bubbles the largest unsorted element to arr[right]
            for (int i = left; i < right; i++) {
                comparisons++;
                if (arr[i] > arr[i + 1]) {
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    swapped = true;
                }
            }
            right--;

            // --- right to left pass ---
            // bubbles the smallest unsorted element to arr[left]
            for (int i = right; i > left; i--) {
                comparisons++;
                if (arr[i] < arr[i - 1]) {
                    int temp = arr[i];
                    arr[i] = arr[i - 1];
                    arr[i - 1] = temp;
                    swapped = true;
                }
            }
            left++;

            // if neither pass made a swap, array is sorted -- no point continuing
            if (!swapped) break;
        }
    }
}