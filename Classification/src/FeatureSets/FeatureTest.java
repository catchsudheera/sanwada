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

        FeatureSetD01 s3=  new FeatureSetD01();
        s3.classify(trainingDataFileLocation,testingDataFileLocation);

        FeatureSetD02 s4=  new FeatureSetD02();
        s4.classify(trainingDataFileLocation,testingDataFileLocation);

        FeatureSetD03 s5=  new FeatureSetD03();
        s5.classify(trainingDataFileLocation,testingDataFileLocation);

        FeatureSetD04 s6=  new FeatureSetD04();
        s6.classify(trainingDataFileLocation,testingDataFileLocation);


    }
}
