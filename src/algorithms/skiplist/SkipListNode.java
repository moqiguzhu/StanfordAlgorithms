package algorithms.skiplist;

public class SkipListNode<T> {
  public T key;
  public SkipListNode<T>[] next;

  SkipListNode(T i, int n) {
    key = i;
    next = new SkipListNode[n];
    for (int j = 0; j < n; j++) {
      next[j] = null;
    }
  }
}

