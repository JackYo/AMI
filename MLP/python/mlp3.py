from datetime import datetime, timedelta
import csv
import sys
import warnings

import numpy as np
from sklearn.preprocessing import StandardScaler
from sklearn.datasets import load_boston
from sklearn.neural_network import MLPRegressor
from sklearn.utils.testing import (assert_raises, assert_greater, assert_equal,
								   assert_false, ignore_warnings)

n_features = None
period = 1440/ int(sys.argv[1])
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


def predict2(sol='lbfgs', hidden_layer=50, max_it=150, warm=False, shuf=False, random_s=1,acti="logistic"):
	sampleMin = 0
	sampleMax = 345
	predictMin = 345
	predictMax = 365

	total_score = 0.0
	date_predict = np.empty((1,3), dtype=np.object)
	date_predict_array = np.empty((0,3))

	for k in range(0,period):

		data_length = combinedData[3][k]
		#Xstand = StandardScaler().fit_transform(combinedData[0][k])
		#Xtrain = Xstand[sampleMin:sampleMax]
		#Xtrain = combinedData[0][k]

		X = combinedData[0][k][sampleMin:data_length-20]
		y = combinedData[1][k][sampleMin:data_length-20]

		#print('X narray , K= {1} :\n{0}'.format(X,k))		
		#print('y array answer:\n{0}'.format(y))

		Xtest = combinedData[0][k][predictMin:data_length]
		ytest = combinedData[1][k][predictMin:data_length]

		#print('X test narray :\n{0}'.format(Xtest))	
		#print('y test array answer :\n{0}'.format(ytest))	
		
		Xpredict = predictData[0][k].reshape(1,-1)
		
		#Xpredict = np.empty((n_features,))
		#for j in range(1,n_features):
		#	Xpredict[j-1] = float(sys.argv[j])
		#Xpredict = Xpredict.reshape(1,-1)
		
		#ypredict = combinedData[1][testTargetIndexL].reshape(1,-1)

		# use logistic
		mlp = MLPRegressor(solver=sol, hidden_layer_sizes=hidden_layer,max_iter=max_it, warm_start=warm, shuffle=shuf, random_state=random_s,activation=acti) 
		mlp.fit(X, y)
		
		test_predict = mlp.predict(Xtest)
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
		trainingScore = mlp.score(X,y)
		date_predict[0][2]= trainingScore
		#print('training score is {0}'.format(trainingScore))
		
				#real predict
		predict = mlp.predict(Xpredict)

		time_object = datetime.strptime(predictData[1][k], '%Y/%m/%d  %H:%M') 
		time_object_next = time_object + timedelta(days = 1)
		
		date_predict[0][0]= datetime.strftime(time_object, '%Y/%m/%d  %H:%M')
		date_predict[0][1]= predict[0]
		#print('predict result:\n {0}'.format(predict))

		date_predict_array= np.append(date_predict_array,date_predict,0)

		#print('\n')

	#print("next_n_time", "prediction", "origin", "training score", "predicting score")
	#print(date_predict_array)
	output_file_name = "../output/output3-" + str(num)
	with open(output_file_name, 'w') as csvfile:
		#spamwriter = csv.writer(csvfile, delimiter=',',quotechar='|', quoting=csv.QUOTE_MINIMAL)
		spamwriter = csv.writer(csvfile, delimiter=',')
		csvfile.write(
			'predict parameters: solver={0}, hidden_layer_sizes={1},max_iter={2}, warm_start={3}, shuffle={4}, random_state={5},activation={6}\n\n'.format(
				sol, hidden_layer, max_it, warm, shuf, random_s, acti))
		spamwriter.writerow(["target_datetime", " prediction_value", " training_score"])

		for row in date_predict_array:
			spamwriter.writerow(row)


ACTIVATION_TYPES = ["identity", "logistic", "tanh", "relu"]

combinedData = readcsv('../data/rawData.csv','../data/answerData.csv')
predictData = readpredict('../data/predictData.csv')
#combinedData = load_boston()


#predict_complete()
#print('"predict parameters:\nsolver="lbfgs", hidden_layer_sizes=50,max_iter=150, warm_start=False, shuffle=False, random_state=1,activation="logistic""')
print('processing...')
num = 1
predict2(sol='lbfgs', hidden_layer=50, max_it=150, warm=False, shuf=False, random_s=1,acti="logistic")
print('1st module finished.')
# print('processing next module...')
# num = 2
# predict2(sol='sgd', hidden_layer=50, max_it=150, warm=False, shuf=False, random_s=1,acti="logistic")
# print('2nd module finished.')
# print('processing next module...')
# num = 3
# predict2(sol='adam', hidden_layer=50, max_it=150, warm=False, shuf=False, random_s=1,acti="logistic")
# print('3rd module finished.')

print('processing next module...')
num = 4
predict2(sol='lbfgs', hidden_layer=3, max_it=150, warm=False, shuf=False, random_s=1,acti="logistic")
print('4th module finished.')

