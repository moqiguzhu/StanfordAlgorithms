package algorithms.scc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import algorithms.graphnode.GraphNode;

/**
 * @author moqiguzhu
 * @param <E>
 *            图节点类
 */
/******************************************
 *强连通分量的非递归实现，需要跑两遍DFS，算法的总时间复杂度是
 *O(n)。
 *!!! need to debug
 * 
 ******************************************/
public class StrongConnectedComponent<E extends GraphNode> {
	/* 使用邻接链表表示图 */
	private Map<E, List<E>> graph;
	/* 图的转置*/
	private Map<E, List<E>> graphReversed;
	/* 保存结果*/
	private List<List<E>> strongConnectedComponents;
	/* 时间戳*/
	private long timeLabel;
	/* DFS使用的栈*/
	private LinkedList<E> stack;
	
	public void init() {
		graph = new HashMap<>();
		graphReversed = new HashMap<>();
		strongConnectedComponents = new ArrayList<>();
		timeLabel = 0L;
		stack = new LinkedList<>();
	}
	
	public void createGraphFromFile(String path) throws Exception {
		init();
		BufferedReader bf = new BufferedReader(new FileReader(path));
		String regex = "\\s";
		String[] result;
		String line;
		while ((line = bf.readLine()) != null) {
			if(line.startsWith("##")) {
				continue;
			}
			result = line.split(regex);
			E tempNode1 = (E) new GraphNode(Integer.valueOf(result[0]));
			E tempNode2 = (E) new GraphNode(Integer.valueOf(result[1]));
			if(graph.containsKey(tempNode1)) {
				graph.get(tempNode1).add(tempNode2);
			} else {
				List<E> tempList = new ArrayList<>();
				tempList.add(tempNode2);
				graph.put(tempNode1, tempList);
			}
			if(!graph.containsKey(tempNode2)) {
				graph.put(tempNode2, new ArrayList<E>());
			}
		}
		//关闭文件
		bf.close();
	}
	
	public Map<Long, E> reverseGraph() {
		//timeLabel -> GraphNode
		timeLabel = 0;		//时间戳回到0
		Map<Long,E> end_node = new HashMap<Long,E>();
		for(E node1 : graph.keySet()) {
			for(E node2: graph.get(node1)) {
				if(graphReversed.containsKey(node2)) {
					graphReversed.get(node2).add(node1);
				} else {
					List<E> tempList = new ArrayList<>();
					tempList.add((E)new GraphNode(node1.getLabel(), 'w', 0L, 0L));
					E node = (E)new GraphNode(node2.getLabel(), 'w', 0L, 0L);
					graphReversed.put(node, tempList);
					end_node.put(node2.getEnd(), node);
				}
			}
		}
		return end_node;
	}
	
	public void DFS() {
		for(E node : graph.keySet()) {
			if(node.getColor() == 'w') {
				DFS_VISIT(node, graph);
			}
		}
	}
	
	public List<E> DFS_VISIT(E node, Map<E, List<E>> graph) {
		List<E> oneStrongComponent = new ArrayList<>();
		stack.addLast(node);
		while(!stack.isEmpty()) {
			E tempNode = stack.peekLast();
			timeLabel = timeLabel + 1;
			tempNode.setStart(timeLabel);
			tempNode.setColor('g');
			for(E ttNode : graph.get(tempNode)) {
				if(ttNode.getColor() == 'w') {
					stack.addLast(ttNode);
				}
			}
			if(stack.peekLast().equals(tempNode)) {
				oneStrongComponent.add(stack.pollLast());
				tempNode.setColor('b');
				timeLabel += 1;
				tempNode.setEnd(timeLabel);
			}
		}
		
		return oneStrongComponent;
	}
	
	
	public List<List<E>> getStrongConnectedComponents() {
		DFS();
		Map<Long,E> end_node = reverseGraph();
		List<Long> ends = new ArrayList<>(end_node.keySet());
		Collections.sort(ends, Collections.reverseOrder());
		for(long end : ends) {
			if(end_node.get(end).getColor() != 'b') {
				strongConnectedComponents.add(DFS_VISIT(end_node.get(end), graphReversed));
			}
		}
		
		return strongConnectedComponents;
	}
	
	public static void main(String[] args) throws Exception {
		StrongConnectedComponent<GraphNode> SCC = new StrongConnectedComponent<>();
		String currentDir = System.getProperty("user.dir");
		String path = currentDir + File.separator + "testdata" + File.separator + "SCC_test1.txt";
		SCC.createGraphFromFile(path);
		List<List<GraphNode>> components = SCC.getStrongConnectedComponents();
		System.out.println(components.size());
	}
}
