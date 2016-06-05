package main.java;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//import URLList;



public class TreeNode<T> implements Iterable<TreeNode<T>> { //TODO: use T to signify type of node with typealiases..

    T data;
    
    TreeNode<T> parent;
    List<TreeNode<T>> children;
    
    T debugName;
    AZCard cardData;
    AZAudio audioContainer;

    AZTransferStructure transferStructure;//note: string is the Intent string. Node is the node to transfer to for a given intent!
    
    public TreeNode(T debugName,AZCard cardData, AZAudio audioContainer ){
        this.debugName = debugName;
        this.cardData = cardData;
        this.audioContainer = audioContainer;
        this.transferStructure = new AZTransferStructure();
        this.children = new LinkedList<TreeNode<T>>();
    }

	public Iterator<TreeNode<T>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String toString(){
		return (String)this.debugName;
	}
	
	void addEdgeForIntent(String intentString, TreeNode n) throws Exception{
		
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
		
		
		//
		// Cards
		//
		AZCard commonCard = new AZCard("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/iconsmall.png", 
				"https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/iconlarge.png", 
				"Card Title Goes Here", "This is card description here!\nAnd more description!"
		);
		

		//
		// States
		//
		TreeNode<String> NODE_ROOT = new TreeNode<String>("NODE_ROOT",
						commonCard,
						new AZAudio(
								new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-1aOnceUponATime.mp3"),
								new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-1aError.mp3")		
						)
		);
			
		TreeNode<String> NODE_TURN_LEFT = new TreeNode<String>("NODE_TURN_LEFT",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-2aRedWentLeftIntoTheForest.mp3"),
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-2aError.mp3")
				)
		);
		
		TreeNode<String> NODE_TURN_RIGHT = new TreeNode<String>("NODE_TURN_RIGHT",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-2bTurningRight.mp3"),
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-2bError.mp3")
				)
		);

		TreeNode<String> NODE_SHINY_THING = new TreeNode<String>("NODE_SHINY_THING",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-3aRedWalkedToTheShinyThing.mp3"),
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-3bError.mp3")
				)
		);
		
		TreeNode<String> NODE_STAY_PATH = new TreeNode<String>("NODE_STAY_PATH",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-3bRedStayedOnThePath.mp3"),
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-3cError.mp3")
				)
		);
		
		TreeNode<String> NODE_SKIPPING = new TreeNode<String>("NODE_SKIPPING",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-3cRedKeptSkippingAlongThePath.mp3"),
						new URLList()
				)
		);
		
		TreeNode<String> NODE_FOLLOW_WOLF = new TreeNode<String>("NODE_FOLLOW_WOLF",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-3dRedFollowedTheWolfOnAShortcut.mp3"),
						new URLList()
				)
		);
		
		
		TreeNode<String> NODE_THROW_CAKE = new TreeNode<String>("NODE_THROW_CAKE",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-4aCake.mp3"),
						new URLList()
				)
		);
		
		
		TreeNode<String> NODE_THROW_CAKE_AND_END = new TreeNode<String>("NODE_THROW_CAKE_AND_END",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-4aCake.mp3",
								    "https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-5aEnding.mp3"
						),
						new URLList()
				)
		);
		
		TreeNode<String> NODE_THE_END = new TreeNode<String>("NODE_THE_END",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-5aEnding.mp3"),
						new URLList()
				)
		);
	
		//
		// Intents
		//
		String INTENT_GO_LEFT = "TinyTales_Red_GoLeftTowardForestIntent";
		String INTENT_GO_RIGHT = "TinyTales_Red_GoRightTowardRiverIntent";
		String INTENT_SHINY = "TinyTales_Red_GoTowardShinyThingIntent";
		String INTENT_GRANDMAS = "TinyTales_Red_GoTowardsGrandmasIntent";
		String INTENT_CAKE = "TinyTales_Red_ThrowCakeAtTheWolfIntent";
		String INTENT_YELL = "TinyTales_Red_YellForHelpIntent";
		String INTENT_SKIPPING = "TinyTales_Red_KeepSkippingOnThePathIntent";
		String INTENT_WOLF = "TinyTales_Red_FollowWolfOnShortcutIntent";
		String INTENT_STOP = "AMAZON.StopIntent";//TODO: how to handle "universal" intents? (exclusivity, etc?)
		
		
		//
		// Edges / Transitions
		//
		NODE_ROOT.addEdgeForIntent(INTENT_GO_LEFT, NODE_TURN_LEFT);
		
			NODE_TURN_LEFT.addEdgeForIntent(INTENT_SHINY, NODE_SHINY_THING);
			NODE_TURN_LEFT.addEdgeForIntent(INTENT_GRANDMAS, NODE_STAY_PATH);
			
				NODE_STAY_PATH.addEdgeForIntent(INTENT_CAKE, NODE_THROW_CAKE_AND_END);
			
		NODE_ROOT.addEdgeForIntent(INTENT_GO_RIGHT, NODE_TURN_RIGHT);
		
			NODE_TURN_RIGHT.addEdgeForIntent(INTENT_SKIPPING, NODE_SKIPPING);
			NODE_TURN_RIGHT.addEdgeForIntent(INTENT_WOLF, NODE_FOLLOW_WOLF);
			
				NODE_SKIPPING.addEdgeForIntent(INTENT_CAKE, NODE_THROW_CAKE);
				NODE_SKIPPING.addEdgeForIntent(INTENT_YELL, NODE_THE_END);

		//TODO: what if graph is cyclic ?
		//TODO: auto transitions
		NODE_ROOT.print();

	}

}