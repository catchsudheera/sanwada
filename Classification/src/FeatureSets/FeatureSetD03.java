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
import java.util.*;

/**
 * Created by Dammina on 03/11/2014.
 */

public class FeatureSetD03 {
    Attribute segmentLength,lastWord,punctuationMark,lastletter,cuephrases;
    Attribute ClassAttribute;
    ArrayList<String> featureVectorClassValues;
    ArrayList<Attribute> featureVectorAttributes;
    Instances TrainingSet;
    Instances TestingSet;
    Hashtable table;
    int hashval=0;
    int cueval=0;

    public FeatureSetD03(){

        table=new Hashtable<String,Integer>();

        // Declare numeric attributes
        segmentLength = new Attribute("segmentLength");
        lastWord = new Attribute("lastWord");
        punctuationMark = new Attribute("punctuation");
        lastletter = new Attribute("lastletter");
        cuephrases = new Attribute("cuephrases");


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
        featureVectorAttributes.add(lastletter);
        featureVectorAttributes.add(cuephrases);
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
            char lastletter = words[words.length-1].charAt(words[words.length-1].length()-1);

            List wordlist = Arrays.asList(words);

            if(wordlist.contains("ඇත්තෙන්ම")){
                cueval+=1;
            }
            if(wordlist.contains("සහ") || wordlist.contains("හා")){
                cueval+=2;
            }
            if(wordlist.contains("නිසා") || wordlist.contains("හින්ද")){
                cueval+=4;
            }
            if(wordlist.contains("එසේම")){
                cueval+=8;
            }
            if(wordlist.contains("එභෙත්") || wordlist.contains("නමුත්")){
                cueval+=16;
            }
            if(wordlist.contains("වගේ") || wordlist.contains("වැනි") || wordlist.contains("වාගේ")){
                cueval+=32;
            }
            if(wordlist.contains("ඉතින්") || wordlist.contains("එවිට")){
                cueval+=64;
            }
            if(wordlist.contains("හෝ")){
                cueval+=128;
            }
            if(wordlist.contains("හරි")){
                cueval+=256;
            }
            if(wordlist.contains("එනිසා") || wordlist.contains("එබැවින්")){
                cueval+=512;
            }
            Instance temp = new DenseInstance(6);

            temp.setValue(featureVectorAttributes.get(0),words.length);
            temp.setValue(featureVectorAttributes.get(1),getHashValue(words[words.length-1]));
            temp.setValue(featureVectorAttributes.get(2),getPunctuationMark(split[0]));
            if(lastletter=='ද'){
                temp.setValue(featureVectorAttributes.get(3),1);
            }
            else{
                temp.setValue(featureVectorAttributes.get(3),0);
            }
            temp.setValue(featureVectorAttributes.get(4),cueval);
            cueval=0;
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

            String var = split[0].replace("?","");
            var=var.replace("!","");
            var=var.replace(".","");
            var=var.trim();
            String[] words = var.split("\\s+");
            char lastletter = words[words.length-1].charAt(words[words.length-1].length()-1);
            List wordlist = Arrays.asList(words);

            if(wordlist.contains("ඇත්තෙන්ම")){
                cueval+=1;
            }
            if(wordlist.contains("සහ") || wordlist.contains("හා")){
                cueval+=2;
            }
            if(wordlist.contains("නිසා") || wordlist.contains("හින්ද")){
                cueval+=4;
            }
            if(wordlist.contains("එසේම")){
                cueval+=8;
            }
            if(wordlist.contains("එභෙත්") || wordlist.contains("නමුත්")){
                cueval+=16;
            }
            if(wordlist.contains("වගේ") || wordlist.contains("වැනි") || wordlist.contains("වාගේ")){
                cueval+=32;
            }
            if(wordlist.contains("ඉතින්") || wordlist.contains("එවිට")){
                cueval+=64;
            }
            if(wordlist.contains("හෝ")){
                cueval+=128;
            }
            if(wordlist.contains("හරි")){
                cueval+=256;
            }
            if(wordlist.contains("එනිසා") || wordlist.contains("එබැවින්")){
                cueval+=512;
            }
            Instance temp = new DenseInstance(6);

            temp.setValue(featureVectorAttributes.get(0),words.length);
            temp.setValue(featureVectorAttributes.get(1),getHashValue(words[words.length-1]));
            temp.setValue(featureVectorAttributes.get(2),getPunctuationMark(split[0]));
            if(lastletter=='ද'){
                temp.setValue(featureVectorAttributes.get(3),1);
            }
            else{
                temp.setValue(featureVectorAttributes.get(3),0);
            }
            temp.setValue(featureVectorAttributes.get(4),cueval);
            cueval=0;

            //class value
            temp.setValue(featureVectorAttributes.get(featureVectorAttributes.size() - 1), split[1]);
            temp.setDataset(TestingSet);
            TestingSet.add(temp);


        }
        br2.close();

    }



    public void classify(String trainingFile,String testingFile) {

        try {
            initTrainingSet(trainingFile);
            initTestingSet(testingFile);


            J48 cModel = new J48();
            cModel.setUnpruned(true);
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
