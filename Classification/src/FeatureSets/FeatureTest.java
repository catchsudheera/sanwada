package FeatureSets;

/**
 * Created by sudheera on 10/27/14.
 */
public class FeatureTest {
    final static String trainingDataFileLocation=System.getProperty("user.dir")+"/Classification/src/datafiles/train.txt";
    final static String testingDataFileLocation=System.getProperty("user.dir")+"/Classification/src/datafiles/test.txt";

    public static void main(String[] args) {
        FeatureSet01 s1=  new FeatureSet01();
        s1.classify(trainingDataFileLocation,testingDataFileLocation);

        FeatureSet02 s2=  new FeatureSet02();
        s2.classify(trainingDataFileLocation,testingDataFileLocation);




    }
}
