package FeatureSets;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.core.*;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Sudheera on 10/11/2014.
 */
public class FeatureSetAllCombinations {

    Attribute segmentLength,lastWord,punctuationMark,lastletter,cuephrases, verb, previousDialogueAct, bagOfWords, nGram;
    Attribute ClassAttribute;
    ArrayList<String> featureVectorClassValues;
    ArrayList<Attribute> featureVectorAttributes;
    Instances TrainingSet;
    Instances TestingSet;
    Hashtable table;
    int hashval=0;
    int cueval=0;
    Set<String> attributeNameSet = new HashSet<String>();
    ArrayList<Double> accuracyList;
    Map<Double,String> accuracyAttributeMap;
    Map<String,String> attributeFmeasureMAp;
    Map<String,String> attributeRecallMAp;
    Map<String,String> attributePrecisionMAp;

    String verbslist=getVerbList();
    List<String> verbs;
    private int gl_count=1;

    public FeatureSetAllCombinations(){

        table=new Hashtable<String,Integer>();

        // Declare numeric attributes
//        segmentLength = new Attribute("segmentLength");
        lastWord = new Attribute("lastWord");
        punctuationMark = new Attribute("punctuation");
        lastletter = new Attribute("lastletter");
        cuephrases = new Attribute("cuephrases");
//        verb = new Attribute("verb");
//        previousDialogueAct = new Attribute("previousDialogueAct");
        bagOfWords = new Attribute("bagOfWords", (FastVector)null);
        nGram = new Attribute("nGram", (FastVector)null);

        //predefined verbs
        verbs= Arrays.asList(verbslist.split(", "));


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
//        featureVectorAttributes.add(segmentLength);
        featureVectorAttributes.add(lastWord);
        featureVectorAttributes.add(punctuationMark);
        featureVectorAttributes.add(lastletter);
        featureVectorAttributes.add(cuephrases);
//        featureVectorAttributes.add(verb);
        featureVectorAttributes.add(bagOfWords);
        featureVectorAttributes.add(nGram);



        //Adding to attributeNameSet
        for(Attribute att : featureVectorAttributes){
            attributeNameSet.add(att.name());
        }

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

            //cue phrases feature value declaration
            List wordlist = Arrays.asList(words);

            cueval = cue_phrase(wordlist);

            Instance temp = new DenseInstance(9);

//            temp.setValue(featureVectorAttributes.get(0),words.length);
            temp.setValue(featureVectorAttributes.get(0),getHashValue(words[words.length-1]));
            temp.setValue(featureVectorAttributes.get(1),getPunctuationMark(split[0]));
            if(lastletter=='ද'){
                temp.setValue(featureVectorAttributes.get(2),1);
            }
            else{
                temp.setValue(featureVectorAttributes.get(2),0);
            }
            temp.setValue(featureVectorAttributes.get(3),cueval);
            cueval=0;

            //verbs
//            boolean local_flag=false;
//            for(int i=words.length-1;i>=0;i--){
//                if(verbs.contains(words[i])){
//                    temp.setValue(featureVectorAttributes.get(5),1);
//                    local_flag=true;
//                    break;
//                }
//            }
//            if(!local_flag){
//                temp.setValue(featureVectorAttributes.get(5),0);
//            }

            temp.setValue(featureVectorAttributes.get(4),var);
            temp.setValue(featureVectorAttributes.get(5),var);

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

            cueval = cue_phrase(wordlist);

            Instance temp = new DenseInstance(9);

//            temp.setValue(featureVectorAttributes.get(0),words.length);
            temp.setValue(featureVectorAttributes.get(0),getHashValue(words[words.length-1]));
            temp.setValue(featureVectorAttributes.get(1),getPunctuationMark(split[0]));
            if(lastletter=='ද'){
                temp.setValue(featureVectorAttributes.get(2),1);
            }
            else{
                temp.setValue(featureVectorAttributes.get(2),0);
            }
            temp.setValue(featureVectorAttributes.get(3),cueval);
            cueval=0;

            //verbs
//            boolean local_flag=false;
//            for(int i=words.length-1;i>=0;i--){
//                if(verbs.contains(words[i])){
//                    temp.setValue(featureVectorAttributes.get(5),1);
//                    local_flag=true;
//                    break;
//                }
//            }
//            if(!local_flag){
//                temp.setValue(featureVectorAttributes.get(5),0);
//            }

            temp.setValue(featureVectorAttributes.get(4),var);
            temp.setValue(featureVectorAttributes.get(5),var);

            //class value
            temp.setValue(featureVectorAttributes.get(featureVectorAttributes.size() - 1), split[1]);
            temp.setDataset(TestingSet);
            TestingSet.add(temp);


        }
        br2.close();

    }

    public void classify(String trainingFile,String testingFile,Boolean fullDetails,Boolean ErrorPrint,Classifier clas,PrintWriter writer) {


        accuracyList = new ArrayList<Double>();
        accuracyAttributeMap= new HashMap<Double, String>();
        attributeFmeasureMAp = new HashMap<String, String>();
        attributeRecallMAp = new HashMap<String, String>();
        attributePrecisionMAp = new HashMap<String, String>();


        System.out.println("############################################################################################");
        System.out.println("################# Classifier : "+clas.getClass().getName()+"  ################################");
        System.out.println("############################################################################################");

        writer.println("############################################################################################");
        writer.println("################# Classifier : "+clas.getClass().getName()+"  ################################");
        writer.println("############################################################################################");

        try {
            initTrainingSet(trainingFile);
            initTestingSet(testingFile);
            Instances oldTrain=TrainingSet,oldTest=TestingSet;


            Set<Set<String>> sets = powerSet(attributeNameSet);
            for (Set<String> workingSet : sets) {
                if(workingSet.size()==0){
                    continue;
                }

                TrainingSet=oldTrain;
                TestingSet=oldTest;

                int[] rmIndices = new int[attributeNameSet.size()-workingSet.size()];
                int c=0;
                for(String attribute : attributeNameSet ){
                    if(!workingSet.contains(attribute)){
                        rmIndices[c]=TrainingSet.attribute(attribute).index();
                        c++;
                    }
                }

                Remove rm = new Remove();

                rm.setAttributeIndicesArray(rmIndices);
                rm.setInputFormat(TrainingSet);
                
                TrainingSet = Filter.useFilter(TrainingSet, rm);
                TestingSet = Filter.useFilter(TestingSet, rm);

                classifyComb(fullDetails,ErrorPrint,clas);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Results : ");

        writer.println();


        Collections.sort(accuracyList);
        Collections.reverse(accuracyList);
        DecimalFormat df = new DecimalFormat("#.##");
        Double d_old=-0.01;
        ArrayList<Double> printList = new ArrayList<Double>();
        for(Double d : accuracyList){
            if(printList.contains(d)){
                continue;
            }
            printList.add(d);
            String temp=accuracyAttributeMap.get(d);
            for(String s:temp.split("@@@")){
                System.out.println(df.format(d)+"%"+"\t"+s);
                writer.println(df.format(d) + "%" + "\t"+"F->"+attributeFmeasureMAp.get(s)+"\t"+"R->"+attributeRecallMAp.get(s)+"\t"+"P->"+attributePrecisionMAp.get(s) +"\t"+ s);
            }
        }

        writer.flush();
        writer.println();
        writer.println();


    }


    public void classifyComb(Boolean fullDetails,Boolean ErrorPrint,Classifier clas) {

        try {



            if(TrainingSet.attribute("nGram")!=null){

                TrainingSet.setClassIndex(TrainingSet.attribute("theClass").index());
                TestingSet.setClassIndex(TestingSet.attribute("theClass").index());

                // Set the tokenizer
                NGramTokenizer tokenizer = new NGramTokenizer();
                tokenizer.setNGramMinSize(3);
                tokenizer.setNGramMaxSize(3);
                tokenizer.setDelimiters("\\s+");
                // Set the filter
                StringToWordVector filter0 = new StringToWordVector();
                filter0.setTokenizer(tokenizer);
                filter0.setAttributeIndicesArray(new int[]{TrainingSet.attribute("nGram").index()});
                filter0.setAttributeNamePrefix("nGramAttr");
                filter0.setWordsToKeep(6);
                filter0.setDoNotOperateOnPerClassBasis(false);
                filter0.setOutputWordCounts(true);

                filter0.setInputFormat(TrainingSet);

                TrainingSet = Filter.useFilter(TrainingSet, filter0);
                TestingSet = Filter.useFilter(TestingSet, filter0);
            }


            if(TrainingSet.attribute("bagOfWords")!=null){

                TrainingSet.setClassIndex(TrainingSet.attribute("theClass").index());
                TestingSet.setClassIndex(TestingSet.attribute("theClass").index());

                StringToWordVector filter = new StringToWordVector();
                filter.setAttributeIndicesArray(new int[]{TrainingSet.attribute("bagOfWords").index()});
                filter.setAttributeNamePrefix("bagOfWordsAttr");
                filter.setWordsToKeep(6);
                filter.setDoNotOperateOnPerClassBasis(false);
                filter.setTFTransform(true);
                filter.setOutputWordCounts(true);

                filter.setInputFormat(TrainingSet);

                TrainingSet = Filter.useFilter(TrainingSet, filter);
                TestingSet = Filter.useFilter(TestingSet, filter);
            }



            Classifier cModel = clas;
            cModel.buildClassifier(TrainingSet);


            Evaluation eTest = new Evaluation(TrainingSet);
            eTest.evaluateModel(cModel, TestingSet);


            //print out the results
            System.out.println("========================================================================================");
            //System.out.print("Results for ");

            Enumeration<Attribute> attributeEnumeration = TrainingSet.enumerateAttributes();
            String ngram_str="";
            String bow_str="";
            String attLine="[";

            while(attributeEnumeration.hasMoreElements()){
                String attname=attributeEnumeration.nextElement().name();
                if(attname.equalsIgnoreCase("theClass")){
                    continue;
                }
                if(attname.contains("nGramAttr")){
                    ngram_str="ngrams, ";
                    continue;
                }

                if(attname.contains("bagOfWordsAttr")){
                    bow_str="bagOfWords, ";
                    continue;
                }
                attLine+=attname;
                attLine+=", ";
            }
            attLine+=ngram_str;
            attLine+=bow_str;
            attLine = attLine.substring(0, attLine.length() - 2);
            attLine+="]";
            System.out.print(gl_count++ + " Testing for :");
            System.out.println(attLine+"......");

            if(fullDetails){

                String strSummary = eTest.toSummaryString();
                System.out.println(strSummary);

                InfoGainAttributeEval infoGainAttributeEval = new InfoGainAttributeEval();
                infoGainAttributeEval.buildEvaluator(TrainingSet);

                attributeEnumeration = TrainingSet.enumerateAttributes();
                while(attributeEnumeration.hasMoreElements()){
                    Attribute att = attributeEnumeration.nextElement();
                    if(att.name().equalsIgnoreCase("theClass")){
                        continue;
                    }
                    if(att.name().contains("Attr")){
                        break;
                    }
                    double v = infoGainAttributeEval.evaluateAttribute(att.index());
                    System.out.print(att.name()+"\t\t");
                    System.out.println(v);
                }

                System.out.println("------------------------------------------------------------------------------------");
                DecimalFormat df = new DecimalFormat("#.###");
                System.out.println("recall : "+ df.format(eTest.weightedRecall()));
                System.out.println("precision : "+df.format(eTest.weightedPrecision()));
                System.out.println("F-measure : "+df.format(eTest.weightedFMeasure()));
            }else{
                DecimalFormat df = new DecimalFormat("#.###");
                double prece = eTest.pctCorrect();
                //System.out.print(df.format(prece));
                //System.out.println("%");
                accuracyList.add(prece);
                if(accuracyAttributeMap.containsKey(prece)){
                    String temp=accuracyAttributeMap.get(prece);
                    temp+="@@@"+attLine;
                    accuracyAttributeMap.put(prece,temp);
                }else{
                    accuracyAttributeMap.put(prece,attLine);
                }

                attributeFmeasureMAp.put(attLine,df.format(eTest.weightedFMeasure()));
                attributeRecallMAp.put(attLine,df.format(eTest.weightedRecall()));
                attributePrecisionMAp.put(attLine,df.format(eTest.weightedPrecision()));

            }

            System.out.println("========================================================================================");

            if(ErrorPrint){
                printErrors(cModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void printErrors(Classifier cModel) throws Exception {

        HashMap<String,Integer> errorMap = new HashMap<String, Integer>();
        HashMap<Double,String> errorMap2 = new HashMap<Double,String>();
        int total_errors=0;
        for (int i = 0; i < TestingSet.numInstances(); i++) {
            double pred = cModel.classifyInstance(TestingSet.instance(i));
            if (TestingSet.get(i).classValue()!=pred){
                total_errors++;
                String key="actual: " + featureVectorClassValues.get((int)TestingSet.get(i).classValue())+", predicted: " + TestingSet.classAttribute().value((int) pred);
                if(errorMap.containsKey(key)){
                    Integer integer = errorMap.get(key);
                    integer++;
                    errorMap.put(key,integer);
                }else{
                    errorMap.put(key,1);
                }
            }
        }

        ArrayList<Double> list= new ArrayList<Double>();
        for (String s : errorMap.keySet()) {
            double d = errorMap.get(s)/1.0;

            while(errorMap2.containsKey(d)){
                d+=0.01;
            }
            DecimalFormat df = new DecimalFormat("#.##");
            df.format(d);
            list.add(d);
            errorMap2.put(d, s);
        }

        Collections.sort(list);
        Collections.reverse(list);

        for (Object o : list) {
            DecimalFormat df = new DecimalFormat("#.#");
            System.out.println(df.format(Math.floor((Double) o)*100/total_errors)+"%"+"\t\t"+errorMap2.get((Double)o));
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


    public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
        Set<Set<T>> sets = new HashSet<Set<T>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }

    public int cue_phrase(List wordlist){

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

        return 0;

    }

    public String getVerbList(){
        String verblist="හොයන්න, හැරෙනවා, ඉන්නකෝ, යොදාගන්න, නඟිනවා, නමපන්, ජීවත්වෙන්න, එනවා, උගන්නන්න, ඉඩතියන්න, අදින්න, දියල්ලා, ගන්න, දකින්න, දෙන්න, බැහැපං, හිතන්, ඇවිදින්න, හිටහන්, කරන්න පටංගත්තොත්, නැවතියන්, නැඟිටින්න, ඉන්න, දීපන්, එන්න, නැගිටිනවා, කියපං, වරෙල්ලා, පලයං, එහෙනම් ඔයා මාව අස්කරලා දාන්න, දුවපන්, පැදපන්, සංතෝෂවෙයල්ලා, කතාබහකරන්න, ඇඳගන්න, ගොඩඑන්න, දුවපල්ලා, අල්ලන්න, පෙරළන්න, ඇරපන්, බලාගන්න, එවන්න, නවත්වනවා, දීපල්ලා, ගේන්න, බලමු, නගින්න, බහින්න, අස්කරගන්න, කරනවා, දෙනවා, නවත්තන්න, දුක්වෙන්න, යන්නකෝ, මරන්න, යමල්ලා, යවන්න, ඔබන්න, හිටගන්නවා, කියන්නකෝ, තේරුම්ගන්න, කාපන්, වහගන්නවා, අරගන්න, වෙනවා, යමන්, එන්න ඇතුලට, කරන්න, වරෙන්,, තියන්න, නිදාගන්න, ඉන්නවා, ගේ්න්න, ගැටගහන්න, කතාකරන්න, කියපන්, කරගන්න,ලිහන්න, මනින්න, වෙන්න, රවට්ටන්න, අඬන්න, හිටපං, දාපන්, අහන්න, හිනාවෙන්න, අරිනවා, වාඩිවෙන්න, උස්සන්න, දුවන්න, හරිගස්සන්න, ගහගන්න, වෙනවා,වදවෙන්න, අරින්න, නගිනවා, නැගිට්ටවන්න, පලයල්ලා, ගෙනියන්න, වෙයල්ලා, නිදාගනින්, වාඩිවෙන්න,වෙන්න, වක්කරපන්, බේරගන්න, වරෙන්, දියන්, වදවෙන්න, බයවෙන්න, පලයන්, පුහුණුවෙන්න, ඉවසන්න, ගහන්න, ඉදපන්, හිටපන්, නැඟිටපල්ලා, දාන්න, හිටපංකෝ, බලන්න, කන්න, ගනින්, ඉඩදෙන්න, පෙන්වන්න, කරහන්, අහන්නකෝ, මැරියන්, තියාගන්න, පටන්ගන්න, දාගන්න, උඩින් තියන්න, කියන්න, යන්න, නවතින්න, නඟින්න, එකතුවෙන්න, කරපන්, එන්නකෝ, යනවා, අතදාන්න, කරගන්න, අල්ලගන්න, වෙයන්, හිතන්න, විවේකගනින්, වරෙව්, නැඟිටපන්, කඩන්න";
        return verblist;
    }

    public Attribute getsegmentLength(){
        return segmentLength;
    }

    public Attribute getlastWord(){
        return lastWord;
    }

    public Attribute getpunctuationMark(){
        return punctuationMark;
    }

    public Attribute getlastletter(){
        return lastletter;
    }

    public Attribute getcuephrases(){
        return cuephrases;
    }

    public Attribute getlineText(){
        return verb;
    }

    public Attribute getpreviousDialogueAct(){
        return previousDialogueAct;
    }

    public Attribute getverb(){
        return verb;
    }

    public ArrayList<Attribute> getFeatureVector(){
        return featureVectorAttributes;
    }

    public ArrayList<String> getFeatureClass(){
        return featureVectorClassValues;
    }

    public Instances getTrainingSet(){
        return TrainingSet;
    }

    public Instances getTestingSet(){
        return TestingSet;
    }

    public List<String> getVerbs(){
        return verbs;
    }

}