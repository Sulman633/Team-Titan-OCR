package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class ImgProcessMatt {
	static BufferedImage img = null;
	// 20x20 array. neuralNetworkInput
	static int[][] neuralNetworkInput = new int[20][20];
	
	private static BufferedImage scaledImage(BufferedImage srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();
	    return resizedImg;
	} 
	
	public double widthHeightRatio(){
		int avgLetterWidth = 25;
		int width = img.getWidth();
		
		double ratio = width/avgLetterWidth;
		
		return ratio;
	}
	
	// generates input for neuralnetworks. 
	public static void generatedInput(int[][] array, ink writing){
		
		for(int i=0; i<array.length; i++){
			for(int j=0; j<array[i].length; j++){
				array[i][j] = 0;
			}
		}
		
		for(int i=0; i<writing.set.size(); i++){
			array[writing.set.get(i).getX()][writing.set.get(i).getY()] = 1;
		}	
	}
	
	/*
	 * Converts 2d neuralNetworkInput into 1d int array
	 * @return - Neural network representation of the passed image
	 */
	public static int[] convert(int[][] array){
		int[] result = new int[array.length*array[0].length]; 
		
		for(int i=0; i<array.length; i++){
			int[] row = array[i];
			for(int j=0; j<array[i].length; j++){
				result[i*row.length+j] = array[i][j];
			}
		}
		return result;
	}
	
	/**
	 * generates the cluster and opens up the image in a bufferedImage
	 * @param fileName
	 * @return - returns the cluster array;
	 */
	
	public static void generateClusterTony(PatternDetector pd, String fileName){
		try{//get image
			File imgf = new File(fileName);
			img = ImageIO.read(imgf);
		}
		catch (IOException e) {
		}
		
		int w = img.getWidth();
		int h = img.getHeight();
		Color c;
		
		int[][] imgarray;
		pixel[][] pixarray;
		
		//ArrayList<ink> essay = new ArrayList<ink>();
		ink writing = new ink(w,h);
		ink paper = new ink(w,h);
		
		imgarray= new int[w][h];
		pixarray= new pixel[w][h];
		
		//write pixels to writing and paper objects
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				imgarray[i][j]=img.getRGB(i, j);
				
				pixel p = new pixel(img.getRGB(i, j),i,j);
				pixarray[i][j]=p;
				if(!p.checkWhite()) writing.addPixel(p);
				else paper.addPixel(p);

			}
		}
		
		pd.scan(pixarray, false);

		
	}
	
	
	
	public static int[] generateCluster(String fileName, BufferedImage bi, int imgSize){
		
		if (bi == null){
			try{//get image
				File imgf = new File(fileName);
				img = ImageIO.read(imgf);
				img = scaledImage(img, imgSize, imgSize);
			}
			catch (IOException e) {
			  }
		} else{
			img = bi;
		}
		
		int w = img.getWidth();
		int h = img.getHeight();
		Color c;
		
		int[][] imgarray;
		pixel[][] pixarray;
		
		//ArrayList<ink> essay = new ArrayList<ink>();
		ink writing = new ink(w,h);
		ink paper = new ink(w,h);
		
		imgarray= new int[w][h];
		pixarray= new pixel[w][h];
		
		//write pixels to writing and paper objects
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				imgarray[i][j]=img.getRGB(i, j);
				
				pixel p = new pixel(img.getRGB(i, j),i,j);
				pixarray[i][j]=p;
				if(!p.checkWhite()) writing.addPixel(p);
				else paper.addPixel(p);

			}
		}
		
		//Stores the individual clusters of black ink in writing
		writing.findClusters(pixarray);

		//sets the neural network inputs to the passed image
		generatedInput(neuralNetworkInput, writing);
		
		int[] result = convert(neuralNetworkInput);
		
		return result;
	}
	
	/**
	 * AlphabetMap of all the cases and add it the hashMap
	 * @return
	 */
	public static Map<String, int[]> generateAlphabetMap(int imgSize){
		Map<String, int[]> alphabetMap = new HashMap<String, int[]>();
		
		//Currently using these alphabets to train the NN
		String[] alph1 = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","lowera","lowerb","lowerc","lowerd","lowere","lowerf","lowerg","lowerh","loweri","lowerj","lowerk","lowerl","lowerm","lowern","lowero","lowerp","lowerq","lowerr","lowers","lowert","loweru","lowerv","lowerw","lowerx","lowery","lowerz"};
		String[] alphRep = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		
		//adds each letter to the map with its matching character
		for(int k=0; k<52; k++){
			String fileName;
			if (imgSize == 10){
				fileName = "Pixel10letters/" + alph1[k] + ".jpg";
			}
			else {
				fileName = alph1[k] + ".jpg";
			}
						
			int[] result = generateCluster(fileName, null, imgSize);
			
			alphabetMap.put(alphRep[k], result);
	
		}
		
		return alphabetMap;
	}
	
	public static Map<String, int[]> generateAlphabetMapTony(ArrayList<BufferedImage> letters, int imgSize){
		Map<String, int[]> alphabetMap = new HashMap<String, int[]>();
		BufferedImage scaledLetter;
		
		//Currently using these alphabets to train the NN
		String[] alphRep = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		
		//adds each letter to the map with its matching character
		for(int k=0; k<letters.size(); k++){
			
			scaledLetter = scaledImage(letters.get(k), imgSize, imgSize);
						
			int[] result = generateCluster(null, scaledLetter, 0);
			
			alphabetMap.put(alphRep[k], result);
	
		}
		
		return alphabetMap;
	}
}