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
        Path path_database = Paths.get(System.getProperty("user.dir")).resolve("testDB");
        System.out.println( "Database Directory: " + path_database.toString() );

        Database DB = Database.GetDB();

        int number_of_tests = 5;

        ArrayList<Integer> number_of_threads = new ArrayList<>();
        number_of_threads.add( 1 );
        number_of_threads.add( 3 );
//        number_of_threads.add( 6 );
//        number_of_threads.add( 9 );
//        number_of_threads.add( 15 );

        ArrayList<String> types_of_lists = new ArrayList<>();
        types_of_lists.add( "Lazy" );
        types_of_lists.add( "LockFree" );


        System.out.println( "list_type,number_of_threads,duration" );
        for( String list_type : types_of_lists )
        {
            for( int nThreads : number_of_threads )
            {
                for( int loop = 0; loop < number_of_tests; loop++ )
                {
                    DB.Initialize( path_database, list_type, true );
                    long startTime = System.nanoTime();
                    ArrayList<dbResolverThrd> threads = new ArrayList<>();

                    for( int i = 0; i < nThreads; ++i ) {
                        threads.add(new dbResolverThrd(i, dbThrd.Op.CREATE, dbThrd.Col.CALENDAR, Arrays.asList("Ca" + i, "justin")));
                        threads.get(i).start();
                    }
                    for( int i = 0; i < nThreads; ++i ) {
                        try {
                            threads.get(i).join();
                        } catch (InterruptedException e) {}
                    }
                    threads.clear();

                    for( int i = 0; i < nThreads; ++i ) {

                        threads.add(new dbResolverThrd(i, dbThrd.Op.EDIT, dbThrd.Col.CALENDAR, i, Arrays.asList(new MicroMap<String,String>("title", "TEST"))));
                        threads.get(i).start();
                    }
                    for( int i = 0; i < nThreads; ++i ) {
                        try {
                            threads.get(i).join();
                        } catch (InterruptedException e) {}
                    }
                    threads.clear();

                    for( int i = 0; i < nThreads; ++i ) {

                        threads.add(new dbResolverThrd(i, dbThrd.Op.DELETE, dbThrd.Col.CALENDAR, i));
                        threads.get(i).start();
                    }
                    for( int i = 0; i < nThreads; ++i ) {
                        try {
                            threads.get(i).join();
                        } catch (InterruptedException e) {}
                    }
                    long endTime = System.nanoTime();
                    long duration = endTime - startTime;
                    System.out.println( list_type + "," + nThreads + "," + duration );
                } // end nThreads
            } // end number_of_tests loop
        } // end types of lists loop

        

//        System.out.println( "------------------------------------------" );
//        members.add( "adrian" );
//        new Group( path_database, members );
    }

}