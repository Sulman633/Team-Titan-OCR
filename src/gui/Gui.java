package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.*;

@SuppressWarnings("serial")
public class Gui extends JFrame {
	private String fileName;
	private JPanel contentPane; //The content pane on the JFrame
	private JTextField txtErrorArea; //This is used to display errors
	private JFileChooser fc; //The File Chooser
	private BufferedImage bufferedImage; //The BufferedImage to passed on after retrieving the info
	private JPanel beforePanel; //The display panel on the left for displaying 
	private JTextArea txtAreaAfter; //The text area that it will be written to on the right
	private PDDocument document;
	private static String inputError = "Please choose a Image or PDF before clicking this button!";
	neuralnetwork.BackProp nn;
	
	public Gui(){
		//Creates the JFrame
		initializeGui(); //Create the GUI 
		pack(); //Keeps it locked resolution
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
	private void open(){
		//Helps to filter the file types
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg", "pdf");
		fc = new JFileChooser();
		fc.setFileFilter(filter); //Sets the filter for the FileChooser
		fc.setAcceptAllFileFilterUsed(false); //Removes the "all files" option from the file chooser
		File temp = new File(System.getProperty("user.home"));
		fc.setCurrentDirectory(temp);
		int returnValue = fc.showOpenDialog(null);
		
	    if (returnValue == JFileChooser.APPROVE_OPTION) {
	        /*
	         * These four lines get name of the file and gets
	         * the extension of the file to compare it to 
	         * the conditional statement.
	         */
	    	File selectedFile = fc.getSelectedFile(); //Stores the selected file into a variable
	        fileName = selectedFile.getName(); //Gets the name the file
	        int index = selectedFile.getName().lastIndexOf('.');
	        String newFileName = fileName.substring(index); //Gets the extension
	        /*
	         * If PDF extension then use the PDFBox library to convert it
	         */
	        if(".pdf".equals(newFileName)){
	        	try {
	        		System.out.println("This is a test");
	        		document = PDDocument.load(selectedFile); //Loads the PDF file
					@SuppressWarnings("unchecked")
					List<PDPage> list = document.getDocumentCatalog().getAllPages(); //Gets all of the pages of the PDF
	        		PDPage page = list.get(0);
	        		bufferedImage = page.convertToImage(); //Converts the PDF
					//Scales the bufferedImage to the size of the JPanel for displaying. 
					bufferedImage = scaledImage(bufferedImage, beforePanel.getWidth(),beforePanel.getHeight());
					//Cleans the JPanel before displaying.
					beforePanel.removeAll();
					//Create a new JPanel to hold the image with the same dimensions as the JPanel.
					JLabel picLabel = new JLabel();
					picLabel.setIcon(new ImageIcon(bufferedImage));
					picLabel.setBounds(0, 0, beforePanel.getWidth(), beforePanel.getHeight());
					beforePanel.add(picLabel);
					beforePanel.repaint(); //Redo the rendering for JPanel
	        		
	        	} catch (IOException e) {
	        		e.printStackTrace();
	        	}
	        }else{
		        try {
					bufferedImage = ImageIO.read(selectedFile); //Converts the file into a BufferedImage
					//Scales the bufferedImage to the size of the JPanel for displaying. 
					bufferedImage = scaledImage(bufferedImage, beforePanel.getWidth(),beforePanel.getHeight());
					//Cleans the JPanel before displaying.
					beforePanel.removeAll();
					//Create a new JPanel to hold the image with the same dimensions as the JPanel.
					JLabel picLabel = new JLabel();
					picLabel.setIcon(new ImageIcon(bufferedImage));
					picLabel.setBounds(0, 0, beforePanel.getWidth(), beforePanel.getHeight());
					beforePanel.add(picLabel);
					beforePanel.repaint();
				} catch (IOException e1) {
					txtErrorArea.setText("Error! Image format not supported");
				}
	        }
	    }
	}
	//Generates the document after converting it 
	private void generate(){
		//Must first have a image loaded to use this option
		if(bufferedImage != null){
			System.out.println("starting NN");
			neuralnetwork.BackProp nn = new neuralnetwork.BackProp();
			nn.productionMode = true;
			nn.testingFile = fileName;
			String results = nn.runNN(null);
			
			display(results);
			nn.destroy();
		}
		else {
			txtErrorArea.setText(inputError);
		}
	}
	
	/**
	 * Using a buffered and file writer the program writes to a file when the save button is clicked.
	 * @param text - the text that is written to the file. 
	 */
	@SuppressWarnings("unused")
	private void save(String text){
		//Must first have a image loaded to use this option
		if(bufferedImage !=null){
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
		}else{
			txtErrorArea.setText(inputError);
		}
	}
	
	public void displayImage(BufferedImage img){
		
	}
	
	public void display(String word){
		txtAreaAfter.setText(word);
	}
	/*
	 * Creates the GUI with appropriate buttons and their 
	 * corresponding action listeners. 
	 */
	private void initializeGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 728, 530);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		beforePanel = new JPanel();
		beforePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		beforePanel.setBackground(Color.WHITE);
		/*
		 * The initialization of the buttons and there corresponding 
		 * action listeners.
		 */
		JButton btnOpen = new JButton("Open");
		
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				open(); //Starts the open process
			}
		});
		
		btnOpen.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JButton btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generate();
			}
		});
		
		btnGenerate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		//Both of the labels above the JPanels on GUI
		JLabel lblBefore = new JLabel("Before");
		lblBefore.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel lblAfter = new JLabel("After");
		lblAfter.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		//The text area for errors for user on GUI
		txtErrorArea = new JTextField();
		txtErrorArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtErrorArea.setColumns(10);
		
		txtAreaAfter = new JTextArea();
		txtAreaAfter.setLineWrap(true);
		txtAreaAfter.setBorder(BorderFactory.createLineBorder(Color.black));
		txtAreaAfter.setBackground(Color.WHITE);
		JScrollPane scrollPane = new JScrollPane(txtAreaAfter);
		
		//Creates the positioning of each component in a GroupLayout
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(beforePanel, GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 520, GroupLayout.PREFERRED_SIZE)
							.addGap(10))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(180)
							.addComponent(btnOpen, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
							.addGap(50)
							.addComponent(btnGenerate, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
							.addGap(50)
							.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(250)
					.addComponent(lblBefore)
					.addPreferredGap(ComponentPlacement.RELATED, 322, Short.MAX_VALUE)
					.addComponent(lblAfter)
					.addGap(260))
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
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
						.addComponent(beforePanel, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE))
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
