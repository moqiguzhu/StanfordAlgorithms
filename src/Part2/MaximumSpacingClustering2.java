package Part2;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


/**
 * @author moqiguzhu
 * @Date 2015-04-02
 * UC Santa Barbara有一个讲解怎么在O(nlog(n))的时间内求解d-dimention最近点对的pdf文档
 * 如果采用文档上面所述的方法，程序的效率会很高，但是实现的复杂度比较高
 * https://class.coursera.org/algo2-004/forum/thread?thread_id=67
 */
public class MaximumSpacingClustering2 {
	private List<Edge> edges;
	/*value-index*/
	private Map<Integer, Integer> nodes;
	private int[] nodeIndexes;
	
	public void readFiles(String path) throws Exception{
		Scanner sc = new Scanner(new File(path));
		nodes = new HashMap<Integer, Integer>();
		//第一行数据不要
		sc.nextLine();
		String str = "";
		int index = 1;
		int val;
		while(sc.hasNextLine()) {
			str = sc.nextLine();
			str = str.replace(" ", "");
			val = Integer.parseInt(str, 2);
			if(!nodes.containsKey(val))
				nodes.put(val, index++);
		}
		int numNodes = nodes.size();
		nodeIndexes = new int[numNodes];
		for(int i = 0; i < numNodes; i++)
			nodeIndexes[i] = i+1;

		sc.close();
	}
	
	//find all the edges that the distance between them less than threshold
	public void findEdges() {
		edges = new ArrayList<Edge>();
		//在这个程序中设置threshold == 2
		Set<Integer> onesAndtwos = new HashSet<Integer>();
		//24 should be a parameter
		int[] temp = new int[24];
		int cur = 1;
		
		for(int i = 0; i < 24; i++) {
			onesAndtwos.add(cur);
			temp[i] = cur;
			cur = cur << 1;
		}
		for(int i = 0; i < temp.length; i++) {
			for(int j = i+1; j < temp.length; j++) {
				onesAndtwos.add(temp[i] + temp[j]);
			}
		}
		
		int e;
		Set<Integer> keys = nodes.keySet();
		int left, right;
		for(int x : keys) {
			for(int y : onesAndtwos) {
				e = x ^ y;
				if(keys.contains(e)) {
					left = nodes.get(x);
					right = nodes.get(e);
					edges.add(new Edge(1, left, right));
				}
			}
		}
	}
	
	//返回的是完成聚类时簇的个数
	public int cluster() {
		//do not need sorting
		UF uf = new UF();
		uf.init(nodeIndexes);
		
		int left, right;
		for(int i = 0; i < edges.size(); i++) {
			left = edges.get(i).getLeft();
			right = edges.get(i).getRight();
			if(!uf.connected(left, right)) {
				uf.union(left, right);
			}
		}
		
		return uf.getCount();
	}
	
	//test this class
	public static void main(String[] args) throws Exception{
		MaximumSpacingClustering2 msc2 = new MaximumSpacingClustering2();
		String path = "C:\\Master\\Stanford Algorithms 2\\week2\\clustering_big.txt";
		long start = System.currentTimeMillis();
		msc2.readFiles(path);
		msc2.findEdges();
		System.out.println("num of clusters: " + msc2.cluster());
		long end = System.currentTimeMillis();
		System.out.println("time consuming: " + (end - start) + "ms");
	}
}
