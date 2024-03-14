import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Fix_Create_Table_MSSQL {
	
	public static void main(String[] args) {
		
		StringBuilder text = new StringBuilder();
		boolean firstTime = true;
		
		try (BufferedReader in = new BufferedReader(new 
					InputStreamReader(new FileInputStream("F:\\Users Shared Folders\\markfl\\Documents\\My Development\\My SQL Source\\HubGroup\\createtm.old.sql"), "UTF-8"))) {
			String line;
			while ((line  = in.readLine()) != null ) {
				
				if (firstTime) {
					firstTime = false;
					text.append("USE [hubgroup]\ngo\n");
				}
				
				line = line + "\n";
				text.append(line);
				
				int a = line.indexOf(");");
				if (a>=0) {
					line = "go\n";
					text.append(line);
				}
			}
			try (FileOutputStream out = new FileOutputStream(new File("F:\\Users Shared Folders\\markfl\\Documents\\My Development\\My SQL Source\\HubGroup\\createtm.sql"))) {
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
