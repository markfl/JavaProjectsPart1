package gui;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.IFSFile;

import controller.Controller;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Preferences prefs;
	private AS400 as400;
	private FormPanel formPanel;
	private StatusBar statusBar;
	private Controller controller;
	private SimpleDateFormat mDCYSDF;
	private SimpleDateFormat cYMDSDF;
	private ProgressDialog progressDialog;
	
	public MainFrame() {
		
		super("Create Returns/Refunds From Beanstore");
		
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
		
		prefs = Preferences.userRoot().node("Returns/Refunds");
		String systemName = prefs.get("system", "");
		String user = prefs.get("user", "");
		
		
		int left = prefs.getInt("left", 0);
		int top = prefs.getInt("top", 0);
		int width = prefs.getInt("width", 600);
		int height = prefs.getInt("height", 600);
		setBounds(left, top, width, height);
		
		mDCYSDF = new SimpleDateFormat("MMM dd, yyyy");
		cYMDSDF = new SimpleDateFormat("yyyyMMdd");
		
		setLayout(new BorderLayout());
		
		if(systemName.isEmpty()) {
			as400 = new AS400();
		} else {
			as400 = new AS400(systemName, user);
		}
		try {
			as400.connectService(AS400.FILE);
			
		} catch (AS400SecurityException e) {
			CloseProgram("Can't Connect to AS400 Exit Exception");
			System.out.println("Exit Exception");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		formPanel = new FormPanel(as400);
		statusBar = new StatusBar();
		controller = new Controller();

		this.setTitle("Create Returns/Refunds From Beanstore On " + systemName);
		
		progressDialog = new ProgressDialog(this, "Building Returns/Refunds for Store");
		
		prefs.put("system", as400.getSystemName());
		prefs.put("user", as400.getUserId());
		
		try {
			controller.connect();
		} catch (Exception e) {
		}
		
		formPanel.setFormListener(new FormListener() {
			public void formEventOccurred(FormEvent ev) {
				String store = ev.getStore();
				String startDate = ev.getStartDate();
				String endDate = ev.getEndDate();
				String weekNumber = ev.getWeekNumber();
				boolean refunds = ev.isRefunds();
				boolean returns = ev.isReturns();
				String saveToPath = ev.getSaveToPath();
				prefs.put("currentIFSDirectory", saveToPath);
				
				try {
					Date workDate = mDCYSDF.parse(startDate);
					startDate = cYMDSDF.format(workDate);
					workDate = mDCYSDF.parse(endDate);
					endDate = cYMDSDF.format(workDate);
				} catch (ParseException e) {
					// Send error message
					e.getMessage();
				}
				
				if(validateFields(store, startDate, endDate, weekNumber, refunds, returns, saveToPath)) {
					
					DecimalFormat decimalFormat = new DecimalFormat("00000");
					int storeNumber = Integer.parseInt(store);
					String storeFormated = decimalFormat.format(storeNumber);
					
					decimalFormat = new DecimalFormat("00");
					int week = Integer.parseInt(weekNumber);
					String weekFormated = decimalFormat.format(week);
					
					String test = saveToPath.substring(saveToPath.length() - 1);
					if(!test.equals("/")) {
						saveToPath = saveToPath + "/";
						formPanel.setSaveToPathField(saveToPath);
					}
					
					if(returns || refunds) {
						buildReturnsRefunds(as400, storeFormated, startDate, endDate, weekFormated, refunds, returns, saveToPath);
					}
				}
			}

			public void exitEventOccured() {
				CloseProgram("Exit Button Pressed");
			}
		});
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				System.out.println("Window Closing");
				controller.disconnect();
				CloseProgram("Job ended normally.");
				dispose();
				System.gc();
			}
		});
		
		setJMenuBar(createMenuBar());
		
		add(formPanel, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void CloseProgram(String where) {
		System.out.println(where);
		String saveToPath = formPanel.getSaveToPathField();
		prefs.put("currentIFSDirectory", saveToPath);
		System.out.println(saveToPath);
		prefs.putInt("left", getX());
		prefs.putInt("top", getY());
		prefs.putInt("width", getWidth());
		prefs.putInt("height", getHeight());
		as400.disconnectAllServices();
		dispose();
		System.gc();
		System.exit(0);
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem refreshItem = new JMenuItem("Refresh");
		JMenuItem resetItem = new JMenuItem("Reset Defaluts");
		JMenuItem exitItem = new JMenuItem("Exit");
		
		fileMenu.add(refreshItem);
		fileMenu.addSeparator();
		fileMenu.add(resetItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		menuBar.add(fileMenu);

		fileMenu.setMnemonic(KeyEvent.VK_F);
		exitItem.setMnemonic(KeyEvent.VK_X);
		
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));

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
	
	private Boolean validateFields(String store, String startDate, String endDate, String weekNumber,
			boolean refunds, boolean returns, String saveToPath) {
		
		if(store.isEmpty()) {
			JOptionPane.showMessageDialog(formPanel, "Store must not be blank", "Entry Error", JOptionPane.ERROR_MESSAGE);
        	return false;
		} else {
			if(!store.equals("99999")) {
				
				
			}
		}

		if(startDate.isEmpty()) {
			JOptionPane.showMessageDialog(formPanel, "Start Date must not be blank", "Entry Error", JOptionPane.ERROR_MESSAGE);
        	return false;
		}
		
		if(endDate.isEmpty()) {
			JOptionPane.showMessageDialog(formPanel, "End Date must not be blank", "Entry Error", JOptionPane.ERROR_MESSAGE);
        	return false;
		} else {
			int sdate = Integer.parseInt(startDate);
			int edate = Integer.parseInt(endDate);
			if(sdate > edate) {
				JOptionPane.showMessageDialog(formPanel, "From Date must not be greater then To Date", "Entry Error", JOptionPane.ERROR_MESSAGE);
	        	return false;
			}
		}
		
		if(weekNumber.isEmpty()) {
			JOptionPane.showMessageDialog(formPanel, "Week Number must not be blank", "Entry Error", JOptionPane.ERROR_MESSAGE);
			formPanel.setWeekNumberFieldFocus();
        	return false;
		} else {
			try {
				int week = Integer.parseInt(weekNumber);
				if((week < 1) || (week > 52)) {
					JOptionPane.showMessageDialog(formPanel, "Invalid Week Number entered", "Entry Error", JOptionPane.ERROR_MESSAGE);
		        	return false;
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(formPanel, "Invalid Week Number entered", "Entry Error", JOptionPane.ERROR_MESSAGE);
	        	return false;
			}
		}
		
		if((!refunds) && (!returns)) {
			JOptionPane.showMessageDialog(formPanel, "Refunds and/or Returns must be checked", "Entry Error", JOptionPane.ERROR_MESSAGE);
			formPanel.setReturnsCheckFocus();
        	return false;
		}
		
		if(saveToPath.isEmpty()) {
			JOptionPane.showMessageDialog(formPanel, "Save To Path must not be blank", "Entry Error", JOptionPane.ERROR_MESSAGE);
			formPanel.setSaveToPathFieldFocus();
        	return false;
		} else {
			IFSFile ifsFile = new IFSFile(as400, saveToPath);
			try {
				if(ifsFile.exists() == false) {
					JOptionPane.showMessageDialog(formPanel, "The directory does not exist", "Retrieval Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (HeadlessException | IOException e) {
				
			}
		}
		
		return true;
	}
	
	public void buildReturnsRefunds(AS400 system, String store, String fromDate, String toDate,
									String week, Boolean refunds, Boolean returns, String fileLocation) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				progressDialog.setVisible(true);
			}
		});
		
		
		
		@SuppressWarnings("rawtypes")
		SwingWorker worker = new SwingWorker() {
			
			protected void done() {
				setCursor(Cursor.getDefaultCursor());

				progressDialog.setVisible(false);
				
				String statusBarText = null;
				
				if((returns) && (!refunds)) {
					int returnsCount = controller.getReturnsCount();
					statusBarText = returnsCount + " returns records found.";
				} else if((!returns) && (refunds)) {
					int refundsCount = controller.getRefundsCount();
					statusBarText = refundsCount + " refunds records found.";
				} else if((returns) && (refunds)) {
					int returnsCount = controller.getReturnsCount();
					int refundsCount = controller.getRefundsCount();
					statusBarText = returnsCount + " returns records and " + refundsCount + " refunds records found.";
				}
				statusBar.setLabel(statusBarText);
				
			}

			@Override
			protected List<String> doInBackground() throws Exception {
				
				if(returns) {
					if (store.equals("99999")) {
						progressDialog.setTitle("Building Returns for All Stores");
					} else {
						int storeInt = Integer.parseInt(store);
						String storeString = Integer.toString(storeInt);
						progressDialog.setTitle("Building Returns for Store " + storeString);
					}
					controller.returns(as400, store, fromDate, toDate, week, fileLocation);
				}
				
				if(refunds) {
					if (store.equals("99999")) {
						progressDialog.setTitle("Building Refunds for All Stores");
					} else {
						int storeInt = Integer.parseInt(store);
						String storeString = Integer.toString(storeInt);
						progressDialog.setTitle("Building Refunds for Store " + storeString);
					}
					controller.refunds(as400, store, fromDate, toDate, week, fileLocation);
				}
				
				return null;
				
			}
		};
		
		worker.execute();
		
	}
}