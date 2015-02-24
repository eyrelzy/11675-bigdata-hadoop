import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import tools.CSVDataReader;
import tools.DataInstance;


public class KNNReducer extends Reducer<IntWritable, Text, Text, Text> {
	private static int k = KNNDriver.DEFAULT_K;
	private HashMap<String,Integer> labelCounts = new HashMap<String,Integer>();
	private HashMap<String,Double> distanceCounts = new HashMap<String,Double>();
	CSVDataReader reader;
	private List<DataInstance> testlist;
//	private List<String> labels;
	
	@Override
	protected void setup(Reducer<IntWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		super.setup(context);
		FileSystem fs = FileSystem.get(context.getConfiguration());
		String test_data_file = context.getConfiguration().get(KNNDriver.TEST_DATA_CONF);
//		String test_label_file = context.getConfiguration().get(KNNDriver.TEST_LABEL_CONF);
		BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(new Path(test_data_file))));
		reader = new CSVDataReader(br);
		testlist = reader.parseCSVAllExample();
//		br = new BufferedReader(new InputStreamReader(fs.open(new Path(test_data_file))));
//		labels=reader.getList(br, 1);
	}
;
	@Override
	protected void reduce(
			IntWritable key, 
			Iterable<Text> values,
            Reducer<IntWritable, Text,Text, Text>.Context context)
            throws java.io.IOException, InterruptedException {
		k = context.getConfiguration().getInt(KNNDriver.K_CONF, k);
		int test_index=key.get();
		System.err.println(test_index);
		String test_data=testlist.get(test_index).toCSVString();
		Iterator<Text> it=values.iterator();
		while(it.hasNext()){
			String val=it.next().toString();
			String[] dist_label=val.split("@");
			double dist=Double.parseDouble(dist_label[0]);
			String label=dist_label[1];
			//count labels
			System.err.println(key+"@"+label+"@"+dist);
			if(labelCounts.containsKey(label)){
				labelCounts.put(label,labelCounts.get(label)+1);
				distanceCounts.put(label,distanceCounts.get(label)+dist);
			}else{
				labelCounts.put(label,1);
				distanceCounts.put(label, dist);
			}
		}
		// record the value... we're done!
		String maxLabel = getMax(labelCounts,distanceCounts);
		//output <data,label)
		context.write(new Text(test_data),new Text(maxLabel));
		labelCounts=new HashMap<String, Integer>();
		distanceCounts=new HashMap<String, Double>();
    }

    ;
    private static String getMax(HashMap<String,Integer> counts, HashMap<String, Double> distances){
		String maxLabel = "";
		List<Map.Entry<String,Integer>> list=new ArrayList<Entry<String, Integer>>();  
        list.addAll(counts.entrySet());  
        ValueComparator vc=new ValueComparator();  
        Collections.sort(list,vc); 
		System.err.println(list);
		maxLabel=list.get(0).getKey();
		int count=list.get(0).getValue();
		double avg_dist=distances.get(maxLabel);
		
		for(int i=1;i<list.size();i++){
			if(list.get(i).getValue()==count){
				double dist=distances.get(list.get(i).getKey());
				if(dist<avg_dist){
					avg_dist=dist;
					maxLabel=list.get(i).getKey();
				}
				
			}else{
				break;
			}
		}
		return maxLabel;
	}
	private static class ValueComparator implements Comparator<Map.Entry<String,Integer>>  
    {  
        public int compare(Map.Entry<String,Integer> m,Map.Entry<String,Integer> n)  
        {  
            return n.getValue()-m.getValue();  
        }  
    }
}
