package model;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Database {
	
	private List<XMLFile> files;
	
	public Database() {
		files = new LinkedList<XMLFile>();
	}
	
	public void addXMLFile(XMLFile xmlFile) {
		files.add(xmlFile);
	}
	
	public void removeFile(int index) {
		files.remove(index);
	}
	
	public void clearFile() {
		files.clear();
	}
	
	public List<XMLFile> getFiles() {
		return Collections.unmodifiableList(files);
	}
}