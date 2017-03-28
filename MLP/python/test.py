from datetime import datetime, timedelta
import csv
import sys
sys.path.insert(0,"./pyrenn")
import warnings

import numpy as np
from sklearn.preprocessing import StandardScaler
from sklearn.datasets import load_boston
from sklearn.neural_network import MLPRegressor
from sklearn.utils.testing import (assert_raises, assert_greater, assert_equal,
								   assert_false, ignore_warnings)
from pyrenn.python import pyrenn
import matplotlib.pyplot as plt

n_features = None
period = int(1440/ int(sys.argv[1]))
test_loop = period

def readpredict(data_file_name):
	global n_features
	with open(data_file_name) as f:
		data_file = csv.reader(f)
		
		# date and feature data
		x = list(data_file)
		data = np.array(x).astype("object")
		

		# data on time
		data_on_time = np.empty((period, n_features))
		time = np.empty((period, ),dtype=np.chararray)
		for i in range(0,period,1):
			
			data_on_time[i] = data[i][1:4]
			time[i] = str(data[i][0])
			
		return data_on_time, time


def readcsv(data_file_name, answer_file_name):
	global n_features
	with open(data_file_name) as f:
		data_file = csv.reader(f)
		temp = next(data_file)
		n_samples = int(temp[0])-1
		n_features = int(temp[1])
		date = np.empty((n_samples,), dtype=np.object)
		data = np.empty((n_samples, n_features))
		target = np.empty((n_samples,))

		# names of features
		temp = next(data_file)  
		feature_names = np.array(temp)

		for i, d in enumerate(data_file):
			if i < n_samples-1:
				date[i] = d[0]
				data[i] = np.asarray(d[1:], dtype=np.float64)
				#target[i] = np.asarray(d[-1], dtype=np.float64)

		with open(answer_file_name) as f2:
			answer_file = csv.reader(f2)
			for j, e in enumerate(answer_file):
				target[j] = np.asarray(e[0], dtype=np.float64)

		return data, target, date
def MAE(a,b,length):
	suma = 0.0

	for n in range(0,length):
		temp = abs(a[n]-b[n])
		suma = suma + temp
	out = float(suma/length)
	#print('MAE:  {0}'.format(out))
	#print('a: {0}\nb: {1}\nout: {2}'.format(a,b,out))
	return(out)

def NMSE(pre,real,length):
	suma = 0.0
	avg = 0.0
	var = 0.0
	for n in range(0,length):
		avg = avg + pre[n]
	avg = int(avg/length)
	
	for n in range(0,length):
		var = var + (pre[n]-avg)**2
	var =  int(var/length)

	for n in range(0,length):
		temp = pre[n]-real[n]
		suma = suma + temp**2
	
	out = float(suma/length/var)
	#print('MAE:  {0}'.format(out))
	#print('a: {0}\nb: {1}\nout: {2}'.format(a,b,out))
	return(out)
def predict(verbose=True,k_max=200,E_stop=1e-3):
	sampleMin = 0
	sampleMax = 30000
	predictMin = 30000
	predictMax = 30100
	featureIndex = sampleMax
	targetIndex = sampleMin
	testTargetIndexL = predictMin
	testTargetIndexR = predictMax

	predict_array = np.zeros((96,))
	real_array = np.zeros((96,))
	csv_column = np.empty((1,5), dtype=np.object)
	csv_column_array = np.empty((0,5))

	for k in range(1,97):

		X = np.transpose(combinedData[sampleMin:featureIndex])
		y = np.transpose(np.array([combinedData[1][targetIndex:sampleMax]]))
		print(X.shape)
		print(y.shape)


		Xtest = np.transpose(combinedData[predictMin:predictMax])
		ytest = np.transpose(np.array([combinedData[1][testTargetIndexL:testTargetIndexR]]))

		#Xpredict = Xbos[predictMin].reshape(1,-1)
		Xpredict = np.array([combinedData[0][testTargetIndexL]])
		
		#Xpredict_ap = np.empty((3,1))
		Xpredict = Xpredict.reshape(3,1)
		# use logistic
		net = pyrenn.CreateNN([3,4,4,1],dIn=[0,1,2,3],dIntern=[],dOut=[1,2,3])
		net = pyrenn.train_LM(X,y,net,verbose=verbose,k_max=k_max,E_stop=E_stop)
		y_train_predict = pyrenn.NNOut(X,net)
		test_predict = pyrenn.NNOut(Xtest,net)
		"""
		if activation == 'identity':
			assert_greater(mlp.score(X, y), 0.84)
		else:
			# Non linear models perform much better than linear bottleneck:
			assert_greater(mlp.score(X, y), 0.95)
			"""

		#show training parameter

		#show training score
		trainingScore = MAE(y_train_predict,combinedData[1][targetIndex:sampleMax])
		csv_column[0][3]= trainingScore
		#print('training score is {0}'.format(trainingScore))
		
		#calculate predict score
		predictScore = MAE(test_predict, combinedData[1][testTargetIndexL:testTargetIndexR])
		csv_column[0][4]= predictScore
		#print('predict score is {0}'.format(predictScore))

		#real predict
		predict = pyrenn.NNOut(Xpredict,net)

		csv_column[0][0]= k
		csv_column[0][1]= predict[0]
		csv_column[0][2]= combinedData[0][testTargetIndexL+1][n_features-1]
		print('predict result:\n {0}'.format(predict))

		featureIndex = featureIndex-1
		targetIndex = targetIndex+1
		testTargetIndexL = testTargetIndexL+1
		testTargetIndexR = testTargetIndexR+1
		csv_column_array= np.append(csv_column_array,csv_column,0)

		predict_array[k] = predict[0]
		real_array[k] = combinedData[0][k][predictMin+1][n_features-1]
		print('predict[{0}]: {1} & {3}\nreal[{0}]:    {2}\n'.format(k,predict_array[k],real_array[k]))

	print("next_n_time", "prediction", "origin", "training score", "predicting score")
	print(csv_column_array)
	with open(output_file_name, 'w') as csvfile:
		#spamwriter = csv.writer(csvfile, delimiter=',',quotechar='|', quoting=csv.QUOTE_MINIMAL)
		spamwriter = csv.writer(csvfile, delimiter=',')
		#print(predict_array)
		#print(real_array)
		
		#csvfile.write('Test prediction MAE is:  {0} , NMSE is: {1}\n'.format(mae,nmse))

		#	'predict parameters: solver={0}, hidden_layer_sizes={1},max_iter={2}, warm_start={3}, shuffle={4}, random_state={5},activation={6}\nTotal predict score is: {7}\n\n'.format(
		#		sol, hidden_layer, max_it, warm, shuf, random_s, acti, mae))
		spamwriter.writerow(["target_datetime", " predicted_value", " real_value", " training_MAE", " predicting_MAE"])

		for row in csv_column_array:
			spamwriter.writerow(row)
combinedData = readcsv('../data/rawData.csv','../data/answerData.csv')
#combinedData = load_boston()

output_file_name = "../output/output" + sys.argv[3] + ".csv"
#predict_complete()
#print('"predict parameters:\nsolver="lbfgs", hidden_layer_sizes=50,max_iter=150, warm_start=False, shuffle=False, random_state=1,activation="logistic""')
print('processing...')
predict(verbose=True,k_max=int(sys.argv[2]),E_stop=1e-3)
print('output file dir: {0}'.format(output_file_name))
print('1st module finished.\n')