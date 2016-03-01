universe = vanilla
environment = CLASSPATH=mallet-2.0.8RC3/class:mallet-2.0.8RC3/lib/mallet-deps.jar

Initialdir = .
Executable = /usr/bin/java

+Group   = "GRAD"
+Project = "INSTRUCTIONAL"
+ProjectDescription = "CS388 Homework 2"

Log = traces/crf_atis_0.log

Notification = Always
Notify_user = h.rahul@utexas.edu

Arguments = cc.mallet.fst.SimpleTagger --train true --model-file models/crf_atis_0.ml --training-proportion 0.8 --test lab data/wsjShortestBothAll/00.txt data/wsjShortestBothAll/01.txt

Output = traces/crf_atis_0.out
Error  = traces/crf_atis_0.err
Queue 1
