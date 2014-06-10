package ustc.sse.datamining.algo.kmeans;

public class Node {
	public Node(int id, double x, double y) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.center = null;
	}
	private int id;
	private double x;
	private double y;
	private Center center;
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
	public Center getCenter() {
		return center;
	}
	public void setCenter(Center center) {
		this.center = center;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String toString(){
		return "node<"+id+"> ["+x+", "+y+"] " + ((center==null)?"N/A":center.getId());
	}
}
