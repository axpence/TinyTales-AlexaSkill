# TinyTales: an Alexa Skill
I created an app (skill) for Amazon's Alexa using the [Alexa Skills Kit](https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit). The app is a choose your own adventure style stories app on Alexa

##How To Build
Build the skill by running `mvn assembly:assembly -DdescriptorId=jar-with-dependencies package` in same directory as the maven `pom.xml` file. The above command will create two `.jar` files within the `/target` directory.

Tiny Tales are interactive children's educational fables and stories told weekly with the objective of teaching children life lessons via storytelling.

Tiny Tales teaches children life lessons through an engaging and adventurous storytelling method enabling you to control the ending of each story.

##How To Deploy
1) go to developer.amazon.com, select Alexa, select Alexa Skills Kit, create a new skill, then proceed to create your skill.
2) Add your specific Application ID to the `supportedApplicationIds` array in TinyTaleRedSpeechletRequestStreamHandler.java.  
3) Create a [lambda function](https://aws.amazon.com/lambda/) in the `us-east-1` AWS region (N. Virginia) whose handler is the `TinyTaleRedSpeechletRequestStreamHandler` class.

##Helper Scripts
I built a helper script that uses `ffmpeg` to encode audio files into the correct format for Alexa. Run `python /static/audio-files/encode_script.py` script after placing audio files within the `/static/audio-files` directory. The output of this script will be placed within `/static/audio-files/encoded-for-echo`.

##Misc Tips and Tricks
I ran into a few hiccups when trying to publish my skill when hosting images you need to have a separate bucket for the companion app to properly read the images so that the image has the proper CORS enabled. See [here](https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/providing-home-cards-for-the-amazon-alexa-app) for more details.

If you would like to open this codebase within the Eclipse IDE, download the latest version of Eclipse and navigate to `file > import > Maven > Existing Maven Projects` and choose the directory with the `pom.xml` file to properly import this project and all dependencies into Eclipse.

