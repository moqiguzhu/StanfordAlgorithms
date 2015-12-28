package algorithms.doublelinkedlist;

import java.util.HashMap;
import java.util.Map;

/**
 * LRU cahce Java implementation
 * 
 * @author moqiguzhu
 * @date 2015-12-28
 * @version 1.0
 */
public class LRUCache {
  private Map<Integer, DLLNode<CacheNode>> key_node;
  private DLL<CacheNode> nodes;
  private int maxSize;

  public LRUCache(int capacity) {
    this.key_node = new HashMap<>();
    this.nodes = new DLL<>();
    this.maxSize = capacity;
  }

  public int get(int key) {
    int result = -1;
    if (key_node.containsKey(key)) {
      nodes.removeToHead(key_node.get(key));
      result = key_node.get(key).info.value;
    }
    
    return result;
  }

  public void set(int key, int value) {
    int leastUsedKey = -1;
    if (key_node.containsKey(key)) {
      nodes.removeToHead(key_node.get(key));
      nodes.head.info.value = value;
    } else {
      if (maxSize == key_node.size()) {
        leastUsedKey = nodes.deleteFromTail().key;
        key_node.remove(leastUsedKey);
      }
      nodes.addToHead(new CacheNode(key, value));
    }
    key_node.put(key, nodes.head);
  }

  public void print() {
    nodes.printAll();
    System.out.println();
  }

  // test LRUCache
  public static void main(String[] args) {
    LRUCache lruc = new LRUCache(10);

    lruc.set(10, 13);
    lruc.set(3, 17);
    lruc.set(6, 11);
    lruc.set(10, 5);
    lruc.set(9, 10);
    lruc.get(13);
    lruc.set(2, 19);
    lruc.get(2);
    lruc.get(3);
    lruc.set(5, 25);
    lruc.get(8);
    lruc.set(9, 22);
    lruc.set(5, 5);
    lruc.set(1, 30);
    lruc.get(11);
    lruc.set(9, 12);
    lruc.get(7);
    lruc.get(5);
    lruc.get(8);
    lruc.get(9);
    lruc.set(4, 30);
    lruc.set(9, 3);
    lruc.get(9);
    lruc.get(10);
    lruc.get(10);
    lruc.set(6, 14);
    lruc.set(3, 1);
    lruc.get(3);
    lruc.set(10, 11);
    lruc.get(8);
    lruc.set(2, 14);
    lruc.get(1);
    lruc.get(5);
    lruc.get(4);
    lruc.set(11, 4);
    lruc.set(12, 24);
    lruc.set(5, 18);
    lruc.get(13);
    lruc.set(7, 23);
    lruc.get(8);
    lruc.get(12);
    lruc.set(3, 27);
    lruc.set(2, 12);
    lruc.get(5);
    lruc.set(2, 9);
    lruc.set(13, 4);
    lruc.set(8, 18);
    lruc.set(1, 7);
    lruc.get(6);
    lruc.set(9, 29);
    lruc.set(8, 21);
    lruc.get(5);
    lruc.set(6, 30);
    lruc.set(1, 12);
    lruc.get(10);
    lruc.set(4, 15);
    lruc.set(7, 22);
    lruc.set(11, 26);
    lruc.set(9, 29);
    lruc.get(5);
    
    // answer should be:
    // -1 19 17 -1 -1 -1 5 -1 12 3 5 5 1 -1 30 5 30 -1 -1 24 18 -1 18 -1 18
  }
}
