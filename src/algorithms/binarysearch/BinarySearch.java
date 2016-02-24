package algorithms.binarysearch;

import java.util.Arrays;

/**
 * this is a class which contains all kinds of functions related with binary search
 * 
 * @author moqiguzhu
 * @date 2016-02-24
 * @version 1.0
 */
public class BinarySearch {
  public boolean binarySearch(int[] sortedArr, int target) {
    return Arrays.binarySearch(sortedArr, target) >= 0;
  }

  public boolean binarySearch(int[] sortedArr, int left, int right, int target) {
    return Arrays.binarySearch(sortedArr, left, right, target) >= 0;
  }

  /**
   * 
   * @param sortedArr sorted array
   * @param left inclusive
   * @param right exclusive
   * @param target target element
   * @return return index that sortedArr[index] least great or equal than target. return
   *         sortedArr.length means that sortedArr[sortedArr.length-1] < target
   */
  public int IndexLeastGreatOrEqual(int[] sortedArr, int left, int right, int target) {
    assert(right > left);
    while (right > left) {
      int mid = ((right - left) >> 1) + left;
      if (target <= sortedArr[mid]) {
        right = mid;
      } else {
        left = mid + 1;
      }
    }

    return left;
  }

  /**
   * 
   * @param sortedArr sortedArray
   * @param left inclusive
   * @param right exclusive
   * @param target target element
   * @return return index that sortedArr[index] most less or equal than target. return -1 means
   *         sortedArr[0] > target
   */
  public int IndexMostLessOrEqual(int[] sortedArr, int left, int right, int target) {
    assert(right > left);
    while (left < right) {
      int mid = ((right - left) >> 1) + left;
      if (target < sortedArr[mid]) {
        right = mid;
      } else {
        left = mid + 1;
      }
    }
    return --left;
  }

  public static void main(String[] args) {
    BinarySearch bs = new BinarySearch();

    int[] nums1 = new int[] {1, 3, 5, 5, 5, 7, 9};
    System.out.println(bs.IndexLeastGreatOrEqual(nums1, 0, 7, 10));
  }
}
