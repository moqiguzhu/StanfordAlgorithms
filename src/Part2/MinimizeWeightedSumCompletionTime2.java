package Part2;

import java.io.File;
import java.util.Scanner;

//run the greedy algorithm that schedules jobs in decreasing order of the 
//difference (weight - length)， this strategy is not always optimal

public class MinimizeWeightedSumCompletionTime2 {
	private int[] weights;
	private int[] lengths;
	private double[] ratios;
	private int size;
	public void readFiles(String path) throws Exception{
		Scanner sc = new Scanner(new File(path));
		//初始化
		size = sc.nextInt();
		weights = new int[size];
		lengths = new int[size];
		ratios = new double[size];
		for(int index = 0; index < size; index++) {
			weights[index] = sc.nextInt();
			lengths[index] = sc.nextInt();
			ratios[index] = (double)weights[index] / lengths[index];
		}
		sc.close();
	} 
	
	//实质上是一个快速排序函数
	public void helpFunction(int l, int r) {
		if(r <= l) return;
		double pivot = ratios[l];
		// i j 两个指针
		int i = l+1, j = l+1;
		double temp;
		for(int k = l+1; k <= r; k++) {
			if(ratios[k] < pivot) j++;
			else {
				temp = ratios[i];
				ratios[i] = ratios[j];
				ratios[j] = temp;
				temp = weights[i];
				weights[i] = weights[j];
				weights[j] = (int)temp;
				i++; j++;
			}
		}
		//交换 注意是i-1
		temp = ratios[l];
		ratios[l] = ratios[i-1];
		ratios[i-1] = temp;
		temp = weights[l];
		weights[l] = weights[i-1];
		weights[i-1] = (int)temp;
		
		helpFunction(l,i-2);
		helpFunction(i,r);
	}
	
	//计算此策略下的cost
	public double optimalFunction() {
		double cost = 0;
		int curCompletion = 0;
		helpFunction(0,size-1);
//		System.out.println(Arrays.toString(diff));
//		System.out.println(Arrays.toString(weights));
		for(int i = 0; i < size; i++) {
			curCompletion += (weights[i]/ratios[i]);		//
			cost += weights[i] * curCompletion;
		}
		return cost;
	}
	
	public static void main(String[] args) throws Exception{
		MinimizeWeightedSumCompletionTime2 mwsc2 = new MinimizeWeightedSumCompletionTime2();
		String path = "C:\\Master\\Stanford Algorithms 2\\jobs.txt";
		mwsc2.readFiles(path);
		double cost = mwsc2.optimalFunction();
		System.out.println("cost = " + cost);
	}
}

