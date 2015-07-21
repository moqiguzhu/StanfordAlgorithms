package Part2;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FastPrimMST {
	private Heap heap;
	private Map<Integer,List<Node>> graph;
	private static int cost = 0;
	
	public void buildGraph() throws Exception{
		String path = "C:\\Master\\Stanford Algorithms 2\\edges.txt";
		File f = new File(path);
		Scanner sc = new Scanner(f);
		String regex = "\\s";
		String line;
		String[] strArr;
		int label1, label2;
		graph = new HashMap<Integer,List<Node>>();	//初始化
		strArr = sc.nextLine().split(regex);
		int numNodes = Integer.valueOf(strArr[0]);
		for(int i = 0; i < numNodes; i++) {
			List<Node> tempList = new ArrayList<Node>();
			graph.put(i+1, tempList);
		}
		while(sc.hasNextLine()) {
			line = sc.nextLine();
			strArr = line.split(regex);
			label1 = Integer.valueOf(strArr[0]);
			label2 = Integer.valueOf(strArr[1]);
			int n1 = Integer.valueOf(strArr[1]), d1 = Integer.valueOf(strArr[2]);
			int n2 = Integer.valueOf(strArr[0]), d2 = d1;
			Node node1 = new Node(n1, d1), node2 = new Node(n2, d2);
			graph.get(label1).add(node1);
			graph.get(label2).add(node2);
		}
		sc.close();
	}
	
	public void createHeap() {
		heap = new Heap();
		List<Integer> list = new ArrayList<Integer>();
		list.addAll(graph.keySet());
		heap.createHeap(list);
	}
	
	public void primMST() {
		//记录节点被加到最小生成树中的顺序
		List<Integer> orders = new ArrayList<Integer>();
		double key;
		int node;
		List<Double> node_index_key;
		while(heap.size() > 1) {
			node_index_key = heap.getMin();
			node = (int)(node_index_key.get(0) / 1);
			key = node_index_key.get(2);
			if(orders.size() == 0) cost += 0;
			else cost += key;
			orders.add(node);
			//change keys
			for(Node nd : graph.get(node)) {
				if(heap.getIndexes().containsKey(nd.getLabel())) {
//					System.out.println(heap.getKeys());
					if(nd.getDist() < heap.getKeys().get(nd.getLabel()))
						heap.setKey(nd.getLabel(), nd.getDist());
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception{
		long start = System.currentTimeMillis();
		FastPrimMST fmst = new FastPrimMST();
		fmst.buildGraph();
		fmst.createHeap();
		fmst.primMST();
		long end = System.currentTimeMillis();
		System.out.println("cost = " + cost);
		System.out.println(end - start);
	}
}
