package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class FormPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JLabel directoryLabel;
	private JTextField directoryField;
	private Preferences prefs;
	@SuppressWarnings("unused")
	private FormListener formListener;
	private String currentIFSDirectory;
	
	public FormPanel() {
		
		Dimension dim = getPreferredSize();
		dim.height = 100;
		setPreferredSize(dim);
		setMinimumSize(dim);
		
		prefs = Preferences.userRoot().node("CopyIFSToPC");
		
		directoryLabel = new JLabel("Directory: ");
		directoryLabel.setDisplayedMnemonic(KeyEvent.VK_S);
		directoryLabel.setLabelFor(directoryField);
		directoryLabel.setFont(new Font("SanSerif", Font.PLAIN, 30));
		
		directoryField = new JTextField(500);
		currentIFSDirectory = prefs.get("currentIFSDirectory", "");
		// currentIFSDirectory = "/magentoapi/";
		int a = currentIFSDirectory.indexOf("//");
		if (a > 0) {
			currentIFSDirectory = "/";
		}
		
		if(currentIFSDirectory == "") {
			directoryField.setText("/");
		} else {
			directoryField.setText(currentIFSDirectory);
		}
		directoryField.setFont(new Font("SanSerif", Font.PLAIN, 30));
		
		Border innerBorder = BorderFactory.createTitledBorder(null, "Get Files and Directories",
				TitledBorder.LEFT , TitledBorder.ABOVE_TOP,new Font("SanSerif", Font.PLAIN, 15));
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		
		layoutComponents();
	}
	
	public String getDirectoryField() {
		return directoryField.getText();
	}

	public void setDirectoryField(JTextField directoryField) {
		this.directoryField = directoryField;
	}
	
	public void setDirectoryField(String directory) {
		this.directoryField.setText(directory);
	}

	public void setDirectoryFocus() {
		directoryField.requestFocus();
	}
	
	public void layoutComponents() {

		setLayout(new BorderLayout());
		
		add(directoryLabel, BorderLayout.WEST);
		add(directoryField, BorderLayout.CENTER);
	}
	
	public void setFormListener(FormListener listener) {
		this.formListener = listener;
	}

	public void clearForm() {
		directoryField.setText(currentIFSDirectory);
	}
}