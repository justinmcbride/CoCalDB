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

        Database DB = Database.GetDB();
        DB.Initialize(pDB);
        int i = 0;
        int NUM_THREADS = 20;
        ArrayList<dbResolverThrd> threads = new ArrayList<>();
        for (i = 0; i < NUM_THREADS; ++i) {
            threads.add(new dbResolverThrd(i, dbThrd.Operation.CREATE, dbThrd.Collection.CALENDAR, Arrays.asList("Cal0", "justin")));
            threads.get(i).start();
        }
        for (i = 0; i < NUM_THREADS; ++i) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {}
        }
        System.out.println(DB.m_collection_calendars);


//        System.out.println( "------------------------------------------" );
//        members.add( "adrian" );
//        new Group( pDB, members );
    }

}