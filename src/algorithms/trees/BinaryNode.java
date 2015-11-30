package algorithms.trees;

/************************  BSTNode.java  **************************
 *             node of a generic binary search tree
 */

public class BinaryNode<T extends Comparable<? super T>> {
    protected T el;
    protected BinaryNode<T> left, right;
    
    public BinaryNode() {
        left = right = null;
    }
    
    public BinaryNode(T el) {
        this(el,null,null);
    }
    
    public BinaryNode(T el, BinaryNode<T> lt, BinaryNode<T> rt) {
        this.el = el; left = lt; right = rt;
    }
}
