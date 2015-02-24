import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Mapper;

import tools.CSVDataReader;
import tools.DataInstance;

public class KNNMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
	private List<DataInstance> testlist;
	private static Text label = new Text();
	private static DoubleWritable distance = new DoubleWritable();
	CSVDataReader reader;
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws java.io.IOException, InterruptedException {
		// calculate the distance for each test sample with the training
		// data
		for (DataInstance di : testlist) {
			DataInstance other = CSVDataReader.parseCSVTrain(value.toString());
			label.set(other.getLabel());
			distance.set(getSimilarity(di, other)); // larger values will show
			// up first
			// testing data+training data, and training data is changing
			System.err.println(testlist.indexOf(di) + ":" + di.toCSVString()
					+ "|" + distance + "|" + other.toCSVString());
			IntWritable marker = new IntWritable(testlist.indexOf(di));
			Text val = new Text(distance.toString() + "@" + label);
			context.write(marker, val);
		}
		// System.out.println("");

	}

	protected void cleanup(
			Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws java.io.IOException, InterruptedException {
		// test.close();
	}

	;

	protected void setup(
			Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws java.io.IOException, InterruptedException {
		FileSystem fs = FileSystem.get(context.getConfiguration());
		String test_data_file = context.getConfiguration().get(
				KNNDriver.TEST_DATA_CONF);
//		System.out.println(test_data_file + "?????" + test_label_file);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				fs.open(new Path(test_data_file))));
		reader = new CSVDataReader(br);
		testlist = reader.parseCSVAllExample();

	};

	private static double getSimilarity(DataInstance e1, DataInstance e2) {
		double dist = 0.0;
		// for now (though this isn't correct for sparse data sets) assume
		// features set of e1 = feature set of e2
		for (Integer featureNum : e1.getFeatureSet()) {
			double diff = e1.getFeature(featureNum) - e2.getFeature(featureNum);
			dist += diff * diff;
		}

		return Math.sqrt(dist);
	};
	
}


