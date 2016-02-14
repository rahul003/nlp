package nlp.lm;
import nlp.lm.BigramModel;
import java.util.*;
import java.io.*;
import nlp.lm.DataManager;

public class BackwardBigramModel extends BigramModel{
    /*  Sentence: b a
        Forward : P(a|b) = n(ba)/n(b)
        Backward: P(b|a) = n(ba)/n(a)
        To do this we use same counts of n(ba) so same map.
        while calculating prob, change the denominator count to a from b. to do this change foll. functions
        Note  dont need to call these fns for testing. only use probs for a bigram calculated.
        Here bigram is 
    */
    /** Return bigram string as two tokens separated by a newline */
    public String bigram (String posterior, String context) {
        return context + "\n" + posterior;
    }
    
    /** Like sentenceLogProb but excludes predicting end-of-sentence when computing prob */
    public double sentenceLogProb2 (List<String> sentence) {
        double sentenceLogProb = 0;
        String prevToken = "";
        String token = "";
        int i = 0;
        while (i <= sentence.size()) {
            if(i<sentence.size())
                token = sentence.get(i);
            else
                token = "</S>";

            DoubleValue unigramVal = unigramMap.get(token);
            if (unigramVal == null) {
                token = "<UNK>";
                unigramVal = unigramMap.get(token);
            }
            if(i!=0)
            {
                String bigram = bigram(prevToken, token);
                DoubleValue bigramVal = bigramMap.get(bigram);
                
                double logProb = Math.log(interpolatedProb(unigramVal, bigramVal));
                sentenceLogProb += logProb;
                
                // System.out.print(bigramPosterior(bigram));
                // System.out.print(" given ");
                // System.out.println(bigramContext(bigram));
            }
            prevToken = token;
            i++;
        }
        return sentenceLogProb;
    }


    /** Returns vector of probabilities of predicting each token in the sentence
     *  including the end of sentence */
    public double[] sentenceTokenProbs (List<String> sentence) {
        String prevToken = "";
        String token = "";
        // Vector for storing token prediction probs
        double[] tokenProbs = new double[sentence.size() + 1];

        int i = 0;
        while (i <= sentence.size()) {
            if(i<sentence.size())
                token = sentence.get(i);
            else
                token = "</S>";
            DoubleValue unigramVal = unigramMap.get(token);
            if (unigramVal == null) {
                token = "<UNK>";
                unigramVal = unigramMap.get(token);
            }
            if(i!=0)
            {
                String bigram = bigram(prevToken, token);
                DoubleValue bigramVal = bigramMap.get(bigram);
                // Store prediction prob for i'th token
                tokenProbs[i] = interpolatedProb(unigramVal, bigramVal);
                // System.out.print(bigramPosterior(bigram));
                // System.out.print(" given ");
                // System.out.print(bigramContext(bigram));
                // System.out.print(":");
                // System.out.println(tokenProbs[i]);
            }
            prevToken = token;
            i++;
        }
        return tokenProbs;
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