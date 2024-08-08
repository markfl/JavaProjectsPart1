package download;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import model.CheckTime;

public class read_follett_qdbasedict {

	public static void main(String[] args) {
		
		CheckTime ct = new CheckTime();
		int counterTotal = 0;
		try (BufferedReader in = new BufferedReader(new
			InputStreamReader(new FileInputStream("C:\\Users Shared Folders\\markfl\\Documents\\My Development\\My SQL Source\\flores_follett\\data\\follett\\qdbasedict.csv"), "UTF-8"))) {
			@SuppressWarnings("unused")
			String line = new String();
			while ((line  = in.readLine()) != null ) {
				counterTotal += 1;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String returnString = ct.calculateElapse("Read qdbasedict");
		System.out.println(counterTotal + " records read to qdbasedict,");
		System.out.println(returnString);
	}
}