package main.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazon.speech.ui.Reprompt;

import main.java.*;

import java.util.HashMap;
import java.util.ArrayList;

import javax.swing.plaf.nimbus.State;
import com.amazon.speech.ui.StandardCard;
import com.amazon.speech.ui.Image;

/**
 * This speechlet for handling speechlet requests. You can think of speechlet requests as lifecycle methods on the lifecycle of an Alexa skill.
 */
public class TinyTaleRedSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(TinyTaleRedSpeechlet.class);
    private static final String CURRENT_STATE_KEY = "CURRENT_STATE";

    private static final String PREVIOUS_INTENT_KEY = "PREVIOUS_INTENT";
    private static final String STATE_PATH_KEY = "STATE_PATH_KEY";

    private static final String kInitialIntent = "kInitialIntent";
    private static final String kInitialState = "NODE_ROOT";
    
    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getWelcomeResponse();//TODO: can set session var here.
    }
    
 
    @Override //TODO: refactor this onIntent() method to be much smaller.
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent incomingIntent = request.getIntent();
        
      if (AZIntent.INTENT_HELP.name.equals(incomingIntent.getName())) { //"AMAZON.HelpIntent"
    	  /*
    	   sample utterances: help,help me,can you help me
			Provide help about how to use the skill. See “Offer Help for Complex Skills” in the Voice Design Best Practices for guidelines about help text.
		   */
        SsmlOutputSpeech speech = new SsmlOutputSpeech();
        speech.setSsml("Tiny tales allows you to listen to interactive stories. As the story progresses, answer the questions you are prompted to as and choose the adventure you'd like to embark on. Are you ready to listen to a story?");
		String errorSSML = "Is there something we can help you with?";
		SsmlOutputSpeech repromptSpeech = new SsmlOutputSpeech();
		repromptSpeech.setSsml(errorSSML);
		
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(repromptSpeech);
		
		SpeechletResponse r = SpeechletResponse.newAskResponse(speech, reprompt);
		r.setShouldEndSession(false);
    	return r;
    	
      } else if(AZIntent.INTENT_STOP.name.equals(incomingIntent.getName()) || AZIntent.INTENT_CANCEL.name.equals(incomingIntent.getName())){//"AMAZON.StopIntent" or "AMAZON.CancelIntent"
    	  /*
    	    AMAZON.StopIntent - Either of the following:
				Let the user stop an action (but remain in the skill)
				Let the user completely exit the skill
			AMAZON.CancelIntent - Either of the following:
				Let the user cancel a transaction or task (but remain in the skill)
				Let the user completely exit the skill
    	   */
    	  
    	  PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
          outputSpeech.setText("Exiting Tiny Tales! Goodbye!");
          SpeechletResponse r = SpeechletResponse.newTellResponse(outputSpeech);
      	  r.setShouldEndSession(true);//since user asked to cancel or stop, end skill's session
          return r;
      } 
        
        // Get the current state the session, get current state node
        String currentState = (String) session.getAttribute(CURRENT_STATE_KEY);
        TinyTalesRedStateManager manager = new TinyTalesRedStateManager();
        AZStateNode<String> currentStateNode = null;
        currentStateNode = manager.getRootNode();
        
        if (!StringUtils.isNotEmpty(currentState)) {
            session.setAttribute(CURRENT_STATE_KEY,kInitialState);
            currentState = kInitialState;//initial state is when the skill/app is launched
        } else {
          currentStateNode = manager.getNodeMatchingDebugName(currentStateNode, currentState);
        }
        
        String previousIntent = (String) session.getAttribute(PREVIOUS_INTENT_KEY);
        String debugStringz = "currentStateAtStartOfTick="+currentState+",previousIntent="+previousIntent+",incomingIntent="+incomingIntent.getName();
        //See if valid transition matches current state
        String speechOutput = null;
        if(currentStateNode.transferStructure.hasValidTransferForIntent(incomingIntent)){
            //Set new state if match, else- error prompt
            AZStateNode<String> nextState = currentStateNode.transferStructure.getStateForIntent(incomingIntent);
            session.setAttribute(CURRENT_STATE_KEY, nextState.debugName);//fire transition!
            debugStringz = "newCurrentStateAfterTransition="+nextState.debugName+","+debugStringz;
            currentStateNode = nextState;
            speechOutput = currentStateNode.audioContainer.getSSML();
        } else {
            speechOutput = currentStateNode.audioContainer.getErrorSSML();
        }
        session.setAttribute("DEBUG_AZ",debugStringz);//tmp!
        
        //update the previous intent
        session.setAttribute(PREVIOUS_INTENT_KEY,incomingIntent.getName());

        String statePathKey = (String) session.getAttribute(STATE_PATH_KEY);
        statePathKey = (statePathKey == null) ? "" : statePathKey;
        session.setAttribute(STATE_PATH_KEY,statePathKey+","+currentStateNode.debugName);//path taken in entire session!
        //TODO: save (currentState, incomingIntent) KV pairs for future debugging in time series within the session
       
        Image introImage = new Image();
        introImage.setLargeImageUrl("https://s3-us-west-2.amazonaws.com/static-alexa-img-assets/color_large2.png");
        introImage.setSmallImageUrl("https://s3-us-west-2.amazonaws.com/static-alexa-img-assets/color_small2.png");
        
        StandardCard card = new StandardCard();
        card.setText(currentStateNode.cardData.title);
        card.setImage(introImage);

        SsmlOutputSpeech speech = new SsmlOutputSpeech();
        speech.setSsml(speechOutput);
        
        SpeechletResponse r = null;
        if(currentStateNode.children.isEmpty()){
        	 r = SpeechletResponse.newTellResponse(speech, card); 
         	 r.setShouldEndSession(true);//since doesn't have children, close the skill.
        } else {
        	String errorSSML = currentStateNode.audioContainer.getErrorSSML();
        	if(errorSSML == null){errorSSML = speechOutput;}
        	SsmlOutputSpeech repromptSpeech = new SsmlOutputSpeech();
        	repromptSpeech.setSsml(errorSSML);
        	
            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(repromptSpeech);

            r = SpeechletResponse.newAskResponse(speech, reprompt, card);
        	r.setShouldEndSession(false);//keep skill open since has children!
        }
        return r;  
    }


    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }


    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getWelcomeResponse() {

        String currentState = kInitialState;
        
        TinyTalesRedStateManager manager = new TinyTalesRedStateManager();
        AZStateNode<String> currentStateNode = null;
        currentStateNode = manager.getRootNode();
        
        Image introImage = new Image();
        introImage.setLargeImageUrl("https://s3-us-west-2.amazonaws.com/static-alexa-img-assets/tinytales-red/story_intro_large.png");
        introImage.setSmallImageUrl("https://s3-us-west-2.amazonaws.com/static-alexa-img-assets/tinytales-red/story_intro_small.png");
        
        StandardCard card = new StandardCard();
        card.setText("Welcome to Tiny Tales!\n\n" + "Tiny Tales are interactive children's educational fables and stories told weekly with the objective of teaching children life lessons via storytelling.\n\nTiny Tales teaches children life's lessons through an engaging and adventurous storytelling method enabling you to control the ending of each story.");
        card.setImage(introImage);
        
        String speechOutput = currentStateNode.audioContainer.getSSML();
    	String errorSSML = currentStateNode.audioContainer.getErrorSSML();
    	if(errorSSML == null){errorSSML = speechOutput;}
    	SsmlOutputSpeech repromptSpeech = new SsmlOutputSpeech();
    	repromptSpeech.setSsml(errorSSML);
    	
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);
        
        SsmlOutputSpeech speech = new SsmlOutputSpeech();
        speech.setSsml(speechOutput);
        
        SpeechletResponse r = SpeechletResponse.newAskResponse(speech, reprompt, card);
        r.setShouldEndSession(false);
        return r;
        
    }

}
