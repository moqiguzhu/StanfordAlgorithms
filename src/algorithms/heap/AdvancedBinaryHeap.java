package algorithms.heap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//添加泛型支持
//Java给的API中，优先队列的实现采用PriorityQueue，但是这个结构在删除队列中某个元素的时候是线性时间的
//你必须要知道怎么初始化这个类才能使用它
//明天继续，单元测试
//AdvancedBinaryHeap<E, K>这才是正确的定义，但是泛型应该怎么写目前尚不清楚，学习相关的泛型知识
//key信息使用Double数据来表示
//各种异常都没有处理
//index都不需要返回吧
//因为采用Map存储数据，同一个元素在Heap中不能出现多次，不过，还是看你怎么用了
public class AdvancedBinaryHeap<E> {
    /*保存节点在heap中的位置信息*/
    private Map<E, Integer> element_index;
    /*heap的底层使用数组实现，节点使用一个整数来标识*/
    private List<E> data;
    /*保存节点的key信息，先后顺序是使用key来确定的*/
    private Map<E, Double> element_key;
    /*using MAX to initiate key values of nodes*/
    private double MAX = Double.MAX_VALUE;
    
    public void init() {
        element_index = new HashMap<E,Integer>();
        data = new ArrayList<E>();      
        data.add(null);                 //sentinel， 数组的第一个空间不使用，方便left和right函数的编写                   
        element_key = new HashMap<E,Double>();
    }
    
    public AdvancedBinaryHeap() {
        super();
        init();
    }
    
    public AdvancedBinaryHeap(Collection<? extends E> c) {
        init();
        int index = 1;
        for(E element : c) {
            data.add(element);
            element_index.put(element, index++);        
            element_key.put(element, MAX);		//使用默认值
        }
    }
    
    public AdvancedBinaryHeap(int initialCapacity) {
        element_index = new HashMap<E,Integer>();
        data = new ArrayList<E>(initialCapacity+1);      
        data.add(null);                 //sentinel， 数组的第一个空间不使用，方便left和right函数的编写                   
        element_key = new HashMap<E,Double>();
    }
    
    //!!! to-do
    public AdvancedBinaryHeap(int initialCapacity, Comparator<? super Double> comparator) {
    	
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
    
    public int size() {
        return data.size() - 1;
    }
    
    public Map<E, Integer> getIndexes() {
        return element_index;
    }

    public Map<E, Double> getKeys() {
        return element_key;
    }
    
    //逐级下降
    public void bubbleDown(E element) {
        int index = element_index.get(element);     
        int l = left(index);
        int r = right(index);
        int min;
        if(l < data.size() && element_key.get(data.get(l)) < element_key.get(data.get(index))) {
            min = l;
        } else {
            min = index;
        }
        if(r < data.size() && element_key.get(data.get(r)) < element_key.get(data.get(min))) {
            min = r;
        }
        if(min != index) {
            E swap = data.get(index);
            E minNode = data.get(min);
            data.set(index, data.get(min));
            data.set(min, swap);
            //indexes information need to be changed
            element_index.put(element, min);
            element_index.put(minNode, index);
            //keys information need to be changed out of the heap data structure
            bubbleDown(element);
        }
    }
    
    public void bubbleUp(E element) {
        int index = element_index.get(element);
        int parentIndex = parent(index);
        if(parentIndex >= 1) {
            E parentNode = data.get(parentIndex);
            if(element_key.get(parentNode) > element_key.get(element)) {
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
    
	//一般来说，在MST的实现中，newKey > key
	public void setKey(E element, double newKey) {
		double oldKey = element_key.get(element);
		element_key.put(element, newKey);
		//逐级下降
		if(newKey > oldKey) bubbleDown(element);
		//逐级上升
		if(newKey < oldKey) bubbleUp(element);
	}
	
	//!!! to-do
	public Comparator<? super Double> comparator() {  
		return null;
	}
	
	public boolean contains(Object object) {
		return element_key.containsKey((E)object);
	}
	
	//!!! to-do
	public Iterator<E> iterator() {
		return null;
	}
	//这个key的命名太具有欺骗性了
	public boolean offer(E element, double key) {
		element_key.put(element, key);
		bubbleUp(element);
		return true;			//处理异常
	}
	
	//返回最小元素和最小元素的key信息
	public List<Object> peek() { 
		if(size() == 0) {
			return null;
		} else {
			List<Object> elementAndkey = new ArrayList<Object>();
			E min = data.get(1);
			elementAndkey.add((Object)min);
			elementAndkey.add((Object)element_key.get(min));
	        
	        return elementAndkey;
		}
	}
	
    //return the information of the node with the minimum key value
    //请注意返回的是Object的List，在使用返回结果之前，需要显示的转换
    public List<Object> poll() {
        if(size() == 0) {
        	return null;
        }
        List<Object> elementAndkey = new ArrayList<Object>();
        E min = data.get(1);
        data.set(1, data.get(data.size()-1));
        element_index.put(data.get(1), 1);
        if(data.size() > 2) {
        	bubbleDown(data.get(1));
        }
        //prepare data for returning
        elementAndkey.add((Object)min);
        elementAndkey.add((Object)element_key.get(min));
        //delete node
        data.remove(data.size()-1);
        element_index.remove(min);
        element_key.remove(min);
        return elementAndkey;
    }
    
    public boolean remove(Object object) {
    	E element = (E)object;
		if(!contains(object)) {
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
