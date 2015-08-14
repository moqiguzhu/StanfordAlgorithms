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
 */

// Java给的API中，优先队列的实现采用PriorityQueue，但是这个结构不支持在堆中快速查找元素
// 于是我实现了这个类，这个heap类删除元素的时间是O(log(n))的，并且增加了动态修改元素key信息的功能。
// 堆中元素的信息是根据key信息来确定的。 这个类可以用来实现快速的Dijkstra算法和Prim MST算法

// 添加对key的泛型支持，类的正确定义应该是AdvancedBinaryHeap<E, K>
// 处理异常，强制类型转换报warning的地方需要修改
// 因为采用Map存储数据，这就要求堆中不能有两个完全一样的数据(缺陷？)
// 泛型单元测试
public class Heap<E, K> {
	/* 保存节点在heap中的位置信息 */
	private Map<E, Integer> element_index;
	/* heap的底层使用数组实现，节点使用一个整数来标识 */
	private List<E> data;
	/* 保存节点的key信息，先后顺序是使用key来确定的 */
	private Map<E, K> element_key;
	/* 用于比较key */
	Comparator<? super K> comparator = null;

	public void init() {
		element_index = new HashMap<E, Integer>();
		data = new ArrayList<E>();
		data.add(null); // sentinel， 数组的第一个空间不使用，方便left和right函数的编写
		element_key = new HashMap<E, K>();
	}

	public Heap() {
		super();
		init();
	}

	public Heap(List<? extends E> c, List<? extends K> k) {
		init();
		E element;
		for (int i = 0; i < c.size(); i++) {
			element = c.get(i);
			data.add(element);
			element_index.put(element, i + 1);
			element_key.put(element, k.get(i));
		}
		for (int i = data.size() / 2; i > 0; i--) {
			bubbleDown(data.get(i));
		}
	}

	public Heap(int initialCapacity) {
		element_index = new HashMap<E, Integer>();
		data = new ArrayList<E>(initialCapacity + 1);
		data.add(null); // sentinel， 数组的第一个空间不使用，方便left和right函数的编写
		element_key = new HashMap<E, K>();
	}

	//
	public Heap(int initialCapacity, Comparator<? super K> comparator) {
		element_index = new HashMap<E, Integer>();
		data = new ArrayList<E>(initialCapacity + 1);
		data.add(null); // sentinel， 数组的第一个空间不使用，方便left和right函数的编写
		element_key = new HashMap<E, K>();
		this.comparator = comparator;
	}

	public Map<E, K> getElement_Keys() {
		return element_key;
	}

	protected List<E> getData() {
		return data;
	}

	protected Map<E, Integer> getElement_index() {
		return element_index;
	}

	protected Comparator<? super K> getComparator() {
		return comparator;
	}

	// return the index of parent
	private int parent(int index) {
		return index / 2;
	}

	// return the index of left child
	private int left(int index) {
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
		// Comparator<? super K>.Use the form Comparator<?> instead since
		// further generic type information will be erased at runtime
		int result = 0;
		if (comparator != null) {
			return comparator.compare(k1, k2);
		} else {
			try {
				if (k1 instanceof Comparator<?> && k2 instanceof Comparator<?>) {
					Comparator<? super K> localComparator = (Comparator<? super K>) k1;
					result = localComparator.compare(k1, k1);
				} else if (k1 instanceof Comparable<?> && k2 instanceof Comparable<?>) {
					result = ((Comparable<K>) k1).compareTo(k2);
				} else {
					throw new ClassCastException();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		if (compareKey(newKey, oldKey) > 0) {
			bubbleDown(element);
		}
		// 逐级上升
		if (compareKey(newKey, oldKey) < 0) {
			bubbleUp(element);
		}
	}
	
	public K getKey(E element) {
		return element_key.get(element);
	}

	// 有可能返回null
	public Comparator<? super K> comparator() {
		return this.comparator;
	}

	public boolean contains(Object object) {
		return element_key.containsKey((E) object);
	}

	public Iterator<E> iterator() {
		return data.subList(1, data.size()).iterator();
	}

	public boolean offer(E element, K key) {
		data.add(element);
		element_key.put(element, key);
		element_index.put(element, size());
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

	// O(n)的时间复杂度
	// 并且存在错误，因为如果两个element的key是一样的话，它们在数组中的位置实际上是任意的
	// 但是如果能保证element不重复，这个函数可以使用
	@Deprecated
	public boolean remove(Object object) {
		E element = (E) object;
		if (!contains(object)) {
			return false;
		} else {
			for (E e : element_index.keySet()) {
				if (compareKey(element_key.get(element), element_key.get(e)) < 0) {
					element_index.put(e, element_index.get(e) - 1);
				}
			}
			int index = element_index.get(element);
			data.remove(index);
			element_key.remove(element);
			element_index.remove(element);

			return true;
		}
	}

	public static void main(String[] args) {
		Heap<Integer, Double> heap = new Heap<Integer, Double>();

		// test offer function
		heap.offer(1, 2.0);
		System.out.println(heap.getData()); // should be {null, 1}
		heap.offer(2, 3.0);
		System.out.println(heap.getData()); // should be {null, 1, 2}

		// test setKey function
		heap.setKey(1, 100.0);
		System.out.println(heap.getData()); // should be {null, 2, 1}
		heap.setKey(2, 1000.0);
		System.out.println(heap.getData()); // should be {null, 1, 2}
		System.out.println(heap.getElement_Keys()); // shoule be {1=100.0,
													// 2=1000.0}

		// test peek function
		System.out.println(heap.peek()); // should be {1, 100.0}
		System.out.println(heap.getData()); // should be {null, 1, 2}

		// test poll function
		System.out.println(heap.poll()); // should be {1, 100.0}
		System.out.println(heap.getData()); // should be {null,2}

		// test contains function
		heap.offer(3, 4.0);
		System.out.println(heap.contains(3)); // should be true
		System.out.println(heap.contains(2)); // should be true
		System.out.println(heap.contains(1)); // should be false

		// test compareKey function
		int temp = heap.compareKey(heap.getElement_Keys().get(2), heap.getElement_Keys().get(3));
		System.out.println(temp > 0); // should be true
		heap.offer(4, 10000.0);
		temp = heap.compareKey(heap.getElement_Keys().get(2), heap.getElement_Keys().get(4));
		System.out.println(temp > 0); // should be false

		// test remove function
		heap.remove(2);
		System.out.println(heap.getData()); // should be {null, 3, 4}
		System.out.println(heap.getElement_index());// should be {3=1, 4=2}
		
		//
	}
}
