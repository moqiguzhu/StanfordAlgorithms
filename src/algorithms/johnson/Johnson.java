package algorithms.johnson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import algorithms.graphedge.GraphEdge;
import algorithms.graphnode.GraphNode;
import algorithms.heap.Heap;

/**
 * 
 * @author moqiguzhu
 * @date 2015-08-18
 * @version 1.0
 */

public class Johnson {
	/* 存储结点及其相邻的节点信息(出度信息) */
	private Map<GraphNode, List<GraphNode>> node_neighborNodes = new HashMap<>();

	/* 存储节点及其相邻的节点信息(入度信息) */
	private Map<GraphNode, List<GraphNode>> neighborNodes_node = new HashMap<>();

	/* 存储图的边权值信息 */
	private Map<GraphEdge, GraphEdge> edge_edge = new HashMap<>();

	private Map<GraphEdge, GraphEdge> edge_edge1 = new HashMap<>();

	/* 存储所有的节点 */
	private Set<GraphNode> nodes = new HashSet<>();

	/* 到源节点的初始距离 */
	private double MAX = Double.MAX_VALUE;

	/* 所有节点对之间的最短距离 */
	private Map<GraphNode, Map<GraphNode, Double>> node_node_dist = new HashMap<>();

	/* 所有节点对最短路径中的最短路径的距离 */
	private double minDist = MAX;

	/* 节点的数目 */
	private int numNodes;

	public void createGraphFromFile(String path) {
		Scanner sc = null;
		File f = new File(path);
		try {
			sc = new Scanner(new File(path));
			String regex = "\\s";
			String line = sc.nextLine();
			String[] strArr;

			while (sc.hasNext()) {
				line = sc.nextLine();
				strArr = line.split(regex);
				
				//神奇的是，下面两种写法都是可行的
				GraphNode left = new GraphNode(Integer.valueOf(strArr[0]));
				GraphNode right = new GraphNode(Integer.valueOf(strArr[1]));
				
//				GraphNode left = new GraphNode(Integer.valueOf(strArr[1]));
//				GraphNode right = new GraphNode(Integer.valueOf(strArr[0]));
				
				double weight = Double.valueOf(strArr[2]);

				GraphEdge edge1 = new GraphEdge(left, right, weight);
				edge_edge.put(edge1, edge1);
				edge_edge1.put(edge1, edge1);

				if (!node_neighborNodes.containsKey(left)) {
					node_neighborNodes.put(left, new ArrayList<GraphNode>());
				}
				if (!neighborNodes_node.containsKey(right)) {
					neighborNodes_node.put(right, new ArrayList<GraphNode>());
				}
				node_neighborNodes.get(left).add(right);
				neighborNodes_node.get(right).add(left);

				nodes.add(left);
				nodes.add(right);
			}

			numNodes = nodes.size();
			GraphNode added = new GraphNode(numNodes + 1); // 新加的节点的label为numNodes+1
			node_neighborNodes.put(added, new ArrayList<GraphNode>());
			for (GraphNode node : nodes) {
				if (!node.equals(added)) {
					node_neighborNodes.get(added).add(node);
					if (!neighborNodes_node.containsKey(node)) {
						neighborNodes_node
								.put(node, new ArrayList<GraphNode>());
					}
					neighborNodes_node.get(node).add(added);

					GraphEdge edge1 = new GraphEdge(added, node, 0); // 新加的边权值全为0
					edge_edge.put(edge1, edge1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
	}

	/**
	 * Johnson算法
	 * @return
	 */
	public boolean solveAPSP() {
		Map<GraphNode, Double> shortestDists = new HashMap<>();
		if(!solveBellmanFord(shortestDists)) {
			return false;
		}

		List<Double> keys = new ArrayList<Double>();
		for(int i = 0; i < nodes.size(); i++) {
			keys.add(MAX);
		}
		
		for (GraphNode node : nodes) {
			Heap<GraphNode, Double> t_heap = new Heap<GraphNode, Double>(new ArrayList<GraphNode>(nodes), keys);
			System.out.println(keys);
			t_heap.setKey(node, .0);
			Map<GraphEdge, GraphEdge> t_edge_edge = new HashMap<>();
			
			for(GraphEdge edge : edge_edge1.keySet()) {
				GraphNode left = edge.getLeftNode();
				GraphNode right = edge.getRightNode();
				
				double weight1 = shortestDists.get(left) - 
						shortestDists.get(right) + edge.getWeight();
				GraphEdge edge1 = new GraphEdge(left, right, weight1);
				
				t_edge_edge.put(edge1, edge1);
			}
			
			Map<GraphNode, Double> initDists = new HashMap<>();
			for(GraphNode tnode : nodes) {
				if(tnode.equals(node)) {
					initDists.put(tnode, .0);
				} else {
					initDists.put(tnode, MAX);
				}
			}
			
			Map<GraphNode, Double> dists = solveDijkstra(t_heap, node_neighborNodes, t_edge_edge, initDists);
			node_node_dist.put(node, dists);
		}
		
		for(GraphNode node : node_node_dist.keySet()) {
			for(GraphNode tnode : node_node_dist.get(node).keySet()) {
				double weight = node_node_dist.get(node).get(tnode) - shortestDists.get(node) + shortestDists.get(tnode);
				node_node_dist.get(node).put(tnode, weight);
				minDist = Math.min(weight, minDist);
			}
		}
		
		return true;
	}

	/**
	 * Dijkstra算法
	 * @param heap
	 * @param node_neighborNodes
	 * @param edge_edge
	 * @param shortestDists
	 * @return
	 */
	public Map<GraphNode, Double> solveDijkstra(Heap<GraphNode, Double> heap,
			Map<GraphNode, List<GraphNode>> node_neighborNodes,
			Map<GraphEdge, GraphEdge> edge_edge,
			Map<GraphNode, Double> shortestDists) {

		while (heap.size() > 0) {
			GraphNode node = (GraphNode) heap.poll().get(0);
			for (GraphNode tnode : node_neighborNodes.get(node)) {
				GraphEdge tedge = new GraphEdge(node, tnode, 0);
				double oldDist = shortestDists.get(tnode);

				double newDist = shortestDists.get(node)
						+ edge_edge.get(tedge).getWeight();
				if (newDist < oldDist) {
					heap.setKey(tnode, newDist);
					shortestDists.put(tnode, newDist);
				}
			}
		}

		return shortestDists;
	}
	
	/**
	 * Bellman-Ford算法
	 * @param shortestDists
	 * @return
	 */
	public boolean solveBellmanFord(Map<GraphNode, Double> shortestDists) {
		int numNodes = nodes.size() + 1;
		double[][] numNodes_node = new double[2][numNodes + 1];

		// 初始化
		for (int j = 1; j < numNodes + 1; j++) {
			if (j == numNodes) {
				numNodes_node[0 % 2][j] = 0;
			} else {
				numNodes_node[0 % 2][j] = MAX;
			}
		}

		// Bellman-Ford算法
		for (int i = 1; i < numNodes + 1; i++) {
			for (int j = 1; j < numNodes + 1; j++) {
				double temp = numNodes_node[(i - 1) % 2][j];

				GraphNode node = new GraphNode(j);
				if (neighborNodes_node.get(node) != null) {
					for (GraphNode nd : neighborNodes_node.get(node)) {
						GraphEdge edge = new GraphEdge(nd, node, 0);
						double ttemp = numNodes_node[(i - 1) % 2][nd.getLabel()]
								+ edge_edge.get(edge).getWeight();
						temp = Math.min(ttemp, temp);
					}
				}
				numNodes_node[i % 2][j] = temp;
			}
		}

		// 判断是不是有negative cycle
		for (int j = 1; j < numNodes + 1; j++) {
			if (numNodes_node[(numNodes - 1) % 2][j] != numNodes_node[(numNodes) % 2][j]) {
				System.out.println("图中存在negative cycle!!!");
				return false;
			}
		}

		// 所有节点到源节点的最短路径
		for (GraphNode node : node_neighborNodes.keySet()) {
			shortestDists.put(node,
					numNodes_node[numNodes % 2][node.getLabel()]);
		}

		return true;
	}
	
	/**
	 * 
	 * @return 所有节点对之间的最短距离
	 */
	public double getMinDist() {
		return minDist;
	}

	// test this class
	public static void main(String[] args) {
		Johnson johnson = new Johnson();
		String path = ".\\testdata\\apsp3.txt";

		long start = System.currentTimeMillis();
		johnson.createGraphFromFile(path);
		long end = System.currentTimeMillis();
		System.out.println("读文件耗时： " + (end - start) / (double) 1000 + "s");

		start = System.currentTimeMillis();
		johnson.solveAPSP();
		end = System.currentTimeMillis();
		System.out
				.println("Johnson算法耗时：" + (end - start) / (double) 1000 + "s");
		System.out.println("所有节点对之间的最短距离为： " + johnson.getMinDist());
	}

}
