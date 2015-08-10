package algorithms.dijkstra;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import algorithms.graphedge.GraphEdge;
import algorithms.graphnode.GraphNode;
import algorithms.heap.AdvancedBinaryHeap;

/**
 * @author moqiguzhu
 * @date 2015-08-10
 * @version 1.0
 */
/*****************************************************
 *Dijkstra提出来的单源最短路径算法，整个算法的时间复杂度为nlog(n)，n是
 *图中节点的个数。使用一个Map来保存每个节点和其邻接节点的信息，使用一个Map
 *保存图中所有的边权值信息。之所以使用两个Map来保存一个图的信息，完全是为了
 *算法的可读性。核心代码在solveDijkstra函数中，总共不过十来行。
 *
 *****************************************************/

public class Dijkstra {
	/*key为节点到源节点之间的当前距离*/
	private AdvancedBinaryHeap<GraphNode, Double> heap;
	/*存储图的边权值信息*/
	private Map<GraphEdge, GraphEdge> edge_edge;
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
		edge_edge = new HashMap<GraphEdge, GraphEdge>();	
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
					GraphEdge edge1 = new GraphEdge(source, dest, weight);
					GraphEdge edge2 = new GraphEdge(dest, source, weight);
					//无向图
					edge_edge.put(edge1, edge1);
					edge_edge.put(edge2, edge2);
					node_neighborNodes.get(source).add(dest);
				}
			}
		}
		sc.close();
	}
	
	public Map<GraphNode, Double> getShortestDists() {
		return shortestDists;
	}
	
	public void solveDijkstra() {
		while(heap.size() > 0) {
			GraphNode node = (GraphNode)heap.poll().get(0);
			for(GraphNode tnode: node_neighborNodes.get(node)) {
				GraphEdge tedge = new GraphEdge(node, tnode, 0);
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
		dij.solveDijkstra();
		Map<GraphNode, Double> shortestDists = dij.getShortestDists();
		System.out.println(shortestDists);
	}
}
