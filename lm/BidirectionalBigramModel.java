package nlp.lm;
import nlp.lm.BigramModel;
import nlp.lm.BackwardBigramModel;
import java.util.*;
import java.io.*;
import nlp.lm.DataManager;

public class BidirectionalBigramModel extends BigramModel{
    /** Unigram model that maps a token to its unigram probability */
    public BackwardBigramModel backward = null;

    /** Interpolation weight for forward model */
    public double lambdaf = 0.45;
    /** Interpolation weight for backward model */
    public double lambdab = 0.45;
    public double lambdau = 0.1;

    public BidirectionalBigramModel() {
        super();
        backward = new BackwardBigramModel();
    }

    public void train (List<List<String>> sentences) {
        super.train(sentences);
        backward.train(sentences);
    }

    /* Compute log probability of sentence given current model 
    does not predict for <s> and </s>
    */
    public double sentenceLogProb (List<String> sentence) {
        // Set start-sentence as initial token
        String prevToken = "<S>";
        // Maintain total sentence prob as sum of individual token
        // log probs (since adding logs is same as multiplying probs)
        double sentenceLogProb = 0;
        // Check prediction of each token in sentence
        String token, nextToken; 
        for(int i=0; i<sentence.size(); i++)
        {
            // for (String token : sentence) {
            token = sentence.get(i);
            if(i<sentence.size()-1)
                nextToken = sentence.get(i+1);
            else
            {
                nextToken = "</S>";
            }
            
            // Retrieve unigram prob
            DoubleValue unigramVal = unigramMap.get(token);
            if (unigramVal == null) {
            // If token not in unigram model, treat as <UNK> token
                token = "<UNK>";
                unigramVal = unigramMap.get(token);
            }
            
            // Get bigram prob
            String bigram = bigram(prevToken, token);
            DoubleValue bigramVal = bigramMap.get(bigram);
            // Compute log prob of token using interpolated prob of unigram and bigram
            double logProbf = Math.log(interpolatedProb(unigramVal, bigramVal));
            // Add token log prob to sentence log prob

            // Get bigram prob
            String bigram2 = bigram(nextToken, token);
            DoubleValue bigramVal2 = backward.bigramMap.get(bigram2);
            // Compute log prob of token using interpolated prob of unigram and bigram
            double logProbb = Math.log(interpolatedProb(unigramVal, bigramVal2));
            // Add token log prob to sentence log prob
            
            double logProb = interpolatedFinalProb(logProbf, logProbb);

            sentenceLogProb += logProb;
            // update previous token and move to next token
            prevToken = token;
        }
        return sentenceLogProb;
    }

        /* Compute log probability of sentence given current model 
    does not predict for <s> and </s>
    uses unigram once
    */
    public double sentenceLogProb3 (List<String> sentence) {
        // Set start-sentence as initial token
        String prevToken = "<S>";
        // Maintain total sentence prob as sum of individual token
        // log probs (since adding logs is same as multiplying probs)
        double sentenceLogProb = 0;
        // Check prediction of each token in sentence
        String token, nextToken; 
        for(int i=0; i<sentence.size(); i++)
        {
            // for (String token : sentence) {
            token = sentence.get(i);
            if(i<sentence.size()-1)
                nextToken = sentence.get(i+1);
            else
            {
                nextToken = "</S>";
            }
            
            // Retrieve unigram prob
            DoubleValue unigramVal = unigramMap.get(token);
            if (unigramVal == null) {
            // If token not in unigram model, treat as <UNK> token
                token = "<UNK>";
                unigramVal = unigramMap.get(token);
            }
            
            // Get bigram prob
            String bigram = bigram(prevToken, token);
            DoubleValue bigramVal = bigramMap.get(bigram);
            // Compute log prob of token using interpolated prob of unigram and bigram
            // double logProbf = Math.log(interpolatedProb(unigramVal, bigramVal));
            // Add token log prob to sentence log prob

            // Get bigram prob
            String bigram2 = bigram(nextToken, token);
            DoubleValue bigramVal2 = backward.bigramMap.get(bigram2);
            // Compute log prob of token using interpolated prob of unigram and bigram
            // double logProbb = Math.log(interpolatedProb(unigramVal, bigramVal2));
            // Add token log prob to sentence log prob
            
            double logProb = Math.log(interpolatedFinalProb3(unigramVal, bigramVal, bigramVal2));

            sentenceLogProb += logProb;
            // update previous token and move to next token
            prevToken = token;
        }
        return sentenceLogProb;
    }

    /** Like test1 but excludes predicting end-of-sentence when computing perplexity */
    public void test (List<List<String>> sentences) {
        double totalLogProb = 0;
        double totalNumTokens = 0;
        for (List<String> sentence : sentences) {
            totalNumTokens += sentence.size();
            double sentenceLogProb = sentenceLogProb(sentence);
        //      System.out.println(sentenceLogProb + " : " + sentence);
            totalLogProb += sentenceLogProb;
        }
        double perplexity = Math.exp(-totalLogProb / totalNumTokens);
        System.out.println("Double unigram Word Perplexity = " + perplexity );
    }
    /** Like test1 but excludes predicting end-of-sentence when computing perplexity */
    public void test3 (List<List<String>> sentences) {
        double totalLogProb = 0;
        double totalNumTokens = 0;
        for (List<String> sentence : sentences) {
            totalNumTokens += sentence.size();
            double sentenceLogProb = sentenceLogProb3(sentence);
        //      System.out.println(sentenceLogProb + " : " + sentence);
            totalLogProb += sentenceLogProb;
        }
        double perplexity = Math.exp(-totalLogProb / totalNumTokens);
        System.out.println("Word Perplexity = " + perplexity );
    }

    /** Returns vector of probabilities of predicting each token in the sentence
     *  including the end of sentence */
    public double[] sentenceTokenProbs (List<String> sentence) {
    // Set start-sentence as initial token
        String prevToken = "<S>";
        String token, nextToken;
    // Vector for storing token prediction probs
        double[] tokenProbs = new double[sentence.size()];
    // Token counter
    // Compute prob of predicting each token in sentence
        for(int i=0; i<sentence.size(); i++)
        {
            // for (String token : sentence) {
            token = sentence.get(i);
            if(i<sentence.size()-1)
                nextToken = sentence.get(i+1);
            else
            {
                nextToken = "</S>";
                System.out.println("WTF");
            }

            DoubleValue unigramVal = unigramMap.get(token);
            if (unigramVal == null) {
                token = "<UNK>";
                unigramVal = unigramMap.get(token);
            }
            String bigram = bigram(prevToken, token);
            DoubleValue bigramValf = bigramMap.get(bigram);
            
            String bigram2 = bigram(nextToken, token);
            DoubleValue bigramValb = backward.bigramMap.get(bigram2);

            // Store prediction prob for i'th token
            tokenProbs[i] = interpolatedFinalProb(interpolatedProb(unigramVal, bigramValf), interpolatedProb(unigramVal, bigramValb));
            prevToken = token;
        }
        return tokenProbs;
    }

    /** Interpolate bigram prob using bigram and unigram model predictions */    
    public double interpolatedFinalProb(double forwardVal, double backwardVal) {
    // Linearly combine weighted unigram and bigram probs
        return lambdaf * forwardVal + lambdab * backwardVal;
    }
    /** Interpolate bigram prob using bigram and unigram model predictions */    
    public double interpolatedFinalProb3(DoubleValue unigramVal, DoubleValue forwardVal, DoubleValue backwardVal) {
        double forw=0;
        double backw=0;
        if(forwardVal!=null)
            forw = forwardVal.getValue();
        if(backwardVal!=null)
            backw = backwardVal.getValue();
        return lambdau* unigramVal.getValue() + lambdaf * forw + lambdab * backw;
    }

    public static void main(String[] args) throws IOException {
        DataManager data = new DataManager(args);

        System.out.println("----------------------------------\nBidirectionalBigramModel");
        BidirectionalBigramModel model = new BidirectionalBigramModel();
        System.out.println("Training...");
        model.train(data.trainSentences);
        // Test on training data using test and test2
        model.test(data.trainSentences);
        model.test3(data.trainSentences);
        System.out.println("Testing...");
        // Test on test data using test and test2
        model.test(data.testSentences);
        model.test3(data.testSentences);
        System.out.println("----------------------------------\nBackward BigramModel");
        BackwardBigramModel backward = new BackwardBigramModel();
        System.out.println("Training...");
        backward.train(data.trainSentences);
        // Test on training data using test and test2
        backward.test(data.trainSentences);
        backward.test2(data.trainSentences);
        System.out.println("Testing...");
        // Test on test data using test and test2
        backward.test(data.testSentences);
        backward.test2(data.testSentences);

        System.out.println("----------------------------------\nForward BigramModel");
        BigramModel forward = new BigramModel();
        System.out.println("Training...");
        forward.train(data.trainSentences);
        forward.test(data.trainSentences);
        forward.test2(data.trainSentences);
        System.out.println("Testing...");
        forward.test(data.testSentences);
        forward.test2(data.testSentences);
    }
}