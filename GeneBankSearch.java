/**
 * write later
 * 
 * @author Juan Becerra, Kass Adams, and Benjamin Clark
 *
 */
public class GeneBankSearch {
	
	private static int ERROR = 1;
	public static String bTreeFileName, queryFileName;
	public static int debugLevel;
	
	public static void main(String args[]) {
		
	}
	
	/**
	 * Parses arguments from the user
	 */
	public static void parseArgs(String args[]) {
		
		if (args.length > 5 || args.length < 3 || args[4] != 0) {
			
			printUsage();
			
		} 
		
		if (args.length == 5) {
			
			debugLevel = Integer.parseInt(args[4]);
		}	
			
		if (Integer.valueOf(args[0]) == 1) {
			// implement cache
		}
		
		bTreeFileName = args[1];
		queryFileName = args[2];
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
