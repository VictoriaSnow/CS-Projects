import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import junit.framework.TestCase;


public class HuffmanEncodingTest extends TestCase {
	public void testEncode() throws FileNotFoundException{
		
		HuffmanEncoding.encode("SmallFile.txt", "encodedSmallFile.txt");
		HuffmanEncoding.decode("encodedSmallFile.txt", "decodedSmallFile.txt");
		String SmallFileDecoded=new Scanner(new File("decodedSmallFile.txt")).useDelimiter("\\A").next();		
		String SmallFile=new Scanner(new File("SmallFile.txt")).useDelimiter("\\A").next();
		assertTrue(SmallFileDecoded.equals(SmallFile));
		
		
		HuffmanEncoding.encode("Kaleidoscope.txt", "encodedKaleidoscope.txt");
		HuffmanEncoding.decode("encodedKaleidoscope.txt", "decodedKaleidoscope.txt");
		String KaleidoscopeDecoded=new Scanner(new File("decodedKaleidoscope.txt")).useDelimiter("\\A").next();
		String Kaleidoscope=new Scanner(new File("Kaleidoscope.txt")).useDelimiter("\\A").next();
		assertTrue(KaleidoscopeDecoded.equals(Kaleidoscope));
		
		
		HuffmanEncoding.encode("lastquestion.txt", "encodedlastquestion.txt");
		HuffmanEncoding.decode("encodedlastquestion.txt", "decodedlastquestion.txt");
		String lastquestionDecoded=new Scanner(new File("decodedlastquestion.txt")).useDelimiter("\\A").next();
		String lastquestion=new Scanner(new File("lastquestion.txt")).useDelimiter("\\A").next();
		assertTrue(lastquestionDecoded.equals(lastquestion));
		
		String Sherlock=new Scanner(new File("TheAdventuresOfSherlockHolmes.txt")).useDelimiter("\\A").next();
		HuffmanEncoding.encode("TheAdventuresOfSherlockHolmes.txt","encodedTheAdventuresOfSherlockHolmes.txt");
		HuffmanEncoding.decode("encodedTheAdventuresOfSherlockHolmes.txt", "decodedTheAdventuresOfSherlockHolmes.txt");
		String SherlockDecoded=new Scanner(new File("decodedTheAdventuresOfSherlockHolmes.txt")).useDelimiter("\\A").next();
		assertTrue(SherlockDecoded.equals(Sherlock));
		

	}

	public void testOtherMethods() throws FileNotFoundException{
		HashMap<String, Integer> freqTable1=new HashMap<String, Integer>();
		freqTable1=HuffmanEncoding.buildFreqTable("SmallFile.txt");
		FileCharIterator iter1 = new FileCharIterator("SmallFile.txt");
		for (String k: freqTable1.keySet()){
			System.out.println(k);
		}
		while (iter1.hasNext()){
			String temp=iter1.next();
			assertTrue(freqTable1.containsKey(temp));
		}
		assertEquals(freqTable1.size(),3);
		assertFalse(freqTable1.isEmpty());
		assertTrue(freqTable1.containsValue(2));
		
		
		HashMap<String, Integer> freqTable2=new HashMap<String, Integer>();
		freqTable2=HuffmanEncoding.buildFreqTable("kitten.txt");
		FileCharIterator iter2 = new FileCharIterator("kitten.txt");
		for (String k: freqTable2.keySet()){
			System.out.println(k);
		}
		while (iter2.hasNext()){
			String temp=iter2.next();
			assertTrue(freqTable2.containsKey(temp));
		}
		assertEquals(freqTable2.size(),9);
		assertFalse(freqTable2.isEmpty());
		assertTrue(freqTable2.containsValue(10));
		assertTrue(freqTable2.containsValue(9));
		
		HashMap<String, Integer> freqTable3=new HashMap<String, Integer>();
		freqTable3=HuffmanEncoding.buildFreqTable("abc.txt");
		FileCharIterator iter3 = new FileCharIterator("abc.txt");
		for (String k: freqTable3.keySet()){
			System.out.println(k);
		}
		while (iter3.hasNext()){
			String temp=iter3.next();
			assertTrue(freqTable3.containsKey(temp));
		}
		assertEquals(freqTable3.size(),27);
		assertFalse(freqTable3.isEmpty());
		assertFalse(freqTable3.containsValue(2));
		
		HashMap<String, Integer> freqTable4=new HashMap<String, Integer>();
		freqTable4=HuffmanEncoding.buildFreqTable("ks.txt");
		FileCharIterator iter4 = new FileCharIterator("ks.txt");
		for (String k: freqTable4.keySet()){
			System.out.println(k);
		}
		while (iter4.hasNext()){
			String temp=iter4.next();
			assertTrue(freqTable4.containsKey(temp));
		}
		assertEquals(freqTable4.size(),3);
		assertFalse(freqTable4.isEmpty());
		assertTrue(freqTable4.containsValue(30));
	}
	
}
