import csv
import numpy as np

with open('data.csv') as csvfile:
	spamreader = csv.reader(csvfile, delimiter=',', quotechar='|')
	#for row in spamreader:
		#print(', '.join(row))

	out = np.array(list(spamreader)[2:], dtype=np.float64)

	print out

