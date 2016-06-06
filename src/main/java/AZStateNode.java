package main.java;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AZStateNode<T> implements Iterable<AZStateNode<T>> { //TODO: use T to signify type of node with typealiases..
    
	public AZStateNode<T> parent;
    public List<AZStateNode<T>> children;
    
    public T debugName;
    public AZCard cardData;
    public AZAudio audioContainer;

    public AZTransferStructure transferStructure;//note: string is the Intent string. Node is the node to transfer to for a given intent!
    
    public AZStateNode(T debugName,AZCard cardData, AZAudio audioContainer ){
        this.debugName = debugName;
        this.cardData = cardData;
        this.audioContainer = audioContainer;
        this.transferStructure = new AZTransferStructure();
        this.children = new LinkedList<AZStateNode<T>>();
    }

	@Override
	public String toString(){
		return (String)this.debugName;
	}
	
	void addEdgeForIntent(String intentString, AZStateNode n) throws Exception{
		
		//TODO: make sure "debug name" doesn't match any other in tree...
		
		if(transferStructure.hasValidTransferForIntent(intentString)){
			throw new Exception("already contains edge! Cannot add two edges for same intent!");
		} 
		transferStructure.addEdgeForIntent(intentString, n);
		this.children.add(n);
		n.parent = this;
	}
	
	public void print() {
		print("", true);
	}
	
	private void print(String prefix, boolean isTail) {
	    System.out.println(prefix + (isTail ? "└── " : "├── ") + debugName);
	    for (int i = 0; i < children.size() - 1; i++) {
	        children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
	    }
	    if (children.size() > 0) {
	        children.get(children.size() - 1).print(prefix + (isTail ?"    " : "│   "), true);
	    }
	}
	
	public static void main(String[] args) throws Exception{

	}

	public Iterator<AZStateNode<T>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}