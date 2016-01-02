package algorithms.trees;

/************************
 * BSTNode.java ************************** node of a generic binary search tree
 */

public class BinaryNode<T extends Comparable<? super T>> {
  protected T el;
  protected BinaryNode<T> left, right;
  /* record height of current node in AVL tree */
  protected int height;

  public BinaryNode() {
    left = right = null;
  }

  public BinaryNode(T el) {
    this(el, null, null);
  }

  public BinaryNode(T el, int height) {
    this(el, height, null, null);
  }

  public BinaryNode(T el, BinaryNode<T> lt, BinaryNode<T> rt) {
    this.el = el;
    left = lt;
    right = rt;
  }

  public BinaryNode(T el, int height, BinaryNode<T> lt, BinaryNode<T> rt) {
    this.el = el;
    this.height = height;
    this.left = lt;
    this.right = rt;
  }
}
