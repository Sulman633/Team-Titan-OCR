package test;

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedImage img = null;
		
		try{//get image
			File imgf = new File("TestingSetBeta.jpg");
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
		
		//Perform scanning of the image for features. -Tony
		PatternDetector pd = new PatternDetector();		
		pd.scan(pixarray);
		
		writing.findClusters(pixarray);
		
	
//		System.out.println(writing.set.size() + "/" + paper.set.size());
//		System.out.println(writing.clus.size());
//		for(int i=0; i<writing.clus.size();i++){
//			System.out.println(writing.clus.get(i).getArea());
//			
//		}

		
		
		
		
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
		
		
		//Highlighting scanning lines. -Tony
//		ArrayList<Integer> lines = pd.getLineCentres();
//		int temp;
//		
//		for (int row = 0; row < img.getHeight(); row++) {
//			
//			temp = row - (pd.getLineHeight() / 2);
//			
//			if (temp >= 0 && lines.contains(row)) {
//				
//				for (int i = 0; i < pd.getLineHeight(); i++)
//					for (int col = 0; col < img.getWidth(); col++)
//						if (img.getRGB(col, temp + i) != -16777216)
//							img.setRGB(col, temp + i, -20561);
//				
//				for (int col = 0; col < img.getWidth(); col++)
//
//					if (img.getRGB(col, row) != -16777216)
//						img.setRGB(col, row, -65536);
//											
//			}
//			
//		}
		
		//Letter highlighting - Tony
		ArrayList<Integer> starts = pd.getLetterBeginning();
		ArrayList<Integer> ends = pd.getLetterEnding();
		ArrayList<Integer> heights = pd.getLetterHeight();
		int count = pd.getLetterCount();
		int height = pd.getLineHeight();
		
		for (int letter = 0; letter < count; letter++) {
		
			for (int x = starts.get(letter); x < ends.get(letter); x++) {
				
				for (int y = heights.get(letter); y < heights.get(letter) + height; y++) {
					
					if (img.getRGB(x, y) != -16777216)
						img.setRGB(x, y, -16711681);
					
				}
				
			}
			
		}		
		
		
		//System.out.println(data.length);
		
		JFrame frame = new JFrame();
		  //JLabel label0 = new JLabel(new ImageIcon(pd.getLetters().get(0)));
		  JLabel label0 = new JLabel(new ImageIcon(img));
		  frame.getContentPane().add(label0, BorderLayout.CENTER);
		  frame.pack();
		  frame.setVisible(true);

	}

}
