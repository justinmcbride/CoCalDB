package Test;

import main.Entities.*;
import main.Structures.MicroMap;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by justinmcbride on 3/23/16.
 *
 * Test file for basic cocal db system using direct file access
 */
class testDriverOps {

    public static void main(String[] args) {
        //Path pDB = Paths.get(System.getProperty("user.home"));
        Path pDB  = Paths.get(System.getProperty("user.dir")).resolve("testDB");
        System.out.println( "WorkDir: " + pDB.toString() );

        FileHelper.Delete( pDB );
        ArrayList<String> Stringlist = new ArrayList<>();
        ArrayList<MicroMap<String,String>> MapList = new ArrayList<>();
        MicroMap<String,ArrayList<String>> addRem = new MicroMap<>();

        Database DB = Database.GetDB();
        DB.Initialize(pDB, "Lazy", true);

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
        //threads.clear();
        System.out.println(DB.m_collection_calendars);
        for (i = 0; i < NUM_THREADS/2; ++i) {

            threads.set(i,new dbResolverThrd(i, dbThrd.Op.EDIT, dbThrd.Col.CALENDAR, i, Arrays.asList(new MicroMap<String,String>("title", "TEST"))));
            threads.get(i).start();
        }
        for (i = 0; i < NUM_THREADS/2; ++i) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {}
        }
        for (i = 0; i < NUM_THREADS; ++i) {

           // threads.set(i, new dbResolverThrd(i, dbThrd.Op.LINK, dbThrd.Col.CALENDAR, 0, new MicroMap<String,List<String>>("events", Arrays.asList("tacos" + i))));
            threads.get(i).start();
        }
        for (i = 0; i < NUM_THREADS; ++i) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {}
        }
        for (i = 0; i < NUM_THREADS/4; ++i) {

           // threads.set(i, new dbResolverThrd(i, dbThrd.Op.UNLINK, dbThrd.Col.CALENDAR, 0, new MicroMap<String,List<String>>("events", Arrays.asList("tacos" + i, "tacos" + (i + 2)))));
            threads.get(i).start();
        }
        for (i = 0; i < NUM_THREADS/2; ++i) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {}
        }
        //threads.clear();

        for (i = 0; i < NUM_THREADS/2; ++i) {

            threads.set(i,new dbResolverThrd(i, dbThrd.Op.DELETE, dbThrd.Col.CALENDAR, i + 5));
            threads.get(i).start();
        }
        for (i = 0; i < NUM_THREADS/2; ++i) {
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