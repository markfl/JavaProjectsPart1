import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class FixMySQLlength {
	
	public static void main(String[] args) {
		
		StringBuilder text = new StringBuilder();
		
		try (BufferedReader in = new BufferedReader(new 
					InputStreamReader(new FileInputStream("F:\\Users Shared Folders\\markfl\\Documents\\My Development\\My SQL Source\\hubgroup\\createtmmy.old.sql"), "UTF-8"))) {
			String line;
			while ((line  = in.readLine()) != null ) {
				int a = line.indexOf("char(");
				if (a > 0) {
					int b = line.indexOf(")");
					if (a > 0) {
						int c = Integer.parseInt(line.substring(a + 5, b));
						if (c > 255) {
							line = line.replace("char(", "text(");
						}
					}
				}
				
				
				line = line + "\n";
				text.append(line);	
			}
			try (FileOutputStream out = new FileOutputStream(new File("F:\\Users Shared Folders\\markfl\\Documents\\My Development\\My SQL Source\\hubgroup\\createtmmy.sql"))) {
				out.write(text.toString().getBytes());
			}
			System.out.println("Program completed normally.");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
