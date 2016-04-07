import edu.stanford.nlp.parser.lexparser.Options;
import edu.stanford.nlp.trees.MemoryTreebank;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.Treebank;
import edu.stanford.nlp.util.Timing;

import java.io.*;

public class DataPreProcessor {

    public static Treebank getTreebank(String name, int num_sentences, String type){
    	//this function returns num_sentences of name data
    	//if brown is called, with type=test, brown10 is used
    	//brown is called, with other type, brown90 is used-
        Options op = new Options();
        op.doDep = false;
        op.doPCFG = true;
        op.setOptions("-goodPCFG", "-evals", "tsv");
        Treebank m = op.tlpParams.memoryTreebank();

        if(name.equals("brown")){
            String path = "data/brown/";
            File genres = new File(path);
            File[] listOfGenres = genres.listFiles();
            for(File g: listOfGenres){
            	//if n sentences reqd, fetch n/8 from each genre
                Treebank r;
                if(type.equals("test"))
                    r = getBrown10(g.toString());
                else
                    r = getBrown90(g.toString());
                int n=0;
                for(Tree t: r){
                    m.add(t);
                    n++;
                    if(n==num_sentences/8)
                        break;
                }
            }
        } else{
            Treebank r = getWSJTreebank(name, type);
            int n=0;
            for(Tree t: r){
                m.add(t);
                n++;
                if(n==num_sentences)
                    break;
            }
        }

        return m;
    }

    public static Treebank getWSJTreebank(String name, String type){
        if(name.equalsIgnoreCase("wsj0222")){
            return getWsj0222();
        }
        else if(name.equalsIgnoreCase("wsj23")){
            return getWsj23();
        } else{
            System.out.println("Couldnt load "+name);
            throw new UnsupportedOperationException();
        }
    }

    public static void convertBrown() throws FileNotFoundException {
        Options op = new Options();
        op.doDep = false;
        op.doPCFG = true;
        op.setOptions("-goodPCFG", "-evals", "tsv");
        String path = "data/brown/";
        File genres = new File(path);
        File[] listOfGenres = genres.listFiles();
        for(File g: listOfGenres){
            MemoryTreebank train = op.tlpParams.memoryTreebank();
            MemoryTreebank test = op.tlpParams.memoryTreebank();
            int train_count = 0;
            int test_count = 0;
            File[] listOfDataFiles = g.listFiles();
            for(File f: listOfDataFiles){
                String[] parts = f.toString().split("/");
                String filename = parts[parts.length-1];
                if(filename.equals("ck08.mrg") || filename.equals("cl02.mrg") || filename.equals("cl23.mrg") || filename.equals("cl24.mrg")){
                	//move these problematic trees to test set
                    int num_s = test.size();
                    test.loadPath(f);
                    test_count+=(test.size()-num_s);
                } else {
                    int num_s = train.size();
                    train.loadPath(f);
                    train_count+=(train.size()-num_s);
                }
            }
            int total = train_count + test_count;
            System.out.println(test_count+"/"+total);
            double fraction = 0.9 * total;
            System.out.println("frac "+ fraction);
            while(train_count>fraction){
                test.add(train.get(train.size()-1));
                train.remove(train.size()-1);
                train_count--;
            }
            //one test and train set for each genre.
          	//so from each genre i can pick equal number of sentences
            PrintWriter out = new PrintWriter(g+"/brown_90.txt");
            out.println(train.toString());
            out.close();

            out = new PrintWriter(g+"/brown_10.txt");
            out.println(test.toString());
            out.close();
        }
    }

    public static void collectWSJ() throws FileNotFoundException {
        Options op = new Options();
        op.doDep = false;
        op.doPCFG = true;
        op.setOptions("-goodPCFG", "-evals", "tsv");
        MemoryTreebank wsj0222 = op.tlpParams.memoryTreebank();
        MemoryTreebank wsj23 = op.tlpParams.memoryTreebank();
        String path = "data/wsj/";
        File sections = new File(path);
        File[] listOfSections = sections.listFiles();
        for(File s: listOfSections){
            String[] parts = s.toString().split("/");
            String secname = parts[parts.length-1];
            if(secname.equals("MERGE.LOG") || secname.startsWith("wsj") || secname.equals("00") || secname.equals("01") || secname.equals("24")){
                continue;
            }

            File[] listOfDataFiles = s.listFiles();
            for(File f: listOfDataFiles){
                if(secname.equals("23"))
                    wsj23.loadPath(f);
                else
                    wsj0222.loadPath(f);
            }
        }

       PrintWriter out = new PrintWriter("data/wsj/wsj_0222.txt");
       out.println(wsj0222.toString());
       out.close();

       out = new PrintWriter("data/wsj/wsj_23.txt");
       out.println(wsj23.toString());
       out.close();
    }

    public static Treebank makeTreebank(String treebankPath, Options op, FileFilter filt) {
        System.err.println("Training a parser from treebank dir: " + treebankPath);
        Treebank trainTreebank = op.tlpParams.memoryTreebank();
        System.err.print("Reading trees...");
        if (filt == null) {
            trainTreebank.loadPath(treebankPath);
        } else {
            trainTreebank.loadPath(treebankPath, filt);
        }
        Timing.tick("done [read " + trainTreebank.size() + " trees].");
        return trainTreebank;
    }

    public static Treebank getWsj23(){
        Options op = new Options();
        op.doDep = false;
        op.doPCFG = true;
        op.setOptions("-goodPCFG", "-evals", "tsv");
        Treebank wsj = makeTreebank("data/wsj/wsj_23.txt",op,null);
        return wsj;
    }

    public static Treebank getWsj0222(){
        Options op = new Options();
        op.doDep = false;
        op.doPCFG = true;
        op.setOptions("-goodPCFG", "-evals", "tsv");
        Treebank wsj = makeTreebank("data/wsj/wsj_0222.txt",op,null);
        return wsj;
    }

    public static Treebank getBrown10(String genre){
        Options op = new Options();
        op.doDep = false;
        op.doPCFG = true;
        op.setOptions("-goodPCFG", "-evals", "tsv");
        Treebank brown10 = makeTreebank(genre+"/brown_10.txt",op,null);
        return brown10;
    }

    public static Treebank getBrown90(String genre){
        Options op = new Options();
        op.doDep = false;
        op.doPCFG = true;
        op.setOptions("-goodPCFG", "-evals", "tsv");
        Treebank brown90 = makeTreebank(genre+"/brown_90.txt",op,null);
        return brown90;
    }

    public static void main(String[] args) throws IOException
    {
       convertBrown();
        // collectWSJ();
    }


}

