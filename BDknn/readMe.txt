#How to run this program?

hadoop jar BDknn.jar /user/hue/knn/input/ /user/hue/knn/output 5

#driver: to set input and output path, and the mapoutputvalue

#mapper: each map function deals with all the test cases and one training data a time.
The mapper input is all the training data, and the output key is test case's index, and its value is the distance and label.
The distance is computed by Euclidean distance.

#combiner: Each key, that is one test case, has several corresponding distance value. Sort the distance in ascending order, and 
write the first k distances into context.

#reducer: Vote for the labels whose distance is within the reach of the test case. Find the maximum vote, and make it as the label of this
test case.


#CSVDataReader: To read the test data file and return data instances.
#DataInstance: to record the iris data, including its features and label.
#DistanceLabel: to store the distance and label in one class.


#Further Analysis:
1.The label result in some degree depends on the k value. So, it's critical to choose a right k.
2.Here, I used Euclidean distance.