package Part2;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author moqiguzhu
 * @date 2015-04-25
 * description:为解决TSP问题设计的类
 * 使用了Nearest Neighbor启发式策略和 2-opt move优化策略
 */
public class TSP {
	/**/
	private List<Point> points;
	/**/
	private List<Edge> edges;
	/**/
	private double[][] dists;
	
	public void readFiles(String path) throws Exception {
		points = new ArrayList<Point>();
		File file = new File(path);
		Scanner sc = new Scanner(file);
		String line = "";
		String regex = "\\s";
		String[] strs = new String[2];
		int label = 1;
		double x, y;
		int numNodes = Integer.parseInt(sc.nextLine());
		dists = new double[numNodes+1][numNodes+1];
		
		//read file
		
		while(sc.hasNext()) {
			line = sc.nextLine();
			strs = line.split(regex);
			x = Double.parseDouble(strs[0]);
			y = Double.parseDouble(strs[1]);
			points.add(new Point(x, y, label++));
		}
		
		sc.close();
	}
	
	public void init() {
		edges = new ArrayList<Edge>();
		int numNodes = points.size();
		for(int i = 0; i < numNodes; i++) {
			for(int j = 0; j < numNodes; j++) {
				double x1 = points.get(i).getX();
				double y1 = points.get(i).getY();
				double x2 = points.get(j).getX();
				double y2 = points.get(j).getY();
				double dist = Math.round(Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)));
				dists[i+1][j+1] = dist;
				if(i != j) {
					// all the cost is zero
					Edge edge = new Edge(dists[i+1][j+1], points.get(i).getLabel(), points.get(j).getLabel());
					edges.add(edge);
				}
			}
		}
	}
	
	// rand表示从哪个节点开始使用最近邻策略
	// 图有多少个节点，就可以有多少个不同rand
	public Tour NearestNeighbor(int rand) {
		List<Edge> tourEdges = new ArrayList<Edge>();
		double cost = 0;
		int numNodes = points.size();
		rand = rand % numNodes + 1;
		int curLabel = rand, nextLabel;
		
		Set<Integer> visited = new HashSet<Integer>();
		int count = 0;
		
		visited.add(curLabel);
		int lastVisted = 0;
		while(count++ < numNodes-1) {
			double curMin = Double.MAX_VALUE;
			int index = 0;
			for(int i = 1; i < numNodes+1; i++) {
				if(dists[curLabel][i] < curMin && curLabel != i && !visited.contains(i)) {
					curMin = dists[curLabel][i];
					index = i;
				}
			}
			nextLabel = index;
			Edge edge = new Edge(curMin, curLabel, nextLabel);
			tourEdges.add(edge);
			cost += curMin;
			visited.add(nextLabel);
			lastVisted = nextLabel;
			
			curLabel = nextLabel;
		}
		//add the last edge
		Edge edge = new Edge(dists[lastVisted][rand], lastVisted, rand);
		tourEdges.add(edge);
		cost += edge.getCost();
		
		Tour tour = new Tour(tourEdges, cost);
		return tour;
	}
	
	// two_opt_move策略
	//这种实现采用的是最笨的方法，时间复杂度是O(n^3)
	public double two_opt_move(Tour t) {
		int numNodes = t.getEdges().size();
		//存储城市的访问顺序
		int[] order = t.getOrder();
		double finalCost = t.getCost();
		double minchange;
		do {
			minchange = 0;
			int mini = -1, minj = -1;
			int i, j;
			for(i = 0; i < numNodes - 3; i++) {
				for(j = i+2; j < numNodes-1; j++) {
					double change = dists[order[i]][order[j]] + dists[order[i+1]][order[j+1]] - 
							dists[order[i]][order[i+1]] - dists[order[j]][order[j+1]];
					if(minchange > change) {
						minchange = change;
						mini = i;
						minj = j;
					}
				}
			}
			//apply mini/minj move
			int[] new_order = new int[numNodes];
			int index, new_index = 0;
			for(int p = 0; p <= mini; p++) {
				index = p;
				new_order[new_index + p] = order[index];
			}
			new_index = new_index + mini;
			for(int p = 1; p <= minj - mini; p++) {
				index = minj - p + 1;
				new_order[new_index + p] = order[index];
			}
			new_index = new_index + minj - mini;
			for(int p = 1; p < numNodes - minj; p++) {
				index = minj + p;
				new_order[new_index + p] = order[index];
			}
			//下面注释掉的代码块同样能够工作
			//之所以注释掉是因为上面的代码块是一种更加通用的写法
			//在3-opt-move的时候也能够用
			{
//				for(int k = 0; k <= mini; k++)
//					new_order[k] = order[k];
//				for(int k = mini+1; k <= minj; k++)
//					new_order[k] = order[minj - k + mini + 1];
//				for(int k = minj+1; k < numNodes; k++)
//					new_order[k] = order[k];
			}
			order = new_order;
			//把边的访问顺序信息保存下来
			
//			System.out.println(Arrays.toString(order));
			
			finalCost += minchange;
			t.setOrder(order);
			t.setCost(finalCost);
		} while(minchange < 0);
		
		return finalCost;
	}
	
	//3-opt-move
	public double three_opt_move(Tour t) {
		int numNodes = t.getEdges().size();
		//存储城市的访问顺序
		int[] order = t.getOrder();
		double finalCost = t.getCost();
		double minchange;
		do {
			minchange = 0;
			int mini = -1, minj = -1, mink = -1;
			int i, j, k;
			//在3-opt-move中，有两种可以选择的方案，flag记录哪种方案能够获得最大的收益
			int flag = 0;
			for(i = 0; i < numNodes - 5; i++) {
				for(j = i+2; j < numNodes - 3; j++) {
					for(k = j+2; k < numNodes - 1; k++) {
						double change1 = dists[order[i]][order[j+1]] + dists[order[i+1]][order[k+1]] +
								dists[order[k]][order[j]] - dists[order[i]][order[i+1]] -
								dists[order[j]][order[j+1]] - dists[order[k]][order[k+1]];
						double change2 = dists[order[i]][order[j+1]] + dists[order[i+1]][order[k]] +
								dists[order[k+1]][order[j]] - dists[order[i]][order[i+1]] -
								dists[order[j]][order[j+1]] - dists[order[k]][order[k+1]];
						double change = Math.min(change1, change2);
						if(minchange > change) {
							minchange = change;
							mini = i;
							minj = j;
							mink = k;
							if(change == change1) flag = 1;
							else flag = 2;
						}
					}
				}
			}
			//apply mini/minj/mink move
			int[] new_order = new int[numNodes];
			if(flag == 1) {
				int new_index = 0, index;
				for(int p = 0; p <= mini; p++) {
					index = p;
					new_order[new_index+p] = order[index];
				}
				new_index = new_index + mini;
				for(int p = 1; p <= mink - minj; p++) {
					index = minj + p;
					new_order[new_index + p] = order[index];
				}
				new_index = new_index + mink - minj;
				for(int p = 1; p <= minj - mini; p++) {
					index = minj - p + 1;
					new_order[new_index + p] = order[index];
				}
				new_index = new_index + minj - mini;
				for(int p = 1; p < numNodes - mink; p++) {
					index = mink + p;
					new_order[new_index + p] = order[index];
				}
			} else if(flag == 2) {
				int new_index = 0, index;
				for(int p = 0; p <= mini; p++) {
					index = p;
					new_order[new_index+p] = order[index];
				}
				new_index = new_index + mini;
				for(int p = 1; p <= mink - minj; p++) {
					index = minj + p;
					new_order[new_index + p] = order[index];
				}
				new_index = new_index + mink - minj;
				for(int p = 1; p <= minj - mini; p++) {
					index = mini + p;
					new_order[new_index + p] = order[index];
				}
				new_index = new_index + minj - mini;
				for(int p = 1; p < numNodes - mink; p++) {
					index = mink + p;
					new_order[new_index + p] = order[index];
				}
			} else {
//				System.out.println("Get local optimal considering 3-opt-move!!!");
			}
			order = new_order;
//			System.out.println(Arrays.toString(order));
			
			finalCost += minchange;
			t.setOrder(order);
			t.setCost(finalCost);
		} while(minchange < 0);
		
		return finalCost;
	}
	
	//test this class
	public static void main(String[] args) throws Exception {
		TSP tsp = new TSP();
		String path	= new String("C:\\Master\\Stanford Algorithms 2\\week5\\Tanzania.txt");
		tsp.readFiles(path);
		tsp.init();
		//len
		int len = 100;
		double[] result2opt = new double[len];
		double[] result3opt = new double[len];
		long start = System.currentTimeMillis();
		for(int i = 0; i < len; i++) {
			//when using "Tanzania" as input data
			//GC overhead limit exceeded
			Tour tour = tsp.NearestNeighbor(i);
			result2opt[i] = tsp.two_opt_move(tour);
//			result3opt[i] = tsp.three_opt_move(tour);
		}
		long end = System.currentTimeMillis();
		System.out.println("time consuming is: " + (end-start));
		Arrays.sort(result2opt);
		System.out.println(Arrays.toString(result2opt));
		Arrays.sort(result3opt);
		System.out.println(Arrays.toString(result3opt));
	}
}
