 package controller;

public class ReplaceFile {
	public static void main(String[] args) {
		
		String companyDir = args[0];
		String suffix = args[2];
		String dbPackage = args[3];
		int counter = 1;
		String name = args[1];
		String fileName = name + suffix + ".java";
		System.out.println(name + " has started.");
		FixDatabase.updateJavaSource(companyDir,fileName, suffix, dbPackage, counter);
		System.out.println(name + " completed normally.");
		
	}
}