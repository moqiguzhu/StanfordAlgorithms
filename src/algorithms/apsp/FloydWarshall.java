package algorithms.apsp;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import algorithms.graphedge.GraphEdge;
import algorithms.graphnode.GraphNode;

/**
 * Floyd-Warshall算法解决所有节点对间最短路径问题，本质上是一种DP算法
 * 
 * @author moqiguzhu
 * @date 2015-08-17
 * @version 1.0
 */

public class FloydWarshall {
	/* 存储图中所有边信息 */
	private Set<GraphEdge> edges = new HashSet<>();
	/* 节点的数目 */
	private int numNodes;

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
			String regex = "\\s";
			String line = sc.nextLine();
			numNodes = Integer.valueOf(line.split(regex)[0]);
			String[] strArr;

			while (sc.hasNext()) {
				line = sc.nextLine();
				strArr = line.split(regex);
				GraphNode left = new GraphNode(Integer.valueOf(strArr[1]));
				GraphNode right = new GraphNode(Integer.valueOf(strArr[0]));
				double weight = Double.valueOf(strArr[2]);

				GraphEdge edge = new GraphEdge(left, right, weight);
				edges.add(edge);
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
	 * 使用Floyd Warshall算法解决所有节点之间的最短距离问题
	 * 
	 * @return
	 */
	public double solveAPSP() {
		// 使用带空间压缩的动态规划
		double[][][] table = new double[numNodes + 1][numNodes + 1][2];

		// 当k==0时，初始化table
		for (int i = 0; i < numNodes + 1; i++)
			for (int j = 0; j < numNodes + 1; j++) {
				if (i == j) {
					table[i][j][0] = 0;
				} else {
					table[i][j][0] = Double.MAX_VALUE;
				}
			}

		// 处理所有相邻的节点
		double ssp = Double.MAX_VALUE;
		for (GraphEdge edge : edges) {
			GraphNode left = edge.getLeftNode();
			GraphNode right = edge.getRightNode();
			table[left.getLabel()][right.getLabel()][0] = edge.getWeight();
			ssp = Math.min(ssp, edge.getWeight());
		}

		// 最短路径
		for (int k = 1; k <= numNodes; k++) {
			for (int i = 1; i < numNodes + 1; i++) {
				for (int j = 1; j < numNodes + 1; j++) {
					table[i][j][k % 2] = Math
							.min(table[i][j][(k - 1) % 2],
									table[i][k][(k - 1) % 2]
											+ table[k][j][(k - 1) % 2]);
					if (k == numNodes) {
						if (table[i][j][numNodes % 2] < ssp) {
							ssp = table[i][j][numNodes % 2];
						}
					}
				}
			}
		}

		// 检查图中是不是有negative cycle
		for (int i = 1; i < numNodes + 1; i++) {
			if (table[i][i][numNodes % 2] < 0) {
				System.out.println("there is a negative cycle in this graph!!!");
				System.exit(1);
			}
		}

		System.out.println("The shortest shortest path of this graph is: " + ssp);

		return ssp;
	}

	// test this class
	public static void main(String[] args) {
		String path = ".\\testdata\\apsp3.txt";
		FloydWarshall fw = new FloydWarshall();

		long start = System.currentTimeMillis();
		fw.createGraphFromFile(path);
		long end = System.currentTimeMillis();
		System.out.println("读文件耗时： " + (end - start) / (double) 1000 + "s");

		start = System.currentTimeMillis();
		fw.solveAPSP();
		end = System.currentTimeMillis();
		System.out.println("Floyd-Warshall算法耗时： " + (end - start)
				/ (double) 1000 + "s");
	}
}
