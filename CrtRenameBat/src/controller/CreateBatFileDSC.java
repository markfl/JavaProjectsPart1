package controller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class CreateBatFileDSC {
	
	static StringBuilder text = new StringBuilder();

	public static void main(String[] args) {
		
		String library;
		
		try (BufferedReader dirin = new BufferedReader(new 
				InputStreamReader(new FileInputStream("C:\\Users Shared Folders\\markfl\\Documents\\My Development\\iSeriesSource\\DSC Logistics\\20171116\\filelist.txt"), "UTF-8"))) {
			while ((library  = dirin.readLine()) != null ) {
				library = library.toLowerCase();
				int a = library.indexOf(".");
				if (a > 0) {
					String fileType = library.substring(0, a);
					String memberName = library.substring(a+1);
					
					if (memberName.equals("clleold")) {
						text.append("ren " + library + " " + fileType + ".clleold\n");
						continue;
					}
					if (fileType.equals("cllemod")) {
						text.append("ren " + library + " " + memberName + ".cllemod\n");
						continue;
					}
					if (fileType.equals("qcllemod")) {
						text.append("ren " + library + " " + memberName + ".cllemod\n");
						continue;
					}
					if (memberName.equals("cllesrc")) {
						text.append("ren " + library + " " + fileType + ".clle\n");
						continue;
					}
					if (fileType.equals("qcllesrc")) {
						text.append("ren " + library + " " + memberName + ".clle\n");
						continue;
					}
					if (memberName.equals("cmdsrc")) {
						text.append("ren " + library + " " + fileType + ".cmd\n");
						continue;
					}
					if (memberName.equals("cpysrc")) {
						text.append("ren " + library + " " + fileType + ".cpy\n");
						continue;
					}
					if (memberName.equals("ddssrc")) {
						text.append("ren " + library + " " + fileType + ".dds\n");
						continue;
					}
					if (fileType.equals("qddssrc")) {
						text.append("ren " + library + " " + memberName + ".dds\n");
						continue;
					}
					if (fileType.equals("qcpysrc")) {
						text.append("ren " + library + " " + memberName + ".cpy\n");
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
					if (fileType.equals("qrpglemod")) {
						text.append("ren " + library + " " + memberName + ".rpglemod\n");
						continue;
					}
					if (memberName.equals("rpgleold")) {
						text.append("ren " + library + " " + fileType + ".rpgleold\n");
						continue;
					}
					if (memberName.equals("rpglesrc")) {
						text.append("ren " + library + " " + fileType + ".rpgle\n");
						continue;
					}
					if (memberName.equals("rpglemod")) {
						text.append("ren " + library + " " + fileType + ".rpglemod\n");
						continue;
					}
					text.append("ren " + library + " " + fileType + "\n");
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
				"C:\\Users Shared Folders\\markfl\\Documents\\My Development\\iSeriesSource\\DSC Logistics\\20171116\\rnmbch.bat"))) {
				out.write(text.toString().getBytes());
				text.setLength(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}