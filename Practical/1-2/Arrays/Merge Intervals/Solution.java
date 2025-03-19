
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {

    public static int[][] merge(int[][] intervals) {
        if (intervals.length <= 1) {
            return intervals;
        }

        // Step 1: Sort intervals based on the start time
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> mergedList = new ArrayList<>();

        // Step 2: Iterate through intervals and merge overlapping ones
        int[] currentInterval = intervals[0];
        mergedList.add(currentInterval);

        for (int[] interval : intervals) {
            int currentEnd = currentInterval[1];
            int nextStart = interval[0];
            int nextEnd = interval[1];

            if (currentEnd >= nextStart) {
                // Merge intervals
                currentInterval[1] = Math.max(currentEnd, nextEnd);
            } else {
                // No overlap, move to the next interval
                currentInterval = interval;
                mergedList.add(currentInterval);
            }
        }

        return mergedList.toArray(new int[mergedList.size()][]);
    }

    public static void main(String[] args) {
        int[][] intervals1 = { { 1, 3 }, { 2, 6 }, { 8, 10 }, { 15, 18 } };
        int[][] result1 = merge(intervals1);
        System.out.println(Arrays.deepToString(result1)); // Output: [[1,6],[8,10],[15,18]]

        int[][] intervals2 = { { 1, 4 }, { 4, 5 } };
        int[][] result2 = merge(intervals2);
        System.out.println(Arrays.deepToString(result2)); // Output: [[1,5]]
    }
}