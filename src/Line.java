import java.util.ArrayList;
import java.util.Arrays;

public class Line {
	ArrayList<Cluster> set;
	ArrayList<Cluster> spaces;
	ArrayList<Word> words;
	private int minX = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int minY = Integer.MAX_VALUE;
	private int maxY = Integer.MIN_VALUE;
	double avgHeight=0;
	
	
	public Line(){
		set = new ArrayList<Cluster>();
		spaces = new ArrayList<Cluster>();
		words = new ArrayList<Word>();
	}
	
	public void addCluster(Cluster x){
		
		if(x.getMaxX()>maxX) maxX=x.getMaxX();
		if(x.getMinX()<minX) minX=x.getMinX();
		if(x.getMaxY()>maxY) maxY=x.getMaxY();
		if(x.getMinY()<minY) minY=x.getMinY();
		
		avgHeight = avgHeight*set.size() + x.getHeight();
		set.add(x);
		avgHeight = avgHeight/set.size();
		
	}
	

	
	public void findAllWords(Pixel[][] map){
		findSpaces(map);
		findWords();
		
	}
	
	//determines space clusters between ink clusters
	public void findSpaces(Pixel[][] map){
		int black;
		boolean success = false;
		
		int maxWidth = 0;
		boolean foundWidth = false;
		
		int restriction = (int)((maxY-minY)*0.75)+minY;
		
		for(int i = minX; i<=maxX; i++){ //left to right iteration of columns
			black = 0;
			success= false;
			for(int j = minY; j<=restriction; j++){//top down iteration of row
				if(!map[i][j].checkWhite())black++;
			}
			
			if(black==0){ //if there isnt a black pixel found in the top down iteration, then the column must be a space
				for(int k=0;k<spaces.size();k++){ //check if the space column can be added to an existing space cluster
					if(spaces.get(k).getMaxX()+1 == i){
						spaces.get(k).increaseMaxX();
						if(spaces.get(k).getWidth()>maxWidth){
							maxWidth = spaces.get(k).getWidth();
							foundWidth = true;
						}
						success = true;
						
					}
				}
				if(success==false){ //if not, then make a new space cluster
					Cluster white = new Cluster();
					white.setDimensions(i, maxY, minY);
					spaces.add(white);
				}
			}
			
		}
		
		if(foundWidth){
			for(int i=0;i<spaces.size();i++){//remove insignificant white space clusters (spaces between letters)
				if(spaces.get(i).getWidth()<maxWidth*.25){
					spaces.remove(i);
					i--;
				}
			}
		}
		
		
		
		
	}
	
	//Once you have your space intervals, it divides the clusters into more specific groups (words)
	public void findWords(){
		Word toAdd = null;
		
		for(int i=0; i<spaces.size(); i++){
			
			toAdd = new Word();
			for(int j=0; j<set.size();j++){
				if(spaces.get(i).getMinX()>set.get(j).getMinX()){
					toAdd.addClusters(set.remove(j));
					j--;
				}
			}
			words.add(toAdd);
		}
		
		toAdd = new Word();
		
		while(!set.isEmpty()){
			toAdd.addClusters(set.remove(0));
		}
		words.add(toAdd);
	}
	
	public int getMinX(){
		return minX;
	}
	
	public int getMaxX(){
		return maxX;
	}
	
	public int getMinY(){
		return minY;
	}
	
	public int getMaxY(){
		return maxY;
	}
	
	public int getHeight(){
		return maxY-minY;
	}
	
	public int getWidth(){
		return maxX-minX;
	}
	
	//testing purposes
	public int[][] imgArray(){
		int[][] image = new int[getWidth()+1][getHeight()+1];
		
		for (int[] line : image) {
			Arrays.fill(line, -1);
		}
	    
		
		
		
		for(int i=0; i<set.size();i++){
			for(int j=0; j<set.get(i).getSet().size();j++){
				int w = set.get(i).getSet().get(j).getX()-minX;
				int h = set.get(i).getSet().get(j).getY()-minY;
				
					image[w][h] = -16777216;

			}
			
		}
		
		return image;
		
	}
	
	/*public void findLetters(){ //  is not being used right now
	ArrayList<Integer> smallClusters = new ArrayList<Integer>();
	double minDistance = Integer.MAX_VALUE;
	int minDistanceIndex = 0;
	boolean foundMin = false;
	
	for(int i=0; i<set.size();i++){
		if((avgHeight/set.get(i).getHeight())>3){
			smallClusters.add(i);
		}
		
	}
	
	for(int i=0;i<smallClusters.size();i++){
		minDistance = Integer.MAX_VALUE;
		int remove = smallClusters.get(i);
		Cluster temp = set.remove(remove);
		for(int j=0;j<set.size();j++){
			if(temp.getMaxY()<set.get(j).getMinY()){
				int x1 = (int)(temp.getMinX() + temp.getWidth()/2);
				int y1 = temp.getMaxY();
				int x2 = (int)(set.get(j).getMinX() + set.get(j).getWidth()/2);
				int y2 = set.get(j).getMinY();
				double distance = Math.pow((Math.pow(Math.abs(x1-x2), 2) + Math.pow(Math.abs(y1-y2), 2)), 0.5); //might need to switch to center of mass
				if(distance<minDistance){
					minDistance = distance;
					minDistanceIndex = j;
					foundMin = true;
				}
				
			}
		}
		if(foundMin){
			for(int k=0;k<temp.getSet().size();k++){
				set.get(minDistanceIndex).addPixel(temp.getSet().get(k));
			}
		}
		
	}
	
	
	
	
}*/


}
