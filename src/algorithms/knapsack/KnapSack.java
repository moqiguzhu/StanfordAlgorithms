package algorithms.knapsack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 使用Branch and Bound方法解决0-1背包问题
 * 
 * @author moqiguzhu
 * @date 2015-08-13
 * @version 1.0
 */

public class KnapSack {
	/* DFS使用的栈 */
	private LinkedList<KnapSackNode> stack;
	/* 当前最优结果 */
	private double curOpt;
	/* 物品的价值密度 */
	private double[] ratios;
	/* 按照物品的价值密度从高到底排序对应的物品下标 */
	private int[] indices;
	/* 贪心策略得到的结果 */
	private double greedyOpt;
	/* 使用放缩策略得到的结果 */
	private double relaxOpt;
	/* 物品的价值 */
	private int[] vals;
	/* 物品的重量 */
	private int[] weights;
	/* 背包的容量 */
	private int capacity;

	/**
	 * 构造函数
	 * 
	 * @param vals
	 *            物品的价值
	 * @param weights
	 *            物品的重量
	 * @param capacity
	 *            物品所占的空间
	 */
	public KnapSack(int[] vals, int[] weights, int capacity) {
		this.vals = vals;
		this.weights = weights;
		this.capacity = capacity;
		init();
	}

	private void init() {
		stack = new LinkedList<KnapSackNode>();
		ratios = new double[vals.length];
		indices = new int[vals.length];

		for (int i = 0; i < vals.length; i++) {
			ratios[i] = (double) vals[i] / weights[i];
			indices[i] = i;
		}

		quickSort(0, ratios.length - 1, ratios, indices);

		int i = 0, tempCap = capacity;
		for (i = 0; i < indices.length; i++) {
			if (tempCap - weights[indices[i]] >= 0) {
				greedyOpt += vals[indices[i]];
				relaxOpt += vals[indices[i]];
				tempCap -= weights[indices[i]];
			} else {
				break;
			}
		}
		relaxOpt += (double) tempCap / weights[indices[i]] * vals[indices[i]];
	}

	private void quickSort(int l, int r, double[] num, int[] index) {
		if (r <= l)
			return;
		double pivot = num[l];
		// i j 两个指针
		int i = l + 1, j = l + 1;
		double temp;
		for (int k = l + 1; k <= r; k++) {
			if (num[k] < pivot)
				j++;
			else {
				temp = num[i];
				num[i] = num[j];
				num[j] = temp;
				// change index information
				temp = index[i];
				index[i] = index[j];
				index[j] = (int) temp;
				i++;
				j++;
			}
		}
		// 交换 注意是i-1
		temp = num[l];
		num[l] = num[i - 1];
		num[i - 1] = temp;
		// change index information
		temp = index[l];
		index[l] = index[i - 1];
		index[i - 1] = (int) temp;

		quickSort(l, i - 2, num, index);
		quickSort(i, r, num, index);
	}

	/**
	 * 
	 * @param index
	 *            当前正在考虑的物品的下标
	 * @param curCap
	 *            当前背包的剩余容量
	 * @return 考虑到当前物品时，使用放缩法得到的最优结果
	 */
	private double relax(int index, int curCap) {
		double relaxReuslt = 0;
		int i = 0;

		for (; i < weights.length; i++) {
			if (curCap - weights[indices[i]] >= 0 && indices[i] > index) {
				relaxReuslt += vals[indices[i]];
				curCap -= weights[indices[i]];
			} else if (curCap - weights[indices[i]] < 0) {
				break;
			} else {
				continue;
			}
		}

		if (i != weights.length) {
			relaxReuslt += (double) curCap / weights[indices[i]] * vals[indices[i]];
		}

		return relaxReuslt;
	}

	/**
	 * 解决0-1背包问题
	 * 
	 * @return 背包能装下的最大商品价值
	 */
	public double solveKnapSack() {
		// init current optimal using greedy strategy
		curOpt = greedyOpt;
		// DFS
		KnapSackNode firstNode = new KnapSackNode(0, relaxOpt, capacity, KnapSackNode.toProcess, 0);
		stack.addLast(firstNode);
		KnapSackNode tempNode;
		int curVal, remainCap, state, index;
		double estVal;
		while (!stack.isEmpty()) {
			tempNode = stack.getLast();
			if (tempNode.getState() == KnapSackNode.toProcess) {
				if (tempNode.getRemainCap() >= weights[tempNode.getIndex()]) {
					tempNode.setState(KnapSackNode.processed);
					index = tempNode.getIndex();
					curVal = tempNode.getCurVal() + vals[index];
					remainCap = tempNode.getRemainCap() - weights[index];
					// relax
					estVal = curVal + relax(index, remainCap);
					// pruning
					if (estVal < curOpt) {
						continue;
					}
					state = KnapSackNode.toProcess;
					index = index + 1;
					// update current optimal result
					if (index == weights.length) {
						if (curVal > curOpt) {
							curOpt = curVal;
						}
						continue;
					}
					KnapSackNode node = new KnapSackNode(curVal, estVal, remainCap, state, index);
					stack.addLast(node);
				} else {
					tempNode.setState(KnapSackNode.processed);
				}
			} else {
				tempNode = stack.removeLast();
				index = tempNode.getIndex();
				curVal = tempNode.getCurVal();
				remainCap = tempNode.getRemainCap();
				// relax
				estVal = curVal + relax(index, remainCap);
				// pruning
				if (estVal < curOpt) {
					continue;
				}
				state = KnapSackNode.toProcess;
				index = index + 1;
				if (index == weights.length) {
					if (curVal > curOpt) {
						curOpt = curVal;
					}
					continue;
				}
				KnapSackNode node = new KnapSackNode(curVal, estVal, remainCap, state, index);
				stack.addLast(node);
			}
		}
		return curOpt;
	}

	// test this class
	public static void main(String[] args) throws Exception {
		List<String> lines = new ArrayList<String>();
		String filePath = ".\\testdata\\knapsack_big.txt";

		BufferedReader input = new BufferedReader(new FileReader(filePath));
		try {
			String line = null;
			while ((line = input.readLine()) != null) {
				lines.add(line);
			}
		} finally {
			input.close();
		}

		String[] firstLine = lines.get(0).split("\\s+");
		int capacity = Integer.parseInt(firstLine[0]);
		int num_item = Integer.parseInt(firstLine[1]);

		int[] values = new int[num_item];
		int[] weights = new int[num_item];

		for (int i = 1; i < num_item + 1; i++) {
			String line = lines.get(i);
			String[] parts = line.split("\\s+");

			values[i - 1] = Integer.parseInt(parts[0]);
			weights[i - 1] = Integer.parseInt(parts[1]);
		}

		KnapSack ks = new KnapSack(values, weights, capacity);

		// test solve function
		long start = System.currentTimeMillis();
		System.out.println("the real optimal result: " + ks.solveKnapSack());
		long end = System.currentTimeMillis();
		System.out.println("time consuming is " + (end - start));
	}
}
