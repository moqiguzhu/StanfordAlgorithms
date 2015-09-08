package algorithms.TSP;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import algorithms.graphedge.GraphEdge;
import algorithms.graphnode.GraphNode;

/**
 * 使用了Nearest Neighbor启发式策略，2-opt-move优化策略，3-opt-move优化策略。
 * 注意这个算法并不能保证最后得到的结果是最优结果，特别是在节点变多的时候，算法的精度也随之下降。
 * 
 * @author moqiguzhu
 * @version 1.0
 * @date 2015-04-25
 */
public class TSP {
	/* 存储所有的节点信息 */
	private List<Point> points = new ArrayList<>();

	/* 存储节点之间的距离信息 */
	private double[][] dists;

	/**
	 * 
	 * @param path
	 *            存储结点信息的文件路径
	 * @throws Exception
	 *             抛出异常
	 */
	public void readFiles(String path) {
		Scanner sc = null;

		String line = "";
		String regex = "\\s";
		String[] strs = new String[2];
		int label = 1;
		double x, y;
		int numNodes;

		try {
			sc = new Scanner(new File(path));
			numNodes = Integer.parseInt(sc.nextLine());
			dists = new double[numNodes + 1][numNodes + 1];

			// read file
			while (sc.hasNext()) {
				line = sc.nextLine();
				strs = line.split(regex);
				x = Double.parseDouble(strs[0]);
				y = Double.parseDouble(strs[1]);
				points.add(new Point(x, y, label++));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
	}

	public void init() {
		int numNodes = points.size();
		for (int i = 0; i < numNodes; i++) {
			for (int j = 0; j < numNodes; j++) {
				double x1 = points.get(i).getX();
				double y1 = points.get(i).getY();
				double x2 = points.get(j).getX();
				double y2 = points.get(j).getY();

				double dist = Math.round(Math.sqrt((x1 - x2) * (x1 - x2)
						+ (y1 - y2) * (y1 - y2)));
				dists[i + 1][j + 1] = dist;
			}
		}
	}

	/**
	 * 
	 * @param rand
	 *            从哪个节点开始使用最近邻策略，图有多少个节点，就可以有多少个不同rand
	 * @return
	 */
	public Tour NearestNeighbor(int rand) {
		List<GraphEdge> tourEdges = new ArrayList<>();
		double cost = 0;
		int numNodes = points.size();
		rand = rand % numNodes + 1;
		int curLabel = rand, nextLabel;

		Set<Integer> visited = new HashSet<Integer>();
		int count = 0;

		visited.add(curLabel);
		int lastVisted = 0;
		while (count++ < numNodes - 1) {
			double curMin = Double.MAX_VALUE;
			int index = 0;
			for (int i = 1; i < numNodes + 1; i++) {
				if (dists[curLabel][i] < curMin && curLabel != i
						&& !visited.contains(i)) {
					curMin = dists[curLabel][i];
					index = i;
				}
			}
			nextLabel = index;
			GraphEdge edge = new GraphEdge(new GraphNode(curLabel),
					new GraphNode(nextLabel), curMin);
			tourEdges.add(edge);
			cost += curMin;
			visited.add(nextLabel);
			lastVisted = nextLabel;

			curLabel = nextLabel;
		}

		// add the last edge
		GraphEdge edge = new GraphEdge(new GraphNode(lastVisted),
				new GraphNode(rand), dists[lastVisted][rand]);
		tourEdges.add(edge);
		cost += edge.getWeight();

		Tour tour = new Tour(tourEdges, cost);
		return tour;
	}

	/**
	 * 此算法的时间复杂度是O(n^3)
	 * 
	 * @param t
	 *            Hanmilton圈
	 * @return 经过两步最优策略之后，Hanmilton圈的边权值之和
	 * 
	 */
	public double two_opt_move(Tour t) {
		int numNodes = t.getOrder().length;
		// 存储城市的访问顺序
		int[] order = t.getOrder();
		double finalCost = t.getCost();
		double minchange;
		do {
			minchange = 0;
			int mini = -1, minj = -1;
			int i, j;
			for (i = 0; i < numNodes - 3; i++) {
				for (j = i + 2; j < numNodes - 1; j++) {
					double change = dists[order[i]][order[j]]
							+ dists[order[i + 1]][order[j + 1]]
							- dists[order[i]][order[i + 1]]
							- dists[order[j]][order[j + 1]];
					if (minchange > change) {
						minchange = change;
						mini = i;
						minj = j;
					}
				}
			}

			// apply mini/minj move
			int[] new_order = new int[numNodes];
			int index, new_index = 0;
			for (int p = 0; p <= mini; p++) {
				index = p;
				new_order[new_index + p] = order[index];
			}
			new_index = new_index + mini;
			for (int p = 1; p <= minj - mini; p++) {
				index = minj - p + 1;
				new_order[new_index + p] = order[index];
			}
			new_index = new_index + minj - mini;
			for (int p = 1; p < numNodes - minj; p++) {
				index = minj + p;
				new_order[new_index + p] = order[index];
			}
			order = new_order;

			// System.out.println(Arrays.toString(order));

			finalCost += minchange;
			t.setOrder(order);
			t.setCost(finalCost);
		} while (minchange < 0);

		return finalCost;
	}

	/**
	 * 
	 * @param t
	 *            Hanmilton圈
	 * @return 经过三步最优策略之后，Hanmilton圈的边权值之和
	 */
	public double three_opt_move(Tour t) {
		int numNodes = t.getOrder().length;
		int[] order = t.getOrder();
		double finalCost = t.getCost();
		double minchange;

		do {
			minchange = 0;
			int mini = -1, minj = -1, mink = -1;
			int i, j, k;
			// 在3-opt-move中，有两种可以选择的方案，flag记录哪种方案能够获得最大的收益
			int flag = 0;
			for (i = 0; i < numNodes - 5; i++) {
				for (j = i + 2; j < numNodes - 3; j++) {
					for (k = j + 2; k < numNodes - 1; k++) {
						double change1 = dists[order[i]][order[j + 1]]
								+ dists[order[i + 1]][order[k + 1]]
								+ dists[order[k]][order[j]]
								- dists[order[i]][order[i + 1]]
								- dists[order[j]][order[j + 1]]
								- dists[order[k]][order[k + 1]];
						double change2 = dists[order[i]][order[j + 1]]
								+ dists[order[i + 1]][order[k]]
								+ dists[order[k + 1]][order[j]]
								- dists[order[i]][order[i + 1]]
								- dists[order[j]][order[j + 1]]
								- dists[order[k]][order[k + 1]];
						double change = Math.min(change1, change2);
						if (minchange > change) {
							minchange = change;
							mini = i;
							minj = j;
							mink = k;
							if (change == change1)
								flag = 1;
							else
								flag = 2;
						}
					}
				}
			}

			// apply mini/minj/mink move
			int[] new_order = new int[numNodes];
			if (flag == 1) {
				int new_index = 0, index;
				for (int p = 0; p <= mini; p++) {
					index = p;
					new_order[new_index + p] = order[index];
				}
				new_index = new_index + mini;
				for (int p = 1; p <= mink - minj; p++) {
					index = minj + p;
					new_order[new_index + p] = order[index];
				}
				new_index = new_index + mink - minj;
				for (int p = 1; p <= minj - mini; p++) {
					index = minj - p + 1;
					new_order[new_index + p] = order[index];
				}
				new_index = new_index + minj - mini;
				for (int p = 1; p < numNodes - mink; p++) {
					index = mink + p;
					new_order[new_index + p] = order[index];
				}
			} else if (flag == 2) {
				int new_index = 0, index;
				for (int p = 0; p <= mini; p++) {
					index = p;
					new_order[new_index + p] = order[index];
				}
				new_index = new_index + mini;
				for (int p = 1; p <= mink - minj; p++) {
					index = minj + p;
					new_order[new_index + p] = order[index];
				}
				new_index = new_index + mink - minj;
				for (int p = 1; p <= minj - mini; p++) {
					index = mini + p;
					new_order[new_index + p] = order[index];
				}
				new_index = new_index + minj - mini;
				for (int p = 1; p < numNodes - mink; p++) {
					index = mink + p;
					new_order[new_index + p] = order[index];
				}
			} else {
				// do nothing
			}
			order = new_order;

			// System.out.println(Arrays.toString(order));

			finalCost += minchange;
			t.setOrder(order);
			t.setCost(finalCost);
		} while (minchange < 0);

		return finalCost;
	}

	// test this class
	public static void main(String[] args) {
		TSP tsp = new TSP();
		String path = new String(".\\testdata\\TSP_western_sahara.txt");

		long start = System.currentTimeMillis();
		tsp.readFiles(path);
		long end = System.currentTimeMillis();
		System.out.println("读文件耗时： " + (end - start) / (double) 1000 + "s");

		start = System.currentTimeMillis();
		tsp.init();
		// 从不同的节点开始，整个算法运行一百次，然后选择其中最好的结果
		int len = 100;
		double[] result2opt = new double[len];
		double[] result3opt = new double[len];
		for (int i = 0; i < len; i++) {
			// when using "Tanzania" as input data, GC overhead limit exceeded
			Tour tour = tsp.NearestNeighbor(i);
			result2opt[i] = tsp.two_opt_move(tour);
			// result3opt[i] = tsp.three_opt_move(tour);
		}
		end = System.currentTimeMillis();
		System.out.println("TSP算法耗时： " + (end - start) / (double) 1000 + "s");

		// 打印最后的结果
		Arrays.sort(result2opt);
		System.out.println(Arrays.toString(result2opt));

		// Arrays.sort(result3opt);
		// System.out.println(Arrays.toString(result3opt));
	}
}
