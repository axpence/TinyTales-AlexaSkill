package main.java;

public class AZAudio {

    URLList audioUrls;//sequentially ordered (plays 0, 1, 2, etc)
    URLList errorAudioUrls;//sequentially ordered (plays 0, 1, 2, etc)
    
    AZAudio(URLList a, URLList b){
    	this.audioUrls = a;
    	this.errorAudioUrls = b;
    }
    
    public String getSSML(){
    	return this.ssmlBuilderHelper(this.audioUrls);
    }
    
    public String getErrorSSML(){
    	return this.ssmlBuilderHelper(this.errorAudioUrls);
    }
    
    public String ssmlBuilderHelper(URLList l){
    	if(l.urls.length == 0){return null;}
    	StringBuilder sb = new StringBuilder();
    	sb.append("<speak>");
    	for(String s : l.urls){
    		sb.append("<audio src='"+s+"'/>");
    	}
    	sb.append("</speak>");
    	return sb.toString();
    }
}
