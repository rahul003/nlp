universe = vanilla
environment = CLASSPATH=mallet-2.0.8RC3/class:mallet-2.0.8RC3/lib/mallet-deps.jar

Initialdir = .
Executable = /usr/bin/java

+Group   = "GRAD"
+Project = "INSTRUCTIONAL"
+ProjectDescription = "CS388 Homework 2"
Log = traces/crf_atisSuff_9.log
Notification = Always
Notify_user = h.rahul@utexas.edu
Arguments = cc.mallet.fst.SimpleTagger --train true --model-file models/crf_atisSuff_9.ml --random-seed 9202 --training-proportion 0.8 --test lab data/atisSuff.txt
Output = traces/crf_atisSuff_9.out
Error = traces/crf_atisSuff_9.err
Queue 1
