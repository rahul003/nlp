import edu.stanford.nlp.parser.lexparser.Options;
import edu.stanford.nlp.trees.MemoryTreebank;
import edu.stanford.nlp.trees.Treebank;
import edu.stanford.nlp.util.Timing;

import java.io.*;

public class DataPreProcessor {

    public static MemoryTreebank getTreebank(String name, int num_sentences, String type){
        if(name.equalsIgnoreCase("brown")){
            if(type.equals("test")){
                return getBrown10(num_sentences);
            }
            else{
                return getBrown90(num_sentences);
            }
        }
        else if(name.equalsIgnoreCase("wsj")){
            return getWSJTreeBank(num_sentences);
        }
        else if(name.equalsIgnoreCase("wsj23")){
                return getWSJSection("23");
        }
        return null;
    }

    public static MemoryTreebank getWSJTreeBank(int numSentences)
    {
        MemoryTreebank m = new MemoryTreebank();
        String basepath = "data/wsj/";

        File sections = new File(basepath);
        File[] listOfSections = sections.listFiles();
        for (File section : listOfSections) {
            if(section.getName().equals("00") ||section.getName().equals("01") || section.getName().equals("23") || section.getName().equals("24"))
                continue;

            if (!section.isFile()) {
                File[] listOfDataFiles = section.listFiles();
                for(File f: listOfDataFiles){
                    m.loadPath(f);
                    if(m.size()>=numSentences)
                        break;
                }
            }
            if(m.size()>=numSentences)
                break;
        }
        while(m.size()>numSentences){
            m.remove(m.size()-1);
        }
        return m;
    }

    public static MemoryTreebank getWSJSection(String sectionNum, int numSentences) {
        MemoryTreebank m = new MemoryTreebank();
        String basepath = "data/wsj/";

        File section = new File(basepath+sectionNum);
        File[] listOfDataFiles = section.listFiles();
        for(File f: listOfDataFiles){
            m.loadPath(f);
        }
        while(m.size()>numSentences){
            m.remove(m.size()-1);
        }
        return m;
    }

    public static MemoryTreebank getWSJSection(String sectionNum){
        return getWSJSection(sectionNum, Integer.MAX_VALUE);
    }
    public static void convertBrown() throws FileNotFoundException {
        MemoryTreebank train = new MemoryTreebank();
        MemoryTreebank test = new MemoryTreebank();
        String path = "data/brown/";
        File genres = new File(path);
        File[] listOfGenres = genres.listFiles();
        for(File g: listOfGenres){
            File[] listOfDataFiles = g.listFiles();
            for(File f: listOfDataFiles){
                train.loadPath(f);
            }
            double fraction = 0.9 * train.size();
            while(train.size()>fraction){
                test.add(train.get(train.size()-1));
                train.remove(train.size()-1);
            }
        }

        PrintWriter out = new PrintWriter("data/brown_90.txt");
        out.println(train.toString());
        out.close();

        out = new PrintWriter("data/brown_10.txt");
        out.println(test.toString());
        out.close();
    }

    public static Treebank makeTreebank(String treebankPath, Options op, FileFilter filt) {
        System.err.println("Training a parser from treebank dir: " + treebankPath);
        Treebank trainTreebank = op.tlpParams.diskTreebank();
        System.err.print("Reading trees...");
        if (filt == null) {
            trainTreebank.loadPath(treebankPath);
        } else {
            trainTreebank.loadPath(treebankPath, filt);
        }
        Timing.tick("done [read " + trainTreebank.size() + " trees].");
        return trainTreebank;
    }

    public static MemoryTreebank getBrown90(){
        MemoryTreebank m = new MemoryTreebank();
        m.loadPath("data/brown_90.txt");
        return m;
    }

    public static MemoryTreebank getBrown10(){
        MemoryTreebank m = new MemoryTreebank();
        m.loadPath("data/brown_10.txt");
        return m;
    }

    public static MemoryTreebank getBrown90(int num_sentences){
        MemoryTreebank m = getBrown90();
        while(m.size()>num_sentences){
            m.remove(m.size()-1);
        }
        return m;
    }
    public static MemoryTreebank getBrown10(int num_sentences){
        MemoryTreebank m = getBrown10();
        while(m.size()>num_sentences){
            m.remove(m.size()-1);
        }
        return m;
    }
    //    public static Treebank getBrown90(){
//        Options op = new Options();
//        op.doDep = false;
//        op.doPCFG = true;
//        op.setOptions("-goodPCFG", "-evals", "tsv");
//        Treebank brown90 = makeTreebank("data/brown_90.txt",op,null);
//        return brown90;
//    }

//    public static Treebank getBrown10(){
//        Options op = new Options();
//        op.doDep = false;
//        op.doPCFG = true;
//        op.setOptions("-goodPCFG", "-evals", "tsv");
//        Treebank brown10 = makeTreebank("data/brown_10.txt",op,null);
//        return brown10;
//    }

    public static void main(String[] args) throws IOException
    {
        Options op = new Options();
        op.doDep = false;
        op.doPCFG = true;
        op.setOptions("-goodPCFG", "-evals", "tsv");

//        Treebank t = makeTreebank("data/wsj/02/wsj_0299.mrg",op ,null);
        getWSJTreeBank(1000);
//        convertBrown();
    }



}

