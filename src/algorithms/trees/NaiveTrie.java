package algorithms.trees;

public class NaiveTrie {
  private NaiveTrieNode root;

  public NaiveTrie() {
    root = new NaiveTrieNode();
  }

  // Inserts a word into the trie.
  public void insert(String word) {
    int index = 0;
    NaiveTrieNode tmp = root;
    for (index = 0; index < word.length(); index++) {
      int pos = Character.valueOf(word.charAt(index)) - 97;
      if (tmp.childs[pos] == null) {
        NaiveTrieNode node = new NaiveTrieNode();
        tmp.childs[pos] = node;
        tmp = node;
        continue;
      }
      tmp = tmp.childs[pos]; // word的前缀是已经存在Trie中某个单词的前缀
    }
    tmp.isLeaf = true;
  }

  // Returns if the word is in the trie.
  public boolean search(String word) {
    int index = 0;
    NaiveTrieNode tmp = root;
    while (index < word.length()) {
      int pos = Character.valueOf(word.charAt(index)) - 97;
      if (tmp.childs[pos] == null) {
        return false;
      }
      tmp = tmp.childs[pos];
      index++;
    }
    return tmp.isLeaf;
  }

  // Returns if there is any word in the trie
  // that starts with the given prefix.
  public boolean startsWith(String prefix) {
    int index = 0;
    NaiveTrieNode tmp = root;
    while (index < prefix.length()) {
      int pos = Character.valueOf(prefix.charAt(index)) - 97;
      if (tmp.childs[pos] == null) {
        return false;
      }
      tmp = tmp.childs[pos];
      index++;
    }
    return true;
  }

  public static void main(String[] args) {
    NaiveTrie trie = new NaiveTrie();
    trie.insert("something");
    System.out.println(trie.search("some"));

    trie.insert("some");
    System.out.println(trie.search("some"));

    trie.insert("okay");
    System.out.println(trie.search("oka"));

    System.out.println(trie.startsWith("some"));
  }
}
