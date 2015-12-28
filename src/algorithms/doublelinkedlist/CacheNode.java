package algorithms.doublelinkedlist;

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
