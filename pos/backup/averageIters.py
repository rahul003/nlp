import sys
import os
from collections import deque

def get_sec(s):
    l = s.split(':')
    return int(l[0]) * 3600 + int(l[1]) * 60 + int(l[2])

def getLast3(d, base):
	three = []
	if (base[:3] == "crf"):
		for i in range(0, 10):
			if len(three)==3:
				break
			if d[10-i-1].strip()=="":
				continue
			else:
				three.append(d[10-i-1])
	else:
		for i in range(0, 10):
			if len(three)==3:
				break
			if d[10-i-1].strip()=="":
				continue
			elif (d[10-i-1].strip().split()[0]=="Trainer"):
				continue
			else:
				three.append(d[10-i-1])
	return three[::-1]


def printAverageResults(experiment):
	base = experiment + "_"
	print base[:-1]

	tr_acc = 0
	te_acc = 0
	oov_acc = 0
	tim = 0
	for i in range(0, 10):
		filen = base+str(i)
		d = deque(open(filen+".err"), maxlen=10)
		
		Last3 = getLast3(d, base)
		# print Last3
		parts = Last3[0].split("=")
		# print parts
		assert(parts[0]=="Training accuracy")
		tr_acc += float(parts[1])

		parts = Last3[1].split("=")
		# print parts
		assert(parts[0]=="Testing accuracy")
		te_acc += float(parts[1])

		parts = Last3[2].split(";")
		pparts = parts[0].split("=")
		# print pparts
		assert(pparts[0]=="Testing oov accuracy")
		oov_acc += float(pparts[1])

		t = open(filen+".log")
		for line in t:
			if line.lstrip()[:6]=="Usr 0 ":
				# print line.lstrip()[6:14]
				tim += int(get_sec(line.lstrip()[6:14]))
				break
	# Usr 0 00:01:10, Sys 0 00:00:00  -  Run Remote Usage
		
	print 'Training acc:', tr_acc/10
	print 'Testing acc:', te_acc/10
	print 'OOV acc:', oov_acc/10
	print 'Time:', tim/10
	print '\n\n'

def runForAllExperiments():
	unique_exp = []
	files = [f for f in os.listdir('.') if os.path.isfile(f)]
	for x in files:
		if x[-3:] not in ['log', 'out', 'err']:
			continue
		if x[:-6] not in unique_exp:
			unique_exp.append(x[:-6])
	# print unique_exp
	for ex in unique_exp:
		printAverageResults(ex)

runForAllExperiments()