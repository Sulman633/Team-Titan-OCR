package preprocess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class imgtest {
	static BufferedImage img = null;
	
	
	// 20x20 array. neuralNetworkInput
	static int[][] neuralNetworkInput = new int[20][20];
	
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
	 * @return - 400 pixels back
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
	 * @return - returns the cluster array; containing 400 pixels
	 */
	public static int[] generateCluster(String fileName){
		
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
		boolean match = false;
		
		imgarray= new int[w][h];
		pixarray= new pixel[w][h];
		
		//convert to a 2d array
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				imgarray[i][j]=img.getRGB(i, j);
				
				match = false;
				pixel p = new pixel(img.getRGB(i, j),i,j);
				pixarray[i][j]=p;
				if(!p.checkWhite()) writing.addPixel(p);
				else paper.addPixel(p);

			}
		}
		
		
		writing.findClusters(pixarray);
		
		
		System.out.println("number of cluster: " + writing.set.size());
		// 
		System.out.println("Generate the Input");
		generatedInput(neuralNetworkInput, writing);
		int[] result = convert(neuralNetworkInput);
		
		//convert from a 2d to 1d array 
		int[] data = new int[h*w];
		
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				data[(i*w)+j]=imgarray[j][i];
			}
		}
		
		
		for(int i=0; i<data.length;i++){
			c = new Color(data[i]);
			//System.out.println(data[i] + " => " + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue());
			
			if(c.getRed()>170 && c.getBlue()>100 && c.getGreen()>180){
				data[i]=-1;
			}else{
				data[i]=-16777216;
			}
			
		}
		
		img.setRGB(0, 0, w, h, data, 0, w);
		
		

		Color a = Color.BLACK;
		System.out.println(a.getRGB());
		
		
		System.out.println(data.length);
		
//		JFrame frame = new JFrame();
//	    JLabel label = new JLabel(new ImageIcon(img));
//	    frame.getContentPane().add(label, BorderLayout.CENTER);
//	    frame.pack();
//	    frame.setVisible(true);
		
		return result;
	}
	
	/**
	 * AlphabetMap of all the cases and add it the hashMap
	 * @return
	 */
	public static Map<String, int[]> generateAlphabetMap(){
		Map<String, int[]> alphabetMap = new HashMap<String, int[]>();
		
		String[] alph = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","lowera","lowerb","lowerc","lowerd","lowere","lowerf","lowerg","lowerh","loweri","lowerj","lowerk","lowerl","lowerm","lowern","lowero","lowerp","lowerq","lowerr","lowers","lowert","loweru","lowerv","lowerw","lowerx","lowery","lowerz"};
		String[] alphRep = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		for(int k=26; k<32; k++){
			String fileName = alph[k] + ".jpg";
			
			System.out.println(fileName);
			
			int[] result = generateCluster(fileName);
			printArray(result);
			
			alphabetMap.put(alphRep[k], result);
	
		}
		
		return alphabetMap;
	}
	
	/**
	 * prints the hashMap
	 * @param mp
	 */
	public static void printMap(Map mp) {
	    Iterator it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	/**
	 * Print the 1d array
	 * @param array
	 */
	public static void printArray(int[] array){
		for(int i=0; i<array.length; i++){
			System.out.print(array[i]);
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, int[]> alphabetMap = generateAlphabetMap();
		
		printMap(alphabetMap);
	}

}
