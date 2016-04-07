import edu.stanford.nlp.parser.lexparser.EvaluateTreebank;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.Options;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.Treebank;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahulrh on 3/28/16.
 */
public class DomainAdaption {

    LexicalizedParser base_lp = null;
    LexicalizedParser adapted_lp = null;

    Treebank seed_set = null;
    Treebank selftrain_set = null;
    Treebank test_set = null;

    public DomainAdaption(Treebank seed, Treebank selftrainingRaw, Treebank test){
        seed_set = seed;
        test_set = test;
        train();
        create_selftrain_set(selftrainingRaw);
        retrain();
    }

    public DomainAdaption(Treebank seed, Treebank test){
    	//when no self-training
        seed_set = seed;
        test_set = test;
        train();
    }

    public DomainAdaption(String path){
    	//just load parsers for testing further
        base_lp = LexicalizedParser.getParserFromSerializedFile("models/base_"+path);
        adapted_lp = LexicalizedParser.getParserFromSerializedFile("models/adapted_"+path);
    }

    public void loadTestbank(Treebank test){
        test_set = test;
    }

    public void saveParserToSerialized(String filepath){
        base_lp.saveParserToSerialized("models/base_"+filepath);
    }

    public void saveParsersToSerialized(String filepath){
        base_lp.saveParserToSerialized("models/base_"+filepath);
        adapted_lp.saveParserToSerialized("models/adapted_"+filepath);
    }

    private void create_selftrain_set(Treebank selftraining) {
        System.out.println("Creating selftrainset");
        Options op = new Options();
        op.doDep = false;
        op.doPCFG = true;
        op.setOptions("-goodPCFG", "-evals", "tsv");
        selftrain_set  = op.tlpParams.memoryTreebank();
        int c=0;
        for (Tree t: selftraining) {
            if(c==0){
                c++;
                continue;
            }
            Tree parsed = base_lp.parseTree(t.yieldHasWord());
            if(parsed!=null)
                selftrain_set.add(parsed);
        }
    }

    public void train(){
        System.out.println("train");
        Options op = new Options();
        op.doDep = false;
        op.doPCFG = true;
        op.setOptions("-goodPCFG", "-evals", "tsv");
        base_lp = LexicalizedParser.trainFromTreebank(seed_set,op);
    }

    public void retrain(){
        System.out.println("retrain");
        Options op = new Options();
        op.doDep = false;
        op.doPCFG = true;
        op.setOptions("-goodPCFG", "-evals", "tsv");
        adapted_lp = LexicalizedParser.getParserFromTreebank(seed_set, selftrain_set, 1., null, op,null, null );
    }

    public void testBaseline(){
        EvaluateTreebank evalBase = new EvaluateTreebank(base_lp);
        String baseline_result = "F-1 score Baseline: "+evalBase.testOnTreebank(test_set);
        System.out.println(baseline_result);
    }

    public void testAdapted(){
        if(adapted_lp==null) {
            System.out.println("No adapted parser");
            return;
        }
        EvaluateTreebank eval = new EvaluateTreebank(adapted_lp);
        String result = "F-1 score Adapted: "+eval.testOnTreebank(test_set);
        System.out.println(result);

    }

    public static void main(String[] args) throws FileNotFoundException {
    	"""
    	Indicate argument name (with a leading hyphen), followed by argument value(s)
    	-train trainBankname num_sentences(optional) -adapt adaptBankname num_sentences(optional) -test testBankname num_sentences(optional). 
    	Can ignore number if you want to train on the whole treebank
    	
    	Required arguments for mode 1 to create new parser with selftraining: -train, -adapt, -test 
    	Required arguments for mode 2 to create new parser without selftraining: -train, -test 

    	Required arguments for mode 3 to load parser: -loadParser, -test.
    	Load parser takes id of model (in models folder, without 'base' or 'adapted' in prefix of model name. For example models/base_trial.mdl would be given as argument trail.mdl
    	
    	Optional arguments: -saveParser, -loadParser , followed by name of model file
    	Treebanks supported: brown, wsj0222, wsj23
    	"""

        boolean training = false;
        String trainTreebankName;
        Treebank trainBank = null;
        int train_num_sent = Integer.MAX_VALUE;

        boolean adapting = false;
        String adaptTreebankName;
        int adapt_num_sent = Integer.MAX_VALUE;
        Treebank adaptBank = null;

        boolean testing = false;
        String testTreebankName;
        int test_num_sent = Integer.MAX_VALUE;
        Treebank testBank = null;

        boolean saveParser = false;
        String saveParserPath="";

        boolean loadParser = false;
        String loadParserPath="";

        int argIndex = 0;
        if (args.length < 1) {
            System.err.println("Wrong usage. Supported arguments: -train brown 1000 -adapt wsj 1000 -test wsj23. Can ignore number if you want to train on the whole treebank");
            return;
        }
        Options op = new Options();
        List<String> optionArgs = new ArrayList<>();
        while (argIndex < args.length && args[argIndex].charAt(0) == '-') {
            if (args[argIndex].equalsIgnoreCase("-train") ||
                    args[argIndex].equalsIgnoreCase("-trainTreebank")) {
                training = true;
                trainTreebankName = args[argIndex+1];

                if(args[argIndex+2].charAt(0)!='-') {
                    train_num_sent = Integer.parseInt(args[argIndex+2]);
                    argIndex++;
                }
                trainBank = DataPreProcessor.getTreebank(trainTreebankName, train_num_sent, "train");
                argIndex++;
            }
            else if(args[argIndex].equalsIgnoreCase("-adapt") ||
                        args[argIndex].equalsIgnoreCase("-adaptTreebank")){
                adapting = true;
                adaptTreebankName = args[argIndex+1];
                if(args[argIndex+2].charAt(0)!='-') {
                    adapt_num_sent = Integer.parseInt(args[argIndex+2]);
                    argIndex++;
                }
                adaptBank = DataPreProcessor.getTreebank(adaptTreebankName, adapt_num_sent, "adapt");
                argIndex++;
            }
            else if(args[argIndex].equalsIgnoreCase("-test") ||
                    args[argIndex].equalsIgnoreCase("-testTreebank")){
                testing = true;
                testTreebankName = args[argIndex+1];
                if(argIndex+2<args.length && args[argIndex+2].charAt(0)!='-') {
                        test_num_sent = Integer.parseInt(args[argIndex+2]);
                        argIndex++;
                    }
                testBank = DataPreProcessor.getTreebank(testTreebankName, test_num_sent, "test");
                argIndex++;
            }
            else if(args[argIndex].equalsIgnoreCase("-loadParser")){
                loadParser = true;
                loadParserPath = args[argIndex+1];
                argIndex++;
            }
            else if(args[argIndex].equalsIgnoreCase("-saveParser")){
                saveParser = true;
                saveParserPath = args[argIndex+1];
                argIndex++;
            }
            else if(args[argIndex].equalsIgnoreCase("-help")){
                System.out.println("Indicate argument name (with a leading hyphen), followed by argument value(s)");
                System.out.println("-train trainBankname num_sentences(optional) -adapt adaptBankname num_sentences(optional) -test testBankname num_sentences(optional)\n Can ignore number if you want to train on the whole treebank");
                System.out.println("Required arguments for mode 1 to create new parser with selftraining: -train, -adapt, -test ");
                System.out.println("Required arguments for mode 2 to create new parser without selftraining: -train, -test ");
                System.out.println("Required arguments for mode 3 to load parser: -loadParser, -test \n load parser takes id of model (in models folder, without 'base' or 'adapted' in prefix of model name. For example models/base_trial.mdl would be given as argument trail.mdl");
                System.out.println("Optional arguments: -saveParser, -loadParser , followed by name of model file");
                System.out.println("Treebanks supported: brown, wsj0222, wsj23");
            }
            argIndex++;
        }

        if(!training){
            if(!testing || !loadParser) {
                System.out.println("Invalid arguments for loading model. Use -help to see help.");
                return;
            }
            DomainAdaption d = new DomainAdaption(loadParserPath);
            d.loadTestbank(testBank);
            d.testBaseline();
            d.testAdapted();
        } else {
            if(adapting && testing) {
                DomainAdaption dom = new DomainAdaption(trainBank, adaptBank, testBank);
                if(saveParser)
                    dom.saveParsersToSerialized(saveParserPath);
                dom.testBaseline();
                dom.testAdapted();
            } else if(testing) {
                //only testing and training
                DomainAdaption dom = new DomainAdaption(trainBank, testBank);
                if(saveParser)
                    dom.saveParserToSerialized(saveParserPath);
                dom.testBaseline();
            } else {
                    System.out.println("Invalid arguments for creating new model. Use -help to see help.");
                    return;
                }
            }
        }

    }

