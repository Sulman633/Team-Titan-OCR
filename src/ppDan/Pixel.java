package ppDan;
import java.awt.Color;

public class Pixel {
	private int c;
	private int x;
	private int y;
	private double priority=1;
	private Color col;
	private boolean connected =false;
	
	public Pixel(int c, int x, int y){

		this.c = c;
		this.x = x;
		this.y = y;
		col = new Color(c);

	}
	
	public int getC(){
		return c;
	}
	
	public Color getColor(){
		return col;
	}
	
	public double getDistance(double kx, double ky){
		//different distance methods
		double distance=0;
		
		//distance = Math.abs(Math.sqrt(Math.pow((x-kx), 2) + Math.pow((y-ky), 2)));//euclidian
		distance = Math.max(Math.abs(x - kx), Math.abs(y - ky)); //chebyshev
		//distance = Math.abs(x - kx) + Math.abs(y - ky);//manhattan
		
		
		return distance;
	}
	
	public boolean checkWhite(){
		if(col.getRed()>170 && col.getBlue()>100 && col.getGreen()>180){
			return true;
		}else{
		
			return false;
		}		
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean checkConnect(){
		return connected;
	}
	
	public void setPriority(double p){
		priority = p;
	}
	
	public double getPriority(){
		return priority;
	}
	
	public void connect(){
		connected=true;
	}
	


}
