package algorithms.doublelinkedlist;

/**
 * 实现LRUCache所需要的类
 * 
 * @author moqiguzhu
 * @date 2015-12-28
 * @version 1.0
 */
public class CacheNode {
  public int key;
  public int value;

  public CacheNode(int key, int value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public String toString() {
    return "key:" + key + " value:" + value;
  }
}
