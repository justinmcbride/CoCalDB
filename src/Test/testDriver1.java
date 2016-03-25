package Test;

import main.*;
import main.Entities.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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

        System.out.println( "------------------------------------------" );
        new Event( pDB );
//        new Event( pDB );
//        new Event( pDB );
        //new Event( pDB );

        System.out.println( "------------------------------------------" );
        ArrayList<String> members = new ArrayList<>();
        members.add( "justin" );
        members.add( "warren" );

        new Group( pDB, "devTeam", members );

        Database DB = new Database(pDB);

        User justin = new User(pDB, "justin");
//        System.out.println( "------------------------------------------" );
//        members.add( "adrian" );
//        new Group( pDB, members );
    }

}