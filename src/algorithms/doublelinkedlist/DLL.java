package algorithms.doublelinkedlist;

// **************************** DLL.java *******************************
// generic doubly linked list class

/**
 * http://www.mathcs.duq.edu/drozdek/DSinJava/DLL.java
 * 
 * @author Adam Drozdek
 */

public class DLL<T> {
  public DLLNode<T> head, tail;

  public DLL() {
    head = tail = null;
  }

  public boolean isEmpty() {
    return head == null;
  }

  public void setToNull() {
    head = tail = null;
  }

  public T firstEl() {
    if (head != null)
      return head.info;
    else
      return null;
  }

  public void addToHead(T el) {
    if (head != null) {
      head = new DLLNode<T>(el, head, null);
      head.next.prev = head;
    } else
      head = tail = new DLLNode<T>(el);
  }

  public void addToTail(T el) {
    if (tail != null) {
      tail = new DLLNode<T>(el, null, tail);
      tail.prev.next = tail;
    } else
      head = tail = new DLLNode<T>(el);
  }

  public T deleteFromHead() {
    if (isEmpty())
      return null;
    T el = head.info;
    if (head == tail) // if only one node on the list;
      head = tail = null;
    else { // if more than one node in the list;
      head = head.next;
      head.prev = null;
    }
    return el;
  }

  public T deleteFromTail() {
    if (isEmpty())
      return null;
    T el = tail.info;
    if (head == tail) // if only one node on the list;
      head = tail = null;
    else { // if more than one node in the list;
      tail = tail.prev;
      tail.next = null;
    }
    return el;
  }

  public void printAll() {
    for (DLLNode<T> tmp = head; tmp != null; tmp = tmp.next)
      System.out.print(tmp.info + " ");
  }

  public T find(T el) {
    DLLNode<T> tmp;
    for (tmp = head; tmp != null && !tmp.info.equals(el); tmp = tmp.next);
    if (tmp == null)
      return null;
    else
      return tmp.info;
  }

  // node must lies in this DLL
  public void removeToHead(DLLNode<T> node) {
    // 注意这里使用的是==
    if (head == tail || node == head)
      return;

    node.prev.next = node.next;

    if (node.next != null)
      node.next.prev = node.prev;
    else
      tail = node.prev;

    // 不能使用下面这行代码 值与地址
    // addToHead(node.info);
    node.next = head;
    node.prev = null;
    head.prev = node;
    head = node;
  }
}
