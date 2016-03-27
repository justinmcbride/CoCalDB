package Test;

import main.*;
import main.Entities.*;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

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

//        System.out.println( "------------------------------------------" );
//        new Event( pDB );
////        new Event( pDB );
////        new Event( pDB );
//        //new Event( pDB );
//
//        System.out.println( "------------------------------------------" );
//        ArrayList<String> members = new ArrayList<>();
//        members.add( "justin" );
//        members.add( "warren" );
//        new Group( pDB, "devTeam", members );
//        User justin = new User(pDB, "justin");
//        Calendar bobCal = new Calendar(pDB, "bobCal", "justin");
//
        Database DB = new Database(pDB);
        DB.Initialize();
//        int i = 0;
//        ArrayList<dbResolverThrd> threads = new ArrayList<>();
//        threads.add(new dbResolverThrd(i, dbThrd.Operation.CREATE, dbThrd.Collection.CALENDAR, Arrays.asList("Cal0","justin")));
//        threads.get(i).start();
//        try {threads.get(i).join();}
//        catch (InterruptedException e) {}

//        System.out.println( "------------------------------------------" );
//        members.add( "adrian" );
//        new Group( pDB, members );
    }

}