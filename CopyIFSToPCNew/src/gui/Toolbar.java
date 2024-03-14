package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Toolbar extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private JButton getButton;
	private JButton upDirButton;
	private JButton copyButton;
	private JButton deleteButton;
	private JButton refreshButton;
	
	private ToolbarListener toolbarListener;
	
	public Toolbar() {
		setBorder(BorderFactory.createEtchedBorder());
		
		getButton = new JButton();
		getButton.setIcon(createIcon("/images/icons8-upload-to-the-cloud-24.png"));
		getButton.setToolTipText("Search Current Directory");

		upDirButton = new JButton();
		upDirButton.setIcon(createIcon("/images/leftarrow_32x32.png"));
		upDirButton.setToolTipText("Move Up A Directory");
		
		copyButton = new JButton();
		copyButton.setIcon(createIcon("/images/savefiles_32x32.png"));
		copyButton.setToolTipText("Copy to PC");

		deleteButton = new JButton();
		deleteButton.setIcon(createIcon("/images/deletebutton_32x32.png"));
		deleteButton.setToolTipText("Delete From IFS");
		
		refreshButton = new JButton();
		refreshButton.setIcon(createIcon("/images/icons8-refresh-32.png"));
		refreshButton.setToolTipText("Refresh Screen");
		
		getButton.addActionListener(this);
		upDirButton.addActionListener(this);
		copyButton.addActionListener(this);
		deleteButton.addActionListener(this);
		refreshButton.addActionListener(this);
		
		Dimension dim = refreshButton.getPreferredSize();
		dim.height = 30;
		dim.width = 30;
		getButton.setPreferredSize(dim);
		upDirButton.setPreferredSize(dim);
		copyButton.setPreferredSize(dim);
		deleteButton.setPreferredSize(dim);
		refreshButton.setPreferredSize(dim);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		add(getButton);
		add(upDirButton);
		add(copyButton);
		add(deleteButton);
		add(refreshButton);
		
		copyButton.setEnabled(false);
	}
	
	private ImageIcon createIcon(String path) {
		URL url = getClass().getResource(path);
		
		if(url == null) {
			System.err.println("Unable to load image: " + path);
		} else {
			ImageIcon icon = new ImageIcon(url);
			return icon;
		}
		
		return null;
	}
	
	public void setToolbarListener(ToolbarListener listener) {
		this.toolbarListener = listener;
	}

	public void getButtonEnable() {
		getButton.setEnabled(true);
	}

	public void getButtonDisable() {
		getButton.setEnabled(false);
	}

	public void upDirButtonEnable() {
		upDirButton.setEnabled(true);
	}

	public void upDirButtonDisable() {
		upDirButton.setEnabled(false);
	}
	
	public void copyButtonEnable() {
		copyButton.setEnabled(true);
	}

	public void copyButtonDisable() {
		copyButton.setEnabled(false);
	}

	public void deleteButtonEnable() {
		deleteButton.setEnabled(true);
	}

	public void deleteButtonDisable() {
		deleteButton.setEnabled(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton clicked = (JButton)e.getSource();
		
		if(clicked == getButton) {
			toolbarListener.getEventOccured();
		}

		if(clicked == upDirButton) {
			toolbarListener.upDirEventOccured();
		}
		
		if(clicked == copyButton) {
			toolbarListener.copyEventOccured();
		}

		if(clicked == deleteButton) {
			toolbarListener.deleteEventOccured();
		}

		if(clicked == refreshButton) {
			toolbarListener.refreshEventOccured();
		}
	}
}
