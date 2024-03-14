package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class CreateInsertRecords {

	public static void main(String[] args) {
		
		String companyDir = args[0];
		StringBuilder text = new StringBuilder();
		String line;
		String inputFileName = "C:\\Users Shared Folders\\markfl\\Documents\\My Development\\My SQL Source\\" + companyDir + "\\drop.sql";
		
		try (BufferedReader in = new BufferedReader(new 
					InputStreamReader(new FileInputStream(inputFileName), "UTF-8"))) {
			while ((line  = in.readLine()) != null ) {
				
				int a = line.indexOf("drop");
				if (a >= 0) {
					String newFileName = line.substring(11);
					line = "INSERT INTO `" + companyDir + "`.`"+ companyDir + "files` (`filename`) VALUES ('" + newFileName + "');";
					text.append(line + "\n");
				}
			}
			try (FileOutputStream out = new FileOutputStream(new File("C:\\Users Shared Folders\\markfl\\Documents\\My Development\\My SQL Source\\" + companyDir + "\\insert.sql"))) {
				out.write(text.toString().getBytes());
			}
			System.out.println("Program completed normally");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}