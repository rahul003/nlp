Directory Structure:
	nlp
		lm
			(..source files..)
		traces
			forward.txt
			backward.txt
			bidirectional.txt

traces directory contains output traces for all the models.
lm directory contains source files.

Run following commands in lm directory
Compiling:
	./compile.sh
Running:
	java -cp ../.. nlp.lm.BackwardBigramModel /projects/nlp/penn-treebank3/tagged/pos/atis/ 0.1

Description of source files:
	DataManager.java
		I moved the code for loading of data, removing of POS tags and splitting into test and train (which was in main function of BigramModel.java) to a separate class which all my models can use
	BigramModel.java
		Almost given code. Changed bigram functions, to be more descriptive in terms of context and posterior. Also added debug functions
	BackwardBigramModel.java 
		BackwardBigramModel inherits BigramModel and changes appropriately to condition on next word.
	BidirectionalBigramModel.java
		BidirectionalBigramModel interpolates BigramModel and BackwardBigramModel
	POSTaggedFile.java
		Unchanged from given
	DoubleValue.java
		Unchanged from given
	compile.sh
		Script to compile

