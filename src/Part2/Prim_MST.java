package Part2;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Prim_MST {
	private Map<Integer,List<Node>> graph;
	private Queue<Node> queue;
	private int MAX = 1000000;
	/*cost of building MST*/
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
	
	public void buildQueue() {
		queue = new PriorityQueue<Node>(10, new Comparator<Node>(){
			@Override
			public int compare(Node n1, Node n2) {
				if(n1.getDist() > n2.getDist()) return 1;
				else if(n1.getDist() < n2.getDist()) return -1;
				else return 0;
			}
		});
		for(int x : graph.keySet()) {
			Node node;
			if(x == 1) node = new Node(x,0);
			else node = new Node(x,MAX);
			queue.add(node);
		}
	}
	
	//使用优先队列的话需要在优先队列中建立节点和节点在优先队列中位置两者之间的联系 这样才能快速修改到源节点的距离信息
	//这就要求自己实现优先队列 因为在删除已经处理过的节点的时候位置信息会变化 这种变化需要及时捕获并记录下来
	public void prim_MST() throws Exception{
		buildGraph();
//		for(int i = 1; i < 10; i++)
//			System.out.println(graph.get(1).toString());
		buildQueue();
		Node node;
		//这个时间复杂度有点高了
		while(!queue.isEmpty()) {
			double minDist = MAX;
			node = queue.peek();
			for(Node nod : queue) {
				if(nod.getDist() < minDist) {
					minDist = nod.getDist();
					node = nod;
				}
			}
			queue.remove(node);
			Set<Integer> set = new HashSet<Integer>();
			for(Node nd : graph.get(node.getLabel())) {
				set.add(nd.getLabel());
			}
			for(Node nd : queue) {
				if(set.contains(nd.getLabel())) {
					double dist = 0;		//dist一定会被重新赋值
					for(Node n : graph.get(node.getLabel())) {		//这里是可以优化的
						if(n.getLabel() == nd.getLabel())
							dist = n.getDist();
					}
					if(nd.getDist() > dist)
						nd.setDist(dist);
				}
			}
			cost += node.getDist();
		}
	}

	public static void main(String[] args) throws Exception {
		Prim_MST mst = new Prim_MST();
		long start = System.currentTimeMillis();
		mst.prim_MST();
		long end = System.currentTimeMillis();
		System.out.println("cost = " + Prim_MST.cost);
		System.out.println(end - start);
	}
}

