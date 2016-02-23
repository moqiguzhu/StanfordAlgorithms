package algorithms.binarysearch;

import java.util.Arrays;

// this is a class which contains all kinds of functions related with binary search
public class BinarySearch {
  public boolean binarySearch(int[] sortedArr, int target) {
    return Arrays.binarySearch(sortedArr, target) >= 0;
  }
  
  // left: inclusive
  // right: exclusive
  public boolean binarySearch(int[] sortedArr, int left, int right, int target) {
    return Arrays.binarySearch(sortedArr, left, right, target) >= 0;
  }
  
  // return index that most less or equal than target
  // x >= target least
  // left: inclusive
  // right: exclusive
  public int IndexMostLessOrEqual(int[] sortedArr, int left, int right, int target) {
    assert(right > left);
    while(right > left) {
      int mid = ((right - left) >> 1) + left;
      if(target <= sortedArr[mid]) {
        right = mid;
      } else {
        left = mid + 1;
      }
    }
    // right is the index that sortedArr[right] < target most
    
    return left;
  }
  
  // return index that least great or equal than target
  // x <= target most
  // left: inclusive
  // right: exclusive
  public int IndexLeastGreatOrEqual(int[] sortedArr, int left, int right, int target) {
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
    
    int[] nums1 = new int[]{1,3,5,5,5,7,9};
    System.out.println(bs.IndexMostLessOrEqual(nums1, 0, 7, 5));
  }
}
