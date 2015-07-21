package Part2;

import java.io.BufferedReader;
import java.io.FileReader;

public class ZeroOneKnapsack {
	private int num_items;
	private int capacity;
	private int[] values;
	private int[] weights;
	
	public void readFiles(String filePath) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		
		capacity = Integer.parseInt(line.split("\\s")[0]);
		num_items = Integer.parseInt(line.split("\\s")[1]);
		values = new int[num_items];
		weights = new int[num_items];
		
		int index = 0;
		while((line = br.readLine()) != null) {
			values[index] = Integer.parseInt(line.split("\\s")[0]);
			weights[index++] = Integer.parseInt(line.split("\\s")[1]);
		}
		br.close();
	}
	
	public int optimalSelecting() {
        int[][] table = new int[capacity+1][num_items+1];
        for(int i = 0; i < capacity+1; i++)
        	table[i][0] = 0;
        for(int j = 1; j < num_items+1; j++) {
            for(int i = 0; i < capacity+1; i++) {
            	if(i-weights[j-1] < 0)
            		table[i][j] = table[i][j-1];
            	else 
            		table[i][j] = Math.max(table[i][j-1], table[i-weights[j-1]][j-1]+values[j-1]);
            }
    	}
        return table[capacity][num_items];
	}
	
	//test this class
	//test more smaller data sets
	public static void main(String[] args) throws Exception{
		ZeroOneKnapsack zok = new ZeroOneKnapsack();
		String filePath = "C:\\Master\\Stanford Algorithms 2\\week3\\knapsack1.txt";
		zok.readFiles(filePath);
		System.out.println(zok.optimalSelecting());
	}
}
