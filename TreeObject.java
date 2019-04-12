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
	private long dnaInput;
	private int frequency;
	
	/**
	 * Constructor for TreeObject
	 */
	public TreeObject (long dnaInput) {
		this.setDnaInput(dnaInput);
		this.setFrequency(1);
	}

	/**
	 * Getter for DnaInput
	 * @return
	 */
	public long getDnaInput() {
		return dnaInput;
	}

	/**
	 * Setter for dnaInput
	 * @param dnaInput
	 */
	public void setDnaInput(long dnaInput) {
		this.dnaInput = dnaInput;
	}

	/**
	 * Getter for frequency
	 * @return
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * Setter for frequency
	 * @param frequency
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	
}
