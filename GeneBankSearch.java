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
	
	private static int ERROR = 1, metaSeqLength;
	public static String bTreeFileName, queryFileName, metadataFileName;
	public static int debugLevel;
	
	public static void main(String args[]) {
		
		parseArgs(args);
		
		try {
			
			BTree bt = new BTree(new File(bTreeFileName), new File(metadataFileName));
			GeneBankCreateBTree create = new GeneBankCreateBTree();
			
			System.out.println("BTreeFileName: " + bTreeFileName);
			System.out.println("MetaDateFileName: " + metadataFileName);
			System.out.println("QueryFileName: " + queryFileName);
			
			Scanner queryScanner = new Scanner(new File(queryFileName));
			String curLine = "";
			
			do {
				curLine = queryScanner.nextLine();
				//long k = create.encodeSequence(curLine);
				//Need other things to be finished first
				
				
			}while (queryScanner.hasNextLine());
			
		} catch(FileNotFoundException e) {
			
		} catch(IOException e) {
			
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
		metadataFileName = bTreeFileName.replace("data", "metadata");
		metaSeqLength = Integer.parseInt(metadataFileName.split("//")[4]);	
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
