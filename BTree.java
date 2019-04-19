import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author Benjamin Clark, Kass Adams, and Juan Becerra
 *
 */
public class BTree {
	
	private int t;//degree of tree, so every BTreeNode has N number of TreeObjects such that: (t-1)<= N <=(2t-1)
	private int sequenceLength;//this is the k parameter of Project Spec.
	BTreeNode root; //root of the BTree
	RandomAccessFile bTreeRAF; //Reads the file
	File BTreeFile;
	//Cache cache; 
	
	public BTree(int t, int k, String gbk) throws IOException {
		
		this.t = t; //degree
		this.sequenceLength = k;
		File metadata = new File(gbk + ".btree.metadata" + k + "." + t);
		
		bTreeRAF = new RandomAccessFile(metadata, "rw");
		bTreeRAF.writeInt(t); //write tree degree to metadata file
		bTreeRAF.writeInt(k); //write sequence length to metadata file
		bTreeRAF.close();
		
		root = new BTreeNode(t, 0);
		BTreeFile = new File(gbk + ".btree.data" + k + "." + t);
		diskWrite(root);
	}
	
	/**
	 * Insert a node into the BTree
	 */
	public void Insert() {
		
	}
	
	/**
	 * Search for a node
	 */
	public BTreeNode search(BTreeNode node, long key) {
		
		int i = 0;
		BTreeNode returnNode = null;
		
		while (i < node.numKeys && key > node.keys[i].getDna()) {	
			i++;	
		}
		
		if (i < node.numKeys && key == node.keys[i].getDna()) {	
			return node;	
		}
		
		if (node.isLeaf) {	
			return null;	
		}
		
		if (node.children[i] != -1) {	
			return diskRead(node.children[i]);	
		}
		return search (returnNode, key);
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
	 * @param node node to be written
	 */
	public void diskWrite(BTreeNode node) {
		
		try {
			
			bTreeRAF = new RandomAccessFile(BTreeFile, "rw");
			bTreeRAF.seek(node.filePosition);
			
			for (int i = 0; i < node.keys.length; i++) {
				bTreeRAF.writeLong(node.keys[i].getDna());
				bTreeRAF.writeInt(node.keys[i].getFrequency());
			}
			
			for (int i = 0; i < node.children.length; i++) {
				bTreeRAF.writeLong(node.children[i]);
			}
			
			bTreeRAF.writeInt(node.numKeys);
			bTreeRAF.writeBoolean(node.isLeaf);
			bTreeRAF.writeLong(node.filePosition);
			bTreeRAF.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * reads the contents of the disk
	 * @param filePos position of node within the file
	 * @return node
	 */
	public BTreeNode diskRead(long filePos) {		
		
		BTreeNode node = new BTreeNode(t,filePos);
		
		try {
			bTreeRAF = new RandomAccessFile(BTreeFile, "r");
			bTreeRAF.seek(filePos);
			
			for (int i = 0; i < node.keys.length; i++) {
				long g = node.keys[i].getDna(); 
				int e = node.keys[i].getFrequency();
				g = bTreeRAF.readLong();
				e = bTreeRAF.readInt();
			}
			
			for (int i = 0; i < node.children.length; i++) {
				node.children[i] = bTreeRAF.readLong();
			}
			
			node.numKeys = bTreeRAF.readInt();
			node.isLeaf = bTreeRAF.readBoolean();
			node.filePosition = bTreeRAF.readLong();
			bTreeRAF.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return node;
	}
	
	/**
	 * print results for debugging
	 */
	public void print() {
		
	}
}
