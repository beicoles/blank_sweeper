
# coding: utf-8

# In[ ]:

import json
import csv
import numpy as np
import argparse
import logging
from impyute.imputation.cs import fast_knn
#import run_config


# In[ ]:

config = 'column_config.json'
data_input = 'Fireball_And_Bolide_Reports.csv'
data_output = 'Fireball_And_Bolide_Reports.csv'
input_path = 'input\\'
tmp_path = 'tmp\\'
output_path = 'output\\'


# In[ ]:

#run_config.run(data_input, config, data_tmp)


# In[ ]:

def build_argsparser():
    parser = argparse.ArgumentParser(description='X TEAM data filler')
    parser.add_argument('--input', type=str, default='Fireball_And_Bolide_Reports.csv', help='123')
    parser.add_argument('--output', type=str, default='Fireball_And_Bolide_Reports.csv', help='234')
    parser.add_argument('--strategy', type=str, default='knn', help='345')
    parser.add_argument('--config', type=str, default='column_config.json', help='456')
    return parser


# In[ ]:

def isfloat(value):
  try:
    float(value)
    return True
  except ValueError:
    return False


# In[ ]:

opt = build_argsparser().parse_args()
data_input = opt.input
data_output = opt.output


# In[45]:

print('Reading input data from {}'.format(data_input))
header = []
column_type = [] # 0 for class, 1 for digit
train_digit = []
train_class = []
origin = []
# assume that the csv file contains at least 1 row and all features in 1st line are filled
with open(input_path+data_input) as csvfile:
    reader = csv.reader(csvfile, delimiter=',')
    header = next(reader, None)
    column_type = [0] * len(header)
    
    row = next(reader, None)
    for i, v in enumerate(row):
        if isfloat(v):
            column_type[i] = 1
    # 1st row is skipped
    
    for row in reader:
        row_digit = [float(v) if v != '' else np.nan for i, v in enumerate(row) if column_type[i] == 1]
        row_class = [v if v != '' else np.nan for i,v in enumerate(row) if column_type[i] == 0]
        row = [i if i != '' else np.nan for i in row]
        train_digit.append(row_digit)
        train_class.append(row_class)
        origin.append(row)
train_nd = np.asarray(train_digit)


# In[41]:

result = fast_knn(train_nd, k=5)


# In[52]:

train_digit = [list(row) for row in result]
train = []
for i, row in enumerate(train_class):
    train.append(row+train_digit[i])


# In[ ]:



