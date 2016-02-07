package nlp.lm;
import nlp.lm.BigramModel;
import nlp.lm.BackwardBigramModel;
import java.util.*;
import java.io.*;

public class DataManager{

    public double testFraction = 0; 
    public List<List<String>> trainSentences = null;
    public List<List<String>> testSentences = null;

    public DataManager(String[] args){
        // All but last arg is a file/directory of LDC tagged input data
        File[] files = new File[args.length - 1];
        for (int i = 0; i < files.length; i++) 
        {
            files[i] = new File(args[i]);
        }
        // Last arg is the TestFrac
        testFraction = Double.valueOf(args[args.length -1]);
        // Get list of sentences from the LDC POS tagged input files
        List<List<String>> sentences =  POSTaggedFile.convertToTokenLists(files);
        int numSentences = sentences.size();
        // Compute number of test sentences based on TestFrac
        int numTest = (int)Math.round(numSentences * testFraction);
        // Take test sentences from end of data
        testSentences = sentences.subList(numSentences - numTest, numSentences);
        // Take training sentences from start of data
        trainSentences = sentences.subList(0, numSentences - numTest);


        System.out.println("# Train Sentences = " + trainSentences.size() + 
         " (# words = " + wordCount(trainSentences) + 
             ") \n# Test Sentences = " + testSentences.size() +
        " (# words = " + wordCount(testSentences) + ")");
        // Create a bigram model and train it.
    }
    public static int wordCount (List<List<String>> sentences) {
        int wordCount = 0;
        for (List<String> sentence : sentences) {
            wordCount += sentence.size();
        }
        return wordCount;
    }
}