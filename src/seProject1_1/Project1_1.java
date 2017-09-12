package seProject1_1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;



public class Project1_1 {
	public static void main(String[] args) {
		createDirectedGraph("data.txt").cons_degug();
	}

	static String[] concat(String[] a, String[] b) {
		String[] c = new String[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	public static Graph createDirectedGraph(String filename) {
		String[] wordtmp = null, words = {};
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null) {
				wordtmp = line.split("[^a-z^A-Z]{1,}");
				for (int i = 0; i < wordtmp.length; i++) {
					wordtmp[i] = wordtmp[i].toLowerCase();
				}
				words = concat(words, wordtmp);
			}br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		Graph graph = new Graph(words);
		return graph;
	}
	String queryBridgeWords(Graph G, String word1, String word2) {
		int word1_num=G.get_word_place(word1);
		int word2_num=G.get_word_place(word2);
		if(word1_num<0 || word2_num<0) {
			return "No word1 or word2 in the graph!";
		}
		G_List lists[] = G.get_lists();
		G_List tmp1=lists[word1_num];
		int bridge[]= {};
		while(tmp1!=null) {
			G_List tmp2=lists[word1_num];
			while(tmp2!=null) {
				if(tmp2.word_place==word2_num) {
					bridge=Arrays.copyOf(bridge, bridge.length+1);
					bridge[bridge.length-1]=tmp1.word_place;
					break;
				}
				tmp2=tmp2.next;
			}
			tmp1=tmp1.next;
		}
		if(bridge.length==0)
			return "No bridge words from word1 to word2!";
		String result="The bridge words from word1 to word2 are: ";
		for(int i=0;i<bridge.length-1;i++) {
			result=result+G.get_word(bridge[i])+", ";
		}
		result = result+"and "+G.get_word(bridge[bridge.length-1])+".";
		return result;
	}

}

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

class Graph {
	private String vector[] = {};
	private int v_number, n_number;
	private G_List lists[] = {};
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

	public String cons_degug() {
		for(String v:vector) {
			System.out.print(v + " ");
		}
		System.out.println();
		for(G_List list:lists) {
			G_List temp=list;
			while(temp!=null) {
				System.out.print(temp.word_place+" "+temp.cost+"/");
				temp=temp.next;
			}
			System.out.println();
		}
		return "";
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
		if(letters.containsKey(word))
			return letters.get(word);
		else
			return -1;
	}
	public String get_word(int place) {
		return vector[place];
	}
}
