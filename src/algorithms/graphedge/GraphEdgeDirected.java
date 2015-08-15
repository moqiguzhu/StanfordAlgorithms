package algorithms.graphedge;

import algorithms.graphnode.GraphNode;

/**
 * GraphEdgeDirected和GraphEdge的唯一不同之处在于其equals函数的书写，后者表示的是无向图这个概念
 * 
 * @author moqiguzhu
 * @date 2015-08-15
 * @version 1.0
 */
public class GraphEdgeDirected {
	/* 边左边的节点 */
	private GraphNode leftNode;
	/* 边右边的节点 */
	private GraphNode rightNode;
	/* 边权值 */
	private double weight;

	public GraphEdgeDirected(GraphNode leftNode, GraphNode rightNode,
			double weight) {
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

	@Override
	public boolean equals(Object o) {
		GraphEdge edge = (GraphEdge) o;
		return (edge.getLeftNode().equals(this.leftNode) && edge.getRightNode()
				.equals(this.rightNode));
	}

	@Override
	public int hashCode() {
		return this.leftNode.hashCode() * this.rightNode.hashCode()
				% Integer.MAX_VALUE;
	}

	@Override
	public String toString() {
		return "left:" + leftNode.getLabel() + " " + "right:"
				+ rightNode.getLabel() + " " + "weight:" + weight;
	}
}
