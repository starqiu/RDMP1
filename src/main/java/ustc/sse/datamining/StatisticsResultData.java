package ustc.sse.datamining;


public class StatisticsResultData {

	private double precision = 0;
	private double recall = 0;
	private double F1Score = 0;
	
	public double getPrecision() {
		return precision;
	}
	public void setPrecision(double precision) {
		this.precision = precision;
	}
	public double getRecall() {
		return recall;
	}
	public void setRecall(double recall) {
		this.recall = recall;
	}
	public double getF1Score() {
		return F1Score;
	}
	public void setF1Score(double f1Score) {
		F1Score = f1Score;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(F1Score);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(precision);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(recall);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		StatisticsResultData other = (StatisticsResultData) obj;
		if (Double.doubleToLongBits(F1Score) != Double
				.doubleToLongBits(other.F1Score))
			return false;
		if (Double.doubleToLongBits(precision) != Double
				.doubleToLongBits(other.precision))
			return false;
		if (Double.doubleToLongBits(recall) != Double
				.doubleToLongBits(other.recall))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "StatisticsResultDate [precision=" + precision + ", recall="
				+ recall + ", F1Score=" + F1Score + "]";
	}

	
}
