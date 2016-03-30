universe = vanilla
environment = CLASSPATH=mallet-2.0.8RC3/class:mallet-2.0.8RC3/lib/mallet-deps.jar

Initialdir = .
Executable = /usr/bin/java

+Group   = "GRAD"
+Project = "INSTRUCTIONAL"
+ProjectDescription = "CS388 Homework 2"
Log = traces/hmm_atismoreF_6.log
Notification = Always
Notify_user = h.rahul@utexas.edu
Arguments = cc.mallet.fst.HMMSimpleTagger --train true --model-file models/hmm_atismoreF_6.ml --random-seed 338 --training-proportion 0.8 --test lab data/atismoreF.txt
Output = traces/hmm_atismoreF_6.out
Error = traces/hmm_atismoreF_6.err
Queue 1
