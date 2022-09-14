import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TextEditor extends JFrame implements ActionListener{

	// --------------------------------------------------- Text Area and Text Editor Appearance
	JTextArea textArea; 		// Text area
	JScrollPane scrollPane;		// Scroll pane
	JLabel fontSizeLabel;		// Label for the font size
	JSpinner fontSizeSpinner;	// Box to change the font size
	JButton fontColourButton;	// Font colour button
	JComboBox fontBox;			// Box for the font type
	
	// --------------------------------------------------- Files Menu
	JMenuBar menuBar;	// Menu bar
	JMenu fileMenu;	// Menu option
	JMenuItem openItem;	// Option 1
	JMenuItem saveItem;	// Option 2
	JMenuItem exitItem;	// Option 3
	
	// --------------------------------------------------- Constructor
	public TextEditor() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Create the frame with the X to close
		this.setTitle("Juanjo's Text Editor");	// Text editor name
		this.setSize(600,600);	// Text editor size
		this.setLayout(new FlowLayout());	// Create layout
		this.setLocationRelativeTo(null); 	// Set the position in the middle
		
		
		// --------------------------------------------------- Text Area and Text Editor Appearance
		// Text Area
		textArea = new JTextArea(); // Create text area
		textArea.setLineWrap(true);	// Create a new text line when the text reaches the end of it
		textArea.setWrapStyleWord(true);	// Wrap style word
		textArea.setFont(new Font("Arial", Font.PLAIN, 16)); // Font type and size
		
		// Scroll Pane
		scrollPane = new JScrollPane(textArea); // Add in the scroll pane the text area
		scrollPane.setPreferredSize(new Dimension(500, 500));	// Scroll size
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // Create Scroll bar
		
		// Font size label
		fontSizeLabel = new JLabel("Size: ");  // Label name
		
		// Font Size Spinner
		fontSizeSpinner = new JSpinner(); // Add a box to make the font bigger or smaller
		fontSizeSpinner.setPreferredSize(new Dimension(50,30)); // Size
		fontSizeSpinner.setValue(16); // Set the default font size
		fontSizeSpinner.addChangeListener(new ChangeListener() { 

			@Override
			public void stateChanged(ChangeEvent e) {
				textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int)fontSizeSpinner.getValue()));
			}
			
		});
		
		// Font colour button
		fontColourButton = new JButton("Colour");	// Colour button
		fontColourButton.addActionListener(this);	// Listener for the button
		
		// Font type box
		// Array of Java's fonts available
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		
		fontBox = new JComboBox(fonts); 	// it adds "fonts" array to the combo box
		fontBox.addActionListener(this);	// Listener to give functionality to the box
		fontBox.setSelectedItem("Arial");	// Default font
		
		// --------------------------------------------------- Files Menu
		// This add a bar with a menu named File with the following options: Open, save and exit the file
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		openItem = new JMenuItem("Open file...");
		saveItem = new JMenuItem("Save as...");
		exitItem = new JMenuItem("Exit");
		
		openItem.addActionListener(this);		//
		saveItem.addActionListener(this);		// Add actions to the file menu options
		exitItem.addActionListener(this);		//
		
		fileMenu.add(openItem); // Adds to file menu Open file
		fileMenu.add(saveItem); // Adds to file menu Save file
		fileMenu.add(exitItem); // Adds to file menu Exit file
		menuBar.add(fileMenu);	// Adds to menuBar the file menu named "File"

		
		this.setJMenuBar(menuBar);
		
		this.add(fontSizeLabel);
		this.add(fontSizeSpinner);
		this.add(fontColourButton);
		this.add(fontBox);
		this.add(scrollPane);
		
		this.setVisible(true); // Used to see the "text editor" area
	}
	
	// Method to apply actions to our text area
	@Override
	public void actionPerformed(ActionEvent e) {

		// Font colour button action
		if(e.getSource() == fontColourButton){
			JColorChooser colorChooser = new JColorChooser();
			
			// Window for the colour selection by the user with a title and a default colour
			Color color = colorChooser.showDialog(null, "Select a color", Color.black);
			
			// Here the foreground colour is applied to the text area
			textArea.setForeground(color);
		}
		
		// Font box action
		if(e.getSource() == fontBox) {
			textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
		}
		
		// File menu option actions
		// Open file...
		if(e.getSource() == openItem) {
			JFileChooser fileChooser = new JFileChooser(); // File chooser to open project
			fileChooser.setCurrentDirectory(new File(".")); // Set directory / "." means default project folder
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files","txt"); // Extension filter
			fileChooser.setFileFilter(filter); // Set the extension to "fileChooser"
			
			int response = fileChooser.showOpenDialog(null);
			
			if (response == JFileChooser.APPROVE_OPTION) {
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath()); // Obtains the selected file path
				Scanner fileIn = null;
				
				try {
					fileIn = new Scanner(file);
					if (file.isFile()) {
						while (fileIn.hasNextLine()) {
							String line= fileIn.nextLine() + "\n"; // All lines read are saved in "line
							textArea.append(line); // the line is add to the text area
						}
					}
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				finally {
					fileIn.close(); // file Reader closed
				}
			}
		}
		
		// Save file...
		if(e.getSource() == saveItem) {
			JFileChooser fileChooser = new JFileChooser(); // File chooser to save project
			fileChooser.setCurrentDirectory(new File(".")); // Set directory / "." means default project folder
			
			int response = fileChooser.showSaveDialog(null);  // Save window
			
			if(response == JFileChooser.APPROVE_OPTION) { // If YES or OK is chosen
				File file;
				PrintWriter fileOut = null;
				
				file = new File(fileChooser.getSelectedFile().getAbsolutePath()); // The file is created in the path selected
				try {
					fileOut = new PrintWriter(file); // Open file to write 
					fileOut.println(textArea.getText()); // It gets the text area text and it is passed to the file created
					
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				finally {
					fileOut.close(); // File writer is closed
				}
			}
		}
		
		// Exit file...
		if(e.getSource() == exitItem) {
			System.exit(0); // Exit
		}
	}
}
