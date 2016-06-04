import java.util.HashMap;


public class AZTransferStructure {

    HashMap<String, TreeNode> transferStructure;//String is the intent string, TreeNode is the node it transfers to!
    
    AZTransferStructure(){
    	transferStructure = new HashMap<String,TreeNode>();
    }
	
    void addEdgeForIntent(String intent, TreeNode n){
    	transferStructure.put(intent,n);
    }
    
    boolean hasValidTransferForIntent(String intent){
    	return transferStructure.get(intent) != null;
    }
}
