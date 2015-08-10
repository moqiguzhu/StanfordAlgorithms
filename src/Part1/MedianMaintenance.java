package Part1;
import java.io.File;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;


public class MedianMaintenance {
	public static void main(String[] args) throws Exception{
		Scanner sc = new Scanner(new File(".\\testdata\\median_test1.txt"));
		PriorityQueue<Integer> lowHalf,highHalf;
		lowHalf = new PriorityQueue<Integer>(10,Collections.reverseOrder());
		highHalf = new PriorityQueue<Integer>();
		int result = 0;
		int temp;
		int med;
		while(sc.hasNext()) {
			temp = sc.nextInt();
			if(lowHalf.isEmpty() || temp < lowHalf.peek())
				lowHalf.add(temp);
			else if(highHalf.isEmpty() || temp > highHalf.peek())
				highHalf.add(temp);
			else 
				lowHalf.add(temp);
			if(lowHalf.size() - highHalf.size() > 1) 
				highHalf.add(lowHalf.remove());
			else if(highHalf.size() - lowHalf.size() > 1)
				lowHalf.add(highHalf.remove());
			else {}
			if(highHalf.isEmpty()) {	//lowHalf不可能为空
				med = lowHalf.peek();
			} else if(highHalf.size() > lowHalf.size()){
				med = highHalf.peek();
			} else if(lowHalf.size() > highHalf.size()) {
				med = lowHalf.peek();
			} else {
				med = lowHalf.peek();
			}
			
			result += med;
		}
		sc.close();
		System.out.println(result % 10000);
	}
}
