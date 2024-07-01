package controller;
import java.util.List;

import model.Database;
import model.XMLFile;

public class Controller {
	static Database db = new Database();
	
	public List<XMLFile> getFiles() {
		return db.getFiles();
	}
	
	public static void removeFile(int row) {
		db.removeFile(row);
	}

	public static void clearFile() {
		db.clearFile();
	} 
	
	public void addXMLFile(String name, String date, String size) {
		
		XMLFile xmlFile = new XMLFile(name, date, size);
		
		db.addXMLFile(xmlFile);
	}
}