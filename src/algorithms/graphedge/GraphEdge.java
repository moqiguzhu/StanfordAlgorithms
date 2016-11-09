package algorithms.graphedge;

import algorithms.graphnode.GraphNode;

public class GraphEdge {
  /* 边左边的节点 */
  private GraphNode leftNode;
  /* 边右边的节点 */
  private GraphNode rightNode;
  /* 边权值 */
  private double weight;

  public GraphEdge(GraphNode leftNode, GraphNode rightNode, double weight) {
    this.leftNode = leftNode;
    this.rightNode = rightNode;
    this.weight = weight;
  }

  public GraphNode getLeftNode() {
    return leftNode;
  }

  public GraphNode getRightNode() {
    return rightNode;
  }

  public double getWeight() {
    return weight;
  }
  
  // max-flow算法需要用到
  public void setWeight(double weight) {
    this.weight = weight;
  }

  @Override
  public boolean equals(Object o) {
    GraphEdge edge = (GraphEdge) o;
    return (edge.getLeftNode().equals(this.leftNode) && edge.getRightNode().equals(this.rightNode));
  }

  @Override
  public int hashCode() {
    return this.leftNode.hashCode() * this.rightNode.hashCode() % Integer.MAX_VALUE;
  }

  @Override
  public String toString() {
    return "left:" + leftNode.getLabel() + " " + "right:" + rightNode.getLabel() + " " + "weight:"
        + weight;
  }
}
