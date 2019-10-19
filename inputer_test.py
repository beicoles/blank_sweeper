import numpy as np
import csv

from sklearn.preprocessing import Imputer

imp = Imputer(missing_values=np.nan, strategy='mean')

path = 'D:\\Project\\nasa\\Fireball_And_Bolide_Reports.csv'
output_path = 'D:\\Project\\nasa\\Fireball_And_Bolide_Reports_output.csv'
header = []
train = []
origin = []
with open(path) as csvfile:
	reader = csv.reader(csvfile, delimiter=',')
	header = next(reader, None)
	for row in reader:
		row = [i if i != '' else np.nan for i in row]
		train.append(row[3:8])
		origin.append(row)
	imp.fit(train)

with open(output_path, 'w', newline='') as csvfile:
	writer = csv.writer(csvfile)
	writer.writerow(header)
	for row in origin:
		trans = imp.transform([row[3:8]])
		row[3:8] = trans[0]
		writer.writerow(row)
		print(row)