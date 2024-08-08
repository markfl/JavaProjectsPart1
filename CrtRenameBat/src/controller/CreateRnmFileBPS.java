package controller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class CreateRnmFileBPS {
	
	static StringBuilder text = new StringBuilder();


	public static void main(String[] args) {
		
		String library;
		
		try (BufferedReader dirin = new BufferedReader(new 
				InputStreamReader(new FileInputStream("C:\\Users Shared Folders\\markfl\\Documents\\My Development\\iSeriesSource\\Bass Pro Shops\\Flores Source\\qsqlsrc\\filelist.txt"), "UTF-8"))) {
			while ((library  = dirin.readLine()) != null ) {
				library = library.toLowerCase();
				int a = library.indexOf(".");
				String memberName = library.substring(0, a);
				String fileType = library.substring(a+1);
				
				if (fileType.equals("sqlsrc")) {
					text.append("ren " + library + " " + memberName + ".sql\n");
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(text);
		try (FileOutputStream out = new FileOutputStream(new File(
				"C:\\Users Shared Folders\\markfl\\Documents\\My Development\\iSeriesSource\\Bass Pro Shops\\Flores Source\\qsqlsrc\\rnmbch.bat"))) {
				out.write(text.toString().getBytes());
				text.setLength(0);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}
