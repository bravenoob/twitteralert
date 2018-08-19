

package ch.bfh.bigdata.semarbeit.twitteralert.messaging;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.lm.NGramBoundaryLM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Classifier {

    private DynamicLMClassifier<NGramBoundaryLM> classifier;
    private int maxCharNGram = 3;
    private String trainingDataDelimiter;

    public Classifier(String trainingDataDelimiter) {
        this.trainingDataDelimiter = trainingDataDelimiter;
    }

    public Classifier(){
         this("#");
    }

    public void train(File trainingData)  {
        Set<String> categorySet = new HashSet<>();
        List<String[]> annotatedData =  new ArrayList<>();
        fillCategoriesAndAnnotatedData(trainingData, categorySet, annotatedData);
        trainClassifier(categorySet, annotatedData);
    }

    private void trainClassifier(Set<String> categorySet, List<String[]> annotatedData){
        String[] categories = categorySet.toArray(new String[0]);
         classifier = DynamicLMClassifier.createNGramBoundary(categories,maxCharNGram);
        for (String[] row: annotatedData) {
            String actualClassification = row[0];
            String text = row[1];
            Classification classification = new Classification(actualClassification);
            Classified<CharSequence> classified = new Classified<>(text,classification);
            classifier.handle(classified);
        }
    }


    private void fillCategoriesAndAnnotatedData(File trainingData,
                                                Set<String> categorySet,
                                                List<String[]> annotatedData) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(trainingData)))) {

            String line = reader.readLine();
            while (line != null) {
                String[] data = line.split(trainingDataDelimiter);
                categorySet.add(data[0]);
                annotatedData.add(data);
                line = reader.readLine();
            }

        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }


    public String classify(String text){
        return  classifier.classify(text.trim()).bestCategory().toLowerCase();
    }

}
