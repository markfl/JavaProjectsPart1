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
import java.util.Collection;

public class FixDatabase {
	
	public static void updateJavaSource(String company, String file, String suffix, String DB, int counter) {
		
		StringBuilder text = new StringBuilder();
		
		int counter1 = 0;
		int counter2 = 0;
		int counter3 = 0;
		int fieldCounter = 0;
		String searchSuffix = ".java";
		String searchString = suffix + searchSuffix; 
		int a = file.indexOf(searchString);
		String newFileName = file.substring(0, a) + searchSuffix;
		String className = file.substring(0, a) + "(";
		String comma = ",";
		String quotes = "\"";
		String equalSign = "=";
		String endBrace = ")";
		String saveLine = "";
		String testFileName = file.substring(0, a);
		String testName = "zprfwrk";
		String skipName1 = "wups";
		boolean connectFound = false;
		boolean firstTime = true;
		boolean toStringFound = false;
		boolean keysFound = false;
		boolean checkSqlFound = false;
		boolean lineSaved = false;
		boolean inWhereClause = false;
		boolean allFieldsFound = false;
		boolean skipConstructor = false;
		boolean caseFieldFound = false;
		Collection<String> checkList = new ArrayList<String>();
		

		try (BufferedReader in = new BufferedReader(new 
					InputStreamReader(new FileInputStream("C:\\Temp\\fixjava\\" + file), "UTF-8"))) {
			String line;
			while ((line  = in.readLine()) != null ) {
				
				a = 0;
				int b = 0;
				int c = 0;
				
				if (lineSaved) {
					lineSaved = false;
					a = line.indexOf("?\";");
					if (a <= 0) {
						b = line.indexOf("?");
						if (b > 0) {
							saveLine = saveLine + " +";
						} else {
							saveLine = saveLine + ";";
							inWhereClause = false;
						}
					} else {
						saveLine = saveLine + " +";
					}
					
					text.append(saveLine + "\n");
				}
				
				counter3++;
				if (counter3 == 5) {
					// System.out.println(line);
				}
				
				// Change all @ to _
				line = line.replace("@", "_");
				line = line.replace("#", "_");
				
				if (testFileName.equals(testName)) {
					if (line != "") {
						System.out.println(counter3 + " " + line);
					}
				}
				
				if (firstTime) {
					firstTime = false;
					text.append("package database." + DB + ";\n");
				}
				
				a = line.indexOf("import java.sql.DriverManager;");
				if (a >= 0) {
					continue;
				}
				if (counter1 == 0) {
					a = line.indexOf("connect(");
					if (a > 0) {
						counter1++;
						connectFound = true;
					}
				}
				
				a = line.indexOf("doubleString = new Double(field).toString();");
				if (a >= 0) {
					line = "      doubleString = String.valueOf(field);";
				}
				
				if (connectFound == true && counter1 <= 23) {
					counter1++;
					continue;
				}
				
				a = line.indexOf("int length = ");
				if (a >= 0) {
					line = line.replace("int length = ", "int fldLength = ");
				}
				
				a = line.indexOf("if (checkSizeString(");
				if (a >= 0) {
					line = line.replace(", length", ", fldLength");
				}
				
				a = line.indexOf("if (checkSizeInt(");
				if (a >= 0) {
					line = line.replace(", length", ", fldLength");
				}

				a = line.indexOf("if (checkSizeDouble(");
				if (a >= 0) {
					line = line.replace(", length", ", fldLength");
				}
				
				// check for private int Key
				a = line.indexOf("private int Key");
				c = line.indexOf(";");
				if (a > 0) {
					keysFound = true;
					checkList.add(line.substring(15, c));
				} else {
					// check for private String Key
					a = line.indexOf("private String Key");
					if (a > 0) {
						keysFound = true;
					} else {
						// check for private double Key
						a = line.indexOf("private double Key");
						if (a > 0) {
							keysFound = true;
							checkList.add(line.substring(18, c));
						} else {
							// check for private long Key
							a = line.indexOf("private long Key");
							if (a > 0) {
								keysFound = true;
								checkList.add(line.substring(16, c));
							}
						}
					}
				}

				if (!allFieldsFound) {
					a = line.indexOf(" case;");
					if (a > 0) {
						caseFieldFound = true;
						line = line.replace("case;", "case_;");
					}
				}
				
				if (caseFieldFound) {
					a = line.indexOf(" case ");
					if (a > 0) {
						line = line.replace("case", "case_");
					}
					a = line.indexOf("casesav");
					if (a > 0) {
						line = line.replace("casesav", "case_sav");
					}
					a = line.indexOf("caseSav;");
					if (a > 0) {
						line = line.replace("caseSav;", "case_Sav;");
					}
					a = line.indexOf("setcase");
					if (a > 0) {
						line = line.replace("setcase", "setcase_");
					}
					a = line.indexOf(".case");
					if (a > 0) {
						line = line.replace(".case", ".case_");
					}
					a = line.indexOf("case)");
					if (a > 0) {
						line = line.replace("case)", "case_)");
					}
					a = line.indexOf("case,");
					if (a > 0) {
						line = line.replace("case,", "case_,");
					}
					a = line.indexOf("case ");
					if (a > 0) {
						line = line.replace("case", "case_");
					}
				}
				
				if (!keysFound && !allFieldsFound) {
					a = line.indexOf("private ");
					if (a > 0) {
						b = line.indexOf("Sav;");
						if (b > 0) {
							allFieldsFound = true;
						} else {
							fieldCounter++;
						}
					} 
				}
				
				if ((allFieldsFound && fieldCounter > 255)
				|| (allFieldsFound && testFileName.equals(skipName1))) {
					a = line.indexOf(className);
					if (a > 0) {
						b = line.indexOf("(");
						String testChar1 = line.substring(a - 1, a);
						String testChar = line.substring(b + 1, b + 2);
						if (!testChar.equals(endBrace) && testChar1.equals(" ")) {
							skipConstructor = true;
							System.out.println(testChar1);
						}
					}
				}
				
				if (skipConstructor) {
					a = line.indexOf("}");
					if (a > 0) {
						skipConstructor = false;
						continue;
					} else continue;
				}
				
				for (String element : checkList) {
					String comString = "if (" + element + " == " + quotes + quotes +") {";
					String newString = "      if (" + element + " == 0) {";
					a = line.indexOf(comString);
					if (a > 0) {
						line = line.replace(line, newString);
					}
				}
				
				// check for invalid date field
				a = line.indexOf("date#");
				if (a > 0) {
					line = line.replace("date#", "date_");
				}
				
				// check for invalid time field
				a = line.indexOf("time#");
				if (a > 0) {
					line = line.replace("time#", "time_");
				}
				
				// check for invalid date field
				a = line.indexOf("switch");
				if (a > 0) {
					line = line.replace("switch", "switch_");
				}
				
				// check for invalid date field
				a = line.indexOf("number#");
				if (a > 0) {
					line = line.replace("number#", "number_");
				}
				
				// check for invalid add field
				a = line.indexOf("add#");
				if (a > 0) {
					line = line.replace("add#", "add_");
				}
				
				// check for invalid row field
				a = line.indexOf("row#");
				if (a > 0) {
					line = line.replace("row#", "row_");
				}
				
				// check for invalid comment field
				a = line.indexOf("comment#");
				if (a > 0) {
					line = line.replace("comment#", "comment_");
				}

				// check for invalid option field
				a = line.indexOf("option#");
				if (a > 0) {
					line = line.replace("option#", "option_");
				}
				
				// check for invalid user field
				a = line.indexOf("user#");
				if (a > 0) {
					line = line.replace("user#", "user_");
				}

				// check for invalid order field
				a = line.indexOf("order#");
				if (a > 0) {
					line = line.replace("order#", "order_");
				}
				
				// check for toStringKey
				if (!toStringFound) {
					b = line.indexOf("toStringKey()");
					if (b > 0) {
						toStringFound = true;
					}
				}
				
				if (toStringFound) {
					c = line.indexOf(quotes);
					if (c > 1) {
						char charString = line.charAt(c-1);
						char equalChar = '=';
						int compare = Character.compare(charString, equalChar);
						if (compare == 0) {
							line =  "                       " + quotes + line.trim();
						}
					}
				}
				
				if (toStringFound  && !keysFound && counter2 <= 4) {
					counter2++;
					continue;
				}
				
				a = line.indexOf("checkSql =");
				if (a > 0) {
					c = line.indexOf(";");
					if (c < 0) {
						checkSqlFound = true;
					}
				}
				
				if (checkSqlFound) {
					c = line.indexOf(quotes);
					if (c > 1) {
						char charString = line.charAt(c-1);
						char equalChar = ' ';
						int compare = Character.compare(charString, equalChar);
						if (compare != 0) {
							line =  "                              " + quotes + " " + line.trim();
							// checkSqlFound = false;
						}
					}
					c = line.indexOf(";");
					if (c >= 0) {
						checkSqlFound = false;
						inWhereClause = false;
					} else {
						a = line.indexOf("\"");
						if (a >= 0) {
							b = line.indexOf("\"", a + 1);
							if (b < 0) {
								c = line.indexOf("+");
								if (c < 0) {
									String testLive = line.trim() + "\\";
									int d = testLive.indexOf("\\");
									String testChar = testLive.substring(d-1,d);
									if (testChar.equals(comma)) {
										line = line + "\" +";
									} else {
										int e = line.indexOf("where");
										if (e > 0 || inWhereClause) {
											inWhereClause = true;
											if (testChar.equals(equalSign)) {
												line = line + "?\"";
												saveLine = line;
												lineSaved = true;
												continue;
											} else {
												line = line + "=?\"";
												// save line to write later
												saveLine = line;
												lineSaved = true;
												continue;
											}
										} else {
											line = line + ",\" +";
										}
									}
								}
							} else {
								b = line.indexOf("+");
								if (b < 0) line = line + "+";
							}
						}
					}
				}
				
				a = line.indexOf(",) {");
				if (a > 0) {
					line = line.replace(",) {", ") {");
				}
				
				a = line.indexOf("+ \", + \"]\"");
				if (a > 0) {
					line = line.replace("+ \", + \"]\"", "+ \"]\"");
				}
				
				a = line.indexOf("System.out.println(\"Field Key");
				if (a > 0) {
					b = line.indexOf(";");
					if (b <= 0) {
						c = line.indexOf(":");
						String keyField = line.substring(a+26, c);
						int d = line.indexOf("+");
						line = line.substring(1, d+1) + " " + keyField + ");";
					}
				}
				
				a = line.indexOf(": not updated properly.");
				if (a > 0) {
					b = line.indexOf(";");
					if (b <= 0) line = line + ";";
				}
				
				a = line.indexOf("Some fields were not               updated properly.");
				if (a >0) {
					line = "         System.out.println(\"Some fields were not updated properly.\");";
				}
				
				text.append(line + "\n");

				
			}
			
			try (FileOutputStream out = new FileOutputStream(new File("C:\\Users Shared Folders\\markfl\\Documents\\My Development\\Eclipse\\Java EE\\" + company + "\\src\\database\\" + DB + "\\" + newFileName))) {
				out.write(text.toString().getBytes());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}