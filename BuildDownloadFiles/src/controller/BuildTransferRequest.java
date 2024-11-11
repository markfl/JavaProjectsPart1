package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class BuildTransferRequest {

	private static ArrayList<String> dtfFile = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		StringBuilder batFile = new StringBuilder();
		
		try (BufferedReader in = new BufferedReader(new 
				InputStreamReader(new FileInputStream("C:\\Temp\\download\\input.txt")))) {
			String newLine = new String();
			while ((newLine  = in.readLine()) != null ) {
				dtfFile.add(newLine);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try (BufferedReader dirin = new BufferedReader(new 
				InputStreamReader(new FileInputStream("C:\\Users Shared Folders\\markfl\\Documents\\My Development\\My SQL Source\\flores_follett\\data\\follett\\filestoread.txt"), "UTF-8"))) {
			String file = new String();
			while ((file  = dirin.readLine()) != null ) {
				int a = file.indexOf(".csv");
				file = file.substring(0, a);
				writeDownloadScripts(file.trim());
				String line = "rtopcb /S " + file.trim() + ".dtf " + file.trim() + ".csv";
				batFile.append(line.trim() + "\n");
			}
			try (FileOutputStream out = new FileOutputStream(new File(
					"C:\\Temp\\download\\download.bat"))) {
				out.write(batFile.toString().getBytes());
				batFile.setLength(0);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeDownloadScripts(String file) {
		
		StringBuilder outLine = new StringBuilder();
		
		for (String newLine : dtfFile) {
			if (newLine.length() >= 9 && newLine.substring(0, 9).equals("HostFile=")) {
				newLine += "MFLORES/" + file.toUpperCase() + "(" + file.toUpperCase() + ")";   	
			} else if (newLine.length() >= 24 && newLine.substring(0, 24).equals("PCFile=C:\\Temp\\download\\")) {
				newLine += file.trim() + ".csv";   
			} else if (newLine.length() >= 25 && newLine.substring(0, 25).equals("FDFFile=C:\\Temp\\download\\")) {
				newLine += file.trim() + ".FDF";   
			}		
			outLine.append(newLine.trim() + "\r\n");
		}
		try (FileOutputStream out = new FileOutputStream(new File(
				"C:\\Temp\\download\\" + file + ".dtf"))) {
			out.write(outLine.toString().getBytes());
			outLine.setLength(0);
			System.out.println(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}