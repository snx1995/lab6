package seproject1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.Scanner;

public class Project1 {

	public static void main(String[] args) {
		String word1 = "to";
		String word2 = "to";
		Graph g1 = createDirectedGraph("data.txt");
		g1.debugCons();
		System.out.println(queryBridgeWords(g1, word1, word2));
		for (String word : g1.getBridgeWords(word1, word2)) {
			System.out.println(word);
		}
		System.out.println(generateNewText(g1, "new and"));
		showDirectedGraph(g1);
		for(int i=0;i<g1.getNumberV();i++) {
			System.out.println(g1.getEdgeNum(i));
		}
		System.out.println(randomWalk(g1));
		 System.out.println(calcShortestPath(g1,"to","to"));
		 
		 System.out.println(1);
		 g1=null;
		 
		 
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
	static String queryBridgeWords(Graph graphG, String word1, String word2) {
		String[] bridge = graphG.getBridgeWords(word1, word2);
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

	static void showDirectedGraph(Graph graphG) {
		GraphViz gViz = new GraphViz("C:\\Users\\majunhua123\\Lab4\\Lab4",
				"C:\\graphviz\\release\\bin\\dot.exe");
		gViz.startofGraph();
		for (G_List list : graphG.getLists()) {
			if (list.next != null) {
				G_List tmp = list;
				while (tmp != null) {
					if(tmp.cost!=0) 
					gViz.addln(graphG.getVector()[list.wordPlace] + "->" + graphG.getVector()[tmp.wordPlace] +"[label=\""+tmp.cost+"\"]"+ ";");
					tmp = tmp.next;
				}
			}
		}
		gViz.endofGraph();
		try {
			gViz.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void showDirectedGraph(Graph graphG,Map<String,String>map) {
		GraphViz gViz = new GraphViz("E:\\Projects\\java\\SEProject_1\\shortestPath",
				"C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe");
		gViz.startofGraph();
		for (G_List list : graphG.getLists()) {
			if (list.next != null) {
				G_List tmp = list;
				while (tmp != null) {
					if(tmp.cost!=0) {
						if(map.containsKey(graphG.getVector()[list.wordPlace])&&map.get(graphG.getVector()[list.wordPlace]).equals(graphG.getVector()[tmp.wordPlace]))
							gViz.addln(graphG.getVector()[list.wordPlace] + "->" + graphG.getVector()[tmp.wordPlace] +"[color= red label=\""+tmp.cost+"\"]"+ ";");
						else
							gViz.addln(graphG.getVector()[list.wordPlace] + "->" + graphG.getVector()[tmp.wordPlace] +"[label=\""+tmp.cost+"\"]"+ ";");
					}
					tmp = tmp.next;
				}
			}
		}
		gViz.endofGraph();
		try {
			gViz.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	static String generateNewText(Graph graphG, String inputText) {
		Random random = new Random();
		String[] letters = inputText.split("[^a-zA-Z]+");
		String[] bridgeWords = null;
		for (int i = 0; i < letters.length; i++)
			letters[i] = letters[i].toLowerCase();
		String newText = letters[0];
		for (int i = 1; i < letters.length; i++) {
			bridgeWords = graphG.getBridgeWords(letters[i - 1], letters[i]);
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

	public static String randomWalk(Graph graphG) {
		int n = graphG.getNumberV();
		int edgeTmp=0;
		boolean[][] edgeVisited = new boolean[n][n];
		
		Random random = new Random();
		int firstWord = random.nextInt(n);
		G_List lists[] = graphG.getLists();
		String walkPath = graphG.getWord(firstWord) + " ";
		G_List tmp=lists[firstWord];
		int edge_bound = graphG.getEdgeNum(firstWord);
		if(edge_bound==0) return graphG.getWord(firstWord);
		int edge = random.nextInt(edge_bound);
		//System.out.println(edge);
		for(int i=0;i<=edge;i++) {
			tmp=tmp.next;
		}
		int nextWord = tmp.wordPlace;
		//System.out.println("nextword:"+nextWord);
		while(!edgeVisited[firstWord][nextWord]) {
			edgeVisited[firstWord][nextWord]=true;
			walkPath = walkPath + graphG.getWord(nextWord)+" ";
			System.out.println(firstWord + "-" + nextWord);
			firstWord=nextWord;
			tmp=lists[firstWord];
			edgeTmp=graphG.getEdgeNum(firstWord);
			if(edgeTmp==0) break;
			edge = random.nextInt(edgeTmp);
			for(int i=0;i<=edge;i++) {
				tmp=tmp.next;
			}
			nextWord = tmp.wordPlace;
		}
		if(edgeTmp!=0) walkPath+=graphG.getWord(tmp.wordPlace);
		//to explore strange new worlds seek out life and civilizations this 
		return walkPath;
	}
	
	static String calcShortestPath(Graph graphG, String word1, String word2) {
		if(graphG.getWordPlace(word1)==-1||graphG.getWordPlace(word2)==-1)
			return "word1 or word2 not in graph";
		if(word1.equals(word2))
			return word1+"->"+word2;
		String minPath="";
		int maxNum=graphG.getNumberV();
		boolean known[]= new boolean[maxNum];
		int length[]=new int[maxNum];
		int wordsPlace[]= new int[maxNum];
		int wordPlace=graphG.getWordPlace(word1);
		known[wordPlace]=true;
		int vect_num=1;
//		System.out.println(maxNum);
		for(int i=0;i<maxNum;i++) {
			length[i]=9999;
		}
		length[wordPlace]=0;
		while(vect_num<maxNum) {
			G_List tmp1=graphG.getLists()[wordPlace];
			G_List tmp2=tmp1.next;
//			System.out.println(vect_num);
			while(tmp2!=null) {
				if(length[tmp2.wordPlace]>length[tmp1.wordPlace]+tmp2.cost) {
					length[tmp2.wordPlace]=length[tmp1.wordPlace]+tmp2.cost;
					wordsPlace[tmp2.wordPlace]=tmp1.wordPlace;
				}
				tmp2=tmp2.next;
			}
			wordPlace = findMin(length,known,maxNum);
			if(wordPlace==-1)break;
			known[wordPlace]=true;
			vect_num++;
		}
		for(int i=0;i<maxNum;i++) {
		System.out.println(i+":"+known[i]+" "+length[i]+" "+graphG.getWord(wordsPlace[i]));
		}
		int placeOfWord2=graphG.getWordPlace(word2);
//		System.out.println(placeOfWord2);
		if(length[placeOfWord2]==9999) {
			minPath="两单词不可达";
			return minPath;
		}
		else {
			int tmp=wordsPlace[placeOfWord2];
			minPath="->"+word2;
			while(length[tmp]!=0) {
				minPath="->"+graphG.getWord(tmp)+minPath;
				tmp=wordsPlace[tmp];
			}
			minPath=word1+minPath;
		}
		Map<String,String>map=new HashMap<String,String>();
		int tmp=placeOfWord2;
		while(length[tmp]!=0) {
			System.out.println(graphG.getWord(tmp));
			map.put(graphG.getWord(wordsPlace[tmp]),graphG.getWord(tmp));
			tmp=wordsPlace[tmp];
		}
		showDirectedGraph(graphG,map);
		return minPath;
		//to explore strange new worlds seek out life and civilizations this hello 
	}
	static int findMin(int[]length,boolean[]known,int maxNum) {
		int min_place=-1;
		int min=999;
		for(int i=0;i<maxNum;i++) {
			if(known[i]==false&&length[i]<min) {
				min_place=i;
				min=length[i];
			}
		}
		return min_place;
	}
}

// 图节点
class G_List {
	public int wordPlace;
	public G_List next;
	public int cost;

	public G_List(int w, int c) {
		wordPlace = w;
		cost = c;
		next = null;
	}

	public G_List() {
	}
}

// 图
class Graph {
	private String vector[] = {};
	private int numberOfV , numberOfN;
	private G_List lists[] = {};
	// 存储word-》wordPlace
	private Map<String, Integer> letters = new HashMap<String, Integer>();

	public Graph(String words[]) {
		int pre = -1;
		numberOfN = words.length;
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
					tmp.wordPlace = vector.length - 1;
					if (lists[pre] == null)
						lists[pre] = tmp;
					else {
						tmp.next = lists[pre].next;
						lists[pre].next = tmp;
					}
				}
				pre = tmp.wordPlace;
			} else {
				G_List tmp = lists[pre];
				int wtmp = letters.get(word);
				boolean flag = false;
				while (tmp != null) {
					if (tmp.wordPlace == wtmp) {
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
		numberOfV = vector.length;
	}

	public String debugCons() {
		
		for (G_List list : lists) {
			G_List temp = list;
			while (temp != null) {
				
				temp = temp.next;
			}
		
		}
		return "";
	}

	public String[] getBridgeWords(String word1, String word2) {
		String[] bridgeWords = {};
		int placeOfWord1 = letters.containsKey(word1) ? letters.get(word1) : -1;
		int placeOfWord2 = letters.containsKey(word2) ? letters.get(word2) : -1;
		if (placeOfWord1 < 0 || placeOfWord2 < 0) {
			return null;
		}
		G_List tmp1 = lists[placeOfWord1].next;
		while (tmp1 != null) {
			G_List tmp2 = lists[tmp1.wordPlace].next;
			while (tmp2 != null) {
				if (tmp2.wordPlace == placeOfWord2) {
					bridgeWords = Arrays.copyOf(bridgeWords, bridgeWords.length + 1);
					bridgeWords[bridgeWords.length - 1] = vector[tmp1.wordPlace];
					break;
				}
				tmp2 = tmp2.next;
			}
			tmp1 = tmp1.next;
		}
		return bridgeWords;
	}

	public String[] getVector() {
		return vector;
	}

	public int getNumberV() {
		return numberOfV;
	}

	public int getNumberN() {
		return numberOfN;
	}

	public G_List[] getLists() {
		return lists;
	}

	public int getWordPlace(String word) {
		if (letters.containsKey(word))
			return letters.get(word);
		else
			return -1;
	}

	public String getWord(int place) {
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
