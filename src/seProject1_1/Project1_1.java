package seProject1_1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class Project1_1 {

	public static void main(String[] args) {
		String word1 = "to";
		String word2 = "to";
		Graph g1 = createDirectedGraph("data.txt");
		g1.cons_debug();
		System.out.println(queryBridgeWords(g1, word1, word2));
		for (String word : g1.getBridgeWords(word1, word2)) {
			System.out.println(word);
		}
		System.out.println(generateNewText(g1, "new and"));
		// showDirectedGraph(g1);
		for(int i=0;i<g1.get_v_number();i++) {
			System.out.println(g1.getEdgeNum(i));
		}
		System.out.println(randomWalk(g1));
	}

	// 合并字符数组a,b
	static String[] concat(String[] a, String[] b) {
		String[] c = new String[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	// 创建邻接表
	public static Graph createDirectedGraph(String filename) {
		// wordtmp保存并分割一行，words保存全部单词
		String[] wordtmp = null, words = {};
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null) {
				// line=line.toLowerCase();
				// 分割方式：以一个或多个全部非小写字母字符为分割点
				wordtmp = line.split("[^a-zA-Z]+");
				for (int i = 0; i < wordtmp.length; i++) {
					wordtmp[i] = wordtmp[i].toLowerCase();
				}
				words = concat(words, wordtmp);
			}
			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		Graph graph = new Graph(words);
		return graph;
	}

	// 输出G中word1和word2的桥接词
	static String queryBridgeWords(Graph G, String word1, String word2) {
		String[] bridge = G.getBridgeWords(word1, word2);
		if (bridge == null)
			return "No " + word1 + " or " + word2 + " in the graph!";
		if (bridge.length == 0)
			return "No bridge words from " + word1 + " to " + word2 + "!";
		if (bridge.length == 1)
			return "The bridge words from " + word1 + " to " + word2 + " is: " + bridge[0];
		String result = "The bridge words from " + word1 + " to " + word2 + " are: ";
		for (int i = 0; i < bridge.length - 1; i++) {
			result = result + bridge[i] + ", ";
		}
		result = result + "and " + bridge[bridge.length - 1] + ".";
		return result;
	}

	static void showDirectedGraph(Graph G) {
		GraphViz gViz = new GraphViz("E:\\Projects\\java\\SEProject_1",
				"C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe");
		gViz.start_graph();
		for (G_List list : G.get_lists()) {
			if (list.next != null) {
				G_List tmp = list.next;
				while (tmp != null) {
					gViz.addln(G.get_vector()[list.word_place] + "->" + G.get_vector()[tmp.word_place] + ";");
					tmp = tmp.next;
				}
			}
		}
		gViz.end_graph();
		try {
			gViz.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String generateNewText(Graph G, String inputText) {
		Random random = new Random();
		String[] letters = inputText.split("[^a-zA-Z]+");
		String[] bridgeWords = null;
		for (int i = 0; i < letters.length; i++)
			letters[i] = letters[i].toLowerCase();
		String newText = letters[0];
		for (int i = 1; i < letters.length; i++) {
			bridgeWords = G.getBridgeWords(letters[i - 1], letters[i]);
			if (bridgeWords == null || bridgeWords.length == 0) {
				newText = newText + " " + letters[i];
			} else if (bridgeWords.length == 1) {
				newText = newText + " " + bridgeWords[0] + " " + letters[i];
			} else {
				newText = newText + " " + bridgeWords[random.nextInt(bridgeWords.length)] + " " + letters[i];
			}
		}
		return newText;
	}

	static String randomWalk(Graph G) {
		int n = G.get_v_number();
		boolean[][] edgeVisited = new boolean[n][n];
		
		Random random = new Random();
		int firstWord = random.nextInt(n);
		G_List lists[] = G.get_lists();
		String walkPath = G.get_word(firstWord) + " ";
		G_List tmp=lists[firstWord];
		int edge = random.nextInt(G.getEdgeNum(firstWord));
		for(int i=0;i<edge;i++) {
			tmp=tmp.next;
		}
		int nextWord = tmp.word_place;
		while(!edgeVisited[firstWord][nextWord]) {
			edgeVisited[firstWord][nextWord]=true;
			System.out.println(firstWord + "-" + nextWord);
			tmp=lists[firstWord];
			walkPath = walkPath + G.get_word(nextWord)+" ";
			firstWord=nextWord;
			edge = random.nextInt(G.getEdgeNum(firstWord));
			for(int i=0;i<edge;i++) {
				tmp=tmp.next;
			}
			nextWord = tmp.word_place;
		}
		//to explore strange new worlds seek out life and civilizations this 
		return walkPath;
	}
}

// 图节点
class G_List {
	public int word_place;
	public G_List next;
	public int cost;

	public G_List(int w, int c) {
		word_place = w;
		cost = c;
		next = null;
	}

	public G_List() {
	}
}

// 图
class Graph {
	private String vector[] = {};
	private int v_number, n_number;
	private G_List lists[] = {};
	// 存储word-》word_place
	private Map<String, Integer> letters = new HashMap<String, Integer>();

	public Graph(String words[]) {
		int pre = -1;
		n_number = words.length;
		for (String word : words) {
			if (!letters.containsKey(word)) {
				G_List tmp = new G_List();
				vector = Arrays.copyOf(vector, vector.length + 1);
				lists = Arrays.copyOf(lists, lists.length + 1);
				vector[vector.length - 1] = word;
				lists[lists.length - 1] = new G_List(lists.length - 1, 0);

				letters.put(word, vector.length - 1);
				if (pre >= 0) {
					tmp.cost = 1;
					tmp.next = null;
					tmp.word_place = vector.length - 1;
					if (lists[pre] == null)
						lists[pre] = tmp;
					else {
						tmp.next = lists[pre].next;
						lists[pre].next = tmp;
					}
				}
				pre = tmp.word_place;
			} else {
				G_List tmp = lists[pre];
				int wtmp = letters.get(word);
				boolean flag = false;
				while (tmp != null) {
					if (tmp.word_place == wtmp) {
						tmp.cost++;
						flag = true;
						break;
					}
					tmp = tmp.next;
				}
				if (!flag) {
					G_List n = new G_List(wtmp, 1);
					n.next = lists[pre].next;
					lists[pre].next = n;
				}
				pre = wtmp;
			}
		}
		v_number = vector.length;
	}

	public String cons_debug() {
		for (String v : vector) {
			System.out.print(v + " ");
		}
		System.out.println();
		for (G_List list : lists) {
			G_List temp = list;
			while (temp != null) {
				System.out.print(temp.word_place + " " + temp.cost + "/");
				temp = temp.next;
			}
			System.out.println();
		}
		return "";
	}

	public String[] getBridgeWords(String word1, String word2) {
		String[] bridgeWords = {};
		int word1_place = letters.containsKey(word1) ? letters.get(word1) : -1;
		int word2_place = letters.containsKey(word2) ? letters.get(word2) : -1;
		if (word1_place < 0 || word2_place < 0) {
			return null;
		}
		G_List tmp1 = lists[word1_place].next;
		while (tmp1 != null) {
			G_List tmp2 = lists[tmp1.word_place].next;
			while (tmp2 != null) {
				if (tmp2.word_place == word2_place) {
					bridgeWords = Arrays.copyOf(bridgeWords, bridgeWords.length + 1);
					bridgeWords[bridgeWords.length - 1] = vector[tmp1.word_place];
					break;
				}
				tmp2 = tmp2.next;
			}
			tmp1 = tmp1.next;
		}
		return bridgeWords;
	}

	public String[] get_vector() {
		return vector;
	}

	public int get_v_number() {
		return v_number;
	}

	public int get_n_number() {
		return n_number;
	}

	public G_List[] get_lists() {
		return lists;
	}

	public int get_word_place(String word) {
		if (letters.containsKey(word))
			return letters.get(word);
		else
			return -1;
	}

	public String get_word(int place) {
		return vector[place];
	}
	
	public int getEdgeNum(int wordPlace) {
		int count = 0;
		G_List tmp=lists[wordPlace];
		while(tmp!=null) {
			if(tmp.next!=null) {
				count++;
			}
			tmp=tmp.next;
		}
		return count;
	}

}
