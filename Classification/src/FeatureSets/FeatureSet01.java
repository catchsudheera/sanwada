package FeatureSets;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;


/**
 * Created by sudheera on 27/23/14.
 */
public class FeatureSet01 {

    Attribute segmentLength,lastWord;
    Attribute ClassAttribute;
    FastVector fvClassVal;
    FastVector fvWekaAttributes;
    Instances TrainingSet;
    Instances TestingSet;
    Hashtable table;
    int hashval=0;

    public FeatureSet01(){

        table=new Hashtable();

        // Declare numeric attributes
        segmentLength = new Attribute("segmentLength");
        lastWord= new Attribute("lastWord");


        // Declare the class attribute along with its values
        fvClassVal = new FastVector(16);
        fvClassVal.addElement("Statement");
        fvClassVal.addElement("Request/Command/Order");
        fvClassVal.addElement("Abandoned/Uninterpretable/Other");
        fvClassVal.addElement("Open Question");
        fvClassVal.addElement("Yes-No Question");
        fvClassVal.addElement("Back-channel/Acknowledge");
        fvClassVal.addElement("Opinion");
        fvClassVal.addElement("Thanking");
        fvClassVal.addElement("No Answer");
        fvClassVal.addElement("Expressive");
        fvClassVal.addElement("Yes Answers");
        fvClassVal.addElement("Conventional Closing");
        fvClassVal.addElement("Reject");
        fvClassVal.addElement("Apology");
        fvClassVal.addElement("Conventional Opening");
        fvClassVal.addElement("Backchannel Question");

        ClassAttribute = new Attribute("theClass", fvClassVal);

        // Declare the feature vector
        fvWekaAttributes = new FastVector(3);
        fvWekaAttributes.addElement(segmentLength);
        fvWekaAttributes.addElement(lastWord);
        //class
        fvWekaAttributes.addElement(ClassAttribute);


        // Create an empty training set
        TrainingSet = new Instances("Rel", fvWekaAttributes,10);

        // Set class index
        TrainingSet.setClassIndex(fvWekaAttributes.size() - 1);

        // Create an empty testing set
        TestingSet = new Instances("Rel", fvWekaAttributes, 10);
        // Set class index
        TestingSet.setClassIndex(fvWekaAttributes.size() - 1);


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

            Instance temp = new BinarySparseInstance(3);

            temp.setValue((Attribute)fvWekaAttributes.elementAt(0),words.length);
            temp.setValue((Attribute)fvWekaAttributes.elementAt(1),getHashValue(words[words.length-1]));

            //class value
            temp.setValue((Attribute)fvWekaAttributes.elementAt(fvWekaAttributes.size()-1),split[1]);

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


            Instance temp = new BinarySparseInstance(3);
            temp.setValue((Attribute)fvWekaAttributes.elementAt(0),words.length);
            temp.setValue((Attribute)fvWekaAttributes.elementAt(1),getHashValue(words[words.length-1]));

            //class value
            temp.setValue((Attribute) fvWekaAttributes.elementAt(fvWekaAttributes.size() - 1), split[1]);

            TestingSet.add(temp);


        }
        br2.close();

    }



    public void classify(String trainingFile,String testingFile) {

        try {
            initTrainingSet(trainingFile);
            initTestingSet(testingFile);


            Classifier cModel = (Classifier) new J48();
            cModel.buildClassifier(TrainingSet);

            Evaluation eTest = new Evaluation(TrainingSet);
            eTest.evaluateModel(cModel, TestingSet);


            // Print the result à la Weka explorer:
            String strSummary = eTest.toSummaryString();
            System.out.println(strSummary);

            System.out.println(eTest.fMeasure(0));


        } catch (Exception e) {
            e.printStackTrace();
           // System.out.println(e.getStackTrace());
        }

    }

    int getHashValue(String word){

        if(table.containsKey(word)){
            return (Integer)table.get(word);
        }else{
            table.put(word,hashval++);
            return getHashValue(word);
        }

    }


}
