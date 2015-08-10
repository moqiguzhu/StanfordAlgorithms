package algorithms.scc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import algorithms.graphnode.GraphNode;

/**
 * @author moqiguzhu
 * @param <E>
 *            图节点类
 */
/******************************************
 *强连通分量的非递归实现，需要跑两遍DFS，算法的总时间复杂度是
 *O(n)。
 *这个bug很微妙，现在这个程序和正确的程序返回的结果非常接近，
 *现在有一种非常耗时的做法就是把这个程序返回的结果和正确程序的
 *输出结果比较，看看有到底有什么区别，还有为什么会多出一个大小
 *为304的强联通分量
 *!!! need to debug
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
	/* 图的节点集合*/
	private Map<E, E> nodes;
	
	public void init() {
		graph = new HashMap<>();
		graphReversed = new HashMap<>();
		strongConnectedComponents = new ArrayList<>();
		timeLabel = 0L;
		stack = new LinkedList<>();
		nodes = new HashMap<>();
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
			E tempNode1 = (E) new GraphNode(Integer.valueOf(result[0]), 'w', 0L , 0L);
			E tempNode2 = (E) new GraphNode(Integer.valueOf(result[1]), 'w', 0L, 0L);
			if(!nodes.containsKey(tempNode1)) {
				nodes.put(tempNode1, tempNode1);
			}
			if(!nodes.containsKey(tempNode2)) {
				nodes.put(tempNode2, tempNode2);
			}
			if(graph.containsKey(tempNode1)) {
				graph.get(tempNode1).add(nodes.get(tempNode2));
			} else {
				List<E> tempList = new ArrayList<>();
				tempList.add(nodes.get(tempNode2));
				graph.put(nodes.get(tempNode1), tempList);
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
					tempList.add(node1);
					graphReversed.put(node2, tempList);
				}
			}
		}
		for(E node : nodes.keySet()) {
			end_node.put(node.getEnd(), node);
		}
		refreshNode();
		
		return end_node;
	}
	
	public void refreshNode() {
		for(E node: nodes.keySet()) {
			node.setColor('w');
			node.setStart(0L);
			node.setEnd(0L);
		}
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
		//已经加入过的节点不能被再次加入，考虑只有四个节点的完全图可以发现这种情况
		Set<E> stackElements = new HashSet<>();
		stackElements.add(node);
		while(!stack.isEmpty()) {
			E tempNode = stack.peekLast();
			timeLabel = timeLabel + 1;
			if(tempNode.getStart() == 0L) {
				tempNode.setStart(timeLabel);
			}
			tempNode.setColor('g');
			if(graph.containsKey(tempNode)) {
				for(E ttNode : graph.get(tempNode)) {
					if(ttNode.getColor() == 'w' && !stackElements.contains(ttNode)) {
						stack.addLast(ttNode);
						stackElements.add(ttNode);
					}
				}
			}
			if(stack.peekLast().equals(tempNode)) {
				oneStrongComponent.add(stack.pollLast());
				stackElements.remove(tempNode);
				tempNode.setColor('b');
				timeLabel += 1;
				tempNode.setEnd(timeLabel);
			}
		}
		
		return oneStrongComponent;
	}
	
	
	public List<List<E>> getStrongConnectedComponents() {
		DFS();
		timeLabel = 0;
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
		String path = currentDir + File.separator + "testdata" + File.separator + "SCC.txt";
		SCC.createGraphFromFile(path);
		List<List<GraphNode>> components = SCC.getStrongConnectedComponents();
		List<Integer> componentsSize = new ArrayList<Integer>();
		for(int i = 0; i < components.size(); i++) {
			componentsSize.add(components.get(i).size());
		}
		Collections.sort(componentsSize, Collections.reverseOrder());
		System.out.println(componentsSize);
	}
}
