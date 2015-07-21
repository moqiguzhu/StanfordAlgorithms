package Part2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author moqiguzhu
 * 参考算法导论实现的heap，与普通的heap不同的是我实现的这个heap存储了节点的位置信息，并且
 * 每个节点对应一个<key,value>，节点在堆中的位置是由这个key value决定的。
 * 保留位置信息之后，能够迅速通过节点本身得到节点在堆中的位置，这对于实现快速Dijkstra算法和
 * Prim的MST算法是极其重要的
 */
public class Heap {
	/*保存节点在heap中的位置信息*/
	private Map<Integer, Integer> indexes;
	/*heap的底层使用数组实现，节点使用一个整数来标识*/
	private List<Integer> data;
	/*保存节点的key信息，先后顺序是使用key来确定的*/
	private Map<Integer, Double> keys;
	/*using MAX to initiate key values of nodes*/
	private double MAX = Double.MAX_VALUE;

	public int size() {
		return data.size();
	}
	
	public Map<Integer, Integer> getIndexes() {
		return indexes;
	}

	public Map<Integer, Double> getKeys() {
		return keys;
	}

	//底层的实现使用动态数组 省掉了自己管理空间的工作
	public void init() {
		indexes = new HashMap<Integer,Integer>();
		data = new ArrayList<Integer>();		
		data.add(null);					//sentinel， 数组的第一个空间不使用，方便left和right函数的编写					
		keys = new HashMap<Integer,Double>();
	}
	
	//return the information of the node with the minimum key value
	public List<Double> getMin() {
		if(data.size() == 1) return null;
		List<Double> node_index_key = new ArrayList<Double>();
		int min = data.get(1);
		data.set(1, data.get(data.size()-1));
		indexes.put(data.get(1), 1);
		if(data.size() > 2) bubbleDown(data.get(1));
		//prepare data for returning
		node_index_key.add((double)min);
		node_index_key.add((double)indexes.get(min));
		node_index_key.add(keys.get(min));
		//delete node
		data.remove(data.size()-1);
		indexes.remove(min);
		keys.remove(min);
		return node_index_key;
	}
	
	//return the index of parent 
	public int parent(int index) {
		return index/2;
	}
	//return the index of left child
	public int left(int index) {
		return 2*index;
	}
	//return the index of right child
	public int right(int index) {
		return 2*index+1;
	}
	
	//逐级下降
	public void bubbleDown(int node) {
		int index = indexes.get(node);		
		int l = left(index);
		int r = right(index);
		int min;
		if(l < data.size() && keys.get(data.get(l)) < keys.get(data.get(index))) min = l;
		else min = index;
		if(r < data.size() && keys.get(data.get(r)) < keys.get(data.get(min))) min = r;
		if(min != index) {
			int swap = data.get(index);
			int minNode = data.get(min);
			data.set(index, data.get(min));
			data.set(min, swap);
			//indexes information need to be changed
			indexes.put(node, min);
			indexes.put(minNode, index);
			//keys information need to be changed out of the heap data structure
			bubbleDown(node);
		}
	}
	
	public void bubbleUp(int node) {
		int index = indexes.get(node);
		int parentIndex = parent(index);
		if(parentIndex >= 1) {
			int parentNode = data.get(parentIndex);
			if(keys.get(parentNode) > keys.get(node)) {
				data.set(index, parentNode);
				data.set(parentIndex, node);
				indexes.put(parentNode, index);
				indexes.put(node, parentIndex);
				bubbleUp(node);
			}
		}
	}
	
	//一般来说，在MST的实现中，newKey > key
	public void setKey(int node, double newKey) {
		double oldKey = keys.get(node);
		keys.put(node, newKey);
		//逐级下降
		if(newKey > oldKey) bubbleDown(node);
		//逐级上升
		if(newKey < oldKey) bubbleUp(node);
	}
	
	// create a heap from a List
	// arr contains indexes of nodes, such as 1-1000
	public void createHeap(List<Integer> list) {
		init();
		for(int i = 0; i < list.size(); i++) { 
			data.add(list.get(i));
			indexes.put(list.get(i), i+1);
			keys.put(list.get(i), MAX);
		}
		for(int i = data.size()/2; i > 0; i--) 
			bubbleDown(i);
	}
}
