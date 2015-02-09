package FeatureSets;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Dammina on 1/31/2015.
 */
public class GetOutPutForWebApp {
    final static String trainingDataFileLocation=System.getProperty("user.dir")+"/Classification/src/datafiles/train.txt";
    final static String testingDataFileLocation=System.getProperty("user.dir")+"/Classification/src/datafiles/test.txt";
    final static String outputFileLocation=System.getProperty("user.dir")+"/Classification/src/datafiles/singleUtterance.txt";

    public static void main(String[] args) throws IOException {

        ClassifySingleUtterance one=new ClassifySingleUtterance();
        Scanner scan=new Scanner(System.in);
//        Writer out;
//        while(true) {
//            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileLocation), "UTF-8"));
//            out.append(scan.nextLine()+"\n");
//
//            out.close();
        String utterance;

        one.setValues(trainingDataFileLocation);
        while(true) {
            utterance = scan.nextLine();
            System.out.println(one.classify(utterance));
        }
//            ArrayList objs = all.classify(trainingDataFileLocation, testingDataFileLocation);
//            Instances TestingSet = (Instances) objs.get(3);
//        }


    }
}
