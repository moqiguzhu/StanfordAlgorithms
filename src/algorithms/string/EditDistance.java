package algorithms.string;

import java.util.ArrayList;
import java.util.List;

/**
 * 编程之美3.3说到过这个问题。
 * 另外，这个问题和LCS有很大的关联。
 * 这种计算两个字符串之间距离的距离也叫做Levenshtein distance。
 * 
 * @author moqiguzhu
 * @date 2015-12-08
 * @version 1.0
 */

public class EditDistance {
  public int minDistance(String word1, String word2) {
    // Step 1
    int n = word1.length(), m = word2.length();
    if (n == 0)
      return m;
    if (m == 0)
      return n;
    int[][] A = new int[n + 1][m + 1];

    // Step 2
    // word1和word2分别为空串时的极端情况
    for (int i = 0; i <= n; ++i) {
      A[i][0] = i;
    }
    for (int j = 0; j <= m; ++j) {
      A[0][j] = j;
    }

    for (int i = 1; i <= n; ++i) { // Step 3
      char word1_i = word1.charAt(i - 1);
      for (int j = 1; j <= m; ++j) { // Step 4
        char word2_j = word2.charAt(j - 1);
//        int cost = (word1_i == word2_j) ? 0 : 1; // Step 5
//        A[i][j] = Math.min(Math.min(A[i - 1][j] + 1, A[i][j - 1] + 1), A[i - 1][j - 1] + cost);// Step 6        
        
        // 另外一种更加贴近算法语义的写法
        if(word1_i == word2_j) {
          A[i][j] = A[i-1][j-1];
        } else {
          A[i][j] = Math.min(Math.min(A[i - 1][j] + 1, A[i][j - 1] + 1), A[i - 1][j - 1] + 1);
        }
      }
    }
    return A[n][m]; // Step 7
  }

  public List<String> createTestCases() {
    List<String> testcases = new ArrayList<String>();

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
    EditDistance ed = new EditDistance();
    List<String> testcases = ed.createTestCases();
    for (int i = 0; i < testcases.size() / 2; i++) {
      System.out.println(ed.minDistance(testcases.get(2 * i), testcases.get(2 * i + 1)));
    }
  }
}

