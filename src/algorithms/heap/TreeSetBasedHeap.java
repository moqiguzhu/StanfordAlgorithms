package algorithms.heap;

import java.util.Map.Entry;
import java.util.Set;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

// 为了让这种方法工作，我们需要能够new Map.Entry的对象
// 但是Map.Entry是一个接口，不能直接通过new关键字生成新的对象
// 参考http://stackoverflow.com/questions/3110547/java-how-to-create-new-entry-key-value
class Heap1 {
  TreeSet<Map.Entry<Integer, Integer>> set = new TreeSet<HashMap.Entry<Integer, Integer>>(new Comparator<HashMap.Entry<Integer, Integer>>() {
    @Override
    public int compare(Entry<Integer, Integer> node1, Entry<Integer, Integer> node2) {
        if(node1.getKey() != node2.getKey()) {
            return node1.getKey() < node2.getKey() ? 1 : -1;
        } else {
            if(node1.getValue() == node2.getValue()) {
                return 0;
            } else {
                return node1.getValue() < node2.getValue() ? 1 : -1;
            }
        }
    }
});
}

class HeapNode implements Comparable<HeapNode> {
  private int value;
  private int label;
  
  public HeapNode(int value, int label) {
      this.value = value;
      this.label = label;
  }

  public int getValue() {
      return value;
  }

  public void setValue(int value) {
      this.value = value;
  }

  public int getLabel() {
      return label;
  }

  public void setLabel(int label) {
      this.label = label;
  }
  
  @Override
  public String toString() {
      return label + ":" + value; 
  }

  @Override
  public int compareTo(HeapNode node1) {
      if(node1.getValue() != this.getValue()) {
          return node1.getValue() < this.getValue() ? 1 : -1;
      } else {
          if(node1.getLabel() == this.getLabel()) {
              return 0;
          } else {
              return node1.getLabel() < this.getLabel() ? 1 : -1;
          }
      }
  }
  
}

public class TreeSetBasedHeap {
  public static void main(String[] args) {
    Set<HeapNode> set = new TreeSet<>();
    
    HeapNode node1 = new HeapNode(2, 1);
    HeapNode node2 = new HeapNode(3, 2);
    HeapNode node3 = new HeapNode(4, 3);
    HeapNode node4 = new HeapNode(5, 4);
    HeapNode node5 = new HeapNode(5, 5);
    
    set.add(node1);
    set.add(node3);
    System.out.println(set);
    
    set.add(node2);
    System.out.println(set);
    
    set.remove(node2);
    set.add(node4);
    System.out.println(set);
    
    set.add(node5);
    System.out.println(set);
  }
}
