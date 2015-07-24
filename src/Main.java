import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
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
	
	public static void main(String[] args) throws Exception{
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		int size = Integer.parseInt(br.readLine());
//		int[] num = new int[size];
//		String str = br.readLine();
//		String[] strArr = str.split("\\s");
//		for(int i = 0; i < size; i++)
//			num[i] = Integer.parseInt(strArr[i]);
//		br.close();
//		
//		Main ma = new Main();
//		ma.mergeSort(num);
//		System.out.println(ma.getCount());
//		System.out.println(1000000000 % 142857);
		
		System.out.println("This is a test line");
		System.out.println("This is also a test line");
		
		int[] x = {1,2,3};
		int[] y = x;
		y[0] = 100;
		System.out.println(Arrays.toString(x));
		System.out.println(Arrays.toString(y));
	}
}
