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
def readcsv(data_file_name, answer_file_name):
	global n_features
	with open(data_file_name) as f:
		data_file = csv.reader(f)
		temp = next(data_file)
		n_samples = int(temp[0])
		n_features = int(temp[1])
		#date = np.empty((n_samples,), dtype=np.object)
		
		#reader = csv.reader(open("test.csv", "rb"), delimiter=",")
			
		# names of features
		templine = next(data_file)  
		feature_names = np.array(templine)
		
		# date and feature data
		x = list(data_file)
		data = np.array(x).astype("object")
		

		# data on time
		data_on_time = np.empty((period, n_samples/period, n_features))
		data_length = np.empty((period,))
		time = np.empty((period, n_samples/period),dtype=np.chararray)
		for i in range(0,period,1):

			temp = data[i:n_samples:period]
			data_length[i] = len(temp)
			for p in range(0,len(temp)):
				data_on_time[i][p] = temp[p][1:4]
				time[i][p] = str(temp[p][0])
				
		"""
		for i, d in enumerate(data_file):
			if i < n_samples-1:
				#parse out time flag
				#time[0] = "00:00", time[1] = "01:00", ...
				
				date[i] = d[0]
				data[i] = np.asarray(d[1:], dtype=np.float64)
				#target[i] = np.asarray(d[-1], dtype=np.float64)
		"""
		
		target = np.empty((period, n_samples/period))
		with open(answer_file_name) as f2:
			answer_file = csv.reader(f2)
			for j, e in enumerate(answer_file):
				#print(e[0])
				target[j%period][j/period] = e[0]
				#print("target {0}".format(target))

		#print("target {0}".format(target))
		return data_on_time, target, time, data_length

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


def predict2(verbose=True,k_max=200,E_stop=1e-3):
	sampleMin = 0
	sampleMax = 345
	predictMin = 345
	predictMax = 365

	total_score = 0.0
	date_predict = np.empty((1,5), dtype=np.object)
	date_predict_array = np.empty((0,5))

	predict_array2 = np.zeros((period,))
	predict_array = np.zeros((period,))
	real_array = np.zeros((period,))
	mae = 0.0
	for k in range(0,test_loop):
		print('Loop No.{0}'.format(k))
		data_length = combinedData[3][k]
		print('data length {0}'.format(data_length))
		#Xori = combinedData[0][k][sampleMin:data_length-20]
		#yori = combinedData[1][k][sampleMin:data_length-20]
		Xstand = StandardScaler().fit_transform(combinedData[0][k])
		#ystand = StandardScaler().fit_transform(combinedData[1][k])
		
		#Xtrain = Xstand[sampleMin:sampleMax]
		#Xtrain = combinedData[0][k]
		#Xstand = StandardScaler().fit_transform(combinedData[0][k])

		y = np.empty((1,345))
		y0 = np.empty((1,7))
		X = np.transpose(Xstand[sampleMin:data_length-20])
		y[0] = combinedData[1][k][sampleMin:data_length-20]
		
		#print('X0 shape:\n{0}'.format(X0.shape))
		#print('y0 shape:\n{0}'.format(y0.shape))
		#print('X narray , K= {1} :\n{0}'.format(X,k))		
		#print('y array answer:\n{0}'.format(y))

		ytest = np.empty((1,20))
		Xtest = np.transpose(Xstand[predictMin:data_length])
		ytest[0] = combinedData[1][k][predictMin:data_length]
		y0[0] = combinedData[1][k][predictMin-7:predictMin]
		X0 = np.transpose(Xstand[predictMin-7:predictMin])
		#print('Xtest shape:\n{0}'.format(Xtest.shape))
		#print('ytest shape:\n{0}'.format(ytest.shape))
		#print('X test narray :\n{0}'.format(Xtest))	
		#print('y test array answer :\n{0}'.format(ytest))	
		
		Xpredict = np.empty((3,1))
		Xpredict_stand = Xstand[predictMin]
		#Xpredict_ap = np.empty((3,1))
		Xpredict = Xpredict_stand.reshape(3,1)
		#print('Xpredict shape:\n{0}'.format(Xpredict.shape))
		#Xpredict = np.empty((n_features,))
		#for j in range(1,n_features):
		#	Xpredict[j-1] = float(sys.argv[j])
		#Xpredict = Xpredict.reshape(1,-1)
		
		#ypredict = combinedData[1][testTargetIndexL].reshape(1,-1)

		# use logistic
		mlp = pyrenn.CreateNN([3,4,4,1],dIn=[0,1,2,3],dIntern=[],dOut=[1,2,3])
		mlp = pyrenn.train_LM(X,y,mlp,verbose=verbose,k_max=k_max,E_stop=E_stop)
		
		y_train_predict = pyrenn.NNOut(X,mlp)
		test_predict = pyrenn.NNOut(Xtest,mlp,P0=X0, Y0=y0)
		
		#print('y test array predict :\n{0}'.format(test_predict))
		"""
		if activation == 'identity':
			assert_greater(mlp.score(X, y), 0.84)
		else:
			# Non linear models perform much better than linear bottleneck:
			assert_greater(mlp.score(X, y), 0.95)
			"""

		#show training parameter
		#print('ACTIVATION_TYPES - {0} : '.format("logistic"))

		#show training score
		#print('y_train_predict shape:\n{0}'.format(y_train_predict.shape))
		#print('y[0] shape:\n{0}'.format(y[0].shape))
		trainingScore = MAE(y_train_predict,y[0],len(y_train_predict))
		date_predict[0][3]= trainingScore
		#print('training score is {0}'.format(trainingScore))
		
		#calculate predict score
		#print('test_predict shape:\n{0}'.format(test_predict.shape))
		#print('ytest[0] shape:\n{0}'.format(ytest[0].shape))
		predictScore = MAE(test_predict,ytest[0],len(test_predict))

		date_predict[0][4]= predictScore
		total_score = float((total_score + predictScore)/period)
		#print('predict score is {0}'.format(predictScore))

		#real predict
		predict = pyrenn.NNOut(Xpredict,mlp,P0=X0, Y0=y0)
		predict2 = pyrenn.NNOut(Xpredict,mlp)

		date_predict[0][0]= combinedData[2][k][predictMin+1]
		date_predict[0][1]= predict[0]
		date_predict[0][2]= combinedData[0][k][predictMin+1][n_features-1]
		#print('predict result:\n {0}'.format(predict))

		date_predict_array= np.append(date_predict_array,date_predict,0)

		predict_array2[k] = predict2[0]
		predict_array[k] = predict[0]
		real_array[k] = combinedData[0][k][predictMin+1][n_features-1]
		print('predict[{0}]: {1} & {3}\nreal[{0}]:    {2}\n'.format(k,predict_array[k],real_array[k],predict_array2[k]))
		
		#print('\n')

	#print("next_n_time", "prediction", "origin", "training_score(MAE)", "predicting_score(MAE)")
	#print(date_predict_array)
	nmse2 = NMSE(predict_array2,real_array,test_loop)
	nmse = NMSE(predict_array,real_array,test_loop)
	mae = MAE(predict_array,real_array,test_loop)
	print('Test prediction MAE: {0}'.format(mae))
	print('Test prediction NMSE: {0}'.format(nmse))
	with open(output_file_name, 'w') as csvfile:
		#spamwriter = csv.writer(csvfile, delimiter=',',quotechar='|', quoting=csv.QUOTE_MINIMAL)
		spamwriter = csv.writer(csvfile, delimiter=',')
		#print(predict_array)
		#print(real_array)
		
		csvfile.write('Test prediction NMSE is:  {0} & {1} , MAE is:  {2}\n'.format(nmse,nmse2,mae))

		#	'predict parameters: solver={0}, hidden_layer_sizes={1},max_iter={2}, warm_start={3}, shuffle={4}, random_state={5},activation={6}\nTotal predict score is: {7}\n\n'.format(
		#		sol, hidden_layer, max_it, warm, shuf, random_s, acti, mae))
		spamwriter.writerow(["target_datetime", " predicted_value", " real_value", " training_MAE", " predicting_MAE"])

		for row in date_predict_array:
			spamwriter.writerow(row)


ACTIVATION_TYPES = ["identity", "logistic", "tanh", "relu"]

combinedData = readcsv('../data/rawData_heavyTaipei.csv','../data/answerData_heavyTaipei.csv')
#combinedData = load_boston()

output_file_name = "../output/output" + sys.argv[3] + ".csv"
#predict_complete()
#print('"predict parameters:\nsolver="lbfgs", hidden_layer_sizes=50,max_iter=150, warm_start=False, shuffle=False, random_state=1,activation="logistic""')
print('processing...')
predict2(verbose=True,k_max=int(sys.argv[2]),E_stop=1e-3)
print('output file dir: {0}'.format(output_file_name))
print('1st module finished.\n')

# print('processing next module...')
# num = 2
# predict2(sol='sgd', hidden_layer=50, max_it=150, warm=False, shuf=False, random_s=1,acti="logistic")
# print('2nd module finished.')
# print('processing next module...')
# num = 3
# predict2(sol='adam', hidden_layer=50, max_it=150, warm=False, shuf=False, random_s=1,acti="logistic")
# print('3rd module finished.')

#print('processing next module...')
#num = 4
#predict2(sol='lbfgs', hidden_layer=3, max_it=150, warm=False, shuf=False, random_s=1,acti="logistic")
#print('4th module finished.')

