
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class KNNDriver extends Configured implements Tool {
	public static final int DEFAULT_K = 3;

	public static final String K_CONF = "k";
	public static final String TEST_EXAMPLE_CONF = "testexample";
	public static final String TEST_DATA_CONF = "test_data";
	public static final String TEST_DATA_FILE = "iris_test_data.csv";
	public static final String TEST_LABEL_CONF = "test_label";
	public static final String TEST_LABEL_FILE = "iris_test_label.csv";
	public static final String TRAIN_SET_CONF = "train_set";
	public static final String TRAIN_SET_FILE = "iris_train.csv";

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new KNNDriver(), args);
		System.exit(res);
	}

	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = getConf();
		conf.set(TEST_DATA_CONF, args[0]+TEST_DATA_FILE);
		conf.set(TEST_LABEL_CONF, args[0]+TEST_LABEL_FILE);
		conf.set(TRAIN_SET_CONF, args[0]+TRAIN_SET_FILE);//train/iris_train.csv
		conf.setInt(K_CONF, Integer.parseInt(args[2]));
		
		Job job = Job.getInstance(conf, "KNN Classifier");
		job.setJarByClass(KNNDriver.class);

		job.setMapperClass(KNNMapper.class);
		job.setCombinerClass(KNNCombiner.class);
		job.setReducerClass(KNNReducer.class);
		
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);//
		job.setOutputKeyClass(Text.class);
	 	job.setOutputValueClass(Text.class);

         FileInputFormat.addInputPath(job, new Path(args[0]+TRAIN_SET_FILE));
         Path out = new Path(args[1]);
         FileSystem.get(conf).delete(out, true);
         FileOutputFormat.setOutputPath(job, out);
         //set the current testing file
         int res = job.waitForCompletion(true) ? 0 : 1;
         if (res != 0) {
             return res;
         }
		return 0;
	}

}
