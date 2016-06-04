//package tinytalered;

import java.util.HashMap;


public class StateNode {

    private String debugName;
    private String smallImageUrl;
    private String largeImageUrl;
    private String[] audioUrls;//sequentially ordered (plays 0, 1, 2, etc)
    private String[] errorAudioUrls;//sequentially ordered (plays 0, 1, 2, etc)
    private HashMap<String,StateNode> transferStructure;//note: string is the Intent. Node is the node to transfer to for a given intent!

    public StateNode(String debugName, String smallImageUrl, String largeImageUrl, String[] audioUrls,String[] errorAudioUrls, HashMap<String,StateNode> transferStructure){
        this.debugName = debugName;
        this.smallImageUrl = smallImageUrl;
        this.largeImageUrl = largeImageUrl;
        this.audioUrls = audioUrls;
        this.errorAudioUrls = errorAudioUrls;
        this.transferStructure = transferStructure;
    }

    public String generateSSML(){
        return "";
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {return true;}
        if (!(o instanceof StateNode)) {return false;}

        StateNode sn = (StateNode) o;

        for (int i = 0; i < audioUrls.length; i++) {
            if(sn.audioUrls[i] == null){return false;}
            if(audioUrls[i].equals(sn.audioUrls[i])){return false;}
        }

        return  smallImageUrl.equals(sn.smallImageUrl) &&
                largeImageUrl.equals(sn.largeImageUrl) &&
                transferStructure.size() == sn.transferStructure.size();
    }

   public static void main(String[] args) {
        // Prints "Hello, World" to the terminal window.
        System.out.println("Hello, World");
        

        //second state
        String[] audioUrls2 = {"https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-0aIntro.mp3"};
        String[] audioErrorUrls2 = {"https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-1aError.mp3"};
        HashMap<String, StateNode> h2 = null;
        final StateNode secondState =  new StateNode("",
                                           "https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/iconsmall.png",
                                           "https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/iconlarge.png",
                                            audioUrls2,
                                            audioErrorUrls2,
                                            h2
                                   );
        
        
        //initial state
        String[] audioUrls = {"https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-0aIntro.mp3"};
        String[] audioErrorUrls = {"https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-1aError.mp3"};
        HashMap<String, StateNode> h = new HashMap<String, StateNode>() {{
            put("fakeIntent",null);
        }};
        final StateNode initialState = new StateNode("INITIAL_STATE",
                                           "https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/iconsmall.png",
                                           "https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/iconlarge.png",
                                            audioUrls,
                                            audioErrorUrls,
                                            h
                                   );
    }






}




//StateNode secondState =  StateNode("INITIAL_STATE",
//        "https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/iconsmall.png",
//        "https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/iconlarge.png",
//        Collections.unmodifiableList(Arrays.asList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-2aRedWentLeftIntoTheForest.mp3")),
//        Collections.unmodifiableList(Arrays.asList("https://s3-us-west-2.amazonaws.com/static-assets-az/tinytales-red/tinytales-red-2aError.mp3")),
//        HashMap<String,StateNode> transferStructure
//);

//ImmutableMap.<String, String>builder().put(1, "one").put(2, "two").put(15, "fifteen").build();
