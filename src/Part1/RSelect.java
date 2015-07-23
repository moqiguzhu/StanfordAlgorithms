package Part1;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class RSelect {
	/**/
	private Map<Integer, List<Integer>> graph;

	// 代码可以改进的地方还不少
	// 可以用一个数组来表示图 而不是一个map
	public int rselect() {
		// RSelect算法的核心代码
		// 在这个实现中 并没有删除graph中的节点 而是使用info来索引graph
		int rand1, rand2;
		/* info记录当前还剩余多少节点 需要初始化 */// info可不可以有更加高效的实现 比如说 HashSet
		List<Integer> info = new ArrayList<Integer>();
		for (int i = 0; i < graph.size(); i++)
			info.add(i + 1);
		Random random = new Random();
		while (info.size() > 2) {
			rand1 = random.nextInt(info.size());
			int label1 = info.get(rand1);
			rand2 = random.nextInt(graph.get(label1).size());
			int label2 = graph.get(label1).get(rand2);
			info.remove((Integer) label2);
			// graph contract kernel codes
			int size = graph.get(label1).size();
			for (int i = size - 1; i > -1; i--)
				// 删除自环 从后面开始删
				if (graph.get(label1).get(i) == label2)
					graph.get(label1).remove(i);
			for (int x : graph.get(label2)) {
				if (x != label1) {
					graph.get(label1).add(x); // 两边都要处理
					graph.get(x).add(label1);
				}
				graph.get(x).remove((Integer) label2); // 这行将会消耗大量时间
			}
		}
		return graph.get(info.get(0)).size();
	}

	public void createGraph() throws Exception {
		// graph = createTestCases();
		// 从文件中构建图
		graph = new HashMap<Integer, List<Integer>>();
		String currentDir = System.getProperty("user.dir");
		String path = currentDir + File.separator + "testdata" + File.separator + "kargerMinCut.txt";
		File f = new File(path);
		Scanner sc = new Scanner(f);
		String regex = "\\s";
		String[] result;
		while (sc.hasNext()) {
			String str = sc.nextLine();
			result = str.split(regex);
			int v = Integer.valueOf(result[0]);
			List<Integer> list = new ArrayList<Integer>();
			for (int i = 1; i < result.length; i++) {
				list.add(Integer.valueOf(result[i]));
			}
			graph.put(v, list);
		}
		sc.close();
	}

	public Map<Integer, List<Integer>> createTestCases() {
		List<Integer> v1 = new ArrayList<Integer>();
		v1.add(2);
		v1.add(3);
		v1.add(4);
		List<Integer> v2 = new ArrayList<Integer>();
		v2.add(1);
		v2.add(3);
		v2.add(4);
		List<Integer> v3 = new ArrayList<Integer>();
		v3.add(1);
		v3.add(2);
		v3.add(4);
		List<Integer> v4 = new ArrayList<Integer>();
		v4.add(2);
		v4.add(3);
		v4.add(1);
		graph = new HashMap<Integer, List<Integer>>();
		graph.put(1, v1);
		graph.put(2, v2);
		graph.put(3, v3);
		graph.put(4, v4);

		return graph;
	}

	public static void main(String[] args) throws Exception {
		RSelect rs = new RSelect();
		// 运行多次
		int times = 1000;
		int min = Integer.MAX_VALUE;
		int temp;
		
		long start = System.currentTimeMillis();
		while (times-- > 0) {
			rs.createGraph();
			temp = rs.rselect();
			min = temp > min ? min : temp;
		}
		long end = System.currentTimeMillis();
		System.out.println("运行1000次， 耗时" + (end - start) + "ms");
		System.out.println("最小割为： " + min);
	}
}
