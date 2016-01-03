package algorithms.trees;

/**
 * @author moqiguzhu
 * @date 2015-01-03
 * @version 1.0
 */
public class NaiveTrieNode {
  // Initialize your data structure here.
  public NaiveTrieNode[] childs;
  public boolean isLeaf;

  public NaiveTrieNode() {
    childs = new NaiveTrieNode[26];
    isLeaf = false;
  }
}
