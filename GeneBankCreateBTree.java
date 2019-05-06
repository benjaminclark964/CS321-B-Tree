import java.io.File;
import java.util.Iterator; 
import java.util.LinkedList; 
import java.util.Scanner;

/**
 * Driver Class 
 * 
 * Accepts arguments to construct a BTree
 * 
 * @author Juan Becerra, Kass Adams, and Benjamin Clark
 * 
 */
public class GeneBankCreateBTree {
	
	private static int sequenceLength;//K
	private static BTree bTree;
	private static Cache<BTreeNode> cache;//TODO: Maybe should be Cache<TreeObject>?
	
	/**
	 * Main Method
	 * 
	 * Expected arguments: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length>
	 * [<cache size>] [<debug level>]
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//	Parse argument 
			if(args.length >= 4 && args.length <= 6) {
				//	Assign values to variables
				int usingCache = Integer.valueOf(args[0]);
				int degree = Integer.valueOf(args[1]);
				File gbkFile = new File(args[2]);
				sequenceLength = Integer.valueOf(args[3]);
				int cacheSize;
				int debugLevel;
				
				//	Determine if cacheSize and debugLevel will be used
				if(args.length >= 5) {
					cacheSize = Integer.valueOf(args[4]);
					if(args.length == 6) {
						debugLevel = Integer.valueOf(args[5]);
					}else {
						debugLevel = 0;
					}
				} else { //if args.length was 4
					cacheSize = 0;
					debugLevel = 0;
				}
				
				/* Ensure that arguments are valid */
				
				//	usingCache must be 0 or 1
				if(!(usingCache == 0 || usingCache == 1)) {
					System.err.println("You must specify whether or not you are using a cache (0 or 1). You entered " + usingCache + ".\n");
					printUsageAndExit();
				}
				
				//	degree must be either 0 (becomes 4096 bytes) or some positive integer
				if(degree == 0) {
					degree = 5;//TODO: need to make actual calculation for the best degree for our block size 4096
				}
				if(!(degree > 1)) {
					System.err.println("Your degree must be greater or equal to 2. You entered " + degree + ".\n");
					printUsageAndExit();
				}
				
				//	gbkFile must exist and must be a file
				if( !(gbkFile.exists()) || !(gbkFile.isFile()) ) {
					System.err.println("The file (" + gbkFile.toString() + ") does not exist or could not be found.\n");
					printUsageAndExit();
				}
				
				//	sequenceLength must be between 1 and 31 (inclusive)
				if(!(sequenceLength >= 1 && sequenceLength <= 31)) {
					System.err.println("Your sequence length must be between 1 and 31 (inclusive). You entered " + sequenceLength + ".\n");
					printUsageAndExit();
				}
				
				//	Cache size must be greater than 0, but can be anything if usingCache is 0 
				if(usingCache == 1 && cacheSize <= 0) {
					System.err.println("Your cache size must be greater than 0. You entered " + cacheSize + ".\n");
					printUsageAndExit();
				}
				
				//	Debug level must either be 0 or 1
				if(!(debugLevel == 0 || debugLevel == 1)) {
					System.err.println("Your debug level must either be 0 or 1. You entered " + debugLevel + ".\n");
					printUsageAndExit();
				}
				
				//	Create a BTree
				bTree = new BTree(degree, sequenceLength, gbkFile.getName());
				
				//	Scan through the file, finds sequences, and creates nodes from file
				scanFile(new Scanner(gbkFile));
				
				//	Print that boi
				bTree.print(bTree.root, debugLevel);
				
			} else {
				//	Handle invalid number of arguments
				System.err.println("Invalid number of arguments (" + args.length + ")\n");
				printUsageAndExit();
			}
			
			
		} catch (Exception e) {
			//	Handle general parsing exceptions
			System.err.println("An error has occurred while parsing your arguments.");
			e.printStackTrace();
			System.err.println();
			printUsageAndExit();
		}
	}
	
	/**
	 * Prints usage and exits program
	 * 
	 * Used when an error is encountered during argument parsing
	 */
	private static void printUsageAndExit() {
		System.err.println("Usage: \n" +
				"java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> " + 
				"[<cache size>] [<debug level>]\n");
		System.exit(1);
	}
	
	/**
	 * Scans through an entire file, creating blocks of "non-garbage"
	 * DNA sequences that are then encoded into a binary long.
	 *  
	 * @param Scanner s
	 * @returns string of valid characters
	 */
	private static void scanFile(Scanner s) {
		//	Scans through entire file
		while(s.hasNextLine()) {
			//	Detect the start of a block
			if(s.nextLine().startsWith("ORIGIN")) {
				StringBuilder sequenceBlock = new StringBuilder();
				//	Iterate through the block
				while(s.hasNextLine()) {
					String temp = s.nextLine();	//	Line to be parsed
					if(!temp.startsWith("//")) {
						Scanner parser = new Scanner(temp);
						while(parser.hasNext()) {
							//	Append the "good" string to our working "good" block
							sequenceBlock.append(parser.next()
									.replace("\\s", "")	//	Removes whitespace
									.replaceAll("[0-9]", ""));	//	Removes all integers (regex)
						}
						parser.close();
					} else {
						slidyBoi(sequenceBlock.toString());
						break;
					}
				}
			}
		}
		s.close();
	}
	
	/**
	 * Takes a parsed String "block", creates Strings of size 'k',
	 * and passes those Strings into encode sequence
	 * 
	 * Once encoded, longs are passed to BTree in order to be inserted
	 * (May not insert if long is duplicate. In this case, frequency is incremented)
	 * 
	 * @param sequence
	 * @return
	 */
	private static void slidyBoi(String sequence) {
		int blockLength = sequence.length();
		LinkedList<Character> window = new LinkedList<Character>();
		Character n = 'n';
		
		for(int i = 0; i < blockLength; i++) {
			//	Create our initial working sequence
			if(i < sequenceLength) {
				window.add(sequence.charAt(i));
			} else {
				if(!window.contains(n)) {
					//	Encode and insert long into the BTree
					bTree.insert(encodeSequence(window));
				}
				
				//	Slide the window
				window.removeFirst();
				window.addLast(sequence.charAt(i));
			}
		}
		
		//	Google workaround
		if(!window.contains(n)) {			
			bTree.insert(encodeSequence(window));
		}
	}
	
	/**
	 * Takes a string and encodes it into a 32 bit long sequence
	 * 
	 * The key is as follows:
	 * 
	 * A = 00
	 * T = 11
	 * C = 01
	 * G = 10
	 * 
	 * Unused beats will be leading 0's
	 * 
	 * @param sequence
	 * @return
	 */
	public static long encodeSequence(LinkedList<Character> window) {
		
		Iterator<Character> it = window.iterator();
		StringBuilder sequence = new StringBuilder();
		while(it.hasNext()) {
			sequence.append(it.next());
		}
		char[] array = sequence.toString().toCharArray();
		StringBuilder str = new StringBuilder();
		for(int i=0; i<array.length; i++) {
			char atcg = Character.toUpperCase(array[i]);
			switch(atcg) {
			case 'A':
				str.append("00");
				break;
			case 'T':
				str.append("11");
				break;
			case 'C':
				str.append("01");
				break;
			case 'G':
				str.append("10");
				break;
			}
		}
			
		return Long.parseLong(str.toString(), 2);
	}
	
	/**
	 * Takes a string and encodes it into a 32 bit long sequence
	 * 
	 * The key is as follows:
	 * 
	 * A = 00
	 * T = 11
	 * C = 01
	 * G = 10
	 * 
	 * Unused beats will be leading 0's
	 * 
	 * @param sequence
	 * @return
	 */
	public static long encodeSequence(String s) {
		
		char[] array = s.toCharArray();
		StringBuilder str = new StringBuilder();
		for(int i=0; i<array.length; i++) {
			char atcg = Character.toUpperCase(array[i]);
			switch(atcg) {
			case 'A':
				str.append("00");
				break;
			case 'T':
				str.append("11");
				break;
			case 'C':
				str.append("01");
				break;
			case 'G':
				str.append("10");
				break;
			}
		}
			
		return Long.parseLong(str.toString(), 2);
	}
	
	/**
	 * 
	 */
	public static String decodeSequence(long dna) {
		String temp = Long.toString(dna,2);// the long converted to string w/o leading 0's
		StringBuilder dnaString = new StringBuilder();// will hold the long converted to a string w/ leading 0's
		int k2 = sequenceLength*2;// number of total bits expected
		int zerosNeeded = k2-temp.length();// number of leading 0's missing
		
		for(int i=0; i<zerosNeeded; i++) {
			dnaString.append("0");// add leading 0's
		}
		
		dnaString.append(temp);// add long w/o leading 0's, dnaString now completed
		
		StringBuilder retVal = new StringBuilder();// will hold final resulting chars a, t, c, g
		int i=0;
		while(i<k2) { 
			
			String letter = ""+dnaString.charAt(i)+dnaString.charAt(i+1); // two bits per char
			switch(letter) {
			case "00":
				retVal.append("a");
				break;
				
			case "01":
				retVal.append("c");
				break;
				
			case "10":
				retVal.append("g");
				break;
				
			case "11":
				retVal.append("t");
				break;
			}
			
			i+=2; // skip over 2 bits just utilized
		}
		
		return retVal.toString();
		
	}
	
}
