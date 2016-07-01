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
public class TTRedSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(TTRedSpeechlet.class);
    
    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        return getWelcomeResponse(session);
    }
    
 
    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
    	log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		
		Intent incomingIntent = request.getIntent();

		if (TTIntent.INTENT_HELP.name.equals(incomingIntent.getName())) { //"AMAZON.HelpIntent" sample utterances: help,help me,can you help me. Provide help about how to use the skill. See “Offer Help for Complex Skills” in the Voice Design Best Practices for guidelines about help text.
			return getHelpAskResponse();
		} else if(TTIntent.INTENT_STOP.name.equals(incomingIntent.getName()) || TTIntent.INTENT_CANCEL.name.equals(incomingIntent.getName())){
			return getStopOrCancelTellResponse();
		} 
        
		// Get the current state the session, get current state node
		AZStateNode<String> currentStateNode = getCurrentStateNode(session);
		
		//See if valid transition matches current state
		String speechOutput = null;
		if(currentStateNode.transferStructure.hasValidTransferForIntent(incomingIntent)){
		    AZStateNode<String> nextState = currentStateNode.transferStructure.getStateForIntent(incomingIntent);
		    session.setAttribute(AZConst.SESSION_KEY_CURRENT_STATE.getValue(), nextState.debugName);//setting the "CURRENT_STATE" in the session ensures the transition is executed
		    currentStateNode = nextState;
		    speechOutput = currentStateNode.audioContainer.getSSML();
		} else {
		    speechOutput = currentStateNode.audioContainer.getErrorSSML();
		}
		
		saveDebugInfoToSession(session, currentStateNode, incomingIntent);
						
		Image introImage = new Image();
		introImage.setLargeImageUrl(currentStateNode.cardData.largeImageUrl);
		introImage.setSmallImageUrl(currentStateNode.cardData.smallImageUrl);
		
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
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        // any cleanup logic goes here
    }

    
    /**
     * Creates and returns a {@code SpeechletResponse} with a Stop or cancel message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */    
    private SpeechletResponse getStopOrCancelTellResponse() {
  	  /* 
	   	AMAZON.StopIntent - Either of the following: (1) Let the user stop an action (but remain in the skill) or (2) Let the user completely exit the skill
		AMAZON.CancelIntent - Either of the following: (1) Let the user cancel a transaction or task (but remain in the skill) or (2) Let the user completely exit the skill
	   */
	  
	  PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
      outputSpeech.setText("Exiting Tiny Tales! Goodbye!");
      SpeechletResponse r = SpeechletResponse.newTellResponse(outputSpeech);
  	  r.setShouldEndSession(true);//since user asked to cancel or stop, end skill's session
      return r;
    }
    
    /**
     * Creates and returns a {@code SpeechletResponse} with a help message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */    
    private SpeechletResponse getHelpAskResponse() {

    	TTRedStateManager manager = new TTRedStateManager();
    	
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(manager.getHelpNode().cardData.body);
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);
		
		SpeechletResponse r = SpeechletResponse.newAskResponse(speech, reprompt);
		r.setShouldEndSession(false);
    	return r;
    }
    
    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getWelcomeResponse(final Session session) {
    	
    	//TODO: initialize session var here.
    	
        TTRedStateManager manager = new TTRedStateManager();
        AZStateNode<String> currentStateNode = null;
        currentStateNode = manager.getRootNode();
        
        Image introImage = new Image();
        introImage.setLargeImageUrl(currentStateNode.cardData.largeImageUrl);
        introImage.setSmallImageUrl(currentStateNode.cardData.smallImageUrl);
        
        StandardCard card = new StandardCard();
        card.setText(currentStateNode.cardData.body);
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

    private AZStateNode<String> getCurrentStateNode(final Session session){
        TTRedStateManager manager = new TTRedStateManager();
		String currentState = (String) session.getAttribute(AZConst.SESSION_KEY_CURRENT_STATE.getValue());
		if (!StringUtils.isNotEmpty(currentState)) {
		    currentState = AZConst.INITIAL_STATE.getValue();//initial state is when the skill/app is launched, the root node in story tree
		    session.setAttribute(AZConst.SESSION_KEY_CURRENT_STATE.getValue(),currentState);
			return manager.getRootNode();
		} else {
		  return manager.getNodeMatchingDebugName(manager.getRootNode(), currentState);
		}
    }
    
    private void saveDebugInfoToSession(final Session session,AZStateNode<String> currentStateNode, Intent incomingIntent){
    	
		String previousIntent = (String) session.getAttribute(AZConst.SESSION_KEY_PREVIOUS_INTENT.getValue());
		String currentState = (String) session.getAttribute(AZConst.SESSION_KEY_CURRENT_STATE.getValue());
		String debugString = "currentStateAtStartOfTick="+currentState+",previousIntent="+previousIntent+",incomingIntent="+incomingIntent.getName();
		//See if valid transition matches current state
		if(currentStateNode.transferStructure.hasValidTransferForIntent(incomingIntent)){
		    AZStateNode<String> nextState = currentStateNode.transferStructure.getStateForIntent(incomingIntent);
		    debugString = "newCurrentStateAfterTransition="+nextState.debugName+","+debugString;
		}
		String debugHistory = (String) session.getAttribute(AZConst.SESSION_KEY_DEBUG_PATH.getValue());
		debugHistory = (debugHistory == null) ? "" : debugHistory;		
		session.setAttribute(AZConst.SESSION_KEY_DEBUG_PATH.getValue(),debugHistory+"\r\n"+debugString);
		//update the previous intent
		session.setAttribute(AZConst.SESSION_KEY_PREVIOUS_INTENT.getValue(),incomingIntent.getName());
		
		String statePathKey = (String) session.getAttribute(AZConst.SESSION_KEY_STATE_PATH.getValue());
		statePathKey = (statePathKey == null) ? "" : statePathKey;
		session.setAttribute(AZConst.SESSION_KEY_STATE_PATH.getValue(),statePathKey+","+currentStateNode.debugName);//path taken in entire session!	
    }
    
}