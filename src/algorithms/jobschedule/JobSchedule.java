package algorithms.jobschedule;

import java.io.File;
import java.util.Scanner;

/**
 * 作业调度问题的贪心算法。计算每个作业的weight/length值，值越大的越优先考虑。
 * 
 * @author moqiguzhu
 * @version 1.0
 * @date 2015-09-08
 */
public class JobSchedule {
	/* 存储每个作业的权重 */
	private int[] weights;

	/* 存储完成每个作业需要的时间 */
	private int[] lengths;

	/* 存储每个作业权重和所需时间的比值 */
	private double[] ratios;

	/* 作业的个数 */
	private int size;

	/**
	 * 
	 * @param path
	 *            作业信息文件路径
	 * @throws Exception
	 *             抛出异常
	 */
	public void readFile(String path) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(path));
			// 初始化
			size = sc.nextInt();
			weights = new int[size];
			lengths = new int[size];
			ratios = new double[size];

			for (int index = 0; index < size; index++) {
				weights[index] = sc.nextInt();
				lengths[index] = sc.nextInt();
				ratios[index] = (double) weights[index] / lengths[index];
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
	 * 按照weight/length从大到小排序
	 * 
	 * @param l
	 *            左边界
	 * @param r
	 *            右边界
	 */
	public void helpFunction(int l, int r) {
		if (r <= l)
			return;
		double pivot = ratios[l];
		// i j 两个指针
		int i = l + 1, j = l + 1;
		double temp;
		for (int k = l + 1; k <= r; k++) {
			if (ratios[k] < pivot)
				j++;
			else {
				temp = ratios[i];
				ratios[i] = ratios[j];
				ratios[j] = temp;
				temp = weights[i];
				weights[i] = weights[j];
				weights[j] = (int) temp;
				i++;
				j++;
			}
		}
		// 交换 注意是i-1
		temp = ratios[l];
		ratios[l] = ratios[i - 1];
		ratios[i - 1] = temp;
		temp = weights[l];
		weights[l] = weights[i - 1];
		weights[i - 1] = (int) temp;

		helpFunction(l, i - 2);
		helpFunction(i, r);
	}

	/**
	 * 
	 * @return 最优的结果
	 */
	public double solveJobSchedule() {
		double cost = 0;
		int curCompletion = 0;		//当前时间
		
		helpFunction(0, size - 1);
		
		for (int i = 0; i < size; i++) {
			curCompletion += (weights[i] / ratios[i]); 
			cost += weights[i] * curCompletion;
		}
		return cost;
	}

	public static void main(String[] args) throws Exception {
		JobSchedule js = new JobSchedule();
		String path = ".\\testdata\\jobs.txt";

		long start = System.currentTimeMillis();
		js.readFile(path);
		long end = System.currentTimeMillis();
		System.out.println("读文件耗时： " + (end - start) / (double) 1000 + "s");
		
		start = System.currentTimeMillis();
		double cost = js.solveJobSchedule();
		end = System.currentTimeMillis();
		System.out.println("算法耗时： " + (end - start) / (double)1000 + "s");
		System.out.println("最优结果： " + cost);
	}
}
