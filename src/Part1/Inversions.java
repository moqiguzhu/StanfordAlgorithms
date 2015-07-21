package Part1;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Inversions {
	private int[] sortedArray;
	private long count;
	
	public long getCount() {
		return count;
	}
	
	public void mergeSort(int[] num) {
		count = 0;
		if(num.length < 2) { 
			sortedArray = num;
		} else {
			sortedArray = new int[num.length];
			helpSort(num,0,num.length-1);
		}
	}
	
	public void helpSort(int[] num, int l, int u) {
		if(l+1 > u) return;
		else {
			int mid = (l+u)/2;
			helpSort(num,l,mid);
			helpSort(num,mid+1,u);
			merge(num,l,mid,u);
		}
	}
	//left: l-mid 包括l和mid
	//right： mid+1-u 包括mid+1和u
	public void merge(int[] num, int l, int mid, int u) {
		int i = l, j = mid+1;
		int flag = l;
		while(i <= mid || j <= u) {
			if(i > mid) {
				while(j <= u) 
					sortedArray[flag++] = num[j++];
			} else if(j > u) {
				while(i <= mid) 
					sortedArray[flag++] = num[i++];
			} else {
				if(num[i] > num[j]) {
					sortedArray[flag++] = num[j++];
					count += (mid-i+1);
				}
				else 
					sortedArray[flag++] = num[i++];
			}
		}
		//两个数组之间的关系要处理好
		for(int p = l; p <= u; p++)
			num[p] = sortedArray[p];
	}
	
	public List<int[]> createTestCases() {
		List<int[]> testcases = new ArrayList<int[]>();
		
		int[] num1 = {2,4,1,3,5};
		testcases.add(num1);
		
		int[] num2 = {2,1};
		testcases.add(num2);
		
		int[] num3 = {};
		testcases.add(num3);
		
		int[] num4 = {1,3,2,6,5,4};
		testcases.add(num4);
		
		int[] num5 = {23,5,25,18,11,4,23,4,17,7};
		testcases.add(num5);
		
		int[] num6 = {18,35,45,32,46,6,15,14,41,21,49,13,10,47,7,10,42,32,24,6};
		testcases.add(num6);
		
		return testcases;
	}
	
	public int[] readFile() throws Exception{
		String filePath = "G:\\Stanford Algorithms\\IntegerArray.txt";
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
	
//	public static void main(String[] args) throws Exception{
//		Inversions inver = new Inversions();
//		List<int[]> testcases = inver.createTestCases();
//		//我自己的测试案列
//		for(int i = 0; i < testcases.size(); i++) {
//			inver.mergeSort(testcases.get(i));
//			System.out.println(Arrays.toString(inver.sortedArray));
//			System.out.println(inver.count);
//		}
//		
//		int[] test = inver.readFile();
//		inver.mergeSort(test);
//		System.out.println(inver.count);
//		System.out.println(Integer.MAX_VALUE);
//	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int size = scanner.nextInt();
		int[] num = new int[size];
		int index = 0;
		while(index < size) {
			num[index] = scanner.nextInt();
			index++;
		}
		scanner.close();

		Inversions inversions = new Inversions();
		inversions.mergeSort(num);
		System.out.println(inversions.getCount());
	}
}
