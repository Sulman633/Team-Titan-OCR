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
import javax.swing.WindowConstants;


public class Preprocess {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedImage img = null;
		
		try{//get image
			File imgf = new File("test10.jpg");
			img = ImageIO.read(imgf);
		}
		catch (IOException e) {
		  }
		
		
		//start -----------
		int width = img.getWidth();
		int height = img.getHeight();
		
		Pixel[][] pixArray; //array representation of image
		Ink writing = new Ink(); //collection of pixels that contain ink
		
		pixArray= new Pixel[width][height];
		
		//mapping image to array and detecting non-white pixels
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				
				Pixel p = new Pixel(img.getRGB(i, j),i,j);
				pixArray[i][j]=p;
				if(!p.checkWhite()) writing.addPixel(p);
			}
		}
		
		writing.findClusters(pixArray); //transforms the collection of pixels to a collection of clusters
		writing.findLines(pixArray); //transforms the collection of clusters to a collection of lines

		writing.lines.get(1).words.get(6).findLetters();
		Display(writing.lines.get(1).words.get(6));
		


	}
	
	
	

	//for testing purposes, not part of the actual algorithm
	public static void Display(Word x){
		int h = x.getHeight();
		int w = x.getWidth();
		
		if(h==0) h=1;
		if(w==0) w=1;
		
		BufferedImage image = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		
		int[][] imgArray = x.imgArray();
		
		int[] data = new int[h*w];
				
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				data[(i*w)+j]=imgArray[j][i];
				
			}
			
		}
		
		image.setRGB(0, 0, w, h, data, 0, w);
		
		JFrame frame = new JFrame();
		  JLabel label = new JLabel(new ImageIcon(image));
		  frame.getContentPane().add(label, BorderLayout.CENTER);
		  frame.pack();
		  frame.setVisible(true);
		  //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		
	}

}
