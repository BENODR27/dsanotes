public class PeakElementFinder {

    public static int findPeakElement(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] > nums[mid + 1]) {
                // We are in a decreasing slope → peak is on the left or mid
                right = mid;
            } else {
                // We are in an increasing slope → peak is on the right
                left = mid + 1;
            }
        }

        return left; // or right; both are equal now
    }

    public static void main(String[] args) {
        int[] nums = { 1, 2, 1, 3, 5, 6, 4 };
        System.out.println("Peak found at index: " + findPeakElement(nums));
    }
}
