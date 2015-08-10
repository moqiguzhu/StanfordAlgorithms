package algorithms.twosum;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author moqiguzhu
 * @date 2015-08-10
 * @version 1.0
 */
/*****************************************************
 * 问题描述：Your task is to compute the number of target values 
 * t in the interval [-10000,10000] (inclusive) such 
 * that there are distinct numbers x,y in the input file 
 * that satisfy x+y=t
 *
 *****************************************************/

public class TwoSum {
	/*输入数组*/
	private List<Double> data = new ArrayList<>();
	/*符合条件的所有数*/
	private Set<Double> distinctVals = new HashSet<>();

	public void readFile(String path) {
		Scanner sc = null;
		try {
			File f = new File(path);
			sc = new Scanner(f);
			while(sc.hasNext()) {
				data.add(Double.valueOf(sc.nextLine()));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(sc != null) sc.close();
		}
	}
	
	public int twoSum() {
		Collections.sort(data);				//排序，便于后面的二分查找
		for(int i = 0; i < data.size(); i++) {
			double ele = data.get(i);
			binarySearch(ele,-10000-ele,10000-ele,i+1,data.size()-1,data);
		}
		return distinctVals.size();
	}
	
	//二分查找，在数组data里查找位于区间[lowBound,highBound]的元素
	public void binarySearch(double ele,double lowBound, double highBound, int start, int end, List<Double> data) {
		if(start > end) {						//查找完毕
			return;
		} else {
			int med = (end + start) / 2;
			double medEle = data.get(med);
			if(medEle >= lowBound && medEle <= highBound) {
				if(medEle != ele) {
					distinctVals.add(medEle + ele);
				}
				for(int i = med+1; i <= end; i++) {
					double curEle = data.get(i);
					if(curEle >= lowBound && curEle <= highBound) {
						if(curEle != ele) {
							distinctVals.add(curEle + ele);
						}
					} else {
						break;
					}
				}
				for(int i = med-1; i >= start; i--) {
					double curEle = data.get(i);
					if(curEle >= lowBound && curEle <= highBound) {
						if(curEle != ele) {
							distinctVals.add(curEle + ele);
						}
					} else {
						return;
					}
				}
			} else if(medEle > highBound) {
				binarySearch(ele, lowBound, highBound, start, med-1, data);
			} else {
				binarySearch(ele, lowBound, highBound, med+1, end, data);
			}
		}
	}
	
	public List<Double> getDistinctVals() {
		twoSum();
		return new ArrayList<>(distinctVals);
	}
	
	public static void main(String[] args) {
		String path = ".\\testdata\\2sum.txt";
		TwoSum twosum = new TwoSum();
		long start = System.currentTimeMillis();            //读文件开始时间 
		twosum.readFile(path);								//读文件
		long end = System.currentTimeMillis();	            //读文件结束时间
		System.out.println("读文件耗时： " + (end - start) / (double)1000 + "s");
		
		start = System.currentTimeMillis();					//twosum算法开始时间
		long count = twosum.getDistinctVals().size();
		end = System.currentTimeMillis();                   //算法结束时间
		System.out.println("满足条件的元素个数为： " + count);		
		System.out.println("twosum算法耗时: " + (end - start) / (double)1000 + "s");
	}
}
