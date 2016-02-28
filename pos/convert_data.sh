javac DataConverter.java WordFeatures.java
java -cp ../ pos.DataConverter /projects/nlp/penn-treebank3/tagged/pos/atis/ ../../data/atis suffixes_short
java -cp ../ pos.DataConverter /projects/nlp/penn-treebank3/tagged/pos/wsj/ ../../data/wsj suffixes_short
