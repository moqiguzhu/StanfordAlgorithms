package algorithms.maxstack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 当栈为空的时候，调用pop操作可以有两种处理方法： 第一种是直接抛出EmptyStackException异常，第二种是返回null，此类的实现采用的是第二种方法。
 * MaxStack和JDK里面的Stack类是合成关系，所以如何让Maxstack类继承Stack类将会是一个不合理的设计。
 * 
 * @author moqiguzhu
 * @date 2015-11-09
 * @version 1.0
 * @param <T>
 */

public class MaxStack<T> {
  /* 元素之间的比较器，通过这个比较器MaxStack类也可以作为一个最小栈来使用 */
  private Comparator<T> comparator;

  /* 存储栈中所有的元素 */
  private LinkedList<T> elements;

  /* 存储所有前驱中的最大者 */
  private LinkedList<T> maxPrecursor;

  // 注意这里并没有使用父类的构造函数
  public MaxStack(Comparator<T> comp) {
    comparator = (Comparator<T>) comp;
    elements = new LinkedList<>();
    maxPrecursor = new LinkedList<>();
  }

  public MaxStack() {
    comparator = null;
    elements = new LinkedList<>();
    maxPrecursor = new LinkedList<>();
  }

  public MaxStack(List<T> eles) {
    comparator = null;
    elements = new LinkedList<>();
    maxPrecursor = new LinkedList<>();

    for (T ele : elements) {
      push(ele);
    }
  }

  // 按照eles中元素顺序从前往后入栈
  public MaxStack(List<T> eles, Comparator<T> comp) {
    comparator = comp;
    elements = new LinkedList<>();
    maxPrecursor = new LinkedList<>();
    for (T ele : eles) {
      push(ele);
    }
  }

  public T push(T element) {
    int flag = 0;

    elements.addLast(element);

    // push the appropriate element in maxPrecursor
    if (maxPrecursor.isEmpty()) {
      maxPrecursor.addLast(element);
    } else {
      flag = compare(maxPrecursor.getLast(), element);
      if (flag > 0) {
        maxPrecursor.addLast(maxPrecursor.getLast());
      } else {
        maxPrecursor.addLast(element);
      }
    }

    return element;
  }

  public int compare(T element1, T element2) {
    int flag = 0;

    if (comparator == null) {
      if (element1 instanceof Comparator<?>) {
        Comparator<T> localComparator = (Comparator<T>) element1;
        flag = localComparator.compare(element1, element2);
      } else if (element1 instanceof Comparable<?>) {
        Comparable<T> localComparable = (Comparable<T>) element1;
        flag = localComparable.compareTo(element2);
      } else {
        throw new ClassCastException();
      }
    } else {
      flag = comparator.compare(element1, element2);
    }

    return flag;
  }

  /**
   * 
   * @return 删除并返回栈顶元素，如果栈为空，返回null
   */
  public T pop() {
    T result = null;
    if (elements.isEmpty()) {
      return null;
    } else {
      maxPrecursor.removeLast();
      result = elements.removeLast();
    }

    return result;
  }

  /**
   * 
   * @return 返回但是不删除栈顶元素，如果栈为空，返回null
   */
  public T peek() {
    if (elements.isEmpty()) {
      return null;
    } else {
      return elements.getLast();
    }
  }

  /**
   * 
   * @return 返回当前栈中的最大值，如果栈为空，返回null
   */
  public T getMax() {
    if (elements.isEmpty()) {
      return null;
    } else {
      return maxPrecursor.getLast();
    }
  }

  public List<T> getElements() {
    return new ArrayList<T>(elements);
  }

  public void clear() {
    elements.clear();
    maxPrecursor.clear();
  }

  public boolean isEmpty() {
    // number of elements in elements and maxPrecursor should be always equal
    assert (elements.isEmpty() == maxPrecursor.isEmpty());
    return elements.isEmpty();
  }

  public int size() {
    assert (elements.size() == maxPrecursor.size());
    return elements.size();
  }

  public static void main(String[] args) {
    // test max stack
    System.out.println("test max stack:");
    MaxStack<Integer> maxStack = new MaxStack<>();
    maxStack.push(2);
    System.out.println(maxStack.getMax()); // should be 2
    maxStack.push(1);
    System.out.println(maxStack.getMax()); // should be 2
    maxStack.push(3);
    System.out.println(maxStack.getMax()); // should be 3
    maxStack.pop();
    System.out.println(maxStack.getMax()); // should be 2

    // test min stack
    System.out.println("test min stack:");
    MaxStack<Double> minStack = new MaxStack<>(Collections.reverseOrder());
    minStack.push(2.0);
    System.out.println(minStack.getMax()); // should be 2.0
    minStack.push(1.0);
    System.out.println(minStack.getMax()); // should be 1.0
    minStack.push(-1.0);
    System.out.println(minStack.getMax()); // should be -1.0
    System.out.println(minStack.pop()); // should be -1.0
    System.out.println(minStack.getMax()); // should be 1.0
  }
}
