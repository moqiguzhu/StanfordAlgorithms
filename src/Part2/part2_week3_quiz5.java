package Part2;

public class part2_week3_quiz5 {
	public static void main(String[] args) {
		double[] p = new double[]{0.2, 0.05, 0.17, 0.1, 0.2, 0.03, 0.25};
		double[][] table = new double[8][8];	
		
		for(int s = 0; s <= 6; s++) {
			for(int i = 1; i+s <= 7; i++) {
				double temp1 = 0;
				double temp2 = Double.MAX_VALUE;
				for(int k = i; k <= i+s; k++)
					temp1 += p[k-1];
				for(int k = i; k <= i+s; k++) {
					double temp3 = 0;
					if(k > i) temp3 += table[i][k-1];
					if(k < i+s) temp3 += table[k+1][i+s];
					if(temp2 > temp3)
						temp2 = temp3;
				}
				if(temp2 == Double.MAX_VALUE) temp2 = 0;
				table[i][i+s] = temp1 + temp2;
			}
		}
		
		System.out.println(table[1][7]);
	}
}
