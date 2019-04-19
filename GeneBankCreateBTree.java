import java.io.File;
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
			if(args.length == 4 || args.length == 6) {
				//	Assign values to variables
				int usingCache = Integer.valueOf(args[0]);
				int degree = Integer.valueOf(args[1]);
				File gbkFile = new File(args[2]);
				int sequenceLength = Integer.valueOf(args[3]);
				int cacheSize;
				int debugLevel;
				
				//	Determine if cacheSize and debugLevel will be used
				if(args.length == 6) {
					cacheSize = Integer.valueOf(args[4]);
					debugLevel = Integer.valueOf(args[5]);
				} else {
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
					degree = 4096;
				}
				if(!(degree > 0)) {
					System.err.println("Your degree must be greater or equal to 0. You entered " + degree + ".\n");
					printUsageAndExit();
				}
				
				//	gbkFile must exist and must be a file
				if(!(gbkFile.exists()) || !(gbkFile.isFile())) {
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
				
				//	Scan through the file, look for Origin
				scanFile(new Scanner(gbkFile));
				
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
						Scanner parser = new Scanner(temp); // TODO USE DELIMITER .useDelimiter("")
						while(parser.hasNext()) {
							//	Append the "good" string to our working "good" block
							sequenceBlock.append(parser.next()
									.replace("\\s", "")	//	Removes whitespace
									.replaceAll("[0-9]", ""));	//	Removes all integers (regex)
						}
						parser.close();
					} else {
						//	TODO Testing for valid sequence - delete when done
						System.out.println(sequenceBlock.toString());
						
						long encodedSequence = encodeSequence(sequenceBlock.toString());
						break;
					}
				}
			}
		}
		s.close();
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
	private static long encodeSequence(String sequence) {
		//	Sequence to return
		long encodedSequence = 0;
		
		//	TODO create algorithm to encode string sequence
		
		
		return encodedSequence;
	}
}
