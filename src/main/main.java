package main;

import Entities.File;

import java.nio.file.Paths;

/**
 * Created by justinmcbride on 3/23/16.
 */
public class main {

    public static void main(String[] args) {
        File<Integer> blahFile = new File<>( 17, Paths.get(System.getProperty("user.home"), "test.txt") );
        blahFile.CommitChange( 16 );
    }

}