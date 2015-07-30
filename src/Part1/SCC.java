package Part1;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.sound.sampled.Line;

/**
 * 
 * @author moqiguzhu
 * @date	2015-02-14
 * 假设所要处理的图的规模不超过int能够表示的范围
 */
class vertex {
	/*节点的颜色 b表示黑色 w表示白色 g表示灰色*/
	private char color;
	/*节点的发现时间*/
	private long d;
	/*节点的完成时间*/
	private long f;
	/*p保存父节点信息*/
	private int p;
	
	public vertex(char c, long d, long f, int p) {
		this.color = c;
		this.d = d;
		this.f = f;
		this.p = p;
	}
	
	@Override
	public String toString() {
		return " " + color + " " + d + " " + f + " " + p + "\n";
	}

	public char getColor() {
		return color;
	}

	public void setColor(char color) {
		this.color = color;
	}

	public long getD() {
		return d;
	}

	public void setD(long d) {
		this.d = d;
	}

	public long getF() {
		return f;
	}

	public void setF(long f) {
		this.f = f;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}	
}

public class SCC {
	/*存储边信息*/
	private static List<List<Integer>> edges;
	/*存储图的转置  对于转置图 节点信息并没有改变 改变的只是边信息*/
	private static List<List<Integer>> reverseEdges;
	/*存储节点信息  注意：边信息和节点信息分开存放能够有效地节省空间*/
	private static List<vertex> vertices;
	/*不想把第一次运行DFS之后得到的信息全部毁掉  运行第二次DFS的时候使用vertices_copy*/
	private static List<vertex> vertices_copy;
	/*时间戳变量*/
	private static long time = 0L;
	/*记录强联通分量的大小*/
	private static int size = 0;
	
	public void createGraph() throws Exception {
		String path = ".\\testdata\\SCC_test1.txt";
		File f = new File(path);
		Scanner sc = new Scanner(f);
		int label,neighbor;
		edges = new ArrayList<List<Integer>>();
		edges.add(new ArrayList<Integer>());	//sentinel
		//test6.txt会报错 只有入度的节点15没有识别出来
		while(sc.hasNext()) {
			label = sc.nextInt();
			neighbor = sc.nextInt();
			while(Math.max(label,neighbor) >= edges.size()) {
				List<Integer> list = new ArrayList<Integer>();
				edges.add(list);
			}
			edges.get(label).add(neighbor);
		}
		sc.close();
		//初始化节点信息
		vertices = new ArrayList<vertex>();
		vertices_copy = new ArrayList<vertex>();
		vertices.add(new vertex('w',0L,0L,0));	//sentinel
		vertices_copy.add(new vertex('w',0L,0L,0));	//sentinel
		for(int i = 1; i < edges.size(); i++) {
			vertex v = new vertex('w',0L,0L,0);
			vertices.add(v);
			vertex v_copy = new vertex('w',0L,0L,0);
			vertices_copy.add(v_copy);
		}		
	}
	
	//第一遍的DFS是要在G转置上跑的 第一遍在G上跑也没有问题 结果都是一样的
	//CLRS上说的和Stanford视频中说的不一样 
	public void DFS() {
		time = 0;
		for(int i = 1; i < vertices_copy.size(); i++) {
			if(vertices_copy.get(i).getColor() == 'w') {
				DFS_VISIT(vertices_copy.get(i),i);
			}
		}
	}
	
	/**
	 * 
	 * @param u	当前要处理的节点
	 * @param label 当前要处理的节点的标号
	 */
	//或许我应该使用一个非递归的实现
	//使用一个栈
	public void DFS_VISIT(vertex u, int label) {
		time = time+1;
		u.setD(time);
		u.setColor('g');
		for(int x : reverseEdges.get(label)) {
			if(vertices_copy.get(x).getColor() == 'w') {
				vertices_copy.get(x).setP(label);
				DFS_VISIT(vertices_copy.get(x),x);			//StackOverflowError 递归是不行的
			}
		}
		u.setColor('b');
		time = time + 1;
		u.setF(time);
	}

	public void SECOND_DFS_VISIT(vertex u, int label) {
		time = time+1;
		u.setD(time);
		u.setColor('g');
		for(int x : edges.get(label)) {
			if(vertices.get(x).getColor() == 'w') {
				vertices.get(x).setP(label);
				SECOND_DFS_VISIT(vertices.get(x),x);
			}
		}
		u.setColor('b');
		time = time + 1;
		size++;
		u.setF(time);
	}
	
	public void reverseGraph() {
		reverseEdges = new ArrayList<List<Integer>>();
		reverseEdges.add(new ArrayList<Integer>());
		for(int i = 1; i < vertices.size(); i++) {
			List<Integer> list = new ArrayList<Integer>();
			reverseEdges.add(list);
		}
		//当然可以从文件中构建图的转置 读磁盘操作花费的时间比较长 这里选择从已经构建好的图中构建图的转置
		for(int i = 1; i < edges.size(); i++) {
			for(int x : edges.get(i)) 
				reverseEdges.get(x).add(i);
		}
	}
	
	public void fiveLargestSCC() {
		long maxF;
		int maxLabel;
		time = 0;		//时间戳回到0点
		List<Integer> result = new ArrayList<Integer>();
		Map<Long,Integer> info = new HashMap<Long,Integer>();
		List<Long> Fs = new ArrayList<Long>(); 
		for(int i = 0; i < vertices_copy.size(); i++) {
			info.put(vertices_copy.get(i).getF(),i);
			Fs.add(vertices_copy.get(i).getF());
		}
		Collections.sort(Fs, Collections.reverseOrder());
		for(int i = 0; i < Fs.size(); i++) {
			maxF = Fs.get(i);
			maxLabel = info.get(maxF);
			if(vertices.get(maxLabel).getColor() == 'b') continue;
			else {
				size = 0;
				SECOND_DFS_VISIT(vertices.get(maxLabel),maxLabel);
				result.add(size);
			}
		}
		Collections.sort(result,Collections.reverseOrder());
		for(int i = 0; i < Math.min(5, result.size()); i++) 
			System.out.println(result.get(i) + "  ");
	}
	
	//先使用小案例进行测试 
	public static void main(String[] args) throws Exception{
		SCC scc = new SCC();
		scc.createGraph();
		scc.reverseGraph();
		scc.DFS();
		scc.fiveLargestSCC();
	}
}

