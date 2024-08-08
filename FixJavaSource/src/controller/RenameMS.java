package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class RenameMS {
	public static void main(String[] args) {
		
		StringBuilder text = new StringBuilder();
		String line = null;
		
		try (BufferedReader in = new BufferedReader(new 
					InputStreamReader(new FileInputStream("F:\\Temp\\fixjava\\rena.txt"), "UTF-8"))) {
			while ((line  = in.readLine()) != null ) {
				int a = line.indexOf(".java");
				if (a > 0) {
					String srcName = line.substring(a+1);
					String lowerName = srcName.toLowerCase();
					text.append("ren " + line + " " + lowerName + "ms.java\n");
				}

			}
			try (FileOutputStream out = new FileOutputStream(new File("F:\\Temp\\fixjava\\rena.bat"))) {
				out.write(text.toString().getBytes());
			}
			System.out.println("Program completed normally");
		} catch (UnsupportedEncodingException e) {
			System.out.println(line);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println(line);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(line);
			e.printStackTrace();
		}
	}
}
