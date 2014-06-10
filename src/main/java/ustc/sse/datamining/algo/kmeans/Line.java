package ustc.sse.datamining.algo.kmeans;

public class Line {
	public Line(Center source, Center target) {
		super();
		this.source = source;
		this.target = target;
		this.k=0;
		this.b=0;
	}
	public Line(Center source, Center target, double k, double b) {
		super();
		this.source = source;
		this.target = target;
		this.k = k;
		this.b = b;
	}
	private Center source;
	private Center target;
	private double k;
	private double b;
	private Point start;
	private Point end;
	private int classifier;
	public Center getSource() {
		return source;
	}
	public void setSource(Center source) {
		this.source = source;
	}
	public Center getTarget() {
		return target;
	}
	public void setTarget(Center target) {
		this.target = target;
	}
	public double getK() {
		return k;
	}
	public void setK(double k) {
		this.k = k;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	public String toString(){
		return "Line< "+k+"*x+"+b + " > for ("+source.getId() + " | " + target.getId()+")";
	}
	public int getClassifier() {
		return classifier;
	}
	public void setClassifier(int classifier) {
		this.classifier = classifier;
	}
	public Point getStart() {
		return start;
	}
	public void setStart(Point start) {
		this.start = start;
	}
	public Point getEnd() {
		return end;
	}
	public void setEnd(Point end) {
		this.end = end;
	}
	
}
