/**
 * 
 * @author Benjamin Clark, Kass Adams, and Juan Becerra
 *
 */
public class BTree {
	
	private int t;//degree of tree, so every BTreeNode has N number of TreeObjects such that: (t-1)<= N <=(2t-1)
	private int sequenceLength;//this is the k parameter of Project Spec.
	
	public void BTree(int degree, int k) {
		this.t = degree;
		this.sequenceLength = k;
	}
	
	/**
	 * Insert a node into the BTree
	 */
	public void Insert() {
		
	}
	
	/**
	 * Search for a node
	 */
	public void Search() {
		
	}
	
	/**
	 * Split child when degree is passed
	 */
	public void splitChild() {
		
	}
	
	/**
	 * Split the root when degree is passed
	 */
	public void splitRoot() {
		
	}
	
	/**
	 * write metadata into disk
	 */
	public void diskWrite() {
		
	}
	
	/**
	 * print results for debugging
	 */
	public void print() {
		
	}
}
