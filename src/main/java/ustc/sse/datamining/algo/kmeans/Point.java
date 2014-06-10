package ustc.sse.datamining.algo.kmeans;

public class Point {
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	private double x;
	private double y;
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String toString(){
		return "["+x+", "+y+"]";
	}
}
