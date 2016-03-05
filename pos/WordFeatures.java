package pos;

import java.io.*;
import java.nio.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

public class WordFeatures{

	private HashSet<String> suffixes = new HashSet<String>();
	private HashSet<String> prefixes = new HashSet<String>();

	public WordFeatures(String suffixes_path) 
	{
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(suffixes_path));
		    // StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        suffixes.add(line.trim().replaceAll("\n ", ""));
		        line = br.readLine();
		    }
		    br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader("prefixes_shortest"));
		    // StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        suffixes.add(line.trim().replaceAll("\n ", ""));
		        line = br.readLine();
		    }
		    br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean isLeadingDigit(final char c){
    	return (c >= '0' && c <= '9');
	}

	public String getFeatures(String word)
	{
		List<String> feats = new ArrayList<String>();
		String rval = "";

		if(Character.isUpperCase(word.charAt(0)))
			feats.add("caps");

		Iterator<String> it = suffixes.iterator();
     	while(it.hasNext()){
        	String suf = it.next();
        	if(word.endsWith(suf))
        	{
        		feats.add(suf);
				// rval += suf + " ";
        	}
     	}

     	Iterator<String> it2 = prefixes.iterator();
     	while(it2.hasNext()){
        	String pref = it2.next();
        	if(word.startsWith(pref))
        	{
        		feats.add(pref);
				// rval += suf + " ";
        	}
     	}

		if(word.contains("-"))
			{
				// rval += " hyph ";
				feats.add("hyph");
			}

		if(isLeadingDigit(word.charAt(0))){
			feats.add("numeroalpha");
			// rval += " numeroalpha ";
		}

		for(String s: feats)
		{
			rval+=s+" ";
		}

		return rval;
	}
}