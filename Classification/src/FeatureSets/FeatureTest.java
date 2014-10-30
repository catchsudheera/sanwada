package FeatureSets;

/**
 * Created by sudheera on 10/27/14.
 */
public class FeatureTest {
    final static String trainingDataFileLocation="/home/sudheera/IdeaProjects/sanwada/Classification/src/datafiles/train.txt";
    final static String testingDataFileLocation="/home/sudheera/IdeaProjects/sanwada/Classification/src/datafiles/test.txt";

    public static void main(String[] args) {
        FeatureSet01 s1=  new FeatureSet01();
        s1.classify(trainingDataFileLocation,testingDataFileLocation);

    }
}
