/**
 * Creates data within each node in on the BTree
 * 
 * @author Kass Adams, Juan Becerra, and Benjamin Clark
 *
 */
public class BTreeNode {
	
	public TreeObject[] keys;
	public long children[];
	public int numKeys = 0;
	public boolen isLeaf;
	public long filePosition; //Postion of node in file, may change later
	
	/**
	 * Creates functionality of BTreeNode
	 * @param t is the degree
	 * @param filePosition postion of node within the file
	 */
	public BTreeNode(int t, long filePosition) {
		
		this.keys = new TreeObject[(2*t-1)];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = new TreeObject(-1L); //start at the root
			numKeys++;
		}
		
		this.children = new long[(2*t)];
		for (int i = 0; i < children.length; i++) {
			children[i] = -1L; //not sure what to put here for what the child is equal to
		}
		
		this.isLeaf = true;
		numKeys = 0;
		this.filePosition = filePosition;
	}
	
	/**
	 * for debugging in future
	 */
	public void printNode() {
		
		System.out.println("File Position: " + filePosition);
		System.out.println("Is Leaf: " + isLeaf);
		System.out.println("Number of Keys: " + numKeys);
		System.out.println("Keys: ");
		for (int i = 0; i < keys.length; ++i) {
			System.out.print(i);
		}
	}
}