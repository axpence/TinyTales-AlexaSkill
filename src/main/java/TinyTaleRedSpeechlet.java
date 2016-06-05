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

import java.util.HashMap;
import java.util.ArrayList;

import javax.swing.plaf.nimbus.State;
//import com.amazon.speech.ui.StandardCard;
//import com.amazon.speech.ui.Image;

/**
 * This sample shows how to create a simple speechlet for handling speechlet requests.
 */
public class TinyTaleRedSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(TinyTaleRedSpeechlet.class);
    private static final String CURRENT_STATE = "CURRENT_STATE";
    private static final String AUDIO_S3_LINK_1  = "https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-1aOnceUponATime.mp3";

    private static final String PREVIOUS_INTENT = "PREVIOUS_INTENT";

    private static final String INITIAL_INTENT = "INITIAL_INTENT";
    private static final String Intent_GoLeft = "TinyTales_Red_GoLeftTowardForestIntent";
    private static final String Intent_GoRight = "TinyTales_Red_GoRightTowardRiverIntent";
    private static final String Intent_Shiny = "TinyTales_Red_GoTowardShinyThingIntent";
    private static final String Intent_Grandmas = "TinyTales_Red_GoTowardsGrandmasIntent";
    private static final String Intent_Cake = "TinyTales_Red_ThrowCakeAtTheWolfIntent";
    private static final String Intent_Yell = "TinyTales_Red_YellForHelpIntent";
    private static final String Intent_Skipping = "TinyTales_Red_KeepSkippingOnThePathIntent";
    private static final String Intent_FollowWolf = "TinyTales_Red_FollowWolfOnShortcutIntent";
    private static final String Intent_Stop = "AMAZON.StopIntent";

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
        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String currentIntent = (intent != null) ? intent.getName() : null;

        // Get the current state the session.
        String currentState = (String) session.getAttribute(CURRENT_STATE);
        if (!StringUtils.isNotEmpty(currentState)) {
            session.setAttribute(CURRENT_STATE,"INITIAL_STATE");

        }

        String previousIntent = (String) session.getAttribute(PREVIOUS_INTENT);
        if (!StringUtils.isNotEmpty(previousIntent)) {
            session.setAttribute(PREVIOUS_INTENT,INITIAL_INTENT);
        }

        return tickSM(currentIntent, session);

        if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            throw new SpeechletException("Invalid Intent");
        }

    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }


    private SpeechletResponse tickSM(String intentName, final Session session)  throws SpeechletException {

        String currentState = (String) session.getAttribute(CURRENT_STATE);
        if (currentState == null){ throw new java.lang.AssertionError(); }

        session.setAttribute(CURRENT_STATE,"intent:"+intentName);//tmp!

//        throw new SpeechletException("Invalid Intent!!!");

        return getDebugResponse(intentName,currentState);
    }


    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getWelcomeResponse() {

        String speechText = "Welcome to TinyTales. Say Hello to get started";

//        Image introImage = Image();
//        introImage.setLargeImageUrl("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/iconlarge.png");
//        introImage.setSmallImageUrl("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/iconsmall.png");
//
//        StandardCard card = new StandardCard();
//        card.setTitle("TestizTitle hurre");
//        card.setText("Testiez With Image");
//        card.setImage(introImage);

        SimpleCard card = new SimpleCard();
        card.setTitle("Title - HelloWorld");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    /**
     * Creates a {@code SpeechletResponse} for the hello intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelloResponse() {
        String speechText = "Hello world";

        String speechOutput = "<speak>"
                + "Start file play"
                + "<audio src='"+AUDIO_S3_LINK_1+"'/>"
                + "End file play"
                + "</speak>";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("HelloWorld");
        card.setContent(speechOutput);

        // Create the plain text output.
        SsmlOutputSpeech speech = new SsmlOutputSpeech();
        speech.setSsml(speechOutput);

        return SpeechletResponse.newTellResponse(speech, card);
    }


    private SpeechletResponse getDebugResponse(String intentName, String currentState) {
        String speechOutput = "Intent name is " + intentName + " boom." + "current state is " + currentState;

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Debug: " + intentName);
        card.setContent(speechOutput);

        // Create the plain text output.
        SsmlOutputSpeech speech = new SsmlOutputSpeech();
        speech.setSsml(speechOutput);

        SpeechletResponse r = SpeechletResponse.newTellResponse(speech, card);
        r.setShouldEndSession(false);
        return r;
    }

    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelpResponse() {
        String speechText = "You can say hello to me!";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("HelloWorld");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    public static void main(String[] args) {
        // Prints "Hello, World" to the terminal window.
        System.out.println("Hello, World");
    }
}
