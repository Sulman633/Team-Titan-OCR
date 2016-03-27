package ppDan;
import java.awt.Color;
import java.util.ArrayList;

public class Ink {
	
	ArrayList<Pixel> set; //collection of pixels
	ArrayList<Cluster> clus; //collection of clusters
	public ArrayList<Line> lines; //collection of line
	
	
	
	public Ink(){
		set = new ArrayList<Pixel>();
		clus = new ArrayList<Cluster>();
		lines = new ArrayList<Line>();
	}
	

	public void addPixel(Pixel p){
		set.add(p);
	}
	
	//goes through each stored pixel and checks if its connected to another pixel through recurssion
	//for every pixel it discovers, it is marked so it can be skipped during the sequential iteration
	public void findClusters(Pixel[][] map){
		for(int i=0; i<set.size();i++){
			Pixel p = set.get(i); 
			if(p.checkConnect()==false){
				Cluster c = new Cluster();
				
				p.connect();
				c.addPixel(p);
				clusterConnect(p,map,c);
				
				clus.add(c);
			}
		}
	}
	
	//finds the top most cluster that isnt already part of a line and declares it as a new line
	//it then looks for other clusters that match its approximate range of minx and maxx value
	//as clusters are discovered, they are marked as part of a some line.
	//this process runs until every cluster is part of atleast one line.
	public void findLines(Pixel[][] map){
		Cluster minCluster = null;
		double maxAvgHeight = Integer.MIN_VALUE;
		
		do{
			minCluster = null;
			int minY = Integer.MAX_VALUE;
			
			
			for(int i=0;i<clus.size();i++){
				if(clus.get(i).isLine()==false){
					if(clus.get(i).getMinY()<minY){
						minY = clus.get(i).getMinY();
						minCluster = clus.get(i);
					}
				}
				
			}
			
			if(minCluster!=null){
				Line tempLine = new Line();
				minCluster.connectLine();
				tempLine.addCluster(minCluster);
				
				//add this code to other shit
							
				boolean repeat = false;
				for(int i=0;i<clus.size();i++){
					repeat = false;
					if(clus.get(i).isLine()==false){
						
						
						if(minCluster.getMaxY()>=clus.get(i).getMinY()){
							clus.get(i).connectLine();
							//count++;
							//new code-------
							if(clus.get(i).getHeight()>minCluster.getHeight()){
								minCluster = clus.get(i);
								repeat = true;
							}
							
							if(Math.abs(minCluster.getMaxY()-clus.get(i).getMaxY())<(minCluster.getHeight()/3)){//for skewed lines
								minCluster = clus.get(i);
								repeat = true;
								
							}
							
							
							
							tempLine.addCluster(clus.get(i));
				
							if(repeat) i=0;
						}
					
					}	
				}
				lines.add(tempLine);
				if(tempLine.avgHeight > maxAvgHeight) maxAvgHeight = tempLine.avgHeight;
			}
			
		}while(minCluster!=null);
		
		for(int i=0;i<lines.size();i++){
			if(maxAvgHeight/3>lines.get(i).avgHeight){
				lines.remove(i);
				i--;
			}
			else{
				lines.get(i).findAllWords(map);
			}
			
		}
	}
	
	
	
	public boolean clusterConnect(Pixel p, Pixel[][] map, Cluster c){
		
		try{
			Pixel p2 = map[p.getX()+1][p.getY()];
		
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
			Pixel p2 = map[p.getX()+1][p.getY()+1];
		
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
			Pixel p2 = map[p.getX()+1][p.getY()-1];
		
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
			Pixel p2 = map[p.getX()][p.getY()+1];
		
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
			Pixel p2 = map[p.getX()][p.getY()-1];
		
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
			Pixel p2 = map[p.getX()-1][p.getY()];
		
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
			Pixel p2 = map[p.getX()-1][p.getY()+1];
		
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
			Pixel p2 = map[p.getX()-1][p.getY()-1];
		
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
	
	/*public void findLetters(){
	int letterWidth;
	
	letterWidth = findLetterWidth();
	
	int numLetters =0;
	
	for(int i=0; i<lines.size();i++){
		for(int j=0; j<lines.get(i).words.size();j++){
			numLetters = (int)(lines.get(i).words.get(j).getWidth()/letterWidth);
			lines.get(i).words.get(j).setNumOfLetters(numLetters);
			
		}
	}
}*/
 
/*public int findLetterWidth(){
	int minWidth = Integer.MAX_VALUE; //min width of a word
	
	for(int i=0; i<lines.size();i++){
		for(int j=0; j<lines.get(i).words.size(); j++){
			if(lines.get(i).words.get(j).getWidth()<minWidth){
				minWidth = lines.get(i).words.get(j).getWidth();
			}
		}
		
	}
	
	
	//find a width of a possible letter
	
	double ratio;
	double minRatio = Double.MAX_VALUE;
	int letterWidth = 0;
	
	for(int i=0; i<lines.size();i++){
		for(int j=0; j<lines.get(i).words.size(); j++){
			ratio = lines.get(i).words.get(j).getWidth()/minWidth;
			
			if(ratio > 1.5){
				if(ratio < minRatio){
					minRatio = ratio;
					letterWidth = lines.get(i).words.get(j).getWidth();
				}
			}
		}
		
	}
	
	if(minRatio > 2){
		//do nothing
	}else{
		letterWidth = minWidth;
	}
	
	return letterWidth;
}*/


	
	

	
	

}
