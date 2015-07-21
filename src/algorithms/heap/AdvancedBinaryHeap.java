package algorithms.heap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author moqiguzhu
 *
 * @param <E>
 *            E是堆中存储的真实数据的类型
 *            Java给的API中，优先队列的实现采用PriorityQueue，但是这个结构在删除队列中某个元素的时候是线性时间的。
 *            于是我实现了这个类，这个heap类删除元素的时间是O(log(n))的，并且增加了动态修改元素key信息的功能。
 *            堆中元素的信息是根据key信息来确定的。 这个类可以用来实现快速的Dijkstra算法和Prim MST算法
 */
// 添加对key的泛型支持，类的正确定义应该是AdvancedBinaryHeap<E, K>
// 处理异常，强制类型转换报warning的地方需要修改
// 因为采用Map存储数据，这就要求堆中不能有两个完全一样的数据(缺陷？)
// 单元测试
public class AdvancedBinaryHeap<E, K> {
	/* 保存节点在heap中的位置信息 */
	private Map<E, Integer> element_index;
	/* heap的底层使用数组实现，节点使用一个整数来标识 */
	private List<E> data;
	/* 保存节点的key信息，先后顺序是使用key来确定的 */
	private Map<E, K> element_key;
	/* using MAX to initiate key values of nodes */
	private double MAX = Double.MAX_VALUE;

	public void init() {
		element_index = new HashMap<E, Integer>();
		data = new ArrayList<E>();
		data.add(null); // sentinel， 数组的第一个空间不使用，方便left和right函数的编写
		element_key = new HashMap<E, K>();
	}

	public AdvancedBinaryHeap() {
		super();
		init();
	}

	public AdvancedBinaryHeap(List<? extends E> c, List<? extends K> k) {
		init();
		E element;
		for (int i = 0; i < c.size(); i++) {
			element = c.get(i);
			data.add(element);
			element_index.put(element, i + 1);
			element_key.put(element, k.get(i));
		}
	}

	public AdvancedBinaryHeap(int initialCapacity) {
		element_index = new HashMap<E, Integer>();
		data = new ArrayList<E>(initialCapacity + 1);
		data.add(null); // sentinel， 数组的第一个空间不使用，方便left和right函数的编写
		element_key = new HashMap<E, K>();
	}

	// !!! to-do
	public AdvancedBinaryHeap(int initialCapacity, Comparator<? super Double> comparator) {

	}

	public Map<E, K> getKeys() {
		return element_key;
	}

	// return the index of parent
	public int parent(int index) {
		return index / 2;
	}

	// return the index of left child
	public int left(int index) {
		return 2 * index;
	}

	// return the index of right child
	public int right(int index) {
		return 2 * index + 1;
	}

	public int size() {
		return data.size() - 1;
	}

	public int compareKey(K k1, K k2) {
		// Cannot perform instanceof check against parameterized type
		// Comparator<? super K>.
		// Use the form Comparator<?> instead since further generic type
		// information will
		// be erased at runtime
		int result = 0;
		try {
			if (k1 instanceof Comparator<?> && k2 instanceof Comparator<?>) {
				Comparator<? super K> comparator = (Comparator<? super K>) k1;
				result = comparator.compare(k1, k1);
			} else if (k1 instanceof Comparable<?> && k2 instanceof Comparable<?>) {
				result = ((Comparable<K>) k1).compareTo(k2);
			} else {
				//
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 逐级下降
	public void bubbleDown(E element) {
		int index = element_index.get(element);
		int l = left(index);
		int r = right(index);
		int min;
		if (l < data.size() && compareKey(element_key.get(data.get(l)), element_key.get(data.get(index))) < 0) {
			min = l;
		} else {
			min = index;
		}
		if (r < data.size() && compareKey(element_key.get(data.get(r)), element_key.get(data.get(min))) < 0) {
			min = r;
		}
		if (min != index) {
			E swap = data.get(index);
			E minNode = data.get(min);
			data.set(index, data.get(min));
			data.set(min, swap);
			// indexes information need to be changed
			element_index.put(element, min);
			element_index.put(minNode, index);
			// keys information need to be changed out of the heap data
			// structure
			bubbleDown(element);
		}
	}

	public void bubbleUp(E element) {
		int index = element_index.get(element);
		int parentIndex = parent(index);
		if (parentIndex >= 1) {
			E parentNode = data.get(parentIndex);
			if (compareKey(element_key.get(parentNode), element_key.get(element)) > 0) {
				data.set(index, parentNode);
				data.set(parentIndex, element);
				element_index.put(parentNode, index);
				element_index.put(element, parentIndex);
				bubbleUp(element);
			}
		}
	}

	public void clear() {
		init();
	}

	// 一般来说，在MST的实现中，newKey > key
	public void setKey(E element, K newKey) {
		K oldKey = element_key.get(element);
		element_key.put(element, newKey);
		// 逐级下降
		if (compareKey(newKey, oldKey) > 0)
			bubbleDown(element);
		// 逐级上升
		if (compareKey(newKey, oldKey) < 0)
			bubbleUp(element);
	}

	// !!! to-do
	public Comparator<? super Double> comparator() {
		return null;
	}

	public boolean contains(Object object) {
		return element_key.containsKey((E) object);
	}

	public Iterator<E> iterator() {
		return data.subList(1, data.size()).iterator();
	}

	public boolean offer(E element, K key) {
		element_key.put(element, key);
		bubbleUp(element);
		return true; // 处理异常
	}

	// 返回最小元素和最小元素的key信息
	public List<Object> peek() {
		if (size() == 0) {
			return null;
		} else {
			List<Object> elementAndkey = new ArrayList<Object>();
			E min = data.get(1);
			elementAndkey.add((Object) min);
			elementAndkey.add((Object) element_key.get(min));

			return elementAndkey;
		}
	}

	// return the information of the node with the minimum key value
	// 请注意返回的是Object的List，在使用返回结果之前，需要显示的转换
	public List<Object> poll() {
		if (size() == 0) {
			return null;
		}
		List<Object> elementAndkey = new ArrayList<Object>();
		E min = data.get(1);
		data.set(1, data.get(data.size() - 1));
		element_index.put(data.get(1), 1);
		if (data.size() > 2) {
			bubbleDown(data.get(1));
		}
		// prepare data for returning
		elementAndkey.add((Object) min);
		elementAndkey.add((Object) element_key.get(min));
		// delete node
		data.remove(data.size() - 1);
		element_index.remove(min);
		element_key.remove(min);

		return elementAndkey;
	}

	public boolean remove(Object object) {
		//
		E element = (E) object;
		if (!contains(object)) {
			return false;
		} else {
			int index = element_index.get(element);
			data.remove(index);
			element_key.remove(element);
			element_index.remove(element);

			return true;
		}
	}
}
