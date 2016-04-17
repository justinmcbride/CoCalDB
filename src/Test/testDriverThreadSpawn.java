package Test;

import main.Entities.Database;
import main.Entities.dbResolverThrd;
import main.Entities.dbThrd;
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
class testDriverThreadSpawn {

    public static void main(String[] args) {
        Path path_database = Paths.get(System.getProperty("user.dir")).resolve("testDB");
        System.out.println("Database Directory: " + path_database.toString());

        Database DB = Database.GetDB();

        int number_of_tests = 1;
        int number_of_tasks = 16;
        int i, k;

        ArrayList<Integer> thread_Limit = new ArrayList<>();
        thread_Limit.add(1);
        thread_Limit.add(2);
        thread_Limit.add(4);
        thread_Limit.add(8);
        thread_Limit.add(16);

        ArrayList<String> types_of_lists = new ArrayList<>();
        types_of_lists.add("Lazy");
        types_of_lists.add("LockFree");

        ArrayList<dbResolverThrd> threads = zeroedList(32);

        /* Total number of processors or cores available to the JVM */
        System.out.println("Available processors (cores): " + Runtime.getRuntime().availableProcessors());

        /* Total amount of free memory available to the JVM */
        System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());

        /* Total memory currently available to the JVM */
        System.out.println("Total memory available to JVM (bytes): " + Runtime.getRuntime().totalMemory());

        System.out.println("list_type, number_of_threads, duration");
        for (String list_type : types_of_lists) {
            for (int threadLimit : thread_Limit) {
                for (int loop = 0; loop < number_of_tests; loop++) {
                    DB.Initialize(path_database, list_type, true);
                    long startTime = System.nanoTime();

                    k = 0; // do all the first creations
                    while (k < number_of_tasks) {
                        for (i = 0; i < threadLimit && (k < number_of_tasks); ++i) {
                            threads.set(i,new dbResolverThrd(i, dbThrd.Op.CREATE, dbThrd.Col.CALENDAR, Arrays.asList("Ca" + k++, "justin")));
                            threads.get(i).start();
                        }
                        threadsJoin(threads, threadLimit);
                    }

                    k = 0; // do a bunch of edits
                    while (k < number_of_tasks) {
                        for (i = 0; i < threadLimit && (k < number_of_tasks); ++i) {
                            threads.set(i,new dbResolverThrd(i, dbThrd.Op.EDIT, dbThrd.Col.CALENDAR, k++, Arrays.asList(new MicroMap<>("title", "TEST"))));
                            threads.get(i).start();
                        }
                        threadsJoin(threads, threadLimit);
                    }

                    k = 0; // do a bunch of deletions
                    while (k < number_of_tasks) {
                        for (i = 0; i < threadLimit && (k < number_of_tasks); ++i) {
                            threads.set(i,new dbResolverThrd(i, dbThrd.Op.DELETE, dbThrd.Col.CALENDAR, k++));
                            threads.get(i).start();
                        }
                        threadsJoin(threads, threadLimit);
                    }
                    long endTime = System.nanoTime();
                    long duration = endTime - startTime;
                    System.out.println(list_type + ", " + threadLimit + ", took " + duration + " ns");
                    //System.out.println( DB.m_collection_calendars );
                } // end nThreads
            } // end number_of_tests loop
        } // end types of lists loop
    }

    static void threadsJoin(ArrayList<dbResolverThrd> threads, int nThreads) {
        for (int j = 0; j < nThreads; ++j) {
            try {
                threads.get(j).join();
            } catch (InterruptedException e) {
                System.err.println(e);

            }
        }

    }

    static ArrayList<dbResolverThrd> zeroedList( int n){ //sets list of size n values to null
        ArrayList<dbResolverThrd> nullList = new ArrayList<>(n);
        for (int i = 0; i < n; ++i ){
            nullList.add(null);
        }
        return nullList;
    }
}

//        System.out.println( "------------------------------------------" );
//        members.add( "adrian" );
//        new Group( path_database, members );
