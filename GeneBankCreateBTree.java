import java.io.File;

/**
 * Driver Class
 * 
 * Accepts arguments to construct a BTree
 * 
 * @author Juan Becerra, Kass Adams, and Benjamin Clark
 *
 */
public class GeneBankCreateBTree {
	//	Variables
	private static int usingCache;
	private static int degree;
	private static File gbkFile;
	private static int sequenceLength;
	private static int cacheSize;
	private static int debugLevel;
	
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
				usingCache = Integer.valueOf(args[0]);
				degree = Integer.valueOf(args[1]);
				gbkFile = new File(args[2]);
				sequenceLength = Integer.valueOf(args[3]);
				
				//	Determine if cacheSize and debugLevel will be used
				if(args.length == 6) {
					cacheSize = Integer.valueOf(args[4]);
					debugLevel = Integer.valueOf(args[5]);
				} else {
					cacheSize = 0;
					debugLevel = 0;
				}
				
				//	Ensure that arguments are valid
				//	TODO
				
				//	Pass variables to the BTree constructor
				//	TODO
				
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
}
