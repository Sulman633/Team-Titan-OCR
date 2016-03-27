package gui;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import preprocess.*;


public class GlobalImage {
	
	public static BufferedImage mainImage;
	public static Ink writing;
	
	public static void setImage(BufferedImage x){
		mainImage = x;
	}
	
	public static void preProcess(){
		
		int width = mainImage.getWidth();
		int height = mainImage.getHeight();
		
		Pixel[][] pixArray; //array representation of image
		writing = new Ink(); //collection of pixels that contain ink
		
		pixArray= new Pixel[width][height];
		
		//mapping image to array and detecting non-white pixels
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				
				Pixel p = new Pixel(mainImage.getRGB(i, j),i,j);
				pixArray[i][j]=p;
				if(!p.checkWhite()) writing.addPixel(p);
			}
		}
		
		writing.findClusters(pixArray); //transforms the collection of pixels to a collection of clusters
		writing.findLines(pixArray);
		
		
		
		
		//System.out.println(writing.lines.size());
		for(int j=0; j<writing.lines.size(); j++){
			System.out.println(writing.lines.get(j).words.size());
			//System.out.println(writing.lines.get(j).getHeight());
			//for(int i=0; i<writing.lines.get(j).words.size();i++){
				//Display(writing.lines.get(j).words.get(i));
			//}
			
		}
		
		
		
		
		
		
		
		
		
	}
	
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
