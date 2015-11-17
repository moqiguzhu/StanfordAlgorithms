package algorithms.maxqueue;

import java.util.*;

import algorithms.maxstack.MaxStack;

/**
 * 就像使用两个栈可以实现一个队列一样，使用两个最大栈我们也能实现一个最大队列。
 * MaxQueue倒是可以实现Queue接口，就是实现这个接口还需要实现太多此类并不需要的方法，所以也没有实现。
 * 
 * @author moqiguzhu
 * @version 1.0
 * @date 2015-11-09
 * @param <T>
 */

public class MaxQueue<T> {
  /* 两个元素之间的比较器 */
  Comparator<T> comparator;

  /* 所有的元素从maxStack1进 */
  private MaxStack<T> maxStack1;

  /* 所有的元素从maxStack2出 */
  private MaxStack<T> maxStack2;

  public MaxQueue() {
    // TODO Auto-generated constructor stub
    comparator = null;
    maxStack1 = new MaxStack<>();
    maxStack2 = new MaxStack<>();
  }

  public MaxQueue(Comparator<T> comp) {
    comparator = comp;
    maxStack1 = new MaxStack<T>(comp);
    maxStack2 = new MaxStack<T>(comp);
  }

  public boolean offer(T e) {
    if (maxStack1.push(e) == e) {
      return true;
    } else {
      return false;
    }
  }

  public T peek() {
    if (maxStack2.isEmpty()) {
      if (maxStack1.isEmpty()) {
        return null;
      } else {
        List<T> tmp = maxStack1.getElements();
        maxStack1.clear();

        maxStack2.clear();
        ListIterator<T> li = tmp.listIterator(tmp.size());
        while(li.hasPrevious()) {
          maxStack2.push(li.previous());
        }
        
        return maxStack2.peek();
      }
    } else {
      return maxStack2.peek();
    }
  }

  public T poll() {
    if (maxStack2.isEmpty()) {
      if (maxStack1.isEmpty()) {
        return null;
      } else {
        List<T> tmp = maxStack1.getElements();
        maxStack1.clear();

        maxStack2.clear();
        ListIterator<T> li = tmp.listIterator(tmp.size());
        while(li.hasPrevious()) {
          maxStack2.push(li.previous());
        }
        
        return maxStack2.pop();
      }
    } else {
      return maxStack2.pop();
    }
  }

  public int size() {
    return maxStack1.size() + maxStack2.size();
  }

  /**
   * 
   * @return 当前队列中的最大者
   */
  public T getMax() {
    T max1 = maxStack1.getMax();
    T max2 = maxStack2.getMax();
    if (maxStack1.isEmpty()) {
      return max2;
    } else if (maxStack2.isEmpty()) {
      return max1;
    } else {
      return maxStack1.compare(max1, max2) > 0 ? max1 : max2;
    }
  }

  public static void main(String[] args) {
    // test max queue
    System.out.println("test max queue: ");
    MaxQueue<Integer> maxQueue = new MaxQueue<>();
    maxQueue.offer(2);
    maxQueue.offer(1);
    System.out.println(maxQueue.getMax()); // should be 2
    maxQueue.offer(3);
    System.out.println(maxQueue.getMax()); // should be 3
    System.out.println(maxQueue.poll()); // should be 2
    System.out.println(maxQueue.getMax()); // should be 3
    System.out.println(maxQueue.peek()); // should be 1
    maxQueue.offer(4);
    System.out.println(maxQueue.size()); // should be 3
    System.out.println(maxQueue.getMax()); // should be 4
    
    // test min queue
    MaxQueue<Integer> minQueue = new MaxQueue<>(Collections.reverseOrder());
    System.out.println("test min queue");
    minQueue.offer(2);
    minQueue.offer(1);
    System.out.println(minQueue.getMax()); // should be 1
    minQueue.offer(3);
    System.out.println(minQueue.getMax()); // should be 1
    System.out.println(minQueue.poll()); // should be 2
    System.out.println(minQueue.getMax()); // should be 1
    System.out.println(minQueue.peek()); // should be 1
    minQueue.offer(-4);
    System.out.println(minQueue.size()); // should be 3
    System.out.println(minQueue.getMax()); // should be -4
  }
}
