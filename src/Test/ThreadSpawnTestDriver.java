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
class ThreadSpawnTestDriver {

    public static void main(String[] args) {
        Path path_database = Paths.get(System.getProperty("user.dir")).resolve("testDB");
        System.out.println("Database Directory: " + path_database.toString());

        Database DB = Database.GetDB();

        int number_of_tests = 3;
        int i, k;
        long averaged_duration, max, min, test_duration;

        ArrayList<Integer> number_of_tasks = new ArrayList<>();
        number_of_tasks.add(1);
        number_of_tasks.add(2);
        number_of_tasks.add(4);
        number_of_tasks.add(8);
        number_of_tasks.add(16);
        number_of_tasks.add(32);
        number_of_tasks.add(128);
        number_of_tasks.add(256);
        number_of_tasks.add(512);
        number_of_tasks.add(1024);
        number_of_tasks.add(2048);
        number_of_tasks.add(4096);
        //number_of_tasks.add( 1 );

        ArrayList<Integer> thread_Limit = new ArrayList<>();
//        thread_Limit.add(1);
//        thread_Limit.add(2);
//        thread_Limit.add(4);
//        thread_Limit.add(8);
//        thread_Limit.add(16);
//        thread_Limit.add(32);
        thread_Limit.add(64);
//        thread_Limit.add(128);
//        thread_Limit.add(256);
//        thread_Limit.add(512);

        ArrayList<String> types_of_lists = new ArrayList<>();
        types_of_lists.add("Lazy");
        types_of_lists.add("LockFree");

        ArrayList<dbResolverThrd> threads = zeroedList(number_of_tasks.get(number_of_tasks.size() - 1));

        /* Total number of processors or cores available to the JVM */
        int num_processors = Runtime.getRuntime().availableProcessors();
        System.out.println("Available processors (cores): " + num_processors);

        /* Total amount of free memory available to the JVM */
        System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());

        /* Total memory currently available to the JVM */
        System.out.println("Total memory available to JVM (bytes): " + Runtime.getRuntime().totalMemory());

        System.out.println("Running " + number_of_tests + " trials, throwing away max and min");

        System.out.println("num_processors, number of tasks, list_type, thread limit, average duration(ms)");
        for (int numTasks : number_of_tasks) {
            for (String list_type : types_of_lists) {
                for (int threadLimit : thread_Limit) {
                    averaged_duration = 0;
                    max = Long.MIN_VALUE;
                    min = Long.MAX_VALUE;
                    for (int loop = 0; loop < number_of_tests; loop++) {
                        DB.Initialize(path_database, list_type, true);
                        long startTime = System.nanoTime();

                        k = 0; // do all the first creations
                        while (k < numTasks) {
                            for (i = 0; i < threadLimit && (k < numTasks); ++i) {
                                threads.set(i, new dbResolverThrd(i, dbThrd.Op.CREATE, dbThrd.Col.CALENDAR, Arrays.asList("Ca" + k++, "justin")));
                                threads.get(i).start();
                            }
                            threadsJoin(threads, threadLimit, numTasks);
                        }

                        k = 0; // do a bunch of edits
                        while (k < numTasks) {
                            for (i = 0; i < threadLimit && (k < numTasks); ++i) {
                                threads.set(i, new dbResolverThrd(i, dbThrd.Op.EDIT, dbThrd.Col.CALENDAR, k++, Arrays.asList(new MicroMap<>("title", "TEST"))));
                                threads.get(i).start();
                            }
                            threadsJoin(threads, threadLimit, numTasks);
                        }

                        k = 0; // do a bunch of deletions
                        while (k < numTasks) {
                            for (i = 0; i < threadLimit && (k < numTasks); ++i) {
                                threads.set(i, new dbResolverThrd(i, dbThrd.Op.DELETE, dbThrd.Col.CALENDAR, k++));
                                threads.get(i).start();
                            }
                            threadsJoin(threads, threadLimit, numTasks);
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
                    System.out.println(num_processors + ", " + numTasks + ", " + list_type + ", " + threadLimit + ", " + averaged_duration);
                } // end nThreads loop
            } // end types of lists loop
        } //end number of tasks loop

    }

    static void threadsJoin(ArrayList<dbResolverThrd> threads, int nThreads, int numTasks) {
        for (int j = 0; (j < nThreads) && (j < numTasks); ++j) {
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
