package model;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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