package ustc.sse.datamining.algo.kmeans;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import ustc.sse.datamining.algo.kmeans.gui.GuiKmean;
/**
 * Implement K-means algorithm with visual motion
 * parameters (parameter should have "-x and number" except "-l(L)")
 * parameters can occur any combination, 
 * if not set, system will use default setting;
 * 
 * -k 8: means total 8 clusters 
 * -n 90: means 90 random nodes
 * -m 25: max run round is 25, stop execute if more than 25 iterations
 * -t 0.001: terminate condition, if changing less than threshold, then no more execution
 * -s 1000: set sleep time, how fast the animation run
 * -l : show line or not
 * -d : <trun off when set> when initial center, 
 * 		set new center stay farest distance with current centers (better result)
 * -seed 1234: specify the random SEED, if seed are same every time, the results will be same
 * -mode : <turn on when set> change to k-modes, which use mid node to measure than user mean
 * 
 * Author: zhangjie
 * Complete Time: 2014/5/30
 */


public class KMean implements Runnable{
	private int k = 6;
	private int num_of_node = 1000;
	private double threshold = 0.001;
	private double estimate = Double.MAX_VALUE;
	private int MaxRun = 50;
	private int sleep = 1000;
	private Set<Node> nodeSet;
	private ArrayList<Center> centerSet;
	private Lib lib;
	private GuiKmean guiKmean;
	private boolean showLine = false;
	private boolean farestSelect = true;
	private boolean kmodes = false;
	private long SEED = Long.MIN_VALUE;
	public static final String NUMBER_STRING = "^\\d+$";
	public static final String FLOAT_STRING = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
	
	public static void kmeans(String[] args) {
		KMean kMean = new KMean();
		kMean.init(args);
		//kMean.print();
		kMean.run();
		
	}

	

	private void init(String[] args) {
		lib = Lib.getInstance();
		if(args.length>0)
			parseParameters(args);
		
		lib.setK(k);
		lib.setNum_of_node(num_of_node);
		nodeSet = new HashSet<Node>();
		centerSet = new ArrayList<Center>();
		if(SEED == Long.MIN_VALUE)
			SEED = Calendar.getInstance().getTimeInMillis();
		Random rnd = new Random(SEED);
		// random node
		for(int i = 0; i<num_of_node/2; i++){
			nodeSet.add(new Node(i, lib.round((rnd.nextDouble())*num_of_node, 2), lib.round(rnd.nextDouble()*num_of_node, 2)));
		}
		// random cluster 
		int cluster = rnd.nextInt(k*2)+1;
		//System.out.println("Random Cluster : " + cluster);
		int remain = num_of_node - num_of_node/2;
		for(int i=0;i<cluster;i++){
			Node node = lib.getRandomNode(nodeSet,rnd);
			if(remain==0)
				break;
			while(remain>0){
				if(rnd.nextDouble()>0.9 && i<cluster-1){
					//System.out.println("Break " + i + " Remain: " +remain);
					break;
				}
				Node nearNode = lib.createRandomNode(node,  num_of_node, remain, rnd);
						
				nodeSet.add(nearNode);
				remain--;
				node = nearNode;
			}	
		}
		if(farestSelect){
			Center original  =  new Center(lib.round(rnd.nextDouble()*num_of_node, 2),
											lib.round(rnd.nextDouble()*num_of_node, 2),0, new HashSet<Line>());
			centerSet.add(original);
			for(int i = 1;i<k;i++){
				Node farestNode = lib.farestNode(nodeSet, centerSet);
				if(farestNode!=null){
					centerSet.add(new Center(farestNode.getX(), farestNode.getY(), i, new HashSet<Line>()));
				}else{
					centerSet.add(new Center(lib.round(rnd.nextDouble()*num_of_node, 2),
												lib.round(rnd.nextDouble()*num_of_node, 2),
													i, new HashSet<Line>()));
				}
			}
		}else{
			for(int i=0;i<k;i++){
				centerSet.add(new Center(lib.round(rnd.nextDouble()*num_of_node, 2),
						lib.round(rnd.nextDouble()*num_of_node, 2),
							i, new HashSet<Line>()));
			}
		}
		lib.calculateLines(centerSet);
		guiKmean = new GuiKmean(nodeSet, centerSet,showLine,SEED);
	}
	
	private void parseParameters(String[] args) {
		System.out.print("Parameter << ");
		for(String string:args){
			System.out.print(string+ " ");
		}
		System.out.print(">>\n\n" );
		try{
			for(int i = 0;i<args.length;i++){
				String str = args[i];
				if(str.equalsIgnoreCase("-k")){
					if(args[i+1].matches(NUMBER_STRING)){
						k = Integer.parseInt(args[i+1]);
					}
				}else if(str.equalsIgnoreCase("-n")){
					if(args[i+1].matches(NUMBER_STRING)){
						num_of_node = Integer.parseInt(args[i+1]);
					}
				}else if(str.equalsIgnoreCase("-m")){
					if(args[i+1].matches(NUMBER_STRING)){
						MaxRun = Integer.parseInt(args[i+1]);
					}
				}else if(str.equalsIgnoreCase("-t")){
					if(args[i+1].matches(FLOAT_STRING)){
						threshold = Double.parseDouble(args[i+1]);
					}
				}else if(str.equalsIgnoreCase("-s")){
					if(args[i+1].matches(NUMBER_STRING)){
						sleep = Integer.parseInt(args[i+1]);
					}
				}
				else if(str.equalsIgnoreCase("-l")){
					showLine  = true;
				}
				else if(str.equalsIgnoreCase("-mode")){
					kmodes  = true;
				}
				else if(str.equalsIgnoreCase("-seed")){
					if(args[i+1].matches(NUMBER_STRING)){
						SEED = Long.parseLong(args[i+1]);
					}
				}
			}
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}
	}

	/*private void print() {
		lib.print(nodeSet, centerSet);
	}*/

	@Override
	public void run() {
		int count = 0;
		try {
			Thread.sleep(sleep*2);
			re_paint rPaint = new re_paint();
			rPaint.setGuiKmean(guiKmean);
			new Thread(rPaint).start();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while((estimate>threshold) && (MaxRun>0)){
			try {
				lib.calculateLines(centerSet);
				lib.chooseYourSide(centerSet,nodeSet);
				estimate = lib.reposition(centerSet,nodeSet,kmodes);
				System.out.println("Round <" + count + "> Diffenent: " + estimate);
				guiKmean.meanPanel.repaint();
				count++;
				Thread.sleep(sleep);
			} catch (Exception e) {	
				e.printStackTrace();
			}
			MaxRun--;
		}
		
	}
	
}
class re_paint implements Runnable{
	GuiKmean guiKmean;
	public void setGuiKmean(GuiKmean guiKmean){this.guiKmean = guiKmean;}
	@Override
	public void run() {
		while(true){
			try {
				guiKmean.meanPanel.repaint();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
}
