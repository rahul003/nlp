import os, sys
#run in projects folder

#######################inverting#####################
"""
90% brown as the labeled seed data 
wsj0222 as self training
wsj23 test

Increasing the size of the seed set 1000,2000,3000,4000,5000, 7000, 10000, 13000, 17000, 21000 from brown
learning curve of F1 score as a function of number of sentences in the seed set. 

Also plot a baseline by conducting a control experiment without self-training on "unlabeled" wsj data. 
"""
for i in [1000,2000,3000,4000,5000, 7000, 10000, 13000, 17000, 21000]:
	id = "br"+ str(i/1000) + "k_wsj0222_wsj23"
	ar = "time /u/rahulrh/jdk1.8.0_74/bin/java -Dvisualvm.id=1545997980208 -Didea.launcher.port=7532 -Didea.launcher.bin.path=/u/rahulrh/apps/idea-IC-143.2287.1/bin -Dfile.encoding=UTF-8 -classpath /u/rahulrh/jdk1.8.0_74/jre/lib/charsets.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/deploy.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/cldrdata.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/dnsns.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/jaccess.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/jfxrt.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/localedata.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/nashorn.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/sunec.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/sunjce_provider.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/sunpkcs11.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/ext/zipfs.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/javaws.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/jce.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/jfr.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/jfxswt.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/jsse.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/management-agent.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/plugin.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/resources.jar:/u/rahulrh/jdk1.8.0_74/jre/lib/rt.jar:/u/rahulrh/projects/nlp/parse/out/production/parse:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/stanford-parser-3.6.0-javadoc.jar:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/stanford-parser-3.6.0-models.jar:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/ejml-0.23.jar:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/stanford-parser-3.6.0-sources.jar:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/slf4j-simple.jar:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/slf4j-api.jar:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/ejml-0.23-src.zip:/u/rahulrh/projects/nlp/parse/libs/stanford-parser-full-2015-12-09/stanford-parser.jar:/u/rahulrh/apps/idea-IC-143.2287.1/lib/idea_rt.jar com.intellij.rt.execution.application.AppMain DomainAdaption -train brown " + str(i) + " -adapt wsj0222 -test wsj23 "
	ar += "-saveParser "+id+".mdl "
	ar += ">traces/"+id+".txt "
	script = ar
	fname = id+".sh"
	h = open(fname,"w")
	h.write(script)
	h.close()

# """
# Additionally, generate a normal "in-domain" learning curve, where you use the same seed set but test on brown itself without self-training. 
# """
# for i in [1000,2000,3000,4000,5000, 7000, 10000, 13000, 17000, 21000]:
# 	id = "br"+ str(i/1000) + "k_br"
# 	ar = "Arguments = DomainAdaption -train brown " + str(i) + " -test brown "
# 	ar += "-saveParser "+id+".mdl\n"
# 	log = "Log = traces/"+id + ".log\n"
# 	output = "\nOutput = traces/"+id+".out\n"
# 	error = "Error = traces/"+id+".err\n"
# 	script = header + log + ar + output + error + q
# 	fname = id+".sh"
# 	h = open(fname,"w")
# 	h.write(script)
# 	h.close()

# """
# brown 90 train
# wsj selftrain change
# wsj test
# """
# for i in [1000,2000,3000,4000,5000, 7000, 10000, 13000, 16000, 20000, 25000, 30000, 35000]:
# 	id = "br10k_wsj0222"+str(i/1000)+"k_wsj23"
# 	ar = "Arguments = DomainAdaption -train brown 10000 -adapt wsj0222 " + str(i) + " -test wsj23 "
# 	ar += "-saveParser "+id+".mdl \n"
# 	log = "Log = traces/"+id + ".log\n"
# 	output = "\nOutput = traces/"+id+".out\n"
# 	error = "Error = traces/"+id+".err\n"
# 	script = header + log + ar + output + error + q
# 	fname = id+".sh"
# 	h = open(fname,"w")
# 	h.write(script)
# 	h.close()


########################IGNORE

"""
trial for baseline stuff. ignore
"""
# id = "wsj1k_br1k_br1k"
# ar = "Arguments = DomainAdaption -train wsj0222 1000 -adapt brown 1000 -test brown 1000 "
# ar += "-saveParser "+id+".mdl \n"
# log = "Log = traces/"+id + ".log\n"
# output = "Output = traces/"+id+".out\n"
# error = "Error = traces/"+id+".err\n"
# script = header + log + ar + output + error + q
# fname = id+".sh"
# h = open(fname,"w")
# h.write(script)
# h.close()