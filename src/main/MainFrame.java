package main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame {

	private final static String title="Steganography";
	
	public final static String ADD_ENCRYPT="AE";
	public final static String ADD_DECRYPT="AD";
	public final static String LSB_ENCRYPT="LE";
	public final static String LSB_DECRYPT="LD";
	
	private JFileChooser fileChooser=new JFileChooser();
	private JLabel sourceImageContainer;
	private JLabel cryptedImageContainer;
	
	public MainFrame() throws HeadlessException {
		super(title);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(700, 600));
		setResizable(false);	
		
		//fileChooser.setFileFilter(new FileNameExtensionFilter("BMP files", "bmp"));
		
		GridBagLayout gridLayout=new GridBagLayout();
		setLayout(gridLayout);
		
		GridBagConstraints constraints=new GridBagConstraints();
		constraints.gridx=0;
		constraints.gridy=0;
		constraints.gridwidth=1;
		constraints.gridheight=1;
		constraints.weightx=110;
		constraints.weighty=110;
		constraints.fill=GridBagConstraints.BOTH;
		
		JPanel leftPanel=new JPanel();
		BoxLayout leftLayout=new BoxLayout(leftPanel, BoxLayout.Y_AXIS);
		leftPanel.setLayout(leftLayout);
		
		JLabel sourceImageLabel=new JLabel("Source image");
		sourceImageContainer=new JLabel(new ImageIcon("default_icon.png"));
		JLabel cryptedImageLabel=new JLabel("Crypted image");
		cryptedImageContainer=new JLabel(new ImageIcon("default_icon.png"));
		
		leftPanel.add(sourceImageLabel);
		leftPanel.add(sourceImageContainer);
		leftPanel.add(cryptedImageLabel);
		leftPanel.add(cryptedImageContainer);
		
		JScrollPane scrollPane=new JScrollPane(leftPanel);
		gridLayout.setConstraints(scrollPane, constraints);
		add(scrollPane);
		
		constraints.gridx=4;
		constraints.gridy=0;
		constraints.gridwidth=1;
		constraints.gridheight=1;
		constraints.weightx=10;
		constraints.weighty=10;
		constraints.fill=GridBagConstraints.HORIZONTAL;
		constraints.anchor=GridBagConstraints.NORTH;
		
		JPanel rightPanel=new JPanel();
		GridBagLayout buttonsLayout=new GridBagLayout();
		rightPanel.setLayout(buttonsLayout);
		
		GridBagConstraints buttonConstraints=new GridBagConstraints();
		buttonConstraints.gridx=0;
		buttonConstraints.gridy=0;
		buttonConstraints.fill=GridBagConstraints.HORIZONTAL;
		buttonConstraints.gridwidth=1;
		buttonConstraints.gridheight=1;
		
		JButton additiveEncrypt=new JButton("Additive encrypt");
		additiveEncrypt.setActionCommand(ADD_ENCRYPT);
		additiveEncrypt.addActionListener(new ButtonActionListener());
		buttonsLayout.setConstraints(additiveEncrypt, buttonConstraints);
		rightPanel.add(additiveEncrypt);
		
		buttonConstraints.gridy=1;
		
		JButton additiveDecrypt=new JButton("Additive decrypt");
		additiveDecrypt.setActionCommand(ADD_DECRYPT);
		additiveDecrypt.addActionListener(new ButtonActionListener());
		buttonsLayout.setConstraints(additiveDecrypt, buttonConstraints);
		rightPanel.add(additiveDecrypt);
		
		buttonConstraints.gridy=2;
		
		JButton lsbEncrypt=new JButton("LSB encrypt");
		lsbEncrypt.setActionCommand(LSB_ENCRYPT);
		lsbEncrypt.addActionListener(new ButtonActionListener());
		buttonsLayout.setConstraints(lsbEncrypt, buttonConstraints);
		rightPanel.add(lsbEncrypt);
		
		buttonConstraints.gridy=3;
		
		JButton lsbDecrypt=new JButton("LSB decrypt");
		lsbDecrypt.setActionCommand(LSB_DECRYPT);
		lsbDecrypt.addActionListener(new ButtonActionListener());
		buttonsLayout.setConstraints(lsbDecrypt, buttonConstraints);
		rightPanel.add(lsbDecrypt);		
		
		gridLayout.setConstraints(rightPanel, constraints);
		add(rightPanel);
		
		pack();
		setVisible(true);
		
	}
	
	private class ButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String command=arg0.getActionCommand();
			if(command.equals(ADD_ENCRYPT)){
				
				fileChooser.showDialog(rootPane, "Select file to encrypt:");
				File file=fileChooser.getSelectedFile();
				
				if(file!=null){
					System.out.println(file.getAbsolutePath());
					//Image image=ImageIO.read(file);
					rootPane.invalidate();
					JOptionPane.showInputDialog(rootPane, "Enter a string to encrypt into image:");
				}
				
			}else if(command.equals(ADD_DECRYPT)){
				
			}else if(command.equals(LSB_ENCRYPT)){
				
			}else{
				
			}
		}
		
	}
	
	

}
