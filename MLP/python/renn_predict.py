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
		data_on_time = np.empty((period, n_samples/period, n_features ))
		data_length = np.empty((period,))
		time = np.empty((period, n_samples/period),dtype=np.chararray)
		for i in range(0,period):

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

def predict(verbose=True,k_max=200,E_stop=1e-3):
	sampleMin = 0
	sampleMax = 345
	predictMin = 345
	predictMax = 365

	total_score = 0.0
	csv_column = np.empty((1,2), dtype=np.object)
	csv_column_array = np.empty((0,2))

	predict_array = np.zeros((period,))
	real_array = np.zeros((period,))
	mae = 0.0
	for k in range(0,test_loop):
		print('Loop No.{0}'.format(k))
		data_length = combinedData[3][k]
		print('data length {0}'.format(data_length))
		#Xori = combinedData[0][k][sampleMin:data_length-20]
		#yori = combinedData[1][k][sampleMin:data_length-20]
		#print('combinedData[0][k] {0}'.format(.shape))
		Xcombined = np.append(combinedData[0][k],np.array([predictData[0][k]]), axis=0)
		print('combinedData[0][k] {0}'.format(combinedData[0][k].shape))
		print('combinedData[0][k] {0}'.format(np.array([predictData[0][k]]).shape))
		print('combinedData[0][k] {0}'.format(Xcombined.shape))
		Xstand = StandardScaler().fit_transform(Xcombined)
		print(Xstand.shape)
		
		#Xpredict = np.empty((3,1))
		Xpredict_stand = Xstand[-1]
		print(Xpredict_stand.shape)
		Xpredict = Xpredict_stand.reshape(3,1)
		#print('Xpredict shape:\n{0}'.format(Xpredict.shape))
		#Xpredict = np.empty((n_features,))
		#for j in range(1,n_features):
		#	Xpredict[j-1] = float(sys.argv[j])
		#Xpredict = Xpredict.reshape(1,-1)
		
		#ypredict = combinedData[1][testTargetIndexL].reshape(1,-1)

		# use logistic
		net = pyrenn.loadNN('../NNsave/NN'+sys.argv[3]+ '_'+ str(k) + '.csv')

		#y_train_predict = pyrenn.NNOut(X,mlp)
		#test_predict = pyrenn.NNOut(Xtest,mlp,P0=X0, Y0=y0)
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
		#trainingScore = MAE(y_train_predict,y[0],len(y_train_predict))
		#csv_column[0][3]= trainingScore
		#print('training score is {0}'.format(trainingScore))
		
		#calculate predict score
		#print('test_predict shape:\n{0}'.format(test_predict.shape))
		#print('ytest[0] shape:\n{0}'.format(ytest[0].shape))
		#predictScore = MAE(test_predict,ytest[0],len(test_predict))
		#csv_column[0][4]= predictScore
		#total_score = float((total_score + predictScore)/period)
		#print('predict score is {0}'.format(predictScore))

		#real predict
		predict = pyrenn.NNOut(Xpredict,net)

		time_object = datetime.strptime(predictData[1][k], '%Y/%m/%d  %H:%M') 
		time_object_next = time_object + timedelta(days = 1)
		
		csv_column[0][0]= datetime.strftime(time_object_next, '%Y/%m/%d  %H:%M')
		csv_column[0][1]= predict[0]

		csv_column_array= np.append(csv_column_array,csv_column,0)

	with open(output_file_name, 'w') as csvfile:
		#spamwriter = csv.writer(csvfile, delimiter=',',quotechar='|', quoting=csv.QUOTE_MINIMAL)
		spamwriter = csv.writer(csvfile, delimiter=',')
		#print(predict_array)
		#print(real_array)
		
		#csvfile.write('Test prediction MAE is:  {0} , NMSE is: {1}\n'.format(mae,nmse))

		#	'predict parameters: solver={0}, hidden_layer_sizes={1},max_iter={2}, warm_start={3}, shuffle={4}, random_state={5},activation={6}\nTotal predict score is: {7}\n\n'.format(
		#		sol, hidden_layer, max_it, warm, shuf, random_s, acti, mae))
		spamwriter.writerow(["target_datetime", " predicted_value"])

		for row in csv_column_array:
			spamwriter.writerow(row)


combinedData = readcsv('../data/rawData.csv','../data/answerData.csv')
predictData = readpredict('../data/predictData.csv')

output_file_name = "../output/prediction" + sys.argv[4] + ".csv"
#predict_complete()
#print('"predict parameters:\nsolver="lbfgs", hidden_layer_sizes=50,max_iter=150, warm_start=False, shuffle=False, random_state=1,activation="logistic""')
print('processing...')
predict(verbose=True,k_max=int(sys.argv[2]),E_stop=1e-3)
print('output file dir: {0}'.format(output_file_name))
print('finished.\n')