javac DataConverter.java WordFeatures.java
java -cp ../ pos.DataConverter /projects/nlp/penn-treebank3/tagged/pos/atis/ ../../data/atisMax suffixes.txt
java -cp ../ pos.DataConverter /projects/nlp/penn-treebank3/tagged/pos/wsj/ ../../data/wsjMax suffixes.txt