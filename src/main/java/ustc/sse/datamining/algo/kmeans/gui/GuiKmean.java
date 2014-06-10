package ustc.sse.datamining.algo.kmeans.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JFrame;

import ustc.sse.datamining.algo.kmeans.Center;
import ustc.sse.datamining.algo.kmeans.Node;

public class GuiKmean extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MeanPanel meanPanel;
	public final int width = 600;
	public final int height = 600;
	public GuiKmean(Set<Node> nodes, ArrayList<Center> centers,boolean showLine,long seed) {
		meanPanel = new MeanPanel(width,height,nodes,centers, showLine, seed);
		InitialContext();
	}
	private void InitialContext() {
		this.setVisible(true);
		this.setLayout(null);
		this.setSize(width, height);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("K-Means 聚类算法");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    Dimension frameSize = this.getSize();
	    this.setLocation((screenSize.width - frameSize.width) / 2,(screenSize.height - frameSize.height) / 2);
	    meanPanel.setBounds(0, 0, width, height);
	    this.add(meanPanel);
	}
}
