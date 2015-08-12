package algorithms.unionfind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author moqiguzhu
 * @date 2015-08-12
 * @version 1.0
 * @param <E>
 *            此数据结构存储元素的类型
 */

public class UnionFind<E> {
	private Map<E, Integer> ele_index;
	private int[] rank;
	private int[] parent;
	private int count;

	/**
	 * 将给定两个元素连接起来
	 * 
	 * @param ele1
	 *            第一个给定的元素
	 * @param ele2
	 *            第二个给定的元素
	 */
	public void union(E ele1, E ele2) {
		if (connected(ele1, ele2))
			return;
		int parent1 = find(ele1);
		int parent2 = find(ele2);
		if (rank[parent1] > rank[parent2])
			parent[parent2] = parent1;
		else if (rank[parent2] > rank[parent1])
			parent[parent1] = parent2;
		else {
			parent[parent2] = parent1;
			rank[parent1] = rank[parent1] + 1; // the only place where rank can
												// be changed
		}
		count--;
	}

	/**
	 * 如果给定的节点不是直接指向其父节点，在找寻其父节点的过程中使用路径压缩技术
	 * 
	 * @param ele
	 *            给定元素
	 * @return 返回给定元素的父节点在内部数组中的下标
	 */
	public int find(E ele) {
		int index = ele_index.get(ele);
		// path compression
		while (parent[index] != parent[parent[index]]) {
			parent[index] = parent[parent[index]];
			// do not change rank
		}
		return parent[index];
	}

	/**
	 * 返回此数据结构中还有多少个簇，这个函数在实现single link聚类的时候有用
	 * 
	 * @return 返回当前数据结构中还存在多少个簇
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * 判断给定的两个元素是不是已经相连，是，返回true，否则返回false
	 * 
	 * @param ele1
	 *            给定的第一个元素
	 * @param ele2
	 *            给定的第二个元素
	 * @return 返回两个元素是否已经相连
	 */
	public boolean connected(E ele1, E ele2) {
		return find(ele1) == find(ele2);
	}

	/**
	 * 初始化此数据结构
	 * 
	 * @param elements
	 *            存储的元素集合
	 */
	public void init(List<E> elements) {
		int size = elements.size();
		count = size;
		ele_index = new HashMap<E, Integer>();
		rank = new int[size];
		parent = new int[size];

		for (int i = 0; i < size; i++) {
			ele_index.put(elements.get(i), i);
			rank[i] = 0;
			parent[i] = i;
		}
	}

	/**
	 * 返回给定元素在内部数组中的下标
	 * 
	 * @return 返回给定元素在内部数组中的下标
	 */
	public int getIndex(E ele) {
		return ele_index.get(ele);
	}

	// !!! test this data structure
	// !!! test single link cluster
	public static void main(String[] args) {
	}
}
