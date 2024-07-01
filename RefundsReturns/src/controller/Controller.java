package controller;

import java.sql.SQLException;

import com.ibm.as400.access.AS400;

import model.Database;

public class Controller {
	
	Database db = new Database();
	int returnsCount = 0;
	int refundsCount = 0;
	
	public void connect() throws Exception {
		db.connect();
	}
	
	public void disconnect() {
		db.disconnect();
	}
	
	public void returns(AS400 system, String store, String fromDate, String toDate, String week, String fileLocation) {
		try {
			returnsCount = db.loadReturns(system, store, fromDate, toDate, week, fileLocation);
		} catch (SQLException e) {
			e.getMessage();
		}
	}
	
	public void refunds(AS400 system, String store, String fromDate, String toDate, String week, String fileLocation) {
		try {
			refundsCount = db.loadRefunds(system, store, fromDate, toDate, week, fileLocation);
		} catch (SQLException e) {
			e.getMessage();
		}
	}

	public int getReturnsCount() {
		return returnsCount;
	}

	public int getRefundsCount() {
		return refundsCount;
	}
	
}