package Part1;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class QuickSort {
	private int sumComparisions = 0;
	private int[] num;
	public int[] readFile() throws Exception{
		String filePath = "G:\\研一\\Stanford Algorithms\\week2\\QuickSort.txt";
		File f = new File(filePath);
		Scanner sc = new Scanner(f);
		
		int[] result;
		List<Integer> temp = new ArrayList<Integer>();
		while(sc.hasNext())
			temp.add(sc.nextInt());
		sc.close();
		
		result = new int[temp.size()];
		for(int i = 0; i < temp.size(); i++)
			result[i] = temp.get(i);
		
		return result;
	}
	
	public int quickSort() throws Exception{
//		num = readFile();
		num = new int[]{3,8,2,5,1,4,7,6};
//		num = new int[]{1,2,3,4};
//		num = new int[]{2,1};
		sumComparisions = 0;
		helpFunction_1(0, num.length-1);
		System.out.println(Arrays.toString(num));
		return sumComparisions;
	}
	
	//使用第一个元素作为pivot
	public void helpFunction_1(int l, int r) {
		if(r <= l) return;
		int pivot = num[l];
		// i j 两个指针
		int i = l+1, j = l+1;
		int temp;
		for(int k = l+1; k <= r; k++) {
			if(num[k] > pivot) j++;
			else {
				temp = num[i];
				num[i] = num[j];
				num[j] = temp;
				i++; j++;
			}
		}
		//交换 注意是i-1
		temp = num[l];
		num[l] = num[i-1];
		num[i-1] = temp;
		
		sumComparisions += r-l;
		
		helpFunction_1(l,i-2);
		helpFunction_1(i,r);
	}
	
	//使用最后一个元素作为pivot
	public void helpFunction_2(int l, int r) {
		if(r <= l) return;
		int temp;
		//交换第一个元素和最后一个元素
		temp = num[l];
		num[l] = num[r];
		num[r] = temp;
		int pivot = num[l];
		
		// i j 两个指针
		int i = l+1, j = l+1;
		for(int k = l+1; k <= r; k++) {
			if(num[k] > pivot) j++;
			else {
				temp = num[i];
				num[i] = num[j];
				num[j] = temp;
				i++; j++;
			}
		}
		//交换 注意是i-1
		temp = num[l];
		num[l] = num[i-1];
		num[i-1] = temp;
		
		sumComparisions += r-l;
		
		helpFunction_2(l,i-2);
		helpFunction_2(i,r);
	}
	
	//使用median-of-three的原则来选择pivot
	public void helpFunction_3(int l, int r) {
		if(r <= l) return;
		int temp,mid,pivot,label;
		//选择pivot
		mid = (r-l)/2+l;
		if(num[mid] < num[r]) {
			if(num[mid] > num[l]) label = mid;
			else if(num[r] > num[l]) label = l;
			else label = r;
		} else {
			if(num[mid] < num[l]) label = mid;
			else if(num[r] > num[l]) label = r;
			else label = l;
		}
		temp = num[l];
		num[l] = num[label];
		num[label] = temp;
		pivot = num[l];
		
		// i j 两个指针
		int i = l+1, j = l+1;
		for(int k = l+1; k <= r; k++) {
			if(num[k] > pivot) j++;
			else {
				temp = num[i];
				num[i] = num[j];
				num[j] = temp;
				i++; j++;
			}
		}
		//交换 注意是i-1
		temp = num[l];
		num[l] = num[i-1];
		num[i-1] = temp;
		
		sumComparisions += r-l;
		
		helpFunction_3(l,i-2);
		helpFunction_3(i,r);
	}
	
	public List<int[]> createTestCases() {
		List<int[]> testcases = new ArrayList<int[]>();
		
		int[] num1 = {3,8,2,5,1,4,7,6};
		int[] num2 = {2,2,2,1,1,1};
		testcases.add(num1);
		testcases.add(num2);
		
		return testcases;
	}
	
	public static void main(String[] args) throws Exception{
		QuickSort qs = new QuickSort();
		System.out.println(qs.quickSort());
	}
}
