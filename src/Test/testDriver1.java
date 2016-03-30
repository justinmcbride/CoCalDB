package Test;

import main.Entities.*;
import main.Structures.MicroMap;


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

        DirectoryMaker.delete(pDB);

        Database DB = Database.GetDB();
        DB.Initialize(pDB, "LockFree");
        int i = 0;
        int NUM_THREADS = 10;
        ArrayList<dbResolverThrd> threads = new ArrayList<>();
        for (i = 0; i < NUM_THREADS; ++i) {
            threads.add(new dbResolverThrd(i, dbThrd.Op.CREATE, dbThrd.Col.CALENDAR, Arrays.asList("Ca" + i, "justin")));
            threads.get(i).start();
        }
        for (i = 0; i < NUM_THREADS; ++i) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {}
        }
        threads.clear();
        System.out.println(DB.m_collection_calendars);
        for (i = 0; i < NUM_THREADS/2; ++i) {

            threads.add(new dbResolverThrd(i, dbThrd.Op.EDIT, dbThrd.Col.CALENDAR, i, Arrays.asList(new MicroMap<String,String>("title", "TEST"))));
            threads.get(i).start();
        }
        for (i = 0; i < NUM_THREADS/2; ++i) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {}
        }
        //threads.clear();
        System.out.println(DB.m_collection_calendars);
        for (i = 5; i < NUM_THREADS; ++i) {

            threads.add(new dbResolverThrd(i, dbThrd.Op.DELETE, dbThrd.Col.CALENDAR, i));
            threads.get(i).start();
        }
        for (i = 5; i < NUM_THREADS; ++i) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {}
        }


//        System.out.println( "------------------------------------------" );
//        members.add( "adrian" );
//        new Group( pDB, members );
    }

}