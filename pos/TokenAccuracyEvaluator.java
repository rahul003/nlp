/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
   This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
   http://www.cs.umass.edu/~mccallum/mallet
   This software is provided under the terms of the Common Public License,
   version 1.0, as published by http://www.opensource.org.  For further
   information, see the file `LICENSE' included with this distribution. */




/** 
   @author Andrew McCallum <a href="mailto:mccallum@cs.umass.edu">mccallum@cs.umass.edu</a>
 */

package cc.mallet.fst;


import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.Iterator;

import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Sequence;

import cc.mallet.util.MalletLogger;

/**
 * Evaluates a transducer model based on predictions of individual tokens.
 */
public class TokenAccuracyEvaluator extends TransducerEvaluator
{
	private static Logger logger = MalletLogger.getLogger(TokenAccuracyEvaluator.class.getName());

	private HashMap<String,Double> accuracy = new HashMap<String,Double>();
	private static HashSet train_tokens = new HashSet();

	public TokenAccuracyEvaluator (InstanceList[] instanceLists, String[] descriptions) {
		super (instanceLists, descriptions);
	}
	
	public TokenAccuracyEvaluator (InstanceList instanceList1, String description1) {
		this (new InstanceList[] {instanceList1}, new String[] {description1});
	}
	
	public TokenAccuracyEvaluator (InstanceList instanceList1, String description1,
			InstanceList instanceList2, String description2) {
		this (new InstanceList[] {instanceList1, instanceList2}, new String[] {description1, description2});
	}
	
	public TokenAccuracyEvaluator (InstanceList instanceList1, String description1,
			InstanceList instanceList2, String description2,
			InstanceList instanceList3, String description3) {
		this (new InstanceList[] {instanceList1, instanceList2, instanceList3}, new String[] {description1, description2, description3});
	}

	public void evaluateInstanceList (TransducerTrainer trainer, InstanceList instances, String description) 
  {
		int numCorrectTokens;
		int totalTokens;
		int numCorrectOOV;
		int totalOOV;
		Transducer transducer = trainer.getTransducer();
		totalTokens = numCorrectTokens = 0;
		totalOOV = numCorrectOOV = 0;

		if (description=="Training" && train_tokens.isEmpty())
		{
			System.out.println("Caching tokens during training");
			for (int i = 0; i < instances.size(); i++) 
			{
				Instance instance = instances.get(i);
				Sequence input = (Sequence) instance.getData();
				for(int j=0; j<input.size(); j++)
				{		
					train_tokens.add(input.get(j).toString());
				}
			}
		}
		for (int i = 0; i < instances.size(); i++) {
			Instance instance = instances.get(i);
			Sequence input = (Sequence) instance.getData();
			Sequence trueOutput = (Sequence) instance.getTarget();
			assert (input.size() == trueOutput.size());

			Sequence predOutput = transducer.transduce (input);
			assert (predOutput.size() == trueOutput.size());

			for (int j = 0; j < trueOutput.size(); j++) {
				totalTokens++;
				if (trueOutput.get(j).equals(predOutput.get(j)))
					numCorrectTokens++;

				if(description=="Testing")
				{
					if (!train_tokens.contains(input.get(j).toString()))
					{
						totalOOV++;
						if (trueOutput.get(j).equals(predOutput.get(j)))
							numCorrectOOV++;
					}
				}

			}
		}
		double acc = ((double)numCorrectTokens)/totalTokens;
		double oovacc = ((double)numCorrectOOV)/totalOOV;
		double profoovCorr = ((double) numCorrectOOV/totalTokens);
		double profoovTotal = ((double) totalOOV/totalTokens);
		accuracy.put(description, acc);
		logger.info (description +" accuracy="+acc);
		if(description=="Testing")
			logger.info (description +" oov accuracy="+oovacc + " ; profoovCorr:"+profoovCorr + "; profoovTotal: "+profoovTotal);
		//leaving this here because this is what my trace files show. I ran the experiments a bit early. I was confused with what Prof. Mooney wanted from OOV rate, although first metric seemed most logical. I just included couple of other metrics so I dont have to rerun the experiments.
	}

	/**
	 * Returns the accuracy from the last time test() or evaluate() was called
	 * @return
	 */
	public double getAccuracy (String description)
	{
		Double ret = accuracy.get(description);
		if (ret != null)
			return ret.doubleValue();
		throw new IllegalArgumentException ("No accuracy available for instance list \""+description+"\"");
	}
}
