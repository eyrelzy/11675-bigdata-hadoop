import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import tools.DistanceLabel;


public class KNNCombiner extends Reducer<IntWritable, Text,IntWritable, Text> {
	private static int k = KNNDriver.DEFAULT_K;
	private List<DistanceLabel> dllist=new ArrayList<DistanceLabel>();
	
	@Override
	protected void reduce(
			IntWritable key,
			Iterable<Text> values,
			Reducer<IntWritable, Text, IntWritable, Text>.Context context)
			throws java.io.IOException, InterruptedException {
		dllist=new ArrayList<DistanceLabel>();
		k = context.getConfiguration().getInt(KNNDriver.K_CONF, k);
		Iterator<Text> it=values.iterator();
		while(it.hasNext()){
			Text dw=it.next();
			String text=new String(dw.toString());
			String[] dist_label=text.split("@");
			double dist=Double.parseDouble(dist_label[0]);
			String label=dist_label[1];
			DistanceLabel dl=new DistanceLabel(label,dist);
			dllist.add(dl);
		}
		Collections.sort(dllist);
		System.err.println(dllist.size());
		for(int i=0;i<dllist.size()&&i<k;i++){
			double dist=dllist.get(i).getDistance();
			String label=dllist.get(i).getLabel();
			Text dw=new Text(dist+"@"+label);
//			System.err.println(key.toString()+"|"+dist+"@"+label);
			context.write(key, dw);
		}
	};
}
