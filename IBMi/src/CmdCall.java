import java.beans.PropertyVetoException;
import java.io.IOException;

import com.ibm.as400.access.*;

public class CmdCall {

	public static void main(String[] args) {
		// Like other Java classes, IBM Toolbox for Java classes
		// throw exceptions when something goes wrong. These must
		// be caught by programs that use IBM Toolbox for Java.
		
			AS400 system = new AS400("as400.mandh.us", "mflores", "Kr@m7713");
			CommandCall cc = new CommandCall(system);
			try {
				cc.run("CRTLIB MYLIB");
			} catch (AS400SecurityException e) {
				e.printStackTrace();
			} catch (ErrorCompletingRequestException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
			AS400Message[] ml = cc.getMessageList();
			for (int i=0; i<ml.length; i++)
			{
				System.out.println(ml[i].getText());
			}
	
		System.exit(0);
	}
}