import random, os
from datetime import datetime
random.seed(datetime.now())

#run in projects folder

f = open('scripts/header.txt','r')
header = f.read()
f.close()

model = "SimpleTagger"
# model = "SimpleTagger"
data = "atis"

if model=="SimpleTagger":
	id = "crf_"+data
else:
	id = "hmm_"+data

prefix = "Notification = Always\nNotify_user = h.rahul@utexas.edu\n"
restarg = " --training-proportion 0.8 --test lab data/"+data+".txt\n"
q = "Queue 1\n"

for i in range(0, 10):
	log = "Log = traces/"+id + "_"+str(i)+".log\n"

	ar = "Arguments = cc.mallet.fst." + model + " --train true --model-file models/"+id+"_"+str(i)+".ml --random-seed "

	r = random.randint(0,10000)

	output = "Output = traces/"+id + "_"+str(i)+".out\n"
	error = "Error = traces/"+id + "_"+str(i)+".err\n"
	script = header + log + prefix + ar + str(r) + restarg + output + error + q

	fname = id+"_"+str(i)+".sh"
	h = open(fname,"w")
	h.write(script)
	h.close()