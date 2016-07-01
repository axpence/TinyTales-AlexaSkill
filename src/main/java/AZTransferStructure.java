package main.java;
import java.util.HashMap;

import com.amazon.speech.slu.Intent;

import main.java.TTIntent;


public class AZTransferStructure {

    HashMap<String, AZStateNode> transferStructure;//String is the intent string, TreeNode is the node it transfers to!
    
    AZTransferStructure(){
    	transferStructure = new HashMap<String,AZStateNode>();
    }
	
    void addEdgeForIntent(TTIntent intent, AZStateNode n){
    	transferStructure.put(intent.name,n);
    }
    
    public boolean hasValidTransferForIntent(TTIntent intent){
    	return transferStructure.get(intent.name) != null;
    }
    
    public AZStateNode<String> getStateForIntent(TTIntent intent){
    	assert(hasValidTransferForIntent(intent));
    	return transferStructure.get(intent.name);
    }
    
    public boolean hasValidTransferForIntent(Intent intent){
    	return transferStructure.get(intent.getName()) != null;
    }
    
    public AZStateNode<String> getStateForIntent(Intent intent){
    	assert(hasValidTransferForIntent(intent));
    	return transferStructure.get(intent.getName());
    }
}
