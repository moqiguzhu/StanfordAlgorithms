package Part2;

import java.io.File;

import java.util.Scanner;

//run the greedy algorithm that schedules jobs in decreasing order of the 
//difference (weight - length)， this strategy is not always optimal

public class MinimizeWeightedSumCompletionTime {
	private int[] weights;
	private int[] lengths;
	private Integer[] diff;
	private int size;
	public void readFiles(String path) throws Exception{
		Scanner sc = new Scanner(new File(path));
		//初始化
		size = sc.nextInt();
		weights = new int[size];
		lengths = new int[size];
		diff = new Integer[size];
		for(int index = 0; index < size; index++) {
			weights[index] = sc.nextInt();
			lengths[index] = sc.nextInt();
			diff[index] = weights[index] - lengths[index];
		}
		sc.close();
	} 
	
	// lengths 的index是没有处理的
	public void helpFunction_1(int l, int r) {
		if(r <= l) return;
		int pivot = diff[l];
		// i j 两个指针
		int i = l+1, j = l+1;
		int temp;
		for(int k = l+1; k <= r; k++) {
			if(diff[k] < pivot || (diff[k] == pivot && weights[k] < weights[l])) j++;
			else {
				temp = diff[i];
				diff[i] = diff[j];
				diff[j] = temp;
				temp = weights[i];
				weights[i] = weights[j];
				weights[j] = temp;
				i++; j++;
			}
		}
		//交换 注意是i-1
		temp = diff[l];
		diff[l] = diff[i-1];
		diff[i-1] = temp;
		temp = weights[l];
		weights[l] = weights[i-1];
		weights[i-1] = temp;
		
		helpFunction_1(l,i-2);
		helpFunction_1(i,r);
	}
	
	//计算此策略下的cost
	public double optimalFunction() {
		double cost = 0;
		int curCompletion = 0;
		helpFunction_1(0,size-1);
//		System.out.println(Arrays.toString(diff));
//		System.out.println(Arrays.toString(weights));
		for(int i = 0; i < size; i++) {
			curCompletion += (weights[i] - diff[i]);
			cost += weights[i] * curCompletion;
		}
		return cost;
	}
	
	public static void main(String[] args) throws Exception{
		MinimizeWeightedSumCompletionTime mwsc = new MinimizeWeightedSumCompletionTime();
		String path = "C:\\Master\\Stanford Algorithms 2\\jobs.txt";
		mwsc.readFiles(path);
		double cost = mwsc.optimalFunction();
		System.out.println("cost = " + cost);
	}
}
