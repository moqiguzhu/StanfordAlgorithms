package algorithms.string;

import java.util.ArrayList;
import java.util.List;

/**
 * 注意与Longest Common Subsequence的区别
 * 
 * @author moqiguzhu
 * @date 2015-12-08
 * @version 1.0
 */

public class LCSubstring {
  public int[][] LCSuff(String a, String b) {
    int[][] lengths = new int[a.length() + 1][b.length() + 1];

    for (int i = 1; i <= a.length(); i++) {
      for (int j = 1; j <= b.length(); j++) {
        if (a.charAt(i - 1) == b.charAt(j - 1)) {
          lengths[i][j] = lengths[i - 1][j - 1] + 1;
        } else {
          lengths[i][j] = 0;
        }
      }
    }

    return lengths;
  }

  public int LCSubstr(String a, String b) {
    int[][] lengths = LCSuff(a, b);
    int max = 0;

    for (int i = 0; i < lengths.length; i++) {
      for (int j = 0; j < lengths[0].length; j++) {
        max = max > lengths[i][j] ? max : lengths[i][j];
      }
    }

    return max;
  }

  public List<String> createTestCases() {
    List<String> testcases = new ArrayList<>();

    String word11 = "";
    String word12 = "ab";
    testcases.add(word11);
    testcases.add(word12);

    String word21 = "travelling";
    String word22 = "traveling";
    testcases.add(word21);
    testcases.add(word22);

    return testcases;
  }

  public static void main(String[] args) {
    LCSubstring lcsubstr = new LCSubstring();
    List<String> testcases = lcsubstr.createTestCases();
    for (int i = 0; i < testcases.size() / 2; i++) {
      System.out.println(lcsubstr.LCSubstr(testcases.get(2 * i), testcases.get(2 * i + 1)));
    }
  }
}
