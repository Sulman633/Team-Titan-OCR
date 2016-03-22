import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Word {
	ArrayList<Cluster> set;
	int numOfLetters = 0;
	//ArrayList<Cluster> letters;
	private int minX = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int minY = Integer.MAX_VALUE;
	private int maxY = Integer.MIN_VALUE;
	
	
	public Word(){

		
		
		set = new ArrayList<Cluster>();
		//letters = new ArrayList<Cluster>();
		
	}
	
	public void setNumOfLetters(int x){
		numOfLetters = x;
		
	}
	
	public int getNumOfLetters(){
		return numOfLetters;
		
	}
	
	public void fLetters(){
		for(int i=0; i<set.size();i++){//go through cluster
			for(int j=0; j<set.get(i).getSet().size();j++){//get point from each cluster
				System.out.println("Point, " + set.get(i).getPixel(j).getX() + ", " + set.get(i).getPixel(j).getY());
				
			}
		}

		
	}
	
	public void findLetters(){
		
		//i have all datapoints for a word
		//find weights
		int k=5; //4 letters in the word
		double fuzz = 1.3; //fuzzy parameter
		ArrayList<Cluster> letters;
		double width;
		int numOfPoints = 0;
		
		//determine centroid of each letter
		letters = new ArrayList<Cluster>();
		width = getWidth()/k; //aprox width of a single letter
		
		//for each, find initial centroid
		System.out.println("--Starting Weights--");
		for(int i=0; i<k;i++){
			double x;
			double y;
			x = minX + width*(i) + width/2;
			y = minY;
			
			System.out.println(x + ", " + y);
			letters.add(new Cluster(x,y));
		}
		
		
		boolean toStop=false;
		while(true){
			//System.out.println("h");
			
			//find membership of each point to each centroid
			
			//go through each point
			for(int i=0; i<set.size();i++){//go through cluster
				for(int j=0; j<set.get(i).getSet().size();j++){//get point from each cluster
					Pixel p1 = set.get(i).getSet().get(j);
					//we have each point now
					
					for(int l=0; l<letters.size();l++){//go through each cluster now
						//
						letters.get(l).addPixel(p1); //adds pixel and gets distance
						
					}
					
					
				}
				
			}
			
			//every cluster has every point with its distance value
			
			//find membership value for each distance
			for(int i=0; i<letters.size();i++){
				letters.get(i).findMembership(fuzz);
			}
			
			//----------
			numOfPoints = letters.get(0).getSet().size();
			double memTotal;
			double memCoefficient;
			//find membership coefficient
			for(int i=0; i<numOfPoints;i++){//go through each point
				memTotal = 0;
				
				
				for(int j=0; j<letters.size();j++){ //go through each cluster
					memTotal = memTotal + letters.get(j).getMembership(i);
				}
				
				for(int j=0; j<letters.size();j++){
					memCoefficient = letters.get(j).getMembership(i)/memTotal;
					letters.get(j).setMemCoefficient(memCoefficient);
				}
				
			}
			
			if(toStop) break;
			
			toStop = true;
			
			System.out.println("-----New centroids----");
			//update centroids
			for(int i=0;i<letters.size();i++){
				double newX;
				double newY;
				
				double oldX = letters.get(i).centroidX;
				double oldY = letters.get(i).centroidY;
				
				double totalMem=0;
				double memX=0;
				double memY=0;
				double mem;
				for(int j=0;j<letters.get(i).getSet().size();j++){
					mem = letters.get(i).getMemCoefficient(j);
					memX += letters.get(i).getPixel(j).getX()*mem;
					memY += letters.get(i).getPixel(j).getY()*mem;
					totalMem += mem;
					
				}
				
				newX = memX/totalMem;
				newY = memY/totalMem;
				
				System.out.println(newX + ", " + newY);
				
				double diff1 = Math.abs(oldX-newX);
				double diff2 = Math.abs(oldY-newY);
				
				if(diff1<=.00000000000001 && diff2<=.00000000000001){
					//System.out.println(Math.abs(oldX-newX) + ", " + Math.abs(oldY-newY));
					toStop=false;
				}
				//new and old are similar, do something, some boolean
				
				letters.set(i, new Cluster(newX,newY));
				
			}
			
			//Scanner test = new Scanner(System.in);
			//test.nextLine();
			
		}
		
		
		//System.out.println(1.0/k);
		
		for(int i=0; i<letters.size();i++){
			System.out.println("Centroid " + i +", " + letters.get(i).centroidX + ", " + letters.get(i).centroidY);
			for(int j=0; j<letters.get(i).getSet().size();j++){
				/*double maxp = -1;
				double maxi = i;
				for(int l=0; l<letters.size();l++){
					if(letters.get(l).getMemCoefficient(j)>maxp){
						maxp = letters.get(l).getMemCoefficient(j);
						maxi = l;
					}
					
					if(i==maxi){
						System.out.println("Point, " + letters.get(i).getPixel(j).getX() +", " + letters.get(i).getPixel(j).getY() + ", " + letters.get(i).getMemCoefficient(j));
					}
				}*/
				
				
				
				
				if(letters.get(i).getMemCoefficient(j)>=.7){
					System.out.println("Point, " + letters.get(i).getPixel(j).getX() +", " + letters.get(i).getPixel(j).getY() + ", " + letters.get(i).getMemCoefficient(j));
				}
			}
		}
		
		//k validity test
		
		
		
		
		
		
		
		
		
	}
	
	
	public void addClusters(Cluster x){
		
		if(x.getMaxX()>maxX) maxX=x.getMaxX();
		if(x.getMinX()<minX) minX=x.getMinX();
		if(x.getMaxY()>maxY) maxY=x.getMaxY();
		if(x.getMinY()<minY) minY=x.getMinY();
		
		set.add(x);
	}
	
	public int[][] imgArray(){
		int[][] image = new int[getWidth()+1][getHeight()+1];
		
		
		//System.out.println(getWidth()+1);
		//System.out.println(getHeight()+1);
	    
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
	

	/*public void findLetters(){
		double dmax = Double.MIN_NORMAL;
		ArrayList<Cluster> letters;
		ArrayList<Pixel> dataset = new ArrayList<Pixel>();
		Random rand = new Random();
		for(int k=4; k<5; k++){ //k-min , tries 1 to 9

			letters = new ArrayList<Cluster>();
			double width = getWidth()/k;

			//while loop of some sort here
			
			//get dmax
			/*for(int i=0; i<set.size();i++){
				for(int j=0; j<set.get(i).getSet().size();j++){
					Pixel p1 = set.get(i).getSet().get(j);
					
					
					for(int l=0; l<set.size();l++){
						for(int m=0; m<set.get(l).getSet().size();m++){
							
							
							
							Pixel p2 = set.get(l).getSet().get(m);
							
							if(!dataset.contains(p2) && !p1.equals(p2)){
								double distance = p1.getDistance(p2.getX(), p2.getY());
								if(dmax<distance) dmax = distance;
							}
							
							
							
						}
					}
					
					dataset.add(p1);
				}
			}
			//*/
			
			/*//System.out.println("dmax: " + dmax);
			int count = 0;
			boolean stopK=false;
			while(true){
				double dmin = Double.MAX_VALUE;
				
				
				
				if(stopK && letters.size()==k){
					//average distance
					for(int i=0;i<k;i++){
						//System.out.println("Cluster " + i + ": " + letters.get(i).getAvgD());
						System.out.println("Centroid Point(k=" + i + "), " + letters.get(i).kx + ", " + letters.get(i).ky);
						for(int j=0; j<letters.get(i).getSet().size();j++){
							if(letters.get(i).other.get(j).getPriority()==1){
								System.out.println("Pixel, " + letters.get(i).other.get(j).getX()+", " + letters.get(i).other.get(j).getY());
							}
							
							
						}
					}
					
					
					
					
					break;
				}
				
				stopK = true;
				
				
				
				
				//picks k random points
				for(int i=0; i<k;i++){
					double newKX;
					double newKY;
					
					if(letters.size()!=k){
						stopK = false;
						//newKY = rand.nextInt(maxY-minY) + minY;
						//newKX = rand.nextInt(maxX-minX) + minX;
						
						newKY = (maxY);
						newKX = (minX + width*(i+1));
						System.out.println(maxX);
						System.out.println("Centroid " + i + ": " + newKX + ", " + newKY);
						
						letters.add(new Cluster(newKX,newKY));
					}else{
						
						newKX = letters.get(i).getAvgX();
						newKY = letters.get(i).getAvgY();
						if(newKX != letters.get(i).kx || newKY != letters.get(i).ky){
							stopK = false;
						}

						letters.set(i, new Cluster(newKX,newKY));

					}
					
					//System.out.println("Centroid " + i + ": " + newKX + ", " + newKY);

				}
				
				
				
				

				
				for(int i=0; i<set.size();i++){
					for(int j=0; j<set.get(i).getSet().size();j++){
						//gets each pixel
						double minDistance = Double.MAX_VALUE;
						double distance;
						int bestK = 0;
						
						for(int l = 0; l<k;l++){
							//gets each k point
			
							distance = set.get(i).getSet().get(j).getDistance(letters.get(l).kx,letters.get(l).ky);
							if(minDistance>distance){ //find best k for each pixel
								minDistance = distance;
								bestK = l;
								
								
							}
						}
						
						for(int l = 0; l<k;l++){
							//gets each k point
							
							distance = set.get(i).getSet().get(j).getDistance(letters.get(l).kx,letters.get(l).ky);
							letters.get(l).addPixel(set.get(i).getSet().get(j),distance/minDistance);
							
						}
						
						
						
						
						
					}
				}
				
				
				
				for(int i=0;i<k;i++){
					//System.out.println("Cluster " + i + ": " + letters.get(i).getAvgD());
					//System.out.println("Centroid Point(k=" + i + "), " + letters.get(i).kx + ", " + letters.get(i).ky);
					//System.out.println("MaxX:" + maxX);
					for(int j=0; j<letters.get(i).getSet().size();j++){
						
					//	System.out.println("Pixel, " + letters.get(i).getSet().get(j).getX()+", " + letters.get(i).getSet().get(j).getY());
					}
				}
				
				//Scanner test = new Scanner(System.in);
				//test.nextLine();
				
				
				
				
				
				/*dmin
				for(int i=0; i<k;i++){
					for(int j=0;j<letters.get(i).getSet().size();j++){
						Pixel p1 = letters.get(i).getSet().get(j);
						
						for(int l=0;l<k;l++){
							
							if(i!=l){
								for(int m=0; m< letters.get(l).getSet().size();m++){
									Pixel p2 = letters.get(l).getSet().get(m);
									double distance = p1.getDistance(p2.getX(), p2.getY());
									if(dmin>distance) dmin=distance;
									
								}
							}
							
						}
						
					}
					
				}
				System.out.println("dmin: " + dmin);
				//
				System.out.println("Index: " + dmin/dmax);*/
				/*
			}
			
			
			
			
		}
	
	}*/


}
