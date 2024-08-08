package gui;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileFilter;
import com.ibm.as400.access.IFSFileInputStream;

import controller.Controller;
import model.IBMi;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private Toolbar toolBar;
	private FormPanel formPanel;
	private Controller controller;
	private Preferences prefs;
	private TablePanel tablePanel;
	private ProgressDialog progressDialog;
	private ProgressDialog progressDialogCopy;
	private ProgressDialog progressDialogDelete;
	private StatusBar statusBar;
	private JSplitPane splitPane;
	private JFileChooser fileChooser;
	private AS400 as400;
	private Stack<String> lastDir;
	
	public MainFrame() {
		
		super("Copy/Delete IFS Files to PC");
		
		try {
			for (LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e1) {
			System.out.println("Can't set look and feel.");
		}
		
		prefs = Preferences.userRoot().node("CopyIFSToPC");
		String systemName = prefs.get("systemname", "westend");
		prefs.put("currentLocalDirectory", "C:\\Temp\\download");

		/*try {
			prefs.clear();
			prefs.put("currentIFSDirectory", "");
			prefs.put("currentLocalDirectory", "C:\\Temp\\download");
		} catch (BackingStoreException e1) {
			e1.printStackTrace();
		}*/
	
		setLayout(new BorderLayout());
		
		toolBar = new Toolbar();
		formPanel = new FormPanel();
		tablePanel = new TablePanel();
		statusBar = new StatusBar();
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tablePanel);
		lastDir = new Stack<String>();
		
		splitPane.setOneTouchExpandable(true);
		
		controller = new Controller();
		progressDialog = new ProgressDialog(this, "Getting All Files & Directories...", true);
		progressDialogCopy = new ProgressDialog(this, "Copying Selected Files...", false);
		progressDialogDelete = new ProgressDialog(this, "Deleting Selected Files...", false);
		
		tablePanel.setData(controller.getFiles());
		
		IBMi dbIBMi = new IBMi(systemName);
	    as400 = dbIBMi.getAS400();
		
		String currentLocalDirectory = prefs.get("currentLocalDirectory", "C:\\Temp\\download");
		int left = prefs.getInt("left", 0);
		int top = prefs.getInt("top", 0);
		int width = prefs.getInt("width", 600);
		int height = prefs.getInt("height", 600);
		setBounds(left, top, width, height);
		
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new java.io.File(currentLocalDirectory));
		fileChooser.setDialogTitle("Select Directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		setJMenuBar(createMenuBar());
		
		tablePanel.setXMLFileTableListener(new XMLFileTableListener() {
			public void rowCopied(int row) {
				copyFiles();
			}
			
			public void rowDeleted(int row) {
				deleteFiles();	
			}
		});

		toolBar.setToolbarListener(new ToolbarListener() {
			
			public void getEventOccured() {
				
				Controller.clearFile();
				toolBar.getButtonEnable();
				toolBar.copyButtonDisable();
				toolBar.deleteButtonDisable();
				String dir = formPanel.getDirectoryField();
				String test = dir.substring(dir.length() - 1);
				if(!test.equals("/")) {
					dir = dir + "/";
					formPanel.setDirectoryField(dir);
				}
				
				try {
					IFSFile ifsFile = new IFSFile(as400, dir);
					if(ifsFile.exists() == false) {
						JOptionPane.showMessageDialog(formPanel, "The directory does not exist", "Retrieval Error", JOptionPane.ERROR_MESSAGE);
					} else {
						prefs.put("currentIFSDirectory", dir);
						checkFormPanelFields(true, false, 0, null);
					}
				} catch (HeadlessException | IOException e) {
				}
			}
			
			public void upDirEventOccured() {
				
				Controller.clearFile();
				String readDir = formPanel.getDirectoryField();
				String dir = null;
				Boolean readLoop = true;
				int a,b,c,d;
				a = b = c = d = 0;
				d = readDir.lastIndexOf("/");
				while (readLoop) {
					a = readDir.indexOf("/", b);
					if (a >= 0) {
						b = readDir.indexOf("/", a+1);
						if(b < d) {
							if (c == 0) {
								dir = readDir.substring(a,b);
							} else {
								dir = dir + readDir.substring(a,b);
							}
							c++;
						} else readLoop = false;
					} else readLoop = false;
				};
				
				if(dir == null) {
					dir = "/";
					formPanel.setDirectoryField(dir);
				}
				
				String test = dir.substring(dir.length() - 1);
				if(!test.equals("/")) {
					dir = dir + "/";
					formPanel.setDirectoryField(dir);
				}
				
				try {
					IFSFile ifsFile = new IFSFile(as400, dir);
					if(ifsFile.exists() == false) {
						JOptionPane.showMessageDialog(formPanel, "The directory does not exist", "Retrieval Error", JOptionPane.ERROR_MESSAGE);
					} else {
						prefs.put("currentIFSDirectory", dir);
					}
				} catch (HeadlessException | IOException e) {
				}
				checkFormPanelFields(true, false, 0, null);
			}
			
			public void copyEventOccured() {
				
				copyFiles();
				
			}

			public void deleteEventOccured() {
				
				deleteFiles();
				
			}

			public void refreshEventOccured() {
				refreshScreen();
			}
		});
		
		tablePanel.setTablePanelListener(new TablePanelListener() {
			
			public void doubleClickEventOccured(int row, String xmlName, String objectType) {
				if(objectType == "<DIR>") {
					String dir = formPanel.getDirectoryField();
					formPanel.setDirectoryField(dir);
					Controller.clearFile();
					checkFormPanelFields(true, false, 0, null);
					if(dir.equals("/")) {
						
					} else {
						
					}
				} else {
					toolBar.copyButtonEnable();
					toolBar.deleteButtonEnable();
				}
			}
			
			public void selectEventOccured(int row, String xmlName, String objectType) {
				if(objectType.equals("<DIR>")) {
					toolBar.getButtonEnable();
					toolBar.copyButtonDisable();
					toolBar.deleteButtonDisable();
					String dir = formPanel.getDirectoryField();
					dir = dir + xmlName + "/";
					formPanel.setDirectoryField(dir);
				} else {
					toolBar.copyButtonEnable();
					toolBar.deleteButtonEnable();
				}
			}

			public void rightClickEventOccured(int row, String xmlName, String objectType) {
				System.out.println("rightClickEventOccured");
			}
		});

		this.setTitle("Copy/Delete " + as400.getSystemName() + " IFS Files to PC");
		
		formPanel.setFormListener(new FormListener() {
			public void formEventOccurred(FormEvent ev) {
				checkFormPanelFields(true, false, 0, null);
			}
		});
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				CloseProgram("Close Box Pressed");
			}
		});
		
		add(toolBar, BorderLayout.NORTH);
		add(splitPane, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		refreshScreen();
		setVisible(true);
		checkFormPanelFields(true, false, 0, null);
		tablePanel.setAutoCreateRowSorter(true);
	}

	private void copyFiles() {
		
		String currentLocalDirectory = prefs.get("currentLocalDirectory", "");
		if (currentLocalDirectory.isEmpty()) {
        	JOptionPane.showMessageDialog(formPanel, "Local directory not set", "Copy Error", JOptionPane.ERROR_MESSAGE);
        	return;
        }
		int[]rows = tablePanel.getSelectedRows();
		if(rows.length <= 1) {
			String currentIFSDirectory = prefs.get("currentIFSDirectory", "");
			
			String object = tablePanel.getValueAt(0, 2);
			if(!object.equals("<DIR>")) {
				String lastSelectedXML = tablePanel.getValueAt(0, 0);
				String copyFromFile = currentIFSDirectory + "/" + lastSelectedXML;
				String copyToFile = currentLocalDirectory + "\\" + lastSelectedXML;
				if(CopyFile(as400, copyFromFile, copyToFile)) {
					statusBar.setLabel("Copy file " + lastSelectedXML + " to "
							+ currentLocalDirectory + " completed normally.");
					System.out.println(lastSelectedXML + " copied.");
				} else {
					JOptionPane.showMessageDialog(formPanel, "Error occured on copy.",
							"Copy Error", JOptionPane.ERROR_MESSAGE);
		        	return;
				}
			}
	

			for(int i = rows.length-1; i >= 0; i--) {
				Controller.removeFile(rows[i]);
			}
		} else {
			progressDialogCopy.setVisible(true);
			String currentIFSDirectory = prefs.get("currentIFSDirectory", "");
			copyDirectory(currentIFSDirectory, rows.length, rows);
		}
		tablePanel.refresh();
		toolBar.copyButtonDisable();
		toolBar.deleteButtonDisable();
	}

	private void deleteFiles() {
		
		int answer = JOptionPane.showConfirmDialog(formPanel, "Are you sure you want to do this?", 
				"Delete Files", JOptionPane.YES_NO_CANCEL_OPTION);
		if(answer == 0) {
			int[]rows = tablePanel.getSelectedRows();
			if(rows.length <= 1) {
				String object = tablePanel.getValueAt(0, 2);
				if(!object.equals("<DIR>")) {
					String currentIFSDirectory = prefs.get("currentIFSDirectory", "");
					String lastSelectedXML = tablePanel.getValueAt(0, 0);
					String deleteFile = currentIFSDirectory + "/" + lastSelectedXML;
					if(DeleteFile(new IFSFile(as400, deleteFile))) {
						statusBar.setLabel("Delete file " + lastSelectedXML + " completed normally.");
					} else {
						JOptionPane.showMessageDialog(formPanel, "Error occured on delete.",
								"Delete Error", JOptionPane.ERROR_MESSAGE);
			        	return;
					}
	
					Controller.removeFile(rows[0]);
				}
				
			} else {
				progressDialogDelete.setVisible(true);
				String currentIFSDirectory = prefs.get("currentIFSDirectory", "");
				clearDirectory(currentIFSDirectory, rows.length);
			}
			tablePanel.refresh();
			toolBar.copyButtonDisable();
			toolBar.deleteButtonDisable();
		}	
	}
	
	private void CloseProgram(String where) {
		prefs.put("currentIFSDirectory", formPanel.getDirectoryField());
		System.out.println(where);
		prefs.putInt("left", getX());
		prefs.putInt("top", getY());
		prefs.putInt("width", getWidth());
		prefs.putInt("height", getHeight());
		as400.disconnectAllServices();
		dispose();
		System.exit(0);
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem getItem = new JMenuItem("Get");
		JMenuItem getDirectoryItem = new JMenuItem("Select Local Directory");
		JMenuItem setIFSDirectoryItem = new JMenuItem("Save Current IFS Directory");
		JMenuItem refreshItem = new JMenuItem("Refresh");
		JMenuItem resetItem = new JMenuItem("Reset Defaluts");
		JMenuItem exitItem = new JMenuItem("Exit");
		
		fileMenu.add(getItem);
		fileMenu.add(getDirectoryItem);
		// fileMenu.add(setIFSDirectoryItem);
		fileMenu.add(refreshItem);
		fileMenu.addSeparator();
		fileMenu.add(resetItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		menuBar.add(fileMenu);

		fileMenu.setMnemonic(KeyEvent.VK_F);
		exitItem.setMnemonic(KeyEvent.VK_X);
		
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		
		getItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkFormPanelFields(true, false, 0, null);
			}
		});
		
		getDirectoryItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					File value = fileChooser.getSelectedFile();
					prefs.put("currentLocalDirectory", value.getPath());
				};
			}
		});
		
		setIFSDirectoryItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentIFSDirectory = formPanel.getDirectoryField();
				prefs.put("currentIFSDirectory", currentIFSDirectory);
			}
		});
		
		refreshItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				formPanel.clearForm();
			}
		});

		resetItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					prefs.clear();
				} catch (BackingStoreException e) {
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(formPanel, "Defaults reset, program needs to restart.", 
						"Reset Complete", JOptionPane.INFORMATION_MESSAGE);
				as400.disconnectAllServices();
				dispose();
				System.gc();
			}
		});
		
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CloseProgram("Exit Menu");
			}
		});
		
		return menuBar;
	}
	
	private void getAllXMLFiles(String directory, boolean copyDir, int row, String dir) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressDialog.setVisible(true);
			}
		});
		
		SwingWorker<List<String>, Integer> worker = new SwingWorker<List<String>, Integer>() {
			
			int count;
			
			protected void done() {
				setCursor(Cursor.getDefaultCursor());

				progressDialog.setVisible(false);
				
				statusBar.setLabel(count + " files and directories found.");
				
				if(isCancelled()) return;
			}

			@Override
			protected void process(List<Integer> counts) {
				// int retrieved = counts.size() - 1;
			}

			@Override
			protected List<String> doInBackground() throws Exception {
				
				String[] txnNames;
				
				count = 0;
				List<String> txnList = new ArrayList<String>();
				
				String currentIFSLocation = "/" + directory;
				prefs.put("currentIFSDirectory", currentIFSLocation);
				IFSFile ifsFile = new IFSFile(as400, currentIFSLocation);
				
				if(ifsFile.exists() == false) {
					JOptionPane.showMessageDialog(formPanel, "The directory does not exist", "Retrieval Error", JOptionPane.ERROR_MESSAGE);
				}
				
				txnNames = ifsFile.list(new MyDirectoryFilter(),"*");
				
				tablePanel.refresh();

				txnList = Arrays.asList(txnNames);
				
				count = txnList.size();
				
				if (count == 0) {
					progressDialog.setVisible(false);
		        	JOptionPane.showMessageDialog(formPanel, "The directory is empty", "Retrieval Error", JOptionPane.ERROR_MESSAGE);
		        }
					
				publish(count);
				
				return txnList;
			}
		};
		
		worker.execute();
	}
	
	class MyDirectoryFilter implements IFSFileFilter {
		
		public boolean accept(IFSFile file) {
			
			try {
				// Get the creation date
				long createDate = file.created();
				Date d = new Date(createDate);
				String dateCreated = d.toString();
				int a = dateCreated.length();
				String year = dateCreated.substring(a-5);
				dateCreated = year + ' ' + dateCreated.substring(4, a-5);
				Long s = file.length();
				String size = null;
				
				if (file.isDirectory())
		            size = "<DIR>";
		         else
		            size = s.toString();
				 
				controller.addXMLFile(file.getName(), dateCreated, size);
				 
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}
	
	public void checkFormPanelFields(boolean upOrDown, boolean copyDir, int row, String dir) {
		
		String directory = formPanel.getDirectoryField();
		prefs.put("currentIFSDirectory", directory);
		if(upOrDown) {
			lastDir.push(directory);
		}
		if(directory.isEmpty()) {
			JOptionPane.showMessageDialog(formPanel, "Directory cannot remain blank", "Retrieval Error", JOptionPane.ERROR_MESSAGE);
			formPanel.setDirectoryFocus();
			return;
		}
		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		getAllXMLFiles(directory, copyDir, row, dir);
	}
	
	public boolean CopyFile(AS400 system, String source, String destination) {
		
	    File destFile = new File(destination);
	    IFSFile sourceFile = new IFSFile(system, source);
	    if (!destFile.exists()){
	        try {
	            destFile.createNewFile();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    // IFSFileInputStream in = null;
	    // OutputStream out = null;
	    try (IFSFileInputStream in = new IFSFileInputStream(sourceFile);
	    	OutputStream out = new FileOutputStream(destFile)) {
	    	
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > -1) {
                out.write(buf, 0, len);
            }
        } catch (AS400SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    return true;
	}
	
	public boolean DeleteFile(IFSFile sourceFile) {
		
		//absolute file name with path
        try {
			if(sourceFile.delete()) {
			    return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}
	
	public void refreshScreen() {
		
		formPanel.clearForm();
		toolBar.copyButtonDisable();
		toolBar.deleteButtonDisable();
		Controller.clearFile();
		tablePanel.refresh();
		statusBar.setLabel("");
		lastDir.removeAllElements();
		
	}

	@SuppressWarnings("rawtypes")
	private void copyDirectory(String directory, int rowCount, int[]rows) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressDialogCopy.setMaximum(rowCount);
				progressDialogCopy.setValue(0);
				progressDialogCopy.setVisible(true);
			}
		});
		
		SwingWorker worker = new SwingWorker() {
			
			protected void done() {
				
				setCursor(Cursor.getDefaultCursor());
				progressDialogCopy.setVisible(false);
				toolBar.copyButtonDisable();
				tablePanel.refresh();
				statusBar.setLabel("Copy selected files(" + rowCount + ") to "
						+ directory + " completed normally.");
			}

			@SuppressWarnings("unused")
			protected void process() {
				
			}

			@SuppressWarnings("unchecked")
			@Override
			protected Object doInBackground() throws Exception {
				
				String currentIFSDirectory = prefs.get("currentIFSDirectory", "");
				String currentLocalDirectory = prefs.get("currentLocalDirectory", "");
				int completedRows = 0;
				for(int row = rowCount-1; row >= 0; row--) {
					completedRows++;
					String object = (String)tablePanel.getValueAt(row, 2);
					if(object.equals("<DIR>")) {
					} else {
						String lastSelectedXML = tablePanel.getValueAt(row, 0);
						String copyFromFile = currentIFSDirectory + "/" + lastSelectedXML;
						String copyToFile = currentLocalDirectory + "\\" + lastSelectedXML;
						if(CopyFile(as400, copyFromFile, copyToFile)) {
							
						} else {
							return null;
						}
					}
					progressDialogCopy.setValue(completedRows);
					Controller.removeFile(rows[row]);
					tablePanel.refresh();
				}
				
				publish(completedRows);
				
				return null;
			}
		};
		
		worker.execute();
	}

	@SuppressWarnings("rawtypes")
	private void clearDirectory(String directory, int rowCount) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressDialogDelete.setMaximum(rowCount);
				progressDialogDelete.setValue(0);
				progressDialogDelete.setVisible(true);
			}
		});
		
		SwingWorker worker = new SwingWorker() {
			
			int row = 0;
			
			protected void done() {
				
				setCursor(Cursor.getDefaultCursor());
				progressDialogDelete.setVisible(false);
				refreshScreen();
				checkFormPanelFields(true, true, rowCount, directory);
				toolBar.copyButtonDisable();
				toolBar.deleteButtonDisable();
	
			}
	
			@SuppressWarnings("unused")
			protected void process() {
				
			}
	
			@SuppressWarnings("unchecked")
			@Override
			protected Object doInBackground() throws Exception {
				
				for(row = 0; row < rowCount; row++) {
					String object = (String)tablePanel.getValueAt(row, 2);
					if(object.equals("<DIR>")) {
					} else {
						String value = (String)tablePanel.getValueAt(row, 0);
						String deleteFile = directory + "/" + value;
						if(DeleteFile(new IFSFile(as400, deleteFile))) {
						} else {
							return null;
						}
					}
					progressDialogDelete.setValue(row);
				}
	
				publish(row);
				
				return null;
			}
		};
		
		worker.execute();
	}
}