package controller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class CreateBatFileHubGroup {
	
	static StringBuilder text = new StringBuilder();

	public static void main(String[] args) {
		
		String library;
		
		try (BufferedReader dirin = new BufferedReader(new 
				InputStreamReader(new FileInputStream("C:\\Users Shared Folders\\markfl\\Documents\\My Development\\iSeriesSource\\Hub Group\\filelist.txt"), "UTF-8"))) {
			while ((library  = dirin.readLine()) != null ) {
				library = library.toLowerCase();
				int a = library.indexOf(".");
				if (a > 0) {
					String fileType = library.substring(0, a);
					String memberName = library.substring(a+1);
	
					if (fileType.equals("qcllesrc")) {
						text.append("ren " + library + " " + memberName + ".clle\n");
						continue;
					}
					if (fileType.equals("qclsrc")) {
						text.append("ren " + library + " " + memberName + ".clp\n");
						continue;
					}
					if (fileType.equals("qcmdsrc")) {
						text.append("ren " + library + " " + memberName + ".cmd\n");
						continue;
					}
					if (fileType.equals("qcpysrc")) {
						text.append("ren " + library + " " + memberName + ".cpy\n");
						continue;
					}
					if (fileType.equals("qddssrc")) {
						text.append("ren " + library + " " + memberName + ".dds\n");
						continue;
					}
					if (fileType.equals("qjavasrc")) {
						text.append("ren " + library + " " + memberName + ".java\n");
						continue;
					}
					if (fileType.equals("qrpgsrc")) {
						text.append("ren " + library + " " + memberName + ".rpg\n");
						continue;
					}
					if (fileType.equals("qrpglesrc")) {
						text.append("ren " + library + " " + memberName + ".rpgle\n");
						continue;
					}
					if (fileType.equals("qsavesrc")) {
						text.append("ren " + library + " " + memberName + ".save\n");
						continue;
					}
					if (fileType.equals("qrpg41919")) {
						text.append("ren " + library + " " + memberName + ".qrpg41919\n");
						continue;
					}
					if (fileType.equals("qcpple")) {
						text.append("ren " + library + " " + memberName + ".cpp\n");
						continue;
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			System.out.print(text);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.print(text);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.print(text);
			e.printStackTrace();
		}
		System.out.print(text);
		try (FileOutputStream out = new FileOutputStream(new File(
				"C:\\Users Shared Folders\\markfl\\Documents\\My Development\\iSeriesSource\\Hub Group\\rnmbch.bat"))) {
				out.write(text.toString().getBytes());
				text.setLength(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}