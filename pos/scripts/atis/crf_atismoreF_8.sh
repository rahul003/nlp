universe = vanilla
environment = CLASSPATH=mallet-2.0.8RC3/class:mallet-2.0.8RC3/lib/mallet-deps.jar

Initialdir = .
Executable = /usr/bin/java

+Group   = "GRAD"
+Project = "INSTRUCTIONAL"
+ProjectDescription = "CS388 Homework 2"
Log = traces/crf_atismoreF_8.log
Notification = Always
Notify_user = h.rahul@utexas.edu
Arguments = cc.mallet.fst.SimpleTagger --train true --model-file models/crf_atismoreF_8.ml --random-seed 7124 --training-proportion 0.8 --test lab data/atismoreF.txt
Output = traces/crf_atismoreF_8.out
Error = traces/crf_atismoreF_8.err
Queue 1
