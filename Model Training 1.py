#for model training
import tensorflow as tf
#for creating datasets
import numpy as np
#creating a neural network
from tensorflow import keras
#converting tensorflow to tensoflow lite
from tensorflow import lite


#DATASET
# y = 2x-1
# x is a feature
x = np.array([-1.0,0.0,1.0,2.0,3.0,4.0], dtype = float) 
# y is a lable
y = np.array([-3.0,-1.0,1.0,3.0,5.0,7.0], dtype = float)



model = keras.Sequential([keras.layers.Dense(units = 1, input_shape=[1]), keras.layers.Dense(units =1, input_shape=[1])])
#2 layered Keras which works as CNNs
model.compile(optimizer ="sgd", loss= 'mean_squared_error')

#training for 500 times
model.fit(x,y,epochs=500)
#model testing using a detemined value
print(model.predict([10]))

kearas_file  = 'linear.h5'
tf.keras.models.save_model(model, kearas_file)
converter = lite.TFLiteConverter.from_keras_model(model)
tfmodel = converter.convert()
open("linear.tflite","wb").write(tfmodel)