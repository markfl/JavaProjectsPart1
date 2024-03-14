package download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

public class BuildDTF {

	public static void main(String[] args) {
		
		StringBuilder text = new StringBuilder();
		Collection<String> instList = new ArrayList<String>();

		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Temp\\download\\download.txt"), "UTF-8"))) {
			String line;
			try {
				while ((line  = in.readLine()) != null ) {
					instList.add(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Temp\\download\\download.csv"), "UTF-8"))) {
			String line;
			String fileName = "";
			String library = "";
			try {
				while ((line  = in.readLine()) != null ) {
					int a;
					a = line.indexOf(",");
					fileName = line.substring(0,a);
					String lowerFileName = fileName.toLowerCase();
					library = line.substring(a+1);
					String lowerLibrary = library.toLowerCase();
					a = 0;
					for (String element : instList) {
						String output = element;
						if (a == 4) {
							output = element + library + "/" + fileName + "(" + fileName + ")";
						}
						if (a == 14) {
							output = element + "C:\\Users Shared Folders\\markfl\\Documents\\My Development\\My SQL Source\\dmbowman\\data\\" + lowerLibrary + "\\" + lowerFileName + ".csv";
						}
						text.append(output + "\n");
						a++;
					}
					try (FileOutputStream out = new FileOutputStream(new File("C:\\temp\\download\\" + lowerFileName + ".dtf"))) {
						out.write(text.toString().getBytes());
						text.setLength(0);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}