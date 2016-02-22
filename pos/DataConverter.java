// package nlp.pos;

import java.io.*;
import java.nio.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

/** 
 *
 * @author Rahul Huilgol
 * Based on Ray Mooney's POSTaggedFile class
*/

public class DataConverter {

	public File file = null;
	protected BufferedReader reader = null;

	public DataConverter(File file) 
	{
		this.file = file;
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
				System.out.println("Happened? "+tokenPos + " " + line);
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
		int slash = tokenPos.lastIndexOf("/");
		if (slash < 0)      
		{
			System.out.println(tokenPos);
		}
		else
			new_line = tokenPos.substring(0,slash) +" "+ tokenPos.substring(slash+1, tokenPos.length());
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
					sentence.add("\n");
					// Save completed sentence
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


	public static List<List<String>> convertToLineSepTokens(File[] files) 
	{ 
		List<List<String>> sentences = new ArrayList<List<String>>();
		for (int i = 0; i < files.length; i++) 
		{
			File file = files[i];
			if (!file.isDirectory()) 
			{
				if (!file.getName().contains("CHANGES.LOG"))
					sentences.addAll(new DataConverter(file).tokenLists());
			}
			else 
			{
				File[] dirFiles = file.listFiles();
				sentences.addAll(convertToLineSepTokens(dirFiles));
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
	
	/** Convert LDC POS tagged files to just lists of tokens for each setences 
	 *  and print them out. */
	public static void main(String[] args) throws IOException 
	{
		File[] files = new File[args.length-1];
		for (int i = 0; i < files.length; i++) 
			files[i] = new File(args[i]);
		String new_filename = args[args.length-1]+".txt";
		List<List<String>> sentences = convertToLineSepTokens(files); 
		writeToFile(sentences, new_filename);
	}
}
