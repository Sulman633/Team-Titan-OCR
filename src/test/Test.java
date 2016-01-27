package test;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Test {
	public static void main(String[] args){
		System.out.println("Test if this is working for you guys!");
		System.out.println("Matt push workgot ");
		
		acceptJpeg();
	}
	
	public static void acceptPDF(){
		
	}
	
	public static void acceptJpeg(){
		try {
			//Loads the image in as a BufferedImage file type
			//Google what a Buffered Image is if you dont know!!
            BufferedImage in = ImageIO.read(new File("test.jpg"));

            //This is all GUI stuff
            JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(in)), "Yeah", JOptionPane.INFORMATION_MESSAGE);
            BufferedImage out = new BufferedImage(in.getWidth(null), in.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = out.createGraphics();
            g2d.drawImage(in, 0, 0, null);
            g2d.dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
}
