package gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private JButton cancelButton;
	private JProgressBar progressBar;
	
	public ProgressDialog(Window parent, String title) {
		super(parent, title, ModalityType.APPLICATION_MODAL);
		
		cancelButton = new JButton("Cancel");
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		
		progressBar.setIndeterminate(true);
		
		progressBar.setString(title);
		
		setLayout(new FlowLayout());
		
		Dimension size = cancelButton.getPreferredSize();
		size.width = 400;
		progressBar.setPreferredSize(size);
		
		add(progressBar);
		add(cancelButton);
		
		cancelButton.setEnabled(false);
		cancelButton.setVisible(false);
		
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		pack();
		
		setLocationRelativeTo(parent);
	}
	
	public void setMaximum(int value) {
		progressBar.setMaximum(value);
	}
	
	public void setValue(int value) {
		progressBar.setValue(value);
	}
	
	public void setVisible(final boolean visible) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				if(visible == false) {
					setCursor(Cursor.getDefaultCursor());
				} else {
					progressBar.setValue(0);
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				}
				
				ProgressDialog.super.setVisible(visible);
			}
		});
	}
}