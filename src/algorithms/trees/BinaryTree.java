package algorithms.trees;

import java.util.Collection;

/**
 * 单纯的二叉树只能作为一种数据存储结构，实现的意义不大。日后需要这样一个结构的时候再实现它。
 * 
 * @author moqiguzhu
 * @date 2015-11-30
 * @version 1.0
 * 
 * @param <T>
 */

public class BinaryTree<T> implements Tree<T> {

  @Override
  public boolean add(T value) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public T remove(T value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void clear() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean contains(T value) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int size() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean validate() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Collection<T> toCollection() {
    // TODO Auto-generated method stub
    return null;
  }

}
