package Test;

import main.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by justinmcbride on 3/23/16.
 *
 * Test file for basic cocal db system using direct file access
 */
public class testDriver1 {

    public static void main(String[] args) {
        //Path pDB = Paths.get(System.getProperty("user.home"));
        Path pDB  = Paths.get(System.getProperty("user.dir")).resolve("testDB");
        System.out.println( "WorkDir: " + pDB.toString() );
//        idFile blahFile = new idFile( 17,Paths.get(System.getProperty("user.home"), "test.txt") );
//        blahFile.SetValue( 15, true );

        new Event( pDB );
        //new Event( pDB );
        //new Event( pDB );
        //new Event( pDB );
    }

}