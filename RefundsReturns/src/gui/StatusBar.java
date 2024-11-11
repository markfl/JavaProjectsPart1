package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class StatusBar extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel label;
	
	public StatusBar() {
		
		Dimension dim = getPreferredSize();
		dim.width = 750;
		dim.height = 30;
		setPreferredSize(dim);
		setMinimumSize(dim);
		
		Border innerBorder = BorderFactory.createEmptyBorder();
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		
		label = new JLabel();
		
		add(label, BorderLayout.CENTER);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
	}

	public void setLabel(String text) {
		this.label.setText(text);
	}
}