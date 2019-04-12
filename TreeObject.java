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
		setDna(dnaInput);
		frequency = 1;
	}

	/**
	 * Getter for DnaInput
	 * @return
	 */
	public long getDna() {
		return dna;
	}

	/**
	 * Setter for dnaInput
	 * @param dnaInput
	 */
	public void setDna(long dnaInput) {
		this.dna = dnaInput;
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
