package Part2;

public class Node {
	private int label;			//节点标号
	private double dist;			//该节点到已经生成的部分生成树的距离
	
	public Node(int l, double d) {
		label = l;
		dist = d;
	}
	
	//Override
	public String toString() {
		return "" + label + "  " + dist;  
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public double getDist() {
		return dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}
}
