package test;

import java.awt.image.BufferedImage;

import neuralnetwork.*;


public class letterSplitter {
	
	BackProp neuralNetworkMain = new BackProp();
	ImgProcessMatt imgProc = new ImgProcessMatt();

	//Main method, determines if an image needs to be split
	public void splitImg(double ratio, BufferedImage img){
		
		if (ratio <= 1.5){
			System.out.println("IMAGE RATIO: Assuming 1 chars in img");
			
			double bestPercentage = runNN(img);
			
			if (bestPercentage > 0.6){
				System.out.println("Most likely 1 character!");
			}
			else {
				System.out.println("Most likely 2 characters!");
				localMinFinder(img);
			}
			
		} else if (ratio > 1.5 && ratio <= 2.5){
			System.out.println("IMAGE RATIO: Assuming 2 chars in img");
			localMinFinder(img);
			
		} else if (ratio > 2.5 && ratio <= 3.5){
			System.out.println("IMAGE RATIO: Assuming 3 chars in img");
			
		}
		
	}
	
	public double runNN(BufferedImage img){
		Neuron[] nnRep = neuralNetworkMain.determineLetter(imgProc.generateCluster(null, img));
		double highestVal = 0;
		for (int i = 0; i < nnRep.length; i++){
			if (nnRep[i].value > highestVal){
				highestVal = nnRep[i].value;
			}
		}
		return highestVal;
	}
	
	//finds and returns the x axis of the local min
	public void localMinFinder(BufferedImage img){
		System.out.println("Running local min find");
		int height  = img.getHeight();
		int width = img.getWidth();
		int middlePoint = width/2;
		
		int[][] imgRep = new int[width][height];
		
		for (int w = 0; w < width; w++){
			for (int h = 0; h < height; h++){
				pixel p = new pixel(img.getRGB(w, h),w,h);
				if (!p.checkWhite()){
					imgRep[w][h] = 1;
				} else{
					imgRep[w][h] = 0;
				}
				
			}
		}
		
		int splitLoc = dropBall(imgRep, middlePoint);
		
		System.out.println("Split the image at: " + splitLoc + " x position.");
		
	}
	
	public int dropBall(int[][] img, int ballStart){
		int ballPosx = ballStart;
		int ballPosy = 0;
		boolean roll = true;
		
		while (roll){
			if (img[ballPosx][ballPosy-1] == 0){
				ballPosy -= 1;
			}
			else if(img[ballPosx-1][ballPosy-1] == 0){
				ballPosy -= 1;
				ballPosx -= 1;
			}
			else if(img[ballPosx+1][ballPosy-1] == 0){
				ballPosy -= 1;
				ballPosx += 1;
			}
			else{
				roll = false; //Local min found
			}
		}
		return ballPosx;
	}
}
