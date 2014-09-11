import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class FileFreqWordsIterator implements Iterator<String> {
	protected Scanner reader;
	private int limit;
    private String inputFileName;
    private ArrayList<String> returnValue = new ArrayList<String>();
    private int count;
    private int maxNumber;
    private String maxString = " ";
    private HashMap<String, Integer> wordList = new HashMap<String, Integer>();
    
    public FileFreqWordsIterator(String inputFileName, int limit) {
    	count = 0;
        try {
            reader = new Scanner(new File(inputFileName));
            while (reader.hasNext()) {
            	String temp=reader.next();
               if (!(wordList.containsKey(temp))){
            	   wordList.put(temp, 1);
               }else{
            	   wordList.put(temp, wordList.get(temp)+1);
               }
            }
            int i = 0;
            while (i < limit) {
               maxNumber = 0;
               for (String key: wordList.keySet()){
            	  if (wordList.get(key) > maxNumber) {
            		  maxNumber = wordList.get(key);
            		  maxString = key;
            	      }
                  } 
                  System.out.println("removing" + maxString);
                  wordList.remove(maxString);
                  i++;
            }
            //count space; 
//            BufferedReader r = new BufferedReader(new FileReader(inputFileName));
//            String line = r.readLine();
//            while (line!= null) {
//            	if (!wordList.containsKey(" ")) {
//            		if (countSpace(line) != 0){
//            	    wordList.put(" ", countSpace(line));
//            	    }
//            	}else{
//            		wordList.put(" ", wordList.get(" ") +countSpace(line));
//            	
//            } if (wordList.get("\n") == null) {
//            	wordList.put("\n", 1);
//            }else{
//            	wordList.put("\n", wordList.get("\n") +1);
//            }
//            
//            	line = r.readLine();
//            }
            Scanner reader2 = new Scanner(new File(inputFileName));
            while (reader2.hasNext()) {
            	String temp=reader2.next();
               if (!(wordList.containsKey(temp))){
            	   returnValue.add(temp);
    
               }else{
            	  System.out.println(temp + " is breaking down");
            	for (int k = 0; k < temp.length(); k++) {
                   returnValue.add(Character.toString(temp.charAt(k)));
            	}
  
            	} if (reader2.hasNext()) {
             	   returnValue.add(" ");
               }
            }
            
            
            this.inputFileName = inputFileName;
        } catch (IOException e) {
            System.err.printf("IOException while reading from file %s\n",
                    inputFileName);
            System.exit(1);
        }
    }
    
//    public static HashMap<String, Integer> breakdown(HashMap<String, Integer> m1) {
//    	HashMap<String, Integer> m2 = new HashMap<String, Integer>();
//       for (String key: m1.keySet()) {
//    		char[] wordReader = key.toCharArray();
//    		   for(int i = 0; i < wordReader.length; i ++) {
//    			String current = Character.toString(wordReader[i]);
//    			if (!(m2.containsKey(current))){
//             	   m2.put(current, 1);
//                }else if (m2.containsKey(current)) {
//                   System.out.println(m2.get(current));
//             	   m2.put(current, m2.get(current) +1);
//             	   
//                }
//    		}
//    	}m1 = m2;
//    return m1;
//    }
    


	@Override
	public boolean hasNext() {
		return count < returnValue.size();
	}

	@Override
	public String next() {
		// TODO Auto-generated method stub
       if (hasNext()){
        	String toRtn = "";
        	Byte b = null;
        	String item = returnValue.get(count);
        	System.out.println(item);
        	for (int i = 0; i < item.getBytes().length; i ++){
        		b = (byte) item.getBytes()[i];
                toRtn += String.format("%8s",Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        	
        	}
        	count++;
        	return toRtn;
	   }else{
		   throw new NoSuchElementException ("end of file");
	}
	}
	@Override
	public void remove() {
		throw new UnsupportedOperationException(
                "FileFreqWordsIterator does not delete from files.");
    }
	
	public static int countSpace(String str) {
		int spaceCount = 0;
		for (int i = 0; i < str.length(); i++) {
		if (str.charAt(i) == ' '){
		spaceCount++;
		}
		}return spaceCount;
		}
	
	
	public HashMap<String, Integer> getWordList(){
		return wordList;
	}
	
	public void setWordList(HashMap<String, Integer> list2) {
		this.wordList= list2;
	}
	
	
	
}
