package preprocess;

import java.awt.Color;
import java.util.ArrayList;

public class ink {
	
	ArrayList<pixel> set = null;
	int w;
	int h;
	ArrayList<cluster> clus;
	
	
	public ink(int w, int h){
		set = new ArrayList<pixel>();
		clus = new ArrayList<cluster>();
		
		this.w = w;
		this.h = h;
	}
	

	public void addPixel(pixel p){
		
		set.add(p);
		
	}
	
	public void findClusters(pixel[][] map){
		for(int i=0; i<set.size();i++){
			pixel p = set.get(i); 
			if(p.checkConnect()==false){
				cluster c = new cluster();
				
				
				p.connect();
				c.addPixel(p);
				clusterConnect(p,map,c);
				
				clus.add(c);
			}
			
		}
		
	}
	
	public boolean clusterConnect(pixel p, pixel[][] map, cluster c){
		
		try{
			pixel p2 = map[p.getX()+1][p.getY()];
		
			if(p2.checkWhite()==false){
				if(p2.checkConnect()==false){
					p2.connect();
					c.addPixel(p2);
					clusterConnect(p2,map,c);	
				}
					
			}
			
		}catch(IndexOutOfBoundsException e){
			
		}
		try{
			pixel p2 = map[p.getX()+1][p.getY()+1];
		
			if(p2.checkWhite()==false){
				if(p2.checkConnect()==false){
					p2.connect();
					c.addPixel(p2);
					clusterConnect(p2,map,c);	
				}
					
			}
			
		}catch(IndexOutOfBoundsException e){
			
		}
		
		try{
			pixel p2 = map[p.getX()+1][p.getY()-1];
		
			if(p2.checkWhite()==false){
				if(p2.checkConnect()==false){
					p2.connect();
					c.addPixel(p2);
					clusterConnect(p2,map,c);	
				}
					
			}
			
		}catch(IndexOutOfBoundsException e){
			
		}
		
		try{
			pixel p2 = map[p.getX()][p.getY()+1];
		
			if(p2.checkWhite()==false){
				if(p2.checkConnect()==false){
					p2.connect();
					c.addPixel(p2);
					clusterConnect(p2,map,c);	
				}
					
			}
			
		}catch(IndexOutOfBoundsException e){
			
		}
		
		try{
			pixel p2 = map[p.getX()][p.getY()-1];
		
			if(p2.checkWhite()==false){
				if(p2.checkConnect()==false){
					p2.connect();
					c.addPixel(p2);
					clusterConnect(p2,map,c);	
				}
					
			}
			
		}catch(IndexOutOfBoundsException e){
			
		}
		
		try{
			pixel p2 = map[p.getX()-1][p.getY()];
		
			if(p2.checkWhite()==false){
				if(p2.checkConnect()==false){
					p2.connect();
					c.addPixel(p2);
					clusterConnect(p2,map,c);	
				}
					
			}
			
		}catch(IndexOutOfBoundsException e){
			
		}
		
		try{
			pixel p2 = map[p.getX()-1][p.getY()+1];
		
			if(p2.checkWhite()==false){
				if(p2.checkConnect()==false){
					p2.connect();
					c.addPixel(p2);
					clusterConnect(p2,map,c);	
				}
					
			}
			
		}catch(IndexOutOfBoundsException e){
			
		}
		
		try{
			pixel p2 = map[p.getX()-1][p.getY()-1];
		
			if(p2.checkWhite()==false){
				if(p2.checkConnect()==false){
					p2.connect();
					c.addPixel(p2);
					clusterConnect(p2,map,c);	
				}
					
			}
			
		}catch(IndexOutOfBoundsException e){
			
		}
		
		
		return true;
		
	}
	
	

	
	

}
