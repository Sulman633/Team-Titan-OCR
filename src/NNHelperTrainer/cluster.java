package NNHelperTrainer;

import java.util.ArrayList;

public class cluster {
	ArrayList<pixel> set;
	int minx = Integer.MAX_VALUE;
	int maxx = Integer.MIN_VALUE;
	int miny = Integer.MAX_VALUE;
	int maxy = Integer.MIN_VALUE;
	
	public cluster(){
		set = new ArrayList<pixel>();
	}
	
	public void addPixel(pixel p){
		if(p.getX()>maxx) maxx=p.getX();
		if(p.getX()<minx) minx=p.getX();
		if(p.getY()>maxy) maxy=p.getY();
		if(p.getY()<miny) miny=p.getY();
		set.add(p);
		
	}
	
	public int getHeight(){
		return maxy-miny;
	}
	
	public int getWidth(){
		return maxx-minx;
	}
	
	public int getArea(){
		return Math.abs(getHeight())*Math.abs(getWidth());
	}

}
