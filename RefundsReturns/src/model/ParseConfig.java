package model;

public class ParseConfig {

	public static void main(String[] args) {

		Database db = new Database();
		
		String output[] = db.parseXMLConfig();
		System.out.println(output[0] + "\n" + output[1] + "\n" + output[2]);		
	}
}