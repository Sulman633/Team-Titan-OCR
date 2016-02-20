package preprocess;

import java.awt.Color;

public class pixel {
	private int c;
	private int x;
	private int y;
	
	private Color col;
	private boolean connected =false;
	
	public pixel(int c, int x, int y){

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
	
	public void connect(){
		connected=true;
	}
	


}
