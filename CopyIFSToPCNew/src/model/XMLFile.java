package model;
public class XMLFile {
	
	private String name;
	private String date;
	private String size;
	
	public XMLFile (String name, String date, String size) {
		
		this.name = name;
		this.date = date;
		this.size = size;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}	
}