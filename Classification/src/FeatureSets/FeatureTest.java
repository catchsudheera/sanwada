package FeatureSets;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.*;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by sudheera on 10/27/14.
 */
public class FeatureTest {
    final static String trainingDataFileLocation=System.getProperty("user.dir")+"/Classification/src/datafiles/train.txt";
    final static String testingDataFileLocation=System.getProperty("user.dir")+"/Classification/src/datafiles/test.txt";
    final static String outputFileLocation=System.getProperty("user.dir")+"/Classification/src/datafiles/singleUtterance.txt";

    public static void main(String[] args) throws IOException {
//        FeatureSet01 s1=  new FeatureSet01();
//        s1.classify(trainingDataFileLocation,testingDataFileLocation);
//
//        FeatureSet02 s2=  new FeatureSet02();
//        s2.classify(trainingDataFileLocation,testingDataFileLocation);
//
//        FeatureSet03 s3 = new FeatureSet03();
//        s3.classify(trainingDataFileLocation,testingDataFileLocation);
//
//        FeatureSet04 s4 = new FeatureSet04();
//        s4.classify(trainingDataFileLocation,testingDataFileLocation);

//        FeatureSet05 s5 = new FeatureSet05();
//        s5.classify(trainingDataFileLocation,testingDataFileLocation);
//
//        FeatureSetD01 ds3=  new FeatureSetD01();
//        ds3.classify(trainingDataFileLocation,testingDataFileLocation);
//
//        FeatureSetD02 ds4=  new FeatureSetD02();
//        ds4.classify(trainingDataFileLocation,testingDataFileLocation);
//
//        FeatureSetD03 ds5=  new FeatureSetD03();
//        ds5.classify(trainingDataFileLocation,testingDataFileLocation);
//
//        FeatureSetD04 ds6=  new FeatureSetD04();
//        ds6.classify(trainingDataFileLocation,testingDataFileLocation);

        Classifier[] classifiers = new Classifier[3];

        classifiers[0] = new RandomForest();
        classifiers[1] = new SimpleLogistic();
        classifiers[2] = new J48();



        PrintWriter writer = new PrintWriter(outputFileLocation);

        for(Classifier c : classifiers){
//            FeatureSetAll all1=new FeatureSetAll();
//            all1.classify(trainingDataFileLocation,testingDataFileLocation);
            FeatureSetAllCombinations all = new FeatureSetAllCombinations();
            all.classify(trainingDataFileLocation, testingDataFileLocation,false,false,c,writer);
        }


        writer.close();


    }
}
