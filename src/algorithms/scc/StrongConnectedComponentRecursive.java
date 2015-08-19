package algorithms.scc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import algorithms.graphnode.GraphNode;

public class StrongConnectedComponentRecursive {
	/*存储图的出度信息*/
	private Map<GraphNode, List<GraphNode>> node_neighborNodes = new HashMap<>();
	
	/*存储转置图的出度信息*/
	private Map<GraphNode, List<GraphNode>> node_neighborNodes_reversed = new HashMap<>();
	
	/*时间标签*/
	private long timeLabel;
	
	/* 图的节点集合*/
	private Map<GraphNode, GraphNode> nodes = new HashMap<>();
	
	public void createGraphFromFile(String path) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(path));
			String line, regex = "\\s";
			String[] strArr;
			while(sc.hasNext()) {
				line = sc.nextLine();
				strArr = line.split(regex);
				
				// left means tail and right means head
				GraphNode left = new GraphNode(Integer.valueOf(strArr[0]), 'w', 0L, 0L);
				GraphNode right = new GraphNode(Integer.valueOf(strArr[1]), 'w', 0L, 0L);
				if(!nodes.containsKey(left)) {
					nodes.put(left, left);
				}
				if(!nodes.containsKey(right)) {
					nodes.put(right, right);
				}
				
				if(!node_neighborNodes.containsKey(left)) {
					node_neighborNodes.put(nodes.get(left), new ArrayList<GraphNode>());
				}
				node_neighborNodes.get(nodes.get(left)).add(nodes.get(right));
				
				if(!node_neighborNodes_reversed.containsKey(right)) {
					node_neighborNodes_reversed.put(nodes.get(right), new ArrayList<GraphNode>());
				}	
				node_neighborNodes_reversed.get(nodes.get(right)).add(nodes.get(left));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(sc != null) {
				sc.close();
			}
		}
	}
	
	public void DFS() {
		timeLabel = 0;
		
		for(GraphNode node : node_neighborNodes.keySet()) {
			if(node.getColor() == 'w') {
				DFS_VISIT(node);
			}
		}
	}
	
	public void DFS_VISIT(GraphNode node) {
		timeLabel += 1;
		node.setStart(timeLabel);
		node.setColor('g');
		
		if(node_neighborNodes.containsKey(node)) {
			for(GraphNode tnode : node_neighborNodes.get(node)) {
				if(tnode.getColor() == 'w') {
					DFS_VISIT(tnode);
				}
			}
		}
		
		node.setColor('b');
		timeLabel += 1;
		node.setEnd(timeLabel);
	}
	
	public List<GraphNode> SECOND_DFS_VISIT(GraphNode node, List<GraphNode> oneSCC) {
		oneSCC.add(node);
		
		timeLabel += 1;
		node.setStart(timeLabel);
		node.setColor('g');
		
		if(node_neighborNodes_reversed.containsKey(node)) {
			for(GraphNode tnode : node_neighborNodes_reversed.get(node)) {
				if(tnode.getColor() == 'w') {
					SECOND_DFS_VISIT(tnode, oneSCC);
				}
			}
		}
		
		node.setColor('b');
		timeLabel += 1;
		node.setEnd(timeLabel);
		
		return oneSCC;
	}
	
	public List<GraphNode> sortNodes() {
		List<GraphNode> sortedNodes = new ArrayList<GraphNode>(nodes.keySet());
		Collections.sort(sortedNodes, new Comparator<GraphNode>() {
			@Override
			public int compare(GraphNode o1, GraphNode o2) {
				if(o1.getEnd() > o2.getEnd()) {
					return -1;
				} else if(o1.getEnd() < o2.getEnd()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		
		return sortedNodes;
	}	
	
	public List<List<GraphNode>> solveSCC() {
		List<List<GraphNode>> SCCs = new ArrayList<>();
		
		DFS();
		List<GraphNode> sortedNodes = sortNodes();

		timeLabel = 0;			//时间戳回到0点
		for(GraphNode node : sortedNodes) {
			node.setColor('w');
		}
		
		for(GraphNode node : sortedNodes) {
			if(node.getColor() == 'w') {
				SCCs.add(SECOND_DFS_VISIT(node, new ArrayList<GraphNode>()));
			}
		}
		
		return SCCs;
	}
	
	// test this class
	// !!! need to debug
	public static void main(String[] args) {
		String path = ".\\testdata\\SCC.txt";
		StrongConnectedComponentRecursive scc = new StrongConnectedComponentRecursive();
		
		long start = System.currentTimeMillis();
		scc.createGraphFromFile(path);
		long end = System.currentTimeMillis();
		System.out.println("读文件耗时： " + (end - start) / (double)1000 + "s");
		
		start = System.currentTimeMillis();
		List<List<GraphNode>> result = scc.solveSCC();
		end = System.currentTimeMillis();
		System.out.println("强连通分量算法耗时： " + (end -start) / (double)1000 + "s");
		
		List<Integer> sizes = new ArrayList<Integer>();
		for(int i = 0; i < result.size(); i++) {
			sizes.add(result.get(i).size());
		}
		
		Collections.sort(sizes, Collections.reverseOrder());
		
		System.out.println("强连通分量的规模从大到小排序为： " + sizes);
	}
}
