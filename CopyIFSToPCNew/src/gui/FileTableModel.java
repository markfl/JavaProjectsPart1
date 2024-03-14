package gui;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.XMLFile;

public class FileTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;

	private List<XMLFile> file;
	
	private String[] colNames = {"Object Name", "Date", "File Size"};
	
	public FileTableModel() {
	}
	
	public void setData(List<XMLFile> file) {
		this.file = file;
	}
	
	public String getColumnName(int column) {
		return colNames[column];
	}

	public int getColumnCount() {
		
		return 3;
	}

	public int getRowCount() {
		return file.size();		
	}

	public Object getValueAt(int row, int col) {
		
		try {
			XMLFile xmlFile = file.get(row);
			
			switch(col) {
			case 0:
				return xmlFile.getName();
			case 1:
				return xmlFile.getDate();
			case 2:
				return xmlFile.getSize();
			}
		} catch (Exception e) {
			System.out.println(row + " - " + col);
		}
		
		
		return null;
	}
}