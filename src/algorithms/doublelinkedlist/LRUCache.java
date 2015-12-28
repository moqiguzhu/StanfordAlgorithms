package algorithms.doublelinkedlist;

import java.util.HashMap;
import java.util.Map;

//!!! 
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
    if (key_node.containsKey(key)) {
      nodes.removeToHead(key_node.get(key));
      return key_node.get(key).info.value;
    } else {
      return -1;
    }
  }

  public void set(int key, int value) {
    int leastUsedKey = -1;
    if (key_node.containsKey(key)) {
      nodes.removeToHead(key_node.get(key));
      nodes.head.info.value = value;
    } else {
      if (maxSize == key_node.size()) {
        leastUsedKey = nodes.deleteFromTail().key;
        nodes.addToHead(new CacheNode(key, value));
        key_node.remove(leastUsedKey);
      } else {
        nodes.addToHead(new CacheNode(key, value));
      }
    }
    key_node.put(key, nodes.head);
  }
  
  public void print() {
    System.out.println("cache content: " + key_node.toString());
  }

  // test LRUCache
  public static void main(String[] args) {
    LRUCache lruc = new LRUCache(2);
    
    lruc.set(2, 1);
    lruc.print();
    
    lruc.set(1, 1);
    lruc.print();
    
    lruc.get(2);
    
    lruc.set(4, 1);
    lruc.print();
  }
}
