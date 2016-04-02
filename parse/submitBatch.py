import os

#has to be run in projects folder

files = [f for f in os.listdir('.') if os.path.isfile(f)]

for f in files:
	if f[-2:] == "sh":
		print f
		os.system("condor_submit "+f)
		os.system("mv "+f+" scripts/submitted/")
