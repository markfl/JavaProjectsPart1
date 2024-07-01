package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import model.XMLFile;

public class TablePanel  extends JPanel {

	private static final long serialVersionUID = 1L;
	private FileTableModel tableModel;
	private JTable table;
	private TablePanelListener tablePanelListener;
	private XMLFileTableListener xmlFileTableListener;
	private int[] selectedRows;
	private Boolean allRowsSelected;
	private JPopupMenu popup;

	public TablePanel() {
		
		Dimension dim = getPreferredSize();
		dim.width = 500;
		setPreferredSize(dim);
		setMinimumSize(dim);
		
		tableModel = new FileTableModel();
		table = new JTable(tableModel);
		table.setFont(new Font("SanSerif", Font.PLAIN, 15));
		popup = new JPopupMenu();
		
		JMenuItem copyFile = new JMenuItem("Copy to PC");
		JMenuItem deleteFile = new JMenuItem("Delete IFS Files");
		JMenuItem copyIFSFile = new JMenuItem("Copy To IFS");
		popup.add(copyFile);
		popup.add(deleteFile);
		popup.add(copyIFSFile);
		
		setAllRowsSelected(false);
		
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mv) {
				
				setAllRowsSelected(false);
				int row = table.rowAtPoint(mv.getPoint());
				String value = (String)table.getValueAt(row, 0);
				String object = (String)table.getValueAt(row, 2);
				
				if(mv.isControlDown()) {
					selectedRows = table.getSelectedRows();
					tablePanelListener.selectEventOccured(row, value, object);
				} else if(mv.isShiftDown()) {
					clearSelectedRows();
					selectedRows = table.getSelectedRows();
					tablePanelListener.selectEventOccured(row, value, object);
				} else if(mv.getClickCount() == 2) {
					clearSelectedRows();
					selectedRows = table.getSelectedRows();
					tablePanelListener.doubleClickEventOccured(row, value, object);
				} else if(mv.getButton() == MouseEvent.BUTTON1) {
					clearSelectedRows();
					selectedRows = table.getSelectedRows();
					tablePanelListener.selectEventOccured(row, value, object);
				} else if(mv.getButton() == MouseEvent.BUTTON3) {
					if(!object.equals("<DIR>")) {
						popup.show(table, mv.getX(), mv.getY());
						tablePanelListener.rightClickEventOccured(row, value, object);
						table.setRowSelectionInterval(row, row);
					}
				}
			}
		});
		
		copyFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
				if(xmlFileTableListener != null) {
					xmlFileTableListener.rowCopied(row);
				}
			}
		});

		deleteFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
				if(xmlFileTableListener != null) {
					xmlFileTableListener.rowDeleted(row);
				}
			}
		});
		
		table.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent ke) {
				if(ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_A) {
					String value = (String)table.getValueAt(0, 0);
					String object = (String)table.getValueAt(0, 2);
					clearSelectedRows();
					setAllRowsSelected(true);
					selectedRows = table.getSelectedRows();
					if(object.equals("<DIR>"))
						object = "String";
					tablePanelListener.selectEventOccured(0, value, object);
				}
			}

			@Override
			public void keyReleased(KeyEvent ke) {
				if(ke.isControlDown()) {
				}
			}

			@Override
			public void keyTyped(KeyEvent ke) {
				if(ke.isControlDown()) {
				} 
			}			
		});
		
		setLayout(new BorderLayout());
		
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		add(new JScrollPane(table), BorderLayout.CENTER);
		
	}
	
	public void setData(List<XMLFile> file) {
		tableModel.setData(file);
	}
	
	public int getRowCount() {
		return table.getRowCount();
	}
	
	public void refresh() {
		tableModel.fireTableDataChanged();
	}

	public void setTablePanelListener(TablePanelListener listener) {
		this.tablePanelListener = listener;
	}
	
	public void setAutoCreateRowSorter(Boolean value) {
		table.setAutoCreateRowSorter(value);
	}

	public void clearSelectedRows() {
		if(selectedRows != null) {
			if(selectedRows.length > 0) {
				for(int i = 0; i < selectedRows.length; i++) {
					selectedRows[i] = 0;
				}
			}
		}
	}
	
	public void setSelectedRows() {
		int rowCount = getRowCount();
		if(rowCount > 0) {
			for(int i = 0; i < rowCount; i++) {
				selectedRows[i] = i;
			}
		}
	}

	public int[] getSelectedRows() {
		selectedRows = table.getSelectedRows();
		return selectedRows;
	}

	public void setSelectedRows(int[] selectedRows) {
		this.selectedRows = selectedRows;
	}
	
	public String getValueAt(int row, int column) {
		return (String)table.getValueAt(selectedRows[row], column);
	}

	public Boolean getAllRowsSelected() {
		return allRowsSelected;
	}

	public void setAllRowsSelected(Boolean allRowsSelected) {
		this.allRowsSelected = allRowsSelected;
	}
	
	public void setXMLFileTableListener(XMLFileTableListener listener) {
		this.xmlFileTableListener = listener;
	}
}
