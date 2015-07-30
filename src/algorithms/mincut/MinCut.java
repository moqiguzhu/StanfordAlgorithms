package algorithms.mincut;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import algorithms.graphnode.GraphNode;

/**
 * @author moqiguzhu
 * @date 2015-07-23
 * @version 1.0
 * @param <E>
 *            节点类
 */
/**********************************************
 *1.使用Scanner读取文件和使用BufferedReader读取文件速度没有
 *太大变化，可能是因为每次都只是读取一行的原因
 *2.将图信息存储起来，在每次运行之前，都使用拷贝的图信息进行计算，这样
 *运算速度也并没有提高，因为没运行一次，图信息就被破坏了，下一次运行前
 *又需要再拷贝一份图信息。这也应该就是本程序的瓶颈。
 *3.RandomSet集合并不能用到这个问题上，这里需要的是能够保存重复元素
 * 集合
 *********************************************/
public class MinCut<E extends GraphNode> {
	/* 使用邻接链表表示图 */
	private Map<E, List<E>> graph;
	/* 当前还没有规约的节点集合 */
	List<E> untractedNodes;
	Random rand = new Random();

	public void init() {
		graph = new HashMap<E, List<E>>();
		untractedNodes = new ArrayList<E>();
	}

	public int kargerMinCut() {
		while (untractedNodes.size() > 2) {
			int rand1 = rand.nextInt(untractedNodes.size());
			E node1 = untractedNodes.get(rand1);
			int rand2 = rand.nextInt(graph.get(node1).size());
			E node2 = graph.get(node1).get(rand2);
			// O(n)
			untractedNodes.remove(node2);
			// graph contract kernel codes
			int size = graph.get(node1).size();
			// 删除自环 从后面开始删
			for (int i = size - 1; i > -1; i--) {
				if (graph.get(node1).get(i).equals(node2)) {
					graph.get(node1).remove(i);
				}
			}
			for (E tempNode : graph.get(node2)) {
				if (!tempNode.equals(node1)) {
					graph.get(node1).add(tempNode); // 两边都要处理
					graph.get(tempNode).add(node1);
				}
				// O(k) k是该节点的邻接节点的个数
				// node2被规约
				graph.get(tempNode).remove(node2); // 这行将会消耗大量时间
			}
		}
		// get(1) get(0)都可以
		return graph.get(untractedNodes.get(0)).size();
	}

	// 指明文件应该有的格式
	public void createGraphFromFile(String path) throws Exception {
		init(); 					// 初始化私有域
		
		BufferedReader bf = new BufferedReader(new FileReader(path));
		String regex = "\\s";
		String[] result;
		String line;
		while ((line = bf.readLine()) != null) {
			result = line.split(regex);
			// 向下转型，不安全
			E tempNode = (E) new GraphNode(Integer.valueOf(result[0]));
			List<E> tempList = new ArrayList<E>();
			for (int i = 1; i < result.length; i++) {
				tempList.add((E) new GraphNode(Integer.valueOf(result[i])));
			}
			graph.put(tempNode, tempList);
		}
		bf.close();
		
		//初始化untractedNodes
		for (int i = 0; i < graph.size(); i++) {
			untractedNodes.add((E) new GraphNode(i + 1));
		}
	}

	public static void main(String[] args) throws Exception {
		MinCut<GraphNode> mincut = new MinCut<GraphNode>();
		String currentDir = System.getProperty("user.dir");
		String path = currentDir + File.separator + "testdata" + File.separator + "kargerMinCut.txt";
		// 运行多次
		int times = 1000;
		int min = Integer.MAX_VALUE;
		int temp;
		long start = System.currentTimeMillis();
		while (times-- > 0) {
			mincut.createGraphFromFile(path);
			temp = mincut.kargerMinCut();
			min = temp > min ? min : temp;
		}
		long end = System.currentTimeMillis();
		System.out.println("运行1000次，总共耗时" + (end - start) + "ms");
		System.out.println("最小割为：" + min);
	}
}
