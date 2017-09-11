package seProject1_1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

public class Project1_1 {
	public static void main(String[] args) {
		
	}
	public static Graph createDirectedGraph(String filename) {
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String line=null;
			while((line = br.readLine())!=null) {
				String[] words = line.split("[^a-z^A-Z]{1,}");
				for(String word:words) {
					System.out.println(word);
					word=word.toLowerCase();
				}
			}
		}catch(FileNotFoundException e1){
			e1.printStackTrace();
		}catch(IOException e2) {
			e2.printStackTrace();
		}
		
		return null;
	}
}
class G_List{
	public int word_place;
	public G_List next;
	public int cost;
}
 class Graph{
	private String vector[];
	private int v_number,n_number;
	private G_List lists[];
	public Graph(String words[]) {
		vector=null;
		lists =null;
		int pri=0;
		n_number=words.length;
		for(String word:words) {
			if(!Arrays.asList(vector).contains(word)) {
				vector=Arrays.copyOf(vector, vector.length+1);
				vector[vector.length-1]=word;
				lists=Arrays.copyOf(lists,lists.length+1);
				
			}
			
		}
		v_number=vector.length;
	}
}
