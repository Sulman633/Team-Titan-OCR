package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Gui extends JFrame {
	private JPanel contentPane;
	private JTextField txtErrorArea; //This is used to display errors
	private JFileChooser fc;
	private BufferedImage bufferedImage;
	
	public Gui(){
		//Creates the JFrame
		initializeGui();
		pack();
		setVisible(true);
		txtErrorArea.setEditable(false);
	}
	/**
	 * Resizes an image using a Graphics2D object backed by a BufferedImage.
	 * @param srcImg - source image to scale
	 * @param w - desired width
	 * @param h - desired height
	 * @return - the new resized image
	 */
	private BufferedImage scaledImage(BufferedImage srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();
	    return resizedImg;
	}
	
	//This method will be used to start the pre-processing 
	public void open(){
		
	}
	
	public void generate(){
		
	}
	/**
	 * Using a buffered and file writer the program writes to a file when the save button is clicked.
	 * @param text - the text that is written to the file. 
	 */
	public void save(String text){
		File file = new File("C:\\Users\\Sulman\\Documents\\EclipseProjects\\Team-Titan-OCR\\output.txt");
		if(!text.equals(" ")){
			BufferedWriter writer = null;
			try{
			    writer = new BufferedWriter( new FileWriter(file));
			    writer.write(text);

			}
			catch (IOException e){
			
			}
			finally{
			    try{
			        if ( writer != null)
			        writer.close();
			    }
			    catch (IOException e){
			    
			    }
			}
		}
		else{
			txtErrorArea.setText("No file selected which can be saved!");
		}
	}
	
	public void displayImage(Image img){
		
	}
	
	public void display(String word){
		
	}
	 
	private void initializeGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 728, 530);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel beforePanel = new JPanel();
		beforePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		beforePanel.setBackground(Color.WHITE);
		
		JButton btnOpen = new JButton("Open");
		//The action listener for Open button and uses JFileChooser
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Helps to filter the file types
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg", "pdf");
				fc = new JFileChooser();
				fc.setFileFilter(filter);
				fc.setAcceptAllFileFilterUsed(false);
				File temp = new File(System.getProperty("user.home"));
				fc.setCurrentDirectory(temp);
				int returnValue = fc.showOpenDialog(null);
			    if (returnValue == JFileChooser.APPROVE_OPTION) {
			        File selectedFile = fc.getSelectedFile();
			        try {
						bufferedImage = ImageIO.read(selectedFile);
						bufferedImage = scaledImage(bufferedImage, beforePanel.getWidth(),beforePanel.getHeight());
						beforePanel.removeAll();
						JLabel picLabel = new JLabel();
						picLabel.setIcon(new ImageIcon(bufferedImage));
						picLabel.setBounds(0, 0, beforePanel.getWidth(), beforePanel.getHeight());
						beforePanel.add(picLabel);
						beforePanel.repaint();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			    }
			}
		});
		btnOpen.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JButton btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnGenerate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblBefore = new JLabel("Before");
		lblBefore.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel lblAfter = new JLabel("After");
		lblAfter.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		txtErrorArea = new JTextField();
		txtErrorArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtErrorArea.setColumns(10);
		
		JTextArea txtAreaAfter = new JTextArea();
		txtAreaAfter.setLineWrap(true);
		txtAreaAfter.setBorder(BorderFactory.createLineBorder(Color.black));
		txtAreaAfter.setBackground(Color.WHITE);
		JScrollPane scrollPane = new JScrollPane(txtAreaAfter);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(beforePanel, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 338, GroupLayout.PREFERRED_SIZE)
							.addGap(1))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnOpen, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
							.addGap(51)
							.addComponent(btnGenerate, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
							.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(139)
					.addComponent(lblBefore)
					.addPreferredGap(ComponentPlacement.RELATED, 322, Short.MAX_VALUE)
					.addComponent(lblAfter)
					.addGap(169))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(txtErrorArea, GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBefore)
						.addComponent(lblAfter))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
						.addComponent(beforePanel, GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtErrorArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnOpen, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnGenerate, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
		
	}

	public static void main(String[] args) {
		new Gui();

	}

}
