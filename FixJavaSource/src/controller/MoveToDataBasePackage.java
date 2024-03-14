package controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MoveToDataBasePackage {

	public static void main(String[] args) {
		
		if (args[0] != null && args[1] != null && args[2] != null) {
			
			String companyDir = args[0];
			String suffix = args[1];
			String file = suffix + ".java";
			String dbPackage = args[2];
			
			int counter = 0;
			String line;
			String inputFileName = "F:\\Temp\\fixjava\\move" + suffix + ".txt";
			
			try (BufferedReader in = new BufferedReader(new 
						InputStreamReader(new FileInputStream(inputFileName), "UTF-8"))) {
				while ((line  = in.readLine()) != null ) {
					int a = line.indexOf(file);
					String newFileName = line.substring(0, a) + ".java";
					counter++;
					FixDatabase.updateJavaSource(companyDir, line, suffix, dbPackage, counter);
					System.out.println(newFileName + " completed normally.");
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
}