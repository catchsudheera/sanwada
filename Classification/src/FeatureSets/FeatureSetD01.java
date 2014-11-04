package FeatureSets;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Dammina on 02/11/2014.
 */
public class FeatureSetD01 {
    Attribute segmentLength,lastWord,punctuationMark,previousDialogueAct;
    Attribute ClassAttribute;
    ArrayList<String> featureVectorClassValues;
    ArrayList<Attribute> featureVectorAttributes;
    Instances TrainingSet;
    Instances TestingSet;
    Hashtable table;
//    Hashtable<String,Integer> dialogueacts;
    int hashval=0;
    int previosDialogueAct= 0;

    public FeatureSetD01(){

        table =new Hashtable<String,Integer>();
//        dialogueacts =new Hashtable<String,Integer>();

        // Declare numeric attributes
        segmentLength = new Attribute("segmentLength");
        lastWord= new Attribute("lastWord");
        punctuationMark = new Attribute("punctuation");
        previousDialogueAct = new Attribute("previousDialogueAct");



        // Declare the class attribute along with its values
        featureVectorClassValues=new ArrayList<String>();
        featureVectorClassValues.add("Statement");
        featureVectorClassValues.add("Request/Command/Order");
        featureVectorClassValues.add("Abandoned/Uninterpretable/Other");
        featureVectorClassValues.add("Open Question");
        featureVectorClassValues.add("Yes-No Question");
        featureVectorClassValues.add("Back-channel/Acknowledge");
        featureVectorClassValues.add("Opinion");
        featureVectorClassValues.add("Thanking");
        featureVectorClassValues.add("No Answer");
        featureVectorClassValues.add("Expressive");
        featureVectorClassValues.add("Yes Answers");
        featureVectorClassValues.add("Conventional Closing");
        featureVectorClassValues.add("Reject");
        featureVectorClassValues.add("Apology");
        featureVectorClassValues.add("Conventional Opening");
        featureVectorClassValues.add("Backchannel Question");

        //add values to dialogueacts Hash table
//        dialogueacts.put("Statement",0);
//        dialogueacts.put("Request/Command/Order",0);
//        dialogueacts.put("Abandoned/Uninterpretable/Other",0);
//        dialogueacts.put("Open Question",0);
//        dialogueacts.put("Yes-No Question",3);
//        dialogueacts.put("Back-channel/Acknowledge",0);
//        dialogueacts.put("Opinion",0);
//        dialogueacts.put("Thanking",0);
//        dialogueacts.put("No Answer",4);
//        dialogueacts.put("Yes Answers",4);
//        dialogueacts.put("Expressive",0);
//        dialogueacts.put("Conventional Closing",0);
//        dialogueacts.put("Reject",0);
//        dialogueacts.put("Apology",0);
//        dialogueacts.put("Conventional Opening",0);
//        dialogueacts.put("Backchannel Question",0);

        ClassAttribute = new Attribute("theClass", featureVectorClassValues);

        // Declare the feature vector
        featureVectorAttributes = new ArrayList<Attribute>();
        featureVectorAttributes.add(segmentLength);
        featureVectorAttributes.add(lastWord);
        featureVectorAttributes.add(punctuationMark);
        featureVectorAttributes.add(previousDialogueAct);
        //class
        featureVectorAttributes.add(ClassAttribute);


        // Create an empty training set
        TrainingSet = new Instances("Rel", featureVectorAttributes,10);

        // Set class index
        TrainingSet.setClassIndex(featureVectorAttributes.size() - 1);

        // Create an empty testing set
        TestingSet = new Instances("Rel", featureVectorAttributes, 10);
        // Set class index
        TestingSet.setClassIndex(featureVectorAttributes.size() - 1);


    }

    public void initTrainingSet(String location) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(location)));
        String line;
        while ((line = br.readLine()) != null) {

            String[] split = line.split("###");

            String var = split[0].replace("?","");
            var=var.replace("!","");
            var=var.replace(".","");
            var=var.trim();
            String[] words = var.split("\\s+");

            Instance temp = new DenseInstance(5);

            temp.setValue(featureVectorAttributes.get(0),words.length);
            temp.setValue(featureVectorAttributes.get(1),getHashValue(words[words.length-1]));
            temp.setValue(featureVectorAttributes.get(2),getPunctuationMark(split[0]));

            temp.setValue(featureVectorAttributes.get(3),getPreviosDialogueAct());

            //class value
            temp.setValue(featureVectorAttributes.get(featureVectorAttributes.size() - 1),split[1]);
            temp.setDataset(TrainingSet);
            TrainingSet.add(temp);

            //update previous dialogue act value
            if(split[1].equalsIgnoreCase("Yes-No Question")){
                previosDialogueAct = 1;
            }
//            else if(split[1].equalsIgnoreCase("Open Question")){
//                previosDialogueAct = 2;
//            }
            else{
                previosDialogueAct = (int) (Math.random()*10000000);
//                previosDialogueAct = 0;
            }
            setPreviosDialogueAct(previosDialogueAct);



        }
        setPreviosDialogueAct(0);
        br.close();
    }
    public void initTestingSet(String location) throws IOException {
        BufferedReader br2 = new BufferedReader(new FileReader(new File(location)));
        String line;
        while ((line = br2.readLine()) != null) {

            String[] split = line.split("###");

            String var = split[0].replace("?","");
            var=var.replace("!","");
            var=var.replace(".","");
            var=var.trim();
            String[] words = var.split("\\s+");


            Instance temp = new DenseInstance(5);
            temp.setValue(featureVectorAttributes.get(0),words.length);
            temp.setValue(featureVectorAttributes.get(1),getHashValue(words[words.length-1]));
            temp.setValue(featureVectorAttributes.get(2),getPunctuationMark(split[0]));
            temp.setValue(featureVectorAttributes.get(3),getPreviosDialogueAct());


            //class value
            temp.setValue(featureVectorAttributes.get(featureVectorAttributes.size() - 1), split[1]);
            temp.setDataset(TestingSet);
            TestingSet.add(temp);

            //update previous dialogue act value
            if(split[1].equalsIgnoreCase("Yes-No Question")){
                previosDialogueAct = 1;
            }
            else if(split[1].equalsIgnoreCase("Open Question")){
                previosDialogueAct = 2;
            }
            else{
                  previosDialogueAct = (int) (Math.random()*10000000);
//                previosDialogueAct = 0;
            }
            setPreviosDialogueAct(previosDialogueAct);


        }
        br2.close();

    }



    public void classify(String trainingFile,String testingFile) {

        try {
            initTrainingSet(trainingFile);
            initTestingSet(testingFile);


            Classifier cModel = new J48();
            cModel.buildClassifier(TrainingSet);

            Evaluation eTest = new Evaluation(TrainingSet);
            eTest.evaluateModel(cModel, TestingSet);


            //print out the results
            System.out.println("=====================================================================");
            System.out.println("Results for "+this.getClass().getSimpleName());
            String strSummary = eTest.toSummaryString();
            System.out.println(strSummary);

            System.out.println("F-measure : "+eTest.fMeasure(0));
            System.out.println("=====================================================================");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getPreviosDialogueAct(){
        return previosDialogueAct;
    }
    private void setPreviosDialogueAct(int pda){
        previosDialogueAct=pda;
    }

    private int getHashValue(String word){

        if(table.containsKey(word)){
            return (Integer)table.get(word);
        }else{
            table.put(word,hashval++);
            return getHashValue(word);
        }

    }


    private int getPunctuationMark(String line){

        if(line.contains("?") && !line.contains("!")){
            return 5;
        }
        else if(line.contains("?") && line.contains("!")){
            return 4;
        }else if(line.contains("!")){
            return 3;
        }else if(line.contains(".")){
            return 2;
        }else if(line.contains("\"")||line.contains("\'")||line.contains("`")){
            return 1;
        }else
            return 0;
    }




}
