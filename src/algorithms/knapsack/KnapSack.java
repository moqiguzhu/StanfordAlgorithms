package algorithms.knapsack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class KnapSack {
	/*Stack for DFS*/
	private Stack<KnapSackNode> stack;
	/*current optimal result*/
	private double curOpt;
	/*物品的价值密度*/
	private double[] ratios;
	/*按照物品的价值密度从高到底排序对应的物品下标*/
	private int[] indices;
	/*greedy optimal*/
	private double greedyOpt;
	/*relax optimal*/
	private double relaxOpt;
	/*物品的价值*/
	private int[] vals;
	/*物品的重量*/
	private int[] weights;
	/*背包的容量*/
	private int capacity;
	
	public KnapSack(int[] vals, int[] weights, int capacity) {
		this.vals = vals;
		this.weights = weights;
		this.capacity = capacity;
		init();
	}

	private void init() {
		this.stack = new Stack<KnapSackNode>();
		ratios = new double[vals.length];
		indices = new int[vals.length];
		for(int i = 0; i < vals.length; i++) {
			ratios[i] = (double)vals[i] / weights[i];
			indices[i] = i;
		}
		quickSort(0,ratios.length-1,ratios,indices);
		int i = 0;
		int tempCap = capacity;
		while(tempCap - weights[indices[i]] >= 0) { 
			relaxOpt += vals[indices[i]];
			tempCap -= weights[indices[i++]];
		}
		relaxOpt += (double)tempCap/weights[indices[i]] * vals[indices[i]];
		
		tempCap = capacity;
		for(i = 0; i < indices.length; i++) {
			if(tempCap - weights[indices[i]] >= 0) {
				greedyOpt += vals[indices[i]];
				tempCap -= weights[indices[i]];
			}
		}
	}
	
	public void quickSort(int l, int r, double[] num, int[] index) {
		if(r <= l) return;
		double pivot = num[l];
		// i j 两个指针
		int i = l+1, j = l+1;
		double temp;
		for(int k = l+1; k <= r; k++) {
			if(num[k] < pivot) j++;
			else {
				temp = num[i];
				num[i] = num[j];
				num[j] = temp;
				//change index information
				temp = index[i];
				index[i] = index[j];
				index[j] = (int)temp;
				i++; j++;
			}
		}
		//交换 注意是i-1
		temp = num[l];
		num[l] = num[i-1];
		num[i-1] = temp;
		//change index information
		temp = index[l];
		index[l] = index[i-1];
		index[i-1] = (int)temp;
		
		quickSort(l, i-2, num, index);
		quickSort(i, r, num, index);
	}
	
	//放缩函数，假定可以取分数件的物品
	public double relax(int index, int curCap) {
		double relaxReuslt = 0;
		int i = 0;
		for(; i < weights.length; i++) {
			if(curCap - weights[indices[i]] >= 0 && indices[i] > index) {
				relaxReuslt += vals[indices[i]];
				curCap -= weights[indices[i]];
			} else if(curCap - weights[indices[i]] < 0) break;
			else if(indices[i] <= index) continue;
		}

		if(i == weights.length) return relaxReuslt;		//在这种情况下，背包的容量能够装下所有的物品
		else return relaxReuslt += (double)curCap/weights[indices[i]] * vals[indices[i]];
	}
	
	public double solveKnapSack() {
		// init current optimal using greedy strategy
		curOpt = greedyOpt;
		//DFS
		KnapSackNode firstNode = new KnapSackNode(0, relaxOpt, capacity, 1, 0);
		stack.add(firstNode);
		KnapSackNode tempNode;
		int curVal, remainCap, state, index;
		double estVal;
		while(!stack.isEmpty()) {
			tempNode = stack.peek();
			if(tempNode.getState() == 1) {
				if(tempNode.getRemainCap() >= weights[tempNode.getIndex()]) {
					stack.peek().setState(2);
					index = tempNode.getIndex();
					curVal = tempNode.getCurVal() + vals[index];
					remainCap = tempNode.getRemainCap() - weights[index];
					//relax
					estVal = curVal + relax(index, remainCap);
					//pruning
					if(estVal < curOpt) continue;
					state = 1;
					index = index+1;
					//update current optimal result
					if(index == weights.length) {
						if(curVal > curOpt) curOpt = curVal;
						continue;
					}
					KnapSackNode node = new KnapSackNode(curVal, estVal, remainCap, state, index);
					stack.push(node);
				} else 
					tempNode.setState(2);
			} else {
				tempNode = stack.pop();
				index = tempNode.getIndex();
				curVal = tempNode.getCurVal();
				remainCap = tempNode.getRemainCap();
				//relax
				estVal = curVal + relax(index, remainCap);		
				//pruning
				if(estVal < curOpt) continue;
				state = 1;
				index = index+1;
				if(index == weights.length) {
					if(curVal > curOpt) curOpt = curVal;
					continue;
				}
				KnapSackNode node = new KnapSackNode(curVal, estVal, remainCap, state, index);
				stack.push(node);
			}
		}
		return curOpt;
	}
	
	//test this class
	public static void main(String[] args) throws Exception{
        List<String> lines = new ArrayList<String>();
        String filePath = "C:\\Master\\Stanford Algorithms 2\\week3\\knapsack_big.txt";

        BufferedReader input =  new BufferedReader(new FileReader(filePath));
        try {
            String line = null;
            while (( line = input.readLine()) != null){
                lines.add(line);
            }
        }
        finally {
            input.close();
        }
        
        String[] firstLine = lines.get(0).split("\\s+");
        int capacity = Integer.parseInt(firstLine[0]);
        int num_item = Integer.parseInt(firstLine[1]);

        int[] values = new int[num_item];
        int[] weights = new int[num_item];

        for(int i=1; i < num_item+1; i++){
          String line = lines.get(i);
          String[] parts = line.split("\\s+");

          values[i-1] = Integer.parseInt(parts[0]);
          weights[i-1] = Integer.parseInt(parts[1]);
        }
        
        KnapSack ks = new KnapSack(values, weights, capacity);
        
		//test solve function
		long start = System.currentTimeMillis();
		System.out.println("the real optimal result: " + ks.solveKnapSack());
		long end = System.currentTimeMillis();
		System.out.println("time consuming is " + (end-start));
	}
}
