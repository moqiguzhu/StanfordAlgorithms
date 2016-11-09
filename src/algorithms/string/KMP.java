package algorithms.string;

/**
 * Knuth-Morris-Pratt字符串匹配算法的实现
 * 
 * @author moqiguzhu
 * @version 1.0
 * @date 2015-09-21
 */
public class KMP {
  /**
   * 有点动态规划的意思在里面。 KMP和LIS好像。都是动态规划 + 一个辅助数组。
   * 
   * @param target 目标串
   * @param pattern 模式串
   */
  public void solveKMP(String target, String pattern) {
    int[] prefixes = computePrefix(pattern);
    int k = 0;
    for (int i = 0; i < target.length(); i++) {
      while (k > 0 && pattern.charAt(k) != target.charAt(i)) {
        k = prefixes[k-1];
      }
      if (pattern.charAt(k) == target.charAt(i)) {
        k++;
      }
      if (k == pattern.length()) {
        System.out.println("valid shift is: " + (i - pattern.length() + 1));
        k = prefixes[k - 1];
      }
    }
  }

  /**
   * 前缀函数的实现 prefixes[i]的语义是从开始到下标为i为止的子串中最长的前后缀串的长度 这里的前后缀串是关键。
   * 前后缀串的长度为n，串的前n个字符和串的后n个字符完全一致。
   * 
   * 
   * @param pattern 模式串
   * @return 模式串的前缀信息
   */
  public int[] computePrefix(String pattern) {
    int[] prefixes = new int[pattern.length()];
    int k = 0;
    prefixes[0] = 0;
    for (int i = 1; i < pattern.length(); i++) {
      while (k > 0 && pattern.charAt(k) != pattern.charAt(i)) {
        k = prefixes[k];
      }
      if (pattern.charAt(k) == pattern.charAt(i)) {
        k++;
      }
      prefixes[i] = k;
    }

    return prefixes;
  }

  // test this class
  public static void main(String[] args) {
    KMP kmp = new KMP();

    String target1 = "ababacb";
    String pattern1 = "ab";

    String target2 = "aaaaa";
    String pattern2 = "a";

    String target3 = "aabcde";
    String pattern3 = "abcdef";

    kmp.solveKMP(target1, pattern1);
    System.out.println();

    kmp.solveKMP(target2, pattern2);
    System.out.println();

    kmp.solveKMP(target3, pattern3);
  }
}

