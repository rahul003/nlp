import edu.stanford.nlp.parser.lexparser.EvaluateTreebank;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.Options;
import edu.stanford.nlp.trees.MemoryTreebank;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahulrh on 3/28/16.
 */
public class DomainAdaption {

    LexicalizedParser base_lp = null;
    LexicalizedParser adapted_lp = null;

    MemoryTreebank seed_set = null;
    MemoryTreebank selftrain_set = null;
    MemoryTreebank test_set = null;

    public DomainAdaption(MemoryTreebank seed, MemoryTreebank selftrainingRaw, MemoryTreebank test){
        long startTime = System.currentTimeMillis();
        seed_set = seed;
        seed_set.textualSummary();
        //Suprisingly not doing this causes null excpetion
        test_set = test;
        test_set.textualSummary();

        train();
        create_selftrain_set(selftrainingRaw);
        retrain();

        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }

    public DomainAdaption(String path){
        base_lp = LexicalizedParser.getParserFromSerializedFile("base_"+path);
        adapted_lp = LexicalizedParser.getParserFromSerializedFile("adapted_"+path);
    }

    public void loadTestbank(MemoryTreebank test){
        test_set = test;
        test_set.textualSummary();
    }

    public void saveParsersToSerialized(String filepath){
        base_lp.saveParserToSerialized("base_"+filepath);
        adapted_lp.saveParserToSerialized("adapted_"+filepath);
    }

    private void create_selftrain_set(MemoryTreebank selftraining) {
        System.out.println("creating selftrain");
        selftrain_set = new MemoryTreebank();

        for(int i=0; i<selftraining.size();i++)
        {
//            ParserQuery q = base_lp.parserQuery();
            selftrain_set.add(base_lp.parse(selftraining.get(i).yieldHasWord()));
//            selftraining.get(2).pennPrint();
//            q.getBestParse().pennPrint();
        }
        selftrain_set.textualSummary();
    }

    public void train(){
        System.out.println("train");
        Options op = new Options();
        op.doDep = false;
        op.doPCFG = true;
        op.setOptions("-goodPCFG", "-evals", "tsv");
        op.testOptions.verbose = false;
        base_lp = LexicalizedParser.trainFromTreebank(seed_set,op);
    }

    public void retrain(){
        System.out.println("retrain");
        Options op = new Options();
        op.doDep = false;
        op.doPCFG = true;
        op.setOptions("-goodPCFG", "-evals", "tsv");
        op.testOptions.verbose = false;
        adapted_lp = LexicalizedParser.getParserFromTreebank(seed_set, selftrain_set, 1., null, op,null, null );
    }

    public void testBaseline(){
        testBaseline(null);
    }

    public void testAdapted(){
        testAdapted(null);
    }

    public void testBaseline(BufferedWriter outFile){
        EvaluateTreebank evalBase = new EvaluateTreebank(base_lp);
        String baseline_result = "F-1 score Baseline: "+evalBase.testOnTreebank(test_set);
        Logger.log(outFile, baseline_result);
    }

    public void testAdapted(BufferedWriter outFile){
        EvaluateTreebank eval = new EvaluateTreebank(adapted_lp);
        String result = "F-1 score Adapted: "+eval.testOnTreebank(test_set);
        Logger.log(outFile, result);
    }

    public static void main(String[] args) throws FileNotFoundException {
        boolean training = false;
        String trainTreebankName;
        MemoryTreebank trainBank = null;
        int train_num_sent;

        boolean adapting = false;
        String adaptTreebankName;
        int adapt_num_sent;
        MemoryTreebank adaptBank = null;

        boolean testing = false;
        String testTreebankName;
        int test_num_sent;
        MemoryTreebank testBank = null;

        boolean saveParser = false;
        String saveParserPath="";

        boolean loadParser = false;
        String loadParserPath="";

        boolean saveTestScores = false;
        String saveTestScoresPath="";

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

                if(args[argIndex+2].charAt(0)=='-') {
                    train_num_sent = Integer.MAX_VALUE;
                }
                else {
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
                if(args[argIndex+2].charAt(0)=='-') {
                    adapt_num_sent = Integer.MAX_VALUE;
                }
                else {
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
                if(args[argIndex+2].charAt(0)=='-') {
                    test_num_sent = Integer.MAX_VALUE;
                }
                else {
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
            }
            else if(args[argIndex].equalsIgnoreCase("-testScores")){
                saveTestScores = true;
                saveTestScoresPath = args[argIndex+1];
                argIndex++;
            }
            else if(args[argIndex].equalsIgnoreCase("-help")){
                System.out.println("Indicate argument name (with a leading hyphen), followed by argument value(s)");
                System.out.println("-train trainBankname num_sentences(optional) -adapt adaptBankname num_sentences(optional) -test testBankname num_sentences(optional) -output testresults_filepath");
                System.out.println("Required arguments for mode 1: -train, -adapt, -test .\n Can ignore number if you want to train on the whole treebank");
                System.out.println("Required arguments for mode 2: -loadParser, -test");
                System.out.println("Optional arguments: -testScores, -saveParser, -loadParser");
                System.out.println("Treebanks supported: brown, wsj, wsj23");
            }
            argIndex++;
        }

        BufferedWriter testScoresOut = null;
        if(saveTestScores) {
            Logger.openOutfile(testScoresOut, saveTestScoresPath);
        }

        if(!training){
            if(!testing || !loadParser) {
                System.out.println("Invalid arguments for loading model. Use -help to see help.");
                return;
            }
            DomainAdaption d = new DomainAdaption(loadParserPath);
            d.loadTestbank(testBank);
            d.testBaseline(testScoresOut);
            d.testAdapted(testScoresOut);
        } else {
            if(!adapting || !testing){
                System.out.println("Invalid arguments for creating new model. Use -help to see help.");
                return;
            }
            DomainAdaption dom = new DomainAdaption(trainBank, adaptBank, testBank);
            dom.saveParsersToSerialized(saveParserPath);
            dom.testBaseline(testScoresOut);
            dom.testAdapted(testScoresOut);
        }

        if(testScoresOut!=null) {
            Logger.closeOutfile(testScoresOut);
        }
    }

}
