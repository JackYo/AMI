from datetime import datetime, timedelta

time_object = datetime.strptime("2015/1/1  00:00:00", '%Y/%m/%d  %H:%M:%S')
for i in range(0,96):
	time_object = time_object + timedelta(minutes = 15)
	print(datetime.strftime(time_object, '%Y/%m/%d  %H:%M:%S'))