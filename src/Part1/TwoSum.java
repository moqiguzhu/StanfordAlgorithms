package Part1;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class TwoSum {
	private List<Double> list;
	private Set<Double> result;

	public void readFiles() throws Exception{
		String path = ".\\testdata\\2sum.txt";
		File f = new File(path);
		Scanner sc = new Scanner(f);
		list = new ArrayList<Double>();
		while(sc.hasNext())
			list.add(Double.valueOf(sc.nextLine()));
		sc.close();
	}
	
	public int twoSum() throws Exception{
		readFiles();
		Collections.sort(list);
		double x;
		result = new HashSet<Double>();
		for(int i = 0; i < list.size(); i++) {
			x = list.get(i);
			search(x,-10000-x,10000-x,i+1,list.size()-1,list);
		}
		return result.size();
	}
	
	public void search(double x,double s, double b, int start, int end, List<Double> list) {
		if(start > end) return;
		int med = (end + start)/2;
		if(list.get(med) >= s && list.get(med) <= b) {
			if(list.get(med) != x) result.add(list.get(med)+x);		//判断条件保证distinctness
			for(int i = med+1; i <= end; i++) {
				if(list.get(i) >= s && list.get(i) <= b) {
					if(list.get(i) != x) result.add(list.get(i)+x);
					else continue;
				} else break;
			}
			for(int i = med-1; i >= start; i--) {
				if(list.get(i) >= s && list.get(i) <= b) {
					if(list.get(i) != x) result.add(list.get(i)+x);
					else continue;
				} else break;
			}
		} else if(list.get(med) > b) search(x,s,b,start,med-1,list);
		else search(x,s,b,med+1,end,list);
	}
	
	public static void main(String[] args) throws Exception{
		TwoSum ts = new TwoSum();
		long start = System.currentTimeMillis();
		System.out.println(ts.twoSum());
		long end = System.currentTimeMillis();
		System.out.println("Time is: " + (end-start)/1000 + "s");
	}
}
