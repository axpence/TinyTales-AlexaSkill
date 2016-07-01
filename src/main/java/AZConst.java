package main.java;

public enum AZConst {
	
	SESSION_KEY_CURRENT_STATE("CURRENT_STATE"),
	SESSION_KEY_PREVIOUS_INTENT("PREVIOUS_INTENT"),
	SESSION_KEY_STATE_PATH("STATE_PATH_KEY"),
	
	INITIAL_STATE("NODE_ROOT");

	private String value;
	public String getValue(){return value;}
	
	AZConst(String value) {this.value = value;} 
}
