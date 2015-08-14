package algorithms.heap;

import static org.junit.Assert.*;

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
	
	public int hashCode() {
		return this.id * 21;
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
	
	@Override
	public int hashCode() {
		return this.key * 21;
	}
}

//单元测试也要写泛型？
public class TestHeap {
	private List<Integer> expectedData;
	private Map<Integer, Integer> expectedElement_index;
	private Map<Integer, Double> expectedElement_key;
	
	private List<Element> expectedData1;
	private Map<Element, Integer> expectedElement_index1;
	private Map<Element, Key> expectedElement_key1;
	
	@Test
	public void testConstrutor() {
		Heap<Integer, Double> heap = new Heap<Integer, Double>();
		assertNotNull("should not be null", heap.getData());
		assertNull("should be null", heap.getComparator());
		assertNotNull("should not be null", heap.getElement_index());
		assertNotNull("should not be null", heap.getElement_Keys());
		assertEquals("failure - int should be equal", 0, heap.size());	
		
		Heap<Element, Key> heap1 = new Heap<Element, Key>();
		assertNotNull("should not be null", heap.getData());
		assertNull("should be null", heap.getComparator());
		assertNotNull("should not be null", heap.getElement_index());
		assertNotNull("should not be null", heap.getElement_Keys());
		assertEquals("failure - int should be equal", 0, heap.size());
	}
	
	@Test
	public void testOffer() {
		Heap<Integer, Double> heap = new Heap<Integer, Double>();
		heap.offer(1, 2.0);
		heap.offer(2, 3.0);
		expectedData = new ArrayList<Integer>(Arrays.asList(null,1,2));
		assertEquals("should be equal", heap.getData(), expectedData);
		
		expectedElement_index = new HashMap<Integer, Integer>();
		expectedElement_index.put(1, 1);
		expectedElement_index.put(2, 2);
		assertEquals("should be equal",heap.getElement_index(), expectedElement_index);
		
		Heap<Element, Key> heap1 = new Heap<Element, Key>();
		heap1.offer(new Element(1), new Key(2));
		heap1.offer(new Element(2), new Key(3));
		expectedData1 = new ArrayList<Element>(Arrays.asList(null, new Element(1), new Element(2)));
		assertEquals("should be equal",heap1.getData(), expectedData1);
		
		expectedElement_index1 = new HashMap<Element, Integer>();
		expectedElement_index1.put(new Element(1), 1);
		expectedElement_index1.put(new Element(2), 2);
		System.out.println(expectedElement_index1);
		//如何比较两个Map相等
		assertEquals("should be equal", heap1.getElement_index(), expectedElement_index1);
	}
	
	@Test
	public void testSetKey() {
		Heap<Integer, Double> heap = new Heap<>();
		heap.offer(1, 2.0);
		heap.offer(2, 3.0);
		heap.setKey(1, 100.0);
		
		expectedElement_key = new HashMap<Integer, Double>();
		expectedElement_key.put(1, 100.0);
		expectedElement_key.put(2, 3.0);
		assertEquals("should be equal",heap.getElement_Keys(), expectedElement_key);
		
		expectedData = new ArrayList<Integer>(Arrays.asList(null, 2, 1));
		assertEquals("should be equal", heap.getData(), expectedData);
	}
	
	@Test
	public void testPeek() {
		Heap<Integer, Double> heap = new Heap<>();
		heap.offer(1,2.0);
		heap.offer(2, 3.0);
		assertEquals("should be equal", heap.peek(), Arrays.asList(1,2.0));
		assertEquals("should be equal", heap.getData(), Arrays.asList(null,1,2));
	}
	
	@Test
	public void testPoll() {
		Heap<Integer, Double> heap = new Heap<>();
		heap.offer(1, 2.0);
		heap.offer(2, 3.0);
		assertEquals("should be equal", heap.poll(), Arrays.asList(1, 2.0));
		assertEquals("should be equal", heap.getData(), Arrays.asList(null, 2));
		assertEquals(heap.size(), 1);
	}
	
	@Test
	public void testContains() {
		
	}
	
	@Test
	public void testCompareKey() {
		
	}
	
	@Test
	public void testRemove() {
		
	}
}
