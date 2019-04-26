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
	 * Inserts into the BTree
	 */
	public void insert(int degree, long key) {
		
		BTreeNode duplicate = search(root, key); //check if the node is already inserted
		
		if (duplicate != null) { //duplicate method here?
			
		}
		
		BTreeNode r = root;
		
		if (r.numKeys == (2*t-1)) {
			BTreeNode s = new BTreeNode(t, getFileLength()); //newNode
			diskWrite(s);
			root.filePosition = getFileLength();
			root = s;
			s.isLeaf = false;
			s.numKeys = 0;
			s.children[0] = r.filePosition;
			s.filePosition = 0;
			splitChild(s, 0, r);
			insertNonFull(s, key);
		} else {
			insertNonFull(r, key);
		}	
	}
	
	/**
	 * Inserts a node into a non full node
	 */
	public void insertNonFull(BTreeNode x, long key) {
		
		int i = x.numKeys - 1;
		
		if (x.isLeaf == true) {
			
			while (i >= 1 && key < x.keys[i].getDna()) {
				x.keys[i+1] = x.keys[i];
				i--;
			}
			x.keys[i+1] = new TreeObject(key);
			x.numKeys++;
			diskWrite(x);
			
		} else {
			
			while (i >= 1 && key < x.keys[i].getDna()) {
				i--;
			}
			
			i++;
			BTreeNode c;
			if (x.children[i] != -1) {
				c = diskRead(x.children[i]);
				
				if (c.numKeys == (2*t-1)) {
					splitChild(x, i, c);
					if(key > x.keys[i].getDna()) {
						i++;
					}
				}
				insertNonFull(diskRead(x.children[i]), key);
			} 
		}	
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
	 * Split child when node is full
	 */
	public void splitChild(BTreeNode x, int i, BTreeNode y) {
		// x is the parent to y
		// y is the node being split
		// z is the node where half of y goes to
		BTreeNode z = new BTreeNode(t, getFileLength());
		z.isLeaf = y.isLeaf;
		z.numKeys = t-1;
		diskWrite(z);
		
		for(int j = 1; j < t-1; j++) {
			z.keys[j] = new TreeObject(y.keys[j+t].getDna(), y.keys[j+t].getFrequency());
			y.keys[j+t] = new TreeObject();
		}
		
		if(!y.isLeaf) {
			for(int j = 1; j < t; j++) {
				z.children[j] = y.children[j+t];
				y.children[j+t] = -1L;
			}
		}
		
		y.numKeys = t-1;
		
		for(int j = x.numKeys; j > i+1; j--) {
			x.children[j+1] = x.children[j];
			x.children[j] = -1L;
		}
		
		x.children[i+1] = z.filePosition;
		
		for(int j = x.numKeys; j > z.numKeys; j--) {
			x.keys[j+1] = new TreeObject(x.keys[j].getDna(), x.keys[j].getFrequency());
		}
		
		x.keys[i] = new TreeObject(y.keys[t-1].getDna(), y.keys[t-1].getFrequency());
		y.keys[t-1] = new TreeObject();
		x.numKeys = x.numKeys + 1;
		
		diskWrite(x);
		diskWrite(y);
		diskWrite(z);
	}
	
	/**
	 * Split the root when node is full
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
	 * Gets the length of a file
	 * @return returns the length the file
	 */
	private long getFileLength() {
		long fileLength = -1L;
		
		try {
			bTreeRAF = new RandomAccessFile(BTreeFile, "r"); //gets the file
			fileLength = bTreeRAF.length(); 
			bTreeRAF.close();
			
		} catch (IOException e) {
			System.err.println("Error: Cannot Access File");
			e.printStackTrace();
		}
		
		return fileLength;
	}
	
	/**
	 * print results for debugging
	 */
	public void print() {
		
	}
}
