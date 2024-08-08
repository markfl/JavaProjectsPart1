
import java.util.prefs.Preferences;

import javax.swing.SwingUtilities;

import gui.MainFrame;

public class App {
	
	public static void main(String[] args) {
		
		Preferences prefs = Preferences.userRoot().node("CopyIFSToPC");
		
		String company = new String();
		if (args.length > 0)
		   company = args[0];
		else
		   company = "westend";
		prefs.put("systemname", company);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainFrame();
			}
		});
	}
}