# TinyTales-AlexaSkill
I created an app for Amazon's Alexa using the [Alexa Skills Kit](https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit). The app is a choose your own adventure style stories app on Alexa

##How To Build

build by running `mvn assembly:assembly -DdescriptorId=jar-with-dependencies package` in same directory as the maven `pom.xml` file


Tiny Tales are interactive children's educational fables and stories told weekly with the objective of teaching children life lessons via storytelling.

Tiny Tales teaches children life's lessons through an engaging and adventurous storytelling method enabling you to control the ending of each story.


##Helper Scripts
I built a helper sript that uses `ffmpeg` to encode audio files into the correct format for Alexa. Run `python /static/audio-files/encode_script.py` script after placing audio files within the `/static/audio-files` directory. The output of this script will be placed within `/static/audio-files/encoded-for-echo`

##misc tips and tricks
I ran into a few hiccups when trying to publish my skill, when hosting images you need to have a separate bucket for the companion app to properly read the images so that the image have the proper CORS enabled. See [here](https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/providing-home-cards-for-the-amazon-alexa-app) for more details.

If you would like to open this codebase within the Eclipse IDE, download the latest version of Eclipse and navigate to `file > import > Maven > Existing Maven Projects` and choose the directory with the `pom.xml` file to properly import this project and all dependencies into Eclipse.