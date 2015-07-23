package algorithms.mincut;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * @author moqiguzhu
 * @date 2015-07-23
 * @version 1.0
 * @param <E>
 *            节点类
 */
/**********************************************
 * 
 *
 *
 * 
 *********************************************/
public class MinCut<E extends GraphNode> {
	/* 使用邻接链表表示图 */
	private Map<E, List<E>> graph;
	/* 当前还没有规约的节点集合 */
	List<E> uncontractedNodes;
	Random rand = new Random();

	public void init() {
		graph = new HashMap<E, List<E>>();
		uncontractedNodes = new ArrayList<E>();
	}

	public int kargerMinCut() {
		if (graph == null) {
			System.out.println("没有初始化！！！");
			System.exit(1);
		} else {
			for (int i = 0; i < graph.size(); i++) {
				uncontractedNodes.add((E) new GraphNode(i + 1));
			}
		}
		while (uncontractedNodes.size() > 2) {
			int rand1 = rand.nextInt(uncontractedNodes.size());
			E node1 = uncontractedNodes.get(rand1);
			int rand2 = rand.nextInt(graph.get(node1).size());
			E node2 = graph.get(node1).get(rand2);
			// O(n)
			uncontractedNodes.remove(node1);
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
				graph.get(tempNode).remove(node2); // 这行将会消耗大量时间
				// node2被规约
				//node2可能还是会被选中，标记一下，！！！
			}
		}
		// get(1) get(0)都可以
		//!!! should be equal
		System.out.println(graph.get(uncontractedNodes.get(0)).size() + " " + graph.get(uncontractedNodes.get(1)).size());
		return graph.get(uncontractedNodes.get(0)).size();
	}

	// 指明文件应该有的格式
	public void createGrahFromFile(String path) throws FileNotFoundException {
		init(); 					// 初始化私有域
		
		Scanner sc = new Scanner(new File(path));
		String regex = "\\s";
		String[] result;
		while (sc.hasNext()) {
			String str = sc.nextLine();
			result = str.split(regex);
			// 向下转型，不安全
			E tempNode = (E) new GraphNode(Integer.valueOf(result[0]));
			List<E> tempList = new ArrayList<E>();
			for (int i = 1; i < result.length; i++) {
				tempList.add((E) new GraphNode(Integer.valueOf(result[i])));
			}
			graph.put(tempNode, tempList);
		}
		sc.close();
	}
	
	public List<E> getUntractedNodes() {
		return uncontractedNodes;
	}

	// !!! to-do
	// 测试MinCut类的正确性
	// 测试MinCut和RSelect到底那个快
	// 使用更快的读文件操作，看看性能能提高多少
	// 实现从Set中随机取一个元素的类
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
			mincut.createGrahFromFile(path);
			temp = mincut.kargerMinCut();
			min = temp > min ? min : temp;
		}
		long end = System.currentTimeMillis();
		System.out.println("运行1000次，总共耗时" + (end - start) + "ms");
		System.out.println("最小割为：" + min);
	}
}
