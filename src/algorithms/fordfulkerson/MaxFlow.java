package algorithms.fordfulkerson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import algorithms.graphedge.GraphEdge;
import algorithms.graphnode.GraphNode;
import javafx.scene.Parent;

/**
 * 数据格式： 第一行：节点个数 第二行：source节点 sink节点 第三行： left right weight ......
 * 
 * @author moqiguzhu
 * @dat2 2016-03-14
 * @version 1.0
 *
 */
public class MaxFlow {
  /* 存储图的节点之间的连接信息 */
  private Map<GraphNode, List<GraphNode>> node_neighborNodes;

  /* 存储图的边权值信息 */
  private Map<GraphEdge, GraphEdge> edge_edge;

  /* source节点 */
  GraphNode source;

  /* sink节点 */
  GraphNode sink;

  public void createGraphFromFile(String path) throws Exception {
    Scanner sc = new Scanner(System.in);
    String line = "";
    String regex = "\\s";
    String[] strArr;

    sc.nextLine(); // 不要第一行
    while (sc.hasNext()) {
      line = sc.nextLine();

    }
  }

  Map<GraphNode, GraphNode> BFS() {
    Map<GraphNode, GraphNode> node_parent = new HashMap<>();
    // 孤立节点不用考虑
    Set<GraphNode> visited = new HashSet<>();

    LinkedList<GraphNode> queue = new LinkedList<>();
    queue.add(source);
    visited.add(source);
    node_parent.put(source, null); // null means no parent

    while (queue.size() != 0) {
      GraphNode tmp = queue.poll();

      for (GraphNode node : node_neighborNodes.get(tmp)) {
        if (!visited.contains(node)) {
          queue.add(node);
          node_parent.put(node, tmp);
          visited.add(node);

          if (node.equals(sink)) {
            return node_parent;
          }
        }
      }
    }

    // null means no valid path anymore
    return null;
  }

  public double fordFulkerson() {
    double max_flow = 0;
    Map<GraphNode, GraphNode> node_parent;
    while ((node_parent = BFS()) != null) {
      double path_flow = Double.MAX_VALUE;

      GraphNode p = node_parent.get(sink);
      GraphNode cur = sink;
      while (p != null) {
        path_flow = Math.min(path_flow, edge_edge.get(new GraphEdge(p, sink, 0.0)).getWeight());
        cur = p;
        p = node_parent.get(p);
      }

      p = node_parent.get(sink);
      cur = sink;
      while (p != null) {
        GraphEdge t_edge = new GraphEdge(p, cur, 0.0);
        // !!!还要处理反向边
        // 因为节点的标号不一定连续 所以使用邻接矩阵也不是很方便
        // 只有在知道节点的标号连续 并且知道节点个数的情况下使用临界矩阵才会比较方便
        // 这个实现效率低 因为可能新增边
        // node_neighbourNodes要变
        edge_edge.get(t_edge).setWeight(edge_edge.get(t_edge).getWeight() - path_flow);
        cur = p;
        p = node_parent.get(p);
      }
      
      max_flow += path_flow;
    }
    
    return max_flow;
  }
  
  // test this class
  public static void main(String[] args) {
    
  }
  
}
