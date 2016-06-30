package main.java;
import java.util.HashMap;

import com.amazon.speech.slu.Intent;

import main.java.AZIntent;


public class AZTransferStructure {

    HashMap<String, AZStateNode> transferStructure;//String is the intent string, TreeNode is the node it transfers to!
    
    AZTransferStructure(){
    	transferStructure = new HashMap<String,AZStateNode>();
    }
	
    void addEdgeForIntent(AZIntent intent, AZStateNode n){
    	transferStructure.put(intent.name,n);
    }
    
    public boolean hasValidTransferForIntent(AZIntent intent){
    	return transferStructure.get(intent.name) != null;
    }
    
    public AZStateNode<String> getStateForIntent(AZIntent intent){
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
