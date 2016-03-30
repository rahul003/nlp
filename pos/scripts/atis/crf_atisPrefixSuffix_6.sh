universe = vanilla
environment = CLASSPATH=mallet-2.0.8RC3/class:mallet-2.0.8RC3/lib/mallet-deps.jar

Initialdir = .
Executable = /usr/bin/java

+Group   = "GRAD"
+Project = "INSTRUCTIONAL"
+ProjectDescription = "CS388 Homework 2"
Log = traces/crf_atisPrefixSuffix_6.log
Notification = Always
Notify_user = h.rahul@utexas.edu
Arguments = cc.mallet.fst.SimpleTagger --train true --model-file models/crf_atisPrefixSuffix_6.ml --random-seed 7457 --training-proportion 0.8 --test lab data/atisPrefixSuffix.txt
Output = traces/crf_atisPrefixSuffix_6.out
Error = traces/crf_atisPrefixSuffix_6.err
Queue 1
