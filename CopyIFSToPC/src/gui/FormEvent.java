package gui;
import java.util.EventObject;

public class FormEvent extends EventObject{

	private static final long serialVersionUID = 1L;
	private String directory;
	
	public FormEvent(Object source) {	
		super(source);
	}
	
	public FormEvent(Object source, String directory) {	
		super(source);
		
		this.directory = directory;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
}