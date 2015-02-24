package tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class DataInstance {
	HashMap<Integer, Double> sparseData;
	private String label;  // the label (assuming it has one)
	public HashMap<Integer, Double> getSparseData() {
		return sparseData;
	}
	public void setSparseData(HashMap<Integer, Double> sparseData) {
		this.sparseData = sparseData;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public DataInstance(){
		sparseData = new HashMap<Integer, Double>();
	}
	
	public DataInstance(DataInstance e){
		// copy everything
		label = e.label;
		sparseData = new HashMap<Integer,Double>(e.sparseData);
	}
	public void addFeature(int featureNum, double value){
		sparseData.put(featureNum, value);
	}
		
	/**
	 * Get the value associated with this feature.
	 * 
	 * @param featureNum
	 * @return the value for featureNum for this example
	 */
	public double getFeature(int featureNum){
		return sparseData.containsKey(featureNum) ? sparseData.get(featureNum) : 0.0;
	}
	
	/**
	 * Set the values of the associated features with feature index featureNum.
	 * 
	 * @param featureNum
	 * @param value
	 */
	public void setFeature(int featureNum, double value){
		sparseData.put(featureNum, value);
	}
	
	/**
	 * Get all the features that this example has (indices).
	 * 
	 * @return the set of features
	 */
	public Set<Integer> getFeatureSet(){
		return sparseData.keySet();
	}
	
	public boolean equalFeatures(DataInstance other){
		return sparseData.equals(other.sparseData);
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(label);
		
		ArrayList<Integer> indices = new ArrayList<Integer>(sparseData.keySet());
		Collections.sort(indices);
		
		for( int featureIndex: indices){
			buffer.append(" " + featureIndex + ":" + sparseData.get(featureIndex));
		}
		
		return buffer.toString();
	}
	
	public String toString(HashMap<Integer, String> featureMap){
		StringBuffer buffer = new StringBuffer();
		buffer.append(label);
		
		ArrayList<Integer> indices = new ArrayList<Integer>(sparseData.keySet());
		Collections.sort(indices);
		
		for( int featureIndex: indices){
			buffer.append(" " + featureMap.get(featureIndex) + ":" + (sparseData.get(featureIndex)));
		}
		
		return buffer.toString();
	}

	/**
	 * CSV representation of this example
	 * 
	 * @return csv representation
	 */
	public String toCSVString(){
		StringBuffer buffer = new StringBuffer();
		
		ArrayList<Integer> indices = new ArrayList<Integer>(sparseData.keySet());
		Collections.sort(indices);
		
		for( int featureIndex: indices){
			buffer.append(sparseData.get(featureIndex) + ",");
		}
		
		buffer.append(label);
		
		return buffer.toString();
	}
	
	
	
	
}
