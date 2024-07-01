import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class BuildBat {
	
	static StringBuilder text = new StringBuilder();

	public static void main(String[] args) {
		try (BufferedReader in = new BufferedReader(new 
				InputStreamReader(new FileInputStream("C:\\Users Shared Folders\\markfl\\Documents\\My Development\\Eclipse\\Java EE\\BuildNPMInstallBat\\package.json"), "UTF-8"))) {
			String line;
			while ((line  = in.readLine()) != null ) {
				int a = line.indexOf("\"");
				int b = line.indexOf("\"", a+1);
				String fileName = line.substring(a+1, b);
				String lineout = "call npm i " + fileName + " --save";
				text.append(lineout + "\n");
		    }
			System.out.println("Program completed normally.");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (FileOutputStream out = new FileOutputStream(new File(
	    		"C:\\Users Shared Folders\\markfl\\Documents\\My Development\\Eclipse\\Java EE\\BuildNPMInstallBat\\package.bat"))) {
			out.write(text.toString().getBytes());
			text.setLength(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}