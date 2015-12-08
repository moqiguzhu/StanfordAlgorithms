package algorithms.string;

import java.util.ArrayList;
import java.util.List;

/**
 * The edit distance when only insertion and deletion is allowed (no substitution), or when the cost
 * of the substitution is the double of the cost of an insertion or deletion, is: d'(X,Y) = n + m -
 * 2 \cdot \left|LCS(X,Y)\right|.
 * 
 * @author moqiguzhu
 * @date 2015-12-08
 * @version 1.0
 */

public class LCS {
  public int computeLCS(String a, String b) {
    int[][] lengths = new int[a.length() + 1][b.length() + 1];

    // row 0 and column 0 are init. to 0 already
    for (int i = 1; i <= a.length(); i++) {
      for (int j = 1; j <= b.length(); j++) {
        if (a.charAt(i-1) == b.charAt(j-1)) {
          lengths[i][j] = lengths[i - 1][j - 1] + 1;
        } else {
          lengths[i][j] = Math.max(lengths[i][j - 1], lengths[i - 1][j]);
        }
      }
    }

    return lengths[a.length()][b.length()];
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
    LCS lcs = new LCS();
    List<String> testcases = lcs.createTestCases();
    for (int i = 0; i < testcases.size() / 2; i++) {
      System.out.println(lcs.computeLCS(testcases.get(2 * i), testcases.get(2 * i + 1)));
    }
  }

}
