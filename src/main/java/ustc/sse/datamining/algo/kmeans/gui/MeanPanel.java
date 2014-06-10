package ustc.sse.datamining.algo.kmeans.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JPanel;

import ustc.sse.datamining.algo.kmeans.Center;
import ustc.sse.datamining.algo.kmeans.Lib;
import ustc.sse.datamining.algo.kmeans.Line;
import ustc.sse.datamining.algo.kmeans.Node;
import ustc.sse.datamining.algo.kmeans.Point;

public class MeanPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	private Set<Node> nodes;
	private ArrayList<Center> centers;
	private ColorList colors; // colors
	private int k, num_of_node;
	private boolean showLine;
	private long count = 0l;
	private Lib lib;
	public MeanPanel(int width, int height,Set<Node> nodes, ArrayList<Center> centers,boolean showLine,long seed) {
		super();
		this.nodes = nodes;
		this.centers = centers;
		this.width = width;
		this.height = height;
		this.showLine = showLine;
		k = Lib.getInstance().getK();
		num_of_node = Lib.getInstance().getNum_of_node();
		colors = ColorList.getInstance(k,seed);
		lib = Lib.getInstance();
		InitialContext();
	}
	
	private void InitialContext() {
		//this.setBounds(0, 0, width, height);
		this.setBackground(Color.red);
	}
	
	public void paint(Graphics g2) {
		
		Graphics2D g = (Graphics2D)g2;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		paintNodes(g);
		paintCenter(g);
		if(showLine)
			paintLines(g);
		
	}
	
	private void paintCenter(Graphics2D g) {
		count++;
		int alpha = (int)count%10;
		//((alpha>5)?alpha:10-alpha)
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(((alpha>5)?alpha:10-alpha)*0.1f)+0.0f));
		for(Center center :centers){
			if(!center.isForeverAlone()){
				g.setColor(colors.getColor(center.getId()));
				g.fillOval(scale(center.getX())-5, scale(center.getY())-5, 10, 10);
				g.drawOval(scale(center.getX())-8, scale(center.getY())-8, 15, 15);
			}
			if(center.isForeverAlone() && (count)%30>15){
				g.setColor(colors.getColor(center.getId()));
				g.fillOval(scale(center.getX())-5, scale(center.getY())-5, 10, 10);
				g.drawOval(scale(center.getX())-8, scale(center.getY())-8, 15, 15);
				if(center.isForeverAlone()){
					g.drawLine(scale(center.getX())-10, scale(center.getY())-10, scale(center.getX())+10, scale(center.getY())+10);
					g.drawLine(scale(center.getX())+10, scale(center.getY())-10, scale(center.getX())-10, scale(center.getY())+10);
				}
			}
		}
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
	}
	
	private void paintNodes(Graphics2D g) {

		
		for(Node node : nodes){
			if(node.getCenter()!=null){
				g.setColor(colors.getColor(node.getCenter().getId()));
			}else{
				g.setColor(Color.white);
			}
			g.drawOval(scale(node.getX())-3,scale(node.getY())-3, 6, 6);
		}
	}
	private void paintLines(Graphics2D g) {
		for(Center center :centers){
			g.setColor(colors.getColor(center.getId()));
			for(Line line :center.getLines()){
				double k = line.getK();
				double b = scale(line.getB());
				//setInitialPointOfLine(line);
				line.setStart(new Point(0, lib.getY(0, k, b)));
				line.setEnd(new Point(width,  lib.getY(width, k, b)));
				//	g.drawLine(0, getY(0, k, b), width, getY(width, k, b));
				//System.out.println("[0,"+ getY(0, k, b) + "] ? ["+width+", "+ getY(width, k, b)+"]" );
			}
			//lib.fixLine(center);
			for(Line line :  center.getLines()){
				paintLine(g, line);
			}
		}
	}
	private void paintLine(Graphics2D g, Line line){
		g.drawLine((int)line.getStart().getX(), (int)line.getStart().getY(),
				(int)line.getEnd().getX(), (int)line.getEnd().getY());
	//	paintPoint(g, line.getStart());
	//	paintPoint(g, line.getEnd());
	}
	/*private void paintPoint(Graphics2D g, Point point){
		System.out.println(point.toString());
		g.fillOval((int)scale(point.getX()-1), (int)scale(point.getY()-1), 20, 20);
	}*/
	private int scale(double value){
		return (int)(value*((double)width/(double)num_of_node));
	}
	private double scale(double value,int oo){
		return (value*((double)width/(double)num_of_node));
	}
	/*private int getY(double x, double k, double b){
		double y =  k*x+b;
		return (int)(y);
	}*/
	private double getX(double y,double k,double b){
		return (y-b)/k;
	}
	public void setInitialPointOfLine(Line line){
		Point startPoint, endPoint;
		double k = line.getK();
		double b = scale(line.getB(),0);
		double startX = 0;
		double startY = lib.getY(startX, k, b);
		if(startY>=0 && startY<=width){
			
		}else if(startY<0){
			startX = getX(0, k, b);
		}else  if(startY>width){
			startX = getX(width, k, b);
		}
		startPoint = new Point(startX, startY);
		
		 startX = width;
		 startY = lib.getY(startX, k, b);
		if(startY>=0 && startY<=width){
			
		}else if(startY<0){
			startX = getX(0, k, b);
		}else  if(startY>width){
			startX = getX(width, k, b);
		}
		endPoint = new Point(startX, startY); 
		line.setStart(startPoint);
		line.setEnd(endPoint);
	}
}
