import os, sys
#run in projects folder
f = open('header_condorscript.txt','r')
header = f.read()
f.close()
q = "Queue 1\n"


id = "wsj1k_br_br"
ar = "Arguments = DomainAdaption -train wsj0222 1000 -adapt brown -test brown "
ar += "-saveParser "+id+".mdl -testScores "+id+".scores"


log = "Log = traces/"+id + ".log\n"
output = "Output = traces/"+id+".out\n"
error = "Error = traces/"+id+".err\n"
script = header + log + ar + output + error + q
fname = id+".sh"
h = open(fname,"w")
h.write(script)
h.close()


# -train wsj0222 1000 -adapt brown -test brown -saveParser wsj1k_br_br.mdl -testScores wsj_1k_br_br.scores
