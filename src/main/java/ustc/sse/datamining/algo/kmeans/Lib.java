package ustc.sse.datamining.algo.kmeans;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Lib {
	private static Lib lib = null;
	private int k, num_of_node;
	private int width = 500;
	/*********************************************************************************************
	 ********************				   Line Function		  			**********************
	 *********************************************************************************************/
	public void calculateLines(ArrayList<Center> centerSet) {
		int count = 1;
		for(Center center:centerSet){
			center.getLines().clear();
		}
		for(Iterator<Center> centerIterator = centerSet.iterator(); centerIterator.hasNext();){
			Center source = centerIterator.next();
			for(int i = count; i<centerSet.size();i++){
				Center target = centerSet.get(i);
				Line line = findMidLine(source, target);
				source.addLines(line);
				target.addLines(new Line(target, source, line.getK(), line.getB()));
			}
			count++;
		}
	}
	private Line findMidLine(Center source, Center target) {
		double gradient =-1/ calcGradient(source, target);
		double midx = (source.getX()+target.getX())/2.0;
		double midy = (source.getY()+target.getY())/2.0;
		double b = calcB(midx, midy, gradient);
		
		return new Line(source, target, gradient, b);
	}
	
	private double calcB(double x, double y,double gradient){
		return y-x*gradient;
	}
	private double calcGradient(Center source, Center target){
		if(target.getY() - source.getY() ==0)
			return 0;
		if(target.getX()-source.getX()==0)
			return Double.MIN_VALUE;
			
		double gradient = (target.getY() - source.getY())/(target.getX()-source.getX());
		return gradient;
	}
	/*********************************************************************************************
	 ********************			   Classification Function		  		**********************
	 *********************************************************************************************/
	public void chooseYourSide(ArrayList<Center> centerSet, Set<Node> nodeSet) {
		for(Center center:centerSet){
			for(Line line:center.getLines()){
				line.setClassifier(testByLine(center.getX(), center.getY(), line));
			}
			for(Node node:nodeSet){
				boolean belone = true;
				for(Line line:center.getLines()){
					if(!testByLine(node, line, line.getClassifier())){
						belone = false;
						break;
					}
				}
				if(belone)
					node.setCenter(center);
			}
		}
	}
	private boolean testByLine(Node node,Line line,int side){	
		return (testByLine(node.getX(), node.getY(), line)==side)?true:false;
	}
	private int testByLine(double x, double y,Line line){
		double lineY = getY(x, line.getK(), line.getB());
		return (y>lineY)?1:-1;
	}
	/*********************************************************************************************
	 ********************			   Re-Position Function			  		**********************
	 *********************************************************************************************/
	public double reposition(ArrayList<Center> centerSet, Set<Node> nodeSet,boolean kmodes) {
		double diff = 0;
		if(!kmodes){
			double[] x = new double[centerSet.size()];
			double[] y = new double[centerSet.size()];
			double[] count =  new double[centerSet.size()];
			
			for(Node node :nodeSet){
				x[node.getCenter().getId()]+=node.getX();
				y[node.getCenter().getId()]+=node.getY();
				count[node.getCenter().getId()]++;
			}
			
				for(Center center:centerSet){
					if(count[center.getId()]==0){
						center.setForeverAlone(true);
						continue;
					}
					center.setForeverAlone(false);
					double x_new = x[center.getId()]/count[center.getId()];
					double y_new = y[center.getId()]/count[center.getId()];
					
					diff+=Math.sqrt((Math.pow(x_new-center.getX(), 2)) + (Math.pow(y_new-center.getY(), 2)));
					center.setX(x_new);
					center.setY(y_new);
				}
			}
		else{
			diff = kmodesMethod(centerSet,nodeSet);
		}
		return diff;
	}
	// K-modes
	private double kmodesMethod(ArrayList<Center> centerSet, Set<Node> nodeSet) {
		double diff=0;
		ArrayList<ArrayList<Cell>> distances = new ArrayList<ArrayList<Cell>>(centerSet.size());
		int[] count = new int[centerSet.size()];
		for(Node node:nodeSet){
			count[node.getCenter().getId()]++;
		}
		for(int i=0;i< centerSet.size();i++){
			distances.add(new ArrayList<Lib.Cell>());
		}
		for(Node node:nodeSet){
			double dis = node.getX()+node.getY();
			int id = node.getCenter().getId();
			distances.get(id).add(new Cell(node, dis));
		}
		for(Center center:centerSet){
			if(count[center.getId()]==0){
				center.setForeverAlone(true);
				continue;
			}
			center.setForeverAlone(false);
			ArrayList<Cell> cells = distances.get(center.getId());
			
			Collections.sort(cells);
			Cell cell = cells.get(getMid(cells.size()));
			
			
			Node midNode = cell.node;
			//Node midNode = distances.get(center.getId()).get(distances.get(center.getId()).size()-1).node;
			double x_new = midNode.getX();
			double y_new = midNode.getY();
			diff+= pointDistance(center.getX(), center.getY(), x_new, y_new);
			center.setX(x_new);
			center.setY(y_new);
		}
		return diff;
	}
	class Cell implements Comparable<Cell>{
		Node node;
		double distance;
		public Cell(Node node, double distance) {
			this.node = node;
			this.distance =distance;
		}
		@Override
		public int compareTo(Cell c2) {
			if(c2.distance > this.distance)
				return 1;
			else {
				return -1;
			}
		}
	}
	private int getMid(int size){
		return size/2;
	}
	/*********************************************************************************************
	 ********************				   Auxiliary Function		  		**********************
	 *********************************************************************************************/
	public Node farestNode(Set<Node> nodes, ArrayList<Center>centers){
		Node farest = null;
		double MaxDis = Double.MIN_VALUE;
		for(Node node : nodes){
			double dis = calculateInitialDistance(node, centers);
			if(dis > MaxDis){
				farest = node;
				MaxDis = dis;
			}
		}
		return farest;
	}
	public double calculateInitialDistance(Node node, ArrayList<Center> centers){
		double distance = 0;
		if(node==null | centers.size()==0)
			return distance;
		for(Center center :centers){
			distance+= pointDistance(node.getX(), node.getY(), center.getX(), center.getY());
		}
		return distance;
	}
	public double round(double num, int digit){
		//BigDecimal   b   =   new   BigDecimal(num);    
		//return  b.setScale(digit,BigDecimal.ROUND_HALF_UP).doubleValue();  
		return num;
	}
	public void print(Set<Node> nodes, ArrayList<Center> centers) {
		if(centers!=null){
			System.out.println("-------------  Centers --------- : " + centers.size());
			for(Center center:centers){
				System.out.println(center.toString());
			}
		}
		if(nodes!=null){
			System.out.println("-------------  Nodes --------- : " + nodes.size());
			for(Node node:nodes){
				System.out.println(node.toString());
			}
		}
	}
	public double getY(double x, double k, double b){
		return (k*x+b);
	}
	public Node getRandomNode(Set<Node> nodeSet,Random random) {
		Node n = null;
		for(Node node:nodeSet){
			n = node;
			if(random.nextDouble()<0.2)
				return node;
		}
		return n;
	}
	/*********************************************************************************************
	 ********************				   Help Paint		  				**********************
	 *********************************************************************************************/
	public void fixLine(Center theCenter) {
		Point center = new Point(theCenter.getX(), theCenter.getY());
		for(Line source:theCenter.getLines()){
			for(Line target:theCenter.getLines()){
				if(target!=source){
					Point cross = getCrossPoint(source, target);
					if(isHigher(source.getStart(), cross) && !isHigher(source.getEnd(), cross)){
						if(isHigher(center, center))
							source.setEnd(cross);
						else 
							source.setStart(cross);		
					}
				}
			}
		}
	}
	public Point getCrossPoint(Line source, Line target) {
		double x = (-1)*((scale(scale(target.getB())-source.getB())/(source.getK()-target.getK())));
		double y = getY(x, source.getK(), scale(source.getB()));
		return new Point(x, y);
	}
	
	private boolean isHigher(Point source , Point target){
		return (source.getY()>target.getY())?true:false;
	}
	/*private double pointDistance(Point source , Point target){
		return pointDistance(source.getX(), source.getY(), target.getX(), target.getY());
	}*/
	private double pointDistance(double sx, double sy, double tx, double ty){
		return Math.sqrt((Math.pow(sx-tx, 2)) + (Math.pow(sy-ty, 2)));
	}
	private double scale(double value){
		return (value*(width/num_of_node));
	}
	/*********************************************************************************************
	 ********************				   Constructor		  				**********************
	 *********************************************************************************************/
	public int getK() {
		return k;
	}
	public void setK(int k) {
		this.k = k;
	}
	public int getNum_of_node() {
		return num_of_node;
	}
	public void setNum_of_node(int num_of_node) {
		this.num_of_node = num_of_node;
	}
	private Lib(){}
	public static Lib getInstance() {
		if(lib == null){
			lib = new Lib();
			lib.printComment();
		}
		return lib;
	}
	public Node createRandomNode(Node node, int num_of_node2, int remain,Random rnd) {
		if(node==null)
			return new Node(0, lib.round((rnd.nextDouble())*num_of_node, 2),lib.round(rnd.nextDouble()*num_of_node, 2));
		double x = round(node.getX()+(rnd.nextDouble()-0.5)*Math.sqrt(num_of_node),2);
		double y = round(node.getY()+(rnd.nextDouble()-0.5)*Math.sqrt(num_of_node),2);
		if(x < 0)
			x += num_of_node2;
		if(y<0)
			y += num_of_node2;
		if(x>num_of_node2)
			x -= num_of_node2;
		if(y>num_of_node2)
			y-=num_of_node2;
		return new Node(num_of_node2-remain, x, y);
	}
	
	private void printComment(){
		
	System.err.println("* Implement K-means algorithm with visual motion "
			+ "\n* parameters (parameter should have [-x and number] except [-l(L)])"
					+ "\n* parameters can occur any combination, "
					+ "\n* if not set, system will use default setting;\n* "
					+ "\n* -k 8: means total 8 clusters "
					+ "\n* -n 90: means 90 random nodes"
					+ "\n* -m 25: max run round is 25, stop execute if more than 25 iterations"
					+ "\n* -t 0.001: terminate condition, if changing less than threshold, then no more execution"
					+ "\n* -s 1000: set sleep time, how fast the animation run"
					+ "\n* -l : show line or not"
					+ "\n* -d : <trun off when set> when initial center, "
					+ "\n* 	set new center stay farest distance with current centers (better result)"
					+ "\n* -seed 1234: specify the random SEED, if seed are same every time, the results will be same"
					+ "\n* -mode : <turn on when set> change to k-modes, which use mid node to measure than user mean"
					+ "\n* "
					+ "\n* Author: zhangjie "
					+ "\n* Complete Time: 2014/5/30\n");
	}	
}
