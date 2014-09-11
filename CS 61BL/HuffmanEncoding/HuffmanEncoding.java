import java.util.*;
import java.io.*;

public class HuffmanEncoding{
	public static void main(String[] args) throws FileNotFoundException {
		StringTokenizer arguments = new StringTokenizer(args[0]);
		String command = ""; String target = ""; String destination = "";
		String frequency ="";
		if(args.length<3) {
			System.out.println("Please enter a command, target, and destination file.");
			return;
		}
		if (args.length== 3){
		command = args[0];
		target = args[1];
		destination = args[2];
		}else if(args.length==4){
			command = args[0];
			target = args[1];
			destination = args[2];
			frequency = args[3];
		}
		if(command.equals("decode")) {
			decode(target, destination);
		}
		else if(command.equals("encode")) {
			encode(target, destination);
		}
		else if (command.equals("encode2")){
			encode2(target, destination, frequency);
		}

		else {
			System.out.println("Must ask to encode or decode file.");
		}
	}
  
  	//Decoding part (uses HuffmanTree, HuffmanNode, HuffmanLeaf)
  	public static void decode(String fname1, String fname2) {
		FileCharIterator iter = new FileCharIterator(fname1);
		HuffmanTree decodingTree = new HuffmanNode(null, null);
		
		//check for existing file
		byte[] toWrite = new byte[0];
		try {
			FileOutputStream out = new FileOutputStream(fname2);
			out.write(toWrite);
			out.close();
		}
		catch(IOException e) {
			System.out.println("error creating new file");
		}
		
		//reconstruct tree
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fname1));
			while(true) {
				String next = reader.readLine();
				if(next.equals("")){
					break;
				}
				String ascii = ""; String encoding = "";
				if(next.substring(0,3).equals("EOF")) {
					ascii = next.substring(0,3);
					encoding = next.substring(4);
				}
				else if(next.charAt(8) != ',') {
					System.out.println("You were wrong. Go do it the hard way.");
					System.exit(1);
				}
				else {
					ascii = next.substring(0,8);
					encoding = next.substring(9);
				}
				//System.out.println("Length:" + encoding.length());
				HuffmanTree temp = decodingTree;
				for(int i=0; i<encoding.length()-1; i++) {
					if(temp instanceof HuffmanLeaf) {
						System.out.println("Unexpected behavior.");
					}
					else {
						if(encoding.charAt(i)=='0') {
							if(((HuffmanNode)temp).myLeft == null) {
								((HuffmanNode)temp).myLeft = new HuffmanNode(null, null);
							}
							temp = ((HuffmanNode)temp).myLeft;
						}
						else {
							if(((HuffmanNode)temp).myRight == null) {
								((HuffmanNode)temp).myRight = new HuffmanNode(null, null);
							}
							temp = ((HuffmanNode)temp).myRight;
						}
					}
				}
				if(encoding.charAt(encoding.length()-1)=='0') {
					((HuffmanNode)temp).myLeft = new HuffmanLeaf(0, ascii);
					temp = decodingTree;
				}
				else {
					((HuffmanNode)temp).myRight = new HuffmanLeaf(0, ascii);
					temp = decodingTree;
				}
			}
			reader.close();
		}
		catch(IOException e) {
			System.out.println("reader died");
			System.exit(1);
		}
		
		System.out.println("Done rebuilding tree.");
		
		System.out.println("Printed tree:");
		decodingTree.print();
		
		//advance iter through codemap
		while(true) {
			String k = iter.next();
			if(k.equals("00001010")) {
				String k2 = iter.next();
				if(k2.equals(k)) {
					break;
				}
			}
			System.out.print("ignoring: "+k);
		}
		
		System.out.println("Done iterating through codemap.");
		
		//use tree to decode text
		HuffmanTree temp = decodingTree;
		StringBuilder strbuild = new StringBuilder();
		outerloop:
		while(iter.hasNext()) {
			String next = iter.next();
			for(int i=0; i<next.length(); i++) {
				if(temp instanceof HuffmanLeaf) {
					if(((HuffmanLeaf)temp).myValue.equals("EOF")) {
						break outerloop;
					}
					//FileOutputHelper.writeBinStrToFile(((HuffmanLeaf)temp).myValue, fname2);
                  	strbuild.append(((HuffmanLeaf)temp).myValue);
					temp = decodingTree;
					i--;
				}
				else if(next.charAt(i)=='0') {
					temp = ((HuffmanNode)temp).myLeft;
				}
				else if(next.charAt(i)=='1'){
					temp = ((HuffmanNode)temp).myRight;
				}
				else {
					System.out.println("Something's wrong...");
				}
			}
		}
      	FileOutputHelper.writeBinStrToFile(strbuild.toString(), fname2);
	}
  	//end decode

  
    //Encoding part
	public static PriorityQueue<Node> pq;
	public static HashMap<String, String> encodeTable;
	public static HashMap<String, Integer> freqTable;
	public static FileCharIterator charIter;
	public static FileFreqWordsIterator wordIter;

	
	
	
	public static  HashMap<String, Integer> buildFreqTable(String fname1){
		//Create frequency table
		charIter = new FileCharIterator(fname1);
		freqTable=new HashMap<String, Integer>();
		while(charIter.hasNext()) {
			String temp = charIter.next();
			if(freqTable.containsKey(temp)) {
				freqTable.put(temp, freqTable.get(temp)+1);
			}
			else {
				freqTable.put(temp, 1);
			}

		}
		freqTable.put("EOF", 1);
		return freqTable;
	}
	public static  HashMap<String, Integer> buildFreqTable2(String fname1, int n){
		//Create frequency table
		wordIter = new FileFreqWordsIterator(fname1, n);
		freqTable=new HashMap<String, Integer>();
		HashMap<String, Integer> copyfreq = new HashMap<String, Integer>();
		while (wordIter.hasNext()) {
			String temp = wordIter.next();
			if(copyfreq.containsKey(temp)) {
				copyfreq.put(temp, copyfreq.get(temp)+1);
			}
			else {
				copyfreq.put(temp, 1);
			} 

		}
		for (String key: copyfreq.keySet()) {
			System.out.println(key);
			if (key.length() >=2 ) {
				freqTable.put(key, copyfreq.get(key));
			}
		}
		freqTable.put("EOF", 1);
		return freqTable;
	}

	public static Node huffmanTree(HashMap<String, Integer> fTable){
		//Create trees by adding all the nodes to a priority queue
		//Build the tree based on the frequency table

		pq=new PriorityQueue<Node>(100, new FrequencyComparator());
		int n=0;
		for(String s:fTable.keySet()){
			pq.add(new Node(s, fTable.get(s)));
			n++;
		}
		Node l, r;
		for(int i=1; i<=n-1;i++){
			Node nd=new Node();
			l=pq.poll();
			r=pq.poll();
			nd.left=l;
			nd.right=r;
			pq.add(nd);

		}
		return pq.poll();
	}
	

	public static void buildEncodeTable(Node root){
		//Build the code table
		encodeTable=new HashMap<String,String>();
		traverse(root, new String());
	}

	public static void traverse(Node n, String s) {
		//Traverse from root to leaves
		if(n==null){
			return;
		}
		traverse(n.left,s+"0");
		traverse(n.right,s+"1");
      		if (n.isLeaf()){
			encodeTable.put(n.ch, s);

		}
	} 

	
	public static void encode(String fname1, String fname2) throws FileNotFoundException{
		//Construct frequency table
		freqTable=buildFreqTable(fname1);
		//Construct a huffman tree
		Node root=huffmanTree(freqTable);
		//Construct the table using tree
		buildEncodeTable(root);
		//Write codemap header
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fname2));
			for(String k : encodeTable.keySet()) {
				writer.write(k + "," + encodeTable.get(k));
				writer.newLine();
			}
			writer.newLine();
			writer.close();
		}
		catch(IOException e) {
			System.out.println("writer died.");
		}
		//Write encoded file to destination
		FileCharIterator it = new FileCharIterator(fname1);
		StringBuilder encodeStr = new StringBuilder();
		encodeStr.append("");

		while(it.hasNext()) {
			String temp = it.next();
			encodeStr.append(encodeTable.get(temp));

		}
		encodeStr.append(encodeTable.get("EOF"));
		while(encodeStr.length()%8 != 0) {
			encodeStr.append("0");
		}
		FileOutputHelper.writeBinStrToFile(encodeStr.toString(), fname2);
	}
	
	public static void encode2(String fname1, String fname2, String frequencyLimit) throws FileNotFoundException{
		int limit = Integer.parseInt(frequencyLimit);
		//Construct frequency table
		freqTable=buildFreqTable2(fname1, limit);
		//Construct a huffman tree
		Node root=huffmanTree(freqTable);
		//Construct the table using tree
		buildEncodeTable(root);

		
		//Write codemap header
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fname2));
			for(String k : encodeTable.keySet()) {
				writer.write(k + "," + encodeTable.get(k));
				writer.newLine();
			}
			writer.newLine();
			writer.close();
		}
		catch(IOException e) {
			System.out.println("writer died.");
		}
		//Write encoded file to destination
		FileFreqWordsIterator it = new FileFreqWordsIterator(fname1, limit);
		StringBuilder encodeStr = new StringBuilder();
		encodeStr.append("");
		while(it.hasNext()) {
			String temp = it.next();
			System.out.println(temp);
			encodeStr.append(encodeTable.get(temp));
		}
		encodeStr.append(encodeTable.get("EOF"));
		while(encodeStr.length()%8 != 0) {
			encodeStr.append("0");
		}
		FileOutputHelper.writeBinStrToFile(encodeStr.toString(), fname2);
	}

		
}





class Node{
	public String ch;
	public int freq;
	public String value;
	public Node left, right;
	public Node(String s, int f){
		ch=s;
		freq=f;
	}
	public Node() {

	}
	public Node(String s1, String s2) {
		ch = s1;
		value = s2;
	}
	public String toString() {
		return ch+""+freq;
	}
	 boolean isLeaf() {
          assert (left == null && right == null) || (left != null && right != null);
          return (left == null && right == null);
      }
}
class FrequencyComparator implements Comparator<Node> {
	public int compare(Node n1,Node n2) {
		int fa=n1.freq;
		int fb=n2.freq;
		return fa-fb;
	}
}

abstract class HuffmanTree implements Comparable <HuffmanTree> {
	public int frequency;
	public HuffmanTree(int freq) {
		frequency = freq;
		
	}
	
	public void setFrequency(int n) {
		frequency = n;
	}
	
	public int compareTo(HuffmanTree t) {
      if (frequency < t.frequency) {
        return -1;
      }
      if (frequency == t.frequency) {
        return 0;
      }
      return 1;
	}
	
	public abstract void print();
}

class HuffmanNode extends HuffmanTree {
	public HuffmanTree myLeft;
	public HuffmanTree myRight;
	
	public HuffmanNode(HuffmanTree left, HuffmanTree right) {
		super(0);
		if(left != null) {
			setFrequency(frequency + left.frequency);
		}
		if(right != null) {
			setFrequency(frequency + right.frequency);
		}
		myLeft = left;
		myRight = right;
	}
	
	public void print() {
		System.out.print("-1");
		myLeft.print();
		System.out.println("0");
		System.out.print("1");
		myRight.print();
	}
}

class HuffmanLeaf extends HuffmanTree {
	public String myValue;
	
	public HuffmanLeaf(int freq, String value){
		super(freq);
		myValue = value;
	}
	
	public void print() {
		System.out.println(myValue);
	}
}












