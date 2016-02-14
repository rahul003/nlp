package nlp.lm;
import nlp.lm.BigramModel;
import java.util.*;
import java.io.*;
import nlp.lm.DataManager;

public class BackwardBigramModel extends BigramModel{

    /** Return bigram string as two tokens separated by a newline, reverse order */
    public String bigram (String nextToken, String token) {
        return token + "\n" + nextToken;
    }

    public static void main(String[] args) throws IOException {
        DataManager data = new DataManager(args);
        System.out.println("Backward BigramModel");
        BackwardBigramModel model = new BackwardBigramModel();
        System.out.println("Training...");
        model.train(data.trainSentences);
        // Test on training data using test and test2
        model.test(data.trainSentences);
        model.test2(data.trainSentences);
        System.out.println("Testing...");
        // Test on test data using test and test2
        model.test(data.testSentences);
        model.test2(data.testSentences);
        System.out.println("----------------------------------");
        System.out.println("----------------------------------");

    }

}