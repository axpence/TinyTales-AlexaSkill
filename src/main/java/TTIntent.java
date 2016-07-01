package main.java;

public enum TTIntent {
	
	INTENT_YES("TinyTales_Red_YesIntent"),
	INTENT_GO_LEFT("TinyTales_Red_GoLeftTowardForestIntent"),
	INTENT_GO_RIGHT("TinyTales_Red_GoRightTowardRiverIntent"),
	INTENT_SHINY("TinyTales_Red_GoTowardShinyThingIntent"),
	INTENT_GRANDMAS("TinyTales_Red_GoTowardsGrandmasIntent"),
	INTENT_CAKE("TinyTales_Red_ThrowCakeAtTheWolfIntent"),
	INTENT_YELL("TinyTales_Red_YellForHelpIntent"),
	INTENT_SKIPPING("TinyTales_Red_KeepSkippingOnThePathIntent"),
	INTENT_WOLF("TinyTales_Red_FollowWolfOnShortcutIntent"),
	INTENT_STOP("AMAZON.StopIntent"),//TODO: how to handle "universal" intents? (exclusivity, etc?)
	INTENT_HELP("AMAZON.HelpIntent"),
	INTENT_CANCEL("AMAZON.CancelIntent");

	public String name;
	TTIntent(String value) {this.name = value;} 
}