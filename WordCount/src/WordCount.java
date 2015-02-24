import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

	public static class TokenizerMapper extends
			Mapper<Object, Text, Text, IntWritable> {

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		private Text test=new Text();
		protected void setup(
	            org.apache.hadoop.mapreduce.Mapper<Object, Text, Text, IntWritable>.Context context)
	            throws java.io.IOException, InterruptedException {
	        System.out.print("loading shared comparison vectors...");

	        // load the test vectors
	        FileSystem fs = FileSystem.get(context.getConfiguration());
	        String abc=context.getConfiguration().get("abc");
	        BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(new Path(context.getConfiguration().get("abc")))));
	        String line = br.readLine();
	        int count = 0;
	        while (line != null) {
	        	System.out.println(line);
	        	line=br.readLine();
	        }
	        System.out.println("done.");
	    }
		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {

			System.err.println(value.toString());
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				System.err.println(word.toString());
				word.set(itr.nextToken());
				context.write(word, one);
			}
			context.write(test, one);
		}
	}

	public static class IntSumReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			int sum = 0;
			// System.err.println(key);
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("abc", args[0]);
		Job job = Job.getInstance(conf, "word count");
//		JobConf conff = new JobConf(WordCount.class);
		
		job.setJarByClass(WordCount.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}