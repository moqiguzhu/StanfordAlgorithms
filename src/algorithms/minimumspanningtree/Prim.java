package algorithms.minimumspanningtree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import algorithms.graphedge.GraphEdge;
import algorithms.graphnode.GraphNode;
import algorithms.heap.Heap;

/**
 * 采用优先队列实现Prim的最小生成树算法
 * 
 * @author moqiguzhu
 * @date 2015-08-14
 * @version 1.0
 */

public class Prim {
	/* 节点以及节点到部分最小生成树的距离 */
	private Heap<GraphNode, Double> heap = new Heap<>();

	/* 存储图的节点之间的连接信息 */
	private Map<GraphNode, List<GraphNode>> node_neighborNodes = new HashMap<>();

	/* 存储图的边权值信息 */
	private Map<GraphEdge, GraphEdge> edge_edge = new HashMap<>();

	/* 节点集合 */
	Set<GraphNode> nodes = new HashSet<>();

	private static final double MAX = Double.MAX_VALUE;

	/**
	 * 从文件中创建图
	 * 
	 * @param path 文件路径
	 * @throws Exception 抛出异常
	 */
	public void createGraphFromFile(String path) throws Exception {
		BufferedReader br = null;
		String line, regex = "\\s";
		String[] strArr;

		try {
			br = new BufferedReader(new FileReader(path));
			br.readLine();
			while ((line = br.readLine()) != null) {
				strArr = line.split(regex);
				GraphNode left = new GraphNode(Integer.valueOf(strArr[0]));
				GraphNode right = new GraphNode(Integer.valueOf(strArr[1]));
				double weight = Integer.valueOf(strArr[2]);

				// 无向图
				GraphEdge edge = new GraphEdge(left, right, weight);
				GraphEdge edge1 = new GraphEdge(right, left, weight);
				edge_edge.put(edge, edge);
				edge_edge.put(edge1, edge1);

				if (!nodes.contains(left)) {
					nodes.add(left);
					heap.offer(left, MAX);
				}
				if (!nodes.contains(right)) {
					nodes.add(right);
					heap.offer(right, MAX);
				}

				if (!node_neighborNodes.containsKey(left)) {
					node_neighborNodes.put(left, new ArrayList<GraphNode>());
				}
				if (!node_neighborNodes.containsKey(right)) {
					node_neighborNodes.put(right, new ArrayList<GraphNode>());
				}
				node_neighborNodes.get(left).add(right);
				node_neighborNodes.get(right).add(left);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	/**
	 * @return 最小生成树的边权值之和
	 */
	public double solveMST() {
		double minDist = 0;

		while (heap.size() > 0) {
			List<Object> node_key = heap.poll();
			GraphNode node = (GraphNode) node_key.get(0);
			double key = (Double) node_key.get(1);

			if (key != MAX) {
				minDist += key;
			}

			for (GraphNode nd : node_neighborNodes.get(node)) {
				if (heap.contains(nd)) {
					GraphEdge edge = new GraphEdge(node, nd, 0);
					double weight = edge_edge.get(edge).getWeight();

					if (weight < heap.getKey(nd)) {
						heap.setKey(nd, weight);
					}
				}
			}
		}
		return minDist;
	}
	
	// test this class
	public static void main(String[] args) throws Exception {
		String path = ".\\testdata\\clustering1.txt";
		Prim prim = new Prim();
		
		long start = System.currentTimeMillis();
		prim.createGraphFromFile(path);
		long end = System.currentTimeMillis();
		System.out.println("读文件耗时： " + (end - start) / (double)1000 + "s");
		
		start = System.currentTimeMillis();
		double minDist = prim.solveMST();
		end = System.currentTimeMillis();
		System.out.println("Prim的MST算法耗时： " + (end - start) / (double)1000 + "s");
		
		System.out.println("最小生成树的代价是： " + minDist);
	}
}
