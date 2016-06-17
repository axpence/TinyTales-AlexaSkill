package tinytalered;

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
import com.amazon.speech.ui.SimpleCard;

import main.java.*;

import java.util.HashMap;
import java.util.ArrayList;

import javax.swing.plaf.nimbus.State;
import com.amazon.speech.ui.StandardCard;
import com.amazon.speech.ui.Image;

/**
 * This sample shows how to create a simple speechlet for handling speechlet requests.
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
    
 
    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String currentIntent = (intent != null) ? intent.getName() : null;

        
      if ("AMAZON.HelpIntent".equals(currentIntent)) {
    	  /*
    	   sample utterances: help,help me,can you help me
			Provide help about how to use the skill. See “Offer Help for Complex Skills” in the Voice Design Best Practices for guidelines about help text.
		   */
    	  //TODO: provide help instructions!
    	  
      } else if("AMAZON.StopIntent".equals(currentIntent) || "AMAZON.CancelIntent".equals(currentIntent)){
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
        try { //TODO: place outside of try/catch
        	currentStateNode = manager.getRootNode();
        } catch (Exception e) {
        	
        }
        
        if (!StringUtils.isNotEmpty(currentState)) {
            session.setAttribute(CURRENT_STATE_KEY,kInitialState);
            currentState = kInitialState;//initial state is when the skill/app is launched
        } else {
          currentStateNode = manager.getNodeMatchingDebugName(currentStateNode, currentState);
        }
        
        
        String previousIntent = (String) session.getAttribute(PREVIOUS_INTENT_KEY);
        String debugStringz = "currentStateAtStartOfTick="+currentState+",previousIntent="+previousIntent+",currentIntent="+currentIntent;
        //See if valid transition matches current state
        String speechOutput = null;
        if(currentStateNode.transferStructure.hasValidTransferForIntent(currentIntent)){
            //Set new state if match, else- error prompt
            AZStateNode<String> nextState = currentStateNode.transferStructure.getStateForIntent(currentIntent);
            session.setAttribute(CURRENT_STATE_KEY, nextState.debugName);//fire transition!
            debugStringz = "newCurrentStateAfterTransition="+nextState.debugName+","+debugStringz;
            currentStateNode = nextState;
            speechOutput = currentStateNode.audioContainer.getSSML();
        } else {
            speechOutput = currentStateNode.audioContainer.getErrorSSML();
        }
        session.setAttribute("DEBUG_AZ",debugStringz);//tmp!
        
        //update the previous intent
        session.setAttribute(PREVIOUS_INTENT_KEY,currentIntent);

        String statePathKey = (String) session.getAttribute(STATE_PATH_KEY);
        statePathKey = (statePathKey == null) ? "" : statePathKey;
        session.setAttribute(STATE_PATH_KEY,statePathKey+","+currentStateNode.debugName);//path taken in entire session!
        //TODO: save (currentState, incomingIntent) KV pairs for future debugging in time series within the session
        
//        SimpleCard card = new SimpleCard();
//        card.setTitle(currentStateNode.cardData.title);
//        card.setContent(currentStateNode.cardData.body);
       
        Image introImage = new Image();
        introImage.setLargeImageUrl("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-redlarge.png");
        introImage.setSmallImageUrl("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-redsmall.png");

        StandardCard card = new StandardCard();
        //card.setTitle("Test Standard Card"); //not in documentation
        card.setText(currentStateNode.cardData.title);
        card.setImage(introImage);

        // Create the plain text output.
        SsmlOutputSpeech speech = new SsmlOutputSpeech();
        speech.setSsml(speechOutput);
        
        SpeechletResponse r = null;
        if(currentStateNode.children.isEmpty()){
        	 r = SpeechletResponse.newTellResponse(speech, card); 
         	 r.setShouldEndSession(true);//since doesn't have children, close the skill.
        } else {
            // Create reprompt
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
        try { //TODO: place outside of try/catch
        	currentStateNode = manager.getRootNode();
        } catch (Exception e) {
        	
        }
        
        SimpleCard card = new SimpleCard();
        card.setTitle("Welcome to Tiny Tales!");
        card.setContent("Tiny Tales are interactive children's educational fables and stories told weekly with the objective of teaching children life lessons via storytelling.\n\nTiny Tales teaches children life's lessons through an engaging and adventurous storytelling method enabling you to control the ending of each story.");
        
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
        r.setShouldEndSession(false);//TODO: Amazon, please fix this..
        return r;
        
    }

}
