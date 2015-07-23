package algorithms.mincut;
/**
 * @author moqiguzhu
 * @date 2015-07-23
 * @version 1.0
 */

/*
 * 节点类，节点标识符一旦确定，不允许改变
 * 如果后续给节点加入更多的属性，则添加对应的方法
 */
public class GraphNode {
	/*节点标识符*/
	private int label;
	
	public GraphNode(int label) {
		this.label = label;
	}
	
	public int getLabel() {
		return this.label;
	}
	
	@Override
	public String toString() {
		return "" + label;
	}
	
	@Override
	public boolean equals(Object o) {
		GraphNode node = (GraphNode)o;
		return node.getLabel() == label;
	}
	
	@Override
	public int hashCode() {
		return label;
	}
}
