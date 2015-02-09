package FeatureSets;

import weka.attributeSelection.InfoGainAttributeEval;
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
 * Created by sudheera on 27/23/14.
 */
public class FeatureSet05 {



    Attribute ClassAttribute,PrevClassAttribute;
    Attribute lastWord;
    ArrayList<String> featureVectorClassValues;
    ArrayList<Attribute> featureVectorAttributes;
    Instances TrainingSet;
    Instances TestingSet;

    Hashtable table;
    int hashval=0;

    public FeatureSet05(){

        table=new Hashtable<String,Integer>();

        lastWord= new Attribute("lastWord");

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
        PrevClassAttribute = new Attribute("prevClass", featureVectorClassValues);

        // Declare the feature vector
        featureVectorAttributes = new ArrayList<Attribute>();

        featureVectorAttributes.add(PrevClassAttribute);
        featureVectorAttributes.add(lastWord);

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
        String prevDA="notSet";
        while ((line = br.readLine()) != null) {

            String[] split = line.split("###");

            String var = split[0].replace("?","");
            var=var.replace("!","");
            var=var.replace(".","");
            var=var.trim();
            String[] words = var.split("\\s+");

            Instance temp = new DenseInstance(3);

            if(!prevDA.equalsIgnoreCase("notSet") && (prevDA.equalsIgnoreCase("Statement")||prevDA.equalsIgnoreCase("Backchannel Question")||prevDA.equalsIgnoreCase("Open Question")||prevDA.equalsIgnoreCase("Yes-No Question"))){
                temp.setValue(featureVectorAttributes.get(0),prevDA);
            }
            prevDA=split[1];

            temp.setValue(featureVectorAttributes.get(1), getHashValue(words[words.length - 1]));

            //class value
            temp.setValue(featureVectorAttributes.get(featureVectorAttributes.size() - 1),split[1]);
            temp.setDataset(TrainingSet);
            TrainingSet.add(temp);
            TrainingSet.setClassIndex(featureVectorAttributes.size() - 1);


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


            Instance temp = new DenseInstance(3);

            temp.setValue(featureVectorAttributes.get(1),getHashValue(words[words.length-1]));

            //class value
            temp.setValue(featureVectorAttributes.get(featureVectorAttributes.size() - 1), split[1]);
            temp.setDataset(TestingSet);
            TestingSet.add(temp);
            TestingSet.setClassIndex(featureVectorAttributes.size() - 1);


        }
        br2.close();

    }



    public void classify(String trainingFile,String testingFile) {

        try {
            initTrainingSet(trainingFile);
            initTestingSet(testingFile);



            // train NaiveBayes
            J48 cModel = new J48();
            cModel.buildClassifier(TrainingSet);
            Instance current;
            double pred=0;
            for (int i = 0; i < TestingSet.numInstances(); i++) {
                current=TestingSet.get(i);
                if(featureVectorClassValues.get((int)pred).equalsIgnoreCase("Statement")||featureVectorClassValues.get((int)pred).equalsIgnoreCase("Backchannel Question")||featureVectorClassValues.get((int)pred).equalsIgnoreCase("Yes-No Question")||featureVectorClassValues.get((int)pred).equalsIgnoreCase("Open Question")){
                    current.setValue(featureVectorAttributes.get(0),featureVectorClassValues.get((int)pred));
                    System.out.println(pred+"  :  "+featureVectorClassValues.get((int)pred));
                    System.out.println(current.toString());
               }
                pred=cModel.classifyInstance(current);

            }




//            J48 cModel = new J48();
//            cModel.setUnpruned(true);
//            cModel.buildClassifier(TrainingSet);

            Evaluation eTest = new Evaluation(TrainingSet);
            eTest.evaluateModel(cModel, TestingSet);


            //print out the results
            System.out.println("=====================================================================");
            System.out.println("Results for "+this.getClass().getSimpleName());
            String strSummary = eTest.toSummaryString();
            System.out.println(strSummary);

            System.out.println("F-measure : "+eTest.weightedFMeasure());
            System.out.println("precision : "+eTest.weightedPrecision());
            System.out.println("recall : "+eTest.weightedRecall());
            System.out.println("=====================================================================");


            InfoGainAttributeEval infoGainAttributeEval = new InfoGainAttributeEval();
            infoGainAttributeEval.buildEvaluator(TrainingSet);

            for (int i = 0; i <featureVectorAttributes.size()-1; i++) {
                double v = infoGainAttributeEval.evaluateAttribute(i);
                System.out.print(featureVectorAttributes.get(i).name()+"\t\t");
                System.out.println(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
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
