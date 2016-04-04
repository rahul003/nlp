universe = vanilla
environment = CLASSPATH=/u/rahulrh/jdk1.8.0_74/jre/lib/charsets.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/deploy.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/cldrdata.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/dnsns.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/jaccess.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/jfxrt.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/localedata.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/nashorn.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/sunec.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/sunjce_provider.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/sunpkcs11.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/zipfs.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/javaws.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/jce.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/jfr.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/jfxswt.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/jsse.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/management-agent.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/plugin.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/resources.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/rt.jar:/u/rahulrh/projects/nlp/parse/out/production/parse:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/stanford-parser-3.6.0-javadoc.jar:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/stanford-parser-3.6.0-models.jar:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/ejml-0.23.jar:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/stanford-parser-3.6.0-sources.jar:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/slf4j-simple.jar:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/slf4j-api.jar:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/ejml-0.23-src.zip:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/stanford-parser.jar

Initialdir = .
Executable = /usr/bin/java

+Group   = "GRAD"
+Project = "INSTRUCTIONAL"
+ProjectDescription = "CS388 Homework 3"

Notification = Always
Notify_user = h.rahul@utexas.edu


Log = trial.log
Arguments = DomainAdaption -train brown 1000 -adapt wsj0222 -test wsj23 -saveParser trial.mdl
Output = trial.out
Error  = trial.err
Queue 1
