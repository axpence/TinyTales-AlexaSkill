package main.java;
import java.util.HashMap;


public class AZTransferStructure {

    HashMap<String, AZStateNode> transferStructure;//String is the intent string, TreeNode is the node it transfers to!
    
    AZTransferStructure(){
    	transferStructure = new HashMap<String,AZStateNode>();
    }
	
    void addEdgeForIntent(String intent, AZStateNode n){
    	transferStructure.put(intent,n);
    }
    
    public boolean hasValidTransferForIntent(String intent){
    	return transferStructure.get(intent) != null;
    }
    
    public AZStateNode<String> getStateForIntent(String intent){
    	assert(hasValidTransferForIntent(intent));
    	return transferStructure.get(intent);
    }
}
