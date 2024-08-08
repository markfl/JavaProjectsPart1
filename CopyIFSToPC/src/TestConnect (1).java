//////////////////////////////////////////////////////////////////////////////////
//
// IFSListFile example.  This program uses the integrated file system classes
// to list the contents of a directory on the server.
//
// Command syntax:
//    IFSList system directory
//
// For example,
//    IFSList MySystem /path1
//
//////////////////////////////////////////////////////////////////////////////////

import java.util.*;
import com.ibm.as400.access.*;

import model.IBMi;

public class TestConnect extends Object {
   public static void main(String[] args) {
	   
	   String company = new String();
	   if (args.length > 0)
		   company = args[0];
	   else
		   company = "westend";

      String directoryName = "/";
      IBMi dbIBMi = new IBMi(company);
      AS400 as400 = dbIBMi.getAS400();
      System.out.println(as400.getSystemName());

      try {
    	  // Create the IFSFile object for the directory.
    	  IFSFile directory = new IFSFile(as400, directoryName);

    	  // Generate the list of name.  Pass the list method the
    	  // directory filter object and the search match criteria.

    	  // Note - this example does the processing in the filter
    	  // object.  An alternative is to process the list after
    	  // it is returned from the list method call.

    	  String[] directoryNames = directory.list(new MyDirectoryFilter(),"*");

    	  // Tell the user if the directory doesn't exist or is empty
    	  if (directoryNames == null)
    		  System.out.println("The directory does not exist");

    	  else if (directoryNames.length == 0)
    		  System.out.println("The directory is empty");
      } catch (Exception e) {
    	  // If any of the above operations failed say the list failed
    	  // and output the exception.

    	  System.out.println("List failed");
    	  System.out.println(e);
      }
         
      System.exit(0);
   }
}

////////////////////////////////////////////////////////////////////////////
//
// The directory filter class prints information from the file object.
//
// Another way to use the filter is to simply return true or false
// based on information in the file object.  This lets the mainline
// function decide what to do with the list of files that meet the
// search criteria.
//
////////////////////////////////////////////////////////////////////////////



class MyDirectoryFilter implements IFSFileFilter {
	
	public boolean accept(IFSFile file) {
		
		try {
			// Print the name of the current file
			System.out.print(file.getName());
			
			// Pad the output so the columns line up
			for (int i = file.getName().length(); i < 18; i++)
				System.out.print(" ");

			// Print the date the file was last changed.
			long changeDate = file.lastModified();
			Date d = new Date(changeDate);
			System.out.print(d);
			System.out.print("  ");
         
			// Print if the entry is a file or directory

			System.out.print("   ");

			if (file.isDirectory())
				System.out.println("<DIR>");
			else
				System.out.println(file.length());
			// Keep this entry.  Returning true tells the IFSList object
			// to return this file in the list of entries returned to the
			// .list() method.

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}