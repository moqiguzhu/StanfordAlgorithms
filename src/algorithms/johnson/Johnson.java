package algorithms.johnson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import algorithms.bellmanford.BellmanFord;
import algorithms.graphedge.GraphEdge;
import algorithms.graphedge.GraphEdgeDirected;
import algorithms.graphnode.GraphNode;
import algorithms.heap.Heap;

public class Johnson {
	/* 存储结点及其相邻的节点信息 */
	private Map<GraphNode, List<GraphNode>> node_neighborNodes = new HashMap<>();

	/* 存储图的边权值信息 */
	private Map<GraphEdge, GraphEdge> edge_edge = new HashMap<>();

	/* 到源节点的初始距离 */
	private double MAX = Double.MAX_VALUE;
	
	/* 所有节点对之间的最短距离*/
	private Map<GraphNode, Map<GraphNode, Double>> node_node_dist = new HashMap<>();
	
	/* 所有节点对最短路径中的最短路径的距离*/
	private double minDist = MAX;

	public void createGraphFromFile(String path) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(path));
			String regex = "\\s";
			String line = sc.nextLine();
			String[] strArr;

			while (sc.hasNext()) {
				line = sc.nextLine();
				strArr = line.split(regex);
				GraphNode left = new GraphNode(Integer.valueOf(strArr[1]));
				GraphNode right = new GraphNode(Integer.valueOf(strArr[0]));
				double weight = Double.valueOf(strArr[2]);
				
				GraphEdge edge = new GraphEdge(left, right, weight);
				edge_edge.put(edge, edge);
				
				if(!node_neighborNodes.containsKey(left)) {
					node_neighborNodes.put(left, new ArrayList<GraphNode>());
				}
				node_neighborNodes.get(left).add(right);
			}
			
			GraphNode added = new GraphNode(-1);  				//新加的节点的label为-1
			node_neighborNodes.put(added, new ArrayList<GraphNode>());
			for(GraphNode node : node_neighborNodes.keySet()) {
				node_neighborNodes.get(added).add(node);
				GraphEdge edge = new GraphEdge(added, node, 0); //新加的边权值全为0
				edge_edge.put(edge, edge);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(sc != null) {
				sc.close();
			}
		}
	}

	public void solveAPSP() {
		BellmanFord bl = new BellmanFord(node_neighborNodes, edge_edge);
		Map<GraphNode, Double> shortestDists = bl.getShortestDists();

		List<GraphNode> nodes = new ArrayList<GraphNode>(
				node_neighborNodes.keySet());
		List<Double> keys = new ArrayList<Double>(nodes.size());
		for(int i = 0; i < keys.size(); i++) {
			keys.set(i, MAX);
		}
		
		for (GraphNode node : nodes) {
			if(node.getLabel() != -1) {					//新加的节点不予考虑
				Heap<GraphNode, Double> t_heap = new Heap<GraphNode, Double>(nodes, keys);
				t_heap.setKey(node, .0);
				Map<GraphEdgeDirected, GraphEdgeDirected> t_edge_edge = new HashMap<>();
				
				for(GraphEdge edge : edge_edge.keySet()) {
					GraphNode left = edge.getLeftNode();
					GraphNode right = edge.getRightNode();
					
					double weight1 = shortestDists.get(left) - 
							shortestDists.get(right) + edge.getWeight();
					double weight2 = shortestDists.get(right) - 
							shortestDists.get(left) + edge.getWeight();
					
					GraphEdgeDirected edge1 = new GraphEdgeDirected(left, right, weight1);
					GraphEdgeDirected edge2 = new GraphEdgeDirected(right, left, weight2);
					
					t_edge_edge.put(edge1, edge1);
					t_edge_edge.put(edge2, edge2);
				}
				
				Map<GraphNode, Double> dists = solveDijkstra(t_heap, node_neighborNodes, t_edge_edge);
				node_node_dist.put(node, dists);
			}
		}
	}

	public Map<GraphNode, Double> solveDijkstra(Heap<GraphNode, Double> heap,
			Map<GraphNode, List<GraphNode>> node_neighborNodes,
			Map<GraphEdgeDirected, GraphEdgeDirected> edge_edge) {
		Map<GraphNode, Double> shortestDists = new HashMap<>();
		
		while (heap.size() > 0) {
			GraphNode node = (GraphNode) heap.poll().get(0);
			for (GraphNode tnode : node_neighborNodes.get(node)) {
				GraphEdgeDirected tedge = new GraphEdgeDirected(node, tnode, 0);
				double oldDist = shortestDists.get(tnode);
				// 需要new一个Edge，不优雅
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
	
	// test this class
	public static void main(String[] args) {
		Johnson johnson = new Johnson();
		String path = ".\\testdata\\apsp3.txt";
		
		long start = System.currentTimeMillis();
		johnson.createGraphFromFile(path);
		long end = System.currentTimeMillis();
		System.out.println("读文件耗时： " + (end - start) / (double)1000 + "s");
		
		start = System.currentTimeMillis();
		johnson.solveAPSP();
		end = System.currentTimeMillis();
		System.out.println("Johnson算法耗时：" + (end - start) / (double)1000 + "s");
	}

}
