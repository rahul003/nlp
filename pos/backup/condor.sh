universe = vanilla
environment = CLASSPATH=mallet-2.0.8RC3/class:mallet-2.0.8RC3/lib/mallet-deps.jar

Initialdir = .
Executable = /usr/bin/java

+Group   = "GRAD"
+Project = "INSTRUCTIONAL"
+ProjectDescription = "CS388 Homework 2"

Log = traces/crf_def_wsj_moreF.log

Notification = Always
Notify_user = h.rahul@utexas.edu

Arguments = cc.mallet.fst.HMMSimpleTagger --train true --model-file models/crf_def_wsj_moreF.ml --training-proportion 0.8 --test lab data/wsjmoreF.txt

Output = traces/crf_def_wsj_moreF.out
Error  = traces/crf_def_wsj_moreF.err
Queue 1
