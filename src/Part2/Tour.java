package Part2;

import java.util.List;

/**
 * @author moqiguzhu
 * @date 2015-04-25
 * description: Hanmilton圈类，在解决TSP问题时定义的类
 */
public class Tour {
	private double cost;
	private List<Edge> edges;
	//指定边的访问顺序，初始化的时候边的访问顺序就是边在edges里面的存储顺序
	private int[] order;
	
	public Tour(List<Edge> edges, double cost) {
		this.cost = cost;
		this.edges = edges;
		//初始化order
		order = new int[edges.size()];
		for(int i = 0; i < edges.size(); i++) {
			order[i] = edges.get(i).getRight();
		}
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public int[] getOrder() {
		return order;
	}

	public void setOrder(int[] order) {
		this.order = order;
	}
}
