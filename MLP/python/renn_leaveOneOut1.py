from datetime import datetime, timedelta
import csv
import sys
sys.path.insert(0,"./pyrenn")
import warnings

import numpy as np
from sklearn.preprocessing import StandardScaler
from pyrenn.python import pyrenn
import matplotlib.pyplot as plt

n_features = None
avg_mae = 0.0
avg_nmse = 0.0
period = int(1440/ int(sys.argv[1]))
test_loop = period
leave_loop = 1

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
				
		
		target = np.empty((period, n_samples/period))
		with open(answer_file_name) as f2:
			answer_file = csv.reader(f2)
			for j, e in enumerate(answer_file):
				target[j%period][j/period] = e[0]
		
		return data_on_time, target, time, data_length

def MAE(a,b,length):
	suma = 0.0

	for n in range(0,length):
		temp = abs(a[n]-b[n])
		suma = suma + temp
	out = float(suma/length)
	return(out)

def MAPE(pre,real,length):
	suma = 0.0

	for n in range(0,length):
		temp = abs(pre[n]-real[n])/real[n]
		suma = suma + temp
	out = float(suma/length)
	return(out)

def NMSE(pre,real,length):
	suma = 0.0
	avg = 0.0
	var = 0.0
	for n in range(0,length):
		avg = avg + real[n]
	avg = int(avg/length)
	
	for n in range(0,length):
		var = var + (real[n]-avg)**2
	var =  int(var/length)

	for n in range(0,length):
		temp = pre[n]-real[n]
		suma = suma + temp**2
	
	out = float(suma/length/var)
	return(out)

def predict(verbose=True,k_max=200,E_stop=1e-3):
	global avg_mae,avg_nmse
	
	left = leave_loop*(int(sys.argv[6])-1)
	right = leave_loop*int(sys.argv[6])
	for m in range(left,right):
		sampleMin = 0
		sampleMax = 345
		predictMin = 345
		predictMax = 365

		total_score = 0.0
		date_predict = np.empty((1,5), dtype=np.object)
		date_predict_array = np.empty((0,5))

		predict_array = np.zeros((period,))
		real_array = np.zeros((period,))
		mae = 0.0
		
		for k in range(0,test_loop):
			print('Loop No.{0}-{1} '.format(m,k))
			data_length = int(combinedData[3][k])
			print('data length {0}'.format(data_length))
			
			Xstand = StandardScaler().fit_transform(combinedData[0][k])

			y = np.empty((1,data_length-1))
			print(y.shape)
			y0 = np.empty((1,7))
			Xcut = np.append( Xstand[sampleMin:m], Xstand[m+1:data_length], axis=0)
			X = np.transpose(Xcut)
			
			ycut = np.append( combinedData[1][k][sampleMin:m], combinedData[1][k][m+1:data_length])
			print(ycut.shape)
			y[0] = ycut	
			
			Xpredict = np.empty((3,1))
			Xpredict_stand = Xstand[m]
			Xpredict = Xpredict_stand.reshape(3,1)
			
			net = pyrenn.CreateNN([3,7,7,1],dIn=[0,1,2,3],dIntern=[],dOut=[1,2,3])
			net = pyrenn.train_LM(X,y,net,verbose=verbose,k_max=k_max,E_stop=E_stop)
			
			#y_train_predict = pyrenn.N
			predict = pyrenn.NNOut(Xpredict,net)
			time_object = datetime.strptime(combinedData[2][k][m], '%Y/%m/%d  %H:%M') 
			time_object_next = time_object + timedelta(days = 1)
			date_predict[0][0]= time_object_next
			date_predict[0][1]= predict[0]
			date_predict[0][2]= combinedData[1][k][m]
			#print('predict result:\n {0}'.format(predict))

			date_predict_array= np.append(date_predict_array,date_predict,0)

			predict_array[k] = predict[0]
			real_array[k] = combinedData[0][k][m+1][n_features-1]
			print('predict[{0}]: {1}\nreal[{0}]:    {2}\n'.format(k,predict_array[k],real_array[k]))
		
		mape = MAPE(predict_array,real_array,test_loop)
		nmse = NMSE(predict_array,real_array,test_loop)
		mae = MAE(predict_array,real_array,test_loop)
		print('Test prediction MAE: {0}'.format(mae))
		print('Test prediction NMSE: {0}'.format(nmse))
		with open(output_file_name, 'a') as csvfile:
			
			spamwriter = csv.writer(csvfile, delimiter=',')			
			csvfile.write(str(m) + ' prediction NMSE is:  {0}, MAE is:  {1}\nMAPE is: {2}\n'.format(nmse,mae,mape))

			spamwriter.writerow(["target_datetime", " predicted_value", " real_value"])
			for row in date_predict_array:
				spamwriter.writerow(row)

		avg_mae = avg_mae + mae
		avg_nmse = avg_nmse + nmse

rawData = sys.argv[3]
answerData = sys.argv[4]
combinedData = readcsv('../data/' +rawData ,'../data/' + answerData)
output_file_name = "../output/" + sys.argv[5]
with open(output_file_name, 'w') as csvfile:
	csvfile.write('')
print('processing...')
predict(verbose=True,k_max=int(sys.argv[2]),E_stop=1e-3)
print('output file dir: {0}'.format(output_file_name))
print('1st module finished.\n')

avg_mae = avg_mae/leave_loop
avg_nmse = avg_nmse/leave_loop
avg_mape = avg_mape/leave_loop
with open(output_file_name, 'a') as csvfile:
	
	csvfile.write('\nAverage NMSE is:  {0}\n'.format(avg_nmse))
	csvfile.write('Average MAE is:  {0}'.format(avg_mae))
	csvfile.write('Average MAE is:  {0}'.format(avg_mape))


