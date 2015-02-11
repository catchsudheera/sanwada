package tests;


import FeatureSets.FeatureSetAll;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Dammina on 2/6/2015.
 */

public class TestJunit {
    private FeatureSetAll fsa;
    private String [] verbs = {"හොයන්න", "හැරෙනවා", "ඉන්නකෝ", "යොදාගන්න", "නඟිනවා", "නමපන්", "ජීවත්වෙන්න", "එනවා", "උගන්නන්න", "ඉඩතියන්න", "අදින්න", "දියල්ලා", "ගන්න", "දකින්න", "දෙන්න", "බැහැපං", "හිතන්", "ඇවිදින්න", "හිටහන්", "කරන්න", "නැවතියන්", "නැඟිටින්න", "ඉන්න", "දීපන්", "එන්න", "නැගිටිනවා", "කියපං", "වරෙල්ලා", "පලයං", "දාන්න", "දුවපන්", "පැදපන්", "සංතෝෂවෙයල්ලා", "කතාබහකරන්න", "ඇඳගන්න", "ගොඩඑන්න", "දුවපල්ලා", "අල්ලන්න", "පෙරළන්න", "ඇරපන්", "බලාගන්න", "එවන්න", "නවත්වනවා", "දීපල්ලා", "ගේන්න", "බලමු", "නගින්න", "බහින්න", "අස්කරගන්න", "කරනවා", "දෙනවා", "නවත්තන්න", "දුක්වෙන්න", "යන්නකෝ", "මරන්න", "යමල්ලා", "යවන්න", "ඔබන්න", "හිටගන්නවා", "කියන්නකෝ", "තේරුම්ගන්න", "කාපන්", "වහගන්නවා", "අරගන්න", "වෙනවා", "යමන්", "එන්න", "කරන්න", "වරෙන්", "තියන්න", "නිදාගන්න", "ඉන්නවා", "ගේ්න්න", "ගැටගහන්න", "කතාකරන්න", "කියපන්", "කරගන්න", "ලිහන්න", "මනින්න", "වෙන්න", "රවට්ටන්න", "අඬන්න", "හිටපං", "දාපන්", "අහන්න", "හිනාවෙන්න", "අරිනවා", "වාඩිවෙන්න", "උස්සන්න", "දුවන්න", "හරිගස්සන්න", "ගහගන්න", "වදවෙන්න", "අරින්න", "නගිනවා", "නැගිට්ටවන්න", "පලයල්ලා", "ගෙනියන්න", "වෙයල්ලා", "නිදාගනින්", "වාඩිවෙන්න", "වක්කරපන්", "බේරගන්න", "වරෙන්", "දියන්", "වදවෙන්න", "බයවෙන්න", "පලයන්", "පුහුණුවෙන්න", "ඉවසන්න", "ගහන්න", "ඉදපන්", "හිටපන්", "නැඟිටපල්ලා", "දාන්න", "හිටපංකෝ", "බලන්න", "කන්න", "ගනින්", "ඉඩදෙන්න", "පෙන්වන්න", "කරහන්", "අහන්නකෝ", "මැරියන්", "තියාගන්න", "පටන්ගන්න", "දාගන්න", "තියන්න", "කියන්න", "යන්න", "නවතින්න", "නඟින්න", "එකතුවෙන්න", "කරපන්", "එන්නකෝ", "යනවා", "අතදාන්න", "කරගන්න", "අල්ලගන්න", "වෙයන්", "හිතන්න", "විවේකගනින්", "වරෙව්", "නැඟිටපන්", "කඩන්න"};
    private ArrayList<String> dialogueActTagsVector;
    final static String trainingDataFileLocation = System.getProperty("user.dir")+"/Classification/src/datafiles/train.txt";
    final static String testingDataFileLocation = System.getProperty("user.dir")+"/Classification/src/datafiles/test_input.txt";

    // Testing the FeatureSetAll constructor
    @Before
    public void BeforeAttributesVector(){
        fsa = new FeatureSetAll();

        File test_file = new File(System.getProperty("user.dir")+"/Classification/src/datafiles/test_input.txt");
        try {
            PrintWriter pw = new PrintWriter(test_file, "UTF-8");
            pw.println("ඔයා මට ඇත්තෙන්ම කියන්නේ ඔයා නැතුව හරි ජීවත්වෙන්න කියලද?###Yes-No Question");
            pw.close();
        }
        catch (FileNotFoundException ex1){
            ex1.printStackTrace();
        }
        catch (UnsupportedEncodingException ex2){
            ex2.printStackTrace();
        }
    }

    @Test
    public void TestAttributesVector(){
        System.out.println("Start Testing the Attribues Vector....");

        assertEquals(fsa.getsegmentLength().name()+ "Attribute Tested...." , "segmentLength", fsa.getsegmentLength().name());
        assertEquals(fsa.getpunctuationMark().name()+ "Attribute Tested....", "punctuation", fsa.getpunctuationMark().name());
        assertEquals(fsa.getpunctuationMark().name()+ "Attribute Tested....", "lastletter", fsa.getlastletter().name());
        assertEquals(fsa.getlastWord().name()+ "Attribute Tested....", "lastWord", fsa.getlastWord().name());
        assertEquals(fsa.getcuephrases().name()+ "Attribute Tested....", "cuephrases", fsa.getcuephrases().name());
        assertEquals(fsa.getverb().name()+ "Attribute Tested....", "verb", fsa.getverb().name());
        assertEquals(fsa.getpreviousDialogueAct().name()+ "Attribute Tested....", "previousDialogueAct", fsa.getpreviousDialogueAct().name());
        assertEquals(fsa.getbowCount().name()+ "Attribute Tested....", "bowCount", fsa.getbowCount().name());
//        assertEquals(fsa.getlineText().name() + "Attribute Tested....", "lineText", fsa.getlineText().name());

        System.out.println("Finished Testing the Attribues Vector....");
    }

    @Test
    public void TestVerbList(){
        System.out.println("Start Testing the Verbs List....");

        assertFalse(verbs==fsa.getVerbs().toArray());

        System.out.println("Finished Testing the Verbs List....");
    }

    @Test
    public void TestDATagList(){
        System.out.println("Start Testing the dialogue acts List....");

        assertTrue(fsa.getFeatureClass().contains("Statement"));
        assertTrue(fsa.getFeatureClass().contains("Backchannel Question"));
        assertTrue(fsa.getFeatureClass().contains("Conventional Opening"));
        assertTrue(fsa.getFeatureClass().contains("Apology"));
        assertTrue(fsa.getFeatureClass().contains("Reject"));
        assertTrue(fsa.getFeatureClass().contains("Conventional Closing"));
        assertTrue(fsa.getFeatureClass().contains("Yes Answers"));
        assertTrue(fsa.getFeatureClass().contains("Expressive"));
        assertTrue(fsa.getFeatureClass().contains("No Answer"));
        assertTrue(fsa.getFeatureClass().contains("Thanking"));
        assertTrue(fsa.getFeatureClass().contains("Opinion"));
        assertTrue(fsa.getFeatureClass().contains("Back-channel/Acknowledge"));
        assertTrue(fsa.getFeatureClass().contains("Yes-No Question"));
        assertTrue(fsa.getFeatureClass().contains("Open Question"));
        assertTrue(fsa.getFeatureClass().contains("Abandoned/Uninterpretable/Other"));
        assertTrue(fsa.getFeatureClass().contains("Request/Command/Order"));

        System.out.println("Finished Testing the dialogue acts List....");
    }

    @Test
    public void TestFeatureVector(){
        System.out.println("Start Testing the Feature Vector....");

        assertTrue(fsa.getFeatureVector().contains(fsa.getsegmentLength()));
        assertTrue(fsa.getFeatureVector().contains(fsa.getpunctuationMark()));
        assertTrue(fsa.getFeatureVector().contains(fsa.getlastletter()));
        assertTrue(fsa.getFeatureVector().contains(fsa.getlastWord()));
        assertTrue(fsa.getFeatureVector().contains(fsa.getcuephrases()));
        assertTrue(fsa.getFeatureVector().contains(fsa.getverb()));
        assertFalse(fsa.getFeatureVector().contains(fsa.getpreviousDialogueAct()));
        assertFalse(fsa.getFeatureVector().contains(fsa.getbowCount()));
        assertTrue(fsa.getFeatureVector().contains(fsa.getlineText()));

        System.out.println("Finished Testing the Feature Vector....");
    }

    @Test
    public void TestInitialTrainingAndTestingSets(){
        System.out.println("Start Testing the Initial Training & Testing Sets....");

        assertTrue(fsa.getTrainingSet().size() == 0);
        assertTrue(fsa.getTestingSet().size() == 0);
        assertEquals(fsa.getFeatureVector().size()-1,fsa.getTrainingSet().classIndex());
        assertEquals(fsa.getFeatureVector().size()-1,fsa.getTestingSet().classIndex());

        System.out.println("Finished Testing the Initial Training & Testing Sets....");
    }

    @Test
    public void TestInitiatingTrainingSet(){
        try {
            fsa.initTrainingSet(System.getProperty("user.dir") + "/Classification/src/datafiles/test_input.txt");
            assertEquals(9,(int)fsa.getTrainingSet().get(0).value(0));
            assertEquals(0,(int)fsa.getTrainingSet().get(0).value(1));
            assertEquals(5,(int)fsa.getTrainingSet().get(0).value(2));
            assertEquals(1,(int)fsa.getTrainingSet().get(0).value(3));
//            assertEquals(257,(int)fsa.getTrainingSet().get(0).value(4));
            assertEquals(1,(int)fsa.getTrainingSet().get(0).value(5));
            assertEquals(4,(int)fsa.getTrainingSet().get(0).value(7));
        }
        catch (FileNotFoundException ex1){
            ex1.printStackTrace();
        }
        catch (IOException ex2){
            ex2.printStackTrace();
        }
    }

    @Test
    public void TestClassify(){
        fsa.classify(trainingDataFileLocation,testingDataFileLocation);

    }

}
