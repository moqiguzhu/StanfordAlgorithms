package algorithms.minimumspanningtree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import algorithms.graphedge.GraphEdge;
import algorithms.graphnode.GraphNode;
import algorithms.unionfind.UnionFind;

/**
 * 使用UnionFind来实现Kruskal的最小生成树算法
 * 
 * @author moqiguzhu
 * @date 2015-08-14
 * @version 1.0
 */
public class Kruskal {
	/* 图中所有的边集合 */
	private List<GraphEdge> edges = new ArrayList<>();
	/* UnionFind数据结构 */
	private UnionFind<GraphNode> uf = new UnionFind<>();
	/* 所有的节点集合，初始化UnionFind */
	private Set<GraphNode> nodes = new HashSet<>();

	/**
	 * 从文件中创建图
	 * 
	 * @param path
	 *            文件路径
	 * @throws Exception
	 *             读取文件时出错，抛出异常
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
				GraphEdge edge = new GraphEdge(left, right, weight);
				edges.add(edge);

				nodes.add(left);
				nodes.add(right);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	private void sortEdges() {
		Collections.sort(edges, new Comparator<GraphEdge>() {
			@Override
			public int compare(GraphEdge edge1, GraphEdge edge2) {
				if (edge1.getWeight() == edge2.getWeight()) {
					return 0;
				} else if (edge1.getWeight() > edge2.getWeight()) {
					return 1;
				} else {
					return -1;
				}
			}
		});
	}

	/**
	 * 使用UnionFind数据结构来实现Kruskal的最小生成树算法
	 * 
	 * @return 最小生成树的边权值和
	 */
	public double solveMST() {
		sortEdges();

		uf.init(new ArrayList<GraphNode>(nodes));

		double minDist = 0;

		for (int i = 0; i < edges.size(); i++) {
			GraphNode left = edges.get(i).getLeftNode();
			GraphNode right = edges.get(i).getRightNode();
			if (!uf.connected(left, right)) {
				uf.union(left, right);
				minDist += edges.get(i).getWeight();
			}
		}

		return minDist;
	}

	// test this class
	public static void main(String[] args) throws Exception {
		String path = ".\\testdata\\MST_test1.txt";
		Kruskal kk = new Kruskal();
		kk.createGraphFromFile(path);
		double minDist = kk.solveMST();
		System.out.println(minDist);
	}
}
