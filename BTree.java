
    
import java.io.File;
import java.io.FileWriter;
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
	
	public BTree(File BtreeFile, File metadata) throws IOException {	
		bTreeRAF = new RandomAccessFile(metadata, "r");
		this.t = bTreeRAF.readInt(); //read in degree in terms of t
		this.sequenceLength = bTreeRAF.readInt(); //sequence length (k) 
		bTreeRAF.close();
		
		this.BTreeFile = BtreeFile;
		bTreeRAF = new RandomAccessFile(BtreeFile, "r");
		root = diskRead(0);		
		bTreeRAF.close();
	}
	
	/**
	 * Inserts into the BTree
	 */
	public void insert(long key) {
		
		//check if key is already inserted
				BTreeNode duplicate = search(root, key);
				if(duplicate != null) {
					for(int i=0; i<duplicate.keys.length; i++) {
						if(duplicate.keys[i].getDna() == key) {
							duplicate.keys[i].incrementFrequency();;
							diskWrite(duplicate);
							return;
						}
					}
				}
				
				BTreeNode r = this.root;
				if(r.numKeys == 2*t-1) {
					BTreeNode newNode = new BTreeNode(t, getFileLength());	
					diskWrite(newNode);	
					this.root.filePosition = getFileLength();
					diskWrite(root);
					this.root = newNode;
					newNode.isLeaf = false;
					newNode.numKeys = 0;
					newNode.children[0] = r.filePosition;
					newNode.filePosition = 0;
					splitChild(newNode,0,r);
					insertNonFull(newNode,key);
				}else {
					insertNonFull(root,key);
				}
			}	
	
	/**
	 * Inserts a node into a non full node
	 */
	public void insertNonFull(BTreeNode x, long key) {
		
		int i = x.numKeys - 1;
		if(x.isLeaf) {
			while( i >= 0 && key < x.keys[i].getDna() ) {
				x.keys[i+1] = new TreeObject(x.keys[i].getDna(), x.keys[i].getFrequency());
				i--;			
			}
			x.keys[i+1] = new TreeObject(key);
			x.numKeys++;
			diskWrite(x);	
		}else {
			while( i >= 0 && key < x.keys[i].getDna()) {
				i--;
			}
			i++;
			BTreeNode c;
			if(x.children[i] != -1) {
				c = diskRead(x.children[i]);
				if( c.numKeys == 2*t-1 ) {
					splitChild(x, i, c);
					if( key > x.keys[i].getDna()){
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
			returnNode = diskRead(node.children[i]);	
		}
		return search (returnNode, key);
	}
	
	/**
	 * Split child when node is full
	 */
	public void splitChild(BTreeNode x, int i, BTreeNode y) {
		//x is the parent to y
				//y is the node being split 
				//z is the new node which ~half of y's keys/children will go to
				BTreeNode z = new BTreeNode(t, getFileLength());
				z.isLeaf = y.isLeaf;
				z.numKeys = t-1;
				diskWrite(z);
				
				for(int j=0; j<t-1; j++) {
					z.keys[j] = new TreeObject(y.keys[j+t].getDna(), y.keys[j+t].getFrequency());
					y.keys[j+t] = new TreeObject();
				}
				if(!y.isLeaf) {
					for(int j=0; j<t; j++) {
						z.children[j] = y.children[j+t];
						y.children[j+t] = -1L;
					}
				}
				
				y.numKeys = t-1;
				for(int j=x.numKeys; j>i; j--) {
					x.children[j+1] = x.children[j];
					x.children[j] = -1L;
				}
				x.children[i+1] = z.filePosition;
				for(int j=x.numKeys-1; j>i-1; j--) {
					x.keys[j+1] = new TreeObject(x.keys[j].getDna(), x.keys[j].getFrequency());
				}
				x.keys[i] = new TreeObject(y.keys[t-1].getDna(), y.keys[t-1].getFrequency());
				y.keys[t-1] = new TreeObject();
				x.numKeys = x.numKeys + 1;
				diskWrite(z);		
				diskWrite(y);		
				diskWrite(x);
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
	 * @throws IOException 
	 */
	public void print(BTreeNode root_node, int debug) throws IOException {
		//in-order traversal of the btree nodes
		//print all keys of the node on each traverse step
		
		//System.out.println(GeneBankCreateBTree.decodeSequence(root_node.keys[0].getDna()));
		
				FileWriter writer = new FileWriter("dump");
				
				int i;
				for(i=0; i < 2*t-1; i++) {
					if (!root_node.isLeaf) {
						if(root_node.children[i] != -1L) {
							BTreeNode n = diskRead(root_node.children[i]);
							print(n, debug);
						}
					}
					TreeObject cur = root_node.keys[i];
					if(cur.getDna() != -1) {
						writer.write(GeneBankCreateBTree.decodeSequence(root_node.keys[i].getDna()));
						System.out.print(cur.getDna() + " ");
						System.out.print(cur.getFrequency() + " ");
						System.out.println();
					}
				}
				
				if (!root_node.isLeaf) {
					if(root_node.children[i] != -1L) {
						BTreeNode n = diskRead(root_node.children[i]);
						print(n, debug);
					}
				}
				
				//	Close writer
				writer.close();
			}
		 }

