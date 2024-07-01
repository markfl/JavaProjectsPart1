package controller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class CreateBatfile1 {
	
	static StringBuilder text = new StringBuilder();

	public static void main(String[] args) {

		String library;
		try (BufferedReader dirin = new BufferedReader(new 
				InputStreamReader(new FileInputStream("C:\\Users Shared Folders\\markfl\\Documents\\My Development\\iSeriesSource\\Hub Group\\filelist.txt"), "UTF-8"))) {
			while ((library  = dirin.readLine()) != null ) {
				library = library.toLowerCase();
				int a = library.indexOf(".");
				if (a > 0) {
					continue;
				}
				String memberName;

				int b = library.indexOf("rpgle");
				if (b > 0) {
					memberName = library.substring(0, b);
					library = "ren " + library + " " + memberName + ".rpgle\n";
					text.append(library);
					continue;
				}
				
				b = library.indexOf("cpy");
				if (b > 0) {
					memberName = library.substring(0, b);
					library = "ren " + library + " " + memberName + ".cpy\n";
					text.append(library);
					continue;
				} 
				
				b = library.indexOf("clle");
				if (b > 0) {
					memberName = library.substring(0, b);
					library = "ren " + library + " " + memberName + ".clle\n";
					text.append(library);
					continue;
				}
				
				b = library.indexOf("cmd");
				if (b > 0) {
					memberName = library.substring(0, b);
					library = "ren " + library + " " + memberName + ".cmd\n";
					text.append(library);
					continue;
				}

				b = library.indexOf("dds");
				if (b > 0) {
					memberName = library.substring(0, b);
					library = "ren " + library + " " + memberName + ".dds\n";
					text.append(library);
					continue;
				}
								
				b = library.indexOf("rpg");
				if (b > 0) {
					int c = library.indexOf("rpgle");
					if (c < 0) {
						memberName = library.substring(0, b);
						library = "ren " + library + " " + memberName + ".rpg\n";
						text.append(library);
						continue;
					}
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
		try (FileOutputStream out = new FileOutputStream(new File(
				"C:\\Users Shared Folders\\markfl\\Documents\\My Development\\iSeriesSource\\Hub Group\\rnmbch.bat"))) {
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
