import os, sys
#run in projects folder
"""
wsj0222 as seed
90% brown as the unlabeled adapt data 
10% brown as test data

Increasing the size of the seed set 
learning curve of F1 score as a function of number of sentences in the seed set. 
Also plot a baseline by conducting a control experiment without self-training on "unlabeled" wsj data. 
"""
# for i in [1000,2000,3000,4000,5000, 7000, 10000, 13000, 16000, 20000, 25000, 30000, 35000]:
# 	id = "wsj"+ str(i/1000) + "k_br_br"
# 	ar = "Arguments = DomainAdaption -train wsj0222 " + str(i) + " -adapt brown -test brown "
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
# Additionally, generate a normal "in-domain" learning curve, where you use the same seed set but test on brown itself without self-training. 
# """
# for i in [1000,2000,3000,4000,5000, 7000, 10000, 13000, 16000, 20000, 25000, 30000, 35000]:
# 	id = "wsj"+ str(i/1000) + "k_wsj23"
# 	ar = "Arguments = DomainAdaption -train wsj0222 " + str(i) + " -test wsj23 "
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
# wsj  train
# brown selftrain change
# brown test
# """
# for i in [1000,2000,3000,4000,5000, 7000, 10000, 13000, 17000, 21000]:
# 	id = "wsj10k_br"+str(i/1000)+"k_br"
# 	ar = "Arguments = DomainAdaption -train wsj0222 10000 -adapt brown " + str(i) + " -test brown "
# 	ar += "-saveParser "+id+".mdl \n"
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
	ar += "-saveParser "+id+".mdl\n"
	log = "Log = traces/"+id + ".log\n"
	output = "\nOutput = traces/"+id+".out\n"
	error = "Error = traces/"+id+".err\n"
	script = header + log + ar + output + error + q
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