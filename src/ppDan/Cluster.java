package ppDan;
import java.util.ArrayList;

public class Cluster {
	private ArrayList<Pixel> set;
	//ArrayList<Pixel> other;
	ArrayList<Double> membership;
	ArrayList<Double> distance;//distances for each pixel from centroid, fuzzy clustering
	ArrayList<Double> memCof;
	
	private int minX = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int minY = Integer.MAX_VALUE;
	private int maxY = Integer.MIN_VALUE;
	
	private double avgD=0;
	
	double centroidX = 0;
	double centroidY = 0;
	
	//private double avgX=0;
	//private double avgY=0;
	
	private boolean line = false;
	
	public Cluster(){
		set = new ArrayList<Pixel>();
		distance = new ArrayList<Double>();
		
	}
	
	public Cluster(double x, double y){
		centroidX = x;
		centroidY = y;
		set = new ArrayList<Pixel>();
		distance = new ArrayList<Double>();
	}
	
	
	public void findMembership(double fuzz){
		membership = new ArrayList<Double>();
		memCof = new ArrayList<Double>();
		double mem;
		
		for(int i=0; i<distance.size();i++){
			if(distance.get(i)==0) mem = 1;
			else{
				mem = Math.pow((1/distance.get(i)),(1/(fuzz-1)));
				
			}
			
			membership.add(mem);
		}
		
	}
	
	public double getMembership(int a){
		return membership.get(a);
	}
	
	public void setMemCoefficient(double a){
		memCof.add(a);
		
	}
	
	public double getMemCoefficient(int a){
		return memCof.get(a);
	}
	
	public Pixel getPixel(int a){
		return set.get(a);
	}
	
	public void addPixel(Pixel p){
		
		if(centroidX==0 && centroidY==0){
			
		}else{
			avgD = avgD*set.size() + p.getDistance(centroidX, centroidY);
			distance.add(p.getDistance(centroidX, centroidY));
		}
		
		
		if(p.getX()>maxX) maxX=p.getX();
		if(p.getX()<minX) minX=p.getX();
		if(p.getY()>maxY) maxY=p.getY();
		if(p.getY()<minY) minY=p.getY();
		
		//avgX = avgX*set.size() + p.getX();
		//avgY = avgY*set.size() + p.getY();
		
		set.add(p);
		
		//avgX = avgX/set.size();
		//avgY = avgY/set.size();
		

		if(centroidX==0 && centroidY==0){
			
		}else{
			avgD = avgD/set.size();
		}
		
		//System.out.println(avgX + ", " + avgY);
	}
	

	public double getAvgD(){
		
		return avgD;
	}
	
/*	public double getAvgX(){
		
		return avgX;
	}
	

	public double getAvgY(){
		
		return avgY;
	}
	*/
	
	
	public int getHeight(){
		return maxY-minY;
	}
	
	public int getWidth(){
		return maxX-minX;
	}
	
	public int getArea(){
		return Math.abs(getHeight())*Math.abs(getWidth());
	}
	
	public void setDimensions(int x, int maxY, int minY){
		maxX = x;
		minX = x;
		this.maxY = maxY;
		this.minY = minY;
		
	}
	
	public int getMinX(){
		return minX;
	}
	
	public int getMaxX(){
		return maxX;
	}
	
	public void increaseMaxX(){
		maxX++;
	}
	
	public int getMinY(){
		return minY;
	}
	
	public int getMaxY(){
		return maxY;
	}
	
	public boolean isLine(){
		return line;
	}
	
	public void connectLine(){
		line = true;
	}
	
	public ArrayList<Pixel> getSet(){
		return set;
	}

}
