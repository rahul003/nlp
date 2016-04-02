import os, sys
#run in projects folder
f = open('header_condorscript.txt','r')
header = f.read()
f.close()
q = "Queue 1\n"

"""
WSJ sections 02-22 as the labeled seed data 
Brown corpus as the unlabeled self-training and test sets. 
Increasing the size of the seed set 1,000, 2,000, 3,000, 4,000, 5,000, 7,000, 10,000, 13,000, 16,000, 20,000, 25,000, 30,000, 35,000 from WSJ sections 02-22 
learning curve of F1 score as a function of number of sentences in the seed set. 

Also plot a baseline by conducting a control experiment without self-training on "unlabeled" Brown data. 
"""
# for i in [1000,2000,3000,4000,5000, 7000, 10000, 13000, 16000, 20000, 25000, 30000, 35000]:
# 	id = "wsj"+ str(i/1000) + "k_br_br"
# 	ar = "Arguments = DomainAdaption -train wsj0222 " + str(i) + " -adapt brown -test brown "
# 	ar += "-saveParser "+id+".mdl -testScores traces/"+id+".scores\n"
# 	log = "Log = traces/"+id + ".log\n"
# 	output = "\nOutput = traces/"+id+".out\n"
# 	error = "Error = traces/"+id+".err\n"
# 	script = header + log + ar + output + error + q
# 	fname = id+".sh"
# 	h = open(fname,"w")
# 	h.write(script)
# 	h.close()


"""
Additionally, generate a normal "in-domain" learning curve, where you use the same seed set but test on WSJ section 23 without self-training. 
"""
# for i in [1000,2000,3000,4000,5000, 7000, 10000, 13000, 16000, 20000, 25000, 30000, 35000]:
# 	id = "wsj"+ str(i/1000) + "k_wsj23"
# 	ar = "Arguments = DomainAdaption -train wsj0222 " + str(i) + " -test wsj23 "
# 	ar += "-saveParser "+id+".mdl -testScores traces/"+id+".scores\n"
# 	log = "Log = traces/"+id + ".log\n"
# 	output = "\nOutput = traces/"+id+".out\n"
# 	error = "Error = traces/"+id+".err\n"
# 	script = header + log + ar + output + error + q
# 	fname = id+".sh"
# 	h = open(fname,"w")
# 	h.write(script)
# 	h.close()

"""
increasing the size of the the self-training set improves F1 by varying the size of self-training set. You will use the first 10,000 labeled sentences from WSJ sections 02-22 as your seed set and the same Brown test set as before. Now increase the self-training set from 1,000 to 2,000, 3,000, 4,000, 5,000, 7,000, 10,000, 13,000, 17,000, 21,000, in the Brown self-training data 
"""
# for i in [1000,2000,3000,4000,5000, 7000, 10000, 13000, 17000, 21000]:
# 	id = "wsj10k_br"+str(i/1000)+"k_br"
# 	ar = "Arguments = DomainAdaption -train wsj0222 10000 -adapt brown " + str(i) + " -test brown "
# 	ar += "-saveParser "+id+".mdl -testScores traces/"+id+".scores\n"
# 	log = "Log = traces/"+id + ".log\n"
# 	output = "\nOutput = traces/"+id+".out\n"
# 	error = "Error = traces/"+id+".err\n"
# 	script = header + log + ar + output + error + q
# 	fname = id+".sh"
# 	h = open(fname,"w")
# 	h.write(script)
# 	h.close()


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
	ar = "Arguments = DomainAdaption -train brown " + str(i) + " -adapt wsj0222 -test wsj23 "
	ar += "-saveParser "+id+".mdl -testScores traces/"+id+".scores\n"
	log = "Log = traces/"+id + ".log\n"
	output = "\nOutput = traces/"+id+".out\n"
	error = "Error = traces/"+id+".err\n"
	script = header + log + ar + output + error + q
	fname = id+".sh"
	h = open(fname,"w")
	h.write(script)
	h.close()


"""
Additionally, generate a normal "in-domain" learning curve, where you use the same seed set but test on brown itself without self-training. 
"""
for i in [1000,2000,3000,4000,5000, 7000, 10000, 13000, 17000, 21000]:
	id = "br"+ str(i/1000) + "k_br"
	ar = "Arguments = DomainAdaption -train brown " + str(i) + " -test brown "
	ar += "-saveParser "+id+".mdl -testScores traces/"+id+".scores\n"
	log = "Log = traces/"+id + ".log\n"
	output = "\nOutput = traces/"+id+".out\n"
	error = "Error = traces/"+id+".err\n"
	script = header + log + ar + output + error + q
	fname = id+".sh"
	h = open(fname,"w")
	h.write(script)
	h.close()

"""
brown 90 train
wsj selftrain change
wsj test
"""
for i in [1000,2000,3000,4000,5000, 7000, 10000, 13000, 16000, 20000, 25000, 30000, 35000]:
	id = "br10k_wsj0222"+str(i/1000)+"k_wsj23"
	ar = "Arguments = DomainAdaption -train brown 10000 -adapt wsj0222 " + str(i) + " -test wsj23 "
	ar += "-saveParser "+id+".mdl -testScores traces/"+id+".scores\n"
	log = "Log = traces/"+id + ".log\n"
	output = "\nOutput = traces/"+id+".out\n"
	error = "Error = traces/"+id+".err\n"
	script = header + log + ar + output + error + q
	fname = id+".sh"
	h = open(fname,"w")
	h.write(script)
	h.close()


########################IGNORE

"""
trial for baseline stuff. ignore
"""
# id = "wsj1k_br1k_br1k"
# ar = "Arguments = DomainAdaption -train wsj0222 1000 -adapt brown 1000 -test brown 1000 "
# ar += "-saveParser "+id+".mdl -testScores traces/"+id+".scores \n"
# log = "Log = traces/"+id + ".log\n"
# output = "Output = traces/"+id+".out\n"
# error = "Error = traces/"+id+".err\n"
# script = header + log + ar + output + error + q
# fname = id+".sh"
# h = open(fname,"w")
# h.write(script)
# h.close()