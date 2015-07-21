package Part2;

/**
 * @author moqiguzhu
 * @date 2015-03-31
 * description: 带rank和path compression的Union-Find数据结构实现
 * U-F数据结构对于可以加速Kruskal的MST算法和与之对应的single link聚类算法
 */
public class UF {
	private int[] id;
	private int[] rank;
	private int[] parent;
	private int count; 
	
	public void union(int index1, int index2) {
		if(connected(index1, index2))
			return;
		int parent1 = find(index1);
		int parent2 = find(index2);
		if(rank[parent1] > rank[parent2])
			parent[parent2] = parent1;
		else if(rank[parent2] > rank[parent1])
			parent[parent1] = parent2;
		else {
			parent[parent2] = parent1;
			//the only place where rank can be changed
			rank[parent1] = rank[parent1] + 1;
		}
		count--;
	}
	
	//return the index of its leader node
	public int find(int index) {
		//path compression
		while(parent[index] != parent[parent[index]]) {
			parent[index] = parent[parent[index]];
			//do not change rank
		}
		return parent[index];
	}
	
	//return the current number of trees(clusters)
	//this function may be useful when we implementing single link clustering algorithms
	//pay attention to that the initiated value of "numClusters" equals number of nodes
	public int getCount() {
		return this.count;
	}
	
	public boolean connected(int index1, int index2) {
		return find(index1) == find(index2);
	}
	
	public void init(int[] nodeLabels) {
		int size = nodeLabels.length;
		count = size;
		id = new int[size + 1];
		rank = new int[size + 1];
		parent = new int[size + 1];
		
		id[0] = -1;		//sentinel
		rank[0] = -1;		//sentinel
		parent[0] = -1;		//sentinel
		for(int i = 1; i < size+1; i++) {
			id[i] = nodeLabels[i-1];
			rank[i] = 0;
			parent[i] = i;
		}
	}
	
	//test this data structure
	public static void main(String[] args) {
	}
}
