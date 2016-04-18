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
 * Test file for testing various db operations
 */
class ExecutorTestDriver {

    public static void main(String[] args) {
        Path path_database = Paths.get(System.getProperty("user.dir")).resolve("testDB");
        System.out.println( "Database Directory: " + path_database.toString() );

        Database DB = Database.GetDB();

        int number_of_tests = 3;
        long averaged_duration, max, min, test_duration;

        ArrayList<Integer> number_of_tasks = new ArrayList<>();
        number_of_tasks.add( 1 );
        number_of_tasks.add( 2 );
        number_of_tasks.add( 4 );
        number_of_tasks.add( 8 );
        number_of_tasks.add( 16 );
        number_of_tasks.add( 32 );
        number_of_tasks.add( 128 );
        number_of_tasks.add( 256 );
        number_of_tasks.add( 512 );
        number_of_tasks.add( 1024 );
        number_of_tasks.add( 2048 );
        number_of_tasks.add( 4096 );
        number_of_tasks.add( 8192 );
        //number_of_tasks.add( 16384 );

        ArrayList<Integer> number_of_threads = new ArrayList<>();
//        number_of_threads.add( 1 );
//        number_of_threads.add( 2 );
//        number_of_threads.add( 4 );
//        number_of_threads.add( 8 );
//        number_of_threads.add( 16 );
//        number_of_threads.add( 32 );
        number_of_threads.add( 64 );
//        number_of_threads.add( 128 );
//        number_of_threads.add( 256 );
//        number_of_threads.add( 512 );


        ArrayList<String> types_of_lists = new ArrayList<>();
        types_of_lists.add( "Lazy" );
        types_of_lists.add( "LockFree" );

        /* Total number of processors or cores available to the JVM */
        int num_processors = Runtime.getRuntime().availableProcessors();
        System.out.println("Available processors (cores): " + num_processors);

        /* Total amount of free memory available to the JVM */
        System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());

        /* Total memory currently available to the JVM */
        System.out.println("Total memory available to JVM (bytes): " + Runtime.getRuntime().totalMemory());

        System.out.println("Running " + number_of_tests + " trials, throwing away max and min");

        System.out.println( "num_processors, number of tasks, list_type, pool size, average duration(ms)" );

        for (int numTasks : number_of_tasks) {
            for (String list_type : types_of_lists) {
                for (int nThreads : number_of_threads) {
                    averaged_duration = 0;
                    max = Long.MIN_VALUE;
                    min = Long.MAX_VALUE;
                    for (int loop = 0; loop < number_of_tests; loop++) {
                        DB.Initialize(path_database, list_type, true);
                        long startTime = System.nanoTime();

                        ExecutorService manager = Executors.newFixedThreadPool(nThreads);

                        // do all the first creations
                        for (int i = 0; i < numTasks; ++i) {
                            manager.submit(new dbResolverThrd(i, dbThrd.Op.CREATE, dbThrd.Col.CALENDAR, Arrays.asList("Ca" + i, "justin")));
                        }

                        manager.shutdown();
                        try {
                            manager.awaitTermination(3, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            manager.shutdownNow();
                            Thread.currentThread().interrupt();
                        }

                        // do a bunch of edits
                        manager = Executors.newFixedThreadPool(nThreads);
                        for (int i = 0; i < numTasks; ++i) {

                            manager.submit(new dbResolverThrd(i, dbThrd.Op.EDIT, dbThrd.Col.CALENDAR, new Integer(i), Arrays.asList(new MicroMap<>("title", "TEST"))));
                        }

                        manager.shutdown();
                        try {
                            manager.awaitTermination(3, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            manager.shutdownNow();
                            Thread.currentThread().interrupt();
                        }

                        // do a bunch of deletions
                        manager = Executors.newFixedThreadPool(nThreads);
                        for (int i = 0; i < numTasks; ++i) {
                            manager.submit(new dbResolverThrd(i, dbThrd.Op.DELETE, dbThrd.Col.CALENDAR, new Integer(i)));
                        }
                        manager.shutdown();
                        try {
                            manager.awaitTermination(3, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            manager.shutdownNow();
                            Thread.currentThread().interrupt();
                        }
                        long endTime = System.nanoTime();
                        test_duration = (endTime - startTime);
                        if (test_duration > max) {
                            max = test_duration;
                        }
                        if (test_duration < min) {
                            min = test_duration;
                        }
                        averaged_duration += test_duration;
                    } // end number_of_tests loop
                    averaged_duration = ( ( (averaged_duration - max - min) / (number_of_tests - 2) ) / 1000000 );
                    System.out.println(num_processors + ", " + numTasks + ", " + list_type + ", " + nThreads + ", " + averaged_duration);
                } // end nThreads loop
            } // end types of lists loop
        }

        

//        System.out.println( "------------------------------------------" );
//        members.add( "adrian" );
//        new Group( path_database, members );
    }

}