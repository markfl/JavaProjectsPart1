import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class DropIDXMySQL {

	public static void main(String[] args) {
		
		StringBuilder text = new StringBuilder();
		
		try (BufferedReader in = new BufferedReader(new 
					InputStreamReader(new FileInputStream("F:\\Users Shared Folders\\markfl\\Documents\\My Development\\My SQL Source\\JDA Database\\jdaixdrpmy.sql"), "UTF-8"))) {
			String line;
			while ((line  = in.readLine()) != null ) {
				int a = line.indexOf(";");
				String fullString = line.substring(0, a);
				String phyFileName = line.substring(11, 17);
				
				text.append(fullString + " on " + phyFileName + ";\n");
				System.out.print(fullString + "\n");
				
				try (FileOutputStream out = new FileOutputStream(new File("F:\\Users Shared Folders\\markfl\\Documents\\My Development\\My SQL Source\\JDA Database\\jdaixdrpmynew.sql"))) {
					out.write(text.toString().getBytes());
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}