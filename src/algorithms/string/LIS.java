package algorithms.string;

import java.util.TreeSet;

/**
 * 第一种解法巧妙地使用了Java中的TreeSet这种数据结构。来自Stack Overflow
 * http://stackoverflow.com/questions/22970883/lis-longest-increasing-subsequence-algorithm
 * 第二种解法来自geeksforgeeks
 * http://www.geeksforgeeks.org/longest-monotonically-increasing-subsequence-size-n-log-n/
 * 
 * @author moqiguzhu
 * @date 2015-12-10
 * @version 1.0
 */

public class LIS {
  public int computeLIS(int[] nums) {
    // 机智
    TreeSet<Integer> set = new TreeSet<Integer>();
    for (int i = 0; i < nums.length; i++) {
      Integer ceil = set.ceiling(nums[i]);
      if (ceil == null) // if ceil not present this simply extends the current sequence
        set.add(nums[i]);
      else { // replace ceil with this value
        set.remove(ceil);
        set.add(nums[i]);
      }
    }
    return set.size();
  }

  public int CeilIndex(int A[], int l, int r, int key) {
    while (r - l > 1) {
      int m = l + (r - l) / 2;
      if (A[m] >= key)
        r = m;
      else
        l = m;
    }

    return r;
  }

  public int LongestIncreasingSubsequenceLength(int A[]) {
    // Add boundary case, when array size is one

    int size = A.length;
    int[] tailTable = new int[size];
    int len; // always points empty slot

    tailTable[0] = A[0];
    len = 1;
    for (int i = 1; i < size; i++) {
      if (A[i] < tailTable[0])
        // new smallest value
        tailTable[0] = A[i];

      else
        if (A[i] > tailTable[len - 1])
          // A[i] wants to extend largest subsequence
          tailTable[len++] = A[i];

      else
          // A[i] wants to be current end candidate of an existing
          // subsequence. It will replace ceil value in tailTable
          tailTable[CeilIndex(tailTable, -1, len - 1, A[i])] = A[i];
    }

    return len;
  }
}
