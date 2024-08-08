package gui;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.SequentialFile;

public class FormPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private JLabel storeLabel;
	private JLabel startDateLabel;
	private JLabel endDateLabel;
	private JLabel weekNumberLabel;
	private JLabel refundsLabel;
	private JLabel returnsLabel;
	private JLabel saveToPathLabel;
	private JComboBox<String> storeField;
	private JDatePickerImpl startDateField;
	private JDatePickerImpl endDateField;
	private JTextField weekNumberField;
	private JCheckBox refundsCheck;
	private JCheckBox returnsCheck;
	private JTextField saveToPathField;
	private JButton okBtn;
	private JButton exitBtn;
	private Preferences prefs;
	private SimpleDateFormat cYMDSDF;
	
	private FormListener formListener;
	
	public FormPanel(AS400 as400) {
		
		Dimension dim = getPreferredSize();
		dim.width = 250;
		dim.height = 250;
		setPreferredSize(dim);
		setMinimumSize(dim);

		// ymdSDF = new SimpleDateFormat("yymmdd");
		cYMDSDF = new SimpleDateFormat("yyyyMMdd");
		
		storeLabel = new JLabel("Store:");
		startDateLabel = new JLabel("Start Date:");
		endDateLabel = new JLabel("End Date: ");
		weekNumberLabel = new JLabel("Week Number: ");
		refundsLabel = new JLabel("Refunds:");
		returnsLabel = new JLabel("Returns:");
		saveToPathLabel = new JLabel("Save To Path:");
		
		startDateField = generateDatePicker(true);
		endDateField = generateDatePicker(false);
		weekNumberField = new JTextField(2);
		refundsCheck = new JCheckBox();
		returnsCheck = new JCheckBox();
		saveToPathField = new JTextField(40);
		okBtn = new JButton("OK");
		exitBtn = new JButton("Exit");
		exitBtn.addActionListener(this);
		
		// setup Store list
		storeField = new JComboBox<String>();
		buildStoreField(as400);
		
		prefs = Preferences.userRoot().node("Returns/Refunds");
		
		String currentIFSDirectory = prefs.get("currentIFSDirectory", "");
		saveToPathField.setText(currentIFSDirectory);
		
		// Set up mnemomics
		okBtn.setMnemonic(KeyEvent.VK_O);
		exitBtn.setMnemonic(KeyEvent.VK_X);
		
		storeLabel.setDisplayedMnemonic(KeyEvent.VK_S);
		storeLabel.setLabelFor(storeField);
		startDateLabel.setDisplayedMnemonic(KeyEvent.VK_F);
		startDateLabel.setLabelFor(startDateField);
		endDateLabel.setDisplayedMnemonic(KeyEvent.VK_T);
		endDateLabel.setLabelFor(endDateField);
		weekNumberLabel.setDisplayedMnemonic(KeyEvent.VK_W);
		weekNumberLabel.setLabelFor(weekNumberField);
		saveToPathLabel.setDisplayedMnemonic(KeyEvent.VK_P);
		saveToPathLabel.setLabelFor(saveToPathField);
		
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String store = (String)storeField.getSelectedItem();
				String startDate = startDateField.getJFormattedTextField().getText();
				String endDate = endDateField.getJFormattedTextField().getText();
				String weekNumber = weekNumberField.getText();
				boolean refunds = refundsCheck.isSelected();
				boolean returns = returnsCheck.isSelected();
				String saveToPath = saveToPathField.getText();
				FormEvent ev = new FormEvent(this, store, startDate, endDate, weekNumber, refunds, returns, saveToPath);
				
				if(formListener != null) {
					formListener.formEventOccurred(ev);
				}
			}
		});
		
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		Border innerBorder = BorderFactory.createTitledBorder("Create Returns/Refunds");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		
		dim = exitBtn.getPreferredSize();
		okBtn.setPreferredSize(dim);
		
		layoutComponents();
	}
	
	public void layoutComponents() {
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.NONE;
		
		gc.weightx = 1;
		gc.weighty = 0.1;
		
		// First Row
		gc.gridx = 0;
		gc.gridy = 0;
		
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(storeLabel, gc);
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(storeField, gc);

		// Second Row
		gc.gridx = 0;
		gc.gridy++;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(startDateLabel, gc);
		
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(startDateField, gc);
		
		// Second Row
		gc.gridx = 0;
		gc.gridy++;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(endDateLabel, gc);
		
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(endDateField, gc);

		// next Row
		gc.gridy++;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(weekNumberLabel, gc);
		
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(weekNumberField, gc);
		
		// Next Row
		gc.gridy++;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(returnsLabel, gc);
		
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(returnsCheck, gc);

		// Next Row
		gc.gridy++;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(refundsLabel, gc);
		
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(refundsCheck, gc);
		
		// Next Row
		gc.gridy++;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(saveToPathLabel, gc);
		
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(saveToPathField, gc);
		
		// Next Row
		gc.gridx = 0;
		gc.gridy++;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(0, 0, 0, 0);
		add(okBtn, gc);
		
		gc.weightx = 1;
		gc.weighty = 2;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(exitBtn, gc);
	}
	
	public void setFormListener(FormListener listener) {
		this.formListener = listener;
	}
	
	public JDatePickerImpl generateDatePicker(Boolean beginningOfMonth) {
		
		JDatePickerImpl datePicker;
		
		UtilDateModel model;
		JDatePanelImpl datePanel;
		
		Calendar currentDate = Calendar.getInstance();
		int sYear = currentDate.get(Calendar.YEAR);
		int sMonth = currentDate.get(Calendar.MONTH);
		int sDay = currentDate.get(Calendar.DAY_OF_MONTH);
		
		if(beginningOfMonth) sDay = sDay - 7;
		
		Properties prop = new Properties();
		prop.put("text.today", "today");
		prop.put("text.nonth", "month");
		prop.put("text.year", "year");
		
		model = new UtilDateModel();
		model.setDate(sYear, sMonth, sDay);
		model.setSelected(true);
		datePanel = new JDatePanelImpl(model, prop);
		datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
		
		return datePicker;
	}

	private void buildStoreField(AS400 as400) {
		
		// setup Store list
		DefaultComboBoxModel<String> storeModel = new DefaultComboBoxModel<String>();
		storeModel.addElement("99999");
		storeField.setModel(storeModel);
		SequentialFile tblstrFile = new SequentialFile(as400, "/QSYS.LIB/MM4R6LIB.LIB/TBLSTR.FILE");
		try {
			tblstrFile.setRecordFormat();
			tblstrFile.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE );
			Record record = tblstrFile.read(1);
			Object field = record.getField(0);
			String strnum = field.toString();
			storeModel.addElement(strnum);
			Boolean readInd = false;
			Date currentDate = new Date();
			String currentCDate = cYMDSDF.format(currentDate);
			int currentIntdate = Integer.parseInt(currentCDate);
			do {
				record = tblstrFile.readNext();
				if(record != null) {
					field = record.getField(20);
					String strhdo = field.toString();
					if(strhdo.equals("S")) {
						field = record.getField(42);
						String century = field.toString();
						field = record.getField(43);
						String closedDate = field.toString();
						Boolean selectRecord = false;
						if(!closedDate.equals("0")) {
							DecimalFormat decimalFormat = new DecimalFormat("000000");
							int closedIntDate = Integer.parseInt(closedDate);
							String closedFmtDate = decimalFormat.format(closedIntDate);
							if(century.equals("1")) {
								closedIntDate = Integer.parseInt("20" + closedFmtDate);
							} else {
								closedIntDate = Integer.parseInt("19" + closedFmtDate);
							}
							if(currentIntdate < closedIntDate) {
								selectRecord = true;
							}
						} else {
							selectRecord = true;
						}
						
						if(selectRecord) {
							field = record.getField(0);
							strnum = field.toString();
							storeModel.addElement(strnum);
						}
					}
				} else {
					readInd = true;
				}
				
			} while(!readInd);
			tblstrFile.close();
		} catch (AS400Exception | AS400SecurityException | InterruptedException | IOException
				| PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	public void setWeekNumberField(String weekNumber) {
		weekNumberField.setText(weekNumber);
	}
	
	public String getSaveToPathField() {
		return saveToPathField.getText();
	}

	public void setSaveToPathField(String saveToPath) {
		saveToPathField.setText(saveToPath);
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton clicked = (JButton)e.getSource();
		
		if(clicked == exitBtn) {
			formListener.exitEventOccured();
		}	
	}
	public void setWeekNumberFieldFocus() {
		weekNumberField.requestFocus();
	}

	public void setRefundsCheckFocus() {
		refundsCheck.requestFocus();
	}

	public void setReturnsCheckFocus() {
		returnsCheck.requestFocus();
	}

	public void setSaveToPathFieldFocus() {
		saveToPathField.requestFocus();
	}

	public void clearForm() {
		startDateField = generateDatePicker(true);
		endDateField = generateDatePicker(false);
		setWeekNumberField("");
		refundsCheck.setSelected(false);
		returnsCheck.setSelected(false);
		setSaveToPathField("");
	}
}