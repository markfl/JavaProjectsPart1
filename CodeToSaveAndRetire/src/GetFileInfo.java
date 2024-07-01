

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class GetFileInfo {
	
	private String FileName;
	Connection conn;
	private int numberOfFields;
	private int numberOfKeyFields;
	private Collection<ArrayList<String>> allFields;
	private String[] allKeyFields = {};
	
	public String getFileName() {
		return FileName;
	}
	
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	
	public Connection getConn() {
		return conn;
	}
	
	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public int getNumberOfFields() {
		return numberOfFields;
	}

	public void setNumberOfFields(int numberOfFields) {
		this.numberOfFields = numberOfFields;
	}

	public int getNumberOfKeyFields() {
		return numberOfKeyFields;
	}
	
	public void setNumberOfKeyFields(int numberOfKeyFields) {
		this.numberOfKeyFields = numberOfKeyFields;
	}

	public Collection<ArrayList<String>> getAllFields() {
		return allFields;
	}

	public void setAllFields(Collection<ArrayList<String>> allFields) {
		this.allFields = allFields;
	}
	
	public String[] getAllKeyFields() {
		return allKeyFields;
	}

	public void setAllKeyFields(String[] allKeyFields) {
		this.allKeyFields = allKeyFields;
	}

/*	public void getFileFieldData(ResultSet resultsSelect) {
		
		Collection<ArrayList<String>> fields = new ArrayList<ArrayList<String>>();
		
		String charType = "char";
		String varCharType = "varchar";
		String numericType = "numeric";
		String[] keyFields = {};
		
		boolean firstRecord = true;
		boolean keyFieldInd = false;
		
		try {
			while (resultsSelect.next()) {
				setFileName(resultsSelect.getString(1));
				String fieldName = resultsSelect.getString(2);
				int fieldOrder = resultsSelect.getInt(3);
				String fieldTypeTest = resultsSelect.getString(4);
				int fieldSize = 0;
				int decimal = 0;
				String fieldType = new String();
				if (fieldTypeTest.equals(charType) || fieldTypeTest.equals(varCharType)) {
					fieldType = "String";
					fieldSize = resultsSelect.getInt(5);
				} else if (fieldTypeTest.equals(numericType)) {
					fieldSize = resultsSelect.getInt(6);
					decimal = resultsSelect.getInt(7);
					if (decimal == 0) {
						fieldType = "int";
					} else {
						fieldType = "double";
					}
				}
				// check for first record read
				if (firstRecord) {
					keyFields = getFileIndexString();
					firstRecord = false;
				}
				Collection<String> fieldList = new ArrayList<String>();
				fieldList.add(fieldName);
				fieldList.add(fieldType);
				fieldList.add(Integer.toString(fieldSize));
				fieldList.add(Integer.toString(decimal));
				keyFieldInd = false;
				if (keyFields != null) {
					for(String key : keyFields) {
						String newKey = key.trim();
						if (newKey.equals(fieldName)) {
							keyFieldInd = true;
							numberOfKeyFields++;
						}
					}
				}
				String keyField = "0";
				if (keyFieldInd) {
					keyField = "1";
				}
				fieldList.add(keyField);
				fieldList.add("set" + fieldName);
				fieldList.add("get" + fieldName);
				fields.add((ArrayList<String>) fieldList);
				setNumberOfFields(fieldOrder);
			}
			setAllFields(fields);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/

	public Collection<ArrayList<String>> getFileFields(boolean firstRecord, ResultSet resultsSelect, Collection<ArrayList<String>> fields) {
		
		String charType = "char";
		String varCharType = "varchar";
		String numericType = "numeric";
		String[] keyFields = {};
		int nbrOfFields = 0;
		int nbrOfKeyFields = 0;
		
		String fieldName;
		try {
			fieldName = resultsSelect.getString(2);
	    	int fieldOrder = resultsSelect.getInt(3);
	    	String fieldTypeTest = resultsSelect.getString(4);
			int fieldSize = resultsSelect.getInt(5);
			int decimal = resultsSelect.getInt(6);
			String fieldType = new String();
	    	if (fieldTypeTest.equals(charType) || fieldTypeTest.equals(varCharType)) {
	    		fieldType = "String";
	    		fieldSize = resultsSelect.getInt(5);
	    	} else if (fieldTypeTest.equals(numericType)) {
	    		fieldSize = resultsSelect.getInt(6);
				decimal = resultsSelect.getInt(7);
	    		if (decimal == 0) {
	    			fieldType = "int";
	    		} else {
	    			fieldType = "double";
	    		}
	    	}
	    	// check for first record read
			if (firstRecord) {
				getFileIndexString();
				firstRecord = false;
			}
			keyFields = getAllKeyFields();
			Collection<String> fieldList = new ArrayList<String>();
			fieldList.add(fieldName);
			fieldList.add(fieldType);
			fieldList.add(Integer.toString(fieldSize));
			fieldList.add(Integer.toString(decimal));
			boolean keyFieldInd = false;
			if (keyFields != null) {
				for(String key : keyFields) {
					String newKey = key.trim();
					if (newKey.equals(fieldName)) {
						keyFieldInd = true;
						break;
					}
				}
			}
			String keyField = "0";
			if (keyFieldInd) {
				keyField = "1";
			}
			if (keyField.equals("1")) {
				nbrOfKeyFields = getNumberOfKeyFields() + 1;
				setNumberOfKeyFields(nbrOfKeyFields);
			}
			fieldList.add(keyField);
			fieldList.add("set" + fieldName);
			fieldList.add("get" + fieldName);
			fields.add((ArrayList<String>) fieldList);
			nbrOfFields = fieldOrder;
			setNumberOfFields(nbrOfFields);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return fields;
	}
	
	public void getFileIndexString() {
		
		CallableStatement statement;
		try {
			statement = conn.prepareCall("{call sp_helpindex (?)}");
			statement.setString(1, FileName);
			boolean hadResults = statement.execute();
			if (hadResults) {
				ResultSet resultSet = statement.getResultSet();
			    // process result set
			    while (resultSet.next()) {
			    	String fileName = resultSet.getString(1);
			    	if (FileName.equals(fileName)) {
			    		String fields = resultSet.getString(3);
				    	String[] keyFields = fields.split(",");
				    	setAllKeyFields(keyFields);
			    	}
			    }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}