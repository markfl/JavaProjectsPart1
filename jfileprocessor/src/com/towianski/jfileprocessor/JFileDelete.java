package com.towianski.jfileprocessor;

/**
 *
 * @author Stan Towianski - June 2015
 */

import com.towianski.models.ResultsData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;



public class JFileDelete //  implements Runnable 
{
    Boolean deleteFilesOnlyFlag = false;
    boolean deleteToTrashFlag = true;
    Boolean deleteReadonlyFlag = false;
    Boolean cancelFlag = false;
    String startingPath = null;
    ArrayList<Path> copyPaths = new ArrayList<Path>();
    Boolean dataSyncLock = false;
    Deleter deleter = null;
    
    public JFileDelete( String startingPath, ArrayList<Path> copyPaths, Boolean deleteFilesOnlyFlag, Boolean deleteToTrashFlag, Boolean deleteReadonlyFlag )
    {
        this.startingPath = startingPath;
        this.copyPaths = copyPaths;
        this.deleteFilesOnlyFlag = deleteFilesOnlyFlag;
        this.deleteToTrashFlag = deleteToTrashFlag;
        this.deleteReadonlyFlag = deleteReadonlyFlag;
        cancelFlag = false;
    }

    public void cancelSearch()
        {
        cancelFlag = true;
        deleter.cancelSearch();
        }

    public ResultsData getResultsData() {
        //System.out.println( "entered jfilecopy getResultsData()" );
        ResultsData resultsData = new ResultsData();
        try {
            resultsData = new ResultsData( cancelFlag, deleter.getProcessStatus(), deleter.getMessage(), deleter.getNumTested(), deleter.getNumFilesDeleted(), deleter.getNumFoldersDeleted() );
            }
        catch( Exception ex )
            {
            ex.printStackTrace();
            }
        //ResultsData resultsData = new ResultsData();
        return resultsData;
    }
    
    static void usage() {
        System.out.println("jFileDelete <path>" + " -name \"<glob_pattern>\"");
        System.exit(-1);
    }

    public void run( DeleteFrameSwingWorker swingWorker )
        {
        deleter = new Deleter( startingPath, copyPaths, deleteFilesOnlyFlag, deleteToTrashFlag, deleteReadonlyFlag, swingWorker );
//            synchronized( dataSyncLock ) 
//                {
                cancelFlag = false;
                for ( Path fpath : copyPaths )
                    {
                    System.out.println( "delete path =" + fpath + "=" );
                    //EnumSet<FileVisitOption> opts = EnumSet.of( FOLLOW_LINKS );
                    if ( fpath.toFile().exists() || Files.isSymbolicLink( fpath ) )
                        {
                        try {
                            Files.walkFileTree( fpath, deleter );
                            } 
                        catch (IOException ioex) 
                            {
                            //System.out.println( "up ERROR  " + "my error getSimpleName" + ioex.getClass().getSimpleName() );
                            System.out.println( "delete io ERROR  " + "my error msg" + ioex.getMessage() );
                            JOptionPane.showMessageDialog( null, ioex.getClass().getSimpleName() + ": " + ioex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );
                            Logger.getLogger(JFileDelete.class.getName()).log(Level.SEVERE, null, ioex);
                            }
                        catch (Exception ex) 
                            {
                            JOptionPane.showMessageDialog( null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );
                            Logger.getLogger(JFileDelete.class.getName()).log(Level.SEVERE, null, ex);
                            System.out.println( "delete ERROR  " + "my error msg" + ex.getMessage() );
                            }
                        }
                    
                    //break;  for testing to do just 1st path
                    }
//                }
        deleter.done();
        }
        
    public static void main(String[] args) throws IOException 
        {
//        if (args.length < 3
//            || !args[1].equals("-name"))
//            usage();

//        Path startingDir = Paths.get(args[0]);
//        String pattern = args[2];

//        startingPath = "F:/data";
//        filePattern = "*.xml";
//        startingPath = args[0];
//        filePattern = args[1];
        System.out.println("java Delete args[0] =" + args[0] +  "=  args[1] =" + args[1] + "=  args[2] =" + args[2] + "=");

//        JFileDelete jfiledeleter = new JFileDelete( args[0], args[1], args[2], null, null );

//        Thread jfinderThread = new Thread( jfilefinder );
//        jfinderThread.start();
//        try {
//            jfinderThread.join();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(JFileFinder.class.getName()).log(Level.SEVERE, null, ex);
//        }
        }
}    
