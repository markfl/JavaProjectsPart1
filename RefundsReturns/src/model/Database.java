package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileWriter;

public class Database {
	
	private Connection con;
	private IFSFile returnsFile;
	private IFSFile refundsFile;
		
	public Database() {
		// progressDialog = new ProgressDialog(this, "Getting All Files & Directories...");
	}
	
	public void connect() {
			
		if (con != null) return;
		
			try {
				String output[] = parseXMLConfig();
				Class.forName("com.ibm.db2.jcc.DB2Driver");
				String URL = output[0];
				System.out.println("Database driver found. ");
				con = DriverManager.getConnection(URL, output[1], output[2]);
				System.out.println("Database connected: " + con);
			} catch (ClassNotFoundException e) {
				System.out.println("Database driver not found. ");
			} catch (SQLException e) {
				System.out.println("Could not connedt to database. ");
			}
		}
	
	public void disconnect() {
		if(con != null) {
			try {
				con.close();
				System.out.println("Database disconnected.");
			} catch (SQLException e) {
				System.out.println("Database can't disconnected.");
			}
		}
	}

	public int loadReturns(AS400 system, String inStore, String fromDate, String toDate, String week, String destination) throws SQLException {
		
		String store = inStore.substring(1);
		String toFileReturns = destination + "ReturnsWeek";
		toFileReturns = toFileReturns + week + ".csv";
		
		String sql = new String();
		
		String sql1 = "Select header.TXHD_TRADING_DATE," 
					+ "header.orgu_code," 
					+ "Sum(detail.txde_price_sold) " 
					+ "From txn_header header, TXN_DETAIL detail Where ";
		String sql2 = "Header.orgu_code = '";
		String sql3 = "' And ";
		String sql4 = "Header.TXHD_TRADING_DATE >= '";
		String sql5 = "' And Header.TXHD_TRADING_DATE <= '";
		String sql6 = "'And header.txhd_voided = 0 " 
					+ "And header.TXHD_ORIG_TXN_NR is null " 
					+ "And header.till_short_desc = detail.till_short_desc " 
					+ "And header.orgu_code = detail.orgu_code " 
					+ "And header.orgu_code_cmpy = detail.orgu_code_cmpy " 
					+ "And header.txhd_txn_nr = detail.txhd_txn_nr " 
					+ "And detail.txde_line_refund = 1 "
					+ "And detail.txde_item_void = 0 " 
					+ "Group By header.TXHD_TRADING_DATE,header.orgu_code";
		
		if(store.equals("9999")) {
			sql = sql1 + sql4 + fromDate + sql5 + toDate + sql6;
		} else {
			sql = sql1 + sql2 + store + sql3 + sql4 + fromDate + sql5 + toDate + sql6;
		}

		System.out.println("Reading/Writing Returns.");
		PreparedStatement selectStmt = con.prepareStatement(sql);
		ResultSet results = selectStmt.executeQuery();
		
		returnsFile = new IFSFile(system, toFileReturns);
		PrintWriter writer = null;
		try {
			if (!returnsFile.exists()) {
				returnsFile.createNewFile();
			  } else {
				  returnsFile.delete();
				  returnsFile.createNewFile();
			  }
			writer = new PrintWriter(new BufferedWriter(new IFSFileWriter(returnsFile)));
			String info = "Date,Store,Amount"; 
			writer.println(info);

		} catch (AS400SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int count = 0;
		
		while(results.next()) {
			count++;
			String resDate = results.getString(1);
			String resStore = results.getString(2);
			String resAmount = results.getString(3);
			String info = resDate + "," + resStore + "," + resAmount;
			writer.println(info);
		}
		System.out.println("Returns complete. " + count);
		// Close the file.
		writer.close();
		selectStmt.close();
		
		return count;
	}
	
	public int loadRefunds(AS400 system, String inStore, String fromDate, String toDate, String week, String destination) throws SQLException {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//progressDialog.setMaximum(9999);
				//progressDialog.setValue(0);
				//progressDialog.setVisible(true);
			}
		});
		
		String store = inStore.substring(1);
		String toFileRefunds = destination + "RefundsWeek";
		toFileRefunds = toFileRefunds + week + ".csv";
		
		String sql1 = "Select origHeader.TXHD_TRADING_DATE, "
				+ "origHeader.orgu_code, "
				+ "origHeader.till_short_desc, "
				+ "origHeader.TXHD_TXN_NR, "
				+ "refundHeader.TXHD_TRADING_DATE, "
				+ "refundHeader.orgu_code, "
				+ "refundHeader.till_short_desc, "
				+ "refundHeader.TXHD_TXN_NR, "
				+ "origHeader.txhd_value_due, "
				+ "refundHeader.txhd_value_due, "
				+ "refund.txdr_price_refund, "
				+ "detail.txde_sku_code, "
				+ "detail.txde_dept_code "
				+ "From TXN_DETAIL_REFUND as refund, "
				+ "TXN_HEADER as origHeader, "
				+ "TXN_HEADER as refundHeader, "
				+ "txn_detail as detail "
				+ "Where ";
		String sql2 = "origHeader.orgu_code = '";
		String sql3 = "' And ";
		String sql4 = "refundHeader.TXHD_TRADING_DATE >= '";
		String sql5 = "' And refundHeader.TXHD_TRADING_DATE <= '";
		String sql6 = "' And origHeader.TXHD_TXN_NR = refund.TXHD_TXN_NR "
				+ "And origHeader.orgu_code_cmpy = refund.orgu_code_cmpy "
				+ "And origHeader.orgu_code = refund.orgu_code "
				+ "And origHeader.till_short_desc = refund.till_short_desc "
				+ "And refundHeader.TXHD_TXN_NR = refund.TXHD_TXN_REFUND "
				+ "And refundHeader.orgu_code_cmpy = refund.cmpy_code_refund "
				+ "And refundHeader.orgu_code = refund.orgu_code_refund "
				+ "And refundHeader.till_short_desc = refund.till_desc_refund "
				+ "And detail.TXHD_TXN_NR = refund.TXHD_TXN_REFUND "
				+ "And detail.orgu_code_cmpy = refund.cmpy_code_refund "
				+ "And detail.orgu_code = refund.orgu_code_refund "
				+ "And detail.till_short_desc = refund.till_desc_refund "
				+ "And detail.txde_detail_nr = refund.txde_detl_refund "
				+ "And detail.txde_item_void = 0 "
				+ "And refund.txdr_voided = 0 "
				+ "And refundHeader.txhd_voided = 0 "
				+ "Order by refundHeader.TXHD_TRADING_DATE, refundHeader.orgu_code";
		
		String sql = null;
		
		if(store.equals("9999")) {
			sql = sql1 + sql4 + fromDate + sql5 + toDate + sql6;
		} else {
			sql = sql1 + sql2 + store + sql3 + sql4 + fromDate + sql5 + toDate + sql6;
		}
		
		System.out.println("Reading/Writing Refunds.");
		PreparedStatement selectStmt = con.prepareStatement(sql);
		ResultSet results = selectStmt.executeQuery();
		
		refundsFile = new IFSFile(system, toFileRefunds);
		PrintWriter writer = null;
		try {
			if (!refundsFile.exists()) {
				refundsFile.createNewFile();
			  } else {
				  refundsFile.delete();
				  refundsFile.createNewFile();
			  }
			writer = new PrintWriter(new BufferedWriter(new IFSFileWriter(refundsFile)));
			String info = "Orig Date,Orig Store,Orig Till,Orig Trans#,Refund Date,Refund Store,Refund Till,Refund Trans#,Orig Amount,Refund Due,Refund Amount,Sku,Dept"; 
			writer.println(info);

		} catch (AS400SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int count = 0;
		while(results.next()) {
			count++;
			String resDate = results.getString(1);
			String origStore = results.getString(2);
			String origTill = results.getString(3);
			String origTXNNR = results.getString(4);
			String tradeDate = results.getString(5);
			String refundStore = results.getString(6);
			String refundTill = results.getString(7);
			String tranNR = results.getString(8);
			String origValueDue = results.getString(9);
			String refundValueDue = results.getString(10);
			String refundPrice = results.getString(11);
			String SKU = results.getString(12);
			String department = results.getString(13);
			
			String info = resDate + "," + origStore + "," 
			+ origTill + "," + origTXNNR + "," + tradeDate 
			+ "," + refundStore + "," + refundTill + "," 
			+ tranNR + "," + origValueDue + "," + refundValueDue + "," 
			+ refundPrice + "," + SKU + "," + department;
			writer.println(info);
		}
		
		System.out.println("Refunds complete. " + count);
		
		// Close the file.
		writer.close();
		selectStmt.close();
		
		return count;
	}
	
	public String[] parseXMLConfig() {
		String output[] = new String[3];
		try {
			 File inputFile = new File("config/RefundsReturns.xml");
			 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			 Document doc = dBuilder.parse(inputFile);
			 doc.getDocumentElement().normalize();
			 NodeList nList = doc.getElementsByTagName("system_parameters_unix");
			 Node nNode = nList.item(0);

			 if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				   Element eElement = (Element) nNode;
				   output[0] = eElement.getElementsByTagName("url").item(0).getTextContent();
				   output[1] = eElement.getElementsByTagName("userid").item(0).getTextContent();
				   output[2] = eElement.getElementsByTagName("password").item(0).getTextContent();
			 }
			} catch (Exception e) {
		          e.printStackTrace();
          }
		return output;
	}
}