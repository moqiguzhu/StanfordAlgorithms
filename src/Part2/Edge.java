package Part2;

public class Edge {
	private double cost;
	private int left;
	private int right;
	
	public Edge(double cost, int left, int right) {
		this.cost = cost;
		this.left = left;
		this.right = right;
	}
	
	public double getCost() {
		return this.cost;
	}
	
	public int getLeft() {
		return this.left;
	}
	
	public int getRight() {
		return this.right;
	}
	
	@Override
	public String toString() {
		return left + " " + right + " " + cost + " "; 
	}
}
