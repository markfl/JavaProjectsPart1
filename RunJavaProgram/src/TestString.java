import java.util.ArrayList;

public class TestString {

	public static void main(String[] args) {
		ArrayList<String> test = new ArrayList<String>();
		test.add("darkey");
		test.add("datype");
		test.add("daactn");
		test.add("daday_a");
		test.add("dayear");
		test.add("damth");
		test.add("daday");
		test.add("dahour");
		String fieldName = "damth";
		int i = test.indexOf(fieldName.trim());
		System.out.println(fieldName + " " + i + " " + test.get(i)  + " " + test.toString());
	}
}