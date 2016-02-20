package preprocess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class imgtest {
	
	// 20x20 array. neuralNetworkInput
	static int[][] neuralNetworkInput = new int[20][20];
	
	// generates input 
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
	
	
	public static void print(int[][] array){
		for(int i=0; i<array.length; i++){
			for(int j=0; j<array[i].length; j++){
				System.out.print(array[i][j] + ",");
			}
		}
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedImage img = null;
		
		try{//get image
			File imgf = new File("testCaseA.jpg");
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
		print(neuralNetworkInput);
		
		
		
		
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
		
		JFrame frame = new JFrame();
		  JLabel label = new JLabel(new ImageIcon(img));
		  frame.getContentPane().add(label, BorderLayout.CENTER);
		  frame.pack();
		  frame.setVisible(true);

	}

}
