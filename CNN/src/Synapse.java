public class Synapse {
	private double weight = Math.random() * 1/9;
	private double lastOut;
	
	public void adjustWeight(double adjustment) {
		weight += adjustment;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double newWeight) {
		weight = newWeight;
	}
	public double getOutput (double input) {
		lastOut = input * weight;
		return lastOut;
	}
	public double getLastOut() {
		return lastOut;
	}
}
