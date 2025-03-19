
class Solution {
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0)
            return 0;

        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;

        for (int price : prices) {
            // Update the minimum price found so far
            if (price < minPrice) {
                minPrice = price;
            }
            // Calculate the profit and update maxProfit if it's greater
            else {
                maxProfit = Math.max(maxProfit, price - minPrice);
            }
        }
        return maxProfit;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();

        int[] prices1 = { 7, 1, 5, 3, 6, 4 };
        System.out.println(solution.maxProfit(prices1)); // Output: 5

        int[] prices2 = { 7, 6, 4, 3, 1 };
        System.out.println(solution.maxProfit(prices2)); // Output: 0

        int[] prices3 = { 2, 4, 1 };
        System.out.println(solution.maxProfit(prices3)); // Output: 2
    }

}
