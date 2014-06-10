package ustc.sse.datamining.algo.kmeans;
import java.util.Set;


public class Center {
	public Center(double x, double y, int id, Set<Line> lines) {
		super();
		this.x = x;
		this.y = y;
		this.id = id;
		this.lines = lines;
	}
	private double x;
	private double y;
	private int id;
	Set<Line> lines;
	boolean foreverAlone = false;
	
	public void addLines(Line line) {
		lines.add(line);
	}
	public boolean isForeverAlone() {
		return foreverAlone;
	}
	public void setForeverAlone(boolean foreverAlone) {
		this.foreverAlone = foreverAlone;
	}
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Set<Line> getLines() {
		return lines;
	}
	public void setLines(Set<Line> lines) {
		this.lines = lines;
	}
	public String toString(){
		return "Center<"+id+"> [" + x +", " + y+"]";
	}

	
}
