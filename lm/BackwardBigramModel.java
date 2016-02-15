package nlp.lm;
import nlp.lm.BigramModel;
import java.util.*;
import java.io.*;
import nlp.lm.DataManager;

/** 
 * @author Rahul Huilgol
*/

public class BackwardBigramModel extends BigramModel{
    // inherits BigramModel to use its functions, except the ones overridden below

    /*  Sentence: b a
        Forward : P(a|b) = n(ba)/n(b)
        Backward: P(b|a) = n(ba)/n(a)
    */
    /** Return bigram string as two tokens separated by a newline
        Note that the difference from BigramModel is that 
        the arguments treated as posterior and context are interchanged.
        This along with the change in BigramModel which now uses a function bigramContext 
        to fetch context helps us build BackwardBigramModel using the same calcProbs function used in BigramModel

        This means that if earlier we considered a\nb as bigram now we consider b\na.
        We also need to make appropriate changes for begin and end sentence markers
    */
    public String bigram (String posterior, String context) {
        return context + "\n" + posterior;
    }
    
    /** excludes predicting begin-of-sentence when computing prob */
    public double sentenceLogProb2 (List<String> sentence) {
        double sentenceLogProb = 0;
        String prevToken = "";
        String token = "";
        int i = 0;
        while (i <= sentence.size())
        {
            //note one extra iteration for last word to become prevToken
            if(i<sentence.size())
                token = sentence.get(i);
            else
                token = "</S>"; //assigns next token as end sentence if reached end. this is for lastword|</S>

            DoubleValue unigramVal = unigramMap.get(token);
            if (unigramVal == null) {
                token = "<UNK>";
                unigramVal = unigramMap.get(token);
            }
            if(i!=0)
            {
                //i==0 we dont have next token so skipped

                //note that this fetches prevToken|token
                String bigram = bigram(prevToken, token);
                DoubleValue bigramVal = bigramMap.get(bigram);
                
                double logProb = Math.log(interpolatedProb(unigramVal, bigramVal));
                sentenceLogProb += logProb;
                printBigram(bigram);
            }
            prevToken = token;
            i++;
        }
        return sentenceLogProb;
    }


    /** Returns vector of probabilities of predicting each token in the sentence
     *  including the start of sentence */
    public double[] sentenceTokenProbs (List<String> sentence) {
        String prevToken = "";
        String token = "";
        // Vector for storing token prediction probs
        double[] tokenProbs = new double[sentence.size() + 1];

        int i = 0;
        while (i <= sentence.size()) 
        {
            if(i<sentence.size())
                token = sentence.get(i);
            else
                token = "</S>";
            DoubleValue unigramVal = unigramMap.get(token);
            if (unigramVal == null) 
            {
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
    }

}