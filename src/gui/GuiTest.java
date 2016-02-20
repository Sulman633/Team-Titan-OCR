package gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.border.LineBorder;

public class GuiTest extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiTest frame = new GuiTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GuiTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 616, 499);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel beforePanel = new JPanel();
		beforePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		beforePanel.setBackground(Color.WHITE);
		
		JPanel afterPanel = new JPanel();
		afterPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		afterPanel.setBackground(Color.WHITE);
		
		JLabel lblBefore = new JLabel("Before");
		lblBefore.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel lblAfter = new JLabel("After");
		lblAfter.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnOpen = new JButton("Open");
		btnOpen.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton btnGenerate = new JButton("Generate");
		btnGenerate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(121)
					.addComponent(lblBefore)
					.addPreferredGap(ComponentPlacement.RELATED, 266, Short.MAX_VALUE)
					.addComponent(lblAfter)
					.addGap(131))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(23)
					.addComponent(beforePanel, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(afterPanel, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(11, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(135)
					.addComponent(btnOpen, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnGenerate, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(129, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBefore)
						.addComponent(lblAfter))
					.addGap(9)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(afterPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(beforePanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE))
					.addGap(45)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOpen, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
						.addComponent(btnGenerate, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
						.addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
}
