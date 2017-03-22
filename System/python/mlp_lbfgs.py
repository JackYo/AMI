import csv
import sys
import warnings

import numpy as np
from sklearn.preprocessing import StandardScaler
from sklearn.datasets import load_boston
from sklearn.neural_network import MLPRegressor
from sklearn.utils.testing import (assert_raises, assert_greater, assert_equal,
								   assert_false, ignore_warnings)

def readcsv(data_file_name):
	with open(data_file_name) as f:
		data_file = csv.reader(f)
		temp = next(data_file)
		n_samples = int(temp[0])-1
		n_features = int(temp[1])
		date = np.empty((n_samples,), dtype=np.object)
		data = np.empty((n_samples, n_features))
		target = np.empty((n_samples,))
		temp = next(data_file)  # names of features
		feature_names = np.array(temp)

		for i, d in enumerate(data_file):
			date[i] = d[0]
			data[i] = np.asarray(d[1:-1], dtype=np.float64)
			target[i] = np.asarray(d[-1], dtype=np.float64)

		return data, target, date

def predict_complete():
	sampleMin = 0
	sampleMax = 10000
	predictMin = 10000
	predictMax = 10096
	featureIndex = sampleMax
	targetIndex = sampleMin
	testTargetIndexL = predictMin
	testTargetIndexR = predictMax


	date_predict = np.empty((1,5), dtype=np.object)
	date_predict_array = np.empty((0,5))

	for k in range(1,97):

		X = Xbos[sampleMin:featureIndex]
		y = combinedData[1][targetIndex:sampleMax]

		Xtest = Xbos[predictMin:predictMax]
		ytest = combinedData[1][testTargetIndexL:testTargetIndexR]

		Xpredict = Xbos[predictMin].reshape(1,-1)
		#ypredict = combinedData[1][testTargetIndexL].reshape(1,-1)

		# use logistic
		mlp = MLPRegressor(solver='lbfgs', hidden_layer_sizes=50,max_iter=150, warm_start=True, shuffle=True, random_state=1,activation="logistic") 
		mlp.fit(X, y)
		
		"""
		if activation == 'identity':
			assert_greater(mlp.score(X, y), 0.84)
		else:
			# Non linear models perform much better than linear bottleneck:
			assert_greater(mlp.score(X, y), 0.95)
			"""

		#show training parameter
		print('ACTIVATION_TYPES - {0} : '.format("logistic"))

		#show training score
		trainingScore = mlp.score(X,y)
		date_predict[0][3]= trainingScore
		print('training score is {0}'.format(trainingScore))
		
		#calculate predict score
		predictScore = mlp.score(Xtest, ytest)
		date_predict[0][4]= predictScore
		print('predict score is {0}'.format(predictScore))

		#real predict
		predict = mlp.predict(Xpredict)
		date_predict[0][0]= date[testTargetIndexL+1]
		date_predict[0][1]= predict
		date_predict[0][2]= combinedData[0][testTargetIndexL+1][n_features]
		print('predict result:\n {0}'.format(predict))

		featureIndex = featureIndex-1
		targetIndex = targetIndex+1
		testTargetIndexL = testTargetIndexL+1
		testTargetIndexR = testTargetIndexR+1
		date_predict_array= np.append(date_predict_array,date_predict,0)

		print('\n')

	print("date", "prediction", "origin", "training score", "predicting score")
	print(date_predict_array)
	with open('output2.csv', 'w') as csvfile:
		#spamwriter = csv.writer(csvfile, delimiter=',',quotechar='|', quoting=csv.QUOTE_MINIMAL)
		spamwriter = csv.writer(csvfile, delimiter=',')
		spamwriter.writerow(["date", "prediction", "origin", "training score", "predicting score"])
		for row in date_predict_array:
			spamwriter.writerow(row)

ACTIVATION_TYPES = ["identity", "logistic", "tanh", "relu"]
n_features = 0
combinedData = readcsv('data/combined2.csv')
#combinedData = load_boston()
Xbos = StandardScaler().fit_transform(combinedData[0])
origin = combinedData[0]
date = combinedData[2]


predict_complete()