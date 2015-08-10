package algorithms.medianmaintenance;

import java.io.File;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * @author moqiguzhu
 * @date 2015-08-10
 * @version 1.0
 */
/*******************************************************************
 * Median Maintenance algorithm
 * 使用两个优先队列来分别保存前一半的元素和后一半的元素，前一半的元素存放在一个大顶堆中，后一半的 
 * 元素存放在一个小顶堆中。新来一个元素，根据新元素和大顶堆和小顶堆堆顶元素的关系，将新元素放入合
 * 适的堆中，新元素的加入有可能导致堆的不均衡，但是不均衡的度一定会控制在2以内。
 *
 *******************************************************************/

public class MedianMaintenance {
	public int solveMedianMaintenance(String path) {
		Scanner sc = null;
		//low half是个大顶堆
		PriorityQueue<Double> lowHalf = new PriorityQueue<>(Collections.reverseOrder());
		//high half是个小顶堆
		PriorityQueue<Double> highHalf = new PriorityQueue<>();
		long medSum = 0;
		try {
			sc = new Scanner(new File(path));
			while(sc.hasNextLine()) {
				double curVal = Double.valueOf(sc.nextLine());
				//往堆里加元素
				if(lowHalf.isEmpty() || lowHalf.peek() >= curVal) {
					lowHalf.offer(curVal);
				} else {
					highHalf.offer(curVal);
				} 
				
				//加元素之后，可能出现不均衡的情况，处理不均衡
				if(lowHalf.size() - highHalf.size() > 1) {
					highHalf.offer(lowHalf.poll());
				}
				if(highHalf.size() - lowHalf.size() > 1) {
					lowHalf.offer(highHalf.poll());
				}
				
				//当前中位数
				double curMed = 0;
				if(lowHalf.isEmpty() || highHalf.size() > lowHalf.size()) {
					curMed = highHalf.peek();
				} else if(highHalf.isEmpty() || lowHalf.size() > highHalf.size()) {
					curMed = lowHalf.peek();
				} else {
					curMed = lowHalf.peek();		   //偶数个元素
				}
				medSum += curMed;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(sc != null) sc.close();
		}
		
		return (int)(medSum % 10000);
	}
	
	public static void main(String[] args) {
		String path = ".\\testdata\\Median.txt";
		MedianMaintenance mm = new MedianMaintenance();
		long start = System.currentTimeMillis();			//开始时间
		int med = mm.solveMedianMaintenance(path);	
		long end = System.currentTimeMillis();				//结束时间
		System.out.println("中位数为： " + med);
		System.out.println("算法总共耗时： " + (end - start) / (double)1000 + "s");
	}
}
