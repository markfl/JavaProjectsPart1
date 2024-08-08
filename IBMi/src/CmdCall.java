import java.beans.PropertyVetoException;
import java.io.IOException;

import com.ibm.as400.access.*;

import model.IBMi;

public class CmdCall {

	public static void main(String[] args) {

		String company = args[0];
		String session = args[1];
		
		IBMi IBMi = new IBMi(company);
		
		// AS400 system = new AS400(output[0], output[1], output[2]);
		CommandCall cc = new CommandCall(IBMi.getAS400());
		
		try {
			cc.run("SNDBRKMSG MSG(TEST_BREAK) TOMSGQ(" + session.trim() + ")");
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
		if (ml.length == 0)
			System.out.println("Comand executed properly.");
		for (int i=0; i<ml.length; i++)
		{
			System.out.println(ml[i].getText());
		}
	
		System.exit(0);
	}
}