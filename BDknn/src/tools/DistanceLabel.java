package tools;

public class DistanceLabel implements Comparable<DistanceLabel> {

	
	private String label;
	private double distance;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public DistanceLabel(){
		
	}
	public DistanceLabel(String label,double distance){
		this.label=label;
		this.distance=distance;
	}
	@Override
	public int compareTo(DistanceLabel dl) {
		// TODO Auto-generated method stub
		return this.distance > dl.distance ? 1 : (this.distance < dl.distance ? -1 : 0);
	}

}
