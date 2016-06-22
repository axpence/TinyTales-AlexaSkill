package main.java;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class TinyTalesRedStateManager {
	
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

	public AZStateNode<String> getRootNode() throws Exception{
		
		//
		// Cards
		//
		AZCard commonCard = new AZCard("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/iconsmall.png", 
				"https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/iconlarge.png", 
				"Red - an adventure story by Tiny Tales", "Tiny Tales are interactive children's educational fables and stories told weekly with the objective of teaching children life lessons via storytelling"
		);

		//
		// States
		//
		AZStateNode<String> NODE_ROOT = new AZStateNode<String>("NODE_ROOT",
						commonCard,
						new AZAudio(
								new URLList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/0aIntro.mp3"),
								new URLList()
						)
		);
		
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
		// Intents
		//
		String INTENT_YES = "TinyTales_Red_YesIntent";
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
		NODE_ROOT.addEdgeForIntent(INTENT_YES,NODE_ONCE_UPON_A_TIME);
			NODE_ONCE_UPON_A_TIME.addEdgeForIntent(INTENT_GO_LEFT, NODE_TURN_LEFT);
			
				NODE_TURN_LEFT.addEdgeForIntent(INTENT_SHINY, NODE_SHINY_THING);
				NODE_TURN_LEFT.addEdgeForIntent(INTENT_GRANDMAS, NODE_STAY_PATH);
				
					NODE_STAY_PATH.addEdgeForIntent(INTENT_YELL, NODE_THE_END);
					NODE_STAY_PATH.addEdgeForIntent(INTENT_CAKE, NODE_THROW_CAKE_AND_END);
				
			NODE_ONCE_UPON_A_TIME.addEdgeForIntent(INTENT_GO_RIGHT, NODE_TURN_RIGHT);
			
				NODE_TURN_RIGHT.addEdgeForIntent(INTENT_SKIPPING, NODE_SKIPPING);
				NODE_TURN_RIGHT.addEdgeForIntent(INTENT_WOLF, NODE_FOLLOW_WOLF);
				
					NODE_SKIPPING.addEdgeForIntent(INTENT_CAKE, NODE_THROW_CAKE_AND_END);
					NODE_SKIPPING.addEdgeForIntent(INTENT_YELL, NODE_THE_END);

		//TODO: what if graph is cyclic ?
		//TODO: auto transitions (autoplays)
		NODE_ROOT.print();
		
		return NODE_ROOT;
	}

	public static void main(String[] args) throws Exception{
		
        TinyTalesRedStateManager manager = new TinyTalesRedStateManager();
		AZStateNode<String> root = manager.getRootNode();
		System.out.println(root.debugName + "\n");
		AZStateNode<String> found = manager.getNodeMatchingDebugName(root,"NODE_THROW_CAKE_AND_END");
		System.out.println(found.debugName);
	}

}
