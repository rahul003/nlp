universe = vanilla
environment = CLASSPATH=mallet-2.0.8RC3/class:path to Mallet/mallet-2.0.8RC3/lib/mallet-deps.jar

Initialdir = ~/projects/
Executable = /usr/bin/java

+Group   = "GRAD"
+Project = "INSTRUCTIONAL"
+ProjectDescription = "CS388 Homework 2"

Log = ~/projects/experiment.log

Notification = complete
Notify_user = h.rahul@utexas.edu

Arguments = cc.mallet.fst.HMMSimpleTagger --train true --model-file hmm.model --training-proportion 0.8 --test lab data/$3.txt

Output = experiment.out
Error  = experiment.err
Queue 1
