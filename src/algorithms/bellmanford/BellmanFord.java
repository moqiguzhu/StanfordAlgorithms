package algorithms.bellmanford;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import algorithms.graphedge.GraphEdge;
import algorithms.graphnode.GraphNode;

/**
 * 使用Bellman-Ford算法解决单源最短路径问题。 注意在使用这个类的时候，节点的label要从1到numNodes取值
 * 
 * @author moqiguzhu
 * @date 2015-08-17
 * @version 1.0
 */

public class BellmanFord {
	/* 存储结点及其相邻的节点信息 */
	private Map<GraphNode, List<GraphNode>> node_neighborNodes = new HashMap<>();

	/* 存储图的边权值信息 */
	private Map<GraphEdge, GraphEdge> edge_edge = new HashMap<>();

	/* key为节点到源节点之间的最短距离 */
	private Map<GraphNode, Double> shortestDists = new HashMap<>();

	/* 到源节点的初始距离 */
	private double MAX = Double.MAX_VALUE;

	public BellmanFord() {

	}

	public BellmanFord(Map<GraphNode, List<GraphNode>> node_neighborNodes,
			Map<GraphEdge, GraphEdge> edge_edge) {
		this.node_neighborNodes = node_neighborNodes;
		this.edge_edge = edge_edge;
	}

	/**
	 * 从文件中读取图信息
	 * 
	 * @param path
	 *            文件路径
	 */
	public void createGraphFromFile(String path) {
		Scanner sc = null;
		try {
			String line, regex = "\\s", regex1 = ",";
			String[] strArr;
			sc = new Scanner(new File(path));
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				strArr = line.split(regex);
				GraphNode source = new GraphNode(Integer.valueOf(strArr[0]));
				node_neighborNodes.put(source, new ArrayList<GraphNode>());
				if (Integer.valueOf(strArr[0]) == 1) { // 源节点
					shortestDists.put(source, .0);
				} else {
					shortestDists.put(source, MAX);
				}
				if (strArr.length > 0) {
					double weight;
					for (int i = 1; i < strArr.length; i++) {
						GraphNode dest = new GraphNode(
								Integer.valueOf(strArr[i].split(regex1)[0]));
						weight = Double.valueOf(strArr[i].split(regex1)[1]);
						GraphEdge edge = new GraphEdge(source, dest, weight);

						edge_edge.put(edge, edge);
						// 这里也不需要显式保存双向的连接信息
						node_neighborNodes.get(source).add(dest);
					}
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
	 * Bellman-Ford算法，如果发现图中存在negative cycle，返回false。 在实现过程中，使用了空间压缩技术
	 * 
	 * return 是否包含negative cycle
	 */
	public boolean solveBellmanFord() {
		int numNodes = node_neighborNodes.size();
		double[][] numNodes_node = new double[2][numNodes + 1];

		// 初始化
		for (int j = 1; j < numNodes + 1; j++) {
			if (j == 1) { // 源节点的label为1
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
				for (GraphNode nd : node_neighborNodes.get(node)) {
					GraphEdge edge = new GraphEdge(nd, node, 0);
					double ttemp = numNodes_node[(i - 1) % 2][nd.getLabel()]
							+ edge_edge.get(edge).getWeight();
					temp = Math.min(ttemp, temp);
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
	 * @return 每个节点到源节点的最短路径
	 */
	public Map<GraphNode, Double> getShortestDists() {
		return shortestDists;
	}

	// test this class
	public static void main(String[] args) {
		String path = ".\\testdata\\Dijkstra.txt";
		BellmanFord bf = new BellmanFord();

		long start = System.currentTimeMillis();
		bf.createGraphFromFile(path);
		long end = System.currentTimeMillis();
		System.out.println("读文件耗时： " + (end - start) / (double) 1000 + "s");

		start = System.currentTimeMillis();
		bf.solveBellmanFord();
		end = System.currentTimeMillis();
		System.out.println("Bellman-Ford算法耗时： " + (end - start) / (double) 1000
				+ "s");

		Map<GraphNode, Double> shortestDists = bf.getShortestDists();
		System.out.println("各节点到源节点的最短路径为： ");
		System.out.println(shortestDists);
	}
}
