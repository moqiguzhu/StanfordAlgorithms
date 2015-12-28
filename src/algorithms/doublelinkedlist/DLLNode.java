package algorithms.doublelinkedlist;

// **************************** DLLNode.java *******************************
// node of generic doubly linked list class

public class DLLNode<T> {
  public T info;
  public DLLNode<T> next, prev;

  public DLLNode() {
    next = null;
    prev = null;
  }

  public DLLNode(T el) {
    info = el;
    next = null;
    prev = null;
  }

  public DLLNode(T el, DLLNode<T> n, DLLNode<T> p) {
    info = el;
    next = n;
    prev = p;
  }
  
  @Override
  public String toString() {
    return "" + info;
  }
}
