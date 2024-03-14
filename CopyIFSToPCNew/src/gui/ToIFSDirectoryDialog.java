package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;

public class ToIFSDirectoryDialog extends JDialog {
	
	private static final long serialVersionUID = -7587588214954093810L;
	private JButton okButton;
	private JButton cancelButton;
	
	public ToIFSDirectoryDialog(Window parent, String title, Boolean indeterminate) {
		super(parent, title, ModalityType.APPLICATION_MODAL);
		
		okButton = new JButton("Ok");
		cancelButton = new JButton("Cancel");
		
		setLayout(new FlowLayout());
		
		Dimension size = cancelButton.getPreferredSize();
		size.width = 400;
		
		add(okButton);
		add(cancelButton);
		
		setLocationRelativeTo(parent);
	}	
}