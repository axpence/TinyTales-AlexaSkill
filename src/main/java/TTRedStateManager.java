package main.java;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import main.java.TTIntent;
import main.java.AZConst;

public class TTRedStateManager {
	
	private AZStateNode<String> helpNode = new AZStateNode<String>("HELP_NODE",
				new AZCard("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/iconsmall.png", 
						"https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/iconlarge.png", 
						"Red - an adventure story by Tiny Tales", "Tiny tales allows you to listen to interactive stories. As the story progresses, answer the questions you are prompted to as and choose the adventure you'd like to embark on. Are you ready to listen to a story?"
				),
				new AZAudio(
						new URLList(),
						new URLList()
				)
	);
	public AZStateNode<String> getHelpNode() {return helpNode;}
	
	private AZStateNode<String> welcomeNode = new AZStateNode<String>(AZConst.INITIAL_STATE.getValue(),
				new AZCard("https://s3-us-west-2.amazonaws.com/static-alexa-img-assets/tinytales-red/story_intro_small.png", 
						"https://s3-us-west-2.amazonaws.com/static-alexa-img-assets/tinytales-red/story_intro_large.png", 
						"Red - an adventure story by Tiny Tales", "Welcome to Tiny Tales!\n\n" + "Tiny Tales are interactive children's educational fables and stories told weekly with the objective of teaching children life lessons via storytelling.\n\nTiny Tales teaches children life's lessons through an engaging and adventurous storytelling method enabling you to control the ending of each story."
				),
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/0aIntro.mp3"),
						new URLList()
				)
	);
	public AZStateNode<String> getWelcomeNode() {return welcomeNode;}
	
	//fetch node by name
	public AZStateNode<String> getNodeMatchingDebugName(AZStateNode<String> startNode, String goalNodeDebugName) {
		
		//BFS to find node.
		if(startNode.debugName.equals(goalNodeDebugName)){
			 System.out.println("Goal Node Found!");
			 System.out.println(startNode);
			 return startNode;
		}
		
		Queue<AZStateNode<String>> queue = new LinkedList<AZStateNode<String>>();
		ArrayList<AZStateNode<String>> explored = new ArrayList<AZStateNode<String>>();
		queue.add(startNode);
		explored.add(startNode);
		
		while(!queue.isEmpty()){
			AZStateNode<String> current = queue.remove();
		    if(current.debugName.equals(goalNodeDebugName)) {
		        System.out.println(explored);
		        return current;
		    }
		    else{
		        queue.addAll(current.children);
		    }
		    explored.add(current);
		}
		
		return null;
	}

	public AZStateNode<String> getRootNode() {
		
		//
		// Cards
		//
		AZCard commonCard = new AZCard("https://s3-us-west-2.amazonaws.com/static-alexa-img-assets/color_small2.png", 
				"https://s3-us-west-2.amazonaws.com/static-alexa-img-assets/color_large2.png", 
				"Red - an adventure story by Tiny Tales", "Tiny Tales are interactive children's educational fables and stories told weekly with the objective of teaching children life lessons via storytelling"
		);

		//
		// States
		//
		
		AZStateNode<String> NODE_ROOT = welcomeNode;
		
		AZStateNode<String> NODE_ONCE_UPON_A_TIME = new AZStateNode<String>("NODE_ONCE_UPON_A_TIME",
						commonCard,
						new AZAudio(
								new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/1aOnceUponATime.mp3"),
								new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/1aError.mp3")		
						)
		);
			
		AZStateNode<String> NODE_TURN_LEFT = new AZStateNode<String>("NODE_TURN_LEFT",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/2aRedWentLeftIntoTheForest.mp3"),
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/2aError.mp3")
				)
		);
		
		AZStateNode<String> NODE_TURN_RIGHT = new AZStateNode<String>("NODE_TURN_RIGHT",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/2bTurningRight.mp3"),
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/2bError.mp3")
				)
		);

		AZStateNode<String> NODE_SHINY_THING = new AZStateNode<String>("NODE_SHINY_THING",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/3aRedWalkedToTheShinyThing.mp3"),
						new URLList("")//TODO: null or empty?
				)
		);
		
		AZStateNode<String> NODE_STAY_PATH = new AZStateNode<String>("NODE_STAY_PATH",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/3bRedStayedOnThePath.mp3"),
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/3bError.mp3")
				)
		);
		
		AZStateNode<String> NODE_SKIPPING = new AZStateNode<String>("NODE_SKIPPING",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/3cRedKeptSkippingAlongThePath.mp3"),
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/3cError.mp3")
				)
		);
		
		AZStateNode<String> NODE_FOLLOW_WOLF = new AZStateNode<String>("NODE_FOLLOW_WOLF",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/3dRedFollowedTheWolfOnAShortcut.mp3"),
						new URLList()
				)
		);
		
		
		AZStateNode<String> NODE_THROW_CAKE_AND_END = new AZStateNode<String>("NODE_THROW_CAKE_AND_END",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/4aCake.mp3",
								    "https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/5aEnding.mp3"
						),
						new URLList()
				)
		);
		
		AZStateNode<String> NODE_THE_END = new AZStateNode<String>("NODE_THE_END",
				commonCard,
				new AZAudio(
						new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/5aEnding.mp3"),
						new URLList()
				)
		);

		
		//
		// Edges / Transitions
		//
		NODE_ROOT.addEdgeForIntent(TTIntent.INTENT_YES,NODE_ONCE_UPON_A_TIME);
			NODE_ONCE_UPON_A_TIME.addEdgeForIntent(TTIntent.INTENT_GO_LEFT, NODE_TURN_LEFT);
			
				NODE_TURN_LEFT.addEdgeForIntent(TTIntent.INTENT_SHINY, NODE_SHINY_THING);
				NODE_TURN_LEFT.addEdgeForIntent(TTIntent.INTENT_GRANDMAS, NODE_STAY_PATH);
				
					NODE_STAY_PATH.addEdgeForIntent(TTIntent.INTENT_YELL, NODE_THE_END);
					NODE_STAY_PATH.addEdgeForIntent(TTIntent.INTENT_CAKE, NODE_THROW_CAKE_AND_END);
				
			NODE_ONCE_UPON_A_TIME.addEdgeForIntent(TTIntent.INTENT_GO_RIGHT, NODE_TURN_RIGHT);
			
				NODE_TURN_RIGHT.addEdgeForIntent(TTIntent.INTENT_SKIPPING, NODE_SKIPPING);
				NODE_TURN_RIGHT.addEdgeForIntent(TTIntent.INTENT_WOLF, NODE_FOLLOW_WOLF);
				
					NODE_SKIPPING.addEdgeForIntent(TTIntent.INTENT_CAKE, NODE_THROW_CAKE_AND_END);
					NODE_SKIPPING.addEdgeForIntent(TTIntent.INTENT_YELL, NODE_THE_END);

		//TODO: what if graph is cyclic ?
		//TODO: how to handle auto transitions (autoplays)
		NODE_ROOT.print();
		
		return NODE_ROOT;
	}

	public static void main(String[] args) throws Exception{
		
        TTRedStateManager manager = new TTRedStateManager();
		AZStateNode<String> root = manager.getRootNode();
		System.out.println(root.debugName + "\n");
		AZStateNode<String> found = manager.getNodeMatchingDebugName(root,"NODE_THROW_CAKE_AND_END");
		System.out.println(found.debugName);
	}

}
