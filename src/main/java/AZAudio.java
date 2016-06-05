package main.java;

public class AZAudio {

    URLList audioUrls;//sequentially ordered (plays 0, 1, 2, etc)
    URLList errorAudioUrls;//sequentially ordered (plays 0, 1, 2, etc)
    
    AZAudio(URLList a, URLList b){
    	this.audioUrls = a;
    	this.audioUrls = b;
    }
}
