package algorithms.minimumspanningtree;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import algorithms.graphedge.GraphEdge;
import algorithms.graphnode.GraphNode;
import algorithms.unionfind.UnionFind;

/**
 * Kruskal最小生成树算法的一个应用，问题描述参考同目录下文件problem1
 * 
 * @author moqiguzhu
 * @date 2015-08-15
 * @version 1.0
 */

public class MaximumSpacingClustering {
	/* 存储所有的边信息 */
	private List<GraphEdge> edges = new ArrayList<>();

	/* 存储图中出现的所有的节点 */
	private Set<GraphNode> nodes = new HashSet<>();

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
			GraphNode left, right;
			double weight;
			sc.nextLine(); // 不需要第一行的信息

			while (sc.hasNext()) {
				left = new GraphNode(sc.nextInt());
				right = new GraphNode(sc.nextInt());
				weight = (double) sc.nextInt();
				GraphEdge edge = new GraphEdge(left, right, weight);
				edges.add(edge);

				nodes.add(left);
				nodes.add(right);
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
	 * 图中所有的边按照边权值从小到大排序
	 */
	public void sortEdges() {
		Collections.sort(edges, new Comparator<GraphEdge>() {
			@Override
			public int compare(GraphEdge e1, GraphEdge e2) {
				if (e1.getWeight() > e2.getWeight())
					return 1;
				else if (e2.getWeight() > e1.getWeight())
					return -1;
				else
					return 0;
			}
		});
	}

	/**
	 * 在给定簇数目为k的情况下，返回簇间距离可能取得的最大值。簇间距离的定义是任意两个簇之间距离的最小值
	 * 
	 * @param k
	 *            簇数目
	 * @return 簇间距离可能取得的最大值
	 */
	public double cluster(int k) {
		sortEdges();

		UnionFind<GraphNode> uf = new UnionFind<>();
		uf.init(new ArrayList<GraphNode>(nodes));

		GraphNode left, right;
		double spacing = -1;

		for (int i = 0; i < edges.size(); i++) {
			left = edges.get(i).getLeftNode();
			right = edges.get(i).getRightNode();
			if (uf.getCount() == k) {
				if (!uf.connected(left, right)) {
					spacing = edges.get(i).getWeight();
					return spacing;
				}
			}
			if (!uf.connected(left, right)) {
				uf.union(left, right);
			}
		}

		// 如果spacing等于-1，表示k等于节点的数目
		return spacing;
	}

	// test this class
	public static void main(String[] args) {
		MaximumSpacingClustering msc = new MaximumSpacingClustering();
		String path = ".\\testdata\\clustering1.txt";

		long start = System.currentTimeMillis();
		msc.createGraphFromFile(path);
		long end = System.currentTimeMillis();
		System.out.println("读文件耗时： " + (end - start) / (double) 1000 + "s");

		double spacing = -1;
		int k = 4;
		start = System.currentTimeMillis();
		spacing = msc.cluster(k);
		end = System.currentTimeMillis();
		System.out.println("聚类算法耗时： " + (end - start) / (double) 1000 + "s");
		System.out.println("spacing is " + spacing);
	}
}
