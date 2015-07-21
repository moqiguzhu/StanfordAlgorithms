package Part2;

/**
 * @author moqiguzhu
 * @date 2015-04-25
 * description: 平面上的点类，除了横纵坐标值之外，还拥有label属性，这是给平面上的点打的标签
 */
public class Point {
	private double x;
	private double y;
	private int label;
	
	public Point(double x, double y, int label) {
		this.x = x;
		this.y = y;
		this.label = label;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}
	
	
}
