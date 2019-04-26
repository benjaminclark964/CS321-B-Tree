/**
 * Object stored within a BTreeNode
 * 
 * Consists of a long value (represents a substring) and
 * an integer value (represents the frequency)
 * 
 * @author Juan Becerra, Kass Adams, Benjamin Clark
 */
public class TreeObject {

	//	Variables
	private long dna;
	private int frequency;
	
	/**
	 * Constructor for TreeObject
	 */
	public TreeObject (long dnaInput) {
		dna = dnaInput;
		frequency = 1;
	}
	
	/**
	 * Used to construct a tree object
	 * @param dnaInput the key
	 * @param Frequency of the key
	 */
	public TreeObject(long dnaInput, int Freq) {
		this.dna = dnaInput;
		this.frequency = Freq;
	}
	
	public TreeObject() {
		this.dna = -1L;
		frequency = 0;
	}

	/**
	 * Getter for DnaInput
	 * @return
	 */
	public long getDna() {
		return dna;
	}

	/**
	 * Getter for frequency
	 * @return
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * Increments frequency by 1
	 * @param frequency
	 */
	public void incrementFrequency() {
		frequency++;
	}
}
