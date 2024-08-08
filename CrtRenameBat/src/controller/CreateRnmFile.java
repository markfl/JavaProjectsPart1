package controller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class CreateRnmFile {
	
	static StringBuilder text = new StringBuilder();

	public static void main(String[] args) {
		
		String library;
		
		try (BufferedReader dirin = new BufferedReader(new 
				InputStreamReader(new FileInputStream("C:\\Users Shared Folders\\markfl\\Documents\\My Development\\iSeriesSource\\Hub Group\\filelist.txt"), "UTF-8"))) {
			while ((library  = dirin.readLine()) != null ) {
				library = library.toLowerCase();
				int a = library.indexOf(".");
				String memberName = library.substring(0, a);
				String fileType = library.substring(a+1);
				
				if (fileType.equals("clle")) {
					text.append("ren " + library + " " + memberName + ".clle\n");
				}
				if (fileType.equals("clp")) {
					text.append("ren " + library + " " + memberName + ".clp\n");
				}
				if (fileType.equals("cmd")) {
					text.append("ren " + library + " " + memberName + ".cmd\n");
				}
				if (fileType.equals("c")) {
					text.append("ren " + library + " " + memberName + ".c\n");
				}
				if (fileType.equals("cpy")) {
					text.append("ren " + library + " " + memberName + ".cpy\n");
				}
				if (fileType.equals("dds")) {
					text.append("ren " + library + " " + memberName + ".dds\n");
				}
				if (fileType.equals("java")) {
					text.append("ren " + library + " " + memberName + ".java\n");
				}
				if (fileType.equals("rpg")) {
					text.append("ren " + library + " " + memberName + ".rpg\n");
				}
				if (fileType.equals("rpgle")) {
					text.append("ren " + library + " " + memberName + ".rpgle\n");
				}
				if (fileType.equals("save")) {
					text.append("ren " + library + " " + memberName + ".save\n");
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