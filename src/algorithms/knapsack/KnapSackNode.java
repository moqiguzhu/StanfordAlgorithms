package algorithms.knapsack;

class KnapSackNode {
	/*当前加到背包中物品的总价值*/
	private int curVal;
	/*在当前情况下，估计把背包容量用完时物品的总价值*/
	private double estVal;
	/*当前背包的剩余容量*/
	private int remainCap;
	/*state == 1 or state == 2*/
	private int state;
	/*index of item*/
	private int index;
	
	public KnapSackNode(int curVal, double estVal, int remainCap, int state, int index) {
		this.curVal = curVal;
		this.estVal = estVal;
		this.remainCap = remainCap;
		this.state = state;
		this.index = index;
	}

	public int getCurVal() {
		return curVal;
	}

	public void setCurVal(int curVal) {
		this.curVal = curVal;
	}

	public double getEstVal() {
		return estVal;
	}

	public void setEstVal(double estVal) {
		this.estVal = estVal;
	}

	public int getRemainCap() {
		return remainCap;
	}

	public void setRemainCap(int remainCap) {
		this.remainCap = remainCap;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
