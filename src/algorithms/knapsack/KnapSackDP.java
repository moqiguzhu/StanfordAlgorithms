package algorithms.knapsack;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * 使用动态规划来求取0-1背包问题的最优解，使用了wiki百科提到的空间压缩技术
 * 
 * @author moqiguzhu
 * @date 2015-08-14
 * @version 1.0
 *
 */
public class KnapSackDP {
	private int numItems;
	private int capacity;
	private int[] values;
	private int[] weights;

	public void readFiles(String filePath) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();

		capacity = Integer.parseInt(line.split("\\s")[0]);
		numItems = Integer.parseInt(line.split("\\s")[1]);
		values = new int[numItems];
		weights = new int[numItems];

		int index = 0;
		while ((line = br.readLine()) != null) {
			values[index] = Integer.parseInt(line.split("\\s")[0]);
			weights[index++] = Integer.parseInt(line.split("\\s")[1]);
		}

		br.close();
	}

	/**
	 * 使用了空间压缩技术，只需要背包容量+1的空间，注意第二层循环是逆序的
	 * 
	 * @return
	 */
	public int solveKnapSack() {
		int[] subOpt = new int[capacity + 1];
		for (int i = 0; i < numItems; i++) {
			for (int j = capacity; j >= 1; j--) {
				if (j - weights[i] >= 0) {
					subOpt[j] = Math.max(subOpt[j], subOpt[j - weights[i]] + values[i]);
				}
			}
		}
		return subOpt[capacity];
	}

	// test this class
	public static void main(String[] args) throws Exception {
		KnapSackDP ks = new KnapSackDP();
		String filePath = ".\\testdata\\knapsack_big.txt";

		long start = System.currentTimeMillis();
		ks.readFiles(filePath);
		long end = System.currentTimeMillis();
		System.out.println("读文件耗时： " + (end - start) / (double) 1000 + "s");

		start = System.currentTimeMillis();
		int opt = ks.solveKnapSack();
		end = System.currentTimeMillis();
		System.out.println("解决0-1背包问题耗时：" + (end - start) / (double) 1000 + "s");

		System.out.println("背包最多能装价值为" + opt + "的物品");
	}
}
