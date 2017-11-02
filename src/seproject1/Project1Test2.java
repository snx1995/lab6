package seproject1;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Project1Test2 {
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testQueryBridgeWords() {
		Graph G = Project1.createDirectedGraph("C:\\Users\\Snxt\\Desktop\\data2.txt");
		
//		Project1.queryBridgeWords(G,"new","this");
//		Project1.queryBridgeWords(G,"are","out");
//		Project1.queryBridgeWords(G,"hht","osdt");
//		Project1.queryBridgeWords(G,"To","to");
		assertEquals("No excel or please in the graph!", Project1.queryBridgeWords(G,"Excel","please"));
		assertEquals("No bridge words from to to seek!", Project1.queryBridgeWords(G,"To","seek"));
		assertEquals("The bridge word from strange to worlds is: new", Project1.queryBridgeWords(G,"Strange","worlds"));
		assertEquals("The bridge words from to to out are: get, and seek.", Project1.queryBridgeWords(G,"To","out"));
		//fail("Not yet implemented");
	}

}
