package main;

import Entities.File;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by justinmcbride on 3/23/16.
 */
public class main {

    public static void main(String[] args) {
        Path home = Paths.get(System.getProperty("user.home"));
        System.out.println( "Home: " + home.toString() );
//        IntegerFile blahFile = new IntegerFile( 17,Paths.get(System.getProperty("user.home"), "test.txt") );
//        blahFile.SetValue( 15, true );
        Path path_events = home.resolve( "events" );
        new Event( path_events );
        new Event( path_events );
        new Event( path_events );
    }

}