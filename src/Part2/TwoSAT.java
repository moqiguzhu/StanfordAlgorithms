package Part2;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

//准备采用规约到SCC的方法来做
//最重要的问题就是将上学期写的SCC的代码的递归的DFS改成迭代就好了

class vertex {
	/*节点的颜色 b表示黑色 w表示白色 g表示灰色*/
	private char color;
	/*节点的发现时间*/
	private long d;
	/*节点的完成时间*/
	private long f;
	/*p保存父节点信息*/
	private int p;
	/*节点的标号信息*/
	private int label;
	
	public vertex(char c, long d, long f, int p, int l) {
		this.color = c;
		this.d = d;
		this.f = f;
		this.p = p;
		this.label = l;
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

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}	
	
}

public class TwoSAT {
	/*存储边信息*/
	private static Map<Integer,List<Integer>> edges;
	/*存储图的转置  对于转置图 节点信息并没有改变 改变的只是边信息*/
	private static Map<Integer,List<Integer>> reverseEdges;
	/*存储节点信息  注意：边信息和节点信息分开存放能够有效地节省空间*/
	private static Map<Integer,vertex> vertices;
	/*不想把第一次运行DFS之后得到的信息全部毁掉  运行第二次DFS的时候使用vertices_copy*/
	private static Map<Integer,vertex> vertices_copy;
	/*时间戳变量*/
	private static long time = 0L;
	/**/
	private Set<Integer> set = new HashSet<Integer>();
	/**/
	private boolean flag;
	/**/
	private Set<Integer> processed;
	
	public void createGraph() throws Exception {
		String path = "C:\\Master\\Stanford Algorithms 2\\week6\\2sat6.txt";
		File f = new File(path);
		Scanner sc = new Scanner(f);
		//文件的第一行数字表示的是2-sat问题中变量的个数以及clause的个数
		sc.nextLine();
		String[] strs;
		String line, regex = "\\s";
		int label1, label2;
		
		processed = new HashSet<Integer>();
		vertices = new HashMap<Integer,vertex>();
		vertices_copy = new HashMap<Integer,vertex>();
		edges = new HashMap<Integer,List<Integer>>();
		reverseEdges = new HashMap<Integer,List<Integer>>();
		
		while(sc.hasNext()) {
			line = sc.nextLine();
			strs = line.split(regex);
			label1 = Integer.parseInt(strs[0]);
			label2 = Integer.parseInt(strs[1]);
			if(!processed.contains(label1)) {
				//默认vertex的label不可能出现Integer.MIN_VALUE
				vertex v1 = new vertex('w', 0L, 0L, Integer.MIN_VALUE, label1);
				vertex v1_minus = new vertex('w', 0, 0, -Integer.MIN_VALUE, -label1);
				vertices.put(label1,v1);
				vertices.put(-label1,v1_minus);
				vertex v1_again = new vertex('w', 0L, 0L, Integer.MIN_VALUE, label1);
				vertex v1_minus_again = new vertex('w', 0L, 0L, Integer.MIN_VALUE, -label1);
				vertices_copy.put(label1, v1_again);
				vertices_copy.put(-label1,v1_minus_again);
				
				processed.add(label1);
				processed.add(-label1);
			}
			if(!processed.contains(label2)) {
				vertex v2 = new vertex('w', 0L, 0L, Integer.MIN_VALUE, label2);
				vertex v2_minus = new vertex('w', 0L, 0L, Integer.MIN_VALUE, -label2);
				vertices.put(label2,v2);
				vertices.put(-label2,v2_minus);
				vertex v2_again = new vertex('w', 0L, 0L, Integer.MIN_VALUE, label2);
				vertex v2_minus_again = new vertex('w', 0L, 0L, Integer.MIN_VALUE, -label2);
				vertices_copy.put(label2,v2_again);
				vertices_copy.put(-label2,v2_minus_again);
				
				processed.add(label2);
				processed.add(-label2);
			}
			//debug
			if(!edges.keySet().contains(-label1)) {
				List<Integer> list = new ArrayList<Integer>();
				list.add(label2);
				edges.put(-label1, list);
			} else {
				edges.get(-label1).add(label2);
			}
			if(!edges.keySet().contains(-label2)) {
				List<Integer> list = new ArrayList<Integer>();
				list.add(label1);
				edges.put(-label2, list);
			} else {
				edges.get(-label2).add(label1);
			}
			
			if(!reverseEdges.keySet().contains(label2)) {
				List<Integer> list = new ArrayList<Integer>();
				list.add(-label1);
				reverseEdges.put(label2, list);
			} else {
				reverseEdges.get(label2).add(-label1);
			}
			if(!reverseEdges.keySet().contains(label1)) {
				List<Integer> list = new ArrayList<Integer>();
				list.add(-label2);
				reverseEdges.put(label1, list);
			} else {
				reverseEdges.get(label1).add(-label2);
			}
		}	
		
		sc.close();
	}
	
	//第一遍的DFS是要在G转置上跑的 第一遍在G上跑也没有问题 结果都是一样的
	//CLRS上说的和Stanford视频中说的不一样 
	public void DFS() {
		time = 0;
		for(int label : vertices_copy.keySet()) {
			vertex v = vertices_copy.get(label);
			if(v.getColor() == 'w')
				DFS_VISIT(v);
		}
	}
	
	/**
	 * 
	 * @param u	当前要处理的节点
	 * @param label 当前要处理的节点的标号
	 */
	//或许我应该使用一个非递归的实现
	//使用一个栈
	public void DFS_VISIT(vertex v) {
		time = time+1;
		v.setD(time);
		v.setColor('g');
		
		int label = v.getLabel();
		if(reverseEdges.get(label) == null) {
			//do nothing
		} else {
			for(int neighbour : reverseEdges.get(label)) {
				if(vertices_copy.get(neighbour).getColor() == 'w') {
					vertices_copy.get(neighbour).setP(label);
					DFS_VISIT(vertices_copy.get(neighbour));
				}
			}
		}

		v.setColor('b');
		time = time + 1;
		v.setF(time);
	}
	
	//在DFS的过程中直接检测2-sat的合法性
	public void SECOND_DFS_VISIT(vertex v) {
		if(!flag)
			return;
		time = time+1;
		v.setD(time);
		v.setColor('g');
		int label = v.getLabel();
		if(edges.get(label) == null) {
			//do nothing
		} else {
			for(int neighbour : edges.get(label)) {
				if(vertices.get(neighbour).getColor() == 'w') {
					vertices.get(neighbour).setP(label);
					SECOND_DFS_VISIT(vertices.get(neighbour));
				}
			}
		}

		v.setColor('b');
		time = time + 1;
		v.setF(time);
		if(!set.contains(-label))
			set.add(label);
		else
			flag = false;
	}
	
	public void checkValid() {
		long maxF;
		int maxLabel;
		time = 0;		//时间戳回到0点
		Map<Long,Integer> info = new HashMap<Long,Integer>();
		List<Long> Fs = new ArrayList<Long>(); 
		for(int label : vertices_copy.keySet()) {
			vertex v = vertices_copy.get(label);
			info.put(v.getF(),label);
			Fs.add(v.getF());
		}
		Collections.sort(Fs, Collections.reverseOrder());
		
		for(int i = 0; i < Fs.size(); i++) {
			maxF = Fs.get(i);
			maxLabel = info.get(maxF);
			if(vertices.get(maxLabel).getColor() == 'b') continue;
			else {
				set.clear();
				flag = true;
				vertex v = vertices.get(maxLabel);
				SECOND_DFS_VISIT(v);
				if(!flag) {
					System.out.println("This 2-sat instance is unsatisfiable!");
					return;
				}
			}
		}
		System.out.println("This 2-sat instance is satisfiable!");
	}
	
	public static void main(String[] args) throws Exception{
		TwoSAT twosat = new TwoSAT();
		twosat.createGraph();
		twosat.DFS();
		twosat.checkValid();
	}
}
