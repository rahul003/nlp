package nlp.lm;
import java.util.*;
import java.io.*;

import nlp.lm.BigramModel;
import nlp.lm.BackwardBigramModel;
import nlp.lm.DataManager;

/** 
 * @author Rahul Huilgol
*/

public class BidirectionalBigramModel{

    public BackwardBigramModel backward = null;
    public BigramModel forward = null;

    /** Interpolation weight for forward bigram */
    public double lambdaf = 0.45;
    /** Interpolation weight for backward bigram */
    public double lambdab = 0.45;
    /** Interpolation weight for unigram */
    public double lambdau = 0.1;

    /* will be loaded to the same value as in BigramModel*/
    public boolean debug;

    public BidirectionalBigramModel() 
    {
        backward = new BackwardBigramModel();
        forward = new BigramModel();
        /* fetch debug value of BigramModel*/
        debug = forward.debug;
    }

    public void train (List<List<String>> sentences) 
    {
        forward.train(sentences);
        backward.train(sentences);
    }

    /* Compute log probability of sentence given current model 
    does not predict for <s> and </s>
    */
    public double sentenceLogProb (List<String> sentence) 
    {
        // Set start-sentence as initial token
        String prevToken = "<S>"; //for first word, prevToken is this
        String token;
        String nextToken = "</S>"; 
        double sentenceLogProb = 0;
        for(int i=0; i<sentence.size(); i++)
        {
            token = sentence.get(i);
            if(i<sentence.size()-1)
                nextToken = sentence.get(i+1);
            else
                nextToken = "</S>"; //for last word, next token is this

            
            DoubleValue unigramVal = forward.unigramMap.get(token);
            if (unigramVal == null) 
            {
                // If token not in unigram model, treat as <UNK> token
                token = "<UNK>";
                unigramVal = forward.unigramMap.get(token);
            }
            if (backward.unigramMap.get(nextToken) == null) 
            {
                // If nexttoken not in unigram model, treat as <UNK> token
                nextToken = "<UNK>";
            }

            // Get forward bigram prob using context of prevToken
            String bigram = forward.bigram(prevToken, token);
            DoubleValue bigramVal = forward.bigramMap.get(bigram);
            double logProbf = Math.log(forward.interpolatedProb(unigramVal, bigramVal));

            // Get backward bigram prob using context of nextToken
            String bigram2 = backward.bigram(nextToken, token);
            DoubleValue bigramVal2 = backward.bigramMap.get(bigram2);
            double logProbb = Math.log(backward.interpolatedProb(unigramVal, bigramVal2));
            
            //only printed if debug is on
            printBigrams(bigram, bigram2);

            double logProb = Math.log(interpolatedProb(unigramVal, bigramVal, bigramVal2));

            sentenceLogProb += logProb;
            // update previous token and move to next token
            prevToken = token;
        }
        return sentenceLogProb;
    }


    //interpolates
    public double interpolatedProb(DoubleValue unigramVal, DoubleValue forwardVal, DoubleValue backwardVal) 
    {
        double forw=0;
        double backw=0;
        if(forwardVal!=null)
            forw = forwardVal.getValue();
        if(backwardVal!=null)
            backw = backwardVal.getValue();
        return lambdau* unigramVal.getValue() + lambdaf * forw + lambdab * backw;
    }

    public void test (List<List<String>> sentences) 
    {
        double totalLogProb = 0;
        double totalNumTokens = 0;
        for (List<String> sentence : sentences) 
        {
            totalNumTokens += sentence.size();
            double sentenceLogProb = sentenceLogProb(sentence);
            totalLogProb += sentenceLogProb;
            if(debug)
                {
                    System.out.println(sentence.toString());
                    break;
                }
        }
        double perplexity = Math.exp(-totalLogProb / totalNumTokens);
        System.out.println("Word Perplexity = " + perplexity );
    }


    //if debug mode is on, prints bigrams
    public void printBigrams(String fbigram, String bbigram)
    {
        if(!debug)
            return;
        System.out.print(forward.bigramPosterior(fbigram));
        System.out.print(" given ");
        System.out.print(forward.bigramContext(fbigram));
        System.out.print("; ");

        System.out.print(backward.bigramPosterior(bbigram));
        System.out.print(" given ");
        System.out.println(backward.bigramContext(bbigram));
    }

    public static void main(String[] args) throws IOException 
    {
        DataManager data = new DataManager(args);
        //only tests for Word perplexities
        BidirectionalBigramModel model = new BidirectionalBigramModel();
        System.out.println("Training...");
        model.train(data.trainSentences);
        model.test(data.trainSentences);
        System.out.println("Testing...");
        model.test(data.testSentences);
    }
}