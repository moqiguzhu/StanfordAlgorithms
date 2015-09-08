package algorithms.TSP;
import java.util.List;

import algorithms.graphedge.GraphEdge;

/**
 * @author moqiguzhu
 * @version 1.0
 * @date 2015-04-25
 * 
 * description: Hanmilton圈类，在解决TSP问题时定义的类。TSP问题的实质就是要找到最小的Hanmilton圈
 */
public class Tour {
	/*Hanmilton圈的所有边权值之和*/
	private double cost;
	
	/*存储结点的访问顺序*/
	private int[] order;
	
	public Tour(List<GraphEdge> edges, double cost) {
		this.cost = cost;
		//初始化order
		order = new int[edges.size()];
		for(int i = 0; i < edges.size(); i++) {
			order[i] = edges.get(i).getRightNode().getLabel();
		}
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public int[] getOrder() {
		return order;
	}

	public void setOrder(int[] order) {
		this.order = order;
	}
}

