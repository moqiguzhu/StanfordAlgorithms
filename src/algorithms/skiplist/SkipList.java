package algorithms.skiplist;

import java.util.Arrays;
import java.util.Random;

public class SkipList<T extends Comparable<? super T>> {
  private int maxLevel;
  private SkipListNode<T>[] root;
  private int[] powers;
  private Random rd = new Random();

  SkipList() {
    this(4);
  }

  SkipList(int i) {
    maxLevel = i;
    root = new SkipListNode[maxLevel];
    powers = new int[maxLevel];
    for (int j = 0; j < maxLevel; j++) {
      root[j] = null;
    }
    choosePowers();
  }

  public boolean isEmpty() {
    return root[0] == null;
  }

  //!!!
  public void choosePowers() {
    powers[maxLevel - 1] = (2 << (maxLevel - 1)) - 1; // 2^maxLevel - 1
    for (int i = maxLevel - 2, j = 0; i >= 0; i--, j++) {
      powers[i] = powers[i + 1] - (2 << j); // 2^(j+1)
    }
  }

  public int chooseLevel() {
    int i, r = Math.abs(rd.nextInt()) % powers[maxLevel - 1] + 1;
    for (i = 1; i < maxLevel; i++)
      if (r < powers[i])
        return i - 1; // return a level < the highest level;
    return i - 1; // return the highest level;
  }

  // make sure (with isEmpty()) that search() is called for a nonempty list;
  public T search(T key) {
    int lvl;
    SkipListNode<T> prev, curr; // find the highest nonnull
    for (lvl = maxLevel - 1; lvl >= 0 && root[lvl] == null; lvl--); // level;
    prev = curr = root[lvl];
    while (true) {
      if (key.equals(curr.key)) // success if equal;
        return curr.key;
      else if (key.compareTo(curr.key) < 0) { // if smaller, go down,
        if (lvl == 0) // if possible
          return null;
        else if (curr == root[lvl]) // by one level
          curr = root[--lvl]; // starting from the
        else
          curr = prev.next[--lvl]; // predecessor which
      } // can be the root;
      else { // if greater,
        prev = curr; // go to the next
        if (curr.next[lvl] != null) // non-null node
          curr = curr.next[lvl]; // on the same level
        else { // or to a list on a lower level;
          for (lvl--; lvl >= 0 && curr.next[lvl] == null; lvl--);
          if (lvl >= 0)
            curr = curr.next[lvl];
          else
            return null;
        }
      }
    }
  }

  public void insert(T key) {
    SkipListNode<T>[] curr = new SkipListNode[maxLevel];
    SkipListNode<T>[] prev = new SkipListNode[maxLevel];
    SkipListNode<T> newNode;
    int lvl, i;
    curr[maxLevel - 1] = root[maxLevel - 1];
    prev[maxLevel - 1] = null;
    for (lvl = maxLevel - 1; lvl >= 0; lvl--) {
      while (curr[lvl] != null && curr[lvl].key.compareTo(key) < 0) {
        prev[lvl] = curr[lvl]; // go to the next
        curr[lvl] = curr[lvl].next[lvl]; // if smaller;
      }
      if (curr[lvl] != null && key.equals(curr[lvl].key)) // don't
        return; // include duplicates;
      if (lvl > 0) // go one level down
        if (prev[lvl] == null) { // if not the lowest
          curr[lvl - 1] = root[lvl - 1]; // level, using a link
          prev[lvl - 1] = null; // either from the root
        } else { // or from the predecessor;
          curr[lvl - 1] = prev[lvl].next[lvl - 1];
          prev[lvl - 1] = prev[lvl];
        }
    }
    lvl = chooseLevel(); // generate randomly level
    newNode = new SkipListNode<T>(key, lvl + 1); // for newNode;
    for (i = 0; i <= lvl; i++) { // initialize next fields of
      newNode.next[i] = curr[i]; // newNode and reset to newNode
      if (prev[i] == null) // either fields of the root
        root[i] = newNode; // or next fields of newNode's
      else
        prev[i].next[i] = newNode; // predecessors;
    }
  }
  
  public static void main(String[] args) {
    SkipList sl = new SkipList<>();
    sl.choosePowers();
    System.out.println(Arrays.toString(sl.powers));
  }
}
