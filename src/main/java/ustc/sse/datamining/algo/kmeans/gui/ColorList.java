package ustc.sse.datamining.algo.kmeans.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class ColorList {
	private static ColorList c = null;
	ArrayList<Color> colors;
	private ColorList(int k, long seed){
		
			colors = new ArrayList<Color>();
			colors.add(RED);
			colors.add(BLUE);
			colors.add(GREEN);
			colors.add(CYAN);
			colors.add(GRAY);
			colors.add(PINK);
			colors.add(ORANGE);
			colors.add(YELLOW);
			colors.add(MAGENTA);
			if(k>9){
				Random rnd = new Random(seed);
				for(int i=0;i<k-9;i++){
					colors.add(new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
				}
			}
	};
	
	public static ColorList getInstance(int k,long seed){
		if(c==null)
			c=new ColorList(k,seed);
		return c;
	}
	
	public Color getColor(int i){
		return colors.get(i);
	}
	
	private final Color RED = Color.red;//0
	private final Color BLUE = Color.BLUE;//1
	private final Color GREEN = Color.GREEN;//2
	private final Color CYAN = Color.CYAN;//3
	private final Color GRAY = Color.GRAY;//4
	private final Color PINK = Color.PINK;//5
	private final Color ORANGE = Color.ORANGE;//6
	private final Color YELLOW = Color.YELLOW;//7
	private final Color MAGENTA = Color.MAGENTA;//8	
}
