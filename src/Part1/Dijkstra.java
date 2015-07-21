package Part1;
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

class Node {
	private int label;			//节点标号
	private int dist;			//源节点到该节点的距离
	
	public Node(int l, int d) {
		label = l;
		dist = d;
	}
	
	//Override
	public String toString() {
		return "" + label + "  " + dist;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public int getDist() {
		return dist;
	}

	public void setDist(int dist) {
		this.dist = dist;
	}
}
public class Dijkstra {
	private Map<Integer,List<Node>> graph;
	private Queue<Node> queue;
	private int MAX = 1000000; 
	Map<Integer,Integer> result = new HashMap<Integer,Integer>();
	public void buildGraph() throws Exception{
		String path = "C:\\C\\llm\\研一\\Stanford Algorithms\\week5\\dijkstraData.txt";
		File f = new File(path);
		Scanner sc = new Scanner(f);
		String regex = "\\s";
		String regex1 = ",";
		String line;
		String[] strArr;
		int label = -1;
		graph = new HashMap<Integer,List<Node>>();	//初始化
		while(sc.hasNextLine()) {
			line = sc.nextLine();
			strArr = line.split(regex);
			if(strArr.length > 0) {
				label = Integer.valueOf(strArr[0]);
			}
			List<Node> tempList = new ArrayList<Node>();
			int n,d;
			for(int i = 1; i < strArr.length; i++) {
				n = Integer.valueOf(strArr[i].split(regex1)[0]);
				d = Integer.valueOf(strArr[i].split(regex1)[1]);
				Node node = new Node(n,d);
				tempList.add(node);
			}
			if(label != -1) graph.put(label, tempList);
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
			//此函数不需要重载
//			@Override
//			public boolean equals(Object obj) {
//				return true;
//			}
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
	public void DijkstraSingleSourceShortestPath() throws Exception{
		buildGraph();
//		for(int i = 1; i < 10; i++)
//			System.out.println(graph.get(1).toString());
		buildQueue();
		Node node;
		//这个时间复杂度有点高了
		while(!queue.isEmpty()) {
			int minDist = MAX;
			node = queue.peek();
			for(Node nod : queue) {
				if(nod.getDist() < minDist) {
					minDist = nod.getDist();
					node = nod;
				}
			}
			queue.remove(node);
			result.put(node.getLabel(), node.getDist());
			Set<Integer> set = new HashSet<Integer>();
			for(Node nd : graph.get(node.getLabel())) {
				set.add(nd.getLabel());
			}
			for(Node nd : queue) {
				if(set.contains(nd.getLabel())) {
					int dist = 0;		//dist一定会被重新赋值
					for(Node n : graph.get(node.getLabel())) {		//这里是可以优化的
						if(n.getLabel() == nd.getLabel())
							dist = n.getDist();
					}
					if(nd.getDist() > (node.getDist() + dist))
						nd.setDist((node.getDist() + dist));
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception{
		Dijkstra dij = new Dijkstra();
		dij.DijkstraSingleSourceShortestPath();
		int[] test = {7,37,59,82,99,115,133,165,188,197};
		for(int x : test)
			System.out.print(dij.result.get(x) + ",");
//		System.out.print(dij.result.toString());
	}
}
