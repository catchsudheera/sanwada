package FeatureSets;

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
import java.util.*;

/**
 * Created by sudheera on 11/2/14.
 */
public class FeatureSet03 {
    private ArrayList<String> bow;
    private ArrayList<String> testingUtterances=new ArrayList<String>();

    private HashMap<String,Integer> Statement;
    private HashMap<String,Integer> RequestCommandOrder;
    private HashMap<String,Integer> AbandonedUninterpretableOther;
    private HashMap<String,Integer> openQuestion;
    private HashMap<String,Integer> YesNoQuestion;
    private HashMap<String,Integer> BackchannelAcknowledge;
    private HashMap<String,Integer> Opinion;
    private HashMap<String,Integer> Thanking;
    private HashMap<String,Integer> NoAnswer;
    private HashMap<String,Integer> Expressive;
    private HashMap<String,Integer> YesAnswers;
    private HashMap<String,Integer> ConventionalClosing;
    private HashMap<String,Integer> Reject;
    private HashMap<String,Integer> Apology;
    private HashMap<String,Integer> ConventionalOpening;
    private HashMap<String,Integer> BackchannelQuestion;

    Attribute segmentLength,lastWord,punctuationMark,bowCount;
    Attribute ClassAttribute;
    ArrayList<String> featureVectorClassValues;
    ArrayList<Attribute> featureVectorAttributes;
    Instances TrainingSet;
    Instances TestingSet;
    Hashtable table;
    int hashval=0;

    public FeatureSet03(){

        table=new Hashtable<String,Integer>();

        // Declare numeric attributes
        segmentLength = new Attribute("segmentLength");
        lastWord= new Attribute("lastWord");
        punctuationMark = new Attribute("punctuation");
        bowCount = new Attribute("bowCount");


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

        ClassAttribute = new Attribute("theClass", featureVectorClassValues);

        // Declare the feature vector
        featureVectorAttributes = new ArrayList<Attribute>();
        featureVectorAttributes.add(segmentLength);
        featureVectorAttributes.add(lastWord);
        featureVectorAttributes.add(punctuationMark);
        featureVectorAttributes.add(bowCount);
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
            temp.setValue(featureVectorAttributes.get(3),getBoWValue(words));

            //class value
            temp.setValue(featureVectorAttributes.get(featureVectorAttributes.size() - 1),split[1]);
            temp.setDataset(TrainingSet);
            TrainingSet.add(temp);



        }
        br.close();
    }

    public void initTestingSet(String location) throws IOException {
        BufferedReader br2 = new BufferedReader(new FileReader(new File(location)));
        String line;
        while ((line = br2.readLine()) != null) {

            String[] split = line.split("###");

            testingUtterances.add(split[0]+" : "+split[1]);

            String var = split[0].replace("?","");
            var=var.replace("!","");
            var=var.replace(".","");
            var=var.trim();
            String[] words = var.split("\\s+");


            Instance temp = new DenseInstance(5);
            temp.setValue(featureVectorAttributes.get(0),words.length);
            temp.setValue(featureVectorAttributes.get(1),getHashValue(words[words.length-1]));
            temp.setValue(featureVectorAttributes.get(2),getPunctuationMark(split[0]));
            temp.setValue(featureVectorAttributes.get(3),getBoWValue(words));

            //class value
            temp.setValue(featureVectorAttributes.get(featureVectorAttributes.size() - 1), split[1]);
            temp.setDataset(TestingSet);
            TestingSet.add(temp);


        }
        br2.close();

    }



    public void classify(String trainingFile,String testingFile) {

        try {

            initiateBagOfWords(trainingFile);
            initTrainingSet(trainingFile);

            initiateBagOfWords(testingFile);
            initTestingSet(testingFile);



            J48 cModel = new J48();
            cModel.setUnpruned(true);
            cModel.buildClassifier(TrainingSet);


//            for (int i = 0; i < TestingSet.numInstances(); i++) {
//                double pred = cModel.classifyInstance(TestingSet.instance(i));
//                if (!testingUtterances.get(i).contains(TestingSet.classAttribute().value((int) pred))){
//                    System.out.print(testingUtterances.get(i));
//                    //System.out.print(", actual: " + TestingSet.classAttribute().value((int) TestingSet.instance(i).classValue()));
//                    System.out.println(", predicted: " + TestingSet.classAttribute().value((int) pred));
//                }
//            }



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




    private void initiateBagOfWords(String location) throws IOException {
        bow = new ArrayList<String>();
        Statement = new HashMap<String,Integer>();
        RequestCommandOrder = new HashMap<String,Integer>();
        AbandonedUninterpretableOther = new HashMap<String,Integer>();
        openQuestion = new HashMap<String,Integer>();
        YesNoQuestion = new HashMap<String,Integer>();
        BackchannelAcknowledge = new HashMap<String,Integer>();
        Opinion = new HashMap<String,Integer>();
        Thanking = new HashMap<String,Integer>();
        NoAnswer = new HashMap<String,Integer>();
        Expressive = new HashMap<String,Integer>();
        YesAnswers = new HashMap<String,Integer>();
        ConventionalClosing = new HashMap<String,Integer>();
        Reject = new HashMap<String,Integer>();
        Apology = new HashMap<String,Integer>();
        ConventionalOpening = new HashMap<String,Integer>();
        BackchannelQuestion = new HashMap<String,Integer>();


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
            }else if(split[1].equalsIgnoreCase("Statement")){
                for (int i = 0; i < words.length ; i++) {
                    if(Statement.containsKey(words[i])){
                        int count = Statement.get(words[i]);
                        count++;
                        Statement.put(words[i],count);

                    }else{
                        Statement.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Request/Command/Order")){
                for (int i = 0; i < words.length ; i++) {
                    if(RequestCommandOrder.containsKey(words[i])){
                        int count = RequestCommandOrder.get(words[i]);
                        count++;
                        RequestCommandOrder.put(words[i],count);

                    }else{
                        RequestCommandOrder.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Abandoned/Uninterpretable/Other")){
                for (int i = 0; i < words.length ; i++) {
                    if(AbandonedUninterpretableOther.containsKey(words[i])){
                        int count = AbandonedUninterpretableOther.get(words[i]);
                        count++;
                        AbandonedUninterpretableOther.put(words[i],count);

                    }else{
                        AbandonedUninterpretableOther.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Yes-No Question")){
                for (int i = 0; i < words.length ; i++) {
                    if(YesNoQuestion.containsKey(words[i])){
                        int count = YesNoQuestion.get(words[i]);
                        count++;
                        YesNoQuestion.put(words[i],count);

                    }else{
                        YesNoQuestion.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Back-channel/Acknowledge")){
                for (int i = 0; i < words.length ; i++) {
                    if(BackchannelAcknowledge.containsKey(words[i])){
                        int count = BackchannelAcknowledge.get(words[i]);
                        count++;
                        BackchannelAcknowledge.put(words[i],count);

                    }else{
                        BackchannelAcknowledge.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Opinion")){
                for (int i = 0; i < words.length ; i++) {
                    if(Opinion.containsKey(words[i])){
                        int count = Opinion.get(words[i]);
                        count++;
                        Opinion.put(words[i],count);

                    }else{
                        Opinion.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Thanking")){
                for (int i = 0; i < words.length ; i++) {
                    if(Thanking.containsKey(words[i])){
                        int count = Thanking.get(words[i]);
                        count++;
                        Thanking.put(words[i],count);

                    }else{
                        Thanking.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("No Answer")){
                for (int i = 0; i < words.length ; i++) {
                    if(NoAnswer.containsKey(words[i])){
                        int count = NoAnswer.get(words[i]);
                        count++;
                        NoAnswer.put(words[i],count);

                    }else{
                        NoAnswer.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Expressive")){
                for (int i = 0; i < words.length ; i++) {
                    if(Expressive.containsKey(words[i])){
                        int count = Expressive.get(words[i]);
                        count++;
                        Expressive.put(words[i],count);

                    }else{
                        Expressive.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Yes Answers")){
                for (int i = 0; i < words.length ; i++) {
                    if(YesAnswers.containsKey(words[i])){
                        int count = YesAnswers.get(words[i]);
                        count++;
                        YesAnswers.put(words[i],count);

                    }else{
                        YesAnswers.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Conventional Closing")){
                for (int i = 0; i < words.length ; i++) {
                    if(ConventionalClosing.containsKey(words[i])){
                        int count = ConventionalClosing.get(words[i]);
                        count++;
                        ConventionalClosing.put(words[i],count);

                    }else{
                        ConventionalClosing.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Reject")){
                for (int i = 0; i < words.length ; i++) {
                    if(Reject.containsKey(words[i])){
                        int count = Reject.get(words[i]);
                        count++;
                        Reject.put(words[i],count);

                    }else{
                        Reject.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Apology")){
                for (int i = 0; i < words.length ; i++) {
                    if(Apology.containsKey(words[i])){
                        int count = Apology.get(words[i]);
                        count++;
                        Apology.put(words[i],count);

                    }else{
                        Apology.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Conventional Opening")){
                for (int i = 0; i < words.length ; i++) {
                    if(ConventionalOpening.containsKey(words[i])){
                        int count = ConventionalOpening.get(words[i]);
                        count++;
                        ConventionalOpening.put(words[i],count);

                    }else{
                        ConventionalOpening.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Backchannel Question")){
                for (int i = 0; i < words.length ; i++) {
                    if(BackchannelQuestion.containsKey(words[i])){
                        int count = BackchannelQuestion.get(words[i]);
                        count++;
                        BackchannelQuestion.put(words[i],count);

                    }else{
                        BackchannelQuestion.put(words[i],1);
                    }

                }
            }

        }

        int[] maxVals;
        String[] maxKeys;


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : Statement.entrySet()) {
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


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : RequestCommandOrder.entrySet()) {
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

        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : AbandonedUninterpretableOther.entrySet()) {
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


        maxVals=new int[3];
        maxKeys=new String[3];
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


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : YesNoQuestion.entrySet()) {
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


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : BackchannelAcknowledge.entrySet()) {
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


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : Opinion.entrySet()) {
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


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : Thanking.entrySet()) {
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


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : NoAnswer.entrySet()) {
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


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : Expressive.entrySet()) {
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


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : YesAnswers.entrySet()) {
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


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : ConventionalClosing.entrySet()) {
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


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : Reject.entrySet()) {
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


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : Apology.entrySet()) {
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


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : ConventionalOpening.entrySet()) {
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


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : BackchannelQuestion.entrySet()) {
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

    private int getBoWValue(String[] words) {

        HashMap<Integer,Integer> tagCount= new HashMap<Integer, Integer>();

        for (int i = 0; i < words.length; i++) {
            int tag_id;
            if(bow.contains(words[i])){
                tag_id=bow.indexOf(words[i]);
                if(bow.lastIndexOf(words[i])!=tag_id){
                    tag_id=0;
                }
                tag_id=(int)Math.floor(tag_id/3.0)+1;
            }else{
                tag_id=0;
            }

            if(tag_id!=0 && tagCount.containsKey(tag_id)){
                int count = tagCount.get(tag_id);
                count++;
                tagCount.put(tag_id,count);

            }else if(tag_id!=0){
                tagCount.put(tag_id,1);
            }
        }


        int[] maxVals;
        int[] maxKeys;


        maxVals=new int[3];
        maxKeys=new int[3];
        maxVals[0]=-1;
        maxVals[1]=-1;
        maxVals[2]=-1;
        for(Map.Entry<Integer,Integer> entry : tagCount.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }


        if(maxVals[0]!=maxVals[1]){
            return maxKeys[0];
        }else{
            return (int)(Math.random()*999999937);
        }
    }
}



