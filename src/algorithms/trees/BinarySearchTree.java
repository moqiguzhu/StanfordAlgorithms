package algorithms.trees;

import java.util.LinkedList;
import java.util.Stack;

/************************
 * BinarySearchTree.java ************************** generic binary search tree
 */

public class BinarySearchTree<T extends Comparable<? super T>> {
  protected BinaryNode<T> root = null;

  public BinarySearchTree() {}

  public void clear() {
    root = null;
  }

  public boolean isEmpty() {
    return root == null;
  }

  public void insert(T el) {
    BinaryNode<T> p = root, prev = null;
    while (p != null) { // find a place for inserting new node;
      prev = p;
      if (el.compareTo(p.el) < 0)
        p = p.left;
      else
        p = p.right;
    }
    if (root == null) // tree is empty;
      root = new BinaryNode<T>(el);
    else if (el.compareTo(prev.el) < 0)
      prev.left = new BinaryNode<T>(el);
    else
      prev.right = new BinaryNode<T>(el);
  }

  public void recInsert(T el) {
    root = recInsert(root, el);
  }

  /**
   * 
   * @param p root node of current tree
   * @param el value needed to be inserted
   * @return tree node that has been inserted
   */
  protected BinaryNode<T> recInsert(BinaryNode<T> p, T el) {
    if (p == null)
      p = new BinaryNode<T>(el);
    else if (el.compareTo(p.el) < 0)
      p.left = recInsert(p.left, el);
    else
      p.right = recInsert(p.right, el);
    return p;
  }

  /**
   * 
   * @param el value needed to be searched
   * @return if the value has been inserted in the tree
   */
  public boolean isInTree(T el) {
    return search(el) != null;
  }

  /**
   * 
   * @param el value that needed to be searched
   * @return if searched value is in the tree, return the value, else return null
   */
  protected T search(T el) {
    BinaryNode<T> p = root;
    while (p != null)
      if (el.equals(p.el))
        return p.el;
      else if (el.compareTo(p.el) < 0)
        p = p.left;
      else
        p = p.right;
    return null;
  }

  /**
   * preorder traversal of binary tree
   */
  public void preorder() {
    preorder(root);
  }

  /**
   * inorder traversal of binary tree
   */
  public void inorder() {
    inorder(root);
  }

  /**
   * postorder traversal of binary tree
   */
  public void postorder() {
    postorder(root);
  }

  /**
   * print the value of currently visited tree node
   * 
   * @param p tree node of being visited
   */
  protected void visit(BinaryNode<T> p) {
    System.out.print(p.el + " ");
  }

  /**
   * recursively implementation of inorder traversal of binary search tree
   * 
   * @param p root of current subtree
   */
  protected void inorder(BinaryNode<T> p) {
    if (p != null) {
      inorder(p.left);
      visit(p);
      inorder(p.right);
    }
  }

  /**
   * recursively implementation of preorder traversal of binary search tree
   * 
   * @param p root of current subtree
   */
  protected void preorder(BinaryNode<T> p) {
    if (p != null) {
      visit(p);
      preorder(p.left);
      preorder(p.right);
    }
  }

  /**
   * recursively implementation of preorder traversal of binary search tree
   * 
   * @param p root of current subtree
   */
  protected void postorder(BinaryNode<T> p) {
    if (p != null) {
      postorder(p.left);
      postorder(p.right);
      visit(p);
    }
  }

  /**
   * delete the corresponding tree node with given value. If the tree is empty or can not find such
   * a tree node, print error information
   * 
   * @param el value that needed to be deleted from tree
   */
  public void deleteByCopying(T el) {
    BinaryNode<T> node, p = root, prev = null;
    while (p != null && !p.el.equals(el)) { // find the node p
      prev = p; // with element el;
      if (el.compareTo(p.el) < 0)
        p = p.left;
      else
        p = p.right;
    }
    node = p;
    if (p != null && p.el.equals(el)) {
      if (node.right == null) // node has no right child;
        node = node.left;
      else if (node.left == null) // no left child for node;
        node = node.right;
      else {
        BinaryNode<T> tmp = node.left; // node has both children;
        BinaryNode<T> previous = node; // 1.
        while (tmp.right != null) { // 2. find the rightmost
          previous = tmp; // position in the
          tmp = tmp.right; // left subtree of node;
        }
        node.el = tmp.el; // 3. overwrite the reference
                          // to the element being deleted;
        if (previous == node) // if node's left child's
          previous.left = tmp.left; // right subtree is null;
        else
          previous.right = tmp.left; // 4.
      }
      if (p == root)
        root = node;
      else if (prev.left == p)
        prev.left = node;
      else
        prev.right = node;
    } else if (root != null)
      System.out.println("el " + el + " is not in the tree");
    else
      System.out.println("the tree is empty");
  }

  public void deleteByMerging(T el) {
    BinaryNode<T> tmp, node, p = root, prev = null;
    while (p != null && !p.el.equals(el)) { // find the node p
      prev = p; // with element el;
      if (el.compareTo(p.el) < 0)
        p = p.right;
      else
        p = p.left;
    }
    node = p;
    if (p != null && p.el.equals(el)) {
      if (node.right == null) // node has no right child: its left
        node = node.left; // child (if any) is attached to its parent;
      else if (node.left == null) // node has no left child: its right
        node = node.right; // child is attached to its parent;
      else { // be ready for merging subtrees;
        tmp = node.left; // 1. move left
        while (tmp.right != null) // 2. and then right as far as
          tmp = tmp.right; // possible;
        tmp.right = // 3. establish the link between
            node.right; // the rightmost node of the left
                        // subtree and the right subtree;
        node = node.left; // 4.
      }
      if (p == root)
        root = node;
      else if (prev.left == p)
        prev.left = node;
      else
        prev.right = node; // 5.
    } else if (root != null)
      System.out.println("el " + el + " is not in the tree");
    else
      System.out.println("the tree is empty");
  }

  /**
   * iteratively implementation of preorder traversal of binary search tree with stack
   */
  public void iterativePreorder() {
    BinaryNode<T> p = root;
    Stack<BinaryNode<T>> travStack = new Stack<BinaryNode<T>>();
    if (p != null) {
      travStack.push(p);
      while (!travStack.isEmpty()) {
        p = travStack.pop();
        visit(p);
        if (p.right != null)
          travStack.push(p.right);
        if (p.left != null) // left child pushed after right
          travStack.push(p.left);// to be on the top of the stack;
      }
    }
  }

  /**
   * iteratively implementation of inorder traversal of binary search tree
   */
  public void iterativeInorder() {
    BinaryNode<T> p = root;
    Stack<BinaryNode<T>> travStack = new Stack<BinaryNode<T>>();
    while (p != null) {
      while (p != null) { // stack the right child (if any)
        if (p.right != null) // and the node itself when going
          travStack.push(p.right); // to the left;
        travStack.push(p);
        p = p.left;
      }
      p = travStack.pop(); // pop a node with no left child
      while (!travStack.isEmpty() && p.right == null) { // visit it and all
        visit(p); // nodes with no right child;
        p = travStack.pop();
      }
      visit(p); // visit also the first node with
      if (!travStack.isEmpty()) // a right child (if any);
        p = travStack.pop();
      else
        p = null;
    }
  }

  /**
   * iteratively implementation of postorder traversal of binary search tree
   */
  public void iterativePostorder2() {
    BinaryNode<T> p = root;
    Stack<BinaryNode<T>> travStack = new Stack<BinaryNode<T>>(),
        output = new Stack<BinaryNode<T>>();
    if (p != null) { // left-to-right postorder = right-to-left preorder;
      travStack.push(p);
      while (!travStack.isEmpty()) {
        p = travStack.pop();
        output.push(p);
        if (p.left != null)
          travStack.push(p.left);
        if (p.right != null)
          travStack.push(p.right);
      }
      while (!output.isEmpty()) {
        p = output.pop();
        visit(p);
      }
    }
  }

  /**
   * iteratively implementation of postorder traversal of binary search tree
   */
  public void iterativePostorder() {
    BinaryNode<T> p = root, q = root;
    Stack<BinaryNode<T>> travStack = new Stack<BinaryNode<T>>();
    while (p != null) {
      for (; p.left != null; p = p.left)
        travStack.push(p);
      while (p != null && (p.right == null || p.right == q)) {
        visit(p);
        q = p;
        if (travStack.isEmpty())
          return;
        p = travStack.pop();
      }
      travStack.push(p);
      p = p.right;
    }
  }

  /**
   * traverse binary tree by level
   */
  public void breadthFirst() {
    BinaryNode<T> p = root;
    LinkedList<BinaryNode<T>> queue = new LinkedList<BinaryNode<T>>();
    if (p != null) {
      queue.addLast(p);
      while (!queue.isEmpty()) {
        p = queue.removeFirst();
        visit(p);
        if (p.left != null)
          queue.addLast(p.left);
        if (p.right != null)
          queue.addLast(p.right);
      }
    }
  }

  /**
   * inorder traversal of binary search tree with Morris's trick
   */
  public void MorrisInorder() {
    BinaryNode<T> p = root, tmp;
    while (p != null)
      if (p.left == null) {
        visit(p);
        p = p.right;
      } else {
        tmp = p.left;
        while (tmp.right != null && // go to the rightmost node of
            tmp.right != p) // the left subtree or
          tmp = tmp.right; // to the temporary parent of p;
        if (tmp.right == null) {// if 'true' rightmost node was
          tmp.right = p; // reached, make it a temporary
          p = p.left; // parent of the current root,
        } else { // else a temporary parent has been
          visit(p); // found; visit node p and then cut
          tmp.right = null; // the right pointer of the current
          p = p.right; // parent, whereby it ceases to be
        } // a parent;
      }
  }

  /**
   * preorder traversal of binary search tree with Morris's trick
   */
  public void MorrisPreorder() {
    BinaryNode<T> p = root, tmp;
    while (p != null) {
      if (p.left == null) {
        visit(p);
        p = p.right;
      } else {
        tmp = p.left;
        while (tmp.right != null && // go to the rightmost node of
            tmp.right != p) // the left subtree or
          tmp = tmp.right; // to the temporary parent of p;
        if (tmp.right == null) {// if 'true' rightmost node was
          visit(p); // reached, visit the root and
          tmp.right = p; // make the rightmost node a temporary
          p = p.left; // parent of the current root,
        } else { // else a temporary parent has been
          tmp.right = null; // found; cut the right pointer of
          p = p.right; // the current parent, whereby it ceases
        } // to be a parent;
      }
    }
  }

  /**
   * post traversal of binary search tree with Morris's trick
   */
  public void MorrisPostorder() {
    BinaryNode<T> p = new BinaryNode<T>(), tmp, q, r, s;
    p.left = root;
    while (p != null)
      if (p.left == null)
        p = p.right;
      else {
        tmp = p.left;
        while (tmp.right != null && // go to the rightmost node of
            tmp.right != p) // the left subtree or
          tmp = tmp.right; // to the temporary parent of p;
        if (tmp.right == null) {// if 'true' rightmost node was
          tmp.right = p; // reached, make it a temporary
          p = p.left; // parent of the current root,
        } else { // else a temporary parent has been found;
          // process nodes between p.left (included) and p (excluded)
          // extended to the right in modified tree in reverse order;
          // the first loop descends this chain of nodes and reverses
          // right pointers; the second loop goes back, visits nodes,
          // and reverses right pointers again to restore the pointers
          // to their original setting;
          for (q = p.left, r = q.right, s = r.right; r != p; q = r, r = s, s = s.right)
            r.right = q;
          for (s = q.right; q != p.left; q.right = r, r = q, q = s, s = s.right)
            visit(q);
          visit(p.left); // visit node p.left and then cut
          tmp.right = null; // the right pointer of the current
          p = p.right; // parent, whereby it ceases to be
        } // a parent;
      }
  }

  /**
   * 
   * @param data elements will be inserted in the binary search tree
   * @param first start index
   * @param last end index
   */
  public void balance(T data[], int first, int last) {
    if (first <= last) {
      int middle = (first + last) / 2;
      insert(data[middle]);
      balance(data, first, middle - 1);
      balance(data, middle + 1, last);
    }
  }

  public void balance(T data[]) {
    balance(data, 0, data.length - 1);
  }
}
