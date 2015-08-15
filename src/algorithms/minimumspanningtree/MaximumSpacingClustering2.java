package algorithms.minimumspanningtree;

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
import algorithms.unionfind.UnionFind;

/**
 * 问题描述见本目录下的文件problem2
 *
 * @author moqiguzhu
 * @date 2015-08-15
 * @version 1.0
 */
public class MaximumSpacingClustering2 {
	/* 完全图中所有边权值小于阈值的边 */
	private List<GraphEdge> edges = new ArrayList<>();

	/* 节点集合 */
	private Map<GraphNode, GraphNode> node_node = new HashMap<>();

	/* 阈值 */
	private int threshold = 2;

	/* 每个节点使用多少位来表示 */
	private int numBits;

	/**
	 * 从文件中读取图信息
	 * 
	 * @param path
	 *            文件路径
	 */
	public void createGraphFromFile(String path) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(path));
			String str;
			int val;
			str = sc.nextLine().split("\\s")[1];
			numBits = Integer.valueOf(str);

			while (sc.hasNext()) {
				str = sc.nextLine();
				str = str.replace(" ", "");
				val = Integer.parseInt(str, 2);

				GraphNode node = new GraphNode(val);
				node_node.put(node, node);
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
	 * 找到图中所有边权值小于阈值的边
	 */
	public void findEdges() {
		Set<Integer> onesAndtwos = new HashSet<Integer>();
		int[] temp = new int[numBits];
		int cur = 1;

		// 所有只有一个1的整数
		for (int i = 0; i < 24; i++) {
			onesAndtwos.add(cur);
			temp[i] = cur;
			cur = cur << 1;
		}

		// 所有有两个1的整数
		for (int i = 0; i < temp.length; i++) {
			for (int j = i + 1; j < temp.length; j++) {
				onesAndtwos.add(temp[i] + temp[j]);
			}
		}

		Set<GraphNode> nodes = node_node.keySet();
		for (GraphNode node : nodes) {
			for (int y : onesAndtwos) {
				int e = node.getLabel() ^ y;
				if (nodes.contains(new GraphNode(e))) {
					GraphNode left = node;
					GraphNode right = node_node.get(new GraphNode(e));
					edges.add(new GraphEdge(left, right, y));
				}
			}
		}
	}

	/**
	 * 本质上还是一个最小生成树算法
	 * 
	 * @return 把距离为1和2的节点全部连接起来之后，剩下的簇的数目
	 */
	public int solveCluster() {
		UnionFind<GraphNode> uf = new UnionFind<>();
		uf.init(new ArrayList<GraphNode>(node_node.keySet()));
		GraphNode left, right;

		for (int i = 0; i < edges.size(); i++) {
			left = edges.get(i).getLeftNode();
			right = edges.get(i).getRightNode();
			if (!uf.connected(left, right)) {
				uf.union(left, right);
			}
		}

		return uf.getCount();
	}

	// test this class
	public static void main(String[] args) {
		MaximumSpacingClustering2 msc2 = new MaximumSpacingClustering2();
		String path = ".\\testdata\\clustering_big.txt";

		long start = System.currentTimeMillis();
		msc2.createGraphFromFile(path);
		long end = System.currentTimeMillis();
		System.out.println("读文件耗时： " + (end - start) / (double) 1000 + "s");

		start = System.currentTimeMillis();
		msc2.findEdges();
		int numClusters = msc2.solveCluster();
		end = System.currentTimeMillis();
		System.out.println("簇的数目为： " + numClusters);
		System.out.println("聚类过程耗时：" + (end - start) / (double) 1000 + "s");
	}
}
