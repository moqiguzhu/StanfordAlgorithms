package algorithms.minimumspanningtree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import algorithms.graphedge.GraphEdge;
import algorithms.graphnode.GraphNode;
import algorithms.heap.Heap;

public class Prim {
	/*节点以及节点到部分最小生成树的距离*/
	private Heap<GraphNode, Double> node_dist = new Heap<>();
	/*初始化距离*/
	private static final double MAX = Double.MAX_VALUE;
	/*节点集合*/
	Set<GraphNode> nodes = new HashSet<>();
	/*边信息需要存储起来*/
	
	public void createGraphFromFile(String path) throws Exception {
		BufferedReader br = null;
		String line, regex = "\\s";
		String[] strArr;

		try {
			br = new BufferedReader(new FileReader(path));
			br.readLine();
			while ((line = br.readLine()) != null) {
				strArr = line.split(regex);
				GraphNode left = new GraphNode(Integer.valueOf(strArr[0]));
				GraphNode right = new GraphNode(Integer.valueOf(strArr[1]));
				double weight = Integer.valueOf(strArr[2]);

				nodes.add(left);
				nodes.add(right);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}
	
	public static void main(String[] args) {
		String path = ".\\testdata\\MST_test1.txt";
		
	}
}
