package tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CSVDataReader implements Iterator<DataInstance> {
	private String nextLine=""; // next line in the file
	private String nextLabel="";
	private static List<String> labels=new ArrayList<String>();
	private static List<String> lines=new ArrayList<String>();
	private BufferedReader in1; // source to be reading data from
	private BufferedReader in2; // source to be reading data from

	public CSVDataReader() {
		
	}
	public CSVDataReader(BufferedReader br) throws IOException {
			while((nextLine = br.readLine())!=null){
				lines.add(nextLine);
			}
	}
	public List<String> getLabelList(BufferedReader br) throws IOException{
			while((nextLabel = br.readLine())!=null){
				labels.add(nextLabel);
			}
			return labels;
	}
	public CSVDataReader(String datafile, String labelfile) {
		System.out.println(datafile+"???"+labelfile);
		labels=new ArrayList<String>();
		lines=new ArrayList<String>();
		try {
			in1 = new BufferedReader(new InputStreamReader(new FileInputStream(
					datafile)));
			in2 = new BufferedReader(new InputStreamReader(new FileInputStream(
					labelfile)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while(hasNext()){
				nextLine = in1.readLine();
				nextLabel = in2.readLine();
				if(nextLine==null)
					break;
				lines.add(nextLine);
				labels.add(nextLabel);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean hasNext() {
		return nextLine != null;
	}

	@Override
	public DataInstance next() {
		DataInstance data = null;
		if (hasNext()) {
			try {
				nextLine = in1.readLine();
				nextLabel = in2.readLine();
				lines.add(nextLine);
				labels.add(nextLabel);
				data = parseCSVExample(nextLine,nextLabel);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	public DataInstance parseCSVExample(String line, String label) {
		DataInstance di = new DataInstance();
		String[] attrs = line.split(",");

		di.setLabel(label);
		int featureIndex = 0;

		for (int i = 0; i < attrs.length; i++) {
			di.addFeature(featureIndex, Double.parseDouble(attrs[i]));
			featureIndex++;
		}
		return di;
	}

	public static DataInstance parseCSVTrain(String text) {
		DataInstance di = new DataInstance();
		String[] attrs = text.split(",");
		int featureIndex = 0;
		for (int i = 0; i < attrs.length; i++) {
			if (i != attrs.length - 1) {
				di.addFeature(featureIndex, Double.parseDouble(attrs[i]));
				featureIndex++;
			}else{
				di.setLabel(attrs[i]);
			}
		}
		return di;
	}

	public List<DataInstance> parseCSVAllExample() {
		List<DataInstance> dilist = new ArrayList<DataInstance>();
		for (int j = 0; j < lines.size(); j++) {
			DataInstance di = parseCSVExample(lines.get(j),"");
			dilist.add(di);
		}
		return dilist;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}
}
