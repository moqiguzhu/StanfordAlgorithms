package algorithms.dijkstra;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import algorithms.edge.Edge;
import algorithms.graphnode.GraphNode;
import algorithms.heap.AdvancedBinaryHeap;

public class Dijkstra {
	/*key为节点到源节点之间的当前距离*/
	private AdvancedBinaryHeap<GraphNode, Double> heap;
	/*存储图的边权值信息*/
	private Map<Edge, Edge> edge_edge;
	/*存储图的节点之间的连接信息*/
	private Map<GraphNode, List<GraphNode>> node_neighborNodes;
	/*key为节点到源节点之间的最短距离*/
	private Map<GraphNode, Double> shortestDists;
	/*到源节点的初始距离*/
	private double MAX = Double.MAX_VALUE;
	
	public void createGraphFromFile(String path) throws Exception {
		//局部变量定义
		Scanner sc = new Scanner(new File(path));
		String regex = "\\s";
		String regex1 = ",";
		String line;
		String[] strArr;
		GraphNode source, dest;
		
		//初始化
		heap = new AdvancedBinaryHeap<GraphNode, Double>();
		edge_edge = new HashMap<Edge, Edge>();	
		node_neighborNodes = new HashMap<GraphNode, List<GraphNode>>();
		shortestDists = new HashMap<GraphNode, Double>();
		
		//读文件
		while(sc.hasNextLine()) {
			line = sc.nextLine();
			strArr = line.split(regex);
			source = new GraphNode(Integer.valueOf(strArr[0]));
			node_neighborNodes.put(source, new ArrayList<GraphNode>());
			if(Integer.valueOf(strArr[0]) == 1) {			//源节点
				heap.offer(source, .0);
				shortestDists.put(source, .0);
			} else {
				heap.offer(source, MAX);
				shortestDists.put(source, MAX);
			}
			if(strArr.length > 0) {
				double weight;
				for(int i = 1; i < strArr.length; i++) {
					dest = new GraphNode(Integer.valueOf(strArr[i].split(regex1)[0]));
					weight = Double.valueOf(strArr[i].split(regex1)[1]);
					Edge edge1 = new Edge(source, dest, weight);
					Edge edge2 = new Edge(dest, source, weight);
					//无向图
					edge_edge.put(edge1, edge1);
					edge_edge.put(edge2, edge2);
					node_neighborNodes.get(source).add(dest);
				}
			}
		}
		sc.close();
	}
	
	public Map<Edge, Edge> getEdges() {
		return edge_edge;
	}
	
	public Map<GraphNode, Double> getShortestDists() {
		return shortestDists;
	}
	
	public void dijkstraSingleSourceShortestPath() {
		while(heap.size() > 0) {
			GraphNode node = (GraphNode)heap.poll().get(0);
			for(GraphNode tnode: node_neighborNodes.get(node)) {
				Edge tedge = new Edge(node, tnode, 0);
				double oldDist = shortestDists.get(tnode);
				//需要new一个Edge，不优雅
				double newDist = shortestDists.get(node) + edge_edge.get(tedge).getWeight();
				if(newDist < oldDist) {
					heap.setKey(tnode, newDist);
					shortestDists.put(tnode, newDist);
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		Dijkstra dij = new Dijkstra();
		String path = ".\\testdata\\Dijkstra.txt";
		dij.createGraphFromFile(path);
		dij.dijkstraSingleSourceShortestPath();
		Map<GraphNode, Double> shortestDists = dij.getShortestDists();
		System.out.println(shortestDists);
//		System.out.println(dij.getEdges());
	}
}
