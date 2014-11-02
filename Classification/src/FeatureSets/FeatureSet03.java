package FeatureSets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by sudheera on 11/2/14.
 */
public class FeatureSet03 {
    private ArrayList<String> bow = new ArrayList<String>();

    private HashMap<String,Integer> Statement = new HashMap<String,Integer>();
    private HashMap<String,Integer> RequestCommandOrder = new HashMap<String,Integer>();
    private HashMap<String,Integer> AbandonedUninterpretableOther = new HashMap<String,Integer>();
    private HashMap<String,Integer> openQuestion = new HashMap<String,Integer>();
    private HashMap<String,Integer> YesNoQuestion = new HashMap<String,Integer>();
    private HashMap<String,Integer> BackchannelAcknowledge = new HashMap<String,Integer>();
    private HashMap<String,Integer> Opinion = new HashMap<String,Integer>();
    private HashMap<String,Integer> Thanking = new HashMap<String,Integer>();
    private HashMap<String,Integer> NoAnswer = new HashMap<String,Integer>();
    private HashMap<String,Integer> Expressive = new HashMap<String,Integer>();
    private HashMap<String,Integer> YesAnswers = new HashMap<String,Integer>();
    private HashMap<String,Integer> ConventionalClosing = new HashMap<String,Integer>();
    private HashMap<String,Integer> Reject = new HashMap<String,Integer>();
    private HashMap<String,Integer> Apology = new HashMap<String,Integer>();
    private HashMap<String,Integer> ConventionalOpening = new HashMap<String,Integer>();
    private HashMap<String,Integer> BackchannelQuestion = new HashMap<String,Integer>();

    public void initiateBagOfWords(String location) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(new File(location)));
        String line;
        while ((line = br.readLine()) != null) {

            String[] split = line.split("###");

            String var = split[0].replace("?","");
            var=var.replace("!","");
            var=var.replace(".","");
            var=var.trim();
            String[] words = var.split("\\s+");

            if(split[1].equalsIgnoreCase("Open Question")){
                for (int i = 0; i < words.length ; i++) {
                    if(openQuestion.containsKey(words[i])){
                        int count = openQuestion.get(words[i]);
                        count++;
                        openQuestion.put(words[i],count);

                    }else{
                        openQuestion.put(words[i],1);
                    }

                }
            }

        }

        int[] maxVals=new int[3];
        String[] maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : openQuestion.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
        bow.add(maxKeys[2]);

    }
}
