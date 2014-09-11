import java.io.File;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import junit.framework.TestCase;


public class FileFreqWordsIteratorTest extends TestCase {
	public void testBreakDown() {
		    //test constructor

			FileFreqWordsIterator iter1 = new FileFreqWordsIterator("kitten.txt", 1);
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "00100000");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "00100000");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "00100000");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "00100000");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "00100000");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "01001011");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "01101001");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "01101101");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "00100000");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "00100000");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "00100000");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "00100000");
			assertTrue(iter1.hasNext());
			assertEquals(iter1.next(), "011010110110100101110100011101000110010101101110");
			assertFalse(iter1.hasNext());
		    
			FileFreqWordsIterator iter2 = new FileFreqWordsIterator("kitten.txt", 2);
			assertEquals(iter2.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "00100000");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "00100000");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "00100000");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "00100000");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "00100000");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "010010110110100101101101");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "00100000");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "00100000");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "00100000");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "011010110110100101110100011101000110010101101110");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "00100000");
			assertTrue(iter2.hasNext());
			assertEquals(iter2.next(), "011010110110100101110100011101000110010101101110");
			assertFalse(iter2.hasNext());
			
			FileFreqWordsIterator iter3 = new FileFreqWordsIterator("times.txt", 4);
			assertTrue(iter3.hasNext());
			assertEquals(iter3.next(), "0110100101110100");//it
			assertEquals(iter3.next(), "00100000");
			assertEquals(iter3.next(), "011101110110000101110011");//was
			assertEquals(iter3.next(), "00100000");
			assertEquals(iter3.next(),"011101000110100001100101");//the
			assertEquals(iter3.next(), "00100000");
			assertEquals(iter3.next(),"01100010");//b
			assertEquals(iter3.next(),"01100101");//e
			assertEquals(iter3.next(),"01110011");//s
			assertEquals(iter3.next(),"01110100");//t
			assertEquals(iter3.next(), "00100000");
			assertEquals(iter3.next(),"0110111101100110");//of
			assertEquals(iter3.next(), "00100000");
			assertEquals(iter3.next(),"01110100");//t
			assertEquals(iter3.next(),"01101001");//i
			assertEquals(iter3.next(),"01101101");//m
			assertEquals(iter3.next(),"01100101");//e
			assertEquals(iter3.next(),"01110011");//s
			assertEquals(iter3.next(), "00101100");//,
			assertEquals(iter3.next(), "00100000");
			assertEquals(iter3.next(), "0110100101110100");//it
			assertEquals(iter3.next(), "00100000");
			assertTrue(iter3.hasNext());
			assertEquals(iter3.next(), "011101110110000101110011");//was
			assertEquals(iter3.next(), "00100000");
			assertEquals(iter3.next(),"011101000110100001100101");//the
			assertEquals(iter3.next(), "00100000");
			assertEquals(iter3.next(),"01110111");//w
			assertEquals(iter3.next(),"01101111");//o
			assertEquals(iter3.next(),"01110010");//r
			assertEquals(iter3.next(),"01110011");//s
			assertEquals(iter3.next(),"01110100");//t
			assertEquals(iter3.next(), "00100000");
			assertEquals(iter3.next(),"0110111101100110");//of
			assertEquals(iter3.next(), "00100000");
			assertEquals(iter3.next(),"01110100");//t
			assertEquals(iter3.next(),"01101001");//i
			assertEquals(iter3.next(),"01101101");//m
			assertEquals(iter3.next(),"01100101");//e
			assertEquals(iter3.next(),"01110011");//s
			assertFalse(iter3.hasNext());
			
			
			FileFreqWordsIterator iter4 = new FileFreqWordsIterator("SmallFile.txt", 1);
			assertTrue(iter4.hasNext());
			assertEquals(iter4.next(),"011000010110001001100001");
			assertFalse(iter4.hasNext());
			
			//test helper function
			/*HashMap<String, Integer> maptest = new HashMap<String, Integer>();
			maptest.put("kitten", 2);
			maptest.put("Kim", 1);
			HashMap<String, Integer> maptest2 = FileFreqWordsIterator.breakdown(maptest);
			assertFalse(maptest2.containsKey("Kim"));
			assertTrue(maptest2.containsKey("kitten"));
			assertTrue(maptest2.containsKey("i"));
			assertTrue(maptest2.containsKey("K"));	
			
			HashMap<String, Integer> maptest0 = new HashMap<String, Integer>();
			maptest0.put("it", 2);
			maptest0.put("was", 2);
			maptest0.put("the", 2);
			maptest0.put("of", 2);
			maptest0.put("best", 1);
			maptest0.put("time", 1);
			maptest0.put("worst", 1);
			maptest0.put(",", 1);
			HashMap<String, Integer> maptest3 = FileFreqWordsIterator.breakdown(maptest0);
			assertFalse(maptest3.containsKey("best"));
			assertFalse(maptest3.containsKey("time"));
			assertFalse(maptest3.containsKey("worst"));
			assertTrue(maptest3.containsKey(","));
			assertTrue(maptest3.containsKey("was"));
			assertTrue(maptest3.get("was").equals(2));
			assertTrue(maptest3.containsKey("t"));
			assertEquals(maptest3.get("e"), (Integer) 2);
			assertTrue(maptest3.containsKey("t"));
			assertEquals(maptest3.get("t"), (Integer) 3);
			assertTrue(maptest3.get("o").equals(1));
			
			HashMap<String, Integer> maptest4 = new HashMap<String, Integer>();
			maptest4.put("hello!", 1);
			maptest4.put("??!!", 1);
			HashMap<String, Integer> maptest5 = FileFreqWordsIterator.breakdown(maptest4);
			assertTrue(maptest5.containsKey("?"));
			assertTrue(maptest5.containsKey("!"));
			assertTrue(maptest5.get("!").equals(3));
			assertTrue(maptest5.get("?").equals(2));
			*/
	}
		
	public void testSpace() {
		// test file
		FileFreqWordsIterator iter = new FileFreqWordsIterator("kitten.txt", 2);
		int count = 0;
		while(iter.hasNext()) {
			if (iter.next().equals("00100000"));
					count++;
		}
		assertEquals(count, 19);
		
		FileFreqWordsIterator iter4 = new FileFreqWordsIterator("SmallFile.txt", 1);
		HashMap<String, Integer> map3 = new HashMap<String, Integer>();
		count = 0;
		while(iter4.hasNext()) {
			if (iter4.next().equals("00100000"));
					count++;
		}
		assertEquals(count, 1);
		
		HashMap<String, Integer> map4 = new HashMap<String, Integer>();
		FileFreqWordsIterator i3 = new FileFreqWordsIterator("times.txt", 5);
		count = 0;
		while(i3.hasNext()) {
			if (i3.next().equals("00100000"));
					count++;
		}
		assertEquals(count, 35);
		
		//method test cases
	    String str = new String("kitten kitten kitten kitten kitten kitten kitten kitten Kim kitten");
		int space = FileFreqWordsIterator.countSpace(str);
		assertEquals(space, 9);
		String str2 = new String("See you.");
		int space2 = FileFreqWordsIterator.countSpace(str2);
		assertEquals(space2, 1);
		String str3 = new String(" ");
		int space3 = FileFreqWordsIterator.countSpace(str3);
		assertEquals(space3, 1);
		String str4 = new String();
		int space4 = FileFreqWordsIterator.countSpace(str4);
		assertEquals(space4, 0);
	
		}
	
	    
	
}
