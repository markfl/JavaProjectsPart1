
import javax.swing.SwingUtilities;

import gui.MainFrame;

public class App {
	
	public static void main(String[] args) {
		
		String company = args[0];
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainFrame(company);
			}
		});
	}
}