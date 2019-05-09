import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
/** 
 * Searches the BTree and prints each sequence of DNA along with its corresponding
 * frequency.
 * 
 * @author Juan Becerra, Kass Adams, Benjamin Clark
 *
 */
public class GeneBankSearch {
	
	private static int ERROR = 1;
	public static String bTreeFileName, queryFileName, dataFileName;
	public static int debugLevel;
	static long foundKey;
	
	public static void main(String args[]) {
		
		parseArgs(args);
		
		try {
			
			BTree bt = new BTree(new File(bTreeFileName), new File(dataFileName));
			@SuppressWarnings("unused")
			GeneBankCreateBTree create = new GeneBankCreateBTree();
			
			System.out.println("BTreeFileName: " + bTreeFileName);
			System.out.println("MetaDateFileName: " + dataFileName);
			System.out.println("QueryFileName: " + queryFileName);
			
			@SuppressWarnings("resource")
			Scanner queryScanner = new Scanner(new File(queryFileName));
			String curLine = "";
			
			while (queryScanner.hasNextLine()) {
				curLine = queryScanner.nextLine();
				int sequenceLength = curLine.length();
				long k = GeneBankCreateBTree.encodeSequence(curLine);
				BTreeNode node = bt.search(bt.root, k);
				
				if(node == null) {
					
				} else {
				
				for(int i = 0; i < node.keys.length; i++) {
					if(node.keys[i].dna == k) {
						System.out.println(GeneBankCreateBTree.decodeSequence(k, sequenceLength) + ": " + node.keys[i].frequency);
					}
				}
			}
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parses arguments from the user
	 */
	public static void parseArgs(String args[]) {
		
		if (args.length > 5 || args.length < 3 || Integer.parseInt(args[0]) != 0) {
			
			printUsage();
			
		} 
		
		if (args.length == 5) {
			
			debugLevel = Integer.parseInt(args[4]);
		}	
		
		bTreeFileName = args[1];
		queryFileName = args[2];
		dataFileName = bTreeFileName.replace("data", "metadata");
	}
	
	/**
	 * Prints to the user how to use the program
	 */
	public static void printUsage() {
		
		System.err.println("Usage: Java GeneBankSearch + "
				+ "<0/1(no/with Cache)> <btree file> <query file> "
				+ "[<cache size>] + [<debug level>]");
		
		System.exit(ERROR);
	}
}
