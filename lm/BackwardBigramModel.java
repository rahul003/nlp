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
    // public String bigram (String posterior, String context) {
    //     return context + "\n" + posterior;
    // }
    
    /** Accumulate unigram and bigram counts for this sentence */
    public void trainSentence (List<String> sentence) 
    {
        // First count an end start sentence token
        //note here that prevToken is actually the token to the right of a word, because we are going right to left
        String prevToken = "</S>";
        DoubleValue unigramValue = unigramMap.get("</S>");
        unigramValue.increment();
        tokenCount++;
        // For each token in sentence, accumulate a unigram and bigram count
        for (int i=sentence.size()-1; i>=0; i--)
        {
            String token = sentence.get(i);
            unigramValue = unigramMap.get(token);
            // If this is the first time token is seen then count it
            // as an unkown token (<UNK>) to handle out-of-vocabulary 
            // items in testing
            if (unigramValue == null) 
            {
                // Store token in unigram map with 0 count to indicate that
                // token has been seen but not counted
                unigramMap.put(token, new DoubleValue());
                token = "<UNK>";
                unigramValue = unigramMap.get(token);
            }
            unigramValue.increment();    // Count unigram
            tokenCount++;               // Count token
            // Make bigram string 
            String bigram = bigram(prevToken, token);
            DoubleValue bigramValue = bigramMap.get(bigram);
            if (bigramValue == null) 
            {
                // If previously unseen bigram, then
                // initialize it with a value
                bigramValue = new DoubleValue();
                bigramMap.put(bigram, bigramValue);
            }
            // Count bigram
            bigramValue.increment();
            prevToken = token;
        }
        // Account for end of sentence unigram
        unigramValue = unigramMap.get("<S>");
        unigramValue.increment();
        tokenCount++;
        // Account for end of sentence bigram
        String bigram = bigram(prevToken, "<S>");
        DoubleValue bigramValue = bigramMap.get(bigram);
        if (bigramValue == null) 
        {
            bigramValue = new DoubleValue();
            bigramMap.put(bigram, bigramValue);
        }
        bigramValue.increment();
    }
    //by not having trainsentence left most is made unk

    /* Compute log probability of sentence given current model */
    public double sentenceLogProb (List<String> sentence) 
    {
        // Set start-sentence as initial token
        String prevToken = "</S>";
        String token;
        double sentenceLogProb = 0;
        // Check prediction of each token in sentence
        for (int i=sentence.size()-1; i>=0; i--) 
        {
            token = sentence.get(i);
            DoubleValue unigramVal = unigramMap.get(token);
            if (unigramVal == null) 
            {
               // If token not in unigram model, treat as <UNK> token
                token = "<UNK>";
                unigramVal = unigramMap.get(token);
            }
            String bigram = bigram(prevToken, token);
            printBigram(bigram);
            DoubleValue bigramVal = bigramMap.get(bigram);
            double logProb = Math.log(interpolatedProb(unigramVal, bigramVal));
            sentenceLogProb += logProb;
            prevToken = token;
        }
        // Check prediction of end of sentence token
        DoubleValue unigramVal = unigramMap.get("<S>");
        String bigram = bigram(prevToken, "<S>");
        printBigram(bigram);
        DoubleValue bigramVal = bigramMap.get(bigram);
        double logProb = Math.log(interpolatedProb(unigramVal, bigramVal));
        // Update sentence log prob based on prediction of </S>
        sentenceLogProb += logProb;
        return sentenceLogProb;
    }

    /* Compute log probability of sentence given current model */
    public double sentenceLogProb2 (List<String> sentence) 
    {
        String prevToken = "</S>";
        String token;
        double sentenceLogProb = 0;
        for (int i=sentence.size()-1; i>=0; i--) 
        {
            token = sentence.get(i);
            // System.out.println(token);
            DoubleValue unigramVal = unigramMap.get(token);
            if (unigramVal == null) {
            token = "<UNK>";
            unigramVal = unigramMap.get(token);
            }
            String bigram = bigram(prevToken, token);
            DoubleValue bigramVal = bigramMap.get(bigram);
            double logProb = Math.log(interpolatedProb(unigramVal, bigramVal));
            sentenceLogProb += logProb;
            prevToken = token;
            printBigram(bigram);
        }
        return sentenceLogProb;
    }

    //not implemented. slightly different than BigramModel so disabling access to BigramModel class method
    public double[] sentenceTokenProbs (List<String> sentence)
    {
        throw new UnsupportedOperationException();
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