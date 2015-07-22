package algorithms.heap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

class Element {
	private int id;
	public Element(int id) {
		// TODO Auto-generated constructor stub
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o) {
		Element element = (Element)o;
		return this.id == element.id;
	}
	
	@Override
	public String toString() {
		return "" + id;
	}
}

class Key implements Comparator<Key> {
	private int key;
	
	public Key(int key) {
		// TODO Auto-generated constructor stub
		this.key = key;
	}
	
	@Override
	public int compare(Key o1, Key o2) {
		// TODO Auto-generated method stub
		return o1.key - o2.key;
	}
	
	@Override
	public boolean equals(Object o1) {
		Key k1 = (Key)o1;
		return k1.key == this.key;
	}
}

//单元测试也要写泛型？
public class TestAdvancedBinaryHeap {
	private List<Integer> expectedData;
	private Map<Integer, Integer> expectedElement_index;
	private Map<Integer, Double> expectedElement_key;
	
	private List<Element> expectedData1;
	private Map<Element, Integer> expectedElement_index1;
	private Map<Element, Key> expectedElement_key1;
	
	@Test
	public void testConstrutor() {
		AdvancedBinaryHeap<Integer, Double> heap = new AdvancedBinaryHeap<Integer, Double>();
		org.junit.Assert.assertNotNull("should not be null", heap.getData());
		org.junit.Assert.assertNull("should be null", heap.getComparator());
		org.junit.Assert.assertNotNull("should not be null", heap.getElement_index());
		org.junit.Assert.assertNotNull("should not be null", heap.getElement_Keys());
		org.junit.Assert.assertEquals("failure - int should be equal", 0, heap.size());	
		
		AdvancedBinaryHeap<Element, Key> heap1 = new AdvancedBinaryHeap<Element, Key>();
		org.junit.Assert.assertNotNull("should not be null", heap.getData());
		org.junit.Assert.assertNull("should be null", heap.getComparator());
		org.junit.Assert.assertNotNull("should not be null", heap.getElement_index());
		org.junit.Assert.assertNotNull("should not be null", heap.getElement_Keys());
		org.junit.Assert.assertEquals("failure - int should be equal", 0, heap.size());
	}
	
	@Test
	public void testOffer() {
		AdvancedBinaryHeap<Integer, Double> heap = new AdvancedBinaryHeap<Integer, Double>();
		heap.offer(1, 2.0);
		heap.offer(2, 3.0);
		expectedData = new ArrayList<Integer>(Arrays.asList(null,1,2));
		org.junit.Assert.assertEquals("should be equal", heap.getData(), expectedData);
		
		expectedElement_index = new HashMap<Integer, Integer>();
		expectedElement_index.put(1, 1);
		expectedElement_index.put(2, 2);
		org.junit.Assert.assertEquals("should be equal",heap.getElement_index(), expectedElement_index);
		
		AdvancedBinaryHeap<Element, Key> heap1 = new AdvancedBinaryHeap<Element, Key>();
		heap1.offer(new Element(1), new Key(2));
		heap1.offer(new Element(2), new Key(3));
		expectedData1 = new ArrayList<Element>(Arrays.asList(null, new Element(1), new Element(2)));
		org.junit.Assert.assertEquals("should be equal",heap1.getData(), expectedData1);
		
		expectedElement_index1 = new HashMap<Element, Integer>();
		expectedElement_index1.put(new Element(1), 1);
		expectedElement_index1.put(new Element(2), 2);
		System.out.println(expectedElement_index1);
		//如何比较两个Map相等
		
	}
	
	@Test
	public void testSetKey() {
		AdvancedBinaryHeap<Integer, Double> heap = new AdvancedBinaryHeap<>();
		heap.offer(1, 2.0);
		heap.offer(2, 3.0);
		heap.setKey(1, 100.0);
		
		expectedElement_key = new HashMap<Integer, Double>();
		expectedElement_key.put(1, 100.0);
		expectedElement_key.put(2, 3.0);
		org.junit.Assert.assertEquals("should be equal",heap.getElement_Keys(), expectedElement_key);
		
		expectedData = new ArrayList<Integer>(Arrays.asList(null, 2, 1));
		org.junit.Assert.assertEquals("should be equal", heap.getData(), expectedData);
	}
}
