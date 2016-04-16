package Test;

import main.Entities.*;
import main.Structures.MicroMap;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by justinmcbride on 3/23/16.
 *
 * Test file for basic cocal db system using direct file access
 */
class testDriver1 {

    public static void main(String[] args) {
        Path path_database = Paths.get(System.getProperty("user.dir")).resolve("testDB");
        System.out.println( "Database Directory: " + path_database.toString() );

        Database DB = Database.GetDB();

        int number_of_tests = 1;
        int number_of_tasks = 15;

        ArrayList<Integer> number_of_threads = new ArrayList<>();
        number_of_threads.add( 1 );
        number_of_threads.add( 2 );
        number_of_threads.add( 4 );
        number_of_threads.add( 8 );
        number_of_threads.add( 16 );

        ArrayList<String> types_of_lists = new ArrayList<>();
        types_of_lists.add( "Lazy" );
        types_of_lists.add( "LockFree" );

        /* Total number of processors or cores available to the JVM */
        System.out.println("Available processors (cores): " + Runtime.getRuntime().availableProcessors());

        /* Total amount of free memory available to the JVM */
        System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());

        /* Total memory currently available to the JVM */
        System.out.println("Total memory available to JVM (bytes): " + Runtime.getRuntime().totalMemory());

        System.out.println( "list_type,number_of_threads,duration" );
        for( String list_type : types_of_lists )
        {
            for( int nThreads : number_of_threads )
            {
                for( int loop = 0; loop < number_of_tests; loop++ )
                {
                    DB.Initialize( path_database, list_type, true );
                    long startTime = System.nanoTime();

                    ExecutorService manager = Executors.newFixedThreadPool( nThreads );

                    // do all the first creations
                    for( int i = 0; i < number_of_tasks; ++i ) {
                        manager.submit(new dbResolverThrd(i, dbThrd.Op.CREATE, dbThrd.Col.CALENDAR, Arrays.asList("Ca" + i, "justin")));
                    }

                    manager.shutdown();
                    try {
                        manager.awaitTermination(3, TimeUnit.SECONDS);
                    }
                    catch( InterruptedException e ) {
                        manager.shutdownNow();
                        Thread.currentThread().interrupt();
                    }

                    // do a bunch of edits
                    manager = Executors.newFixedThreadPool( nThreads );
                    for( int i = 0; i < number_of_tasks; ++i ) {

                        manager.submit(new dbResolverThrd(i, dbThrd.Op.EDIT, dbThrd.Col.CALENDAR, new Integer(i), Arrays.asList(new MicroMap<String,String>("title", "TEST"))));
                    }

                    manager.shutdown();
                    try {
                        manager.awaitTermination(3, TimeUnit.SECONDS);
                    }
                    catch( InterruptedException e ) {
                        manager.shutdownNow();
                        Thread.currentThread().interrupt();
                    }

                    // do a bunch of deletions
                    manager = Executors.newFixedThreadPool( nThreads );
                    for( int i = 0; i < number_of_tasks; ++i ) {
                        manager.submit(new dbResolverThrd(i, dbThrd.Op.DELETE, dbThrd.Col.CALENDAR, new Integer(i)));
                    }
                    manager.shutdown();
                    try {
                        manager.awaitTermination(3, TimeUnit.SECONDS);
                    }
                    catch( InterruptedException e ) {
                        manager.shutdownNow();
                        Thread.currentThread().interrupt();
                    }
                    long endTime = System.nanoTime();
                    long duration = endTime - startTime;
                    System.out.println( list_type + ", " + nThreads + ", took " + duration + " ns" );
                    //System.out.println( DB.m_collection_calendars );
                } // end nThreads
            } // end number_of_tests loop
        } // end types of lists loop

        

//        System.out.println( "------------------------------------------" );
//        members.add( "adrian" );
//        new Group( path_database, members );
    }

}