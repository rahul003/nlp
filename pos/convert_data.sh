javac DataConverter.java WordFeatures.java
java -cp ../ pos.DataConverter /projects/nlp/penn-treebank3/tagged/pos/atis/ ../../data/atisPrefixSuffix suffixes_shortest
java -cp ../ pos.DataConverter /projects/nlp/penn-treebank3/tagged/pos/wsj/ ../../data/wsjPrefixSuffix suffixes_shortest