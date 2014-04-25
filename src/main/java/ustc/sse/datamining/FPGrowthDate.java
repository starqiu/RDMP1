package ustc.sse.datamining;

import java.util.LinkedList;

public class FPGrowthDate {
	private double support = 0;
	private double confidence = 0;
	private LinkedList<Integer> fromItems = new LinkedList<Integer>();
	private LinkedList<Integer> toItems = new LinkedList<Integer>();
	public double getSupport() {
		return support;
	}
	public void setSupport(double support) {
		this.support = support;
	}
	public double getConfidence() {
		return confidence;
	}
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	public LinkedList<Integer> getFromItems() {
		return fromItems;
	}
	public void setFromItems(Integer fromItems) {
		this.fromItems.add(fromItems);
	}
	public LinkedList<Integer> getToItems() {
		return toItems;
	}
	public void setToItems(Integer toItems) {
		this.toItems.add(toItems);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(confidence);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((fromItems == null) ? 0 : fromItems.hashCode());
		temp = Double.doubleToLongBits(support);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((toItems == null) ? 0 : toItems.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FPGrowthDate other = (FPGrowthDate) obj;
		if (Double.doubleToLongBits(confidence) != Double
				.doubleToLongBits(other.confidence))
			return false;
		if (fromItems == null) {
			if (other.fromItems != null)
				return false;
		} else if (!fromItems.equals(other.fromItems))
			return false;
		if (Double.doubleToLongBits(support) != Double
				.doubleToLongBits(other.support))
			return false;
		if (toItems == null) {
			if (other.toItems != null)
				return false;
		} else if (!toItems.equals(other.toItems))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "DateFPGrowth [support=" + support + ", confidence="
				+ confidence + ", fromItems=" + fromItems + ", toItems="
				+ toItems + "]";
	}
	
	
}
