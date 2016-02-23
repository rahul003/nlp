// package nlp.pos;

import java.io.*;
import java.nio.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;
import 
/** 
 *
 * @author Rahul Huilgol
 * Based on Ray Mooney's POSTaggedFile class
*/

public class DataConverter {

	public File file = null;
	protected BufferedReader reader = null;
	protected WordFeatures wf = null; 
	public DataConverter(File file, boolean moreFeats, WordFeatures wfeat)
	{
		this.file = file;
		this.moreFeats = moreFeats;
		this.wf = wfeat;
		try {
			this.reader = new BufferedReader(new FileReader(file));
		}
		catch (IOException e) {
			System.out.println("\nCould not open DataConverter: " + file);
			System.exit(1);
		}
	}

	protected String getNextPOSLine() 
	{
		String line = null;
		try {
			do {
			// Read a line from the file
			line = reader.readLine();
			if (line == null) {
				// End of file, no more tokens, return null
				reader.close();
				return null;
			}
			// Sentence boundary indicator
			if (line.startsWith("======="))
				line = "\n";
			// If sentence number indicator for ATIS or comment for Brown, ignore it
			if (line.startsWith("[ @") || line.startsWith("*x*"))
				line = "";
			} while (line.equals(""));
		}
		catch (IOException e) {
			System.out.println("\nCould not read from TextFileDocument: " + file);
			System.exit(1);
		}
		return line;
	}

	/** Take a line from the file and return a list of String tokens in the line */    
	protected List<String> getTokens (String line) 
	{
		List<String> tokenList = new ArrayList<String>();
		line = line.trim();

		// Use a tokenizer to extract token/POS pairs in line, 
		// ignore brackets indicating chunk boundaries
		StringTokenizer tokenizer = new StringTokenizer(line, " []");
		while (tokenizer.hasMoreTokens()) 
		{
			String tokenPos = tokenizer.nextToken();
			tokenList.add(segmentToken(tokenPos));
			// If last token in line has end of sentence tag ".", 
			// add a sentence end token </S>
			if (tokenPos.endsWith("/.") && !tokenizer.hasMoreTokens()) 
			{
				// System.out.println("Happened? "+tokenPos + " " + line);
				tokenList.add("</S>");
			}
		}
		return tokenList;
	}

	/*
	* Convert a 'token/POS' to 'token POS'
	*/
	protected String segmentToken (String tokenPos) 
	{
		// POS tag follows the last slash
		String new_line = "";
		String word;
		int slash = tokenPos.lastIndexOf("/");
		if (slash < 0)      
		{
			System.out.println(tokenPos);
		}
		else
		{
			word = tokenPos.substring(0, slash);
			new_line = word +" ";
			if(this.moreFeats)
			{
				new_line += wf.getFeatures(word)
			}
			new_line += tokenPos.substring(slash+1, tokenPos.length());
		}

		return new_line;
	}

	/** Return a List of sentences each represented as a List of String tokens for 
		the sentences in this file */
	protected List<List<String>> tokenLists() 
	{
		List<List<String>> sentences = new ArrayList<List<String>>();
		List<String> sentence = new ArrayList<String>();
		String line;
		while ((line=getNextPOSLine()) != null) 
		{
			// Newline line indicates new sentence
			if (line.equals("\n")) 
			{
				if (!sentence.isEmpty()) 
				{
					// Save completed sentence
					sentence.add("\n");
					sentences.add(sentence);
					// and start a new sentence
					sentence = new ArrayList<String>();
				}
			}
			else 
			{
				// Get the tokens in the line
				List<String> tokens = getTokens(line);
				if (!tokens.isEmpty()) 
				{
					// If last token is an end-sentence token "</S>"
					if (tokens.get(tokens.size()-1).equals("</S>")) 
					{
						// Then remove it 
						tokens.remove(tokens.size()-1);
						// and add final sentence tokens
						sentence.addAll(tokens);
						sentence.add("\n");
						// Save completed sentence
						sentences.add(sentence);
						// and start a new sentence
						sentence = new ArrayList<String>();
					}
					else 
					{
						// Add the tokens in the line to the current sentence
						sentence.addAll(tokens);
					}
				}
			}
		}
		// File should always end at end of a sentence
		assert(sentence.isEmpty());
		return sentences;
	}

	public static List<List<String>> convertToLineSepTokens(File[] files, Boolean moreFeats, WordFeatures wf) 
	{ 
		List<List<String>> sentences = new ArrayList<List<String>>();
		for (int i = 0; i < files.length; i++) 
		{
			File file = files[i];
			if (!file.isDirectory()) 
			{
				if (!file.getName().contains("CHANGES.LOG"))
					sentences.addAll(new DataConverter(file, moreFeats, wf).tokenLists());
			}
			else 
			{
				File[] dirFiles = file.listFiles();
				sentences.addAll(convertToLineSepTokens(dirFiles, moreFeats, wf));
			}
			
		}          
		return sentences;
	}

	public static void writeToFile(List<List<String>> sentences, String new_filename) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter(new_filename , "UTF-8");
		for(List<String> sentence: sentences)
		{
			for(String word: sentence)
			{
				writer.println(word);
			}
		}
		writer.close();
	}
	
	/*
	args[0] is file/folder to convert
	args[1] is new file location & name
	args[2] is suffix file name
	*/

	public static void coreConvert(String[] args, Boolean moreFeats)
	{
		File[] files = new File[1];
		files[0] = new File(args[0]);
		WordFeatures wf = null;

		String id = "";
		if(moreFeats)
		{
			id = "moreF";
			if(args.length()>2)
			{
				wf = new WordFeatures();
			}
			else
			{
				System.out.println("Suffix file not given. Couldn't create");
				return;
			}
		}

		System.out.println(args[0]);
		List<List<String>> sentences = convertToLineSepTokens(files, True, wf); 
		String new_filename = args[1]+id+".txt";
		writeToFile(sentences, new_filename);

		String[] names = files[0].list();
		for(String name : names)
		{
		    if (new File(files[0]+"/"+name).isDirectory())
		    {
		        System.out.println(files[0]+"/"+name);
				new_filename = args[1] + id + "/"+name+".txt";
				File[] sub_files = new File[1];
				sub_files[0] = new File(files[0]+"/"+name+"/");
		        sentences = convertToLineSepTokens(files, True, wf); 
				writeToFile(sentences, new_filename);
		    }
		}
	}

	public static void main(String[] args) throws IOException 
	{	
		coreConvert(args, false);

		if(args.length()>2)
		{
			coreConvert(args, true);
		}
	}
}


public class WordFeatures{

	private HashSet suffixes = new HashSet();

	public WordFeatures(String suffixes_path)
	{
		BufferedReader br = new BufferedReader(new FileReader(suffixes_path));
		try {
		    // StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        suffixes.add(line.trim().replaceAll("\n ", ""));
		        line = br.readLine();
		    }
		} finally {
		    br.close();
		}
	}

	private boolean isLeadingDigit(final char c){
    	return (c >= '0' && c <= '9');
	}

	public getFeatures(String word)
	{
		String rval = "";
		if(isUpperCase(word.charAt(0)))
			rval += "caps ";

		for(String suf:suffixes)
		{
			if(word.endsWith(suf))
				rval += suf + " ";
		}

		if(word.contains("-"))
			rval += " hyph";

		if(isLeadingDigit(word.charAt(0)))
			rval += " numeroalpha";

		return rval;
	}
}
